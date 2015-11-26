package h3.com.toolbartest;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by ngh1 on 2015-11-26.
 */
class AnimalView extends ImageView {

    public AnimalView(Context context) {
        super(context);
    }

    public AnimalView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimalView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int min = Math.min(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));

        if (min > 2048) {
            min = 2048;
        }

        setMeasuredDimension(min, min);
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
    }
}
