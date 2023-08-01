package com.maan.eway.vehicleupload;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.stereotype.Component;

@Component
public class VehicleCustomFieldSetMapper implements FieldSetMapper<Record> {

	@Override
    public Record mapFieldSet(FieldSet fieldSet) throws BindException {
        Record record = new Record();
        Object[] row = new Object[fieldSet.getValues().length];
        for (int i = 0; i < fieldSet.getValues().length; i++) {
            row[i] = StringUtils.isNotBlank(fieldSet.getValues()[i])?fieldSet.getValues()[i].replace("\n", "").replace("\r", "").replace("'", "").replaceAll("^\"|\"$", ""):"";
        }
        record.setColumns(row);
        return record;
    }
}