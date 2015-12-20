package core_lib.toolutils.area_query;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import core_lib.global_data_cache.LocalCacheDataPathConstantTools;
import core_lib.toolutils.DebugLog;
import core_lib.toolutils.SimpleCopyFileTools;
import core_lib.toolutils.SimpleStorageUtilTools;

/**
 * Created by tangzhihua on 15/9/10.
 * 地域数据库查询工具
 */
public enum SimpleAreaQueryTools {
    getInstance;

    private final String TAG = this.getClass().getSimpleName();

    // 标志模块初始化成功
    private volatile boolean isInitSuccessed;


    // 在SD卡上面的数据库文件完整路径
    private static final String DBFilePathInSDCard = LocalCacheDataPathConstantTools.localCacheAreaDatabaseRootPathInSDCard().getPath() + "/" + MySQLHelper.DATABASE_NAME;

    // 缓存一个只读的db对象优化查询速度(这个DB对象一直不释放)
    private SQLiteDatabase readableDB;

    public void init(final Context context) {

        // AsyncTask是抽象类.AsyncTask定义了三种泛型类型 Params，Progress和Result。
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                do {
                    if (!new File(DBFilePathInSDCard).exists() && !SimpleStorageUtilTools.isExternalStoreWritable()) {
                        // SD卡上缓存的数据库文件不存在 并且 当前SD卡不能写操作
                        break;
                    }

                    // 将asserts目录下面的数据库文件拷贝到SD卡上面进行缓存
                    boolean isCopyFileSuccess = SimpleCopyFileTools.copyFileFromAssetsUseBufferedStream(context, MySQLHelper.DATABASE_NAME, DBFilePathInSDCard);
                    if (!isCopyFileSuccess || !new File(DBFilePathInSDCard).exists()) {
                        // 拷贝失败 或者 SD卡上缓存的数据库文件不存在
                        break;
                    }

                    try {
                        readableDB = new MySQLHelper(context).getReadableDatabase();
                    } catch (Exception e) {
                        // 打开数据库文件失败
                        DebugLog.e(TAG, e.getLocalizedMessage());
                        readableDB = null;
                        break;
                    }

                    return true;
                } while (false);

                return false;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                isInitSuccessed = aBoolean;
            }
        }.execute();


    }

    private final class MySQLHelper extends SQLiteOpenHelper {
        /* 数据库名 */
        private final static String DATABASE_NAME = "area.sqlite";
        /* 表名 */
        private final static String TABLE_NAME = "area";

        /* 表中的字段 */
        private final static String FIELD_ID = "ID";
        private final static String FIELD_Name = "Name";
        private final static String FIELD_ParentId = "ParentId";
        private final static String FIELD_ShortName = "ShortName";
        private final static String FIELD_LevelType = "LevelType";
        private final static String FIELD_CityCode = "CityCode";
        private final static String FIELD_ZipCode = "ZipCode";
        private final static String FIELD_MergerName = "MergerName";
        private final static String FIELD_lng = "lng";
        private final static String FIELD_Lat = "Lat";
        private final static String FIELD_Pinyin = "Pinyin";

        public MySQLHelper(final Context context) {
            super(context, DBFilePathInSDCard, null, DB_VERSION);
        }

        /**
         * 1-->2 add header table
         * 2-->3 update info
         * 3--> update info haha
         */
        public static final int DB_VERSION = 1;


        /**
         * Creates database the first time we try to open it.
         */
        @Override
        public void onCreate(final SQLiteDatabase db) {

            onUpgrade(db, 0, DB_VERSION);
        }

        /**
         * Updates the database format when a content provider is used
         * with a database that was created with a different format.
         * <p>
         * Note: to support downgrades, creating a table should always drop it first if it already
         * exists.
         */
        @Override
        public void onUpgrade(final SQLiteDatabase db, int oldV, final int newV) {

            for (int version = oldV + 1; version <= newV; version++) {
                //upgradeTo(db, version);
            }
        }

        /**
         * Upgrade database from (version - 1) to version.
         */
//        private void upgradeTo(SQLiteDatabase db, int version) {
//            switch (version) {
//                case 1:
//                    createDownloadsTable(db);
//                    break;
//                case 2:
//                    createHeadersTable(db);
//                    break;
//                case 3:
//                    addColumn(db, DB_TABLE, Downloads.Impl.COLUMN_IS_PUBLIC_API,
//                            "INTEGER NOT NULL DEFAULT 0");
//                    addColumn(db, DB_TABLE, Downloads.Impl.COLUMN_ALLOW_ROAMING,
//                            "INTEGER NOT NULL DEFAULT 0");
//                    addColumn(db, DB_TABLE, Downloads.Impl.COLUMN_ALLOWED_NETWORK_TYPES,
//                            "INTEGER NOT NULL DEFAULT 0");
//                    break;
//                case 103:
//                    addColumn(db, DB_TABLE, Downloads.Impl.COLUMN_IS_VISIBLE_IN_DOWNLOADS_UI,
//                            "INTEGER NOT NULL DEFAULT 1");
//                    makeCacheDownloadsInvisible(db);
//                    break;
//                case 4:
//                    addColumn(db, DB_TABLE, Downloads.Impl.COLUMN_BYPASS_RECOMMENDED_SIZE_LIMIT,
//                            "INTEGER NOT NULL DEFAULT 0");
//                    break;
//                default:
//                    throw new IllegalStateException("Don't know how to upgrade to " + version);
//            }
//        }
    }

    /**
     * 根据 parentId 进行查询
     *
     * @param parentId :
     * @return 查询到数据列表, 会根据 id 进行升序排序
     */
    public List<Area> queryByParentId(final int parentId) {

        List<Area> areaList = new ArrayList<>();

        try {

            if (!isInitSuccessed) {
                throw new Exception("查询地域功能模块, 还未初始化完成.");
            }

            //查询表中的数据
            Cursor cursor = readableDB.query(
                    // table name
                    MySQLHelper.TABLE_NAME,
                    // columns 数据库列名称数组 写入后最后返回的Cursor中只能查到这里的列的内容(如果参数是null,则返回所有列(不鼓励设置为null,以免防止读出的数据没有用到))
                    new String[]{
                            MySQLHelper.FIELD_ID,
                            MySQLHelper.FIELD_ParentId,
                            MySQLHelper.FIELD_Name,
                            MySQLHelper.FIELD_Pinyin,
                            MySQLHelper.FIELD_ShortName,
                            MySQLHelper.FIELD_MergerName,
                            MySQLHelper.FIELD_lng,
                            MySQLHelper.FIELD_Lat},
                    // selection 查询条件 (设置为null,返回这个table的所有行.)
                    MySQLHelper.FIELD_ParentId + "=" + parentId,
                    // selectionArgs 查询结果
                    null,
                    // groupBy 分组列
                    null,
                    // having 分组条件
                    null,
                    // orderBy 排序列
                    MySQLHelper.FIELD_ID + " ASC");

            if (cursor != null) {

                while (cursor.moveToNext()) {

                    int id = cursor.getInt(cursor.getColumnIndex(MySQLHelper.FIELD_ID));
                    int parentID = cursor.getInt(cursor.getColumnIndex(MySQLHelper.FIELD_ParentId));
                    String name = cursor.getString(cursor.getColumnIndex(MySQLHelper.FIELD_Name));
                    String pinyin = cursor.getString(cursor.getColumnIndex(MySQLHelper.FIELD_Pinyin));
                    String shortName = cursor.getString(cursor.getColumnIndex(MySQLHelper.FIELD_ShortName));
                    String mergerName = cursor.getString(cursor.getColumnIndex(MySQLHelper.FIELD_MergerName));
                    String lng = cursor.getString(cursor.getColumnIndex(MySQLHelper.FIELD_lng));
                    String lat = cursor.getString(cursor.getColumnIndex(MySQLHelper.FIELD_Lat));

                    Area area = new Area(id, parentID, name, pinyin, shortName, mergerName, lng, lat);
                    areaList.add(area);
                }

                cursor.close();//关闭结果集
            }
        } catch (Exception e) {
            DebugLog.e(TAG, e.getLocalizedMessage());

        }
        return areaList;
    }

    /**
     * 查询省
     *
     * @return
     */
    public List<Area> getProvinces() {
        return queryByParentId(100000);
    }

    /**
     * 根据 area id 查询 area模型
     *
     * @param areaId
     * @return
     */
    public Area queryAreaByID(final int areaId) {
        Area area = null;
        try {

            if (!isInitSuccessed) {
                throw new Exception("查询地域功能模块, 还未初始化完成.");
            }

            //查询表中的数据
            Cursor cursor = readableDB.query(
                    // table name
                    MySQLHelper.TABLE_NAME,
                    // columns 数据库列名称数组 写入后最后返回的Cursor中只能查到这里的列的内容(如果参数是null,则返回所有列(不鼓励设置为null,以免防止读出的数据没有用到))
                    new String[]{
                            MySQLHelper.FIELD_ID,
                            MySQLHelper.FIELD_ParentId,
                            MySQLHelper.FIELD_Name,
                            MySQLHelper.FIELD_Pinyin,
                            MySQLHelper.FIELD_ShortName,
                            MySQLHelper.FIELD_MergerName,
                            MySQLHelper.FIELD_lng,
                            MySQLHelper.FIELD_Lat},
                    // selection 查询条件 (设置为null,返回这个table的所有行.)
                    MySQLHelper.FIELD_ID + "=" + areaId,
                    // selectionArgs 查询结果
                    null,
                    // groupBy 分组列
                    null,
                    // having 分组条件
                    null,
                    // orderBy 排序列
                    MySQLHelper.FIELD_ID + " ASC");

            if (cursor != null && cursor.moveToNext()) {

                int id = cursor.getInt(cursor.getColumnIndex(MySQLHelper.FIELD_ID));
                int parentID = cursor.getInt(cursor.getColumnIndex(MySQLHelper.FIELD_ParentId));
                String name = cursor.getString(cursor.getColumnIndex(MySQLHelper.FIELD_Name));
                String pinyin = cursor.getString(cursor.getColumnIndex(MySQLHelper.FIELD_Pinyin));
                String shortName = cursor.getString(cursor.getColumnIndex(MySQLHelper.FIELD_ShortName));
                String mergerName = cursor.getString(cursor.getColumnIndex(MySQLHelper.FIELD_MergerName));
                String lng = cursor.getString(cursor.getColumnIndex(MySQLHelper.FIELD_lng));
                String lat = cursor.getString(cursor.getColumnIndex(MySQLHelper.FIELD_Lat));

                area = new Area(id, parentID, name, pinyin, shortName, mergerName, lng, lat);

                cursor.close();//关闭结果集
            }
        } catch (Exception e) {
            DebugLog.e(TAG, e.getLocalizedMessage());
        }

        return area;
    }
}
