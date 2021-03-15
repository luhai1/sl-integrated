package com.sl.excel.innter;


import java.util.ArrayList;
import java.util.List;

public class ExcelRowMate {
    private List<Column> columns =new ArrayList<>();

    private Object raw;

    public List<Column> getColumns() { return this.columns; }



    public void setColumns(List<Column> columns) { this.columns = columns; }



    public void addColumn(Column column) { this.columns.add(column); }



    public Object getRaw() { return this.raw; }



    public void setRaw(Object raw) { this.raw = raw; }
}
