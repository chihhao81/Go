package go.hao.tw.go.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import go.hao.tw.go.R;

/**
 * Created by Hao on 2017/5/21.
 */

public class SimulateChess extends BaseDataView implements View.OnTouchListener {

    private GoView goView;

    private List<OnSimulateListener> list = new ArrayList<>();

    public SimulateChess(Context context, GoView goView) {
        super(context);
        this.goView = goView;
        init();
    }

    private void init(){
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

    public void addOnSimulateListener(OnSimulateListener onSimulateListener){
        list.add(onSimulateListener);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(nowX != -1 && nowY != -1)
            drawChess(canvas, nowX, nowY, goView.isBlackTurn() ? tBlackPaint : tWhitePaint);
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
                if(list.size() > 0 && nowX > -1 && nowY > -1)
                    for(OnSimulateListener callback : list)
                        callback.simulate(nowX, nowY);

                nowX = -1;
                nowY = -1;
                invalidate();
                break;
        }
        return true;
    }
}
