package cn.skyduck.huangzitao.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;


import cn.skyduck.huangzitao.R;
import cn.skyduck.huangzitao.fragment.HomePageFragment;
import cn.skyduck.huangzitao.fragment.UserCenterFragment;
import core_lib.toolutils.ToolsFunctionForThisProgect;

public class MainActivity extends FragmentActivity {

    // 定义FragmentTabHost对象
    private FragmentTabHost tabHost;

    /**
     * 主界面中, tabhost控件的tab tag
     *
     * @author zhihua.tang
     */
    public enum MainTabHostTagEnum {
        // 服务
        HomePage(0, R.string.homepage, HomePageFragment.class, R.drawable.selector_mainactivity_tabspec_indicator_homepage),
        // 我的
        UserCenter(1, R.string.user_center, UserCenterFragment.class, R.drawable.selector_mainactivity_tabspec_indicator_usercenter);

        private int tabIndex;
        private int tabName;
        private Class<?> tabClass;
        private int tabIconResID;

        private MainTabHostTagEnum(int tabIndex, int tabName, Class<?> tabClass, int tabIconResID) {
            this.tabIndex = tabIndex;
            this.tabName = tabName;
            this.tabClass = tabClass;
            this.tabIconResID = tabIconResID;
        }

        public int getTabIndex() {
            return tabIndex;
        }

        public int getTabName() {
            return tabName;
        }

        public Class<?> getTabClass() {
            return tabClass;
        }

        public int getTabIconResID() {
            return tabIconResID;
        }

        /**
         * @param tabIndex
         * @return
         */
        public static MainTabHostTagEnum valueOfTabIndex(int tabIndex) {
            for (MainTabHostTagEnum item : MainTabHostTagEnum.values()) {
                if (item.tabIndex == tabIndex) {
                    return item;
                }
            }

            return null;
        }

    }

    /**
     * 根据tag切换tabhost控件
     *
     * @param tagEnum
     */
    public void changeTabHostByTag(MainTabHostTagEnum tagEnum) {
        tabHost.setCurrentTabByTag(tagEnum.name());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    /**
     * 初始化组件
     */
    private void initView() {

        // 实例化TabHost对象，得到TabHost
        tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        // 去掉tab之间的分隔线
        tabHost.getTabWidget().setDividerDrawable(android.R.color.white);

        // 初始化每个tab控件
        for (int i = 0; i < MainTabHostTagEnum.values().length; i++) {
            // 为每一个Tab按钮设置图标、文字和内容
            MainTabHostTagEnum tagEnum = MainTabHostTagEnum.valueOfTabIndex(i);
            TabHost.TabSpec tabSpec = tabHost.newTabSpec(tagEnum.name()).setIndicator(newTabItemView(tagEnum));
            // 将Tab按钮添加进Tab选项卡中
            tabHost.addTab(tabSpec, tagEnum.getTabClass(), null);
            // 设置Tab按钮的背景(因为在main_tab_button_view.xml设置background了, 所以这里不需要设置了)
            //tabHost.getTabWidget().getChildAt(i).setBackgroundColor(getResources().getColor(R.color.tabhost_background_select_color));

            // 注意，tabHost.newTabSpec("TAG1").setIndicator(yourTabItemview); 这里的 "TAG1" 其实没什么什么意思，区分一下每个tab就好。
            // 重点在于setIndicator函数，其有三个不同的实现，也就是说，你可以使用三种方式来定义你的Tab的风格：
            // 只使用要文字标识tab
            // TabHost.TabSpec.setIndicator(CharSequence label)
            // 使用文字+icon标识tab
            // TabHost.TabSpec.setIndicator(CharSequence label, Drawable icon)
            // 使用自定义的View表示tab
            // TabHost.TabSpec.setIndicator(View view)
        }

    }

    /**
     * 新建一个 tab view, 并给其设置 icon + 文字
     */
    private View newTabItemView(MainTabHostTagEnum dataSource) {
        View view = LayoutInflater.from(this).inflate(R.layout.view_mainactivity_tabspec, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(dataSource.getTabIconResID());

        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(dataSource.getTabName());

        return view;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ToolsFunctionForThisProgect.onKeyDownForFinishApp(this, keyCode, event);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
