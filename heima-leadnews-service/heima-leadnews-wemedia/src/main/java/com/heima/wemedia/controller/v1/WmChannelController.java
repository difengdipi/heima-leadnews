package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.ChannelDto;
import com.heima.model.wemedia.pojos.WmChannel;
import com.heima.wemedia.service.WmChannelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: 周海
 * @Create : 2024/3/26
 **/
@RestController
@RequestMapping("/api/v1/channel")
@Api(tags = "频道管理")
public class WmChannelController {

    @Autowired
    private WmChannelService wmChannelService;

    @GetMapping("/channels")
    public ResponseResult findAll() {
        return wmChannelService.findAll();
    }

    @PostMapping("/save")
    @ApiOperation("保存频道")
    public ResponseResult insert(@RequestBody WmChannel wmChannel) {
        return wmChannelService.insert(wmChannel);
    }

    @PostMapping("/list")
    public ResponseResult list(@RequestBody ChannelDto dto) {
        return wmChannelService.list(dto);
    }


    @PostMapping("/update")
    public ResponseResult update(@RequestBody WmChannel wmChannel) {
        return wmChannelService.update(wmChannel);

    }


}


