package core_lib.domainbean_model.local_news;

import java.util.Calendar;

/**
 * Created by Cxy on 2015/12/1.
 */
public class News {
    private final String id;
    // 封面图片URL
    private final String img;
    // 标题
    private final String title;
    // 摘要
    private final String digest;
    // 被关注数量
    private final int follows;
    // 被举报数量
    private final int report;
    // 被评论数量
    private final int comments;
    // 被分享数量
    private final int shares;
    // 发布时间
    private final long time;
    // 详情URL
    private final String detailUrl;

    public News(String id, String img, String title, String digest, int follows, int report, int comments, int shares, long time, String detailUrl) {
        this.id = id;
        this.img = img;
        this.title = title;
        this.digest = digest;
        this.follows = follows;
        this.report = report;
        this.comments = comments;
        this.shares = shares;
        this.time = time;
        this.detailUrl = detailUrl;
    }

    public String getId() {
        return id;
    }

    public String getImg() {
        return img;
    }

    public String getTitle() {
        return title;
    }

    public String getDigest() {
        return digest;
    }

    public int getFollows() {
        return follows;
    }

    public int getReport() {
        return report;
    }

    public int getComments() {
        return comments;
    }

    public int getShares() {
        return shares;
    }

    public Calendar getTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar;
    }

    @Override
    public String toString() {
        return "News{" +
                "id='" + id + '\'' +
                ", img='" + img + '\'' +
                ", title='" + title + '\'' +
                ", digest='" + digest + '\'' +
                ", follows=" + follows +
                ", report=" + report +
                ", comments=" + comments +
                ", shares=" + shares +
                ", time=" + getTime() +
                ", detailUrl='" + detailUrl + '\'' +
                '}';
    }
}
