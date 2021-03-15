package com.sl;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Image;
import com.sl.pdf.*;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TestController {
    @RequestMapping("testPdf")
    public void testPdf( HttpServletResponse response) {
        PdfData pdfData = getPdfData();
        PdfUtil.generateToPdf(pdfData,response);
    }

    private PdfData getPdfData(){
        PdfData pdfData = new PdfData();
        List<PdfFtlData> pdfFtlDataList = new ArrayList<>();
        PdfFtlData pdfFtlData = new PdfFtlData();
//        pdfData.setNeedWaterImage(true);
        WaterImage waterImage = new WaterImage();
        waterImage.setImagePath("/templates/ftl/bijiegongzhang.jpg");
        waterImage.setScaleAbsoluteWidth(100);
        waterImage.setScaleAbsoluteHeight(100);
        waterImage.setFillOpacity(1);
        waterImage.setRotation(90);
        waterImage.setRotationDegrees(90);
        pdfData.setWaterImage(waterImage);

        WaterMark waterMark = new WaterMark();
        waterMark.setWaterX(250);
        waterMark.setWaterY(200);
        waterMark.setWaterText("水印文字");
        waterMark.setSize(50);
        waterMark.setColorFill(BaseColor.GRAY);
        waterMark.setFillOpacity(1);
        pdfFtlData.setTemplateFileName("unitPayPlan.ftl");
        pdfFtlData.setTemplatePath("templates/ftl");
        Map<String,Object> tmpData = new HashMap();
        Map<String,Object> data = new HashMap();

        List<Map<String,Object>> unitPayPlanList = new ArrayList();
        for(int i = 0;i<3;i++){
            Map<String,Object> obj = new HashMap();
            obj.put("insuredType","职工"+i);
            obj.put("payState","已支付");
            obj.put("insuredHandleOrg","insuredHandleOrg"+i);
            obj.put("insuredNum","100"+i);
            obj.put("startTime","20200601"+i);
            obj.put("endTime","20200624"+i);
            unitPayPlanList.add(obj);
        }
        data.put("unitName","晶奇");
        data.put("creditCode","123456");
        data.put("unitCode","115211");
        data.put("unitPayPlanList",unitPayPlanList);

        data.put("currentDate","2020-06-24");
        tmpData.put("data",data);

        pdfFtlData.setTemplateData(tmpData);
        pdfFtlData.setNeedWaterImage(true);
        pdfFtlData.setWaterImage(waterImage);
        pdfFtlData.setNeedWaterMark(true);
        pdfFtlData.setWaterMark(waterMark);
        pdfFtlDataList.add(pdfFtlData);
        PdfFtlData pdfFtlData1 = new PdfFtlData();
        pdfFtlData1.setTemplateFileName("test.ftl");
        pdfFtlData1.setTemplatePath("templates/ftl");
        Map<String,Object> tmpData1 = new HashMap();
        Map<String,Object> data1 = new HashMap();
        data1.put("testData","luhaipt");
        tmpData1.put("data",data1);
        pdfFtlData1.setTemplateData(tmpData1);
        pdfFtlData1.setNeedWaterMark(true);
        pdfFtlData1.setWaterMark(waterMark);
        //  pdfFtlDataList.add(pdfFtlData1);
        // pdfFtlDataList.add(pdfFtlData);
        // pdfFtlDataList.add(pdfFtlData1);
        // pdfFtlDataList.add(pdfFtlData);
        pdfData.setPdfFtlDataList(pdfFtlDataList);

        PdfHeader pdfHeader = new PdfHeader();
        pdfHeader.setHeaderText("这是页眉");
        pdfHeader.setAlignment(0);
        pdfHeader.setRightSpace(250);
        pdfHeader.setTopSpace(-20);

        PdfFooter pdfFooter = new PdfFooter();
        pdfFooter.setHeaderText("-");
        pdfFooter.setAlignment(2);
        pdfFooter.setLeftSpace(260);
        pdfFooter.setBottomSpace(-10);

        pdfData.setNeedHeader(true);
        pdfData.setNeedFooter(true);
        pdfData.setPdfHeader(pdfHeader);
        pdfData.setPdfFooter(pdfFooter);
        // todo 目录
        try{
            Image image =  Image.getInstance(IOUtils.toByteArray(new ClassPathResource("/templates/ftl/115121.jpg").getInputStream()));
            pdfHeader.setImage(image);
            pdfFooter.setImage(image);
        }catch (Exception e){
            e.printStackTrace();
        }

        // pdfData.setNeedCatalog(true);
        PdfCatalog pdfCatalog = new PdfCatalog();
        Map<String, Integer> catalogs = new HashMap<>();
        catalogs.put("表格1",1);
        catalogs.put("test1",4);
        catalogs.put("表格2",5);
        catalogs.put("test2",8);
        catalogs.put("表格3",9);
        //  pdfCatalog.setCatalogs(catalogs);
        // pdfData.setPdfCatalog(pdfCatalog);
        return pdfData;
    }
}
