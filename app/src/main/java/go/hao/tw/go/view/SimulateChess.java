package go.hao.tw.go.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;

import go.hao.tw.go.R;

/**
 * Created by Hao on 2017/5/21.
 */

public class SimulateChess extends BaseDataView implements View.OnTouchListener {

    private GoView goView;

    private Paint tBlackPaint; // 半透明黑棋
    private Paint tWhitePaint; // 半透明白棋

    private OnSimulateCallback onSimulateCallback;

    private boolean straight = false; // 時間太短就直接落子 不跟你演了

    public SimulateChess(Context context, GoView goView) {
        super(context);
        this.goView = goView;
        init(context);
    }

    private void init(Context context){
        tBlackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        tBlackPaint.setColor(ContextCompat.getColor(context, R.color.trans_black));
        tWhitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        tWhitePaint.setColor(ContextCompat.getColor(context, R.color.trans_white));

        setOnTouchListener(this);
    }

    /** 取得目前座標 */
    private void setNowXY(MotionEvent event){
        int x = Math.round(event.getX() / SPACE) -1;
        int y = Math.round(event.getY() / SPACE) -1;

        if(x != nowX || y != nowY) {
            if(x < 0 || x > 18 || y < 0 || y > 18)
                return;

            setNowXY(x, y);
            invalidate();
        }
    }

    public void setOnSimulateCallback(OnSimulateCallback onSimulateCallback){
        this.onSimulateCallback = onSimulateCallback;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawChess(canvas, nowX, nowY, goView.turns % 2 == 0 ? tWhitePaint : tBlackPaint);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                setNowXY(event);
                break;
            case MotionEvent.ACTION_DOWN:
                setNowXY(event);
                break;
            case MotionEvent.ACTION_UP:
                if(onSimulateCallback != null && nowX > -1 && nowY > -1)
                    onSimulateCallback.simulate(nowX, nowY);

                nowX = -1;
                nowY = -1;
                invalidate();
                break;
        }
        return true;
    }
}
