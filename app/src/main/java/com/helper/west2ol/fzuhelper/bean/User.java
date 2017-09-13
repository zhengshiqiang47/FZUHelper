package com.helper.west2ol.fzuhelper.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by CoderQiang on 2017/9/13.
 */
@Entity
public class User {
    @Id(autoincrement = true)
    private Long  userId;

    private String FzuAccount;
    private String FzuPasssword;

    @Generated(hash = 822298545)
    public User(Long userId, String FzuAccount, String FzuPasssword) {
        this.userId = userId;
        this.FzuAccount = FzuAccount;
        this.FzuPasssword = FzuPasssword;
    }

    @Generated(hash = 586692638)
    public User() {
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
}
