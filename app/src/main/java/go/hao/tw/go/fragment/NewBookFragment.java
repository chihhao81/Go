package go.hao.tw.go.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import go.hao.tw.go.R;

/**
 * Created by Hao on 2017/5/28.
 */

public class NewBookFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_new_book, container, false);
        return layout;
    }

    @Override
    protected void buildView() {

    }

    @Override
    public boolean onBackPressed() {
        return true;
    }
}