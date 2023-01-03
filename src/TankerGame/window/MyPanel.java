package TankerGame.window;

import TankerGame.tank.EnemyTank;
import TankerGame.tank.MyTank;
import TankerGame.tank.Tank;
import TankerGame.tank.Bomb;
import TankerGame.tank.Shot;
import TankerGame.utils.AePlayWave;
import TankerGame.utils.Node;
import TankerGame.utils.Recorder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Iterator;
import java.util.Vector;

import static TankerGame.constant.Constant.*;
import static TankerGame.tank.MyTank.getMyTank;
import static TankerGame.tank.MyTank.myTankBlood;

/**
 * 功能描述
 *
 * @author 今天就把借口用完
 * @date 2022/11/07  11:10
 * <p>
 * 坦克大战的绘图区域
 * <p>
 * 1.
 * public void drawTank(int x,int y,Graphics g,int directionm,int type)：
 * 画出坦克:传入横纵坐标，画笔，坦克方向，以及坦克类型（0代表己方坦克，1代表敌方坦克）
 * 2.为了监听 键盘监听事件，实现keyListener接口，完成对键盘的监听
 */
public class MyPanel extends JPanel implements KeyListener, Runnable {
    //游戏状态
    private int status;//程序状态判断 赋值范围 GAME_init = 1  GAME_FORMER = 2
    private int killTank_Counter = 0;//用于每击杀10个坦克，增加自己坦克血量值
    private static int ememyTankSize = ENEMY_TankSize;
    MyTank myTank = null;//定义己方坦克
    Vector<EnemyTank> enemyTanks = new Vector<>();//设置敌方坦克集合
    //定义一个存放炸弹的vector，当子弹击中坦克时，加入一个bomb对象到bombs
    Vector<Bomb> bombs = new Vector<>();
    //定义一个存放Node对象的Vector，用于恢复敌人的坦克
    Vector<Node> nodes = new Vector<>();
    //定义三张炸弹照片，用于显示爆炸效果
    Image image0 = null;
    Image image1 = null;
    Image image2 = null;
    //定义一张爱心图片，用于表示我方坦克的血量
    Image image3 = null;
    public MyPanel() {
        init(GAME_CHOICE);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.fillRect(0, 0, GAME_WINDOW_WIDTH, WINDOW_HEIGHT);//将游戏界面填充为矩形，默认为黑色
        controlINfo(g);

        switch (status) {
            case GAME_CHOICE:
                startWindow(g);
                break;
            case GAME_INIT:
            case GAME_FORMER:
                if (myTank.isLive) {
                    switch (myTank.getMytankType()){
                        case 0:
                            drawTank(myTank.getX(), myTank.getY(), g, myTank.getDirection(), 0);
                            break;
                        case 2:
                            drawTank(myTank.getX(), myTank.getY(), g, myTank.getDirection(), 2);
                    }
                }
                //遍历画出敌方坦克和敌方坦克子弹
                for (int i = 0; i < enemyTanks.size(); i++) {
                    //从敌方坦克的vector中取出敌方坦克
                    EnemyTank enemyTank = enemyTanks.get(i);
                    //首先判断当前坦克是否存活
                    if (enemyTank.isLive) {
                        drawTank(enemyTank.getX(), enemyTank.getY(), g, enemyTank.getDirection(), 1);
                        //画出坦克的全部子弹
                        for (int j = 0; j < enemyTank.shots.size(); j++) {
                            Shot shot = enemyTank.shots.get(j);
                            if (shot != null && shot.getLive()) {
                                g.fillOval(shot.getX(), shot.getY(), 4, 4);
                            } else {
                                enemyTank.shots.remove(shot);
                            }
                        }
                    } else {
                        enemyTanks.remove(enemyTank);
                    }
                }
                //如果mytank的子弹不为空则画出包含的子弹
                if (myTank.shots != null) {
                    switch (myTank.getMytankType()){
                        case 0:
                            g.setColor(Color.cyan);
                            break;
                        case 2:
                            g.setColor(Color.pink);
                    }
                    for (int i = 0; i < myTank.shots.size(); i++) {
                        Shot shot = myTank.shots.get(i);
                        if (shot != null && shot.getLive() == true) {
                            g.fillOval(shot.getX(), shot.getY(), 4, 4);
                        }
                    }
                }
                //如果bombs集合中有对象就画出
                for (int i = 0; i < bombs.size(); i++) {
                    Bomb bomb = bombs.get(i);
                    if (bomb.life > 6) {
                        g.drawImage(image0, bomb.getX(), bomb.getY(), 60, 60, this);
                    } else if (bomb.life > 3) {
                        g.drawImage(image1, bomb.getX(), bomb.getY(), 60, 60, this);
                    } else {
                        g.drawImage(image2, bomb.getX(), bomb.getY(), 60, 60, this);
                    }
                    bomb.lifeDown();//减小炸弹的生命
                    //如果炸弹的生命周期结束，将炸弹从vector删除
                    if (bomb.isLive() != true) {
                        bombs.remove(bomb);
                    }
                }
                break;
            case GAME_OVER:
                if (EXIT_TIME > 0) {
                    try {
                        exitWindow(g);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    EXIT_TIME--;
                } else {
                    System.exit(0);
                }
                break;
        }
        showInfo(g);
    }
    /*
     * public void hitEnemyTank(Shot s, EnemyTank enemyTank)
     * myTank发出的子弹是否击中敌方
     * 在mypanel.run方法中调用，传入myTank子弹和敌人全部坦克，判断是否打中
     * */
    public void hitEnemyTank(Shot s, Tank tank) {
        //判断是否击中坦克
        switch (tank.getDirection()) {
            case 0://敌人坦克朝上 和朝下
            case 1:
                if (s.getX() >= tank.getX() && s.getX() <= tank.getX() + 40
                        && s.getY() >= tank.getY() && s.getY() <= tank.getY() + 60) {
                    s.setLive(false);
                    //创建Bomb对象，加入到bombs集合
                    if (tank instanceof EnemyTank) {
                        tank.isLive = false;
                        killTank_Counter++;
                        enemyTanks.remove(tank);
                        Bomb bomb = new Bomb(tank.getX(), tank.getY());
                        bombs.add(bomb);
                        Recorder.addhitEnemyTankNum();
                    } else if (tank instanceof MyTank) {
                        myTankBlood--;
                        if (myTankBlood == 0) {
                            status = GAME_OVER;//己方坦克死亡，表示游戏结束
                            tank.isLive = false;
                        }
                    }
                }
                break;
            case 2://敌人坦克朝左 和朝右
            case 3:
                if (s.getX() >= tank.getX() && s.getX() <= tank.getX() + 60
                        && s.getY() >= tank.getY() && s.getY() <= tank.getY() + 40) {
                    s.setLive(false);
                    //创建Bomb对象，加入到bombs集合
                    if (tank instanceof EnemyTank) {
                        tank.isLive = false;
                        killTank_Counter++;
                        enemyTanks.remove(tank);
                        Bomb bomb = new Bomb(tank.getX(), tank.getY());
                        bombs.add(bomb);

                        Recorder.addhitEnemyTankNum();
                    } else if (tank instanceof MyTank) {
                        myTankBlood--;
                        if (myTankBlood == 0) {
                            status = GAME_OVER;
                            tank.isLive = false;
                        }

                    }
                }
                break;
        }
    }

    /*
     * public void hitMyTank(Shot s, Mytank mytank)
     * 敌方Tank发出的子弹是否击中敌方
     * 在mypanel.run方法中调用，传入myTank子弹和敌人全部坦克，判断是否打中
     * */
    public void hitMyTank() {
        //判断enemyTank的shot是否击中敌方坦克
        for (int i = 0; i < enemyTanks.size(); i++) {
            EnemyTank enemyTank = enemyTanks.get(i);
            if (enemyTank.shots != null) {
                //遍历所有坦克
                for (int j = 0; j < enemyTank.shots.size(); j++) {
                    Shot shot = enemyTank.shots.get(j);
                    if (myTank.isLive && shot.getLive()) {
                        hitEnemyTank(shot, myTank);
                    }
                }
            }
        }
    }

    /*
     * public void drawTank(int x,int y,Graphics g,int direction,int type)：
     * 画出坦克:传入横纵坐标，画笔，坦克方向，以及坦克类型（0代表己方坦克，1代表敌方坦克）
     * */
    public void drawTank(int x, int y, Graphics g, int direction, int type) {
        //根据Tank的Type类型绘制颜色
        switch (type) {
            case 0://MyTAnk颜色为青色
                g.setColor(Color.cyan);
                break;
            case 1://enemy Tank为黄色
                g.setColor(Color.yellow);
                break;
            case 2:
                g.setColor(Color.pink);
        }
        //根据坦克的方向绘制tank。
        switch (direction) {//0 1 2 3 分别表示上 下 左 右
            case 0:
                g.fill3DRect(x, y, 10, 60, false);//3d填充tank左轮
                g.fill3DRect(x + 10, y + 10, 20, 40, false);//3d填充tank船舱
                g.fill3DRect(x + 30, y, 10, 60, false);//3d填充tank右轮
                g.fillOval(x + 10, y + 20, 20, 20);//3d填充船舱的圆形
                g.drawLine(x + 20, y + 30, x + 20, y);
                break;
            case 1:
                g.fill3DRect(x, y, 10, 60, false);//3d填充tank左轮
                g.fill3DRect(x + 10, y + 10, 20, 40, false);//3d填充tank船舱
                g.fill3DRect(x + 30, y, 10, 60, false);//3d填充tank右轮
                g.fillOval(x + 10, y + 20, 20, 20);//3d填充船舱的圆形
                g.drawLine(x + 20, y + 30, x + 20, y + 60);//画出炮管
                break;
            case 2://左
                g.fill3DRect(x, y, 60, 10, false);//3d填充tank左轮
                g.fill3DRect(x + 10, y + 10, 40, 20, false);//3d填充tank船舱
                g.fill3DRect(x, y + 30, 60, 10, false);//3d填充tank右轮
                g.fillOval(x + 20, y + 10, 20, 20);//3d填充船舱的圆形
                g.drawLine(x + 30, y + 20, x, y + 20);//画出炮管
                break;
            case 3://右
                g.fill3DRect(x, y, 60, 10, false);//3d填充tank左轮
                g.fill3DRect(x + 10, y + 10, 40, 20, false);//3d填充tank船舱
                g.fill3DRect(x, y + 30, 60, 10, false);//3d填充tank右轮
                g.fillOval(x + 20, y + 10, 20, 20);//3d填充船舱的圆形
                g.drawLine(x + 30, y + 20, x + 60, y + 20);//画出炮管
                break;
            default:
                System.out.println("暂时没有处理");
        }
    }

    public void init(int key) {
        this.status = key;
        //初始化图片对象
        image0 = Toolkit.getDefaultToolkit().getImage(MyPanel.class.getResource("/TankerGame/res/bomb0.png"));
        image1 = Toolkit.getDefaultToolkit().getImage(MyPanel.class.getResource("/TankerGame/res/bomb1.png"));
        image2 = Toolkit.getDefaultToolkit().getImage(MyPanel.class.getResource("/TankerGame/res/bomb2.png"));
        image3 = Toolkit.getDefaultToolkit().getImage(MyPanel.class.getResource("/TankerGame/res/blood.png"));

        //将mypanel 的 enemyTanks 设置给Recorder的 vector
        Recorder.setEnemyTanks(enemyTanks);
        switch (key) {
            case GAME_CHOICE:
                break;
            case GAME_INIT://开启新游戏
                //初始化一些绘图对象
                //初始化我方坦克
                Recorder.setHitEnemyTankNum(0);//设置已打击敌方坦克数
                myTank = getMyTank(GAME_WINDOW_WIDTH-60, WINDOW_HEIGHT-100);
                myTank.setEnemyTank(enemyTanks);//用于判断mytank是否与敌人坦克碰撞
                //初始化敌方坦克
                for (int i = 0; i < ememyTankSize; i++) {
                    EnemyTank enemyTank = new EnemyTank((i + 1) * 150, ((int) (Math.random() * (i + 1)) * 100));
                    enemyTank.setDirection((int) (Math.random() * 4));
                    //加入坦克
                    new Thread(enemyTank).start();
                    enemyTanks.add(enemyTank);
                    // 将Vector<EnemyTank> enemyTanks传给当前坦克 Enemytank的Vector<EnemyTank> enemyTanks
                    //每个坦克都存有一个包含全部敌方坦克的Vector
                    enemyTank.setEnemyTanks(enemyTanks);
                }
                Recorder.setMyTank(myTank);
                break;
            case GAME_FORMER://继续上一局游戏
                //获得Nodes 敌人坐标和方向；
                File file = new File(Recorder.getRecordFile());
                if (file.exists()) {
                    nodes = Recorder.getNodesAndhitEnemyTankNum();
                } else {
                    //文件不存在key == 1 只能开启新游戏
                    key = 1;
                }
                //初始化敌方坦克
                for (int i = 0; i < nodes.size(); i++) {
                    Node node = nodes.get(i);
                    if (node.getBlood() == ememyTankFlag) {
                        EnemyTank enemyTank = new EnemyTank(node.getX(), node.getY());
                        enemyTank.setDirection(node.getDirection());
//            new Thread(shot).start();  现将子弹的产生放在ENEMYTANK类中
                        //加入坦克,坦克线程启动
                        new Thread(enemyTank).start();
                        enemyTanks.add(enemyTank);
                        // 将Vector<EnemyTank> enemyTanks传给当前坦克 Enemytank的Vector<EnemyTank> enemyTanks
                        //每个坦克都存有一个包含全部敌方坦克的Vector
                        enemyTank.setEnemyTanks(enemyTanks);
                    } else {
                        myTank = getMyTank(node.getX(), node.getY());
                        myTank.setDirection(node.getDirection());
                        myTank.setMyTankBlood(node.getBlood());
                        myTank.setEnemyTank(enemyTanks);//用于判断mytank是否与敌人坦克碰撞
                    }
                }
                Recorder.setMyTank(myTank);
                if(Recorder.getHitEnemyTankNum() >= KILLTANK_CIRCLE)
                {
                    myTank.setMytankType(2);
                }
                break;
        }
        if (status == 1 || status == 2) {
            new AePlayWave("src\\TankerGame\\res\\Tanker.wav").start();
        }
    }

    //键盘监听响应
    @Override
    public void keyTyped(KeyEvent e) {
    }

    //上下左右wdsa 键按下的
    @Override
    public void keyPressed(KeyEvent e) {
        if (status == GAME_CHOICE) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_1:
                    init(GAME_INIT);
                    break;
                case KeyEvent.VK_2:
                    init(GAME_FORMER);
                    break;
            }
        }
        if (myTank.isLive) {
            if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
                //获取键盘的按下后，改变坦克的方向
                myTank.setDirection(0);
                myTank.TankMoveUp();
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
                myTank.setDirection(1);
                myTank.TankMoveDown();
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
                myTank.setDirection(2);
                myTank.TankMoveLeft();
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
                myTank.setDirection(3);
                myTank.TankMoveRight();
            }
            //射击
            if (e.getKeyCode() == KeyEvent.VK_J) {
                //获取键盘的按下后，发射子弹
                myTank.shotEnemy();
            }
            //坦克加速
            if (e.getKeyCode() == KeyEvent.VK_K) {
                myTank.setSpeed(10);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_K) {
            myTank.setSpeed(5);
        }
    }

    /*
     *编写该游戏的操作说明
     * */
    public void controlINfo(Graphics g) {
        g.setColor(Color.BLACK);
        Font font = new Font("微软", Font.BOLD, 25);
        g.setFont(font);
        g.fill3DRect(GAME_WINDOW_WIDTH,WINDOW_HEIGHT - 310,WINDOW_WIDTH-GAME_WINDOW_WIDTH,4,false);
        g.drawString("操作说明", GAME_WINDOW_WIDTH + 70, WINDOW_HEIGHT - 280);//y=500
        g.fill3DRect(GAME_WINDOW_WIDTH,WINDOW_HEIGHT - 270,WINDOW_WIDTH-GAME_WINDOW_WIDTH,4,false);
        g.setColor(Color.RED);
        g.drawString("上", GAME_WINDOW_WIDTH + 110, WINDOW_HEIGHT - 220);
        g.drawString("W", GAME_WINDOW_WIDTH + 110, WINDOW_HEIGHT - 190);
        g.drawString("S", GAME_WINDOW_WIDTH + 110, WINDOW_HEIGHT - 160);
        g.drawString("下", GAME_WINDOW_WIDTH + 110, WINDOW_HEIGHT - 130);
        g.drawString("左 A", GAME_WINDOW_WIDTH + 50, WINDOW_HEIGHT - 160);
        g.drawString("D 右", GAME_WINDOW_WIDTH + 140, WINDOW_HEIGHT - 160);
        g.setColor(Color.BLUE);
        g.drawString("按J坦克进行射击", GAME_WINDOW_WIDTH + 50, WINDOW_HEIGHT - 90);
        g.drawString("按K坦克加速", GAME_WINDOW_WIDTH + 50, WINDOW_HEIGHT - 60);
    }

    //编写方法，显示我方击毁敌方坦克信息
    public void showInfo(Graphics g) {
        //画出玩家总成绩
        g.setColor(Color.BLACK);
        Font font = new Font("宋体", Font.BOLD, 25);
        g.setFont(font);
        //绘制击毁坦克信息
        g.drawString("您已累计击毁坦克", GAME_WINDOW_WIDTH + 20, 30);
        drawTank(1020, 50, g, 0, 1);
        g.setColor(Color.BLACK);
        g.drawString(Recorder.getHitEnemyTankNum() + "", GAME_WINDOW_WIDTH + 80, 100);
        //绘制己方坦克血量
        g.drawString("我方坦克血量", GAME_WINDOW_WIDTH + 20, 160);

        //用于设置信息界面的坦克颜色
        if (Recorder.getHitEnemyTankNum()>=KILLTANK_CIRCLE)
        {
            //击杀数大于10
            drawTank(GAME_WINDOW_WIDTH + 20, 180, g, 0, 2);
        }
        else {
            drawTank(GAME_WINDOW_WIDTH + 20, 180, g, 0, 0);
        }

        for (int i = 0; i < myTankBlood; i++) {//这里可以用一个变量来控制血条的多少
            g.drawImage(image3, GAME_WINDOW_WIDTH +  (60 + i*35) , 180, 45, 45, this);
        }
    }


    //编写游戏难度升级参数调整,每击杀10个增加一次难度,敌人数量+2，敌人速度+5，子弹+1
    //我方坦克运行速度+5
    public void upgradeGameDifficulty(){
        ememyTankSize += 1;
        for (EnemyTank enemyTank : enemyTanks) {
            //地方坦克的移动速度
            enemyTank.setSpeed(enemyTank.getSpeed() + 5);
            //地方坦克射击的速度
            for (Shot shot : enemyTank.shots) {
                shot.setSpeed(shot.getSpeed()+5);
            }
            enemyTank.enemyTankBulletNum = enemyTank.enemyTankBulletNum < 5 ? enemyTank.enemyTankBulletNum+1:5;
        }
        myTank.setSpeed(myTank.getSpeed()+5);
        new AePlayWave("src\\TankerGame\\res\\Tanker.wav").start();
    }
    //开始界面
    public void startWindow(Graphics g) {
        //画坦克
        drawTank(GAME_WINDOW_WIDTH / 2 - 140, WINDOW_HEIGHT / 2 - 95, g, 0, 0);
        drawTank(GAME_WINDOW_WIDTH / 2 + 120, WINDOW_HEIGHT / 2 - 95, g, 0, 1);
        //文字边框
        g.setColor(Color.green);
        g.drawRoundRect(GAME_WINDOW_WIDTH / 2 - 70, WINDOW_HEIGHT / 2 - 95, 160, 60, 30, 30);

        g.setColor(Color.GRAY);
        g.drawRoundRect(GAME_WINDOW_WIDTH / 2 - 160, WINDOW_HEIGHT / 2 - 20, 350, 40, 5, 5);
        g.setColor(Color.green);
        g.setFont(new Font("微软雅黑", Font.BOLD, 40));
        g.drawString("坦克大战", GAME_WINDOW_WIDTH / 2 - 70, WINDOW_HEIGHT / 2 - 50);
        g.setColor(Color.orange);
        g.setFont(new Font("微软雅黑", Font.PLAIN, 30));
        g.drawString("1:开启新游戏 2:继续上局", GAME_WINDOW_WIDTH / 2 - 150, WINDOW_HEIGHT / 2 + 10);
    }
    //游戏出局提示，界面停止1秒
    public void exitWindow(Graphics g) throws InterruptedException {
        g.setColor(Color.GREEN);
        g.drawRoundRect(GAME_WINDOW_WIDTH / 2 - 130, WINDOW_HEIGHT / 2 - 95, 340, 50, 5, 5);
        g.setColor(Color.RED);
        g.setFont(new Font("微软雅黑", Font.BOLD, 50));
        g.drawString("GAME OVER", GAME_WINDOW_WIDTH / 2 - 120, WINDOW_HEIGHT / 2 - 50);
    }

    @Override
    public void run() {
        //每隔50ms，重绘panel
        while (true) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (status == GAME_INIT || status == GAME_FORMER) {//判断Mytank的shot是否击中敌方坦克
                for (int i = 0; i < myTank.shots.size(); i++) {
                    if (myTank.shots.get(i) != null && myTank.shots.get(i).getLive()) {
                        //遍历所有坦克
                        for (int j = 0; j < enemyTanks.size(); j++) {
                            EnemyTank enemyTank = enemyTanks.get(j);
                            hitEnemyTank(myTank.shots.get(i), enemyTank);
                        }
                    }
                }
                //判断敌方坦克的shot是否击中Mytank坦克
                hitMyTank();
                //判断己方坦克是否和敌方坦克碰撞,碰撞直接死亡
                if (myTank.isTouchEnemyTanks()) {
                    status = GAME_OVER;
                    myTank.isLive = false;
                    Bomb bomb = new Bomb(myTank.getX(), myTank.getY());
                    bombs.add(bomb);
                }
                //让坦克始终保持在ememyTankSize个数目
                if (enemyTanks.size() < ememyTankSize) {
                    for (int i = enemyTanks.size(); i < ememyTankSize; i++) {
                        EnemyTank enemyTank = new EnemyTank((int) (Math.random() * 900), (int) (Math.random() * 650));
                        enemyTank.setDirection((int) (Math.random() * 4));
                        //加入坦克
                        new Thread(enemyTank).start();
                        enemyTanks.add(enemyTank);
                        // 将Vector<EnemyTank> enemyTanks传给当前坦克 Enemytank的Vector<EnemyTank> enemyTanks
                        enemyTank.setEnemyTanks(enemyTanks);
                        myTank.setEnemyTank(enemyTanks);
                    }
                }
            }
            if (killTank_Counter == KILLTANK_CIRCLE)
            {
                if(myTank.getMyTankBlood() < 5){
                    myTank.setMyTankBlood(myTank.getMyTankBlood()+1);
                }
                myTank.setMytankType(2);
                killTank_Counter = 0;
                upgradeGameDifficulty();
            }
            this.repaint();
        }
    }
}
