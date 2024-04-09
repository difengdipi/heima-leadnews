package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.ChannelDto;
import com.heima.model.wemedia.pojos.WmChannel;

public interface WmChannelService extends IService<WmChannel> {

    /**
     * 查询所有频道
     *
     * @return
     */
    public ResponseResult findAll();


    /**
     * 新增频道
     *
     * @param wmChannel
     * @return
     */
    ResponseResult insert(WmChannel wmChannel);

    /**
     * 频道名称模糊分页查询
     *
     * @param dto
     * @return
     */
    ResponseResult list(ChannelDto dto);

    /**
     * 修改频道
     *
     * @param wmChannel
     * @return
     */
    ResponseResult update(WmChannel wmChannel);
}