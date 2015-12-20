package core_lib.domainbean_model.login;

import java.io.Serializable;

/**
 * 登录
 */
public final class LoginNetRespondBean implements Serializable, Cloneable {

    private static final long serialVersionUID = -8276082554036156504L;
    // 用户手机
    private String phone;
    // 用户ID
    private String uid;
    // 用户头像
    private String userAvatar;

    @Override
    public LoginNetRespondBean clone() {
        try {
            LoginNetRespondBean cloneObject = (LoginNetRespondBean) super.clone();
            cloneObject.phone = this.phone;
            cloneObject.uid = this.uid;
            return cloneObject;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e); // won't happen
        }
    }

    public String getPhone() {
        return phone;
    }

    public String getUid() {
        return uid;
    }

    @Override
    public String toString() {
        return "LoginNetRespondBean{" +
                "phone='" + phone + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
