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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import go.hao.tw.go.App;
import go.hao.tw.go.R;
import go.hao.tw.go.tools.SGFChessBook;

/**
 * Created by Hao on 2017/5/29.
 */

public class SelectFileAdapter extends RecyclerView.Adapter<SelectFileAdapter.ViewHolder> {

    private final String START_PATH = Environment.getExternalStorageDirectory().getPath();

    private Context context;

    private OnSGFSelectedListener onSGFSelectedListener;

    private List<FileInfo> fileList = new ArrayList<>();

    private String path = START_PATH;

    public SelectFileAdapter(Context context){
        this.context = context;
        sort();
    }

    /** 資料夾在前 檔案在後 */
    private void sort(){
        List<FileInfo> dirs = new ArrayList<>();
        List<FileInfo> files = new ArrayList<>();
        File dir = new File(path);
        File[] allFile = dir.listFiles();
        for(File file : allFile){
            if(file.isDirectory() && file.list().length > 0)
                dirs.add(new FileInfo(true, file.getName()));
            else if(!file.isDirectory() && file.getName().endsWith("sgf"))
                files.add(new FileInfo(false, file.getName()));
        }
        Collections.sort(dirs, new Comparator<FileInfo>() {
            @Override
            public int compare(FileInfo lhs, FileInfo rhs) {
                return lhs.name.charAt(0) - rhs.name.charAt(0);
            }
        });
        fileList.clear();
        fileList.addAll(dirs);
        fileList.addAll(files);

        if(path.equals(START_PATH)){
            try {
                String[] local = context.getAssets().list("sgf");
                for(String name : local)
                    fileList.add(new FileInfo(false, true, name));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_select_file, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FileInfo file = fileList.get(position);
        holder.ivFileType.setImageResource(file.isDir ? R.drawable.file_type_folder : R.drawable.file_type_else);
        holder.tvName.setText(file.name);

        holder.layout.setTag(position);
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public void setOnSGFSelectedListener(OnSGFSelectedListener onSGFSelectedListener){
        this.onSGFSelectedListener = onSGFSelectedListener;
    }

    public interface OnSGFSelectedListener {
        void onSelect(SGFChessBook chessBook);
    }

    private class FileInfo {
        boolean isDir, local = false;
        String name;
        FileInfo(boolean isDir, String name){
            this.isDir = isDir;
            this.name = name;
        }
        FileInfo(boolean isDir, boolean local, String name){
            this.isDir = isDir;
            this.local = local;
            this.name = name;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private View layout;
        private ImageView ivFileType;
        private TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);

            layout = itemView;
            ivFileType = (ImageView)itemView.findViewById(R.id.ivFileType);
            tvName = (TextView)itemView.findViewById(R.id.tvName);

            int size = App.screenHeight * 15 / 100;
            itemView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, size));
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            FileInfo info = fileList.get((int)v.getTag());
            if(info.isDir){ // 進下一層
                path += "/" + info.name;
                sort();
            } else if(onSGFSelectedListener != null){
                try {
                    if(info.local) { // assets
                        InputStream inputStream = context.getAssets().open("sgf/"+info.name);
                        int size = inputStream.available();
                        byte[] buffer = new byte[size];
                        inputStream.read(buffer);
                        inputStream.close();
                        onSGFSelectedListener.onSelect(new SGFChessBook(new String(buffer, "UTF-8")));
                    } else { // 檔案
                        File file = new File(path+"/"+info.name);
                        StringBuilder text = new StringBuilder();
                        BufferedReader br = new BufferedReader(new FileReader(file));
                        String line;

                        while ((line = br.readLine()) != null) {
                            text.append(line);
                            text.append('\n');
                        }
                        br.close();
                        onSGFSelectedListener.onSelect(new SGFChessBook(text.toString()));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}