package core_lib.toolutils.area_query;

/**
 * Created by tangzhihua on 15/9/10.
 */
public class Area {
    // id
    private final int id;
    //
    private final int parentId;
    // 例 : 和平区
    private final String name;
    // 例 : Heping
    private final String pinyin;
    // 例 : 和平区
    private final String shortName;
    // 例 : 中国,天津,天津市,和平区
    private final String mergerName;
    // 例 : 117.21456
    private final String lng;// 经度
    // 例 : 39.11718
    private final String lat;// 纬度

    public Area(int id, int parentId, String name, String pinyin, String shortName, String mergerName, String lng, String lat) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.pinyin = pinyin;
        this.shortName = shortName;
        this.mergerName = mergerName;
        this.lng = lng;
        this.lat = lat;
    }

    public int getId() {
        return id;
    }

    public int getParentId() {
        return parentId;
    }

    public String getName() {
        return name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public String getShortName() {
        return shortName;
    }

    public String getMergerName() {
        return mergerName;
    }

    public String getLng() {
        return lng;
    }

    public String getLat() {
        return lat;
    }

    @Override
    public String toString() {
        return "Area{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                ", pinyin='" + pinyin + '\'' +
                ", shortName='" + shortName + '\'' +
                ", mergerName='" + mergerName + '\'' +
                ", lng='" + lng + '\'' +
                ", lat='" + lat + '\'' +
                '}';
    }
}
