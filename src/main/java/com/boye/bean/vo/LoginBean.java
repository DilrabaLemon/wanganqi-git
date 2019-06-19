package com.boye.bean.vo;

import com.boye.base.entity.BaseEntity;

import java.io.Serializable;

public class LoginBean extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -1950854292611300829L;

	private String login_number;

    private String password;

    public LoginBean() {
    }

    public String getLogin_number() {
        return login_number;
    }

    public void setLogin_number(String login_number) {
        this.login_number = login_number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
