package go.hao.tw.go.view;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.util.HashMap;
import java.util.List;

import go.hao.tw.go.tools.SGFChessBook;

/**
 * Created by Hao on 2017/5/21.
 */

public class GoView extends FrameLayout {

    public static final Point[] POINTS = new Point[]{
            new Point(15, 3),
            new Point(3, 15),
            new Point(3, 3),
            new Point(15, 15),
            new Point(3, 9),
            new Point(15, 9),
            new Point(9, 3),
            new Point(9, 15),
            new Point(9, 9),
    }; // 讓子座標

    private CheckerBoard checkerBoard;
    private SimulateChess simulateChess;
    private SGFChessBook chessBook;

    public int turns = 1; // 手數
    private int tryTurns; // 紀錄按下試下時的手數(打譜)
    private boolean isAb = false; // 是不是讓子棋

    public GoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        checkerBoard = new CheckerBoard(context, this);
        simulateChess = new SimulateChess(context, this);

        simulateChess.addOnSimulateListener(checkerBoard.onSimulateListener);

        addView(checkerBoard);
        addView(simulateChess);
    }

    /** 設定棋譜 */
    public void setChessBook(SGFChessBook chessBook){
        this.chessBook = chessBook;
        simulateChess.setEnabled(false);

        List<String> abList = chessBook.getAbList();
        if(abList.size() > 0) {
            isAb = true;
            for(String position : abList) {
                int x = position.charAt(0) - SGFChessBook.ASCII_LOW_A;
                int y = position.charAt(1) - SGFChessBook.ASCII_LOW_A;
                checkerBoard.setAbPosition(x, y);
            }
        }
        checkerBoard.invalidate();
    }

    /** 讓子 */
    public void setAb(int count){
        isAb = true;
        for(int i = 0; i < count; i++){
            Point point = POINTS[i];
            if((count == 5 || count == 7) && i == count-1)
                point = POINTS[POINTS.length-1];
            checkerBoard.setAbPosition(point.x, point.y);
        }
    }

    /** 黑棋的局? */
    public boolean isBlackTurn(){
        return turns % 2 == (isAb ? 0 : 1);
    }

    /** 是不是正在試下 */
    public boolean isTrying(){
        return simulateChess.isEnabled();
    }

    /** 清空棋盤 */
    public void clear(){
        turns = 1;
        isAb = false;
        checkerBoard.clear();
    }

    /** 上N手 */
    public void last(int n){
        turns = turns > n ? turns-n : 1;
        if(isTrying() && turns < tryTurns)
            tryTurns = turns;
        checkerBoard.recovery(turns);
    }

    /** 下N手 */
    public void next(int next){
        for(int i = 0; i < next; i++){
            SGFChessBook.ChessBookInfo info = chessBook.getChessBookInfo(turns);
            if(info != null)
                checkerBoard.downHere(info.x, info.y);
        }
        checkerBoard.invalidate();
    }

    /** 試下 */
    public void setTry(){
        if(isTrying()){ // 結束試下
            turns = tryTurns;
            checkerBoard.recovery(tryTurns);
            simulateChess.setEnabled(false);
        } else { // 開始試下
            tryTurns = turns;
            simulateChess.setEnabled(true);
        }
    }

    /** 虛手 */
    public void pass(){
        checkerBoard.pass();
    }

    /** 結束 */
    public void gameOver(){
        simulateChess.setEnabled(false);
    }

    /** 復活啦 */
    public void resurrection(){
        simulateChess.setEnabled(true);
    }

    /** 點選死子 */
    public void setDeadChessMode(){
        checkerBoard.setDeadChessMode();
    }

    /** 回到正常模式 */
    public void setNormalMode(){
        checkerBoard.setNormalMode();
    }

    /** 輸贏啦 */
    public int[] judgement(){
        return checkerBoard.judgement();
    }

    /** 取得棋譜 */
    public HashMap<Integer, CheckerBoard.HistoryInfo> getHistoryList(){
        return checkerBoard.getHistoryList();
    }

    /** 每回合結束的監聽 */
    public void setOnTurnOverListener(CheckerBoard.OnTurnOverListener listener){
        checkerBoard.setOnTurnOverListener(listener);
    }
}
