package go.hao.tw.go.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import go.hao.tw.go.R;
import go.hao.tw.go.tools.Point;

/**
 * Created by Hao on 2017/5/21.
 */

public class SimulateChess extends View implements View.OnTouchListener {

    private Context context;

    private Paint tBlackPaint; // 半透明黑棋
    private Paint tWhitePaint; // 半透明白棋

    public SimulateChess(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context){
        this.context = context;

        tBlackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        tBlackPaint.setColor(ContextCompat.getColor(context, R.color.trans_black));
        tWhitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        tWhitePaint.setColor(ContextCompat.getColor(context, R.color.trans_white));

        setOnTouchListener(this);
    }

    private void drawChess(Canvas canvas, Point point, Paint paint){
        canvas.drawCircle(point.x, point.y, CheckerBoard.RADIUS, paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }
}
