package com.manyi.mall.widget.GalleryWidget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.manyi.mall.R;

/**
 * Created by iceman on 2014/7/23.
 */
public class LoadProgressImageView extends ImageView {
	public static final Paint fillPaint = new Paint();
    public static final Paint strokePaint = new Paint();
	float currentProgress = 0f;
    private Context mContext;

    private int progressHeight = 10;

	public LoadProgressImageView(Context context) {
		super(context);
	}

	public LoadProgressImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
        mContext = context;
        fillPaint.setColor(mContext.getResources().getColor(R.color.color_b5b3b1));
        strokePaint.setColor(mContext.getResources().getColor(R.color.color_e0e0e0));
	}

	public LoadProgressImageView(Context context, AttributeSet attrs,
                                 int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (currentProgress == 0 || currentProgress == 100) {
			return;
		}
        Rect rect = canvas.getClipBounds();
        int strokeRight = rect.right - getPaddingRight() -progressHeight;
        rect.left = rect.left - getPaddingLeft() + progressHeight;
        rect.top = rect.top + getPaddingTop() + rect.height()/2 - progressHeight/2;
        rect.bottom = rect.top + progressHeight;
        float divideWidth = (float)rect.width() /100;
        rect.right = (int) (rect.left + divideWidth *currentProgress ) - progressHeight;
		// rect.bottom -= height*currentProgress;
		// System.out.println("当前绘制范围:左" + rect.left + "--右" +
		// rect.right+"--上"+rect.top+"--下"+rect.bottom);//实测,top-180,bottom-0
        Rect strokeRect = new Rect();
        strokeRect.left = rect.left;
        strokeRect.right = strokeRight ;
        strokeRect.bottom = rect.bottom ;
        strokeRect.top = rect.top ;
        canvas.drawRoundRect(new RectF(strokeRect), progressHeight / 2, progressHeight / 2, strokePaint);
        canvas.drawRoundRect(new RectF(rect), progressHeight/2, progressHeight/2, fillPaint);
    }

	public void setCurrentProgress(float i) {
		currentProgress = i;
		invalidate();
	}

    public int getProgressHeight() {
        return progressHeight;
    }

    public void setProgressHeight(int progressHeight) {
        this.progressHeight = progressHeight;
    }
}
