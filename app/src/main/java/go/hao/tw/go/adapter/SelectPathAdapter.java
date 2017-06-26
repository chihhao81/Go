package go.hao.tw.go.adapter;

import android.content.Context;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import go.hao.tw.go.App;
import go.hao.tw.go.R;

/**
 * Created by Hao on 2017/6/21.
 */

public class SelectPathAdapter extends RecyclerView.Adapter<SelectPathAdapter.ViewHolder> {

    private final String START_PATH = Environment.getExternalStorageDirectory().getPath();

    private Context context;

    private List<String> fileList = new ArrayList<>();

    private String path = START_PATH;

    public SelectPathAdapter(Context context){
        this.context = context;
        sort();
    }

    /** 資料夾在前 檔案在後 */
    private void sort(){
        fileList.clear();
        File dir = new File(path);
        File[] allFile = dir.listFiles();
        for(File file : allFile)
            if(file.isDirectory() && file.list().length > 0)
                fileList.add(file.getName());

        Collections.sort(fileList, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.charAt(0) - rhs.charAt(0);
            }
        });
        notifyDataSetChanged();
    }

    public boolean back(){
        if(path.equals(START_PATH))
            return true;
        int index = path.lastIndexOf("/");
        path = path.substring(0, index);
        sort();
        return false;
    }

    public String getPath(){
        String temp = path;
        path = START_PATH;
        return temp;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_select_file, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvName.setText(fileList.get(position));
        holder.layout.setTag(position);
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private View layout;
        private TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);

            layout = itemView;
            tvName = (TextView)itemView.findViewById(R.id.tvName);

            int size = App.screenHeight * 15 / 100;
            itemView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, size));
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            path += "/" + fileList.get((int)v.getTag());
            sort();
        }
    }
}