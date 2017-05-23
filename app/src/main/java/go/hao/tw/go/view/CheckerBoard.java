package go.hao.tw.go.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hao on 2017/5/20.
 */

public class CheckerBoard extends BaseDataView {

    private final float LINE_LENGTH = SPACE * 19; // 線的長度
    private final int[] STARS_POSITION = new int[]{3, 9, 15}; // 星位位置
    private final int TEXT_SIZE = 14; // 文字大小
    private final byte BLANK = 0;
    private final byte BLACK = 1;
    private final byte WHITE = 2;
    private final byte CHECK_BLACK = 3;
    private final byte CHECK_WHITE = 4;

    private GoView goView;

    private Paint blackPaint;
    private Paint whitePaint;
    private Paint redPaint; // 標示最新一手

    private List<Byte[][]> historyList = new ArrayList<>();

    private byte[][] board = new byte[19][19];

    public CheckerBoard(Context context, GoView goView) {
        super(context);
        this.goView = goView;
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawLine(canvas);
        drawStar(canvas);
        drawText(canvas);
        drawChess(canvas);
        drawRedPoint(canvas);
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

    /** 畫棋子 */
    private void drawChess(Canvas canvas){
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                if(board[i][j] == BLANK)
                    continue;
                if(board[i][j] == CHECK_BLACK)
                    board[i][j] = BLACK;
                else if(board[i][j] == CHECK_WHITE)
                    board[i][j] = WHITE;
                drawChess(canvas, i, j, board[i][j]
                        == BLACK ? blackPaint : whitePaint); // 把陣列的0~18轉成1~19
            }
        }
    }

    /** 標示目前手的紅點 */
    private void drawRedPoint(Canvas canvas){
        if(nowX != -1 && nowY != -1)
            canvas.drawCircle(getPosLength(nowX), getPosLength(nowY), RED_RADIUS, redPaint);
    }

    /** 超過陣列範圍 */
    private boolean outOfArray(int x, int y){
        return x < 0 || x > 18 || y < 0 || y > 18;
    }

    /** 目前手數輪到誰 */
    private byte getWhoIsNowTruns(){
        return goView.turns % 2 == 0 ? WHITE : BLACK;
    }

    /** 發出四個遞迴檢查上下左右的敵方棋子是不是沒氣了 */
    private boolean checkEatArount(int x, int y){
        boolean eat = false;
        if(checkEat(x, y-1)) { // 上
            eat(x, y - 1);
            eat = true;
        }
        if(checkEat(x, y+1)) { // 下
            eat(x, y + 1);
            eat = true;
        }
        if(checkEat(x-1, y)) { // 左
            eat(x - 1, y);
            eat = true;
        }
        if(checkEat(x+1, y)) { // 右
            eat(x + 1, y);
            eat = true;
        }

        return eat;
    }

    /** 遞迴的方式檢查對手是不是已經沒氣了, true = 都沒氣了 */
    private boolean checkEat(int x, int y){
        if(outOfArray(x, y)) // 超過陣列範圍 牆壁是沒氣的
            return true;

        byte who = getWhoIsNowTruns(); // 目前輪到誰
        byte now = board[x][y]; // 目前檢查誰

        if(now == BLANK) // 還有氣
            return false;
        if(now == who) // 自己人 所以是沒氣
            return true;
        if(now == CHECK_WHITE || now == CHECK_BLACK) // 檢查過了
            return true;
        if(board[x][y] == BLACK)
            board[x][y] = CHECK_BLACK;
        else if(board[x][y] == WHITE)
            board[x][y] = CHECK_WHITE;

        if(!checkEat(x, y-1) || !checkEat(x, y+1) || !checkEat(x-1, y) || !checkEat(x+1, y))
            return false;
        return true;
    }

    /** 提子 */
    private void eat(int x, int y){
        if(outOfArray(x, y))
            return;
        if(board[x][y] == getWhoIsNowTruns() || board[x][y] == BLANK) // 自己人或空的
            return;

        board[x][y] = BLANK;
        eat(x, y-1);
        eat(x, y+1);
        eat(x-1, y);
        eat(x+1, y);
    }

    /** 禁止填海 */
    private boolean isSuicide(int x, int y){
        if(!checkEat(x, y-1) || !checkEat(x, y+1) || !checkEat(x-1, y) || !checkEat(x+1, y))
            return false;
        return true;
    }

    /** 模擬落子的摳貝殼 */
    public OnSimulateCallback onSimulateCallback = new OnSimulateCallback() {
        @Override
        public void simulate(int x, int y) {
            // 原本xy是1~19, 轉成陣列要的0~18
            if(board[x][y] == BLANK) {
                board[x][y] = getWhoIsNowTruns();
                if(!checkEatArount(x, y) && isSuicide(x, y)) { // 沒提子 又去填海
                    board[x][y] = BLANK;
                    return;
                }
                setNowXY(x, y);
                goView.turns++;
                invalidate();
            }
        }
    };
}
