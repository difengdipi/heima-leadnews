package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dtos.ChannelDto;
import com.heima.model.wemedia.pojos.WmSensitive;
import com.heima.wemedia.mapper.WmSensitiveMapper;
import com.heima.wemedia.service.WmSensitiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: 周海
 * @Create : 2024/4/9
 **/
@Service
@Slf4j
public class WmSensitiveServiceImpl extends ServiceImpl<WmSensitiveMapper, WmSensitive> implements WmSensitiveService {

    @Autowired
    private WmSensitiveMapper wmSensitiveMapper;

    /**
     * 删除敏感词
     *
     * @param id
     * @return
     */
    public ResponseResult del(Integer id) {
        log.info("删除敏感词");
        int flag = 0;
        //判断敏感词是否为空
        if (id == null) {
            return ResponseResult.errorResult(401, "Unauthorized");
        }
        try {
            flag = wmSensitiveMapper.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return flag == 0 ? ResponseResult.errorResult(200, "Content") : ResponseResult.okResult(200);
    }

    /**
     * 查询敏感词列表
     *
     * @param dto
     * @return
     */
    public ResponseResult list(ChannelDto dto) {
        log.info("查询敏感词列表");
        //判断参数
        if (dto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        IPage page = new Page(dto.getPage(), dto.getSize());
        LambdaQueryWrapper<WmSensitive> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        if (dto.getName() != null) {
            lambdaQueryWrapper.like(WmSensitive::getSensitives, dto.getName());
        }
        lambdaQueryWrapper.orderByDesc(WmSensitive::getCreatedTime);
        page = page(page, lambdaQueryWrapper);
        ResponseResult responseResult = new ResponseResult(dto.getPage(), dto.getSize(), page.getTotal());
        responseResult.setData(page.getRecords());

        return responseResult;

    }

    /**
     * 更新敏感词
     *
     * @param wmSensitive
     * @return
     */
    public ResponseResult update(WmSensitive wmSensitive) {
        //判断参数
        if (wmSensitive == null && wmSensitive.getSensitives() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //判断是否更改
        WmSensitive dbuser = wmSensitiveMapper.selectById(wmSensitive.getId());
        if (dbuser != null && dbuser.getSensitives().equals(wmSensitive.getSensitives())) {
            //未更改
            return ResponseResult.okResult(201, "Created");
        }
        //更改
        BeanUtils.copyProperties(wmSensitive, dbuser);
        //判断更改后的词数据库中是否存在
        WmSensitive dbUser2 = wmSensitiveMapper.selectOne(Wrappers.<WmSensitive>lambdaQuery().eq(WmSensitive::getSensitives, wmSensitive.getSensitives()));
        if (dbUser2 == null && dbuser != null) {
            //不存在，直接修改
            update(dbuser);
            return ResponseResult.okResult(dbuser);
        }
        //存在
        return ResponseResult.errorResult(401, "Unauthorized");
    }

    /**
     * 添加敏感词
     *
     * @param wmSensitive
     * @return
     */
    public ResponseResult insert(WmSensitive wmSensitive) {
        //检验参数
        if (wmSensitive == null) {
            return ResponseResult.errorResult(401, "Unauthorized");
        }
        //判断敏感词是否存在
        WmSensitive dbUser = wmSensitiveMapper.selectOne(Wrappers.<WmSensitive>lambdaQuery().eq(WmSensitive::getSensitives, wmSensitive.getSensitives()));
        if (dbUser != null) {
            return ResponseResult.errorResult(201, "Created");
        }
        //不存在
        int insert = wmSensitiveMapper.insert(wmSensitive);
        return insert == 1 ? ResponseResult.okResult(wmSensitive) : ResponseResult.errorResult(403, "Forbidden");
    }
}
