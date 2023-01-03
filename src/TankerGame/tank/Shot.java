package TankerGame.tank;

import TankerGame.utils.MyUtils;

/**
 * 功能描述
 *
 * @author 今天就把借口用完
 * @date 2022/10/21  16:46
 *
 * 设计 子弹
 */
public class Shot implements Runnable {
    private int x;//子弹发出的横坐标
    private int y;//子弹发出的纵坐标
    private int direction = 0;//子弹方向
    private int speed = 10;//子弹速度
    private Boolean isLive = true;//子弹是否存活
    public Shot(int x, int y, int direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
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

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public Boolean getLive() {
        return isLive;
    }

    public void setLive(Boolean live) {
        isLive = live;
    }

    @Override
    public void run() {//射击行为
        while (isLive){
            //子弹休眠
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //根据方向改变子弹 X，Y坐标
            switch (direction) {
                case 0:
                    y -= speed;
                    break;
                case 1:
                    y += speed;
                    break;
                case 2:
                    x -= speed;
                    break;
                case 3:
                    x += speed;
                    break;
            }
//            System.out.println("x:" + x + " y:" + y);//当子弹到画板边界 销毁子弹
            if (MyUtils.isOutWindow(x,y)) {
                isLive = false;
            }
            if (!isLive){
                break;
            }
        }
    }
}
