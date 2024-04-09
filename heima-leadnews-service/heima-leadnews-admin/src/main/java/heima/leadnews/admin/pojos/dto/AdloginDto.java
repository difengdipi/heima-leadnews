package heima.leadnews.admin.pojos.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: 周海
 * @Create : 2024/4/8
 **/
@Data
public class AdloginDto {
    /**
     * 手机号
     */
    @ApiModelProperty(value = "用户名", required = true)
    private String name;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码", required = true)
    private String password;
}
