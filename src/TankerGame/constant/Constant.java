package TankerGame.constant;

/**
 * 功能描述
 *
 * @author 今天就把借口用完
 * @date 2022/11/17  18:00
 */
public class Constant {
    // 界面整体宽度
    public static final int WINDOW_WIDTH = 1270;
    // 界面整体高度
    public static final int WINDOW_HEIGHT = 750;
    // 游戏界面整体宽度 得出 信息界面宽度 1270 - 1000= 270;
    public static final int GAME_WINDOW_WIDTH = 1000;
    // 游戏状态
    public static final int GAME_CHOICE = 0;//0 选择页面
    public static final int GAME_INIT = 1;//1表示选择新游戏 2 表示继续上一局
    public static final int GAME_FORMER = 2;//1表示选择新游戏 2 表示继续上一局
    public static final int GAME_OVER = 3;// 表示游戏结束
    public static int EXIT_TIME = 20;// 游戏结束后 20*50ms = 1000ms = 1s的退出界面展示

    public static final int KILLTANK_CIRCLE = 10;//每击杀10个人，MyTank_Blood的局部变量加+1，最多为5
    //我方坦克初始信息
    public static final int MyTank_Blood = 3;//初始设定为3
    //敌方坦克相关信息

    //地方坦克的一些信息
    public static final int ENEMY_TankSize = 5;
    //地方坦克的子弹数
    public static final int EnemyTankBulletNum = 2;
    //用于文件操作中判断是我方坦克还是地方坦克，地方坦克初始血为 -1表示地方坦克
    public static final int ememyTankFlag = -1;

}
