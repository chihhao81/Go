package go.hao.tw.go.fragment;

import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import go.hao.tw.go.App;
import go.hao.tw.go.R;
import go.hao.tw.go.activity.MainActivity;
import go.hao.tw.go.adapter.SelectPathAdapter;
import go.hao.tw.go.pop.PKSettingDialog;
import go.hao.tw.go.tools.SpacesItemDecoration;
import go.hao.tw.go.tools.ToolsBox;
import go.hao.tw.go.view.BaseDataView;
import go.hao.tw.go.view.CheckerBoard;
import go.hao.tw.go.view.GoView;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

/**
 * Created by chihhao on 2017/5/26.
 */

public class PKFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout llWhiteMenu, llWhiteChoose, llBlackMenu, llBlackChoose, llPKResult, llSelectPath;
    private GoView goView;
    private ImageView ivWhite, ivBlack;
    private TextView tvWhiteInfo, tvBlackInfo, tvResult;
    private Button btnWhiteRegret, btnWhiteSurrender, btnWhitePass, btnWhiteConfirm, btnWhiteCancel;
    private Button btnBlackRegret, btnBlackSurrender, btnBlackPass, btnBlackConfirm, btnBlackCancel;
    private Button btnReviewGame, btnSaveGame, btnSelectPath;
    private RecyclerView recyclerView;

    private PKSettingDialog pkSettingDialog;
    private SelectPathAdapter adapter;

    private int passTurns; // 檢查是不是連續虛手結束比賽
    private String sgfText;// 儲存棋譜用

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(layout == null){
            layout = inflater.inflate(R.layout.fragment_pk, container, false);
            buildView();
        }
        return layout;
    }

    @Override
    protected void buildView() {
        llWhiteMenu = (LinearLayout)findViewById(R.id.llWhiteMenu);
        llWhiteChoose = (LinearLayout)findViewById(R.id.llWhiteChoose);
        llBlackMenu = (LinearLayout)findViewById(R.id.llBlackMenu);
        llBlackChoose = (LinearLayout)findViewById(R.id.llBlackChoose);
        llPKResult = (LinearLayout)findViewById(R.id.llPKResult);
        llSelectPath = (LinearLayout)findViewById(R.id.llSelectPath);
        goView = (GoView)findViewById(R.id.goView);
        ivWhite = (ImageView)findViewById(R.id.ivWhite);
        ivBlack = (ImageView)findViewById(R.id.ivBlack);
        tvWhiteInfo = (TextView)findViewById(R.id.tvWhiteInfo);
        tvBlackInfo = (TextView)findViewById(R.id.tvBlackInfo);
        tvResult = (TextView)findViewById(R.id.tvResult);
        btnWhiteRegret = (Button)findViewById(R.id.btnWhiteRegret);
        btnWhiteSurrender = (Button)findViewById(R.id.btnWhiteSurrender);
        btnWhitePass = (Button)findViewById(R.id.btnWhitePass);
        btnWhiteConfirm = (Button)findViewById(R.id.btnWhiteConfirm);
        btnWhiteCancel = (Button)findViewById(R.id.btnWhiteCancel);
        btnBlackRegret = (Button)findViewById(R.id.btnBlackRegret);
        btnBlackSurrender = (Button)findViewById(R.id.btnBlackSurrender);
        btnBlackPass = (Button)findViewById(R.id.btnBlackPass);
        btnBlackConfirm = (Button)findViewById(R.id.btnBlackConfirm);
        btnBlackCancel = (Button)findViewById(R.id.btnBlackCancel);
        btnReviewGame = (Button)findViewById(R.id.btnReviewGame);
        btnSaveGame = (Button)findViewById(R.id.btnSaveGame);
        btnSelectPath = (Button)findViewById(R.id.btnSelectPath);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        pkSettingDialog = new PKSettingDialog(activity);
        pkSettingDialog.setOnDismissListener(onDismissListener);
        pkSettingDialog.show();

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(App.screenWidth, App.screenWidth);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        goView.setLayoutParams(params);

        int space = (int)BaseDataView.SPACE;
        int height = App.screenWidth / 4 - space*2;
        params = new RelativeLayout.LayoutParams(height, height);
        params.addRule(RelativeLayout.ABOVE, R.id.goView);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.setMargins(space, space, 0, 0);
        ivWhite.setLayoutParams(params);

        params = new RelativeLayout.LayoutParams(height, height);
        params.addRule(RelativeLayout.BELOW, R.id.goView);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.setMargins(0, 0, space, space);
        ivBlack.setLayoutParams(params);

        goView.setOnTurnOverListener(onTurnOverListener);
        btnWhiteRegret.setOnClickListener(this);
        btnWhiteSurrender.setOnClickListener(this);
        btnWhitePass.setOnClickListener(this);
        btnWhiteConfirm.setOnClickListener(this);
        btnWhiteCancel.setOnClickListener(this);
        btnBlackRegret.setOnClickListener(this);
        btnBlackSurrender.setOnClickListener(this);
        btnBlackPass.setOnClickListener(this);
        btnBlackConfirm.setOnClickListener(this);
        btnBlackCancel.setOnClickListener(this);
        btnReviewGame.setOnClickListener(this);
        btnSaveGame.setOnClickListener(this);
        btnSelectPath.setOnClickListener(this);
    }

    /** 白色同意 */
    private void whiteAgree(){
        if(btnBlackRegret.isSelected()) { // 黑悔棋
            goView.last(goView.isBlackTurn() ? 2 : 1); // 回到上一次黑棋下的地方
        } else if(btnWhiteSurrender.isSelected()) { // 白投降
            goView.gameOver();
            llWhiteMenu.setVisibility(View.GONE);
            llBlackMenu.setVisibility(View.GONE);
            setPKResult(String.format(activity.getString(R.string.win_for_surrend), pkSettingDialog.getBlackPlayer()), "B+Resign");
        }
        cancel();
    }

    /** 黑色同意 */
    private void blackAgree(){
        if(btnWhiteRegret.isSelected()) { // 白悔棋
            goView.last(goView.isBlackTurn() ? 1 : 2); // 回到上一次白棋下的地方
        } else if(btnBlackSurrender.isSelected()) { // 黑投降
            goView.gameOver();
            llWhiteMenu.setVisibility(View.GONE);
            llBlackMenu.setVisibility(View.GONE);
            setPKResult(String.format(activity.getString(R.string.win_for_surrend), pkSettingDialog.getWhitePlayer()), "W+Resign");
        }
        cancel();
    }

    /** 結束統計畫面 */
    private void setPKResult(String content, String result){
        llPKResult.setVisibility(View.VISIBLE);
        llWhiteChoose.setVisibility(View.GONE);
        llBlackChoose.setVisibility(View.GONE);
        tvResult.setText(content);
        String sgfStart = "(;GM[1]FF[4]CA[UTF-8]RU[Japanese]SZ[19]KM["
                + pkSettingDialog.getKm() + "]PW["
                + pkSettingDialog.getWhitePlayer() + "]PB["
                + pkSettingDialog.getBlackPlayer() + "]RE["
                + result + "]";
        sgfText = ToolsBox.toSGF(sgfStart, pkSettingDialog.getAb(), goView.getHistoryList());
    }

    /** 取消 */
    private void cancel(){
        btnWhiteRegret.setSelected(false);
        btnWhiteSurrender.setSelected(false);
        btnWhiteConfirm.setSelected(false);
        btnWhiteCancel.setSelected(false);
        btnBlackRegret.setSelected(false);
        btnBlackSurrender.setSelected(false);
        btnBlackConfirm.setSelected(false);
        btnBlackCancel.setSelected(false);
        tvWhiteInfo.setText("");
        tvBlackInfo.setText("");
        llWhiteChoose.setVisibility(View.GONE);
        llBlackChoose.setVisibility(View.GONE);
        btnWhiteConfirm.setVisibility(View.VISIBLE);
        btnWhiteCancel.setVisibility(View.VISIBLE);
        btnBlackConfirm.setVisibility(View.VISIBLE);
        btnBlackCancel.setVisibility(View.VISIBLE);
    }

    /** 結束遊戲 */
    private void gameOver(){
        tvWhiteInfo.setText(activity.getString(R.string.get_dead_chess));
        tvBlackInfo.setText(activity.getString(R.string.get_dead_chess));
        llWhiteChoose.setVisibility(View.VISIBLE);
        llBlackChoose.setVisibility(View.VISIBLE);
        btnWhiteConfirm.setVisibility(View.VISIBLE);
        btnWhiteCancel.setVisibility(View.VISIBLE);
        btnBlackConfirm.setVisibility(View.VISIBLE);
        btnBlackCancel.setVisibility(View.VISIBLE);
        llWhiteMenu.setVisibility(View.GONE);
        llBlackMenu.setVisibility(View.GONE);

        goView.setDeadChessMode();

        btnWhiteConfirm.setOnClickListener(onTemporaryListener);
        btnWhiteCancel.setOnClickListener(onTemporaryListener);
        btnBlackConfirm.setOnClickListener(onTemporaryListener);
        btnBlackCancel.setOnClickListener(onTemporaryListener);
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

    /** 監聽每回合結束 */
    private CheckerBoard.OnTurnOverListener onTurnOverListener = new CheckerBoard.OnTurnOverListener() {
        @Override
        public void onTurnOver(boolean pass) {
            if(pass) {
                if(passTurns+1 == goView.turns) { // 連續兩回合虛手 結束遊戲
                    goView.gameOver();
                    gameOver();
                } else {
                    passTurns = goView.turns;
                }
            } else {
                cancel();
            }
        }
    };

    /** 遊戲結束後偷偷改監聽 */
    private View.OnClickListener onTemporaryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnWhiteConfirm: // 白確認
                case R.id.btnBlackConfirm: // 黑確認
                    v.setSelected(true);
                    if(btnWhiteConfirm.isSelected() && btnBlackConfirm.isSelected()) {
                        int[] resultPlace = goView.judgement();
                        boolean bWin = resultPlace[0] > (resultPlace[1] + pkSettingDialog.getKm());
                        String winner = bWin ? pkSettingDialog.getBlackPlayer() : pkSettingDialog.getWhitePlayer();
                        float winPlace = Math.abs(resultPlace[0] - (resultPlace[1] + pkSettingDialog.getKm()));
                        String result = String.format(activity.getString(R.string.pk_for_place), activity.getString(R.string.info_pb), resultPlace[0]) + "\n"
                                + String.format(activity.getString(R.string.pk_for_place), activity.getString(R.string.info_pw), resultPlace[1]) + "\n"
                                + String.format(activity.getString(R.string.win_for_place)
                                        , winner, winPlace);
                        winner = bWin ? "B+" : "W+";
                        setPKResult(result, winner+winPlace);
                    }
                    break;
                case R.id.btnWhiteCancel: // 白取消
                case R.id.btnBlackCancel: // 黑取消
                    btnWhiteConfirm.setOnClickListener(PKFragment.this);
                    btnWhiteCancel.setOnClickListener(PKFragment.this);
                    btnBlackConfirm.setOnClickListener(PKFragment.this);
                    btnBlackCancel.setOnClickListener(PKFragment.this);
                    llWhiteMenu.setVisibility(View.VISIBLE);
                    llBlackMenu.setVisibility(View.VISIBLE);
                    goView.setNormalMode();
                    goView.resurrection();
                    goView.last(2); // 回復到虛手前
                    cancel();
                    break;
            }
        }
    };

    /** 設定dialog關閉監聽 */
    private DialogInterface.OnDismissListener onDismissListener = new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
            int ab = pkSettingDialog.getAb();
            if(ab > 0) {
                if(ab == 1)
                    Toast.makeText(activity, "沒有在讓1子的啦", Toast.LENGTH_SHORT).show();
                else
                    goView.setAb(ab);
            }
        }
    };

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
            case R.id.btnWhiteRegret: // 白悔棋
                v.setSelected(true);
                tvBlackInfo.setText(activity.getString(R.string.sure_regret));
                llBlackChoose.setVisibility(View.VISIBLE);
                break;
            case R.id.btnBlackRegret: // 黑悔棋
                v.setSelected(true);
                tvWhiteInfo.setText(activity.getString(R.string.sure_regret));
                llWhiteChoose.setVisibility(View.VISIBLE);
                break;
            case R.id.btnWhiteSurrender: // 白投降
                v.setSelected(true);
                tvWhiteInfo.setText(activity.getString(R.string.sure_surrender));
                llWhiteChoose.setVisibility(View.VISIBLE);
                break;
            case R.id.btnBlackSurrender: // 黑投降
                v.setSelected(true);
                tvBlackInfo.setText(activity.getString(R.string.sure_surrender));
                llBlackChoose.setVisibility(View.VISIBLE);
                break;
            case R.id.btnWhiteConfirm: // 白確認
                whiteAgree();
                break;
            case R.id.btnBlackConfirm: // 黑確認
                blackAgree();
                break;
            case R.id.btnWhiteCancel: // 白取消
                cancel();
                break;
            case R.id.btnBlackCancel: // 黑取消
                cancel();
                break;
            case R.id.btnWhitePass: // 白虛手
                if(!goView.isBlackTurn()) {
                    goView.pass();
                } else {
                    tvWhiteInfo.setText(activity.getString(R.string.not_ur_turn));
                    llWhiteChoose.setVisibility(View.VISIBLE);
                    btnWhiteConfirm.setVisibility(View.GONE);
                    btnWhiteCancel.setVisibility(View.GONE);
                }
                break;
            case R.id.btnBlackPass: // 黑虛手
                if(goView.isBlackTurn()) {
                    goView.pass();
                } else {
                    tvBlackInfo.setText(activity.getString(R.string.not_ur_turn));
                    llBlackChoose.setVisibility(View.VISIBLE);
                    btnBlackConfirm.setVisibility(View.GONE);
                    btnBlackCancel.setVisibility(View.GONE);
                }
                break;
            case R.id.btnReviewGame: // 覆盤
                ((MainActivity)activity).goReview(sgfText);
                break;
            case R.id.btnSaveGame: // 儲存棋譜
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(activity, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    ((MainActivity)activity).checkPermission(new String[]{READ_EXTERNAL_STORAGE}, MainActivity.REQUEST_CODE_PKFRAGMENT);
                else
                    showSelectPath();
                break;
            case R.id.btnSelectPath: // 儲存路徑
                try{
                    String name = pkSettingDialog.getBlackPlayer()+"-"+pkSettingDialog.getWhitePlayer()+".sgf";
                    String path = adapter.getPath();
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
