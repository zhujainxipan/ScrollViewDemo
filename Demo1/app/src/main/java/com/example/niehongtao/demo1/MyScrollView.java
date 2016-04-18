package com.example.niehongtao.demo1;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by niehongtao on 2015/8/25.
 */
public class MyScrollView extends ScrollView {

    private OnScrollListener onScrollListener;//就是一个接口
    /**
     * 主要是用在用户手指离开MyScrollView，MyScrollView还在继续滑动，我们用来保存Y的距离，然后做比较
     * 如果高度lastY和scrollView.getScrollY()是相等的，则表示，
     * ScrollView已经停止，这时再根据你的要求判断停止之后的业务。
     * 这时的scrollView.getScrollY()就是控件滚动的距离
     * 如果不相等，证明还在滑动中，继续发handler继续记录lastY。
     */
    private int lastScrollY;//弄明白lastScrollY的变化就会明白一切

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 设置滚动接口
     * @param onScrollListener
     */
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    /**
     * 滚动的回调接口
     */
    public interface OnScrollListener{
        /**
         * 回调方法， 返回MyScrollView滑动的Y方向距离
         * @param scrollY
         */
        public void onScroll(int scrollY);
    }

    /**
     * 重写onTouchEvent， 当用户的手在MyScrollView上面的时候，
     * 直接将MyScrollView滑动的Y方向距离回调给onScroll方法中，当用户抬起手的时候，
     * MyScrollView可能还在滑动，所以当用户抬起手我们隔5毫秒给handler发送消息，在handler处理
     * MyScrollView滑动的距离
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(onScrollListener != null){
            //1.记录下手放上去的时候,在不断的更新，随着我们手指的移动
            lastScrollY = this.getScrollY();//返回的是myscrollviewY方向上滚动的距离
            onScrollListener.onScroll(lastScrollY);
        }
        //其实我们如果不是因为我们的手指离开手机屏幕时，scrollview还在滑动的话，其实我们只需要写到这里就可以了
        //并且也不用写handler，因为如果使用scrollView.getScrollY()获得的是你手滑动的距离，当你松开手之后，
        //界面会继续滑动，这个方法是获取不到新的Y值，所以，我们可以添加监听，时刻监视着ScrollView的变化。
        switch(ev.getAction()){
            case MotionEvent.ACTION_UP:
                //在MotionEvent.ACTION_UP中，发送延迟的消息，这样就可以起到监听的作用，并且记录当前的高度lastScrollY
                //手指离开的时候，myscrollview在我们的手指在上面的时候，其滚动的距离已经记录下来了，就是lastScrollY（上面记录的）
                //但是这个记录并不全，因为当我们的手指离开的时候，可能scrollview还在滑动，所以需要用handler发送消息监听
                handler.sendMessageDelayed(handler.obtainMessage(), 5);//就是延迟发送一个消息，你将在和改handler绑定的线程中接收到这个消息
                break;
        }
        return super.onTouchEvent(ev);
    }


    /**
     * 用于用户手指离开MyScrollView的时候获取MyScrollView滚动的Y距离，然后回调给onScroll方法中
     */
    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            //这个是在5s之后执行的，我们以为他已经停下来了，如果没有停下来是得不到新的getScrollY的
            //？？？？也就是说如果记录下的y和此时getScrollY相等，说明还没有停下来
            int scrollY = MyScrollView.this.getScrollY();
            //此时的距离和记录下的距离不相等，在隔5毫秒给handler发送消息
            //这里在不断的发消息，不断的检测，不断的onscroll，没有停下来
            //这样比较灵敏一些
            if (lastScrollY != 0) {
                handler.sendMessageDelayed(handler.obtainMessage(), 10);
                lastScrollY = scrollY;
            }
            if (onScrollListener != null) {
                onScrollListener.onScroll(scrollY);
            }
        }

    };
}
