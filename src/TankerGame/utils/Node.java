package TankerGame.utils;

import static TankerGame.constant.Constant.ememyTankFlag;

/**
 * 功能描述
 *
 * @author 今天就把借口用完
 * @date 2022/11/11  13:57
 * 一个Node对象，表示一个敌人坦克的信息
 *
 */
public class Node {
    private int x;
    private int y;
    private int direction;

    private int blood = ememyTankFlag;//初始值为-1
    public Node(int x, int y, int direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public int getBlood() {
        return blood;
    }

    public void setBlood(int blood) {
        this.blood = blood;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
