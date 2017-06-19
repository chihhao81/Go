package go.hao.tw.go.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.util.HashMap;

import go.hao.tw.go.tools.SGFChessBook;

/**
 * Created by Hao on 2017/5/21.
 */

public class GoView extends FrameLayout {

    private SGFChessBook chessBook;

    private CheckerBoard checkerBoard;
    private SimulateChess simulateChess;

    public int turns = 1; // 手數
    private int tryTurns; // 紀錄按下試下時的手數(打譜)

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
    }

    /** 黑棋的局? */
    public boolean isBlackTurn(){
        return turns % 2 == 1;
    }

    /** 上N手 */
    public void last(int n){
        turns = turns > n ? turns-n : 1;
        checkerBoard.recovery(turns);
    }

    /** 下N步 */
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
        if(simulateChess.isEnabled()){ // 結束試下
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
