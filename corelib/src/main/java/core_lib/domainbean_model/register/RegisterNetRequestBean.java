package core_lib.domainbean_model.register;

/**
 * Created by zhihuatang on 15/11/28.
 */
public class RegisterNetRequestBean {
    // 用户名(手机号码)
    private final String username;
    // 密码
    private final String password;
    // 验证码
    private final String verificationCode;

    public RegisterNetRequestBean(String username, String password, String verificationCode) {
        this.username = username;
        this.password = password;
        this.verificationCode = verificationCode;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    @Override
    public String toString() {
        return "RegisterNetRequestBean{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", verificationCode='" + verificationCode + '\'' +
                '}';
    }
}
