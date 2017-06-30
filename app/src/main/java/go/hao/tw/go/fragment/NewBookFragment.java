package go.hao.tw.go.fragment;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;

import go.hao.tw.go.App;
import go.hao.tw.go.R;
import go.hao.tw.go.activity.MainActivity;
import go.hao.tw.go.adapter.SelectPathAdapter;
import go.hao.tw.go.tools.SpacesItemDecoration;
import go.hao.tw.go.tools.ToolsBox;
import go.hao.tw.go.view.GoView;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

/**
 * Created by Hao on 2017/5/28.
 */

public class NewBookFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout llSelectPath;
    private GoView goView;
    private Button btnLast, btnPass, btnSaveGame, btnSelectPath;
    private RecyclerView recyclerView;

    private SelectPathAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(layout == null){
            layout = inflater.inflate(R.layout.fragment_new_book, container, false);
            buildView();
        }
        return layout;
    }

    @Override
    protected void buildView() {
        llSelectPath = (LinearLayout)findViewById(R.id.llSelectPath);
        goView = (GoView)findViewById(R.id.goView);
        btnLast = (Button)findViewById(R.id.btnLast);
        btnPass = (Button)findViewById(R.id.btnPass);
        btnSaveGame = (Button)findViewById(R.id.btnSaveGame);
        btnSelectPath = (Button)findViewById(R.id.btnSelectPath);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        goView.setLayoutParams(new LinearLayout.LayoutParams(App.screenWidth, App.screenWidth));

        btnLast.setOnClickListener(this);
        btnPass.setOnClickListener(this);
        btnSaveGame.setOnClickListener(this);
        btnSelectPath.setOnClickListener(this);
    }

    /** 選擇路徑 */
    public void showSelectPath(){
        if(adapter == null) {
            adapter = new SelectPathAdapter(activity);
            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
            recyclerView.addItemDecoration(new SpacesItemDecoration(0, 1, 0, 1));
            recyclerView.setAdapter(adapter);
        }
        llSelectPath.setVisibility(View.VISIBLE);
    }

    /** 轉換時間 output ex. 2016/1/19  13:39 */
    private String transTime(long time) {
        String result;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        result = calendar.get(Calendar.YEAR) + "_" +

                ((calendar.get(Calendar.MONTH) + 1) < 10 ? "0" : "") +
                (calendar.get(Calendar.MONTH) + 1) + "_" +

                (calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0" : "") +
                calendar.get(Calendar.DAY_OF_MONTH) + "__" +

                (calendar.get(Calendar.HOUR_OF_DAY) < 10 ? "0" : "") +
                calendar.get(Calendar.HOUR_OF_DAY) + ":" +

                (calendar.get(Calendar.MINUTE) < 10 ? "0" : "")
                + calendar.get(Calendar.MINUTE);

        return result;
    }

    @Override
    public boolean onBackPressed() {
        if(llSelectPath.isShown()) {
            if(adapter.back())
                llSelectPath.setVisibility(View.GONE);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLast:
                goView.last(1);
                break;
            case R.id.btnPass:
                goView.pass();
                break;
            case R.id.btnSaveGame:
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(activity, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    ((MainActivity)activity).checkPermission(new String[]{READ_EXTERNAL_STORAGE}, MainActivity.REQUEST_CODE_NEWFRAGMENT);
                else
                    showSelectPath();
                break;
            case R.id.btnSelectPath:
                try{
                    String name = transTime(System.currentTimeMillis())+".sgf";
                    String path = adapter.getPath();
                    String sgfText = "(;GM[1]FF[4]CA[UTF-8]AP[CGoban:3]ST[2]RU[Japanese]SZ[19]KM[6.5]"
                            + "PW[" + activity.getString(R.string.default_wp)
                            + "]PB[" + activity.getString(R.string.default_bp) + "]";
                    sgfText = ToolsBox.toSGF(sgfText, 0, goView.getHistoryList());

                    File file = new File(path, name);
                    file.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    OutputStreamWriter myOutWriter = new OutputStreamWriter(fileOutputStream);
                    myOutWriter.append(sgfText);
                    myOutWriter.close();
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    llSelectPath.setVisibility(View.GONE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}