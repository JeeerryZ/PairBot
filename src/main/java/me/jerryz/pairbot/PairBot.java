package me.jerryz.pairbot;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PairBot {

    private static boolean isRunning = false;
    private static final JFrame f = new JFrame();
    private static final JButton b = new JButton();
    private static final JLabel l = new JLabel();

    public static void main(String[] args) {
        l.setBounds(30, 15, 270, 18);
        l.setText("Stopped");
        b.setBounds(30, 35, 230, 40);
        b.setText("Run");
        schedule(); //STARTING THE WHOLE PROCESS
        b.addActionListener(e -> {
            b.setText(isRunning ? "Run" : "Stop");
            isRunning = !isRunning;
            if (isRunning) {
                System.out.println("Starting...");
            } else {
                System.out.println("Stopped");
                l.setText("Stopped");
            }
        });
        f.setSize(300, 140);
        f.setTitle("PairBot");
        f.add(b);
        f.add(l);
        f.setAlwaysOnTop(true);
        f.setResizable(false);
        f.setLayout(null);
        f.setVisible(true);
    }

    public static void schedule() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {

            try {
                if (isRunning) {
                    //BufferedImage image = takeScreenshot();
                    Robot robot = new Robot();
                    Rectangle rectangle = new Rectangle(1400, 882, 480, 44);
                    BufferedImage image = robot.createScreenCapture(rectangle);
                    String text = getTextFromImage(image);
                    System.out.println(text);
                    l.setText(text);
                    if (text.contains("Aval") && text.contains("6/6")) {
                        robot.keyPress(KeyEvent.VK_ESCAPE);
                        robot.keyRelease(KeyEvent.VK_ESCAPE);
                        Thread.sleep(2000);
                        startMatch();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }, 0, 300, TimeUnit.MILLISECONDS);


    }

    public static void startMatch() throws AWTException {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Robot robot = new Robot();
        Runnable openMap = () -> {
            robot.keyPress(KeyEvent.VK_M);
            robot.keyRelease(KeyEvent.VK_M);
            robot.mouseMove(661, 196);
        };
        Runnable clickCrucible = () -> {
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseMove(574, 459);
        };
        Runnable clickTrials = () -> {
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseMove(1645, 889);
        };
        Runnable clickLaunch = () -> {
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        };
        executor.schedule(openMap, 1000, TimeUnit.MILLISECONDS);
        executor.schedule(clickCrucible, 2500, TimeUnit.MILLISECONDS);
        executor.schedule(clickTrials, 4000, TimeUnit.MILLISECONDS);
        executor.schedule(clickLaunch, 5300, TimeUnit.MILLISECONDS);
    }

    public static String getTextFromImage(BufferedImage image) throws TesseractException {
        Tesseract tesseract = new Tesseract();
        tesseract.setTessVariable("user_defined_dpi", "70");
        tesseract.setDatapath("tessdata\\");
        tesseract.setLanguage("por");
        return tesseract.doOCR(image);
    }

    public static BufferedImage takeScreenshot() throws AWTException, IOException {
        Robot robot = new Robot();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle rectangle = new Rectangle(1410, 882, 480, 44);
        BufferedImage image = robot.createScreenCapture(rectangle);
        BufferedImage result = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_BYTE_BINARY);

        Graphics2D graphic = result.createGraphics();
        graphic.drawImage(image, 0, 0, Color.WHITE, null);
        graphic.dispose();
        File outputFile = new File("C:\\Users\\gabri\\Desktop", "image.jpeg");
        ImageIO.write(result, "jpeg", outputFile);
        return result;
    }
}

