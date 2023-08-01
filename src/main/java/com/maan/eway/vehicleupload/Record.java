package com.maan.eway.vehicleupload;

public class Record {
	
	private Object[] columns;

    public void setColumnByIndex(Object candidate, int index) {
        columns[index] = candidate;
    }

    public Object getColumnByIndex(int index){
        return columns[index];
    }

    public void setColumns(Object[] columns) {
        this.columns = columns;
    }

	public Object[] getColumns() {
		return columns;
	}


}
