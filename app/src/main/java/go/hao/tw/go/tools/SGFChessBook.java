package go.hao.tw.go.tools;

import android.util.Log;

import go.hao.tw.go.App;
import go.hao.tw.go.R;

/**
 * Created by chihhao on 2017/5/26.
 */

public class SGFChessBook extends ChessBook{

    private final String RU = "RU"; // 規則制度
    private final String SZ = "SZ"; // Size
    private final String KM = "KM"; // 貼目
    private final String TM = "TM"; // 時間
    private final String OT = "OT"; // 加時
    private final String PW = "PW"; // PlayWhite
    private final String PB = "PB"; // PlayBlack
    private final String WR = "WR"; // 白棋棋力
    private final String BR = "BR"; // 黑棋棋力
    private final String DT = "DT"; // 日期
    private final String PC = "PC"; // 呃 比賽資訊？
    private final String RE = "RE"; // Result
    private final String B  = "temp_black" ; // 黑棋
    private final String W  = "temp_white" ; // 白棋
    private final String BL = "BL"; // 黑棋剩餘時間
    private final String WL = "WL"; // 白棋剩餘時間
    private final String C  = "C" ; // 註解
    private final int ASCII_LOW_A = 97; // ascii code a

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

            ChessBookInfo info = new ChessBookInfo();
            info.turns = turns;
            info.x = position.charAt(0) - ASCII_LOW_A;
            info.y = position.charAt(1) - ASCII_LOW_A;
            if(getStringValue(str, C).length() > 0){
                int indexStart = str.indexOf(C) + C.length() + 1;
                int indexEnd = str.lastIndexOf("]");
                info.msg = str.substring(indexStart, indexEnd);
            }

            hashMap.put(turns, info);
            turns++;
        }
        maxTurns = turns-1;
    }

    /** 取得比賽資訊 */
    private void getGameContent(StringBuffer stringBuffer){
        String str = getInfoString(stringBuffer);
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
        // 比賽結果
        result.append(App.context.getString(R.string.info_re)).append(getStringValue(str, RE));

        return result.toString();
    }

    /** 取得這次資訊 */
    private String getInfoString(StringBuffer stringBuffer){
        int index = stringBuffer.indexOf(";");

        if(index >= 0)
            stringBuffer.delete(0, index+1);
        else
            return "";

        index = stringBuffer.indexOf(";");
        if(index >= 0)
            return stringBuffer.substring(0, index);
        else
            return stringBuffer.toString();
    }

    /** 根據key取值 */
    private String getStringValue(String src, String key){
        int index = src.indexOf(key);
        if(index < 0)
            return "";
        src = src.substring(index + key.length() + 1);
        index = src.indexOf("]");
        return src.substring(0, index);
    }
}
