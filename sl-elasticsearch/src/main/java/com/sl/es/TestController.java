package com.sl.es;

import com.sl.es.config.ESIndex;
import com.sl.es.config.ESQueryModel;
import com.sl.es.config.ESQueryResult;
import com.sl.es.util.ElasticsearchUtil;
import com.sl.es.util.JsonUtil;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TestController {
    @RequestMapping("createESIndex")
    public void createESIndex(){
        try {
            ElasticsearchUtil.createIndex("luhai");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping("createESIndexBean")
    public void createESIndexBean(){
        try {
            ESIndex esIndex = new ESIndex("index_test");
            String mapping = "{\n" +
                    "        \"properties\":{\n" +
                    "            \"id\":{\n" +
                    "                \"type\":\"text\"\n" +
                    "            },\n" +
                    "            \"name\":{\n" +
                    "                \"type\":\"text\"\n" +
                    "            },\n" +
                    "            \"age\":{\n" +
                    "                \"type\":\"integer\"\n" +
                    "            },\n" +
                    "            \"birthday\":{\n" +
                    "                \"type\":\"date\"\n" +
                    "            },\n" +

                    "            \"areaId\":{\n" +
                    "                \"type\":\"integer\"\n" +
                    "            },\n" +
                    "            \"sex\":{\n" +
                    "                \"type\":\"integer\"\n" +
                    "            }\n" +
                    "        }\n" +
                    "}";
            esIndex.setMappingSource(mapping);
            ElasticsearchUtil.createIndex(esIndex);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping("deleteESIndex")
    public void deleteESIndex(){
        try {
            ElasticsearchUtil.deleteIndex("ncms");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping("addIndexData")
    public void addIndexData(){
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("id",21);
        dataMap.put("name","张三");
        dataMap.put("age",23);
        dataMap.put("birthday","2020-10-21");
        dataMap.put("sex","1");
        dataMap.put("areaId","0");
        try {
            ElasticsearchUtil.addData("index_test","21",JsonUtil.toJson(dataMap));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @RequestMapping("batchAddData")
    public void batchAddData(){
        List<Map<String,Object>> dataList = new ArrayList<>();
        Map<String,Object> dataMap;
        for(int i=1;i<20;i++){
            dataMap = new HashMap<>();
            dataMap.put("id",i + "");
            dataMap.put("name","张三"+i);
            dataMap.put("age",20 + i);
            dataMap.put("birthday","2020-10-21");
            dataMap.put("sex",i%2);
            dataMap.put("areaId",i%3);
            dataList.add(dataMap);
        }
        try {
            ElasticsearchUtil.batchAddData("index_test",dataList);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping("deleteData")
    public void deleteData(){
        try {
            ElasticsearchUtil.deleteData("test_index","19");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @RequestMapping("batchDeleteData")
    public void batchDeleteData(){
        List<String> ids = new ArrayList<>();
        ids.add("18");
        ids.add("17");
        ids.add("16");
        try {
            ElasticsearchUtil.batchDeleteData("test_index",ids);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping("deleteByQuery")
    public void deleteByQuery(){

        DeleteByQueryRequest request = new DeleteByQueryRequest("test_index");
        request.setQuery(new TermQueryBuilder("age", "29"));
        try {
            ElasticsearchUtil.deleteByQuery(request);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping("queryES")
    public String queryES(){
        ESQueryModel queryModel = new  ESQueryModel();

        String[] includeFields = {"id","name"};
        String[] excludeFields = {"birthday"};
        queryModel.setFrom(0).setSize(10).setIndexName("index_test");
        //  queryModel.setExcludeFields(excludeFields);
        //  queryModel.addSortQuery(queryModel.new SortQuery("age",SortOrder.ASC));
        // 多组聚合 sum、avg统计
        ESQueryModel.AggregationQuery aggregationQuery2 = queryModel.new AggregationQuery();
        aggregationQuery2
                .pushAggTerm(aggregationQuery2.new AggBean("group_of_area1","areaId"))
                .addAggSum(aggregationQuery2.new AggBean("sum_age1","age"));

        ESQueryModel.AggregationQuery aggregationQuery3 = queryModel.new AggregationQuery();
        aggregationQuery3
                .pushAggTerm(aggregationQuery3.new AggBean("group_of_sex","sex"))
                .pushAggTerm(aggregationQuery3.new AggBean("group_of_area2","areaId"))
                .addAggSum(aggregationQuery3.new AggBean("sum_age2","age"))
                .addAggMax(aggregationQuery3.new AggBean("max_age","age"))
                .addAggMin(aggregationQuery3.new AggBean("min_age","age"))
                .addAggAvg(aggregationQuery3.new AggBean("avg_age","age"))
                .addAggCount(aggregationQuery3.new AggBean("count_age","age"))
                .addCardinalityCount(aggregationQuery3.new AggBean("cardina_age","age"));

        queryModel.addAggregationQuery(aggregationQuery2);

        queryModel.addAggregationQuery(aggregationQuery3);
        try {
            ESQueryResult esQueryResult = ElasticsearchUtil.queryData(queryModel);

            return JsonUtil.toJson(esQueryResult);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
