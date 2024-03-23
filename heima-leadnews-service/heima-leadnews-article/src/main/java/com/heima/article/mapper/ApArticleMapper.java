package com.heima.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.pojos.ApArticle;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: 周海
 * @Create : 2024/3/22
 **/
@Mapper
public interface ApArticleMapper extends BaseMapper<ApArticle> {


    /**
     * 加载文章最新列表
     * @param dto 1.加载更多 2.加载最新
     * @param type
     * @return
     */
    public List<ApArticle> loadArticleList(ArticleHomeDto dto,Short type);
}
