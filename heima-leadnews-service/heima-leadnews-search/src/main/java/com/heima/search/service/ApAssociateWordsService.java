package com.heima.search.service;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.search.dto.UserSearchDto;

/**
 * @author: 周海
 * @Create : 2024/4/8
 **/
public interface ApAssociateWordsService {

    /**
     * 联想词
     *
     * @param userSearchDto
     * @return
     */
    ResponseResult findAssociate(UserSearchDto userSearchDto);
}
