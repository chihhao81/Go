package go.hao.tw.go.tools;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

/**
 * Created by Hao on 2017/5/29.
 */

public class SpacesItemDecoration extends ItemDecoration {
    private int left;
    private int right;
    private int bottom;
    private int top;

    public SpacesItemDecoration(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
        outRect.left = this.left;
        outRect.top = this.top;
        outRect.right = this.right;
        outRect.bottom = this.bottom;
    }
}
