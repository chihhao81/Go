package go.hao.tw.go.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.HashMap;

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

    private HashMap<Integer, HistoryInfo> historyList = new HashMap<>(); // 歷史紀錄

    private byte[][] board = new byte[19][19];
    private byte checkType; // 正在檢查什麼誰沒有氣
    private int eat; // 吃幾顆

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
                drawChess(canvas, i, j, board[i][j] == BLACK ? blackPaint : whitePaint); // 把陣列的0~18轉成1~19
            }
        }

        historyList.put(goView.turns-1, new HistoryInfo().build());
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

    /** 目前手數對手是誰 */
    private byte getOpponent(){
        return goView.turns % 2 == 0 ? BLACK : WHITE;
    }

    /** 複製目前盤面到byte[][], -1 = now */
    private byte[][] copyIndexBoard(int index){
        byte[][] result = new byte[19][19];
        byte[][] board;
        if(index == -1)
            board = this.board;
        else
            board = historyList.get(index).board;

        for(int i = 0; i < board.length; i++)
            for(int j = 0; j < board[i].length; j++)
                result[i][j] = board[i][j];
        return result;
    }

    /** 將check的復原 */
    private void resetCheck(){
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                if(board[i][j] == CHECK_BLACK)
                    board[i][j] = BLACK;
                else if(board[i][j] == CHECK_WHITE)
                    board[i][j] = WHITE;
            }
        }
    }

    /** 檢查是不是隊友 */
    private boolean isTeamMate(int x, int y){
        if(outOfArray(x, y))
            return false;
        return board[x][y] == getWhoIsNowTruns();
    }

    /** 檢查是不是對手 */
    private boolean isOpponent(int x, int y){
        if(outOfArray(x, y))
            return false;
        return board[x][y] == getOpponent();
    }

    /** 發出四個遞迴檢查上下左右的敵方棋子是不是沒氣了 */
    private boolean checkArount(int x, int y) {
        eat = 0;
        checkType = getOpponent();
        if (checkEat(x, y-1)) // 上
            eat += eat(x, y-1);
        if (checkEat(x, y+1)) // 下
            eat += eat(x, y+1);
        if (checkEat(x-1, y)) // 左
            eat += eat(x-1, y);
        if (checkEat(x+1, y)) // 右
            eat += eat(x+1, y);

        // 都沒有提子 要檢查是不是跑去填海了 如果四周有隊友的話 以隊友的座標為出發點 不然會有錯QQ
        if(eat == 0){
            checkType = getWhoIsNowTruns();
            if(isTeamMate(x, y-1)) return isDead(x, y-1);
            else if(isTeamMate(x, y+1)) return isDead(x, y+1);
            else if(isTeamMate(x-1, y)) return isDead(x-1, y);
            else if(isTeamMate(x+1, y)) return isDead(x+1, y);
            else return isDead(x, y);
        }
        return false;
    }

    private boolean checkEat(int x, int y){
        return isOpponent(x, y) && isDead(x, y) && !isRobbery(x, y);
    }

    /** 是不是沒氣了 */
    private boolean isDead(int x, int y){
        return checkLife(x, y) == 0;
    }

    /** 判斷打劫 */
    private boolean isRobbery(int x, int y){
        HistoryInfo info = historyList.get(goView.turns-1);
        return info.eat == 1 && info.x == x && info.y == y;
    }

    /** 遞迴的方式檢查還有幾氣 */
    private int checkLife(int x, int y){
        if(outOfArray(x, y)) // 超過陣列範圍 牆壁是沒氣的
            return 0;

        byte now = board[x][y]; // 目前檢查誰

        if(now == BLANK) // 還有氣
            return 1;
        else if(now == CHECK_WHITE || now == CHECK_BLACK) // 檢查過了
            return 0;
        else if(now != checkType) // 道不同 扣你一氣
            return 0;
        if(board[x][y] == BLACK)
            board[x][y] = CHECK_BLACK;
        else if(board[x][y] == WHITE)
            board[x][y] = CHECK_WHITE;

        return checkLife(x, y-1) + checkLife(x, y+1) + checkLife(x-1, y) + checkLife(x+1, y);
    }

    /** 提子 */
    private int eat(int x, int y){
        int eat = 1;
        if(outOfArray(x, y))
            return 0;
        if(board[x][y] == getWhoIsNowTruns() || board[x][y] == BLANK) // 自己人或空的
            return 0;

        board[x][y] = BLANK;
        eat += eat(x, y-1) + eat(x, y+1) + eat(x-1, y) + eat(x+1, y);
        return eat;
    }

    /** 清空棋盤 */
    public void clear(){
        historyList.clear();
        board = new byte[19][19];
        setNowXY(-1, -1);
        invalidate();
    }

    /** 上一手 */
    public void last(){
        HistoryInfo info = historyList.get(goView.turns-1);
        board = info.board;
        setNowXY(info.x, info.y);
        invalidate();
    }

    /** 模擬落子的摳貝殼 */
    public OnSimulateCallback onSimulateCallback = new OnSimulateCallback() {
        @Override
        public void simulate(int x, int y) {
            // 原本xy是1~19, 轉成陣列要的0~18
            if(board[x][y] == BLANK) {
                board[x][y] = getWhoIsNowTruns();
                if(checkArount(x, y)) {
                    board[x][y] = BLANK;
                    resetCheck();
                    return;
                }
                setNowXY(x, y);
                invalidate();
                goView.turns++;
            }
        }
    };

    /** 歷史紀錄 */
    private class HistoryInfo{
        byte[][] board;
        int x, y, eat;

        HistoryInfo build(){
            x = nowX;
            y = nowY;
            eat = CheckerBoard.this.eat;
            board = copyIndexBoard(-1);
            return this;
        }
    }
}
