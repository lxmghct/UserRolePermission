package com.nankai.code.common;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode
@Accessors
@ToString
public class Permission implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
    private String permissionCode;
    private String description;
    private String system;
    private String classify;
    private Integer parentId;
    private Date createTime;
}
