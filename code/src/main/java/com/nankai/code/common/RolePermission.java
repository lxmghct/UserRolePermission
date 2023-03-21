package com.nankai.code.common;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ToString
public class RolePermission implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer roleId;
    private Integer permissionId;
    private Date createTime;
}
