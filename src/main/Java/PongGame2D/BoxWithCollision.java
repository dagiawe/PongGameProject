package PongGame2D;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
//here is the class for all the boxes  that i use in this project

public class BoxWithCollision extends PongGame implements KeyListener {
    public Color color;
    public int wi, hi;
    public float speed;
    public Vector2D location;
    public Vector2D rectoint;
    public boolean UP, DOWN, RIGHT, LEFT, CLICK;
    public Vector2D velocity;


    public BoxWithCollision(int x, int y, int xx, int yy, Color color) {
        speed = 0.5f;
        wi = xx;
        hi = yy;
        location = new Vector2D();
        velocity = new Vector2D();
        velocity.set(0,2);
        location.set(x, y);
        this.color = color;
    }

    //key listener methods for the player paddle
    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            UP = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            DOWN = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            RIGHT = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            LEFT = true;
        }
    }

    /**
     * Handle the key-released event from the text field.
     */
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            UP = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            DOWN = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            RIGHT = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            LEFT = false;
        }
    }

    //this method is used by the pedal classes off the player and the Ai
//it tells where the ball is about to hit the box
    public void rectpoint(MultiballWithCollision ball, BoxWithCollision box) {
        double xc = ball.position.getX();
        double yc = ball.position.getY();
        double xd = box.location.getX();
        double yd = box.location.getY();
        double xdd = box.location.getX() + box.wi;
        double ydd = box.location.getY() + box.hi;
        double zx = clamp(xd, xdd, xc);
        double zy = clamp(yd, ydd, yc);
        rectoint = new Vector2D((float) zx, (float) zy);
    }

    //this is a method that calculets the lenght between two points
    //it is used by the clases of both the Ai and the player
    public double getLength(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    //this is also used by the classes of both the ai and player
    //it returns in which region around the box is  the ball
    public double clamp(double min, double max, double value) {
        if (value <= min)
            return min;
        else return Math.min(value, max);
    }

    public void Movebox() {
        //Check for collision with another ball

        //Check boundaries for the ball
        if (location.getY() > 710)
            location.setY(20);//up
        else if (location.getY() < 20)
            location.setY(710);//down
        //update the ball's current location
        location = location.add(velocity);
    }
    //this draws the all the boxes in the game
    //it also have a frame for all the box
    public void DrawBox(Graphics g) {
        g.setColor(Color.YELLOW);
        g.drawRoundRect((int) location.getX() - 5, (int) location.getY() - 5, wi + 5, hi + 5, 10, 10);
        g.drawRoundRect((int) location.getX() + 5, (int) location.getY() + 5, wi - 4, hi - 4, 10, 10);
        drawRect(g, (int) location.getX(), (int) location.getY(), wi, hi);
    }

    //this fill draw all the boxes in the game
    public void drawRect(Graphics cg, int xCenter, int yCenter, int wi, int hi) {
        cg.setColor(color);
        cg.fillRect(xCenter, yCenter, wi, hi);
    }
}

//here is the abstract class that is used for both the ai and player paddles
abstract class Paddle extends BoxWithCollision {
    public Paddle(int x, int y, int xx, int yy, Color color) {
        super(x, y, xx, yy, color);
    }

    abstract void sethard(float f);

    abstract void Move();

    abstract void BoxBounderyAndColluion(ArrayList<MultiballWithCollision> balls, BoxWithCollision b);

    abstract void resolveCollision(MultiballWithCollision ball, BoxWithCollision box);

    abstract float CalculateNewVelocityAngle(MultiballWithCollision ball);

    abstract boolean PlayerBallcolliding(MultiballWithCollision ball, BoxWithCollision box);


    public void MoveAI(ArrayList<MultiballWithCollision> multiballs, Paddle p) {
    }
}

class playerPaddle extends Paddle {
    public playerPaddle(int x, int y, int xx, int yy, Color color) {
        super(x, y, xx, yy, color);
    }

    float ballbounespeedP = 1f;//this is to float  make the ball bounds off of the pedalled a speed well

    //the ball and the box collision is resolved automatically
    //this method adds velocity in the direction the player moves
    //this player may move in the direction to the left ,right,up and down(but the Ai can't )
    public void Move() {
        if (UP) {
            velocity = velocity.add(new Vector2D(0, -0.5f));
        }
        if (DOWN) {
            velocity = velocity.add(new Vector2D(0, 0.5f));
        }
        if (RIGHT) {
            velocity = velocity.add(new Vector2D(0.5f, 0));
        }
        if (LEFT) {
            velocity = velocity.add(new Vector2D(-0.5f, 0));
        }
        location = location.add(velocity);
    }

    @Override
    //this method checks the boundary the collusion of the box-with the edge of the game and the balls
    public void BoxBounderyAndColluion(ArrayList<MultiballWithCollision> balls, BoxWithCollision box) {
        for (MultiballWithCollision B : balls) {
            if (PlayerBallcolliding(B, box)) {
                rectpoint(B, box);
                resolveCollision(B, box);
                B.position = B.position.add(B.Velocity);
            }
        }
        if (location.getX() <= 100)
            velocity.setX(velocity.getX() * -0.2f);//right
        else if (location.getX() + wi >= 400)
            velocity.setX(velocity.getX() * -0.2f);//left
        if (location.getY() + hi >= 720)
            velocity.setY(velocity.getY() * -0.2f);//up
        else if (location.getY() <= 0)
            velocity.setY(velocity.getY() * -0.2f);//down
        //location = location.add( velocity );
    }

    //this method is called in the above BoxBounderyAndColluion methoed to resolve the collection
    //between a ball and a box
    @Override
    public void resolveCollision(MultiballWithCollision ball, BoxWithCollision box) {
        Vector2D delta = rectoint.subtract(ball.position);
        float d = delta.getLength();
        Vector2D mtd = delta.multiply(((ball.radius + ball.radius) - d) / d);
        float im1 = 1 ;
        float im2 = 1 ;
        rectoint = rectoint.add(mtd.multiply(im1 / (im1 + im2)));
        ball.position = ball.position.subtract(mtd.multiply(im2 / (im1 + im2)));
        Vector2D v = ball.Velocity;
        float vn = v.dot(mtd.normalize());
        if (vn > 0.0f) return;
        // float i = (-(1.0f +0.3f) * vn) / (im1 + im2);
        // Vector2D impulse = mtd.multiply(i);
        float i = (vn) / (im1 + im2);
        Vector2D impulse = mtd.multiply(i);
        ball.Velocity = ball.Velocity.subtract(impulse.multiply(im2));
        ball.speed *= ballbounespeedP;
        System.out.println(ballbounespeedP);
    }

    //This method tells theles in what dirction that the ball shoud go to make the the angel calculated above
    //the good thing here is that the back of the peddal will make the  x vlocity 0 so that it willnot score
    //on itself
    //it also make the uper and the down edge of the peddal to reflect the ball towards the Ai peddal
    @Override
    public boolean PlayerBallcolliding(MultiballWithCollision ball, BoxWithCollision box) {
        double xc = ball.position.getX();
        double yc = ball.position.getY();
        double xd = box.location.getX();
        double yd = box.location.getY();
        double xdd = box.location.getX() + box.wi;
        double ydd = box.location.getY() + box.hi;
        double zx = clamp(xd, xdd, xc);
        double zy = clamp(yd, ydd, yc);
        double le = getLength(xc, yc, zx, zy);
        float thata = CalculateNewVelocityAngle(ball);
        if (le < 25) {
            if (zx > xd && zx < xdd && yd >= yc + ball.radius) {
                //ball.Velocity.setY(ball.Velocity.getY() * -1f);
                double newVx = Math.abs((Math.cos(thata)) * ball.speed);
                double newVy = (-Math.sin(thata)) * (ball.speed);
                ball.Velocity.setX((float) (newVx));
                ball.Velocity.setY((float) newVy);
                System.out.println("7");
            }
            if (zy > yd && zy < ydd && xd >= xc + ball.radius) {
                if (thata > 0.2) {
                    double newVx = Math.abs((Math.cos(thata)) * ball.speed);
                    double newVy = (-Math.sin(thata)) * (ball.speed);
                    double oldsign = Math.signum(ball.Velocity.getX());
                    ball.Velocity.setX((float) (newVx * (0 * oldsign)));
                    ball.Velocity.setY((float) newVy);
                    System.out.println("1");
                } else {
                    double newVx = Math.abs((Math.cos(thata)) * ball.speed);
                    double newVy = (-Math.sin(thata)) * (ball.speed);
                    double oldsign = Math.signum(ball.Velocity.getX());
                    ball.Velocity.setX((float) (newVx * (0 * oldsign)));
                    ball.Velocity.setY((float) newVy);
                    System.out.println("2");
                }
            } else if (zx >= xd && zx <= xdd && ydd >= yc - ball.radius) {
                //ball.Velocity.setY(ball.Velocity.getY() * -1);
                double newVx = Math.abs((Math.cos(thata)) * ball.speed);
                double newVy = (-Math.sin(thata)) * (ball.speed);
                ball.Velocity.setX((float) newVx);
                ball.Velocity.setY((float) newVy);
                System.out.println("6");
            } else if (zy >= yd && zy <= ydd && xdd >= xc - ball.radius) {
                if (thata > 0.2) {
                    double newVx = Math.abs((Math.cos(thata)) * ball.speed);
                    double newVy = (-Math.sin(thata)) * (ball.speed);
                    double oldsign = Math.signum(ball.Velocity.getX());
                    ball.Velocity.setX((float) (newVx * (-1 * oldsign)));
                    ball.Velocity.setY((float) newVy);
                    System.out.println("3");
                } else if (thata < 0.2 && thata > -0.2) {
                    ball.Velocity.setX(ball.Velocity.getX() * -1);
                    System.out.println("4");
                } else {
                    double newVx = Math.abs((Math.cos(thata)) * ball.speed);
                    double newVy = (-Math.sin(thata)) * (ball.speed);
                    double oldsign = Math.signum(ball.Velocity.getX());
                    ball.Velocity.setX((float) (newVx * (-1 * oldsign)));
                    ball.Velocity.setY((float) newVy);
                    System.out.println("5");
                }
            }
            return true;
        }
        return false;
    }

    //this method call the balles velocity angel afther it hits the peddal
    @Override
    public float CalculateNewVelocityAngle(MultiballWithCollision ball) {
        float RelativeInterseactY = location.getY() + (hi / 2.0f) - (ball.position.getY() + (ball.radius));
        float NormalIntersectY = RelativeInterseactY / (hi / 2.0f);
        float theta = NormalIntersectY * 45;
        return (float) Math.toRadians(theta);
    }

    @Override
    //this sets the ballbounespeed so that the ball will move faster and make the game hard
    public void sethard(float a) {
        ballbounespeedP = a;
        System.out.println("          " + ballbounespeedP);
        // TODO Auto-generated method stub
    }
}

class aiPaddle extends Paddle {
    public aiPaddle(int x, int y, int xx, int yy, Color color) {
        super(x, y, xx, yy, color);
    }

    float ballbounespeedA = 1f;//this is to float  make the ball boundes off of the peddalwith a speed well

    //the ball and the box collion is rsolved automaticaly
    public void Move() {
        if (UP) {
            velocity = velocity.add(new Vector2D(0, -0.2f));
            //up
            if (location.getY() <= 0) {
                velocity.setY(velocity.getY() * 0f);//down
            }
        }
        if (DOWN) {
            velocity = velocity.add(new Vector2D(0, 0.2f));
            if (location.getY() + hi >= 720) {
                velocity.setY(velocity.getY() * 0f);
            }
        }
        if (RIGHT) {
            //velocity=velocity.add(new Vector2D(0.5f,0));
        }
        if (LEFT) {
            //velocity=velocity.add(new Vector2D(-0.5f,0));
        }
        location = location.add(velocity);
    }
    //AI movement code

    @Override
    public void BoxBounderyAndColluion(ArrayList<MultiballWithCollision> balls, BoxWithCollision box) {
        for (MultiballWithCollision B : balls) {
            if (PlayerBallcolliding(B, box)) {
                rectpoint(B, box);
                resolveCollision(B, box);
                B.position = B.position.add(B.Velocity);
            }
        }
        if (location.getX() <= 500) {
            velocity.setX(velocity.getX() * -1f);//right
            location = location.add(velocity);
        } else if (location.getX() + wi >= 1000) {
            velocity.setX(velocity.getX() * -0.5f);
            location = location.add(velocity);
        }//left
        if (location.getY() + hi >= 720)
            velocity.setY(velocity.getY() * -0f);//up
        else if (location.getY() <= 0)
            velocity.setY(velocity.getY() * -0f);//down
        //location = location.add( velocity );
    }

    @Override
    public void resolveCollision(MultiballWithCollision ball, BoxWithCollision box) {
        Vector2D delta = rectoint.subtract(ball.position);
        float d = delta.getLength();
        Vector2D mtd = delta.multiply(((ball.radius + ball.radius) - d) / d);
        float im1 = 1 / 1;
        float im2 = 1 / 1;
        rectoint = rectoint.add(mtd.multiply(im1 / (im1 + im2)));
        ball.position = ball.position.subtract(mtd.multiply(im2 / (im1 + im2)));
        Vector2D v = ball.Velocity;
        float vn = v.dot(mtd.normalize());
        if (vn > 0.0f) return;
        // float i = (-(1.0f +0.3f) * vn) / (im1 + im2);
        // Vector2D impulse = mtd.multiply(i);
        float i = ((1.0f + 0f) * vn) / (im1 + im2);
        Vector2D impulse = mtd.multiply(i);
        ball.Velocity = ball.Velocity.subtract(impulse.multiply(im2));
        ball.speed *= ballbounespeedA;
    }

    @Override
    public float CalculateNewVelocityAngle(MultiballWithCollision ball) {
        float RelativeInterseactY = location.getY() + (hi / 2.0f) - (ball.position.getY() + (ball.radius));
        //System.out.print(RelativeInterseactY);
        float NormalIntersectY = RelativeInterseactY / (hi / 2.0f);
        float theta = NormalIntersectY * 45;
        return (float) Math.toRadians(theta);
    }

    @Override
    public boolean PlayerBallcolliding(MultiballWithCollision ball, BoxWithCollision box) {
        double xc = ball.position.getX();
        double yc = ball.position.getY();
        double xd = box.location.getX();
        double yd = box.location.getY();
        double xdd = box.location.getX() + box.wi;
        double ydd = box.location.getY() + box.hi;
        double zx = clamp(xd, xdd, xc);
        double zy = clamp(yd, ydd, yc);
        double le = getLength(xc, yc, zx, zy);
        float thata = CalculateNewVelocityAngle(ball);
        if (le < 25) {
            if (zx >= xd && zx <= xdd && yd >= yc + ball.radius) {
                //ball.Velocity.setY(ball.Velocity.getY() * -10f);
                double newVy = Math.abs((Math.cos(thata)) * ball.speed);
                double newVx = (-Math.sin(thata)) * (ball.speed);
                ball.Velocity.setX((float) (newVx));
                ball.Velocity.setY((float) newVy);
            }
            if (zy > yd && zy < ydd && xd >= xc + ball.radius) {
                if (thata > 0.2) {
                    double newVx = Math.abs((Math.cos(thata)) * ball.speed);
                    double newVy = (-Math.sin(thata)) * (ball.speed);
                    double oldsign = Math.signum(ball.Velocity.getX());
                    ball.Velocity.setX((float) (newVx * (-1 * oldsign)));
                    ball.Velocity.setY((float) newVy);
                } else if (thata < 0.2 && thata > -0.2) {
                    ball.Velocity.setX(ball.Velocity.getX() * -1);
                } else {
                    double newVx = Math.abs((Math.cos(thata)) * ball.speed);
                    double newVy = (-Math.sin(thata)) * (ball.speed);
                    double oldsign = Math.signum(ball.Velocity.getX());
                    ball.Velocity.setX((float) (newVx * (-1 * oldsign)));
                    ball.Velocity.setY((float) newVy);
                }
            } else if (zx >= xd && zx <= xdd && ydd >= yc - ball.radius) {
                //ball.Velocity.setY(ball.Velocity.getY() * -10);
                double newVy = Math.abs((Math.cos(thata)) * ball.speed);
                double newVx = (-Math.sin(thata)) * (ball.speed);
                double oldsign = Math.signum(ball.Velocity.getX());
                ball.Velocity.setX((float) (newVx * (-1 * oldsign)));
                ball.Velocity.setY((float) newVy);
            } else if (zy > yd && zy < ydd && xdd >= xc - ball.radius) {
                if (thata > 0.2) {
                    double newVx = Math.abs((Math.cos(thata)) * ball.speed);
                    double newVy = (-Math.sin(thata)) * (ball.speed);
                    double oldsign = Math.signum(ball.Velocity.getX());
                    ball.Velocity.setX((float) (newVx * (0 * oldsign)));
                    ball.Velocity.setY((float) newVy);
                } else {
                    double newVx = Math.abs((Math.cos(thata)) * ball.speed);
                    double newVy = (-Math.sin(thata)) * (ball.speed);
                    double oldsign = Math.signum(ball.Velocity.getX());
                    ball.Velocity.setX((float) (newVx * (0 * oldsign)));
                    ball.Velocity.setY((float) newVy);
                }
            }
            return true;
        }
        return false;
    }


    public void MoveAI(ArrayList<MultiballWithCollision> multiballs, Paddle paddlesAi) {
        MultiballWithCollision Ballsnear = null;
        for (MultiballWithCollision ball : multiballs) {
            if (Ballsnear == null) {
                Ballsnear = ball;
            } else if (paddlesAi.location.getX() < ball.position.getX() && ball.position.getX() < Ballsnear.position.getX()) {
                Ballsnear = ball;
            }
        }
        if (paddlesAi.location.getY() + paddlesAi.hi / 2 < Ballsnear.position.getY()) {
            paddlesAi.DOWN = true;
            paddlesAi.UP = false;
        } else if (paddlesAi.location.getY() > Ballsnear.position.getY()) {
            paddlesAi.UP = true;
            paddlesAi.DOWN = false;
        } else {
            paddlesAi.UP = true;
            paddlesAi.DOWN = false;
        }
        paddlesAi.speed = 50f;
        Move();
    }

    @Override
    public void sethard(float a) {
        ballbounespeedA = a;
        System.out.println(ballbounespeedA);
    }
}
