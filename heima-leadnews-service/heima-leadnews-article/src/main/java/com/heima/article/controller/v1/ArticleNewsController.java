package com.heima.article.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: 周海
 * @Create : 2024/3/28
 **/
@Api(tags = "文章管理相关接口文档")
@RestController
@RequestMapping("/api/v1/news")
public class ArticleNewsController {


    //TODO:需要实现
    @ApiOperation("查看详情")
    @GetMapping("/one/{id}")
    public ResponseResult One(@PathVariable Integer id) {
        return null;
    }

}
