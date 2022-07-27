

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import javax.swing.JFrame;
import javax.swing.JPanel;
//here the game starts running and at the end ti calls the menu

public class BasicsPanel extends JPanel {

    @Serial
    private static final long serialVersionUID = 1L;
    Image Buffer;
    String player;
    mainMenu menu;
    int width, height;
    public Graphics rasterGraphics;


    //set up the graphics
    public void setup() {
        Buffer = this.createImage(1000, 770);
        rasterGraphics = Buffer.getGraphics();
    }

    //constructor for the panel
    public BasicsPanel(int w, int h) {
        width = w;
        height = h;
        menu = new mainMenu(this);
    }

    //Method to draw or to perform the game
    public void draw() {

        long delta = 0;//a filed to see the seconds and generate the ball every 15 seconds

        //ArrayList for the balls with one ball for now
        ArrayList<MultiballWithCollision> multiballs = new ArrayList<MultiballWithCollision>();
        multiballs.add(new MultiballWithCollision(500, 360, 2.0f, 1.6f, Color.red));

        //ArrayList for the boxs of the palyer
        ArrayList<BoxWithCollision> multibox = new ArrayList<BoxWithCollision>();
        multibox.add(new BoxWithCollision(10, 20, 50, 90, Color.red));
        multibox.add(new BoxWithCollision(10, 140, 50, 90, Color.red));
        multibox.add(new BoxWithCollision(10, 260, 50, 90, Color.red));
        multibox.add(new BoxWithCollision(10, 380, 50, 90, Color.red));
        multibox.add(new BoxWithCollision(10, 500, 50, 90, Color.red));
        multibox.add(new BoxWithCollision(10, 620, 50, 90, Color.red));


        //ArrayList for the boxs of the AIpalyer
        ArrayList<BoxWithCollision> multibox2 = new ArrayList<BoxWithCollision>();
        multibox2.add(new BoxWithCollision(930, 20, 50, 90, Color.blue));
        multibox2.add(new BoxWithCollision(930, 140, 50, 90, Color.blue));
        multibox2.add(new BoxWithCollision(930, 260, 50, 90, Color.blue));
        multibox2.add(new BoxWithCollision(930, 380, 50, 90, Color.blue));
        multibox2.add(new BoxWithCollision(930, 500, 50, 90, Color.blue));
        multibox2.add(new BoxWithCollision(930, 620, 50, 90, Color.blue));

        //ArrayList for the boxs of the peddal for both the palyer and the Ai
        ArrayList<Paddle> paddles = new ArrayList<>(Arrays.asList(new playerPaddle(120, 220, 50, 300, Color.red), new aiPaddle(820, 220, 50, 300, Color.blue)));

        //crating a jFrame to to add inputlisneres
        JFrame PBOx = (JFrame) this.getTopLevelAncestor();
        PBOx.addKeyListener(paddles.get(0));

        PBOx.addMouseMotionListener(menu);
        PBOx.addMouseListener(menu);


        //setting a font
        Font font = new Font("Menlo", Font.PLAIN, 30);//

        Menu();//here is a method that is found in this class below  to display a manu
        //every time the Game ends (after displaying the score


        //menu.hard is in the mainMenu class it has a button to make the game hard or
        //essayer if you adjust the float numbers inside this if statement
        if (menu.hard) {

            paddles.get(0).sethard(1.8f);
            paddles.get(1).sethard(0.5f);
            menu.hard = false;//makes the hard disable
            //so that next time the user should select hard to make it hard
        }


        //while loop to run the game
        while (true) {

            long time = System.currentTimeMillis();//starting time
            //breaking if one of them wins
            if (6 - multibox2.size() == 3) {
                player = "multibox2";
                break;
            }
            if (6 - multibox.size() == 3) {
                player = "multibox";
                break;
            }


            //this is the background
            rasterGraphics.setColor(Color.black);
            rasterGraphics.fillRect(0, 0, Buffer.getWidth(null), Buffer.getHeight(null));

            //making the paddles functional (doing what they need to do)
            for (Paddle p : paddles) {
                p.DrawBox(rasterGraphics);
                p.Move();
                p.BoxBounderyAndColluion(multiballs, p);
                p.MoveAI(multiballs, p);
            }


            //drawing the boxes left and right (scoring boxes
            for (BoxWithCollision B : multibox) {
                B.DrawBox(rasterGraphics);
                if (multibox.size() < 6) {
                    MovingScoringBoxes(multibox);//moves the scoring boxes
                }
            }
            for (BoxWithCollision B : multibox2) {
                B.DrawBox(rasterGraphics);
                if (multibox2.size() < 6) {
                    MovingScoringBoxes(multibox2);//moves the scoring boxes
                }
            }

            for (MultiballWithCollision b : multiballs) {
                b.DrawBall(rasterGraphics);//drawing the balls
                b.MoveBall(multiballs);//moving the balls
                //this is for removing the box hit from the array
                for (Iterator<BoxWithCollision> iterator = multibox.iterator(); iterator.hasNext(); ) {
                    BoxWithCollision B = iterator.next();

                    MoveBabox(multiballs, multibox);
                    if (B.equals(getobj()))//checking the box
                    {
                        iterator.remove();//removing
                    }
                }
                for (Iterator<BoxWithCollision> iterator = multibox2.iterator(); iterator.hasNext(); ) {
                    BoxWithCollision B = iterator.next();
                    MoveBabox(multiballs, multibox2);
                    if (B.equals(getobj()))//checking the box
                    {
                        iterator.remove();//removing
                    }
                }
            }


            if (delta >= 1500)//adding a new ball with  other color and velocity every 15 second
            {
                multiballs.add(new MultiballWithCollision(500, 360, (float) Math.random() * 4, (float) Math.random() * 4, Color.getHSBColor((float) Math.random() * 4, (float) Math.random() * 4, (float) Math.random() * 4)));
                delta = 0;
            }
            //displaying the score well playing the game
            rasterGraphics.setFont(font);
            rasterGraphics.setColor(Color.WHITE);

            rasterGraphics.drawString("  \nScore", 450, 40);
            rasterGraphics.setColor(Color.red);
            rasterGraphics.drawString("  \nScore", 448, 38);
            rasterGraphics.setColor(Color.red);
            rasterGraphics.drawString(String.valueOf(6 - multibox2.size()), 450, 70);

            rasterGraphics.setColor(Color.WHITE);
            rasterGraphics.drawString("  to ", 470, 70);
            rasterGraphics.setColor(Color.blue);
            rasterGraphics.drawString("      " + String.valueOf(6 - multibox.size()), 470, 70);
            getGraphics().drawImage(Buffer, 0, 0, null);


            long deltaTime = System.currentTimeMillis() - time;//end time
            delta += deltaTime;//counting
            try {
                Thread.sleep(20 - deltaTime);
            } catch (Exception e) {
            }
        }


        //displaying the winner with a celebrating background for 15 seconds
        if (6 - multibox.size() >= 3) {

            while (true) {
                long time = System.currentTimeMillis();
                rasterGraphics.setColor(Color.getHSBColor((float) Math.random() * 4, (float) Math.random() * 4, (float) Math.random() * 4));
                rasterGraphics.fillRect(0, 0, Buffer.getWidth(null), Buffer.getHeight(null));
                rasterGraphics.setFont(new Font("Roboto", Font.PLAIN, 100));
                rasterGraphics.setColor(Color.blue);
                rasterGraphics.drawString("Player 2(Blue) Wins!!", 20, 200);
                rasterGraphics.drawString("   \nScore   ", 100, 400);
                rasterGraphics.setColor(Color.red);
                rasterGraphics.drawString("   :" + String.valueOf(6 - multibox2.size()), 400, 400);
                rasterGraphics.setColor(Color.red);
                rasterGraphics.drawString("      to  " + String.valueOf(6 - multibox.size()), 450, 400);
                getGraphics().drawImage(Buffer, 0, 0, null);

                if (delta >= 1500) {
                    break;
                }
                long deltaTime = System.currentTimeMillis() - time;
                delta += deltaTime;
                try {
                    Thread.sleep(20 - deltaTime);
                } catch (Exception ignored) {
                }

            }
        } else if (6 - multibox2.size() >= 3) {
            while (true) {
                long time = System.currentTimeMillis();
                rasterGraphics.setColor(Color.getHSBColor((float) Math.random() * 4, (float) Math.random() * 4, (float) Math.random() * 4));
                rasterGraphics.fillRect(0, 0, Buffer.getWidth(null), Buffer.getHeight(null));
                rasterGraphics.setFont(new Font("Roboto", Font.PLAIN, 100));
                rasterGraphics.setColor(Color.red);
                rasterGraphics.drawString("Player 1(Red) Wins!!", 20, 200);
                rasterGraphics.drawString("\nScore", 100, 400);
                rasterGraphics.setColor(Color.yellow);
                rasterGraphics.drawString("\nScore", 98, 398);
                rasterGraphics.setColor(Color.blue);
                rasterGraphics.drawString("\nScore", 100, 400);
                rasterGraphics.drawString(":" + String.valueOf(6 - multibox2.size()), 400, 400);
                rasterGraphics.setColor(Color.blue);
                rasterGraphics.drawString("   to " + String.valueOf(6 - multibox.size()), 450, 400);
                getGraphics().drawImage(Buffer, 0, 0, null);

                if (delta >= 1500) {
                    break;
                }
                long deltaTime = System.currentTimeMillis() - time;
                delta += deltaTime;
                try {
                    Thread.sleep(20 - deltaTime);
                } catch (Exception e) {
                }
            }
        }
		this.draw(); //redraw and start with the menu again
    }


    //this is the Menu method the is used in the draw method
    public void Menu() {
        //ser background
        rasterGraphics.setColor(Color.getHSBColor((float) Math.random() * 4, (float) Math.random() * 4, (float) Math.random() * 4));
        rasterGraphics.fillRect(0, 0, Buffer.getWidth(null), Buffer.getHeight(null));

        if (menu.active) {
            while (menu.active) {
                menu.DDraw(rasterGraphics);
                getGraphics().drawImage(Buffer, 0, 0, null);
            }
            menu.active = true;
        }
    }

    public void MovingScoringBoxes(ArrayList<BoxWithCollision> multibox) {

        for (BoxWithCollision box : multibox) {
            box.velocity.setY(box.speed);

            if (box.location.getY() + box.hi >= 740 || box.location.getY() <= 0) {

                for (BoxWithCollision b : multibox) {
                    b.velocity.setY(b.velocity.getY() * -1f);
                    b.speed *= -1;
                }
            }
            box.location = box.location.add(box.velocity);
        }

    }

    public boolean MoveBabox(ArrayList<MultiballWithCollision> balls, ArrayList<BoxWithCollision> box) {
        for (BoxWithCollision b : box) {
            for (MultiballWithCollision B : balls) {
                if (colliding(B, b)) {
                    B.position = B.position.add(B.Velocity);
                    setobj(b);
                    return true;
                }
            }
        }
        return false;
    }

    private BoxWithCollision bb;
    public void setobj(BoxWithCollision b) {
        bb = b;
    }

    public BoxWithCollision getobj() {
        return bb;
    }

    public boolean colliding(MultiballWithCollision ball, BoxWithCollision box) {
        double xc = ball.position.getX();
        double yc = ball.position.getY();

        double xd = box.location.getX();
        double yd = box.location.getY();
        double xdd = box.location.getX() + box.wi;
        double ydd = box.location.getY() + box.hi;

        double zx = clamp(xd, xdd, xc);
        double zy = clamp(yd, ydd, yc);

        double le = getLength(xc, yc, zx, zy);

        if (le < 25) {
            if (zx == xd && zy == yd) {
                ball.Velocity.setY(ball.Velocity.getY() * -1);
                ball.Velocity.setX(ball.Velocity.getX() * -1);
            }
            if (zx == xdd && zy == ydd) {
                ball.Velocity.setY(ball.Velocity.getY() * -1);
                ball.Velocity.setX(ball.Velocity.getX() * -1);
            }
            if (zx == xd && zy == ydd) {
                ball.Velocity.setY(ball.Velocity.getY() * -1);
                ball.Velocity.setX(ball.Velocity.getX() * -1);
            }
            if (zx == xdd && zy == yd) {
                ball.Velocity.setY(ball.Velocity.getY() * -1);
                ball.Velocity.setX(ball.Velocity.getX() * -1);
            }
            if (zx > xd && zx < xdd && yd >= yc + ball.radius) {
                ball.Velocity.setY(ball.Velocity.getY() * -1);
                ball.Velocity.setX(ball.Velocity.getX() * 1f);
            }
            if (zy > yd && zy < ydd && xd >= xc + 20) {
                ball.Velocity.setX(ball.Velocity.getX() * -1);
                ball.Velocity.setY(ball.Velocity.getY() * 1f);
            }
            if (zx > xd && zx < xdd && ydd >= yc - 20) {
                ball.Velocity.setY(ball.Velocity.getY() * -1);
                ball.Velocity.setX(ball.Velocity.getX() * 1f);
           }
            if (zy > yd && zy < ydd && xdd >= xc - 20) {
                ball.Velocity.setX(ball.Velocity.getX() * -1);
                ball.Velocity.setY(ball.Velocity.getY() * 1f);
            }
            return true;
        }
        return false;
    }

    public double clamp(double min, double max, double value) {
        if (value <= min)
            return min;
        else if (value >= max)
            return max;
        else
            return value;
    }

    public double getLength(double x1, double y1, double x2, double y2) {
        double length = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
        return length;
    }

}
    







 

