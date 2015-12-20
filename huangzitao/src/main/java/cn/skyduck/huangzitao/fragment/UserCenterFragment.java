package cn.skyduck.huangzitao.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import cn.skyduck.huangzitao.R;
import core_lib.toolutils.DebugLog;

/**
 * 主界面 - tabhost - 我的
 */
public class UserCenterFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        DebugLog.e(TAG, "onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DebugLog.e(TAG, "onCreateView");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_center, container, false);
    }

    @Override
    public void onAttach(Context context) {
        DebugLog.e(TAG, "onAttach");
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        DebugLog.e(TAG, "onDetach");
        super.onDetach();
    }
}
