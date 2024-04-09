package com.heima.search.service.Impl;

import com.alibaba.fastjson.JSON;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.search.dto.UserSearchDto;
import com.heima.model.user.pojos.ApUser;
import com.heima.search.service.ApUserSearchService;
import com.heima.search.service.ArticleSearch;
import com.heima.utils.Thread.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: 周海
 * @Create : 2024/4/7
 **/
@Service
@Slf4j
public class ArticleSearchImpl implements ArticleSearch {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private ApUserSearchService apUserSearchService;

    /**
     * es文章分页检索
     *
     * @param dto
     * @return
     */
    @Async
    public ResponseResult search(UserSearchDto dto) {
        //1检查参数
        if (dto == null || StringUtils.isNotBlank(dto.getSearchWords())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //异步调用保存搜索记录
        ApUser user = AppThreadLocalUtil.getUser();
        if (user != null && dto.getFromIndex() == 0) {
            apUserSearchService.insert(dto.getSearchWords(), user.getId());
        }
        List<Map> maps = new ArrayList<>();
        //2设置查条件
        try {
            SearchRequest request = new SearchRequest("app_info_article");
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders.queryStringQuery(dto.getSearchWords()).field("content").defaultOperator(Operator.OR);
            boolQueryBuilder.must(queryStringQueryBuilder);
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("publishTime").lt(dto.getMinBehotTime().getTime());
            boolQueryBuilder.filter(rangeQueryBuilder);

            //3分页查询
            searchSourceBuilder.from(0);
            searchSourceBuilder.size(dto.getPageSize());
            //4按照发布时间倒序查询

            searchSourceBuilder.sort("punlishTime", SortOrder.DESC);

            //设置高亮显示  title 显示高亮
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("title")
                    .preTags("<font style='color: red; font-size: inherit;'>")
                    .postTags("</font>");

            searchSourceBuilder.highlighter(highlightBuilder);

            searchSourceBuilder.query(boolQueryBuilder);
            request.source(searchSourceBuilder);
            SearchResponse response = null;

            response = restHighLevelClient.search(request, RequestOptions.DEFAULT);


            //6。解析数据
            SearchHit[] hits = response.getHits().getHits();
            for (SearchHit hit : hits) {
                String json = hit.getSourceAsString();
                Map map = JSON.parseObject(json, Map.class);
                //处理高亮数据
                if (hit.getHighlightFields() != null && hit.getHighlightFields().size() > 0) {
                    //高亮标题
                    Text[] titles = hit.getHighlightFields().get("title").getFragments();
                    String title = StringUtils.join(titles);
                    map.put("h_title", title);
                } else {
                    //原始标题
                    map.put("h_title", map.get("title"));
                }
                maps.add(map);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseResult.okResult(maps);
    }
}
