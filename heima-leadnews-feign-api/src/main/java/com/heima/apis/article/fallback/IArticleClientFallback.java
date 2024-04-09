package com.heima.apis.article.fallback;

import com.heima.apis.article.IAritcleClient;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import org.springframework.stereotype.Component;

/**
 * @author: 周海
 * @Create : 2024/3/27
 **/
@Component
public class IArticleClientFallback implements IAritcleClient {

    public ResponseResult saveArticle(ArticleDto dto) {
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR, "获取数据失败");
    }

}
