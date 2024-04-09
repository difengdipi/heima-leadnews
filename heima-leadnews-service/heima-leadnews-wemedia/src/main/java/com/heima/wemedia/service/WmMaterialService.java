package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmMaterialDto;
import com.heima.model.wemedia.pojos.WmMaterial;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: 周海
 * @Create : 2024/3/24
 **/
public interface WmMaterialService extends IService<WmMaterial> {
    /**
     * 素材图片上传
     *
     * @param multipartFile
     * @return
     */
    ResponseResult uploadPicature(MultipartFile multipartFile);

    /**
     * 素材管理
     *
     * @param materialDto
     * @return
     */
    ResponseResult findList(WmMaterialDto materialDto);

    /**
     * 素材图片删除
     *
     * @param id
     * @return
     */
    ResponseResult delPictures(Integer id);

    /**
     * 图片取消收藏或收藏
     *
     * @param id 参数1 收藏 0取消
     * @return
     */
    ResponseResult updateCollectById(Integer id);
}
