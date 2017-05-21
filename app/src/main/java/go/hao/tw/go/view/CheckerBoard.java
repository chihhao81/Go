package go.hao.tw.go.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import go.hao.tw.go.App;

/**
 * Created by Hao on 2017/5/20.
 */

public class CheckerBoard extends View {

    public static final float SPACE = App.screenWidth / 20f;
    public static final float RADIUS = SPACE / 2f - 1;

    private final float LINE_LENGTH = SPACE * 19; // 線的長度
    private final int[] STARS_POSITION = new int[]{4, 10, 16}; // 星位位置
    private final int TEXT_SIZE = 14; // 文字大小
    private final byte BLANK = 0;
    private final byte BLACK = 1;
    private final byte WHITE = 2;

    private Paint blackPaint;
    private Paint whitePaint;
    private Paint redPaint; // 標示最新一手

    private List<Byte[][]> historyList = new ArrayList<>();

    private int turns = 1; // 手數
    private byte[][] board = new byte[19][19];

    public CheckerBoard(Context context) {
        super(context);
        init();
    }

    private void init(){
        blackPaint = new Paint();
        blackPaint.setStrokeWidth(1);
        blackPaint.setTextSize(TEXT_SIZE);
        blackPaint.setColor(Color.BLACK);
        blackPaint.setAntiAlias(true);

        whitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        whitePaint.setColor(Color.WHITE);

        redPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        redPaint.setColor(Color.RED);
    }

    /** 取得位置長度 */
    private float getPosLength(int pos){
        return SPACE * pos;
    }

    /** 畫線 */
    private void drawLine(Canvas canvas){
        for(int i = 1; i <= 19; i++){
            float start = SPACE * i;
            canvas.drawLine(start, SPACE, start, LINE_LENGTH, blackPaint); // 直線
            canvas.drawLine(SPACE, start, LINE_LENGTH, start, blackPaint); // 橫線
        }
    }

    /** 星位 */
    private void drawStar(Canvas canvas){
        float r = SPACE / 5;
        for(int i = 0; i < STARS_POSITION.length; i++)
            for(int j = 0; j < STARS_POSITION.length; j++)
                canvas.drawCircle(getPosLength(STARS_POSITION[i]), getPosLength(STARS_POSITION[j]), r, blackPaint);
    }

    /** 座標文字 */
    private void drawText(Canvas canvas){
        String[] ary = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T"};
        for(int i = 0; i < 19; i++){
            canvas.drawText(ary[i], getPosLength(i+1) - TEXT_SIZE/2, LINE_LENGTH + TEXT_SIZE, blackPaint);
            canvas.drawText(""+(19-i), LINE_LENGTH + TEXT_SIZE, getPosLength(i+1) + TEXT_SIZE/2, blackPaint);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawLine(canvas);
        drawStar(canvas);
        drawText(canvas);
    }
}
