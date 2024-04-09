package com.heima.search.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: 周海
 * @Create : 2024/4/7
 **/

@RestController
@RequestMapping(" /api/v1/associate")
@Api(tags = "文章相关性搜索接口文档")
public class AssociateController {


    @PostMapping("/search")
    @ApiOperation("文章搜索提示接口")
    public ResponseResult search(@RequestBody String msg) {
        return null;
    }

}
