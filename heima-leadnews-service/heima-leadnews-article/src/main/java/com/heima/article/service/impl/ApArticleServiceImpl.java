package com.heima.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.service.ApArticleService;
import com.heima.common.constants.ArticleConstants;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.common.dtos.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author: 周海
 * @Create : 2024/3/22
 **/
@Service
@Transactional
@Slf4j
public class ApArticleServiceImpl extends ServiceImpl<ApArticleMapper, ApArticle>  implements ApArticleService{

    @Resource
    private  ApArticleMapper apArticleMapper;

    private final static short MAX_PAGE_SIZE = 50;

    /**
     * 加载文章列表
     * @param articleHomeDto
     * @param type
     * @return
     */
    public ResponseResult load(ArticleHomeDto articleHomeDto,Short type){
        //1.进行参数的校验
        Integer size = articleHomeDto.getSize();
        if (size == null || size == 0) {
            size = 10 ;
        }
        //分页查询的参数
        size = Math.min(size,50);
        //校验类型
        if (!type.equals(ArticleConstants.LOADTYPE_LOAD_MORE) && !type.equals(ArticleConstants.LOADTYPE_LOAD_NEW)) {
            type = ArticleConstants.LOADTYPE_LOAD_MORE;
        }
        //频道参数的校验
        if (StringUtils.isBlank(articleHomeDto.getTag())) {
            articleHomeDto.setTag(ArticleConstants.DEFAULT_TAG);
        }
        //时间的校验
        if (articleHomeDto.getMaxBehotTime() == null) articleHomeDto.setMaxBehotTime(new Date());
        if (articleHomeDto.getMinBehotTime() == null) articleHomeDto.setMinBehotTime(new Date());
        //2.查询
        List<ApArticle> apArticles = apArticleMapper.loadArticleList(articleHomeDto, type);
        //3.返回结果
        return ResponseResult.okResult(apArticles);
    }
}
