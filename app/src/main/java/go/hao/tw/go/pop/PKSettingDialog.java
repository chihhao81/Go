package go.hao.tw.go.pop;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import go.hao.tw.go.App;
import go.hao.tw.go.R;

/**
 * Created by chihhao on 2017/6/14.
 */

public class PKSettingDialog extends Dialog {

    private View layout;
    private EditText etBlackPlayer, etWhitePlayer, etKm;
    private Button btnConfirm;

    public PKSettingDialog(@NonNull Context context) {
        super(context, R.style.dialog);

        layout = LayoutInflater.from(context).inflate(R.layout.dialog_pk_setting, null);

        etBlackPlayer = (EditText)layout.findViewById(R.id.etBlackPlayer);
        etWhitePlayer = (EditText)layout.findViewById(R.id.etWhitePlayer);
        etKm = (EditText)layout.findViewById(R.id.etKm);
        btnConfirm = (Button)layout.findViewById(R.id.btnConfirm);

        btnConfirm.setLayoutParams(new LinearLayout.LayoutParams(App.screenWidth*8/10, ViewGroup.LayoutParams.WRAP_CONTENT));
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        setContentView(layout);
    }

    public String getBlackPlayer(){
        return etBlackPlayer.getText().length() == 0 ? etBlackPlayer.getHint().toString() : etBlackPlayer.getText().toString();
    }

    public String getWhitePlayer(){
        return etWhitePlayer.getText().length() == 0 ? etWhitePlayer.getHint().toString() : etWhitePlayer.getText().toString();
    }

    public float getKm(){
        return Float.parseFloat(etKm.getText().length() == 0 ? etKm.getHint().toString() : etKm.getText().toString());
    }
}
