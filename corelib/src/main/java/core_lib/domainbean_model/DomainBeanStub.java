package core_lib.domainbean_model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import core_lib.domainbean_model.local_news.LocalNewsNetRequestBean;
import core_lib.domainbean_model.local_news.LocalNewsNetRespondBean;
import core_lib.domainbean_model.local_news.News;

public final class DomainBeanStub {

    public static boolean isUseStubDomainBean = true;

    private DomainBeanStub() {

    }

    public static Object getNetRespondBeanStubByNetRequestBean(final Object netRequestBean) {

        if (netRequestBean instanceof LocalNewsNetRequestBean) {
            // 增加一个模拟的数据源
            List<News> dataSource = new ArrayList<>();
            Random random = new Random();
            for (int i = 0; i < 5; i++) {
                final String id = (i + ((LocalNewsNetRequestBean) netRequestBean).getPage() * 5) + "";
                // 封面图片URL
                final String img = "";
                // 标题
                final String title = "新闻 " + (i + ((LocalNewsNetRequestBean) netRequestBean).getPage() * 5);
                // 摘要
                final String digest = "摘要 " + (i + ((LocalNewsNetRequestBean) netRequestBean).getPage() * 5);
                // 被关注数量
                int follows = Math.abs(random.nextInt()) % 1000;
                // 被举报数量
                final int report = Math.abs(random.nextInt()) % 1000;
                // 被评论数量
                int comments = Math.abs(random.nextInt()) % 1000;
                // 被分享数量
                int shares = Math.abs(random.nextInt()) % 1000;
                switch (((LocalNewsNetRequestBean) netRequestBean).getSortEnum()) {
                    case date:
                        break;
                    case follow:
                        follows = 1000;
                        break;
                    case share:
                        shares = 1000;
                        break;
                    case comment:
                        comments = 1000;
                        break;
                }
                // 发布时间
                final long time = new Date().getTime();
                // 详情URL
                final String detailUrl = "";
                News xinwen = new News(id, img, title, digest, follows, report, comments, shares, time, detailUrl);

                dataSource.add(xinwen);
            }
            return new LocalNewsNetRespondBean(dataSource);
        }
        return null;
    }


}
