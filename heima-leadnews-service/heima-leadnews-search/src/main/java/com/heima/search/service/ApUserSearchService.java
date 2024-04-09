package com.heima.search.service;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.search.pojos.dto.HistorySearchDto;

/**
 * @author: 周海
 * @Create : 2024/4/8
 **/
public interface ApUserSearchService {
    /**
     * 保存用户搜索记录
     *
     * @param Keyword
     * @param UserId
     */
    public void insert(String Keyword, Integer UserId);

    /**
     * 查询搜索历史
     *
     * @return
     */
    public ResponseResult findUserSearch();

    /**
     * 删除搜索历史
     *
     * @param historySearchDto
     * @return
     */
    public ResponseResult delUserSearch(HistorySearchDto historySearchDto);

}
