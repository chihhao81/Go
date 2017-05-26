package go.hao.tw.go.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import go.hao.tw.go.App;
import go.hao.tw.go.R;
import go.hao.tw.go.tools.ChessBook;
import go.hao.tw.go.view.GoView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private GoView goView;
    private Button btnInit;
    private Button btnLast;
    private Button btnNext;
    private TextView tvInfo;

    private ChessBook chessBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        goView = (GoView)findViewById(R.id.goView);
        btnInit = (Button)findViewById(R.id.btnInit);
        btnLast = (Button)findViewById(R.id.btnLast);
        btnNext = (Button)findViewById(R.id.btnNext);
        tvInfo = (TextView)findViewById(R.id.tvInfo);

        chessBook = getSgf("newhand-CrawlChaos.sgf");

        goView.setLayoutParams(new LinearLayout.LayoutParams(App.screenWidth, App.screenWidth));
        goView.setChessBook(chessBook);
        tvInfo.setText(chessBook.getChessBookInfo(0).msg);

        btnInit.setOnClickListener(this);
        btnLast.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    /** 取得sgf擋 */
    private ChessBook getSgf(String path){
        ChessBook chessBook = null;
        try {
            InputStream inputStream = getAssets().open(path);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            chessBook = new ChessBook(new String(buffer, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return chessBook;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnInit:
                goView.clear();
                break;
            case R.id.btnLast:
                goView.last();
                tvInfo.setText(chessBook.getChessBookInfo(goView.turns).msg);
                break;
            case R.id.btnNext:
                goView.next();
                tvInfo.setText(chessBook.getChessBookInfo(goView.turns).msg);
                break;
        }
    }
}
