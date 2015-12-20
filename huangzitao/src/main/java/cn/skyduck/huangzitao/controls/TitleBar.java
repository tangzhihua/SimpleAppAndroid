package cn.skyduck.huangzitao.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;


import android.widget.TextView;

import cn.skyduck.huangzitao.R;


/**
 * Created by adamin on 15/11/11.
 */
public class TitleBar extends RelativeLayout {

    /**
     * 标识当前TitleBar的样式类型
     */
    public enum TitleBarModeEnum {
        // 通用型 左 中 右
        general(0),
        // 定制型 - 右边是一个 spinner
        right_button_is_spinner(1),
        // 定制型 - 包含一个搜索框
        search_box(2),
        // 定制型 - 右边包含2个button
        right_double_button(3);

        private int code;

        TitleBarModeEnum(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public static TitleBarModeEnum valueOfCode(int code) {

            for (TitleBarModeEnum item : TitleBarModeEnum.values()) {
                if (item.code == code) {
                    return item;
                }
            }

            return null;
        }
    }

    // TitleBarModeEnum -- general 相关控件
    private TextView titleTextView;
    private TextView leftButtonTextView;
    private View leftButtonClickView;
    private TextView rightButtonTextView;
    private View rightButtonClickView;
    // TitleBarModeEnum -- right_button_is_spinner 相关控件
    private Spinner rightSpinner;
    // TitleBarModeEnum -- search_box 相关控件
    private EditText searchEditText;

    /*****************
     * 对外公开的事件监听
     *****************/
    private OnClickListener leftButtonOnClickListener;
    private OnClickListener rightButtonOnClickListener;
    private OnClickListener rightButtonExOnClickListener;
    private OnClickListener middleButtonOnClickListener;

    public interface OnSearchBoxInputCompleteListener {
        void onSearchBoxInputComplete(String value);
    }

    private OnSearchBoxInputCompleteListener searchBoxInputCompleteListener;

    public void setSearchBoxInputCompleteListener(OnSearchBoxInputCompleteListener searchBoxInputCompleteListener) {
        this.searchBoxInputCompleteListener = searchBoxInputCompleteListener;
    }

    public interface OnRightSpinnerItemSelectedChangeListener {
        void onItemSelectedChange(String newItem);
    }

    private OnRightSpinnerItemSelectedChangeListener rightSpinnerItemSelectedChangeListener;

    public void setOnLeftButtonClickListener(@Nullable OnClickListener l) {
        this.leftButtonOnClickListener = l;
    }

    public void setOnRightButtonClickListener(@Nullable OnClickListener r) {
        this.rightButtonOnClickListener = r;
    }

    public void setRightButtonExOnClickListener(OnClickListener rightButtonExOnClickListener) {
        this.rightButtonExOnClickListener = rightButtonExOnClickListener;
    }

    public void setMiddleButtonOnClickListener(OnClickListener middleButtonOnClickListener) {
        this.middleButtonOnClickListener = middleButtonOnClickListener;
    }

    public void setRightSpinnerItemSelectedChangeListener(@Nullable OnRightSpinnerItemSelectedChangeListener rightSpinnerItemSelectedChangeListener) {
        this.rightSpinnerItemSelectedChangeListener = rightSpinnerItemSelectedChangeListener;
    }

    /*****************                  对外公开的事件监听                   *****************/


    /**
     * @param context
     */
    public TitleBar(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 使用布局XML 文件中的自定义属性来初始化控件
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);

        TitleBarModeEnum titleBarModeEnum = TitleBarModeEnum.valueOfCode(typedArray.getInt(R.styleable.TitleBar_titlebarMode, 0));

        // 先把布局文件转化成Java 代码
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // 从指定的 xml 资源中, inflate 一个新的视图层次.
        // 参数 resource : 要加载的XML 布局资源ID
        // 参数 root :(可选参数) 如果attachToRoot参数为 true 的话, root 参数就是新创建视图的父视图, 新创建的视图, 会被追加到 root 视图中.
        // 参数 attachToRoot :
        // 返回值 : 如果提供了 root 参数, 并且attachToRoot参数为 true 的话, 返回root.
        //         否则返回新创建视图xml 文件中的根视图

        switch (titleBarModeEnum) {
            case general:
                inflater.inflate(R.layout.control_titlebar_general, this, true);
                break;

        }

        findViews();


        // title text
        if (titleTextView != null) {
            final String titleText = typedArray.getString(R.styleable.TitleBar_title_text);
            titleTextView.setText(titleText);

            // title size
            // title color
            // title icon
            final Drawable titleIcon = typedArray.getDrawable(R.styleable.TitleBar_title_icon);
            if (titleIcon != null) {
                // 设置边界(必须设置边界, 否则显示不出图片)
                titleIcon.setBounds(0, 0, titleIcon.getMinimumWidth(), titleIcon.getMinimumHeight());
            }
            titleTextView.setCompoundDrawables(null, null, titleIcon, null);

            titleTextView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (middleButtonOnClickListener != null) {
                        middleButtonOnClickListener.onClick(v);
                    }
                }
            });
        }


        // left button
        if (leftButtonTextView != null) {
            final String leftButtonText = typedArray.getString(R.styleable.TitleBar_left_button_text);
            leftButtonTextView.setText(leftButtonText);
            final Drawable leftButtonIcon = typedArray.getDrawable(R.styleable.TitleBar_left_button_icon);
            if (leftButtonIcon != null) {
                // 设置边界(必须设置边界, 否则显示不出图片)
                leftButtonIcon.setBounds(0, 0, leftButtonIcon.getMinimumWidth(), leftButtonIcon.getMinimumHeight());
            }
            leftButtonTextView.setCompoundDrawables(leftButtonIcon, null, null, null);

            //
            if (leftButtonClickView != null) {
                // 根据用户的配置, 来控制左右按钮是否响应用户点击事件
                leftButtonClickView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (leftButtonClickView.getVisibility() == VISIBLE || leftButtonClickView.getVisibility() == VISIBLE) {
                            if (leftButtonOnClickListener != null) {
                                leftButtonOnClickListener.onClick(v);
                            }
                        }
                    }
                });
            }
        }

        // right button
        if (rightButtonTextView != null) {
            final String rightButtonText = typedArray.getString(R.styleable.TitleBar_right_button_text);
            rightButtonTextView.setText(rightButtonText);
            final Drawable rightButtonIcon = typedArray.getDrawable(R.styleable.TitleBar_right_button_icon);
            if (rightButtonIcon != null) {
                // 设置边界(必须设置边界, 否则显示不出图片)
                rightButtonIcon.setBounds(0, 0, rightButtonIcon.getMinimumWidth(), rightButtonIcon.getMinimumHeight());
            }
            rightButtonTextView.setCompoundDrawables(null, null, rightButtonIcon, null);

            if (rightButtonClickView != null) {
                rightButtonClickView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (rightButtonClickView.getVisibility() == VISIBLE || rightButtonClickView.getVisibility() == VISIBLE) {
                            if (rightButtonOnClickListener != null) {
                                rightButtonOnClickListener.onClick(v);
                            }
                        }
                    }
                });
            }
        }

        // right button is spinner
        if (rightSpinner != null) {
            int right_spinner_entries_resId = typedArray.getResourceId(R.styleable.TitleBar_right_spinner_entries, -1);
            if (right_spinner_entries_resId != -1) {
                final String[] values = context.getResources().getStringArray(right_spinner_entries_resId);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, values);
                rightSpinner.setAdapter(adapter);
                // 注意 : 当设置 OnItemSelectedListener 之后, 如果有数据源的话, 会立刻执行 onItemSelected() 回调, 为了避免这种情况,
                //        可以在调用 setOnItemSelectedListener 方法之前调用 setSelection(0, true); , 一定是之前, 而不是之后
                rightSpinner.setSelection(0, true);
                rightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {// 当选中下拉框中的选项是触发的事件
                        if (rightSpinnerItemSelectedChangeListener != null) {
                            rightSpinnerItemSelectedChangeListener.onItemSelectedChange(parent.getItemAtPosition(pos).toString());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {// 未选中下拉项（即空项）触发的事件
                        // Another interface callback
                    }
                });
            }
        }


        if (searchEditText != null) {
            searchEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (searchBoxInputCompleteListener != null) {
                        searchBoxInputCompleteListener.onSearchBoxInputComplete(searchEditText.getText().toString());
                    }
                }
            });
        }

        // 回收对象
        typedArray.recycle();
    }


    private void findViews() {
        titleTextView = (TextView) findViewById(R.id.title_textView);
        leftButtonTextView = (TextView) findViewById(R.id.left_button_textView);
        leftButtonClickView = findViewById(R.id.left_button_click_view);
        rightButtonTextView = (TextView) findViewById(R.id.right_button_textView);
        rightButtonClickView = findViewById(R.id.right_button_click_view);


    }


    /**
     * 设置标题 - 文本
     *
     * @param title
     */
    public void setTitle(String title) {
        if (titleTextView != null) {
            titleTextView.setText(title);
        }
    }

    /**
     * 设置标题 - 资源ID
     *
     * @param resid
     */
    public void setTitle(@StringRes int resid) {
        if (titleTextView != null) {
            titleTextView.setText(resid);
        }
    }
}
