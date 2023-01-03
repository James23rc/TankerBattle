package TankerGame.window;

import TankerGame.utils.Recorder;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Scanner;

import static TankerGame.constant.Constant.WINDOW_HEIGHT;
import static TankerGame.constant.Constant.WINDOW_WIDTH;

/**
 * 功能描述
 *
 * @author 今天就把借口用完
 * @date 2022/11/17  20:21
 */
public class MainWindow extends JFrame {
    //主方法进入游戏，画板在框架中实现，先定义一个框架Mypanel
    private MyPanel myPanel = null;
    private void init(){
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("请输入你的选择 1:开启新游戏 2:继续上局");
//        String key = scanner.next();
        myPanel = new MyPanel();
        //将mypanel作为一个线程不断重绘画面
        Thread thread = new Thread(myPanel);
        thread.start();
        //将面板加入到框架中
        this.add(myPanel);
        //设置框架的大小1270 750 //画板 1000 750
        this.setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
        //设置点击退出后，正常退出
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //将画板加入键盘事件监听
        this.addKeyListener(myPanel);
        this.setVisible(true);
        //在jFrame中添加相应关闭窗口的处理
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    Recorder.keepRecorder();//关闭窗口的时候就将游戏信息记录到文件中
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                System.out.println("监听到关闭窗口");
            }
        });
    }
    public MainWindow(){
        super("坦克大战");
        init();
    }
}
