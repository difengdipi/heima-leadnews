package heima.leadnews.admin.service.Impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.utils.common.AppJwtUtil;
import heima.leadnews.admin.mapper.AdUserMapper;
import heima.leadnews.admin.pojos.AdUser;
import heima.leadnews.admin.pojos.dto.AdloginDto;
import heima.leadnews.admin.service.AdUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: 周海
 * @Create : 2024/4/8
 **/
@Service
@Slf4j
public class AdUserServiceImpl extends ServiceImpl<AdUserMapper, AdUser> implements AdUserService {
    /**
     * 管理端用户登录
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult login(AdloginDto dto) {
        log.info("管理端用户登录：{}", dto);
        //1.正常登录 用户名和密码
        if (StringUtils.isNotBlank(dto.getName()) && StringUtils.isNotBlank(dto.getPassword())) {
            //1.1 根据用户名查询用户信息
            AdUser dbUser = getOne(Wrappers.<AdUser>lambdaQuery().eq(AdUser::getName, dto.getName()));
            if (dbUser == null) {
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "用户信息不存在");
            }
            //1.2 比对密码
            String salt = dbUser.getSalt();
            String password = dto.getPassword();

            String pswd = DigestUtils.md5DigestAsHex((password + salt).getBytes());
            if (!pswd.equals(dbUser.getPassword())) {
                return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
            }
            //1.3 返回数据  jwt  user
            String token = AppJwtUtil.getToken(Long.valueOf(dbUser.getId()));

            Map<String, Object> map = new HashMap<>();
            map.put("token", token);
            dbUser.setSalt("");
            dbUser.setPassword("");
            //更新最后的登录时间
            dbUser.setLoginTime(new Date());

            map.put("user", dbUser);

            return ResponseResult.okResult(map);
        } else {
            //2.验证失败
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
    }
}
