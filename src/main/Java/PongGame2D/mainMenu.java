package PongGame2D;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.*;

//here is the main  menu that to be used by the user to play the Game or to Quit

//and now I am going to run the game
// have fun!!


public class mainMenu implements MouseListener, MouseMotionListener {


    public boolean active;
    public boolean hard = false;
    //button play
    private final Rectangle playBtn;
    private boolean pHighlight = false;
    //button quit
    private final Rectangle quitBtn;
    private boolean qHighlight = false;
    //button hard
    private final Rectangle hardBtn;
    private boolean hHighlight = false;


    private Font font;

    public mainMenu(BasicsPanel Panel) {


        active = true;


        int w, h, x, y;
        w = 300;
        h = 150;
        y = Panel.height / 2 - h / 2;
        x = Panel.width / 4 - w / 2;
        playBtn = new Rectangle(x, y, w, h);
        x = Panel.width * 3 / 4 - w / 2;
        quitBtn = new Rectangle(x, y, w, h);
        y = Panel.height * 3 / 4 - h / 2;
        x = Panel.width / 4 - w / 2;
        w = 350;
        hardBtn = new Rectangle(x, y, w, h);
        font = new Font("Menlo", Font.PLAIN, 110);


    }

    //drawing the buttons and making color changes when touched
    public void DDraw(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        g.setFont(font);
        g.setColor(Color.BLUE);



        if (pHighlight) {
            g.setColor(Color.WHITE);
        }
        g2d.fill(playBtn);

        g.setColor(Color.BLUE);
        if (qHighlight) {
            g.setColor(Color.WHITE);
        }

        g2d.fill(quitBtn);

        g.setColor(Color.BLUE);
        if (hHighlight) {
            g.setColor(Color.WHITE);
        }
        g2d.fill(hardBtn);

        g.setColor(Color.WHITE);
        g2d.draw(playBtn);

        g2d.draw(quitBtn);
        g2d.draw(hardBtn);
        int strWidth;
        int strHeight;

        String playTxt = "Play";
        strWidth = g.getFontMetrics(font).stringWidth(playTxt);
        strHeight = g.getFontMetrics(font).getHeight();
        g.setColor(Color.YELLOW);
        g.drawString(playTxt, (int) (playBtn.getX() + playBtn.getWidth() / 2 - strWidth / 2),
                (int) (playBtn.getY() + playBtn.getHeight() / 2 + strHeight / 4));
        g.setColor(Color.red);
        String quitTxt = "Quit";
        g.drawString(quitTxt, (int) (quitBtn.getX() + quitBtn.getWidth() / 2 - strWidth / 2),
                (int) (quitBtn.getY() + quitBtn.getHeight() / 2 + strHeight / 4));
        g.setColor(Color.MAGENTA);
        String hardTxt = "Hard";
        g.drawString(hardTxt, (int) (hardBtn.getX() + hardBtn.getWidth() / 2 - strWidth / 2),
                (int) (hardBtn.getY() + hardBtn.getHeight() / 2 + strHeight / 4));
        font = new Font("Roboto", Font.PLAIN, 150);
    }

    public void mouseMoved(MouseEvent e) {
        Point p = e.getPoint();
        pHighlight = playBtn.contains(p);
        qHighlight = quitBtn.contains(p);
        hHighlight = hardBtn.contains(p);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
            Point p = e.getPoint();
        if (playBtn.contains(p)) {
            active = false;
            font = new Font("Menlo", Font.PLAIN, 20);
        } else if (quitBtn.contains(p)) {
            System.exit(0);
        } else if (hardBtn.contains(p)) {
            hard = true;
            System.out.println("aaaaa");
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }


}
