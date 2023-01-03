package TankerGame.tank;


import java.awt.*;
import java.util.Iterator;
import java.util.Vector;

import static TankerGame.constant.Constant.MyTank_Blood;
import static TankerGame.utils.MyUtils.distanceToPoint;

/**
 * 功能描述
 *
 * @author 今天就把借口用完
 * @date 2022/10/14  12:08
 * 11.10设计 自己坦克碰到enemy坦克 自己死亡
 */
public class MyTank extends Tank {
    //定义我方坦克进化的类型，击杀10个以后，坦克类型由0青色，变为类型2 粉红色
    private static int mytankType = 0;
    //定义一个射击对象，表示一个射击行为(线程)
    Shot shot = null;
    public static int myTankBlood = MyTank_Blood;
    public Vector<Shot> shots = new Vector<>();
    //保存敌方坦克的集合用于判断是否与敌方坦克发生碰撞
    Vector<EnemyTank> enemyTanks = new Vector<>();
    private static MyTank myTank=null;
    private MyTank(int x,int y){
        super(x,y);
    };

    public static MyTank getMyTank(int x,int y) {
        if (myTank==null){
            myTank = new MyTank(x,y);
        }
        return myTank;
    }

    public int getMytankType() {
        return mytankType;
    }
    public void setMytankType(int mytankType) {
        this.mytankType = mytankType;
    }

    public int getMyTankBlood() {
        return myTankBlood;
    }
    public void setMyTankBlood(int myTankBlood) {
        this.myTankBlood = myTankBlood;
    }
    public void setEnemyTank(Vector<EnemyTank> enemyTanks){
        this.enemyTanks = enemyTanks;
    }
//    public MyTank(int x,int y) {
//        super(x,y);
//    }
    //射击行为
    public void shotEnemy(){
        //创建shot对象，根据当前myTank对象的位置和方向来创建shot
        switch (getDirection()){
            case 0:
                shot = new Shot(getX()+20,getY(),0);
                shots.add(shot);
                break;
            case 1:
                shot = new Shot(getX()+20,getY()+60,1);
                shots.add(shot);
                break;
            case 2:
                shot = new Shot(getX(),getY()+20,2);
                shots.add(shot);
                break;
            case 3:
                shot = new Shot(getX()+60,getY()+20,3);
                shots.add(shot);
                break;
        }
        shots.add(shot);
        new Thread(shot).start();
    }
    //判断是否碰撞
    public boolean isTouchEnemyTanks(){
        switch (this.getDirection()){
            case 0:
            case 1:
                for (int i = 0; i < enemyTanks.size(); i++) {
                    EnemyTank enemyTank = enemyTanks.get(i);
                    if (enemyTank.getDirection() == 0 || enemyTank.getDirection() == 1) {
                        //当前坦克的中心点
                        Point pointA = new Point(this.getX() + 20, this.getY() + 30);
                        //其他坦克的中心点
                        Point pointB = new Point(enemyTank.getX() + 20, enemyTank.getY() + 30);
                        if (distanceToPoint(pointA, pointB) <= 60) {
                            return true;
                        }
                    }
                    //如果敌方坦克是左/右  将坦克当作直径为60的圆
                    if (enemyTank.getDirection() == 2 || enemyTank.getDirection() == 3) {
                        //当前坦克的中心点
                        Point pointA = new Point(this.getX() + 20, this.getY() + 30);
                        //其他坦克的中心点
                        Point pointB = new Point(enemyTank.getX() + 30, enemyTank.getY() + 20);
                        if (distanceToPoint(pointA, pointB) <= 60) {
                            return true;
                        }
                    }
                }
                break;
            case 2:
            case 3:
                for (int i = 0; i < enemyTanks.size(); i++) {
                    EnemyTank enemyTank = enemyTanks.get(i);
                    //坦克是上下
                    if (enemyTank.getDirection() == 0 || enemyTank.getDirection() == 1){
                        //当前坦克的中心点
                        Point pointA = new Point(this.getX()+30,this.getY()+20);
                        //其他坦克的中心点
                        Point pointB = new Point(enemyTank.getX()+20,enemyTank.getY()+30);
                        if (distanceToPoint(pointA,pointB) <= 60){
                            return true;
                        }
                    }
                    //如果敌方坦克是左/右  将坦克当作直径为60的圆
                    if (enemyTank.getDirection() == 2 || enemyTank.getDirection() == 3){
                    //当前坦克的中心点
                        Point pointA = new Point(this.getX()+30,this.getY()+20);
                        //其他坦克的中心点
                        Point pointB = new Point(enemyTank.getX()+30,enemyTank.getY()+20);
                        if (distanceToPoint(pointA,pointB) <= 60){
                            return true;
                        }
                    }
                }
                break;
        }
        return false;
    }



}
