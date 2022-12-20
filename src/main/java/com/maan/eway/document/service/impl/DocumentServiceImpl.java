package com.maan.eway.document.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.maan.eway.bean.CoverDocumentMaster;
import com.maan.eway.bean.CoverDocumentUploadDetails;
import com.maan.eway.bean.CoverDocumentUploadDetailsId;
import com.maan.eway.bean.ProductMaster;
import com.maan.eway.document.req.DocTypeDropDownReq;
import com.maan.eway.document.req.DocumentDeleteReq;
import com.maan.eway.document.req.DocumentUploadReq;
import com.maan.eway.document.req.FilePathReq;
import com.maan.eway.document.req.GetDocListReq;
import com.maan.eway.document.res.ClientDocListRes;
import com.maan.eway.document.res.DocCategoryRes;
import com.maan.eway.document.res.DocTypeRes;
import com.maan.eway.document.res.FilePathRes;
import com.maan.eway.document.service.DocumentService;
import com.maan.eway.error.Error;
import com.maan.eway.master.req.CoverDocumentMasterGetReq;
import com.maan.eway.master.res.CoverDocumentMasterGetRes;
import com.maan.eway.repository.CoverDocumentMasterRepository;
import com.maan.eway.repository.CoverDocumentUploadDetailsRepository;
import com.maan.eway.res.CommonRes;
import com.maan.eway.res.SuccessRes;



@Service
@Transactional
public class DocumentServiceImpl implements DocumentService{
	
	private Logger log = LogManager.getLogger(DocumentServiceImpl.class);

	@Value("${file.directoryPath}")
	private String directoryPath;
	
	@Value("${file.compressedImg}")
	private String compressedImg;
	
	@PersistenceContext
	private EntityManager em;
	
	
	@Autowired
	private CoverDocumentUploadDetailsRepository  documentuploaddetailsrepository;
	@Autowired
	private CoverDocumentMasterRepository docRepo;
	
	public static  Map<String,String> ALLOWED_CONTENTTYPE;
	  static {
		  ALLOWED_CONTENTTYPE = new HashMap<String, String>();
		  //Image
		  ALLOWED_CONTENTTYPE.put(".bmp","image/bmp");
		  ALLOWED_CONTENTTYPE.put(".jpg","image/jpeg");
		  ALLOWED_CONTENTTYPE.put(".png","image/png");
		  ALLOWED_CONTENTTYPE.put(".jpeg","image/jpeg");
		  //Doc
		  ALLOWED_CONTENTTYPE.put(".doc","application/msword");
		  ALLOWED_CONTENTTYPE.put(".docx","application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		  ALLOWED_CONTENTTYPE.put(".pdf","application/pdf");		  
		  ALLOWED_CONTENTTYPE.put(".xlsx","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		  ALLOWED_CONTENTTYPE.put(".xls","application/vnd.ms-excel");
		  //Vid
		  ALLOWED_CONTENTTYPE.put(".avi","video/x-msvideo");
		  ALLOWED_CONTENTTYPE.put(".3gp","video/3gpp"); 
		  ALLOWED_CONTENTTYPE.put(".mpeg","video/mpeg");
		  ALLOWED_CONTENTTYPE.put(".mp4","video/mp4");
		  ALLOWED_CONTENTTYPE.put(".m4v","video/m4v");
		  ALLOWED_CONTENTTYPE.put(".flv","video/x-flv");
		  ALLOWED_CONTENTTYPE.put(".mp4","video/mp4");
		  ALLOWED_CONTENTTYPE.put(".m3u8","application/x-mpegURL");
		  ALLOWED_CONTENTTYPE.put(".ts","video/MP2T");
		  ALLOWED_CONTENTTYPE.put(".3gp","video/3gpp");
		  ALLOWED_CONTENTTYPE.put(".mov","video/quicktime");
		  ALLOWED_CONTENTTYPE.put(".avi","video/x-msvideo");
		  ALLOWED_CONTENTTYPE.put(".wmv","video/x-ms-wmv");

	  }
	//Upload
	@Override
	public List<Error> docvalidation(DocumentUploadReq req,MultipartFile file) {

		List<Error> errorList = new ArrayList<Error>();
		
		log.info(req);


		/*		if(StringUtils.isNotBlank(req.getUploadedBy()) ) {
					boolean containsValue = ALLOWED_CONTENTTYPE.containsValue(file.getContentType());
				if (!containsValue) {
					errorList.add(new Error("01", "ReferenceNo", file.getContentType() + " is Not Allowed for "
							+ req.getOrginalFileName()));
				}
*/
				long fileSizeInBytes = file.getSize();
				double size_kb = fileSizeInBytes / 1024;
				double size_mb = size_kb / 1024;

				if (size_mb > 25) {
					errorList.add(new Error("01", "FileSize", "File Size Must be 25Mb Current file value is" + size_mb
							+ "MB for " + req.getOriginalFileName()));
				}
				
				CoverDocumentUploadDetails  data = documentuploaddetailsrepository.findByQuoteNoAndSectionIdAndIdAndDocumentId(req.getQuoteNo() ,Integer.valueOf(req.getSectionId())  ,Integer.valueOf(req.getId()) , Integer.valueOf(req.getDocumentId()));
				
				if(data!=null ) {
					errorList.add(new Error("01", "Document Type", "This Document Type Already Uploaded" ));
					
				}
			
		return errorList;

	}

	//Detete
	@Override
	public CommonRes fileupload(DocumentUploadReq DocumentUploadDetails,MultipartFile file) {
		
		CommonRes res = new CommonRes();
		
		try {
				CoverDocumentUploadDetails doc = new CoverDocumentUploadDetails();
				
				Random random = new Random();
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				
				String newfilename  = "";
				String newfilename1 = "";
				//OrginalFile
				Path destination = Paths.get(directoryPath) ;
				newfilename= random.nextInt(100) + timestamp.toString().replace(":", "T").replace(" ", "S").replace("-", "H").replace(".", "D") +"."+FilenameUtils.getExtension(file.getOriginalFilename());
				Files.copy(file.getInputStream(),destination.resolve(newfilename));
				
				Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());
				//BackupFile
				Path destination1 = Paths.get(compressedImg) ; 
				newfilename1= random.nextInt(100) + timestamp1.toString().replace(":", "T").replace(" ", "S").replace("-", "H").replace(".", "D") +"."+FilenameUtils.getExtension(file.getOriginalFilename());
				Files.copy(file.getInputStream(),destination1.resolve(newfilename1));
			
				doc.setFileName(newfilename);
				doc.setFilePathOrginal(directoryPath+newfilename);
				doc.setFilePathBackup(compressedImg+newfilename1);
				doc.setOrginalFileName(file.getOriginalFilename());
				doc.setUploadedTime(new Date());
				//doc.setUploadedBy(DocumentUploadDetails.getUploadedBy());
				
				CoverDocumentMaster docDetails = 	getByDocumentId(DocumentUploadDetails.getCompanyId() ,DocumentUploadDetails.getProductId() , DocumentUploadDetails.getSectionId() , DocumentUploadDetails.getDocumentId() );
				
				doc.setRequestReferenceNo(DocumentUploadDetails.getRequestReferenceNo());
				doc.setProductId(Integer.valueOf(DocumentUploadDetails.getProductId()));
				doc.setSectionId(Integer.valueOf(DocumentUploadDetails.getSectionId()));
				doc.setCoverId("99999");
				doc.setDocumentId(Integer.valueOf(DocumentUploadDetails.getDocumentId()));
				doc.setCreatedBy(DocumentUploadDetails.getCreatedBy());
				doc.setDocumentType(docDetails.getDocumentType());
				doc.setDocumentTypeDesc(docDetails.getDocumentTypeDesc());
				doc.setDocApplicable(docDetails.getDocApplicable());
				doc.setDocumentDesc(docDetails.getDocumentName());
				doc.setDocApplicableId(docDetails.getDocApplicableId()==null?null:docDetails.getDocApplicableId().toString());
				doc.setCompanyId(DocumentUploadDetails.getCompanyId());
				doc.setEntryDate(new Date());
				doc.setUploadedTime(new Date());
				doc.setRequestedBy("");
				doc.setUploadedBy("");
				doc.setQuoteNo(DocumentUploadDetails.getQuoteNo());
				doc.setId(Integer.valueOf(DocumentUploadDetails.getId()));;
				
				
				//RefRunning number
				if(StringUtils.isBlank(DocumentUploadDetails.getDocumentReferenceNo())   ) {
					Random rnd = new Random();
					int number = rnd.nextInt(10000);
					String randomNo = String.format("%04d", number);
					doc.setDocumentReferenceNo(Integer.valueOf(randomNo));
				} else {
					doc.setDocumentReferenceNo(Integer.valueOf(DocumentUploadDetails.getDocumentReferenceNo()));
				}
				
				documentuploaddetailsrepository.save(doc);
			
			res.setCommonResponse("File Upload Sucessfully");
			res.setIsError(false);						
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public CoverDocumentMaster getByDocumentId(String insId, String productId ,String sectionId ,String documentId ) {
		CoverDocumentMaster res = new CoverDocumentMaster();
		
		try {
			Date today = new Date();
			Calendar cal = new GregorianCalendar();
			cal.setTime(today);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 1);
			today = cal.getTime();
			cal.set(Calendar.HOUR_OF_DAY, 1);
			cal.set(Calendar.MINUTE, 1);
			Date todayEnd = cal.getTime();
			// Criteria
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<CoverDocumentMaster> query = cb.createQuery(CoverDocumentMaster.class);
			List<CoverDocumentMaster> list = new ArrayList<CoverDocumentMaster>();

			// Find All
			Root<CoverDocumentMaster> c = query.from(CoverDocumentMaster.class);

			// Select
			query.select(c);

			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(c.get("documentName")));

			// Effective Date Max Filter
			Subquery<Long> effectiveDate = query.subquery(Long.class);
			Root<CoverDocumentMaster> ocpm1 = effectiveDate.from(CoverDocumentMaster.class);
			effectiveDate.select(cb.max(ocpm1.get("effectiveDateStart")));
			javax.persistence.criteria.Predicate a1 = cb.equal(c.get("companyId"), ocpm1.get("companyId"));
			javax.persistence.criteria.Predicate a2 = cb.equal(c.get("productId"), ocpm1.get("productId"));
			javax.persistence.criteria.Predicate a3 = cb.equal(c.get("sectionId"), ocpm1.get("sectionId"));
			javax.persistence.criteria.Predicate a4 = cb.equal(c.get("coverId"), ocpm1.get("coverId"));
			javax.persistence.criteria.Predicate a5 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
			Predicate a11 = cb.equal(ocpm1.get("documentId") , c.get("documentId"));
			effectiveDate.where(a1, a2, a3, a4, a5,a11);
			// Effective Date End
			Subquery<Long> effectiveDate2 = query.subquery(Long.class);
			Root<CoverDocumentMaster> ocpm2 = effectiveDate2.from(CoverDocumentMaster.class);
			effectiveDate2.select(cb.max(ocpm2.get("effectiveDateEnd")));
			Predicate a6 = cb.equal(c.get("sectionId"), ocpm2.get("sectionId"));
			Predicate a7 = cb.equal(c.get("coverId"), ocpm2.get("coverId"));
			Predicate a8 = cb.equal(c.get("companyId"), ocpm2.get("companyId") );
			Predicate a9 = cb.equal(c.get("productId"), ocpm2.get("productId") );
			Predicate a10 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
			Predicate a12 = cb.equal(ocpm2.get("documentId") , c.get("documentId"));
			effectiveDate2.where(a6,a7,a8,a9,a10,a12);
					
			// Where
			javax.persistence.criteria.Predicate n1 = cb.equal(c.get("status"), "Y");
			javax.persistence.criteria.Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
			javax.persistence.criteria.Predicate n3 = cb.equal(c.get("companyId"), insId);
			javax.persistence.criteria.Predicate n4 = cb.equal(c.get("productId"), productId);
			javax.persistence.criteria.Predicate n5 = cb.equal(c.get("sectionId"), sectionId);
			javax.persistence.criteria.Predicate n6 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
			javax.persistence.criteria.Predicate n7 = cb.equal(c.get("documentId"), documentId);
			query.where(n1, n2, n3, n4, n5,n6,n7).orderBy(orderList);

			// Get Result
			TypedQuery<CoverDocumentMaster> result = em.createQuery(query);
			list = result.getResultList();
			res = list.get(0);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		return res;
	}

	@Override
	public CommonRes deleteFile(DocumentDeleteReq req) {

		CommonRes commonRes = new CommonRes();
		SuccessRes res = new SuccessRes(); 
		try {
			
			documentuploaddetailsrepository.deleteByQuoteNoAndIdAndDocumentIdAndDocumentReferenceNo(req.getQuoteNo() ,Integer.valueOf(req.getId()),
					Integer.valueOf(req.getDocumentId()) , Integer.valueOf(req.getDocumentReferenceNo()));
			
			res.setResponse("Document Deleted Sucessfully");
			res.setSuccessId(req.getDocumentId());
			
			commonRes.setCommonResponse(res);
			commonRes.setIsError(false);
			commonRes.setErrorMessage(Collections.emptyList());
			commonRes.setMessage("File Upload Faild");
			
		} catch (Exception e) {
			e.printStackTrace();
			List<Error> errors = new ArrayList<Error>();
			errors.add(new Error("01","Failed","Document Deleted Failed"));
			commonRes.setCommonResponse(res);
			commonRes.setIsError(true);
			commonRes.setErrorMessage(errors);
			commonRes.setMessage("File Upload Faild");
		}
		return commonRes;
	}
	
	// Drop|Down
		@Override
		public List<DocTypeRes> getDocTypeDropDowns(DocTypeDropDownReq req) {
			List<DocTypeRes> resList = new ArrayList<DocTypeRes>();
			try {
				Date today  = new Date();
				Calendar cal = new GregorianCalendar(); 
				cal.setTime(today);
				cal.set(Calendar.HOUR_OF_DAY, 23);cal.set(Calendar.MINUTE, 1);
				today   = cal.getTime();
				cal.set(Calendar.HOUR_OF_DAY, 1);cal.set(Calendar.MINUTE, 1);
				Date todayEnd   = cal.getTime();
				// Criteria
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<CoverDocumentMaster> query = cb.createQuery(CoverDocumentMaster.class);
				List<CoverDocumentMaster> docTypeList = new ArrayList<CoverDocumentMaster>();

				Root<CoverDocumentMaster> login = query.from(CoverDocumentMaster.class);

				// Order By
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(login.get("documentId")));
				
				// Effective Date Max Filter
				Subquery<Long> effectiveDate = query.subquery(Long.class);
				Root<CoverDocumentMaster> ocpm1 = effectiveDate.from(CoverDocumentMaster.class);
				effectiveDate.select(cb.max(ocpm1.get("effectiveDateStart")));
				Predicate a1 = cb.equal(login.get("productId"),ocpm1.get("productId") );
				//Predicate a2 = cb.equal(login.get("coverId"),ocpm1.get("coverId") );
				Predicate a3 = cb.equal(login.get("sectionId"),ocpm1.get("sectionId") );
				Predicate a4 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
				effectiveDate.where(a1,a3,a4);
				
				// Effective Date Max Filter
				Subquery<Long> effectiveDate2 = query.subquery(Long.class);
				Root<CoverDocumentMaster> ocpm2 = effectiveDate2.from(CoverDocumentMaster.class);
				effectiveDate2.select(cb.max(ocpm2.get("effectiveDateEnd")));
				Predicate a5 = cb.equal(login.get("productId"),ocpm2.get("productId") );
			//	Predicate a6 = cb.equal(login.get("coverId"),ocpm2.get("coverId") );
				Predicate a7 = cb.equal(login.get("sectionId"),ocpm2.get("sectionId") );
				Predicate a8 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
				effectiveDate2.where(a5,a7,a8);
				
			    // Where	
				Predicate p1 = cb.equal(login.get("status"), "Y");
				Predicate p2 = null ;
				Predicate p3 = cb.equal(login.get("effectiveDateStart"), effectiveDate);
				Predicate p4 = cb.equal(login.get("effectiveDateEnd"), effectiveDate2);
			
				if(  req.getDocApplicable() !=null && req.getDocApplicable().equalsIgnoreCase("Client_Info")  ) {
					String docApplicable = "PERSONAL_DETAILS";
					p2 = cb.like(login.get("docCons"), "%" + docApplicable + "%" );
				} else if(req.getDocApplicable() !=null &&  req.getDocApplicable().equalsIgnoreCase("Lead_Info")  ) {
					String docApplicable = "QUOTATION_DETAILS";
					p2 = cb.like(login.get("docCons"), "%" + docApplicable + "%" );
				} else {
					p2 = cb.equal(login.get("docApplicableId"),req.getDocApplicableId() );
				}
				
				query.select(login).where(p1, p2,p3,p4).orderBy(orderList) ;

				TypedQuery<CoverDocumentMaster> result = em.createQuery(query);
				docTypeList = result.getResultList();

				for (CoverDocumentMaster data : docTypeList ) {

					DocTypeRes res = new DocTypeRes();
					
					res.setCode(data.getDocumentId().toString());
					res.setCodeDesc(data.getDocumentDesc());
					resList.add(res);
				}

			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception is ---> " + e.getMessage());
			}
			return resList;
		}
	

	private static <T> java.util.function.Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
	    Map<Object, Boolean> seen = new ConcurrentHashMap<>();
	    return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
		// Get Doc List
		@Override
		public List<ClientDocListRes> getTotalDocList(GetDocListReq req) {
			DozerBeanMapper dozerMapper = new DozerBeanMapper(); 
			List<ClientDocListRes> totalDocResList = new ArrayList<ClientDocListRes>();

			try {
				List<CoverDocumentUploadDetails> list = documentuploaddetailsrepository.findByQuoteNoAndIdAndDocumentTypeOrderByEntryDateDesc(req.getQuoteNo() , 
						Integer.valueOf(req.getId()) ,req.getDocumentType() )  ;

				for (CoverDocumentUploadDetails data : list) {

					ClientDocListRes res = dozerMapper.map(data, ClientDocListRes.class);
					res.setFilename(data.getFileName());
					res.setDocDesc(data.getDocumentDesc());
					totalDocResList.add(res);
				}

			} catch (Exception e) {
				e.printStackTrace();
				log.info(e.getMessage());
				return null;
			}
			return totalDocResList;
		}


		@Override
		public FilePathRes getFilePath(FilePathReq req) {
			FilePathRes res = new FilePathRes();
			DozerBeanMapper dozerMapper = new DozerBeanMapper(); 
			try {
				CoverDocumentUploadDetails getFile = documentuploaddetailsrepository.findByQuoteNoAndIdAndDocumentIdAndDocumentReferenceNo(req.getQuoteNo() ,Integer.valueOf(req.getId()),
														Integer.valueOf(req.getDocumentId()) , Integer.valueOf(req.getDocumentReferenceNo()));
				res = dozerMapper.map(getFile, FilePathRes.class);
				res.setDocumentId(getFile.getDocumentId() == null ? "" : getFile.getDocumentId().toString());
				res.setDocApplicable(getFile.getDocApplicable());
				res.setDocumentDesc(getFile.getDocumentDesc());
				res.setFilename(getFile.getFileName());
				res.setFilepathname(getFile.getFilePathOrginal());
				if (StringUtils.isNotBlank(res.getFilepathname()) && new File(res.getFilepathname()).exists()) {
					res.setImgurl(new GetFileFromPath(res.getFilepathname()).call().getImgUrl());
					res.setDocumentReferenceNo(getFile.getDocumentReferenceNo().toString());
				} else
					System.out.println("File is Not found!!" + res.getFilepathname());
			} catch (Exception e) {
				e.printStackTrace();
				// Log.info("Exception Is --->" + e.getMessage());
				return null;
			}
			return res;
		}
		
		@Override
		public FilePathRes getCompressedImages(FilePathReq req) {
			FilePathRes res = new FilePathRes();
			DozerBeanMapper dozerMapper = new DozerBeanMapper(); 
			try {
				CoverDocumentUploadDetails getFile = documentuploaddetailsrepository.findByQuoteNoAndIdAndDocumentIdAndDocumentReferenceNo(req.getQuoteNo() ,Integer.valueOf(req.getId()),
						Integer.valueOf(req.getDocumentId()) , Integer.valueOf(req.getDocumentReferenceNo()));
				
				res = dozerMapper.map(getFile, FilePathRes.class);
				res.setDocumentId(getFile.getDocumentId()==null?"":getFile.getDocumentId().toString());
				res.setDocApplicable(getFile.getDocApplicable());
				res.setDocumentDesc(getFile.getDocumentDesc());
				res.setFilename(getFile.getFileName());
				res.setFilepathname(getFile.getFilePathBackup());
				res.setCommonfilepath(getFile.getFilePathBackup() );
			//	res.setVehChassisNo(getFile.getClaimNo());
				if(StringUtils.isNotBlank(res.getCommonfilepath()) && new File(res.getCommonfilepath()).exists()) {
							res.setImgurl(new GetFileFromPath(res.getCommonfilepath()).call().getImgUrl());
							res.setDocumentReferenceNo(getFile.getDocumentReferenceNo().toString());
					}else
						 System.out.println("File is Not found!!"+res.getFilepathname());
				} catch (Exception e) {
					e.printStackTrace();
					//Log.info("Exception Is --->" + e.getMessage());
					return null;
				}
			return res;
		}

}
	

