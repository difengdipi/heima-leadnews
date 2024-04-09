package com.heima.article.service;

import com.heima.model.article.pojos.ApArticle;

/**
 * @author: 周海
 * @Create : 2024/3/28
 **/
public interface ArticleFreemarkerService {

    /**
     * 生成静态文件上传到minio中
     *
     * @param apArticle
     * @param content
     */
    public void buildArticleToMinIO(ApArticle apArticle, String content);
}
