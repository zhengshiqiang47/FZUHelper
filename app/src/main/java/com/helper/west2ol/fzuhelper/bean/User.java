package com.helper.west2ol.fzuhelper.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created by CoderQiang on 2017/9/13.
 */
@Entity
public class User{
    @Id(autoincrement = true)
    private Long  userId;

    private String FzuAccount;
    private String FzuPasssword;
    private boolean isLogin;
    private String name;
    private String xymc;
    private String zymc;
    private String sex;
    private String nianji;
    private String banji;
    private String phone;

    @Generated(hash = 851377206)
    public User(Long userId, String FzuAccount, String FzuPasssword,
            boolean isLogin, String name, String xymc, String zymc, String sex,
            String nianji, String banji, String phone) {
        this.userId = userId;
        this.FzuAccount = FzuAccount;
        this.FzuPasssword = FzuPasssword;
        this.isLogin = isLogin;
        this.name = name;
        this.xymc = xymc;
        this.zymc = zymc;
        this.sex = sex;
        this.nianji = nianji;
        this.banji = banji;
        this.phone = phone;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNianji() {
        return nianji;
    }

    public void setNianji(String nianji) {
        this.nianji = nianji;
    }

    public String getBanji() {
        return banji;
    }

    public void setBanji(String banji) {
        this.banji = banji;
    }

    public String getXymc() {
        return xymc;
    }

    public void setXymc(String xymc) {
        this.xymc = xymc;
    }

    public String getZymc() {
        return zymc;
    }

    public void setZymc(String zymc) {
        this.zymc = zymc;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public Long  getUserId() {
        return userId;
    }

    public void setUserId(Long  userId) {
        this.userId = userId;
    }

    public String getFzuAccount() {
        return FzuAccount;
    }

    public void setFzuAccount(String fzuAccount) {
        FzuAccount = fzuAccount;
    }

    public String getFzuPasssword() {
        return FzuPasssword;
    }

    public void setFzuPasssword(String fzuPasssword) {
        FzuPasssword = fzuPasssword;
    }

    public boolean getIsLogin() {
        return this.isLogin;
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }
}
