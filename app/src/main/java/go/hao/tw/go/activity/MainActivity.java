package go.hao.tw.go.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import go.hao.tw.go.App;
import go.hao.tw.go.R;
import go.hao.tw.go.view.CheckerBoard;
import go.hao.tw.go.view.SimulateChess;

public class MainActivity extends AppCompatActivity {

    private CheckerBoard checkerBoard;
    private SimulateChess simulateChess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int w = App.screenWidth;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(w, w);
        checkerBoard = (CheckerBoard)findViewById(R.id.checkerBoard);
        simulateChess = (SimulateChess)findViewById(R.id.simulateChess);
        checkerBoard.setLayoutParams(params);
        simulateChess.setLayoutParams(params);
    }
}
