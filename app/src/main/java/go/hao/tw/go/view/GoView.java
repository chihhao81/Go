package go.hao.tw.go.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import go.hao.tw.go.tools.SGFChessBook;

/**
 * Created by Hao on 2017/5/21.
 */

public class GoView extends FrameLayout {

    private SGFChessBook chessBook;

    private CheckerBoard checkerBoard;
    private SimulateChess simulateChess;

    public int turns = 1; // 手數
    public int maxTurns; // 最後手數(打譜)
    private int tryTurns; // 紀錄按下試下時的手數(打譜)

    public GoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        checkerBoard = new CheckerBoard(context, this);
        simulateChess = new SimulateChess(context, this);

        simulateChess.setOnSimulateCallback(checkerBoard.onSimulateCallback);

        addView(checkerBoard);
        addView(simulateChess);
    }

    /** 設定棋譜 */
    public void setChessBook(SGFChessBook chessBook){
        this.chessBook = chessBook;
        simulateChess.setEnabled(false);
    }

    /** 清空棋盤 */
    public void clear(){
        turns = 1;
        checkerBoard.recovery(turns);
    }

    public void lastFive(){
        turns = turns > 5 ? turns-5 : 1;
        checkerBoard.recovery(turns);
    }

    /** 上一手 */
    public void last(){
        turns = turns > 1 ? turns-1 : 1;
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

    }
}
