package core_lib.domainbean_model.local_news;

import core_lib.global_data_cache.GlobalConstantTools;

/**
 * 本地新闻接口
 * Created by zhihuatang on 15/11/28.
 */
public class LocalNewsNetRequestBean {
    // 接口类型
    private final GlobalConstantTools.LocalNewsTypeEnum typeEnum;
    // 排序类型
    private final GlobalConstantTools.LocalNewsSortTypeEnum sortEnum;
    // 当前分页号码
    private final int page;
    // 当前分页数据最大数量
    private final int total;

    public LocalNewsNetRequestBean(GlobalConstantTools.LocalNewsTypeEnum typeEnum, GlobalConstantTools.LocalNewsSortTypeEnum sortEnum, int page, int total) {
        this.typeEnum = typeEnum;
        this.sortEnum = sortEnum;
        this.page = page;
        this.total = total;
    }

    public GlobalConstantTools.LocalNewsTypeEnum getTypeEnum() {
        return typeEnum;
    }

    public GlobalConstantTools.LocalNewsSortTypeEnum getSortEnum() {
        return sortEnum;
    }

    public int getPage() {
        return page;
    }

    public int getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return "LocalNewsNetRequestBean{" +
                "typeEnum=" + typeEnum +
                ", sortEnum=" + sortEnum +
                ", page=" + page +
                ", total=" + total +
                '}';
    }
}
