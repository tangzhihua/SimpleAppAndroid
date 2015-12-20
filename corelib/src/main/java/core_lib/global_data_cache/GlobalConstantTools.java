package core_lib.global_data_cache;


/**
 * 应用中使用到的常量定义
 *
 * @author zhihua.tang
 */
public final class GlobalConstantTools {

    private GlobalConstantTools() {
        throw new AssertionError("这个是一个工具类, 不能创建实例对象.");
    }

    /**
     * 应用名称
     */
    public final static String APP_NAME = "ZhangXinSheQu";

    /**
     * 用户类型
     */
    public enum UserTypeEnum {
        PuTong(0, "普通用户"),
        DanWei(1, "单位用户"),
        ShangJia(2, "商家用户"),
        FuWu(3, "服务用户"),
        KuaiDi(4, "快递用户");

        private int code;
        private String description;

        UserTypeEnum(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public int getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 验证码类型
     */
    public enum VerifyTypeEnum {
        Register(0, "普通用户"),
        ForgetPassword(1, "找回密码");

        private int code;
        private String description;

        VerifyTypeEnum(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public int getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 本地新闻接口中的type(用于标识 是请求 本地新闻 还是 趣闻分享)
     */
    public enum LocalNewsTypeEnum {
        LocalNews(0, "本地新闻"),
        CuriositiesShare(1, "趣闻分享");

        private int code;
        private String description;

        LocalNewsTypeEnum(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public int getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 本地新闻列表中的排序类型
     */
    public enum LocalNewsSortTypeEnum {
        date(0, "按日期排序"),
        follow(1, "按关注排序"),
        share(2, "按分享排序"),
        comment(3, "按评论排序");

        private int code;
        private String description;

        LocalNewsSortTypeEnum(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public int getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

        public static LocalNewsSortTypeEnum valueOfDescription(String description) {

            for (LocalNewsSortTypeEnum item : LocalNewsSortTypeEnum.values()) {
                if (item.description.equals(description)) {
                    return item;
                }
            }

            return null;
        }
    }
}