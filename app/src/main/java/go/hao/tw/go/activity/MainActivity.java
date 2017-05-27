package go.hao.tw.go.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import go.hao.tw.go.R;
import go.hao.tw.go.fragment.LoadBookFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FrameLayout flContains;
    private Button btnPK, btnLoadBook, btnNewBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        flContains = (FrameLayout)findViewById(R.id.flContains);
        btnPK = (Button)findViewById(R.id.btnPK);
        btnLoadBook = (Button)findViewById(R.id.btnLoadBook);
        btnNewBook = (Button)findViewById(R.id.btnNewBook);

        btnPK.setOnClickListener(this);
        btnLoadBook.setOnClickListener(this);
        btnNewBook.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPK:
                break;
            case R.id.btnLoadBook:
                getSupportFragmentManager().beginTransaction().replace(R.id.flContains, new LoadBookFragment()).commitAllowingStateLoss();
                break;
            case R.id.btnNewBook:
                break;
        }
        flContains.setVisibility(View.VISIBLE);
    }
}
