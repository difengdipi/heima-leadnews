package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.ChannelDto;
import com.heima.model.wemedia.pojos.WmSensitive;
import com.heima.wemedia.service.WmSensitiveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: 周海
 * @Create : 2024/4/9
 **/
@RestController
@RequestMapping("/api/v1/sensitive")
@Api(tags = "敏感词相关接口文档")
public class WmSensitiveController {

    @Autowired
    private WmSensitiveService wmSensitiveService;

    @DeleteMapping("/del/{id}")
    @ApiOperation("删除敏感词")
    public ResponseResult del(@PathVariable("id") Integer id) {
        return wmSensitiveService.del(id);
    }


    @PostMapping("/list")
    @ApiOperation("查询列表")
    public ResponseResult list(@RequestBody ChannelDto dto) {
        return wmSensitiveService.list(dto);
    }

    @PostMapping("/save")
    public ResponseResult insert(@RequestBody WmSensitive wmSensitive) {
        return wmSensitiveService.insert(wmSensitive);
    }

    @PostMapping("/update")
    public ResponseResult update(@RequestBody WmSensitive wmSensitive) {
        return wmSensitiveService.update(wmSensitive);
    }


}
