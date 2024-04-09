package com.heima.search.service;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.search.dto.UserSearchDto;

/**
 * @author: 周海
 * @Create : 2024/4/7
 **/
public interface ArticleSearch {
    /**
     * es文章分页检索
     *
     * @param dto
     * @return
     */
    public ResponseResult search(UserSearchDto dto);

}
