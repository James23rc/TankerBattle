package TankerGame.tank;

import java.awt.*;

import static TankerGame.constant.Constant.GAME_WINDOW_WIDTH;
import static TankerGame.constant.Constant.WINDOW_HEIGHT;

/**
 * 功能描述
 *
 * @author 今天就把借口用完
 * @date 2022/10/14  12:05
 * 所有坦克的父类
 */
public class Tank {
    private int x;
    private int y;
    private int direction = 0;//定义坦克的方向 0 1 2 3 表示上下左右
    private int speed = 5;//按下键盘后 tank 移动的速度
    public boolean isLive = true;

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDirection() {
        return direction;
    }

    public void TankMoveUp (){
        if (y > speed ) {
            y -= speed;
        }
    }
    public void TankMoveDown (){
        if (y  < WINDOW_HEIGHT - 90 - speed) {
            y += speed;
        }
    }
    public void TankMoveLeft (){
        if (x > speed) {
            x -= speed;
        }

    }
    public void TankMoveRight (){
        if (x  < GAME_WINDOW_WIDTH -60 - speed) {
            x += speed;
        }
    }
    public void setDirection(int direction) {
        this.direction = direction;
    }
    public Tank(int x, int y) {
        this.x = x;
        this.y = y;
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
}
