package com.sl.excel.controller;

import com.sl.excel.innter.ExcelMate;
import com.sl.excel.innter.WorkbookParser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ExcelController {


    @RequestMapping("testExport")
    public void testExport() {
         WorkbookParser wbParser = new WorkbookParser();

        List<ExcelData> excelDataList = new ArrayList<>();
        ExcelData data  = new ExcelData();
        data.setName("luhai");
        data.setAge(10);
        excelDataList.add(data);

        ExcelMate excelMate =  wbParser.export(excelDataList);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("D:\\" + excelMate.getFullName());
            fileOutputStream.write(excelMate.getData());
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
