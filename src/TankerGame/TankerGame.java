package TankerGame;

import TankerGame.utils.Recorder;
import TankerGame.window.MainWindow;
import TankerGame.window.MyPanel;

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
 * @date 2022/11/07  11:16
 */
public class TankerGame extends JFrame {
    public static void main(String[] args) {
        new MainWindow();
    }
}
