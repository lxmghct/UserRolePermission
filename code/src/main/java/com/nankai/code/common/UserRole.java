package com.nankai.code.common;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserRole implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer userId;
    private Integer roleId;
    private Date createTime;
}
