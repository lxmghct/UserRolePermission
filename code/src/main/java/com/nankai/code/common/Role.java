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
public class Role implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
    private String description;
    private Integer status;
    private Date createTime;
}
