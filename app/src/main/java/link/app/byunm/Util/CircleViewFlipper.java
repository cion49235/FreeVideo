package link.app.byunm.Util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ViewFlipper;

public class CircleViewFlipper extends ViewFlipper {

    Paint paint = new Paint();
    double heightRate = 0.87;

    public CircleViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        int width = getWidth();

        float margin = 30;
        float radius = 30;

        float cx = width / 2 - ((radius*2+margin)*(getChildCount()-1) / 2);
        float cy = (float)(getHeight() * heightRate);

        canvas.save();

        for ( int i=0; i<getChildCount(); i++ ) {
            /*if ( i == getDisplayedChild() ) {
                paint.setColor(Color.argb(50, 0, 0, 0));
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                canvas.drawCircle(cx, cy, radius, paint);
            } else {
                paint.setColor(Color.argb(120, 255, 255, 255));
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                canvas.drawCircle(cx, cy, radius, paint);
            }*/
            cx += radius * 2 + margin;
        }
        canvas.restore();
    }

    public void setHeightRate(double heightRate) {
        this.heightRate = heightRate;
    }
}
