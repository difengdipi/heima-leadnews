package heima.leadnews.admin.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import heima.leadnews.admin.pojos.dto.AdloginDto;
import heima.leadnews.admin.service.AdUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: 周海
 * @Create : 2024/4/8
 **/
@RestController
@RequestMapping("/login")
@Api(tags = "管理端登录文档")
public class logincontroller {

    @Autowired
    private AdUserService adUserService;

    @PostMapping("/in")
    @ApiOperation("管理端用户登录")
    public ResponseResult login(@RequestBody AdloginDto adloginDto) {
        return adUserService.login(adloginDto);
    }
}
