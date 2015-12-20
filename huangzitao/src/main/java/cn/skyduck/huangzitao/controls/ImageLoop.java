package cn.skyduck.huangzitao.controls;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.skyduck.huangzitao.R;


/**
 * Created by zhihuatang on 15/11/30.
 * 图片轮播控件
 */
public class ImageLoop extends RelativeLayout {
    private final List<String> imageUrlList = new ArrayList<>();
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.page_control_layout)
    LinearLayout pageControlLayout;

    public ImageLoop(Context context) {
        super(context);
    }

    public ImageLoop(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 先把布局文件转化成Java 代码
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // 从指定的 xml 资源中, inflate 一个新的视图层次.
        // 参数 resource : 要加载的XML 布局资源ID
        // 参数 root :(可选参数) 如果attachToRoot参数为 true 的话, root 参数就是新创建视图的父视图, 新创建的视图, 会被追加到 root 视图中.
        // 参数 attachToRoot :
        // 返回值 : 如果提供了 root 参数, 并且attachToRoot参数为 true 的话, 返回root.
        //         否则返回新创建视图xml 文件中的根视图
        inflater.inflate(R.layout.control_image_loop_layout, this, true);

        ButterKnife.bind(this);
    }

    public void setImageUrlList(List<String> imageUrlList) {
        this.imageUrlList.clear();
        this.imageUrlList.addAll(imageUrlList);
    }
}
