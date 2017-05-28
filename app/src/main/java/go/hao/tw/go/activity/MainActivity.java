package go.hao.tw.go.activity;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import go.hao.tw.go.R;
import go.hao.tw.go.fragment.LoadBookFragment;
import go.hao.tw.go.fragment.NewBookFragment;
import go.hao.tw.go.fragment.PKFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FragmentManager fragmentManager;

    private FrameLayout flContains;
    private Button btnPK, btnLoadBook, btnNewBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        fragmentManager = getSupportFragmentManager();

        flContains = (FrameLayout)findViewById(R.id.flContains);
        btnPK = (Button)findViewById(R.id.btnPK);
        btnLoadBook = (Button)findViewById(R.id.btnLoadBook);
        btnNewBook = (Button)findViewById(R.id.btnNewBook);

        btnPK.setOnClickListener(this);
        btnLoadBook.setOnClickListener(this);
        btnNewBook.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        if(flContains.isShown()) {
            flContains.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPK:
                fragmentManager.beginTransaction().replace(R.id.flContains, new PKFragment()).commitAllowingStateLoss();
                break;
            case R.id.btnLoadBook:
                fragmentManager.beginTransaction().replace(R.id.flContains, new LoadBookFragment()).commitAllowingStateLoss();
                break;
            case R.id.btnNewBook:
                fragmentManager.beginTransaction().replace(R.id.flContains, new NewBookFragment()).commitAllowingStateLoss();
                break;
        }
        flContains.setVisibility(View.VISIBLE);
    }
}
