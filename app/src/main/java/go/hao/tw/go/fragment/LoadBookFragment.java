package go.hao.tw.go.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

import go.hao.tw.go.App;
import go.hao.tw.go.R;
import go.hao.tw.go.tools.SGFChessBook;
import go.hao.tw.go.view.GoView;

/**
 * Created by chihhao on 2017/5/26.
 */

public class LoadBookFragment extends BaseFragment implements View.OnClickListener {

    private GoView goView;
    private Button btnInit;
    private Button btnLastFive;
    private Button btnLast;
    private Button btnNext;
    private Button btnNextFive;
    private Button btnEnd;
    private Button btnShowNumber;
    private Button btnTry;
    private TextView tvInfo;

    private SGFChessBook chessBook;

    public LoadBookFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(layout == null){
            layout = inflater.inflate(R.layout.fragment_load_book, container, false);
            buildView();
        }
        return layout;
    }

    @Override
    protected void buildView() {
        goView = (GoView)findViewById(R.id.goView);
        btnInit = (Button)findViewById(R.id.btnInit);
        btnLastFive = (Button)findViewById(R.id.btnLastFive);
        btnLast = (Button)findViewById(R.id.btnLast);
        btnNext = (Button)findViewById(R.id.btnNext);
        btnNextFive = (Button)findViewById(R.id.btnNextFive);
        btnEnd = (Button)findViewById(R.id.btnEnd);
        btnShowNumber = (Button)findViewById(R.id.btnShowNumber);
        btnTry = (Button)findViewById(R.id.btnTry);
        tvInfo = (TextView)findViewById(R.id.tvInfo);

        chessBook = getSgf("newhand-CrawlChaos.sgf");

        goView.setLayoutParams(new LinearLayout.LayoutParams(App.screenWidth, App.screenWidth));
        goView.setChessBook(chessBook);
        tvInfo.setText(chessBook.getChessBookInfo(0).msg);

        btnInit.setOnClickListener(this);
        btnLastFive.setOnClickListener(this);
        btnLast.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnNextFive.setOnClickListener(this);
        btnEnd.setOnClickListener(this);
        btnShowNumber.setOnClickListener(this);
        btnTry.setOnClickListener(this);
    }

    /** 取得sgf擋 */
    private SGFChessBook getSgf(String path){
        SGFChessBook chessBook = null;
        try {
            InputStream inputStream = activity.getAssets().open(path);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            chessBook = new SGFChessBook(new String(buffer, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return chessBook;
    }

    /** 是不是正在試下 */
    private boolean isTrying(){
        if(!btnTry.isSelected())
            return false;
        Toast.makeText(activity, activity.getString(R.string.finish_try), Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnInit:
                if(isTrying())
                    return;
                goView.clear();
                tvInfo.setText(chessBook.getMsg(goView.turns == 1 ? 0 : goView.turns));
                break;
            case R.id.btnLastFive:
                if(isTrying())
                    return;
                goView.lastFive();
                tvInfo.setText(chessBook.getMsg(goView.turns == 1 ? 0 : goView.turns));
                break;
            case R.id.btnLast:
                if(isTrying())
                    return;
                goView.last();
                tvInfo.setText(chessBook.getMsg(goView.turns == 1 ? 0 : goView.turns));
                break;
            case R.id.btnNext:
                if(isTrying())
                    return;
                goView.next(1);
                tvInfo.setText(chessBook.getMsg(goView.turns));
                break;
            case R.id.btnNextFive:
                if(isTrying())
                    return;
                goView.next(5);
                tvInfo.setText(chessBook.getMsg(goView.turns));
                break;
            case R.id.btnEnd:
                if(isTrying())
                    return;
                goView.next(chessBook.getMaxTurns());
                tvInfo.setText(chessBook.getMsg(chessBook.getMaxTurns()));
                break;
            case R.id.btnShowNumber:
                Toast.makeText(activity, "懶得做啦", Toast.LENGTH_SHORT).show();;
                break;
            case R.id.btnTry:
                goView.setTry();
                btnTry.setSelected(!btnTry.isSelected());
                break;
        }
    }
}