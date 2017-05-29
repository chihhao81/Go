package go.hao.tw.go.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import go.hao.tw.go.App;
import go.hao.tw.go.R;
import go.hao.tw.go.view.BaseDataView;
import go.hao.tw.go.view.CheckerBoard;
import go.hao.tw.go.view.GoView;

/**
 * Created by chihhao on 2017/5/26.
 */

public class PKFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout llWhiteMenu, llWhiteChoose, llBlackMenu, llBlackChoose;
    private GoView goView;
    private ImageView ivWhite, ivBlack;
    private TextView tvWhiteInfo, tvBlackInfo;
    private Button btnWhiteRegret, btnWhiteSurrender, btnWhitePass, btnWhiteConfirm, btnWhiteCancel;
    private Button btnBlackRegret, btnBlackSurrender, btnBlackPass, btnBlackConfirm, btnBlackCancel;

    private int passTurns; // 檢查是不是連續虛手結束比賽

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
        goView = (GoView)findViewById(R.id.goView);
        ivWhite = (ImageView)findViewById(R.id.ivWhite);
        ivBlack = (ImageView)findViewById(R.id.ivBlack);
        tvWhiteInfo = (TextView)findViewById(R.id.tvWhiteInfo);
        tvBlackInfo = (TextView)findViewById(R.id.tvBlackInfo);
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

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(App.screenWidth, App.screenWidth);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        goView.setLayoutParams(params);

        int space = (int)BaseDataView.SPACE;
        int height = (App.screenHeight - App.screenWidth) / 2 - space;
        params = new RelativeLayout.LayoutParams(height, height);
        params.addRule(RelativeLayout.ABOVE, R.id.goView);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.setMargins(space, 0, 0, 0);
        ivWhite.setLayoutParams(params);

        params = new RelativeLayout.LayoutParams(height, height);
        params.addRule(RelativeLayout.BELOW, R.id.goView);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.setMargins(0, 0, space, 0);
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
    }

    /** 白色同意 */
    private void whiteAgree(){
        if(btnBlackRegret.isSelected()) { // 黑悔棋
            goView.last(goView.isBlackTurn() ? 2 : 1); // 回到上一次黑棋下的地方
        } else if(btnWhiteSurrender.isSelected()) { // 白投降
            goView.gameOver();
            llWhiteMenu.setVisibility(View.GONE);
            llBlackMenu.setVisibility(View.GONE);
            Toast.makeText(activity, "黑勝", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(activity, "白勝", Toast.LENGTH_SHORT).show();
        }
        cancel();
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
        llWhiteMenu.setVisibility(View.GONE);
        llBlackMenu.setVisibility(View.GONE);

        btnWhiteConfirm.setOnClickListener(onTemporaryListener);
        btnWhiteCancel.setOnClickListener(onTemporaryListener);
        btnBlackConfirm.setOnClickListener(onTemporaryListener);
        btnBlackCancel.setOnClickListener(onTemporaryListener);
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
                    if(btnWhiteConfirm.isSelected() && btnBlackConfirm.isSelected()){
                        Toast.makeText(activity, "結束", Toast.LENGTH_SHORT).show();
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
                    goView.resurrection();
                    goView.last(2); // 回復到虛手前
                    cancel();
                    break;
            }
        }
    };

    @Override
    public boolean onBackPressed() {
        return true;
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
                if(goView.turns % 2 == 0) {
                    goView.pass();
                } else {
                    tvWhiteInfo.setText(activity.getString(R.string.not_ur_turn));
                    llWhiteChoose.setVisibility(View.VISIBLE);
                    btnWhiteConfirm.setVisibility(View.GONE);
                    btnWhiteCancel.setVisibility(View.GONE);
                }
                break;
            case R.id.btnBlackPass: // 黑虛手
                if(goView.turns % 2 == 1) {
                    goView.pass();
                } else {
                    tvBlackInfo.setText(activity.getString(R.string.not_ur_turn));
                    llBlackChoose.setVisibility(View.VISIBLE);
                    btnBlackConfirm.setVisibility(View.GONE);
                    btnBlackCancel.setVisibility(View.GONE);
                }
                break;
        }
    }
}
