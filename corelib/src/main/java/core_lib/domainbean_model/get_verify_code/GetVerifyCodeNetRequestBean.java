package core_lib.domainbean_model.get_verify_code;

import core_lib.global_data_cache.GlobalConstantTools;

/**
 * Created by zhihuatang on 15/11/28.
 */
public class GetVerifyCodeNetRequestBean {
    // 手机号码
    private final String phoneNumber;
    // 验证码类型
    private final GlobalConstantTools.VerifyTypeEnum verifyTypeEnum;

    public GetVerifyCodeNetRequestBean(String phoneNumber, GlobalConstantTools.VerifyTypeEnum verifyTypeEnum) {
        this.phoneNumber = phoneNumber;
        this.verifyTypeEnum = verifyTypeEnum;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public GlobalConstantTools.VerifyTypeEnum getVerifyTypeEnum() {
        return verifyTypeEnum;
    }

    @Override
    public String toString() {
        return "VerifyCodeNetRequestBean{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", verifyTypeEnum=" + verifyTypeEnum +
                '}';
    }
}
