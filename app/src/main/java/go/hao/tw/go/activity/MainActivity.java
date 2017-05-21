package go.hao.tw.go.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import go.hao.tw.go.App;
import go.hao.tw.go.R;
import go.hao.tw.go.view.GoView;

public class MainActivity extends AppCompatActivity {

    private GoView goView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goView = (GoView)findViewById(R.id.goView);
        goView.setLayoutParams(new LinearLayout.LayoutParams(App.screenWidth, App.screenWidth));
    }
}
