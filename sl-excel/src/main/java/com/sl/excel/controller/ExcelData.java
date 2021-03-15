package com.sl.excel.controller;

import com.sl.excel.annotation.Excel;
import com.sl.excel.annotation.ExcelColumn;
import lombok.Data;

@Excel
@Data
public class ExcelData {
    @ExcelColumn(index=1)
    private String name;
    @ExcelColumn(index=0)
    private Integer age;
}
