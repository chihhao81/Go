package go.hao.tw.go.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.view.View;

import go.hao.tw.go.App;
import go.hao.tw.go.R;

/**
 * Created by chihhao on 2017/5/22.
 */

public abstract class BaseDataView extends View{

    public static final float SPACE = App.screenWidth / 20f; // 格子間隔
    protected final float RADIUS = SPACE / 2f - 1; // 棋子半徑
    protected final float RED_RADIUS = RADIUS / 3; // 棋子半徑
    protected final int TEXT_SIZE = 14; // 文字大小

    protected Paint blackPaint;
    protected Paint whitePaint;
    protected Paint redPaint; // 標示最新一手
    protected Paint tBlackPaint; // 半透明黑棋
    protected Paint tWhitePaint; // 半透明白棋

    protected int nowX = -1, nowY = -1;

    public BaseDataView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context){
        blackPaint = new Paint();
        blackPaint.setStrokeWidth(1);
        blackPaint.setTextSize(TEXT_SIZE);
        blackPaint.setColor(Color.BLACK);
        blackPaint.setAntiAlias(true);

        whitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        whitePaint.setColor(Color.WHITE);

        redPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        redPaint.setColor(Color.RED);

        tBlackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        tBlackPaint.setColor(ContextCompat.getColor(context, R.color.trans_black));

        tWhitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        tWhitePaint.setColor(ContextCompat.getColor(context, R.color.trans_white));
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
        canvas.drawCircle(getPosLength(x), getPosLength(y), RADIUS, paint);
    }

    public interface OnSimulateListener {
        void simulate(int x, int y);
    }
}
