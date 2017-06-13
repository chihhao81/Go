package go.hao.tw.go.tools;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import go.hao.tw.go.App;
import go.hao.tw.go.R;

/**
 * Created by chihhao on 2017/5/26.
 */

public class SGFChessBook extends ChessBook{

    private final String RU = "RU["; // 規則制度
    private final String SZ = "SZ["; // 棋盤規格
    private final String KM = "KM["; // 貼目
    private final String TM = "TM["; // 時間
    private final String OT = "OT["; // 加時
    private final String PW = "PW["; // 白棋棋手
    private final String PB = "PB["; // 黑棋棋手
    private final String WR = "WR["; // 白棋棋力
    private final String BR = "BR["; // 黑棋棋力
    private final String DT = "DT["; // 日期
    private final String PC = "PC["; // 比賽地點
    private final String RE = "RE["; // 結果
    private final String B  = ";B["; // 黑棋
    private final String W  = ";W["; // 白棋
    private final String BL = "BL["; // 黑棋剩餘時間
    private final String WL = "WL["; // 白棋剩餘時間
    private final String C  = "]C["; // 註解
    private final int ASCII_LOW_A = 97; // ascii code a

    private List<Integer> varyList = new ArrayList<>();

    public SGFChessBook(String str){
        Log.e("Hao", str);
        analysis(new StringBuffer(str));
    }

    private void analysis(StringBuffer stringBuffer){
        int turns = 1;
        getGameContent(stringBuffer);
        while(stringBuffer.indexOf(";") >= 0){
            String str = getInfoString(stringBuffer);
            String key = turns % 2 == 0 ? W : B;
            String position = getStringValue(str, key);
            if(position.isEmpty()) {
                if(str.contains(key))  // 虛手
                    turns++;
                continue;
            }

            if(str.contains("(") && str.contains(")"))
                continue;
            else if(str.contains("("))
                varyList.add(turns);
            else if(str.contains(")")){
                turns = varyList.remove(varyList.size()-1);
                for(int i = turns; i < hashMap.size(); i++)
                    hashMap.remove(i);
                continue;
            }

            ChessBookInfo info = new ChessBookInfo();
            info.turns = turns;
            info.x = position.charAt(0) - ASCII_LOW_A;
            info.y = position.charAt(1) - ASCII_LOW_A;
            if(!getStringValue(str, C).isEmpty()) {
                info.msg = str.substring(str.indexOf(C) + C.length());
            }

            hashMap.put(turns, info);
            turns++;
        }
        maxTurns = turns-1;
    }

    /** 取得比賽資訊 */
    private void getGameContent(StringBuffer stringBuffer){
        stringBuffer.delete(0, stringBuffer.indexOf(";")+1);
        String str = stringBuffer.substring(0, stringBuffer.indexOf(";"));
        stringBuffer.delete(0, stringBuffer.indexOf(";"));
        ChessBookInfo info = new ChessBookInfo();
        info.x = -1;
        info.y = -1;
        info.turns = 0;
        info.time = getGameTime(str);
        info.msg = getGameContent(str, info.time);
        hashMap.put(0, info);
    }

    /** 拆解比賽時間 */
    private String getGameTime(String str){
        String time1, time2;
        time1 = getStringValue(str, TM);
        time2 = getStringValue(str, OT);
        if(time2.contains(" "))
            time2 = time2.substring(0, time2.indexOf(" "));
        return App.context.getString(R.string.info_game_time) + time1 + "+" + time2;
    }

    /** 拆解比賽訊息 */
    private String getGameContent(String str, String time){
        StringBuffer result = new StringBuffer();

        // 黑棋資訊
        result.append(App.context.getString(R.string.info_pb)).append(getStringValue(str, PB))
                .append(String.format("(%s)\n", getStringValue(str, BR)));
        // 白棋資訊
        result.append(App.context.getString(R.string.info_pw)).append(getStringValue(str, PW))
                .append(String.format("(%s)\n", getStringValue(str, WR)));
        // 比賽時間
        result.append(time).append("\n");
        // 貼目
        result.append(App.context.getString(R.string.info_km)).append(Float.parseFloat(getStringValue(str, KM))).append("\n");
        // 比賽結果
        result.append(App.context.getString(R.string.info_re)).append(getStringValue(str, RE));
        // 註解
        if(!getStringValue(str, C).isEmpty()) {
            int indexStart = str.indexOf(C) + C.length();

            String temp = str.substring(indexStart);
            int tempIndex = temp.indexOf(":");
            temp = temp.substring(tempIndex);
            int indexEnd = indexStart+tempIndex+temp.indexOf("]");

            result.append("\n\n").append(str.substring(indexStart, indexEnd));
        }

        return result.toString();
    }

    /** 取得這次資訊 */
    private String getInfoString(StringBuffer stringBuffer) {
        while(true){
            char c = stringBuffer.charAt(0);
            if(c == '(' || c == ';' || stringBuffer.length() == 0)
                break;
            else
                stringBuffer.deleteCharAt(0);
        }

        for(int i = 2; i < stringBuffer.length()-1; i++){
            char c = stringBuffer.charAt(i);
            char ac = stringBuffer.charAt(i+1);

            if(c == 'C' && ac == '[') {
                int size = stringBuffer.length();
                for(int j = i; j < size; j++) {
                    c = stringBuffer.charAt(j);
                    if (c == ']') {
                        if(stringBuffer.charAt(j-1) != '\\') {
                            String result = stringBuffer.substring(0, j);
                            stringBuffer.delete(0, j+1);
                            return result;
                        } else {
                            stringBuffer.deleteCharAt(j-1);
                            j--;
                            size--;
                        }
                    }
                }
            } else if(c == '\n' && (ac == '(' || ac == ';') || (ac == '\n' && i == stringBuffer.length()-1)){
                String result = stringBuffer.substring(0, i);
                stringBuffer.delete(0, i+1);
                return result;
            }
        }

        if(stringBuffer.length() > 0) {
            String result = stringBuffer.toString();
            stringBuffer.delete(0, stringBuffer.length()-1);
            return result;
        } else {
            return "";
        }
    }

    /** 根據key取值 */
    private String getStringValue(String src, String key){
        int index = src.indexOf(key);
        if(index < 0)
            return "";
        src = src.substring(index + key.length());
        index = src.indexOf("]");
        return src.substring(0, index);
    }
}
