package com.heima.search.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.search.dto.UserSearchDto;
import com.heima.search.service.Impl.ArticleSearchImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: 周海
 * @Create : 2024/4/7
 **/
@RestController
@RequestMapping("/api/v1/article/search")
@Api(tags = "搜索文章相关接口文档")
@Slf4j
public class ArticleSearchContoller {
    @Autowired
    private ArticleSearchImpl articleSearch;

    @PostMapping("/search")
    @ApiOperation("搜索文章接口")
    public ResponseResult search(@RequestBody UserSearchDto dto) {
        log.info("搜索文章");
        return articleSearch.search(dto);
    }

}
