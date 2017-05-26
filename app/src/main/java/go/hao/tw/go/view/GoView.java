package go.hao.tw.go.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import go.hao.tw.go.App;
import go.hao.tw.go.tools.ChessBook;

/**
 * Created by Hao on 2017/5/21.
 */

public class GoView extends FrameLayout {

    private ChessBook chessBook;

    private CheckerBoard checkerBoard;
    private SimulateChess simulateChess;

    public int turns = 1; // 手數

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
    public void setChessBook(ChessBook chessBook){
        this.chessBook = chessBook;
    }

    /** 清空棋盤 */
    public void clear(){
        turns = 1;
        checkerBoard.clear();
    }

    /** 上一手 */
    public void last(){
        if(turns > 1) {
            turns -= 1;
            checkerBoard.last();
        }
    }

    /** 下一步 */
    public void next(){
        if(chessBook != null){
            ChessBook.ChessBookInfo info = chessBook.getChessBookInfo(turns);
            if(info != null)
                checkerBoard.onSimulateCallback.simulate(info.x, info.y);
        }
    }
}
