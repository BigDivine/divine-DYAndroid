package com.divine.yang.main.bean;

/**
 * Project Name  : DivinePlatform
 * Package       : com.divine.yang.main.bean
 * Author        : Divine.Yang(Divine)
 * Email         : 
 * Create Date   : 2024/12/6
 * Description   :
 */
public class UserBean {
    private String userName, userOrg;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserOrg() {
        return userOrg;
    }

    public void setUserOrg(String userOrg) {
        this.userOrg = userOrg;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "userName='" + userName + '\'' +
                ", userOrg='" + userOrg + '\'' +
                '}';
    }
}
