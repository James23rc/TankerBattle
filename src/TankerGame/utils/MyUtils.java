package TankerGame.utils;

import java.awt.*;

/**
 * 功能描述
 *
 * @author 今天就把借口用完
 * @date 2022/11/17  19:39
 */
public class MyUtils {
    //计算两个坦克中心点间的距离，
    public static double distanceToPoint(Point pointA, Point pointB) {
        return Math.sqrt((pointA.x - pointB.x)*(pointA.x - pointB.x)+(pointA.y - pointB.y)*(pointA.y - pointB.y));
    }

    public static boolean isOutWindow(int x,int y){
        if (!(x >= 0 && x <= 1000 && y >= 0 && y <= 750)){
            return true;
        }else {
            return false;
        }
    }
}
