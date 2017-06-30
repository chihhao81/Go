package go.hao.tw.go.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import go.hao.tw.go.adapter.SelectFileAdapter;
import go.hao.tw.go.tools.SGFChessBook;
import go.hao.tw.go.tools.SpacesItemDecoration;
import go.hao.tw.go.view.CheckerBoard;
import go.hao.tw.go.view.GoView;

/**
 * Created by chihhao on 2017/5/26.
 */

public class LoadBookFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout llSelectFile;
    private GoView goView;
    private Button btnInit, btnLastFive, btnLast;
    private Button btnNext, btnNextFive, btnEnd;
    private Button btnShowNumber, btnTry;
    private TextView tvInfo;
    private RecyclerView recyclerView;

    private SGFChessBook chessBook;
    private SelectFileAdapter adapter;

    public static LoadBookFragment newInstance(String sgf){
        LoadBookFragment fragment = new LoadBookFragment();
        Bundle bundle = new Bundle();
        bundle.putString("sgf", sgf);
        fragment.setArguments(bundle);
        return fragment;
    }

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
        llSelectFile = (LinearLayout)findViewById(R.id.llSelectFile);
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
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        if(getArguments() == null) {
            adapter = new SelectFileAdapter(activity);
            adapter.setOnSGFSelectedListener(onSGFSelectedListener);
            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
            recyclerView.addItemDecoration(new SpacesItemDecoration(0, 1, 0, 1));
            recyclerView.setAdapter(adapter);
        } else {
            String sgf = getArguments().getString("sgf");
            llSelectFile.setVisibility(View.GONE);
            chessBook = new SGFChessBook(sgf);
            goView.setLayoutParams(new LinearLayout.LayoutParams(App.screenWidth, App.screenWidth));
            goView.setChessBook(chessBook);
            tvInfo.setText(chessBook.getChessBookInfo(0).msg);
        }

        btnInit.setOnClickListener(this);
        btnLastFive.setOnClickListener(this);
        btnLast.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnNextFive.setOnClickListener(this);
        btnEnd.setOnClickListener(this);
        btnShowNumber.setOnClickListener(this);
        btnTry.setOnClickListener(this);
    }

    /** 是不是正在試下 */
    private boolean isTrying(){
        if(!btnTry.isSelected())
            return false;
        Toast.makeText(activity, activity.getString(R.string.finish_try), Toast.LENGTH_SHORT).show();
        return true;
    }

    /** 選擇檔案 */
    public SelectFileAdapter.OnSGFSelectedListener onSGFSelectedListener = new SelectFileAdapter.OnSGFSelectedListener() {
        @Override
        public void onSelect(SGFChessBook chessBook) {
            LoadBookFragment.this.chessBook = chessBook;
            goView.setLayoutParams(new LinearLayout.LayoutParams(App.screenWidth, App.screenWidth));
            goView.setChessBook(chessBook);
            tvInfo.setText(chessBook.getChessBookInfo(0).msg);
            llSelectFile.setVisibility(View.GONE);
        }
    };

    @Override
    public boolean onBackPressed() {
        if(llSelectFile.isShown())
            return adapter.back();
        else {
            goView.clear();
            llSelectFile.setVisibility(View.VISIBLE);
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnInit: // 清空
                goView.last(goView.turns);
                tvInfo.setText(chessBook.getMsg(goView.turns == 1 ? 0 : goView.turns-1));
                if(goView.isTrying()) {
                    goView.setTry();
                    btnTry.setSelected(false);
                }
                break;
            case R.id.btnLastFive: // 前五手
                goView.last(5);
                tvInfo.setText(chessBook.getMsg(goView.turns == 1 ? 0 : goView.turns-1));
                break;
            case R.id.btnLast: // 前一手
                goView.last(1);
                tvInfo.setText(chessBook.getMsg(goView.turns == 1 ? 0 : goView.turns-1));
                break;
            case R.id.btnNext: // 下一手
                if(isTrying())
                    return;
                goView.next(1);
                tvInfo.setText(chessBook.getMsg(goView.turns-1));
                break;
            case R.id.btnNextFive: // 下五手
                if(isTrying())
                    return;
                goView.next(5);
                tvInfo.setText(chessBook.getMsg(goView.turns-1));
                break;
            case R.id.btnEnd: // 最後一手
                if(isTrying())
                    return;
                goView.next(chessBook.getMaxTurns());
                tvInfo.setText(chessBook.getMsg(chessBook.getMaxTurns()));
                break;
            case R.id.btnShowNumber: // 顯示手數
                Toast.makeText(activity, "懶得做啦", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnTry: // 試下
                goView.setTry();
                btnTry.setSelected(!btnTry.isSelected());
                break;
        }
    }
}
