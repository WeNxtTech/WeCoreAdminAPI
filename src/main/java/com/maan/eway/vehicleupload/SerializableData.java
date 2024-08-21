package com.maan.eway.vehicleupload;


import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SerializableData implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Map<String, Serializable>> data;

    public SerializableData(List<Map<String, String>> originalData) {
        this.data = originalData.stream()
            .map(map -> map.entrySet().stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> (Serializable) entry.getValue()
                )))
            .collect(Collectors.toList());
    }

    public List<Map<String, Serializable>> getData() {
        return data;
    }
}
