package display;

import graphics.Render;
import graphics.Screen;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * Created by User on 23/08/15.
 */
public class Display extends Canvas implements Runnable {
    public static final int HEIGHT = 800;
    public static final int WIDTH = 1080;
    public static final String TITLE = "MineFront pre Alpha 0.01";



    private Thread thread;
    private Screen screen;
    public boolean running = false;
    private BufferedImage img;
    private int[] pixels;

    public Display(){
        screen = new Screen(WIDTH, HEIGHT);
        img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
    }

    private void start(){
        if (running)
            return;
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    private void stop(){
        if(!running)
            return;

        running = false;
        try{
            // Ends the thread, or throws an exception.
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

    }

    public static void main (String args[]){
        Display game = new Display();
        JFrame frame = new JFrame();

        frame.add(game);

        frame.setTitle(TITLE);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(false);
        frame.setVisible(true);

        game.start();
    }

    @Override
    public void run() {
        int frames = 0;
        double unprocessedSeconds = 0;
        long previousTime = System.nanoTime();
        double secondsPerTick = 1 / 60.0;
        int tickCount = 0;
        boolean ticked = false;
        while (running) {
            long currentTime = System.nanoTime();
            long passedTime = currentTime - previousTime;
            previousTime = currentTime;
            unprocessedSeconds += passedTime / 1000000000.0;

            while(unprocessedSeconds > secondsPerTick) {
                tick();
                unprocessedSeconds -= secondsPerTick;
                ticked = true;
                tickCount++;
                if (tickCount % 60 == 0){
                    System.out.println(frames + " FPS:");
                    previousTime += 1000;
                    frames = 0;
                }
                if(ticked) {
                    render();
                    frames++;
                }
                render();
                frames++;
            }
        }
    }


    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null) {
            createBufferStrategy(3);
            return;
        }
        screen.render();

        for(int i = 0; i < WIDTH * HEIGHT; i++) {
            pixels[i] = screen.pixels[i];
        }

        Graphics g = bs.getDrawGraphics();
        g.drawImage(img, 0, 0, WIDTH, HEIGHT,null);
        g.dispose();
        bs.show();

    }

    private void tick() {
    }
}
