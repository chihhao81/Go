package go.hao.tw.go.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Hao on 2017/5/27.
 */

public class ChessBook {

    protected HashMap<Integer, ChessBookInfo> hashMap = new HashMap<>();
    protected List<String> abList = new ArrayList<>();
    protected String bPlayer, wPlayer;
    protected float km = 0; // 貼目
    protected int maxTurns;

    public String getbPlayer(){
        return bPlayer;
    }

    public String getwPlayer(){
        return wPlayer;
    }

    public float getKm(){
        return km;
    }

    public int getMaxTurns(){
        return maxTurns;
    }

    public List<String> getAbList(){
        return abList;
    }

    /** 根據手數取得資訊 */
    public ChessBookInfo getChessBookInfo(int index){
        return hashMap.get(index);
    }

    /** 取得註解資訊 */
    public String getMsg(int index){
        if(hashMap.get(index) == null)
            return "";
        return hashMap.get(index).msg;
    }

    public class ChessBookInfo{
        public int x, y;
        public int turns;
        public String time;
        public String msg;
    }
}
