package TankerGame.utils;

import TankerGame.tank.EnemyTank;
import TankerGame.tank.MyTank;

import java.io.*;
import java.util.Vector;

/**
 * 功能描述
 *
 * @author 今天就把借口用完
 * @date 2022/11/10  20:16
 * 记录相关信息，和文件交互
 */
public class Recorder {
    //静态变量，记录我方击毁敌方坦克数量
    private static int hitEnemyTankNum = 0;
    //定义IO对象
    private static FileWriter fw = null;
    private static BufferedWriter bw = null;
    private static BufferedReader br = null;
    private static String recordFile = "src\\TankerGame\\res\\playerRecord.txt";
    //定义敌方坦克Vector 获取敌方坦克的坐标与方向
    public static Vector<EnemyTank> enemyTanks = null;
    //定义MYTank 获取我方坦克的坐标 方向 血量
    public static MyTank myTank = null;

    public static String getRecordFile() {
        return recordFile;
    }

    //定义以一个Node Vector 将文件中存储的信息取出来 放入到每一个node 节点中，实现恢复上一局功能
    private static Vector<Node> nodes = new Vector<>();

    public static void setMyTank(MyTank myTank) {
        Recorder.myTank = myTank;
    }


    public static void setEnemyTanks(Vector<EnemyTank> enemyTanks) {
        Recorder.enemyTanks = enemyTanks;
    }

    //编写一个方法，读取playerRecord.txt 中的信息；
    //该方法在游戏启动的时候调用
    public static Vector<Node> getNodesAndhitEnemyTankNum() {
        try {
            String line = "";
            String[] xydb = line.split(" ");
            br = new BufferedReader(new FileReader(recordFile));
            //读取击杀对方坦克数量
            hitEnemyTankNum = Integer.parseInt(br.readLine());
            //读取myTank位置和血量
            line = br.readLine();
            xydb = line.split(" ");
            Node nodeMytank = new Node(Integer.parseInt(xydb[0]), Integer.parseInt(xydb[1]),
                    Integer.parseInt(xydb[2]));
            nodeMytank.setBlood(Integer.parseInt(xydb[3]));
            nodes.add(nodeMytank);

            //循环读取敌方坦克文件，生成Node集合
            while ((line = br.readLine()) != null){
                String[] xyd = line.split(" ");
                Node node = new Node(Integer.parseInt(xyd[0]), Integer.parseInt(xyd[1]),
                            Integer.parseInt(xyd[2]));
                nodes.add(node);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            if (br != null){
                try {
                    br.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return nodes;
    }

    //增加一个方法，当游戏退出时，将hitEnemyTankNum ，敌人坦克的坐标，敌人坦克的方向
    //保存到playerRecord.txt文件中
    public static void keepRecorder() throws IOException {
        bw = new BufferedWriter(new FileWriter(recordFile));
        try {
            //写入击杀敌方坦克数
            bw.write(hitEnemyTankNum + "\r\n");
            //写入我方坦克的位置，方向，以及血量
            String mytankRecord = myTank.getX() + " "
                                    + myTank.getY() + " "
                                    + myTank.getDirection() +" "
                                    + myTank.getMyTankBlood();
            bw.write(mytankRecord);
            bw.write("\r\n");

            //遍历敌人坦克 Vector
            for (int i = 0; i < enemyTanks.size(); i++) {
                EnemyTank enemyTank = enemyTanks.get(i);
                if (enemyTank.isLive) {
                    String enemyTankRecord = enemyTank.getX() + " "
                                            + enemyTank.getY() + " " +
                                                enemyTank.getDirection();
                    bw.write(enemyTankRecord);
                    bw.write("\r\n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (bw != null) {
                bw.close();
            }
        }
    }

    public static int getHitEnemyTankNum() {
        return hitEnemyTankNum;
    }

    public static void setHitEnemyTankNum(int hitEnemyTankNum) {
        Recorder.hitEnemyTankNum = hitEnemyTankNum;
    }

    public static void addhitEnemyTankNum() {
        Recorder.hitEnemyTankNum++;
    }
}
