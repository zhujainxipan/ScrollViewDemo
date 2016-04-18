package com.example.niehongtao.demo1;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by niehongtao on 2015/8/25.
 */
public class MainActivity extends FragmentActivity {
    private int hight = 0;
    private MyScrollView myScrollView;
    private TextView mTvTitle;
    private TextView mTvActionBarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myScrollView = (MyScrollView) findViewById(R.id.sv_myscrollview);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvActionBarTitle = (TextView) findViewById(R.id.action_bar_title);
        mTvActionBarTitle.setText("心脏病科");
        myScrollView.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                if (hight == 0) {
                    hight = mTvTitle.getBottom();//返回的是该控件相对于父控件的bootm（100dp）
                }
                if (scrollY > hight) {
                    //scrollY指的是myscrollview移动的距离
                    if (mTvActionBarTitle.getVisibility() == View.INVISIBLE) {
                        mTvActionBarTitle.setVisibility(View.VISIBLE);
                        mTvTitle.setVisibility(View.INVISIBLE);
                    }
                } else {
                    if (mTvActionBarTitle.getVisibility() == View.VISIBLE) {
                        mTvActionBarTitle.setVisibility(View.INVISIBLE);
                        mTvTitle.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }


}
