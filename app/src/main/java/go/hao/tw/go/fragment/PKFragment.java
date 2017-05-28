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

import go.hao.tw.go.App;
import go.hao.tw.go.R;
import go.hao.tw.go.view.BaseDataView;
import go.hao.tw.go.view.GoView;

/**
 * Created by chihhao on 2017/5/26.
 */

public class PKFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout llWhiteChoose, llBlackChoose;
    private GoView goView;
    private ImageView ivWhite, ivBlack;
    private TextView tvWhiteInfo, tvBlackInfo;
    private Button btnWhiteRegret, btnWhiteSurrender, btnWhitePass, btnWhiteConfirm, btnWhiteCancel;
    private Button btnBlackRegret, btnBlackSurrender, btnBlackPass, btnBlackConfirm, btnBlackCancel;

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
        llWhiteChoose = (LinearLayout)findViewById(R.id.llWhiteChoose);
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

    /** 白色同意 - 黑悔棋 / 白投降 */
    private void whiteAgree(){
        if(btnBlackRegret.isSelected())
            ;
        else if(btnWhiteSurrender.isSelected())
            ;
        cancel();
    }

    /** 黑色同意 - 白悔棋 / 黑投降 */
    private void blackAgree(){
        if(btnWhiteRegret.isSelected())
            ;
        else if(btnBlackSurrender.isSelected())
            ;
        cancel();
    }

    /** 取消 */
    private void cancel(){
        btnWhiteRegret.setSelected(false);
        btnWhiteSurrender.setSelected(false);
        btnBlackRegret.setSelected(false);
        btnBlackSurrender.setSelected(false);
        tvWhiteInfo.setText("");
        tvBlackInfo.setText("");
        llWhiteChoose.setVisibility(View.GONE);
        llBlackChoose.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnWhiteRegret:
                tvBlackInfo.setText(activity.getString(R.string.sure_regret));
                llBlackChoose.setVisibility(View.VISIBLE);
                break;
            case R.id.btnBlackRegret:
                tvWhiteInfo.setText(activity.getString(R.string.sure_regret));
                llWhiteChoose.setVisibility(View.VISIBLE);
                break;
            case R.id.btnWhiteSurrender:
                tvWhiteInfo.setText(activity.getString(R.string.sure_surrender));
                llWhiteChoose.setVisibility(View.VISIBLE);
                break;
            case R.id.btnBlackSurrender:
                tvBlackInfo.setText(activity.getString(R.string.sure_surrender));
                llBlackChoose.setVisibility(View.VISIBLE);
                break;
            case R.id.btnWhiteConfirm:
                whiteAgree();
                break;
            case R.id.btnBlackConfirm:
                blackAgree();
                break;
            case R.id.btnWhiteCancel:
                cancel();
                break;
            case R.id.btnBlackCancel:
                cancel();
                break;
            case R.id.btnWhitePass:
            case R.id.btnBlackPass:
                goView.pass();
                break;
        }
    }
}
