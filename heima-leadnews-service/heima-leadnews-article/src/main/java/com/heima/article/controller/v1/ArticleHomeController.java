package com.heima.article.controller.v1;

import com.heima.article.service.ApArticleService;
import com.heima.common.constants.ArticleConstants;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author: 周海
 * @Create : 2024/3/22
 **/
@RestController
@RequestMapping("/api/v1/article")
@Api(tags = "文章项目接口文档")
@Slf4j
public class ArticleHomeController {
    @Resource
    private ApArticleService apArticleService;

    @ApiOperation("加载首页接口")
    @PostMapping("/load")
    public ResponseResult load(@RequestBody ArticleHomeDto articleHomeDto){
        log.info("加载首页");
        return apArticleService.load(articleHomeDto, ArticleConstants.LOADTYPE_LOAD_MORE);
    }
    @ApiOperation("加载更多接口")
    @PostMapping("/loadmore")
    public ResponseResult loadmore(@RequestBody ArticleHomeDto articleHomeDto){
        log.info("加载更多");

        return apArticleService.load(articleHomeDto,ArticleConstants.LOADTYPE_LOAD_MORE);
    }
    @ApiOperation("加载最新接口")
    @PostMapping("/loadnew")
    public ResponseResult loadnew(@RequestBody ArticleHomeDto articleHomeDto){
        log.info("加载最新");

        return apArticleService.load(articleHomeDto,ArticleConstants.LOADTYPE_LOAD_NEW);
    }




}
