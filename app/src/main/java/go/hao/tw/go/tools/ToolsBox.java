package go.hao.tw.go.tools;

import java.util.HashMap;

import go.hao.tw.go.App;
import go.hao.tw.go.view.CheckerBoard;

/**
 * Created by Hao on 2017/6/19.
 */

public class ToolsBox {

    public static String getString(int id){
        return App.context.getString(id);
    }
    
    public static String toSGF(String startContent, HashMap<Integer, CheckerBoard.HistoryInfo> historyInfoList){
        StringBuilder stringBuilder = new StringBuilder(startContent);
        char[] ary = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's'};
        for(int i = 1; i < historyInfoList.size(); i++){
            CheckerBoard.HistoryInfo info = historyInfoList.get(i);
            if(info == null)
                break;
            if(info.x == -1 || info.y == -1)
                continue;
            char x = ary[info.x];
            char y = ary[info.y];
            char who = i%2 == 1 ? 'B' : 'W';
            stringBuilder.append(";").append(who).append("[").append(x).append(y).append("]\n");
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
