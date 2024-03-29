package com.heima.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author: 周海
 * @Create : 2024/3/22
 **/
public interface ApArticleService extends IService<ApArticle> {

    public ResponseResult load(ArticleHomeDto dto, Short type);

    /**
     * 保存App端相关文章
     * @param dto
     * @return
     */
    public ResponseResult saveArticle(ArticleDto dto);


}
