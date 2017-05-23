package go.hao.tw.go.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import go.hao.tw.go.App;

/**
 * Created by chihhao on 2017/5/22.
 */

public abstract class BaseDataView extends View{

    protected final float SPACE = App.screenWidth / 20f; // 格子間隔
    protected final float RADIUS = SPACE / 2f - 1; // 棋子半徑
    protected final float RED_RADIUS = RADIUS / 3; // 棋子半徑

    protected int nowX = -1, nowY = -1;

    public BaseDataView(Context context) {
        super(context);
    }

    protected void setNowXY(int x, int y){
        nowX = x;
        nowY = y;
    }

    /** 取得位置長度 */
    protected float getPosLength(int pos){
        return SPACE * (pos + 1);
    }

    /** 畫棋子 */
    protected void drawChess(Canvas canvas, int x, int y, Paint paint){
        if(nowX != -1 && nowY != -1)
            canvas.drawCircle(getPosLength(x), getPosLength(y), RADIUS, paint);
    }

    public interface OnSimulateCallback{
        void simulate(int x, int y);
    }
}
