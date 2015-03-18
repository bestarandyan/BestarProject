package com.manyi.mall.widget.filtrate;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.manyi.mall.R;
import com.manyi.mall.common.util.CommonUtil;
import com.manyi.mall.interfaces.SelectItemClickListener;
import com.manyi.mall.interfaces.SelectViewCloseListener;

import java.lang.reflect.Method;

/**
 * Created by bestar on 2015/2/2.
 */
public class FiltrateView extends RelativeLayout implements View.OnClickListener{
    public FiltrateView(Context context) {
        super(context);
    }

    public FiltrateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FiltrateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    RelativeLayout mSelectLayout;
    ListView selectListView;
    LinearLayout mTouchView;
    View mLineView;
    int mListViewHeight = 0;
    boolean isAdded = false;//是否已经添加过
    boolean isOpen = false;//是否已经打开
    SelectViewCloseListener closeListener;

    public void addSelectLayout(Context context,BaseAdapter adapter,View view, final SelectItemClickListener itemClickListener){
        mLineView = view;
        mSelectLayout   = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.layout_select, null);
        selectListView = (ListView) mSelectLayout.findViewById(R.id.selectListView);
        mTouchView = (LinearLayout) mSelectLayout.findViewById(R.id.transLayout);
        mTouchView.setOnClickListener(this);
        selectListView.setAdapter(adapter);
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        int height = metric.heightPixels;   // 屏幕高度（像素）
        int dip = CommonUtil.dip2px(context,48);
        if ((height - mLineView.getBottom())*0.8f > (adapter.getCount() * dip)){
            mListViewHeight = adapter.getCount()*dip;
        }else{
            mListViewHeight = (int) ((height - mLineView.getBottom())*0.8);
        }
        RelativeLayout.LayoutParams listViewParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mListViewHeight);
        selectListView.setLayoutParams(listViewParams);
        selectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemClickListener.ItemClick(i);
            }
        });

        RelativeLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.BELOW,view.getId());
        addView(mSelectLayout,params);
        openSelectView(context);
        isAdded = true;

        View  parent = (View) getParent();
        parent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAdded()){
                    closeSelectView();
                }
            }
        });

    }

    public boolean isAdded(){
        return  isAdded;
    }

    public boolean isOpen(){
        return  isOpen;
    }

    private int getTargetHeight(View v) {

        try {
            Method m = v.getClass().getDeclaredMethod("onMeasure", int.class,
                    int.class);
            m.setAccessible(true);
            m.invoke(v, MeasureSpec.makeMeasureSpec(
                    ((View) v.getParent()).getMeasuredWidth(),
                    MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED));
        } catch (Exception e) {

        }
        return v.getMeasuredHeight();
    }

    public void closeSelectView(){
        if (selectListView==null) return;
        TranslateAnimation listDisAnimation = new TranslateAnimation(0,0,0.0f,-mListViewHeight);
        listDisAnimation.setDuration(350);
        listDisAnimation.setFillAfter(true);
        selectListView.startAnimation(listDisAnimation);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0);
        alphaAnimation.setDuration(350);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
               removeView(mSelectLayout);
                isAdded = false;
                isOpen = false;
                closeListener.OnClose();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mTouchView.startAnimation(alphaAnimation);

    }

 public void openSelectView(Context context){
     if (selectListView==null) return;
        TranslateAnimation listDisAnimation = new TranslateAnimation(0,0,-mListViewHeight,0);
        listDisAnimation.setDuration(350);
        listDisAnimation.setFillAfter(true);
        selectListView.startAnimation(listDisAnimation);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f,1.0f);
        alphaAnimation.setDuration(350);
        alphaAnimation.setFillAfter(true);
        mTouchView.startAnimation(alphaAnimation);
        isOpen = true;
    }

    public void setOnSelectCloseListener(SelectViewCloseListener listener){
        this.closeListener = listener;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.transLayout){
            closeSelectView();
        }
    }
}
