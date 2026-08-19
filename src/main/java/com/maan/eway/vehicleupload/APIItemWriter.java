package com.maan.eway.vehicleupload;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.google.gson.Gson;
import com.maan.eway.batch.entity.EserviceMotorDetailsRaw;
import com.maan.eway.batch.repository.EserviceMotorDetailsRawRepository;
import com.maan.eway.repository.MasterLookupRepository;

public class APIItemWriter implements ItemWriter<EserviceMotorDetailsRaw> {

    @Autowired
    private EserviceMotorDetailsRawRepository eserviceMotorRawRepo;

    @Autowired
    private VehicleAsynchronousProcess vehicleAsyncService;

    @Autowired
    private MasterLookupRepository masterRepo;

    // ── Inject all @Value properties that APIItemProcessor needs ──────────────
    @Value("${tira.api}")
    private String tiraApi;

    @Value("${save.vehicle.api}")
    private String vehicleApi;

    @Value("${premium.calc.api}")
    private String calcApi;

    @Value("${employee.validation.api}")
    private String employeeValidationApi;

    @Value("${employee.delete.api}")
    private String employeeDeleteApi;

    @Value("${employee.merge.api}")
    private String employeeMergeApi;

    @Value("${passenger.save.api}")
    private String travelSaveApi;

    @Value("${save.vehicleInfo.api}")
    private String saveVehicleApi;

    private final String authorization;

    public APIItemWriter(String authorization) {
        this.authorization = authorization;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(Chunk<? extends EserviceMotorDetailsRaw> chunk) throws Exception {

        // ── Build processor with all dependencies injected ────────────────────
        APIItemProcessor processor = new APIItemProcessor(authorization);
        processor.setService(vehicleAsyncService);
        processor.setMasterRepo(masterRepo);
        processor.setTiraApi(tiraApi);
        processor.setVehicleApi(vehicleApi);
        processor.setCalcApi(calcApi);
        processor.setSaveVehicleApi(saveVehicleApi);

        List<EserviceMotorDetailsRaw> items = (List<EserviceMotorDetailsRaw>) chunk.getItems();

        List<CompletableFuture<EserviceMotorDetailsRaw>> futures = items.stream()
            .map(item -> {
                boolean isTira    = "Y".equalsIgnoreCase(item.getTiraSearchByDesc());
                boolean isCompany = "100002".equals(item.getCompanyId().toString());

                if (isTira && isCompany) {
                    return processor.processTiraFlowAsync(item);
                } else {
                    return CompletableFuture.supplyAsync(() -> {
                        try {
                            List<Map<String, Object>> errorList = new ArrayList<>();
                            processor.processExistingFlow(item, errorList);
                            if (!errorList.isEmpty()) {
                                item.setErrorDesc(new Gson().toJson(errorList));
                                item.setStatus("E");
                            }
                            return item;
                        } catch (Exception e) {
                            item.setStatus("E");
                            item.setErrorDesc(e.getMessage());
                            return item;
                        }
                    });
                }
            })
            .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .get(300, TimeUnit.SECONDS);

        List<EserviceMotorDetailsRaw> results = futures.stream()
            .map(f -> {
                try { return f.get(); }
                catch (Exception e) { return null; }
            })
            .filter(Objects::nonNull)
            .toList();

        eserviceMotorRawRepo.saveAll(results);
    }
}