package go.hao.tw.go.tools;

import java.util.HashMap;

/**
 * Created by Hao on 2017/5/27.
 */

public class ChessBook {

    protected HashMap<Integer, ChessBookInfo> hashMap = new HashMap<>();
    protected int maxTurns;

    /** 取得結束手數 */
    public int getMaxTurns(){
        return maxTurns;
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
