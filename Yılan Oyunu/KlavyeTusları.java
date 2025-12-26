import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KlavyeTusları implements KeyListener {
    private Yılan yılan;
    private Yılanoyunu game;

    public KlavyeTusları(Yılan yılan, Yılanoyunu game) {
        this.yılan = yılan;
        this.game = game;
    }
    
     public Yılan getYılan() {
        return yılan;
    }

    public void setYılan(Yılan yılan) {
        this.yılan = yılan;
    }


    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_RIGHT && yılan.yonX != -1 && !game.yonDegisti) {
            yılan.yonX = 1;
            yılan.yonY = 0;
            game.yonDegisti = true;
        }

        if (key == KeyEvent.VK_LEFT && yılan.yonX != 1 && !game.yonDegisti) {
            yılan.yonX = -1;
            yılan.yonY = 0;
            game.yonDegisti = true;

        }

        if (key == KeyEvent.VK_UP && yılan.yonY != 1 && !game.yonDegisti) {
            yılan.yonX = 0;
            yılan.yonY = -1;
            game.yonDegisti = true;

        }

        if (key == KeyEvent.VK_DOWN && yılan.yonY != -1 && !game.yonDegisti) {
            yılan.yonX = 0;
            yılan.yonY = 1;
            game.yonDegisti = true;

        }
        if (key == KeyEvent.VK_P) {

            if (game.timer.isRunning()) {
                game.timer.stop();
            } else {
                game.timer.start();
            }

        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");
    }

}
