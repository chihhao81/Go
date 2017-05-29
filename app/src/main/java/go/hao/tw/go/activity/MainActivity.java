package go.hao.tw.go.activity;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import go.hao.tw.go.R;
import go.hao.tw.go.fragment.BaseFragment;
import go.hao.tw.go.fragment.LoadBookFragment;
import go.hao.tw.go.fragment.NewBookFragment;
import go.hao.tw.go.fragment.PKFragment;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

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


    /** 確認權限 */
    public void checkPermission(String[] permissions){
        List<String> list = new ArrayList<>();
        for(String permission : permissions)
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) //未取得權限，向使用者要求允許權限
                list.add(permission);

        if(list.size() == 0)
            return;

        String [] check = new String[list.size()];
        list.toArray(check);
        ActivityCompat.requestPermissions(this, check, 9527);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for(int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            if (permission.equals(READ_EXTERNAL_STORAGE)) {
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED)
                    fragmentManager.beginTransaction().replace(R.id.flContains, new LoadBookFragment()).commitAllowingStateLoss();
                else
                    flContains.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(flContains.isShown()) {
            List<Fragment> list = fragmentManager.getFragments();
            for(Fragment fragment : list){
                if(fragment == null)
                    continue;
                if(((BaseFragment)fragment).onBackPressed()) {
                    fragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss();
                    flContains.setVisibility(View.GONE);
                    return;
                }
            }
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
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    checkPermission(new String[]{READ_EXTERNAL_STORAGE});
                else
                    fragmentManager.beginTransaction().replace(R.id.flContains, new LoadBookFragment()).commitAllowingStateLoss();
                break;
            case R.id.btnNewBook:
                fragmentManager.beginTransaction().replace(R.id.flContains, new NewBookFragment()).commitAllowingStateLoss();
                break;
        }
        flContains.setVisibility(View.VISIBLE);
    }
}
