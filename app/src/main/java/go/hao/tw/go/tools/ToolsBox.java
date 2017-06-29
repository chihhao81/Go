package go.hao.tw.go.tools;

import android.graphics.Point;

import java.util.HashMap;

import go.hao.tw.go.App;
import go.hao.tw.go.view.CheckerBoard;
import go.hao.tw.go.view.GoView;

/**
 * Created by Hao on 2017/6/19.
 */

public class ToolsBox {

    public static String getString(int id){
        return App.context.getString(id);
    }
    
    public static String toSGF(String startContent, int ab, HashMap<Integer, CheckerBoard.HistoryInfo> historyInfoList){
        StringBuilder stringBuilder = new StringBuilder(startContent);
        char[] ary = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's'};

        if(ab > 1) {
            stringBuilder.append("AB");
            for(int i = 0; i < ab; i++) {
                Point point = GoView.POINTS[i];
                if((ab == 5 || ab == 7) && i == ab-1)
                    point = GoView.POINTS[GoView.POINTS.length-1];
                char x = ary[point.x];
                char y = ary[point.y];
                stringBuilder.append("[").append(x).append(y).append("]");
            }
        }
        stringBuilder.append("\n");

        for(int i = 1; i < historyInfoList.size(); i++){
            CheckerBoard.HistoryInfo info = historyInfoList.get(i);
            if(info == null)
                break;

            char who = i%2 == 1 ? 'B' : 'W';
            if(info.x == -1 || info.y == -1) {
                stringBuilder.append(";").append(who).append("[]\n");
                continue;
            }
            char x = ary[info.x];
            char y = ary[info.y];
            stringBuilder.append(";").append(who).append("[").append(x).append(y).append("]\n");
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
