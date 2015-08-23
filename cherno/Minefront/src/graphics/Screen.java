package graphics;

import java.util.Random;

/**
 * Created by User on 23/08/15.
 */
public class Screen extends Render {

    private Render test;

    public Screen(int width, int height) {
        super(width, height);
        Random randy = new Random();
        test = new Render(256, 256);
        for(int i = 0; i < 256*256; i++){
            test.pixels[i] = randy.nextInt();
        }
    }

    public void render(){
        draw(test, 0, 0);
    }
}
