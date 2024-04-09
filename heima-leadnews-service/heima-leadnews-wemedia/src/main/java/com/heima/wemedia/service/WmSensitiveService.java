package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.ChannelDto;
import com.heima.model.wemedia.pojos.WmSensitive;

/**
 * @author: 周海
 * @Create : 2024/4/9
 **/
public interface WmSensitiveService extends IService<WmSensitive> {
    /**
     * 删除敏感词
     *
     * @param id
     * @return
     */
    ResponseResult del(Integer id);

    /**
     * 查询敏感词列表
     *
     * @param dto
     * @return
     */
    ResponseResult list(ChannelDto dto);

    /**
     * 更新敏感词
     *
     * @param wmSensitive
     * @return
     */
    ResponseResult update(WmSensitive wmSensitive);

    /**
     * 添加敏感词
     *
     * @param wmSensitive
     * @return
     */

    ResponseResult insert(WmSensitive wmSensitive);
}
