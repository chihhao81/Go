package go.hao.tw.go.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import go.hao.tw.go.App;
import go.hao.tw.go.R;
import go.hao.tw.go.view.GoView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private GoView goView;
    private Button btnInit;
    private Button btnLast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        goView = (GoView)findViewById(R.id.goView);
        btnInit = (Button)findViewById(R.id.btnInit);
        btnLast = (Button)findViewById(R.id.btnLast);

        goView.setLayoutParams(new LinearLayout.LayoutParams(App.screenWidth, App.screenWidth));

        btnInit.setOnClickListener(this);
        btnLast.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnInit:
                goView.clear();
                break;
            case R.id.btnLast:
                goView.last();
                break;
        }
    }
}
