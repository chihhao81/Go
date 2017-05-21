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

    public GoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        checkerBoard = new CheckerBoard(context);
        simulateChess = new SimulateChess(context);

        addView(checkerBoard);
        addView(simulateChess);
        invalidate();
    }
}
