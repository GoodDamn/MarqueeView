package good.damn.marqueeview.graphics;

import android.app.appsearch.SearchResult;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.google.android.material.radiobutton.MaterialRadioButton;

public class Circle extends Entity {

    private static final String TAG = "Circle";

    private float mRadius;
    private float mAngle = 0;

    private float mDebugStickX = 0;
    private float mDebugStickY = 0;

    public Circle() {
        super();
        mPaintBackground.setStyle(Paint.Style.STROKE);
        mPaintForeground.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawArc(mMarStartX-mRadius,
                mMarStartY-mRadius,
                mMarStartX+mRadius,
                mMarStartY+mRadius,
                0,360,
                false,mPaintBackground);
        canvas.drawArc(mMarStartX-mRadius,
                mMarStartY-mRadius,
                mMarStartX+mRadius,
                mMarStartY+mRadius,
                0,360*mProgress,
                false, mPaintForeground);

        if (RELEASE_MODE) {
            return;
        }

        super.onDraw(canvas); // debug mode

        canvas.drawCircle(mDebugStickX, mDebugStickY, mPaintBackground.getStrokeWidth(), mPaintDebug);

        canvas.drawCircle(mMarStartX, mMarStartY, 5, mPaintDebug);
        canvas.drawLine(mMarStartX-mRadius,mMarStartY,mMarStartX+mRadius,mMarStartY,mPaintDebug);
        canvas.drawLine(mMarStartX,mMarStartY-mRadius,mMarStartX,mMarStartY+mRadius,mPaintDebug);

        canvas.drawText("ANG: " + mAngle, mStickX-mStickBound,mStickY-mStickBound-mPaintDebug.getTextSize()*4, mPaintDebug);
    }

    @Override
    public void onLayout(int width, int height, float startX, float startY, float endX, float endY) {
        super.onLayout(width, height, startX, startY, endX, endY);
        mRadius = (float) Math.hypot(mMarEndX-mMarStartX, mMarEndY-mMarStartY);
        mStickX = mMarStartX + mRadius;
    }

    @Override
    public void reset() {
        super.reset();
        mStickX = mMarStartX + mRadius;
        mAngle = 0;
    }

    @Override
    void onPlace(float x, float y) {
        // Shit-code below (mStickX, mStickY, mProgress)

        float localX = x - mMarStartX;
        float localY = y - mMarStartY;

        float ang = (float) Math.toDegrees(Math.atan2(localY,localX));

        if (ang < 0) {
            ang = 360 + ang;
        }

        if (ang > 135 && mAngle < 90) {
            ang = 1;
        }


        mAngle = ang;
        mProgress = mAngle / 360;

        float sin = (float) Math.sin(mAngle * 0.016625f);
        float cos = (float) Math.cos(mAngle * 0.016625f);

        mDebugStickX = (float) (mMarStartX + mRadius*cos);
        mDebugStickY = (float) (mMarStartY + mRadius*sin);

        Log.d(TAG, "onPlace: SIN: " + sin + " COS: " + cos);

        mStickX = x;
        mStickY = y;
    }
}
