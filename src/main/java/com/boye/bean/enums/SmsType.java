package com.boye.bean.enums;

import com.boye.common.enums.IntValueEnum;

public enum SmsType  implements IntValueEnum {

    //查询成功

    VERCODE(1,"验证码"),

    ;

    public int value;
    public String text;

    SmsType(int value, String text) {
        this.value = value;
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
