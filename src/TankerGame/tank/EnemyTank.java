package TankerGame.tank;

import java.awt.*;
import java.util.Vector;

import static TankerGame.constant.Constant.EnemyTankBulletNum;
import static TankerGame.utils.MyUtils.distanceToPoint;

/**
 * 功能描述
 *
 * @author 今天就把借口用完
 * @date 2022/11/07  12:08
 * <p>
 * //敌人的坦克
 * <p>
 * 敌人坦克发送子弹
 * 1.在敌人坦克类中使用vevtor保存多个shot对象
 * 2.当每创建敌人坦克时，给初始化一个shot对象，同时启动shot
 * 3.在绘制敌人坦克上，要遍历敌人坦克对象vector，绘制所有子弹
 * <p>
 * 4.敌方的坦克也可以自由随机的上下左右移动，把敌人坦克当作线程使用
 */
public class EnemyTank extends Tank implements Runnable {
    public Vector<Shot> shots = new Vector<>();//定义保存多个shot
    public static int enemyTankBulletNum = EnemyTankBulletNum;
    Vector<EnemyTank> enemyTanks = new Vector<>();
    //敌人的坦克需要单独设置一个类 继承Tank
    //第二 敌人坦克有自己特殊的属性与方法
    //第三 我们需要把坦克在画板中显示 因此操作代码在 mypanel
    //第四 敌人坦克数量多，可以放入集合Vector，因为要考虑多线程问题；
    //增加成员，EnemyTank 可以得到敌人坦克的Vector
    //分析
    //1.Vector<EnemyTank> enemyTanks = new Vector();
    //2.提供一个方法 将Mypanel 中 Vector<EnemyTank> enemyTanks = new Vector();
    //  设置到 EnemyTank类中，这样Mypanel初始化多少个EnemyTank 都可以传到 EnemyTank类中的 Vector<EnemyTank> enemyTanks中

    public void setEnemyTanks(Vector<EnemyTank> enemyTanks) {
        this.enemyTanks = enemyTanks;
    }
    //编写方法，判断当前这个坦克，是否和vector enemyTanks中的坦克发生重叠。
    public boolean isTouchEnemyTank() {
        //判断当前坦克的方向
        switch (this.getDirection()) {
            case 0://this坦克上
            case 1://下
                for (int i = 0; i < enemyTanks.size(); i++) {
                    EnemyTank enemyTank = enemyTanks.get(i);
                    //不和自己比较
                    if (enemyTank != this) {
                        //如果敌方（敌方的tank不是只指 enemyTank 只是表示不同于this.Tank）坦克是上/下  将坦克当作直径为60的圆
                        //this坦克的两个点左上角，右上角（myTank.x，myTank.y） （myTank.x+40，myTank.x）
                        //两个点不能进入敌方坦克的体积范围 x:（EnenmyTank.x,EnenmyTank.x+40）
                        //                          y:(EnenmyTank.y,EnenmyTank.y+60)
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
                }
                break;
            case 2://左
            case 3://右
                for (int i = 0; i < enemyTanks.size(); i++) {
                    EnemyTank enemyTank = enemyTanks.get(i);
                    //不和自己比较
                    if (enemyTank != this) {
                        //如果敌方（敌方的tank不是只指 enemyTank 只是表示不同于this.Tank）坦克是上/下  将坦克当作直径为60的圆
                        //this坦克的两个点左上角，右上角（myTank.x，myTank.y） （myTank.x+40，myTank.x）
                        //两个点不能进入敌方坦克的体积范围 x:（EnenmyTank.x,EnenmyTank.x+40）
                        //                          y:(EnenmyTank.y,EnenmyTank.y+60)
                        if (enemyTank.getDirection() == 0 || enemyTank.getDirection() == 1) {
                            //当前坦克的中心点
                            Point pointA = new Point(this.getX() + 30, this.getY() + 20);
                            //其他坦克的中心点
                            Point pointB = new Point(enemyTank.getX() + 20, enemyTank.getY() + 30);
                            if (distanceToPoint(pointA, pointB) <= 60) {
                                return true;
                            }
                        }
                        //如果敌方坦克是左/右  将坦克当作直径为60的圆
                        if (enemyTank.getDirection() == 2 || enemyTank.getDirection() == 3) {
                            //当前坦克的中心点
                            Point pointA = new Point(this.getX() + 30, this.getY() + 20);
                            //其他坦克的中心点
                            Point pointB = new Point(enemyTank.getX() + 30, enemyTank.getY() + 20);
                            if (distanceToPoint(pointA, pointB) <= 60) {
                                return true;
                            }
                        }
                    }
                }
                break;
        }
        return false;
    }

    public EnemyTank(int x, int y) {
        super(x, y);
    }
    @Override
    public void run() {
        //填充子弹
        while (true) {
            if (shots == null || shots.size() < enemyTankBulletNum) {
                Shot shot = null;
                switch (getDirection()) {
                    case 0:
                        shot = new Shot(getX() + 20, getY(), getDirection());
                        shots.add(shot);
                        new Thread(shot).start();
                        break;
                    case 1:
                        shot = new Shot(getX() + 20, getY() + 60, getDirection());
                        shots.add(shot);
                        new Thread(shot).start();
                        break;
                    case 2:
                        shot = new Shot(getX(), getY() + 20, getDirection());
                        shots.add(shot);
                        new Thread(shot).start();
                        break;
                    case 3:
                        shot = new Shot(getX() + 60, getY() + 20, getDirection());
                        shots.add(shot);
                        new Thread(shot).start();
                        break;
                }

            }
            //根据坦克方向继续移动
            switch (getDirection()) {
                case 0:
                    //如果没有与其他enemyTank相撞
                    if (!isTouchEnemyTank()) {
                        for (int i = 0; i < 5; i++) {
                            TankMoveUp();
                            //休眠100ms
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    } else {
                        setDirection(1);
                        for (int i = 0; i < 10; i++) {
                            TankMoveDown();
                            //休眠100ms
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    break;
                case 1:
                    if (!isTouchEnemyTank()) {
                        for (int i = 0; i < 5; i++) {
                            TankMoveDown();
                            //休眠100ms
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    } else {
                        setDirection(0);
                        for (int i = 0; i < 10; i++) {
                            TankMoveUp();
                            //休眠100ms
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    break;
                case 2:
                    if (!isTouchEnemyTank()) {
                        for (int i = 0; i < 5; i++) {
                            TankMoveLeft();
                            //休眠100ms
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    } else {
                        setDirection(3);
                        for (int i = 0; i < 10; i++) {
                            TankMoveRight();
                            //休眠100ms
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    break;
                case 3:
                    if (!isTouchEnemyTank()) {
                        for (int i = 0; i < 5; i++) {
                            TankMoveRight();
                            //休眠100ms
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    } else {
                        setDirection(2);
                        for (int i = 0; i < 10; i++) {
                            TankMoveLeft();
                            //休眠100ms
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    break;
            }

            //休眠100ms
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //然后随机改变坦克的方向 0-3,现在设计 当myTank去追踪enemyTank（未实现 11.18）
            this.setDirection((int) (Math.random() * 4));
            //转弯后休眠0.5秒
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //在并发编程的时候一定要考虑 线程什么时候结束
            if (!(isLive)) {
                break;
            }
            //在创建该对象的时候启动线程
        }
    }
}
