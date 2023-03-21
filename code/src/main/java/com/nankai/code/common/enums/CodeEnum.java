package com.nankai.code.common.enums;

import lombok.Getter;

@Getter
public enum CodeEnum {
    //状态码按顺序依次往下写
    //作用就是吧所有的值都枚举出来，到时候用的时候直接导就行
    SUCCESS(200, "成功"),
    ERROR(400, "失败");
    Integer code;
    String desc;

    CodeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
