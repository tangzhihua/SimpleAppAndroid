package core_lib.domainbean_model.login;

/**
 * 登录
 *
 * @author zhihua.tang
 */
public final class LoginNetRequestBean {
    // 用户名(手机号码)
    private final String username;
    // 密码
    private final String password;

    public LoginNetRequestBean(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "LoginNetRequestBean [username=" + username + ", password=" + password + "]";
    }

}
