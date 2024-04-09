package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmMaterialDto;
import com.heima.wemedia.service.WmMaterialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: 周海
 * @Create : 2024/3/24
 **/
@RestController
@Api(tags = "自媒体相关接口")
@RequestMapping("/api/v1/material")
public class WmMaterialController {

    @Autowired
    private WmMaterialService wmMaterialService;

    /**
     * 素材上传图片
     *
     * @param multipartFile
     * @return
     */
    @ApiOperation("上传图片")
    @PostMapping("/upload_picture")
    public ResponseResult uploadPictures(MultipartFile multipartFile) {
        return wmMaterialService.uploadPicature(multipartFile);
    }

    @ApiOperation("图片素材管理")
    @PostMapping("/list")
    public ResponseResult findList(@RequestBody WmMaterialDto materialDto) {
        return wmMaterialService.findList(materialDto);
    }

    /**
     * 素材图片删除
     *
     * @param id
     * @return
     */
    @ApiOperation("图片删除")
    @GetMapping("/del_picture/{id}")
    public ResponseResult delPictures(@PathVariable Integer id) {
        return wmMaterialService.delPictures(id);
    }

    /**
     * 图片取消收藏
     *
     * @param id
     * @return
     */
    @ApiOperation("图片取消收藏")
    @GetMapping("/cancel_collect/{id}")
    public ResponseResult canCollect(@RequestBody Integer id) {
        return wmMaterialService.updateCollectById(id);
    }

    @ApiOperation("图片收藏")
    @GetMapping("/collect/{id}")
    public ResponseResult collect(@PathVariable Integer id) {
        return wmMaterialService.updateCollectById(id);
    }


}
