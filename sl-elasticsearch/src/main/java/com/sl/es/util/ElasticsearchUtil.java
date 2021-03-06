package com.sl.es.util;

import com.sl.es.config.ESIndex;
import com.sl.es.config.ESQueryModel;
import com.sl.es.config.ESQueryResult;
import com.sl.es.constant.ElasticsearchConstant;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.min.Min;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCount;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.apache.commons.collections.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Component
@DependsOn("elasticSearchConfig")
public class ElasticsearchUtil {
    @Resource
    private RestHighLevelClient client;


    private static RestHighLevelClient restHighLevelClient;


    @PostConstruct
    public void init() {
        restHighLevelClient = client;
    }

    // ????????????
    public static boolean createIndex(String indexName) throws IOException {
        if (isExistsIndex(indexName)) {
            return false;
        }

        //??????????????????
        CreateIndexRequest request = new CreateIndexRequest(indexName); //hcode_index????????????
        //???????????????????????????
        CreateIndexResponse response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        return response.isAcknowledged();
    }


    /**
     * ??????????????????(??????/mapping)
     *
     * @param esIndex ??????
     */
    public static boolean createIndex(ESIndex esIndex) throws IOException {

        if (isExistsIndex(esIndex.getIndexName())) {
            return false;
        }
        //??????????????????
        CreateIndexRequest request = new CreateIndexRequest(esIndex.getIndexName());
        //????????????????????????
        request.settings(Settings.builder()
                .put("index.number_of_shards", esIndex.getIndexNumberShards())
                .put("index.number_of_replicas", esIndex.getIndexNumberReplicas())
        );
        //???????????????????????????????????????ES??????JavaDTO???????????????JSON?????????
        request.mapping(esIndex.getMappingSource(), esIndex.getIndexType());
        //????????????
        CreateIndexResponse response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        return response.isAcknowledged();
    }

    /**
     * ????????????????????????????????????
     *
     * @param indexName ?????????
     * @return ?????????true; ????????????false;
     */
    public static boolean isExistsIndex(String indexName) throws IOException {
        GetIndexRequest request = new GetIndexRequest(indexName);
        return restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
    }


    /**
     * ????????????
     *
     * @param indexName ????????????
     */
    public static boolean deleteIndex(String indexName) throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest(indexName);//??????????????????????????????
        //????????????
        AcknowledgedResponse response = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
        return response.isAcknowledged();
    }


    /**
     * ??????????????????
     *
     * @param indexName
     * @param dataId
     * @param dataSource
     * @throws IOException
     */
    public static void addData(String indexName, String dataId, String dataSource) throws IOException {
        IndexRequest indexRequest = new IndexRequest(indexName);
        indexRequest.id(dataId).source(dataSource, XContentType.JSON);
        IndexResponse response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
    }

    /**
     * ??????????????????
     *
     * @param indexName
     * @param dataId
     * @param dataMap
     * @throws IOException
     */
    public static void addData(String indexName, String dataId, Map<String, Object> dataMap) throws IOException {
        IndexRequest indexRequest = new IndexRequest(indexName);
        indexRequest.id(dataId).source(dataMap);
        IndexResponse response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
    }

    /**
     * ??????????????????
     *
     * @param indexName ?????????
     * @param dataList  ????????????map?????????id?????????????????????????????????id??????????????????id
     * @throws IOException
     */
    public static void batchAddData(String indexName, List<Map<String, Object>> dataList) throws IOException {
        if (StringUtils.isEmpty(indexName) || CollectionUtils.isEmpty(dataList)) {
            return;
        }
        IndexRequest indexRequest;
        String id;
        List<IndexRequest> requests = new ArrayList<>();
        for (Map<String, Object> dataMap : dataList) {
            if (dataMap.containsKey(ElasticsearchConstant.ES_ID)) {
                id = (String) dataMap.get(ElasticsearchConstant.ES_ID);
            } else {
                id = UUID.randomUUID().toString();
            }
            indexRequest = new IndexRequest(indexName);
            indexRequest.id(id).source(dataMap);
            requests.add(indexRequest);
        }
        batchAddData(requests);

    }

    /**
     * ??????????????????
     *
     * @param indexRequestList ??????????????????request
     * @throws IOException
     */
    public static void batchAddData(List<IndexRequest> indexRequestList) throws IOException {
        if (CollectionUtils.isEmpty(indexRequestList)) {
            return;
        }
        BulkRequest bulkRequest = new BulkRequest();
        for (IndexRequest indexRequest : indexRequestList) {
            bulkRequest.add(indexRequest);
        }
        BulkResponse response = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);

    }

    /**
     * ??????id?????????????????????
     *
     * @param indexName ?????????
     * @param id        ????????????id
     * @throws IOException
     */
    public static void deleteData(String indexName, String id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(indexName);
        deleteRequest.id(id);
        DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
    }

    /**
     * ??????id????????????
     *
     * @param indexName
     * @param ids
     * @throws IOException
     */
    public static void batchDeleteData(String indexName, List<String> ids) throws IOException {
        if (StringUtils.isEmpty(indexName) || CollectionUtils.isEmpty(ids)) {
            return;
        }
        List<DeleteRequest> deleteRequestList = new ArrayList<>();
        DeleteRequest deleteRequest;
        for (String id : ids) {
            deleteRequest = new DeleteRequest(indexName).id(id);
            deleteRequestList.add(deleteRequest);
        }
        batchDeleteData(deleteRequestList);
    }

    /**
     * ????????????
     *
     * @param deleteRequestList ????????????request
     * @throws IOException
     */
    public static void batchDeleteData(List<DeleteRequest> deleteRequestList) throws IOException {
        if (CollectionUtils.isEmpty(deleteRequestList)) {
            return;
        }
        BulkRequest bulkRequest = new BulkRequest();
        for (DeleteRequest indexRequest : deleteRequestList) {
            bulkRequest.add(indexRequest);
        }
        BulkResponse response = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
    }

    /**
     * ????????????????????????
     *
     * @param deleteByQueryRequest
     * @throws IOException
     */
    public static long deleteByQuery(DeleteByQueryRequest deleteByQueryRequest) throws IOException {
        BulkByScrollResponse response = restHighLevelClient.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);
        return response.getDeleted();
    }

    /**
     * es??????
     *
     * @param esQueryModel ???????????????
     * @return
     * @throws IOException
     */
    public static ESQueryResult queryData(ESQueryModel esQueryModel) throws IOException {
        if (StringUtils.isEmpty(esQueryModel.getIndexName())) {
            return null;
        }
        SearchRequest searchRequest = generateSearchRequest(esQueryModel);
        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        if (response.status() != RestStatus.OK) {
            return null;
        }
        return getESQueryResult(response, esQueryModel);

    }


    private static SearchRequest generateSearchRequest(ESQueryModel esQueryModel) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(esQueryModel.getIndexName());
        // ??????????????????
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder queryBuilder = new BoolQueryBuilder();

        /** ?????? ????????????MustTermQuery??????*/
        List<ESQueryModel.MustTermQuery> mustTermQueries = esQueryModel.getMustTermQueries();
        if (CollectionUtils.isNotEmpty(mustTermQueries)) {
            for (ESQueryModel.MustTermQuery mustTermQuery : mustTermQueries) {
                if (mustTermQuery.isMust()) {
                    queryBuilder.must(QueryBuilders.termQuery(mustTermQuery.getName(), mustTermQuery.getValue()));
                } else {
                    queryBuilder.mustNot(QueryBuilders.termQuery(mustTermQuery.getName(), mustTermQuery.getValue()));
                }
            }
        }

        /** ?????????????????????in??? MustTermsQuery??????*/
        List<ESQueryModel.MustTermsQuery> mustTermsQueries = esQueryModel.getMustTermsQueries();
        if (CollectionUtils.isNotEmpty(mustTermsQueries)) {
            for (ESQueryModel.MustTermsQuery mustTermsQuery : mustTermsQueries) {
                if (mustTermsQuery.isMust()) {
                    queryBuilder.must(QueryBuilders.termsQuery(mustTermsQuery.getName(), mustTermsQuery.getValues()));
                } else {
                    queryBuilder.mustNot(QueryBuilders.termsQuery(mustTermsQuery.getName(), mustTermsQuery.getValues()));
                }
            }
        }


        /** ?????????????????? ?????? MustRangeQuery*/
        List<ESQueryModel.MustRangeQuery> mustRangeQueries = esQueryModel.getMustRangeQueries();
        if (CollectionUtils.isNotEmpty(mustRangeQueries)) {
            for (ESQueryModel.MustRangeQuery mustRangeQuery : mustRangeQueries) {
                RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery(mustRangeQuery.getName());
                if (mustRangeQuery.getGte() != null) {
                    rangeQuery.gte(mustRangeQuery.getGte());
                }
                if (mustRangeQuery.getGt() != null) {
                    rangeQuery.gt(mustRangeQuery.getGt());
                }
                if (mustRangeQuery.getLte() != null) {
                    rangeQuery.lte(mustRangeQuery.getLte());
                }
                if (mustRangeQuery.getLt() != null) {
                    rangeQuery.lt(mustRangeQuery.getLt());
                }
                if (mustRangeQuery.isMust()) {
                    queryBuilder.must(rangeQuery);
                } else {
                    queryBuilder.mustNot(rangeQuery);
                }
            }
        }

        /** ????????????????????????????????? ?????? MustWildcardQuery*/
        List<ESQueryModel.MustWildcardQuery> mustWildcardQueries = esQueryModel.getMustWildcardQueries();
        if (CollectionUtils.isNotEmpty(mustWildcardQueries)) {
            for (ESQueryModel.MustWildcardQuery mustWildcardQuery : mustWildcardQueries) {
                if (mustWildcardQuery.isMust()) {
                    queryBuilder.must(QueryBuilders.wildcardQuery(mustWildcardQuery.getName(), mustWildcardQuery.getQuery()));
                } else {
                    queryBuilder.mustNot(QueryBuilders.wildcardQuery(mustWildcardQuery.getName(), mustWildcardQuery.getQuery()));
                }
            }

        }

        /** ???????????? **/
        sourceBuilder.query(queryBuilder);

        /** ?????? AggregationQuery*/
        List<ESQueryModel.AggregationQuery> aggregationQueries = esQueryModel.getAggregationQueries();
        if (CollectionUtils.isNotEmpty(aggregationQueries)) {
            for (ESQueryModel.AggregationQuery aggregationQuery : aggregationQueries) {
                if (!aggregationQuery.isAggTermEmpty()) {
                    //????????????????????????
                    ESQueryModel.AggregationQuery.AggBean aggBean = aggregationQuery.getAggTerms().get(aggregationQuery.getAggTerms().size()-1);
                    AggregationBuilder aggregationBuilder = AggregationBuilders.terms(aggBean.getName()).field(aggBean.getField());

                    //??????????????????
                    for (ESQueryModel.AggregationQuery.AggBean sumAggBean : aggregationQuery.getAggSum()) {
                        aggregationBuilder.subAggregation(AggregationBuilders.sum(sumAggBean.getName()).field(sumAggBean.getField()));
                    }

                    //??????????????????
                    for (ESQueryModel.AggregationQuery.AggBean avgAggBean : aggregationQuery.getAggAvg()) {
                        aggregationBuilder.subAggregation(AggregationBuilders.avg(avgAggBean.getName()).field(avgAggBean.getField()));
                    }

                    //??????????????????
                    for (ESQueryModel.AggregationQuery.AggBean countAggBean : aggregationQuery.getAggCount()) {
                        aggregationBuilder.subAggregation(AggregationBuilders.count(countAggBean.getName()).field(countAggBean.getField()));
                    }
                    // ????????????????????????
                    for (ESQueryModel.AggregationQuery.AggBean cardinalityCount : aggregationQuery.getAggCardinalityCount()) {
                        aggregationBuilder.subAggregation(AggregationBuilders.cardinality(cardinalityCount.getName()).field(cardinalityCount.getField()));
                    }
                    // ?????????????????????
                    for (ESQueryModel.AggregationQuery.AggBean maxAggBean : aggregationQuery.getAggMax()) {
                        aggregationBuilder.subAggregation(AggregationBuilders.max(maxAggBean.getName()).field(maxAggBean.getField()));
                    }
                    // ?????????????????????
                    for (ESQueryModel.AggregationQuery.AggBean minAggBean : aggregationQuery.getAggMin()) {
                        aggregationBuilder.subAggregation(AggregationBuilders.min(minAggBean.getName()).field(minAggBean.getField()));
                    }
                    //????????????????????????
                    AggregationBuilder finalBuilder = aggregationBuilder;
                    List<ESQueryModel.AggregationQuery.AggBean> aggBeans = aggregationQuery.getAggTerms();
                    if(CollectionUtils.isNotEmpty(aggBeans)) {
                        // ?????????????????????????????????
                        for (int i=aggBeans.size()-2;i>=0;i-- ) {
                            AggregationBuilder tempBuilder = AggregationBuilders.terms(aggBeans.get(i).getName()).field(aggBeans.get(i).getField());
                            finalBuilder = tempBuilder.subAggregation(finalBuilder);
                        }
                    }
                    /** ???????????? **/
                    sourceBuilder.aggregation(finalBuilder);
                }
            }

        }

        /** ??????????????????*/
        List<ESQueryModel.SortQuery> sortQueries = esQueryModel.getSortQueries();
        if (CollectionUtils.isNotEmpty(sortQueries)) {
            for (ESQueryModel.SortQuery sortQuery : sortQueries) {
                sourceBuilder.sort(new FieldSortBuilder(sortQuery.getField()).order(sortQuery.getSortOrder()));
            }

        }

        /**?????? **/
        if (esQueryModel.getFrom() != null && esQueryModel.getSize() != null) {
            sourceBuilder.from(esQueryModel.getFrom());
            sourceBuilder.size(esQueryModel.getSize());
        }
        /** ????????????????????????**/
        if ((esQueryModel.getIncludeFields() != null && esQueryModel.getIncludeFields().length > 0) || (esQueryModel.getExcludeFields() != null && esQueryModel.getExcludeFields().length > 0)) {
            sourceBuilder.fetchSource(esQueryModel.getIncludeFields(), esQueryModel.getExcludeFields());
        }
        searchRequest.source(sourceBuilder);
        return searchRequest;
    }

    /**
     * ????????????????????????
     *
     * @param response     es??????
     * @param esQueryModel es????????????
     * @return
     */
    private static ESQueryResult getESQueryResult(SearchResponse response, ESQueryModel esQueryModel) {
        ESQueryResult esQueryResult = new ESQueryResult();

        //??????source
        List<Map<String, Object>> dataList = Arrays.stream(response.getHits().getHits()).map(b -> {
            return b.getSourceAsMap();
        }).collect(Collectors.toList());
        esQueryResult.setHitsList(dataList);
        //?????????????????????????????????
        List<ESQueryModel.AggregationQuery> aggregationQueryList = esQueryModel.getAggregationQueries();
        if(CollectionUtils.isNotEmpty(aggregationQueryList)){
            // ?????????????????????
            List<List<Map<String,Object>>> listDataList = new ArrayList<>();
            for (ESQueryModel.AggregationQuery aggregationQuery : aggregationQueryList) {
                // ????????????????????????
                if (!aggregationQuery.isAggTermEmpty()) {
                    List<ESQueryModel.AggregationQuery.AggBean> aggBeanList = aggregationQuery.getAggTerms();
                    if(CollectionUtils.isNotEmpty(aggBeanList)) {
                        // ??????????????????????????????????????????
                        int index = 0;
                        ParsedLongTerms terms = response.getAggregations().get(aggBeanList.get(index).getName());
                        List bucketList =  terms.getBuckets();
                        // ????????????????????????????????????
                        List<Map<String,Object>> mapList = processBuckets(bucketList,index,aggBeanList,null,aggregationQuery);
                        listDataList.add(mapList);
                    }

                }
            }
            esQueryResult.setAggregationsList(listDataList);
        }

        return esQueryResult;
    }

    private  static List<Map<String,Object>>  processBuckets(List<Terms.Bucket> bucketList, int index, List<ESQueryModel.AggregationQuery.AggBean> aggBeanList, Map<String,Object> dataMap, ESQueryModel.AggregationQuery aggregationQuery){
        if(CollectionUtils.isEmpty(bucketList)){
            return null;
        }
        if(dataMap == null){
            dataMap = new HashMap<>();
        }
        List<Map<String,Object>> mapList = new ArrayList<>();
        if(index < aggBeanList.size()-1){    // ??????????????????????????????
            for(Terms.Bucket bucket : bucketList){
                  Map<String,Object> subMap = new HashMap<>();
                  subMap.putAll(dataMap);
                  subMap.put(aggBeanList.get(index).getField(),bucket.getKey());
                  Terms terms =  bucket.getAggregations().get(aggBeanList.get(index+1).getName());
                  List<Map<String,Object>> dataList = processBuckets((List<Terms.Bucket>)terms.getBuckets(),index+1,aggBeanList,subMap,aggregationQuery);
                  mapList.addAll(dataList);
              }
        }else{
            // ??????????????????????????????
            Map<String,Object> finaMap;
            for (Terms.Bucket bucket : bucketList) {
                finaMap = new HashMap<>();
                finaMap.put(aggBeanList.get(index).getField(),bucket.getKey());
                finaMap.putAll(dataMap);
                    //??????????????????
                    for (ESQueryModel.AggregationQuery.AggBean sumAggBean : aggregationQuery.getAggSum()) {
                        Sum sum = bucket.getAggregations().get(sumAggBean.getName());
                        finaMap.put(sumAggBean.getName(),sum.getValue());
                    }

                    //??????????????????
                    for (ESQueryModel.AggregationQuery.AggBean avgAggBean : aggregationQuery.getAggAvg()) {
                        Avg avg = bucket.getAggregations().get(avgAggBean.getName());
                        finaMap.put(avgAggBean.getName(),avg.getValue());
                    }
                    for (ESQueryModel.AggregationQuery.AggBean count : aggregationQuery.getAggCount()){
                        ValueCount valueCount = bucket.getAggregations().get(count.getName());
                        finaMap.put(count.getName(),valueCount.getValue());
                    }
                    // ????????????????????????
                    for (ESQueryModel.AggregationQuery.AggBean cardinalityCount : aggregationQuery.getAggCardinalityCount() ){
                        Cardinality cardinality = bucket.getAggregations().get(cardinalityCount.getName());
                        finaMap.put(cardinalityCount.getName(),cardinality.getValue());
                    }

                    //?????????????????????
                    for (ESQueryModel.AggregationQuery.AggBean maxAggBean : aggregationQuery.getAggMax() ){
                        Max max = bucket.getAggregations().get(maxAggBean.getName());
                        finaMap.put(maxAggBean.getName(),max.getValue());
                    }
                    //?????????????????????
                    for (ESQueryModel.AggregationQuery.AggBean minAggBean : aggregationQuery.getAggMin() ){
                        Min max = bucket.getAggregations().get(minAggBean.getName());
                        finaMap.put(minAggBean.getName(),max.getValue());
                    }
                 mapList.add(finaMap);
              }
        }

        return mapList;
    }

}

