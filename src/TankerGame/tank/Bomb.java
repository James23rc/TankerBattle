package TankerGame.tank;

/**
 * 功能描述
 *
 * @author 今天就把借口用完
 * @date 2022/11/08  12:47
 * 炸弹：做出爆炸效果，在Panel类中实现
 * 当子弹击中对方时，出现爆炸效果
 *
 */
public class Bomb {
    private int x,y;//炸弹的周期
    public int life = 9;//炸弹的生命周期
    private boolean isLive = true;//判断炸弹生命周期是否还存活

    public Bomb(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
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
    //减少炸弹生命值，实现延时爆炸
    public void lifeDown(){
        if (life > 0){
            life--;
        }else {
            isLive = false;
        }
    }
}
