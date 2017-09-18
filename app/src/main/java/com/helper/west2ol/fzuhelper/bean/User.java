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

    @Generated(hash = 555467210)
    public User(Long userId, String FzuAccount, String FzuPasssword,
            boolean isLogin) {
        this.userId = userId;
        this.FzuAccount = FzuAccount;
        this.FzuPasssword = FzuPasssword;
        this.isLogin = isLogin;
    }

    @Generated(hash = 586692638)
    public User() {
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
