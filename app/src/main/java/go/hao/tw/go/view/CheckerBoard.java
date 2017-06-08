package go.hao.tw.go.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;

/**
 * Created by Hao on 2017/5/20.
 */

public class CheckerBoard extends BaseDataView {

    private final float LINE_LENGTH = SPACE * 19; // 線的長度
    private final int[] STARS_POSITION = new int[]{3, 9, 15}; // 星位位置
    private final byte BLANK = 0;
    private final byte BLACK = 1;
    private final byte WHITE = 2;
    private final byte CHECK_BLANK = 3;
    private final byte CHECK_BLACK = 4;
    private final byte CHECK_WHITE = 5;

    private GoView goView;

    private OnTurnOverListener onTurnOverListener;

    private HashMap<Integer, HistoryInfo> historyList = new HashMap<>(); // 歷史紀錄

    private byte[][] board = new byte[19][19];
    private byte checkType; // 正在檢查什麼誰沒有氣
    private int eat; // 吃幾顆
    private int bPlace = 0; // 黑目
    private int wPlace = 0; // 白目
    private int tempPlace = 0; // 暫時數的
    private boolean ggMode = false; // 是不是在點選死子模式

    public CheckerBoard(Context context, GoView goView) {
        super(context);
        this.goView = goView;
        init();
    }

    private void init(){
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
            canvas.drawText(ary[i], getPosLength(i) - TEXT_SIZE/2, LINE_LENGTH + TEXT_SIZE, blackPaint);
            canvas.drawText(""+(19-i), LINE_LENGTH + TEXT_SIZE, getPosLength(i) + TEXT_SIZE/2, blackPaint);
        }
    }

    /** 畫棋子 */
    private void drawChess(Canvas canvas){
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++) {
                if (board[i][j] == CHECK_BLANK)
                    board[i][j] = BLANK;
                if (board[i][j] == BLANK)
                    continue;

                if (ggMode) {
                    Paint paint;
                    if(board[i][j] == BLACK)
                        paint = blackPaint;
                    else if(board[i][j] == WHITE)
                        paint = whitePaint;
                    else if(board[i][j] == CHECK_BLACK)
                        paint = tBlackPaint;
                    else
                        paint = tWhitePaint;

                    drawChess(canvas, i, j, paint);
                } else {
                    if (board[i][j] == CHECK_BLACK)
                        board[i][j] = BLACK;
                    else if (board[i][j] == CHECK_WHITE)
                        board[i][j] = WHITE;
                    if (canvas != null)
                        drawChess(canvas, i, j, board[i][j] == BLACK ? blackPaint : whitePaint);
                }
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
        return goView.isBlackTurn() ? BLACK : WHITE;
    }

    /** 目前手數對手是誰 */
    private byte getOpponent(){
        return goView.isBlackTurn() ? WHITE : BLACK;
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

    /** 檢查是不是對手 */
    private boolean isOpponent(byte myself, byte opp){
        if(myself == BLACK || myself == CHECK_BLACK)
            return opp == WHITE || opp == CHECK_WHITE;
        else if(myself == WHITE || myself == CHECK_WHITE)
            return opp == BLACK || opp == CHECK_BLACK;
        return myself == CHECK_BLANK;
    }

    /** 發出四個遞迴檢查上下左右的敵方棋子是不是沒氣了 */
    private boolean checkArount(int x, int y) {
        eat = 0;
        checkType = getOpponent();
        if (checkEat(x, y-1)) // 上
            eat(x, y-1);
        if (checkEat(x, y+1)) // 下
            eat(x, y+1);
        if (checkEat(x-1, y)) // 左
            eat(x-1, y);
        if (checkEat(x+1, y)) // 右
            eat(x+1, y);

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
    private void eat(int x, int y){
        if(outOfArray(x, y))
            return;
        if(board[x][y] == getWhoIsNowTruns() || board[x][y] == BLANK) // 自己人或空的
            return;

        board[x][y] = BLANK;
        eat += 1;
        eat(x, y-1);
        eat(x, y+1);
        eat(x-1, y);
        eat(x+1, y);
    }

    /** 設為死子 */
    private void setDeadChess(int x, int y, byte type){
        if(outOfArray(x, y))
            return;

        byte now = board[x][y];

        if(isOpponent(type, now))
            return;
        else if((type == BLACK && now == CHECK_BLACK) || (type == CHECK_BLACK && now == BLACK))
            return;
        else if((type == WHITE && now == CHECK_WHITE) || (type == CHECK_WHITE && now == WHITE))
            return;
        else if(now == CHECK_BLANK)
            return;
        else if(now == BLANK)
            board[x][y] = CHECK_BLANK;

        if(now == type){
            if(type == BLACK)
                board[x][y] = CHECK_BLACK;
            else if(type == CHECK_BLACK)
                board[x][y] = BLACK;
            else if(type == WHITE)
                board[x][y] = CHECK_WHITE;
            else if(type == CHECK_WHITE)
                board[x][y] = WHITE;
        }

        setDeadChess(x, y-1, type);
        setDeadChess(x, y+1, type);
        setDeadChess(x-1, y, type);
        setDeadChess(x+1, y, type);
    }

    /** 回到哪一手 */
    public void recovery(int turns){
        HistoryInfo info = historyList.get(turns-1);
        if(info == null)
            return;
        board = info.board;
        setNowXY(info.x, info.y);
        invalidate();
    }

    /** 虛手 */
    public void pass(){
        setNowXY(-1, -1);
        goView.turns++;
        invalidate();
        if(onTurnOverListener != null)
            onTurnOverListener.onTurnOver(true);
    }

    /** 指定落子, 前進手數用 */
    public void downHere(int x, int y){
        if(x != -1 && y != -1 && board[x][y] == BLANK) {
            board[x][y] = getWhoIsNowTruns();
            if(checkArount(x, y)) {
                board[x][y] = BLANK;
                resetCheck();
                return;
            }
            setNowXY(x, y);
            goView.turns++;
            drawChess(null);
        }
    }

    /** 點選死子模式 */
    public void setDeadChessMode(){
        ggMode = true;
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    int x = Math.round(event.getX() / SPACE) - 1;
                    int y = Math.round(event.getY() / SPACE) - 1;

                    if ((!(x < 0 || x > 18 || y < 0 || y > 18)) && board[x][y] != BLANK) {
                        setDeadChess(x, y, board[x][y]);
                        invalidate();
                    }
                }
                return true;
            }
        });
    }

    /** 回到正常模式 */
    public void setNormalMode(){
        ggMode = false;
        setOnTouchListener(null);
    }

    /** 輸贏啦 */
    public void judgement(){
        // 被點掉的死子
        for(int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == CHECK_BLACK) {
                    wPlace++;
                    board[i][j] = BLANK;
                } else if(board[i][j] == CHECK_WHITE){
                    bPlace++;
                    board[i][j] = BLANK;
                }
            }
        }

        // 過程中的提子
        for(int i = 1; i < goView.turns-2; i++){
            int eat = historyList.get(i).eat;
            if(i % 2 == BLACK)
                wPlace += eat;
            else
                bPlace += eat;
        }

        for(int i = 0; i < board.length; i++)
            for (int j = 0; j < board[i].length; j++)
                if(board[i][j] == BLANK)
                    checkPlace(i, j);
    }

    /** 數空, return true = 黑目 */
    private boolean checkPlace(int x, int y){
        return false;
    }

    /** 模擬落子的摳貝殼 */
    public OnSimulateListener onSimulateListener = new OnSimulateListener() {
        @Override
        public void simulate(int x, int y) {
            if(x != -1 && y != -1 && board[x][y] == BLANK) {
                board[x][y] = getWhoIsNowTruns();
                if(checkArount(x, y)) {
                    board[x][y] = BLANK;
                    resetCheck();
                    return;
                }
                setNowXY(x, y);
                invalidate();
                goView.turns++;
                if(onTurnOverListener != null)
                    onTurnOverListener.onTurnOver(false);
            }
        }
    };

    public void setOnTurnOverListener(OnTurnOverListener onTurnOverListener){
        this.onTurnOverListener = onTurnOverListener;
    }

    public interface OnTurnOverListener{
        void onTurnOver(boolean pass);
    }

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
