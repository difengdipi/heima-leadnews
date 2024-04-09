package com.heima.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.article.pojos.ApArticleConfig;

import java.util.Map;

/**
 * @author: 周海
 * @Create : 2024/4/1
 **/
public interface ApArticleConfigService extends IService<ApArticleConfig> {
    /**
     * 修改文章
     *
     * @param map
     */
    void updateByMap(Map map);
}
