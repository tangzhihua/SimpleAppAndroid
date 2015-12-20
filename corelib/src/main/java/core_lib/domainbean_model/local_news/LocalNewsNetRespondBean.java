package core_lib.domainbean_model.local_news;

import java.util.ArrayList;
import java.util.List;

/**
 * 本地新闻接口
 * Created by zhihuatang on 15/11/28.
 */
public class LocalNewsNetRespondBean {
    private final List<News> list = new ArrayList<>();

    public LocalNewsNetRespondBean(List<News> list) {
        this.list.addAll(list);
    }

    public List<News> getList() {
        return list;
    }

    @Override
    public String toString() {
        return "LocalNewsNetRespondBean{" +
                "list=" + list +
                '}';
    }
}
