package com.heima.model.wemedia.dtos;

import com.heima.model.common.dtos.PageRequestDto;
import lombok.Data;

/**
 * @author: 周海
 * @Create : 2024/4/9
 **/

@Data
public class ChannelDto extends PageRequestDto {
    private String name;
}
