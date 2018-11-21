package com.company.appname.app.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description="Simple detail about the user.")
public class UserSimpleDTO {

    @ApiModelProperty(notes = "User identifier")
    private Long id;

    @ApiModelProperty(notes = "The name of the user")
    private String name;

}
