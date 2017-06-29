package go.hao.tw.go.activity;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import go.hao.tw.go.R;
import go.hao.tw.go.fragment.BaseFragment;
import go.hao.tw.go.fragment.LoadBookFragment;
import go.hao.tw.go.fragment.NewBookFragment;
import go.hao.tw.go.fragment.PKFragment;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public final static int REQUEST_CODE_LOADFRAGMENT = 0;
    public final static int REQUEST_CODE_PKFRAGMENT = 1;
    public final static int REQUEST_CODE_NEWFRAGMENT = 2;

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

    /** pk完覆盤 */
    public void goReview(String sgf){
        fragmentManager.beginTransaction().replace(R.id.flContains, LoadBookFragment.newInstance(sgf)).addToBackStack("").commitAllowingStateLoss();
    }

    /** 確認權限 */
    public void checkPermission(String[] permissions, int code){
        List<String> list = new ArrayList<>();
        for(String permission : permissions)
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) //未取得權限，向使用者要求允許權限
                list.add(permission);

        if(list.size() == 0)
            return;

        String [] check = new String[list.size()];
        list.toArray(check);
        ActivityCompat.requestPermissions(this, check, code);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            if (permission.equals(READ_EXTERNAL_STORAGE) && requestCode == REQUEST_CODE_LOADFRAGMENT) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED)
                    fragmentManager.beginTransaction().replace(R.id.flContains, new LoadBookFragment()).addToBackStack("").commitAllowingStateLoss();
                else
                    flContains.setVisibility(View.GONE);
            } else if(permission.equals(READ_EXTERNAL_STORAGE)) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    for(Fragment fragment : fragmentManager.getFragments()) {
                        if (fragment != null && fragment instanceof PKFragment) {
                            ((PKFragment) fragment).showSelectPath();
                            break;
                        } else if(fragment != null && fragment instanceof NewBookFragment){
                            ((NewBookFragment) fragment).showSelectPath();
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(fragmentManager.getBackStackEntryCount() > 0) {
            for(Fragment fragment : fragmentManager.getFragments()) {
                if (fragment != null) {
                    boolean back = ((BaseFragment) fragment).onBackPressed();
                    if(back){
                        fragmentManager.popBackStack();
                        if(fragmentManager.getBackStackEntryCount() == 1)
                            flContains.setVisibility(View.GONE);
                    }
                    break;
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
                fragmentManager.beginTransaction().replace(R.id.flContains, new PKFragment()).addToBackStack("").commitAllowingStateLoss();
                break;
            case R.id.btnLoadBook:
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    checkPermission(new String[]{READ_EXTERNAL_STORAGE}, REQUEST_CODE_LOADFRAGMENT);
                else
                    fragmentManager.beginTransaction().replace(R.id.flContains, new LoadBookFragment()).addToBackStack("").commitAllowingStateLoss();
                break;
            case R.id.btnNewBook:
                fragmentManager.beginTransaction().replace(R.id.flContains, new NewBookFragment()).addToBackStack("").commitAllowingStateLoss();
                break;
        }
        flContains.setVisibility(View.VISIBLE);
    }
}
