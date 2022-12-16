package com.maan.eway.admin.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.eway.admin.service.AdminDropDownService;
import com.maan.eway.bean.ListItemValue;
import com.maan.eway.bean.LoginMaster;
import com.maan.eway.bean.ProductMaster;
import com.maan.eway.common.req.GetTableDropDownReq;
import com.maan.eway.common.req.LovDropDownReq;
import com.maan.eway.repository.ListItemValueRepository;
import com.maan.eway.repository.LoginMasterRepository;
import com.maan.eway.repository.ProductMasterRepository;
import com.maan.eway.req.SubUserTypeReq;
import com.maan.eway.res.ColummnDropRes;
import com.maan.eway.res.DropDownRes;
import com.maan.eway.res.SubUserTypeDropDownRes;

@Service
public class AdminDropDownServiceImpl  implements AdminDropDownService{


	private Logger log = LogManager.getLogger(AdminDropDownServiceImpl.class);

	
	@Autowired
	private ListItemValueRepository listRepo;
	
	@Autowired
	private LoginMasterRepository loginRepo ;
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private ProductMasterRepository productrepo ;
	
		@Override
		public List<DropDownRes> getUserType(LovDropDownReq req) {
			List<DropDownRes> resList = new ArrayList<DropDownRes>();
			try {
			//	List<ListItemValue> getList = listRepo.findByItemTypeAndStatusOrderByItemCodeAsc("USER_TYPE", "Y");
				String itemType = "USER_TYPE" ;
				List<ListItemValue> list  = getListItem(req , itemType);
				for (ListItemValue data : list) {
					DropDownRes res = new DropDownRes();
					res.setCode(data.getItemCode());
					res.setCodeDesc(data.getItemValue());
					res.setStatus(data.getStatus());
					resList.add(res);
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception is ---> " + e.getMessage());
				return null;
			}
			return resList;
		}
		
		@Override
		public List<ColummnDropRes> getTableDetails(GetTableDropDownReq req) {
			List<ColummnDropRes> resList = new ArrayList<ColummnDropRes>();
			try {
				//String tableName = "Eservice_Customer_Details" ;
				//List<String> removerUnderScore = new ArrayList<>(Arrays.asList(tableName.split("_")) ) ;
				
				/*Object entityName =null;
				for (String ent : removerUnderScore) {
					String lowerCase = ent.toLowerCase() ;
					String firstLetterCaps =  lowerCase.substring(0, 1).toUpperCase() + lowerCase.substring(1) ;
					entityName = entityName==null ? firstLetterCaps  :entityName +  firstLetterCaps ; 
					
				}*/
				if(StringUtils.isNotBlank(req.getProductId()) && StringUtils.isBlank(req.getTableName())) {
					String tablename="MsVehicleDetails";
					List<ProductMaster> idfortable = productrepo.findByProductIdOrderByEffectiveDateStartDesc(Integer.parseInt(req.getProductId()));
					String oneProduct = idfortable.get(0).getMotorYn();
					if(oneProduct.equals("M"))
						tablename="MsVehicleDetails";
					else if(oneProduct.equals("H"))
						tablename="MsHumanDetails";
					else if(oneProduct.equals("A"))
						tablename="MsAssetDetails";
						
						req.setTableName(tablename);
				}  
				
			String entityName = "com.maan.eway.bean."+req.getTableName();//entityName + ".class" ;
				 Class<?> forName = Class.forName(entityName);//forName(entityName);
				

			//	Class table = (Class) entityName ;
				
				Field[] members = forName.getDeclaredFields();
				
				        for(Field member:members){
				        	if(! member.getName().equalsIgnoreCase("serialVersionUID") ) {
				        		System.out.println(member.getName());
				        		String output = member.getName().substring(0, 1).toUpperCase() + member.getName().substring(1);
			        			String field =output.replaceAll("(.)([A-Z])", "$1_$2");
			        			System.out.println(field);
			        			String display =output.replaceAll("(.)([A-Z])", "$1 $2");
				        			System.out.println(display);
				        			ColummnDropRes res = new ColummnDropRes();
				    				res.setColumnName(field);
				    				res.setDispalyName(display);
				    				res.setFieldName( member.getName());
				    				resList.add(res);
				        			    
				        	//	customerReferenceNo
				        	//	Customer Reference No
				        	//	Customer_Reference_No
				        	}
//				            System.out.println(member.getClass().getSimpleName());
//				            System.out.println(member.getClass().getCanonicalName());
//				            System.out.println(member.getClass().getTypeName());
//				            System.out.println(member.getClass().getComponentType());
//				            System.out.println(member.getClass().getModifiers());
//				            System.out.println(member.getClass().getAnnotations());
				        }			
				
				
			/*	List<ListItemValue> getList = listRepo.findByItemTypeAndStatusOrderByItemCodeAsc("FUEL_TYPE", "Y");

				for (ListItemValue data : getList) {
					DropDownRes res = new DropDownRes();
					res.setCode(data.getItemCode());
					res.setCodeDesc(data.getItemValue());
					res.setStatus(data.getStatus());
					resList.add(res);
				} */
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception is ---> " + e.getMessage());
				return null;
			}
			return resList;
		}

		@Override
		public List<SubUserTypeDropDownRes> getSubUserType(SubUserTypeReq req) {
			List<SubUserTypeDropDownRes> resList = new ArrayList<SubUserTypeDropDownRes>();
			try {
			//	List<ListItemValue> getList = listRepo.findByItemTypeAndStatusOrderByParam2Asc(req.getUserType(), "Y");
				String itemType = req.getUserType() ;
				LovDropDownReq req2 = new LovDropDownReq();
				req2.setBranchCode(req.getBranchCode());
				req2.setInsuranceId(req.getInsuranceId());
				List<ListItemValue> list  = getListItem(req2 , itemType);
				
				LoginMaster loginData = null ;
				if(StringUtils.isNotBlank(req.getLoginId()) ) {
					loginData = loginRepo.findByLoginId(req.getLoginId()) ;
				}
				 
				
				for (ListItemValue data : list) {
					SubUserTypeDropDownRes res = new SubUserTypeDropDownRes();
					if( loginData != null ) {
						
						// Issuer
						if (loginData.getUserType().equalsIgnoreCase("Issuer")  ) {
							
							if(  loginData.getSubUserType().equalsIgnoreCase("both")  &&  (data.getItemValue().equalsIgnoreCase("low") || data.getItemValue().equalsIgnoreCase("high")))  {
								res.setCode(data.getItemCode());
								res.setCodeDesc(data.getItemValue());
								res.setDisplayName(data.getParam1());
								res.setStatus(data.getStatus());
								resList.add(res);
							} else  {
								if(loginData.getSubUserType().equalsIgnoreCase(data.getItemValue()) ) {
									res.setCode(data.getItemCode());
									res.setCodeDesc(data.getItemValue());
									res.setDisplayName(data.getParam1());
									res.setStatus(data.getStatus());
									resList.add(res);
								}
							}
						}  else  {
							if(loginData.getSubUserType().equalsIgnoreCase(data.getItemValue()) ) {
								res.setCode(data.getItemCode());
								res.setCodeDesc(data.getItemValue());
								res.setDisplayName(data.getParam1());
								res.setStatus(data.getStatus());
								resList.add(res);
							}
						}
					} else {
						res.setCode(data.getItemCode());
						res.setCodeDesc(data.getItemValue());
						res.setDisplayName(data.getParam1());
						res.setStatus(data.getStatus());
						resList.add(res);
					}
					
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception is ---> " + e.getMessage());
				return null;
			}
			return resList;
		}

	
	
		@Override
		public List<DropDownRes> getProductIcons(LovDropDownReq req) {
			List<DropDownRes> resList = new ArrayList<DropDownRes>();
			try {
			//	List<ListItemValue> getList = listRepo.findByItemTypeAndStatusOrderByItemCodeAsc("PRODUCT_ICONS", "Y");
				String itemType = "PRODUCT_ICONS" ;
				List<ListItemValue> list  = getListItem(req , itemType);
				for (ListItemValue data : list) {
					DropDownRes res = new DropDownRes();
					res.setCode(data.getItemCode());
					res.setCodeDesc(data.getItemValue());
					res.setStatus(data.getStatus());
					resList.add(res);
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception is ---> " + e.getMessage());
				return null;
			}
			return resList;
		}

		@Override
		public List<DropDownRes> getCalcTypes(LovDropDownReq req) {
			List<DropDownRes> resList = new ArrayList<DropDownRes>();
			try {
				//List<ListItemValue> getList = listRepo.findByItemTypeAndStatusOrderByItemIdAsc("CALCULATION_TYPE", "Y");
				String itemType = "CALCULATION_TYPE" ;
				List<ListItemValue> list  = getListItem(req , itemType);
				for (ListItemValue data : list) {
					DropDownRes res = new DropDownRes();
					res.setCode(data.getItemCode());
					res.setCodeDesc(data.getItemValue());
					res.setStatus(data.getStatus());
					resList.add(res);
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception is ---> " + e.getMessage());
				return null;
			}
			return resList;
		}

		@Override
		public List<DropDownRes> getCoverageTypes(LovDropDownReq req) {
			List<DropDownRes> resList = new ArrayList<DropDownRes>();
			try {
			//	List<ListItemValue> getList = listRepo.findByItemTypeAndStatusOrderByItemCodeAsc("COVERAGE_TYPE", "Y");
				String itemType = "COVERAGE_TYPE" ;
				List<ListItemValue> list  = getListItem(req , itemType);
				for (ListItemValue data : list) {
					DropDownRes res = new DropDownRes();
					res.setCode(data.getItemCode());
					res.setCodeDesc(data.getItemValue());
					res.setStatus(data.getStatus());
					resList.add(res);
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception is ---> " + e.getMessage());
				return null;
			}
			return resList;
		}

		@Override
		public List<DropDownRes> getRangeParams(LovDropDownReq req) {
			List<DropDownRes> resList = new ArrayList<DropDownRes>();
			try {
			//	List<ListItemValue> getList = listRepo.findByItemTypeAndStatusOrderByItemCodeAsc("RANGE", "Y");
				String itemType = "RANGE" ;
				List<ListItemValue> list  = getListItem(req , itemType);
				for (ListItemValue data : list) {
					DropDownRes res = new DropDownRes();
					res.setCode(data.getItemCode());
					res.setCodeDesc(data.getItemValue());
					res.setStatus(data.getStatus());
					resList.add(res);
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception is ---> " + e.getMessage());
				return null;
			}
			return resList;
		}

		@Override
		public List<DropDownRes> getDiscreteParams(LovDropDownReq req) {
			List<DropDownRes> resList = new ArrayList<DropDownRes>();
			try {
			//	List<ListItemValue> getList = listRepo.findByItemTypeAndStatusOrderByItemCodeAsc("DISCRETE", "Y");
				String itemType = "DISCRETE" ;
				List<ListItemValue> list  = getListItem(req , itemType);
				for (ListItemValue data : list) {
					DropDownRes res = new DropDownRes();
					res.setCode(data.getItemCode());
					res.setCodeDesc(data.getItemValue());
					res.setStatus(data.getStatus());
					resList.add(res);
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception is ---> " + e.getMessage());
				return null;
			}
			return resList;
		}

	
		@Override
		public List<DropDownRes> getProductCategory(LovDropDownReq req) {
			List<DropDownRes> resList = new ArrayList<DropDownRes>();
			try {
			//	List<ListItemValue> getList = listRepo.findByItemTypeAndStatusOrderByItemCodeAsc("PRODUCT_CATEGORY", "Y");
				String itemType = "PRODUCT_CATEGORY" ;
				List<ListItemValue> list  = getListItem(req , itemType);
				for (ListItemValue data : list) {
					DropDownRes res = new DropDownRes();
					res.setCode(data.getItemCode());
					res.setCodeDesc(data.getItemValue());
					res.setStatus(data.getStatus());
					resList.add(res);
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception is ---> " + e.getMessage());
				return null;
			}
			return resList;
		}

		private static <T> java.util.function.Predicate<T> distinctByKey(java.util.function.Function<? super T, ?> keyExtractor) {
		    Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		    return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
		}
		
		
		public synchronized List<ListItemValue> getListItem(LovDropDownReq req , String itemType) {
			List<ListItemValue> list = new ArrayList<ListItemValue>();
			try {
				Date today = new Date();
				Calendar cal = new GregorianCalendar();
				cal.setTime(today);
				today = cal.getTime();
				Date todayEnd = cal.getTime();
				
				// Criteria
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<ListItemValue> query=  cb.createQuery(ListItemValue.class);
				// Find All
				Root<ListItemValue> c = query.from(ListItemValue.class);
				
				//Select
				query.select(c);
				// Order By
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(c.get("branchCode")));
				
				
				// Effective Date Start Max Filter
				Subquery<Long> effectiveDate = query.subquery(Long.class);
				Root<ListItemValue> ocpm1 = effectiveDate.from(ListItemValue.class);
				effectiveDate.select(cb.max(ocpm1.get("effectiveDateStart")));
				Predicate a1 = cb.equal(c.get("itemId"),ocpm1.get("itemId"));
				Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
				effectiveDate.where(a1,a2);
				// Effective Date End Max Filter
				Subquery<Long> effectiveDate2 = query.subquery(Long.class);
				Root<ListItemValue> ocpm2 = effectiveDate2.from(ListItemValue.class);
				effectiveDate2.select(cb.max(ocpm2.get("effectiveDateEnd")));
				Predicate a3 = cb.equal(c.get("itemId"),ocpm2.get("itemId"));
				Predicate a4 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
				effectiveDate2.where(a3,a4);
							
				// Where
				Predicate n1 = cb.equal(c.get("status"),"Y");
				Predicate n2 = cb.equal(c.get("effectiveDateStart"),effectiveDate);
				Predicate n3 = cb.equal(c.get("effectiveDateEnd"),effectiveDate2);	
				Predicate n4 = cb.equal(c.get("companyId"), req.getInsuranceId());
				Predicate n5 = cb.equal(c.get("companyId"), "99999");
				Predicate n6 = cb.equal(c.get("branchCode"), req.getBranchCode());
				Predicate n7 = cb.equal(c.get("branchCode"), "99999");
				Predicate n8 = cb.or(n4,n5);
				Predicate n9 = cb.or(n6,n7);
				Predicate n10 = cb.equal(c.get("itemType"),itemType);
				query.where(n1,n2,n3,n8,n9,n10).orderBy(orderList);
				// Get Result
				TypedQuery<ListItemValue> result = em.createQuery(query);
				list = result.getResultList();
				
				list = list.stream().filter(distinctByKey(o -> Arrays.asList(o.getItemCode()))).collect(Collectors.toList());
				list.sort(Comparator.comparing(ListItemValue :: getItemValue));
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception is ---> " + e.getMessage());
				return null;
			}
			return list ;
		}

		@Override
		public List<DropDownRes> getDocType(LovDropDownReq req) {
			List<DropDownRes> resList = new ArrayList<DropDownRes>();
			try {
			//	List<ListItemValue> getList = listRepo.findByItemTypeAndStatusOrderByItemCodeAsc("PRODUCT_CATEGORY", "Y");
				String itemType = "DOC_TYPE" ;
				List<ListItemValue> list  = getListItem(req , itemType);
				for (ListItemValue data : list) {
					DropDownRes res = new DropDownRes();
					res.setCode(data.getItemCode());
					res.setCodeDesc(data.getItemValue());
					res.setStatus(data.getStatus());
					resList.add(res);
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception is ---> " + e.getMessage());
				return null;
			}
			return resList;
		}


}
