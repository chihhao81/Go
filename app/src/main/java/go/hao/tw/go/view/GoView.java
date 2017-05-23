package go.hao.tw.go.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import go.hao.tw.go.App;

/**
 * Created by Hao on 2017/5/21.
 */

public class GoView extends FrameLayout {

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
}
