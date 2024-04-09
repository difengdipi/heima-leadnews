package heima.leadnews.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import heima.leadnews.admin.pojos.AdUser;
import heima.leadnews.admin.pojos.dto.AdloginDto;

/**
 * @author: 周海
 * @Create : 2024/4/8
 **/
public interface AdUserService extends IService<AdUser> {
    /**
     * 管理端用户登录
     *
     * @param adloginDto
     * @return
     */
    public ResponseResult login(AdloginDto adloginDto);
}
