package PongGame2D;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

//this is for the balls moving around on the Game
public class MultiballWithCollision {
    public Color color;
    public int radius;
    public Vector2D position, Velocity;
    //These variables are how much we are going to change the current X and Y per loop
    public float speed;
    float save;
    public MultiballWithCollision(int x, int y, float xv, float yv, Color colorin) {
        speed = 10f;
        position = new Vector2D();
        Velocity = new Vector2D();
        position.set(x, y);
        Velocity.set(xv, yv);
        color = colorin;
        radius = 20;
    }
    public void stop()
    {
        save = this.speed;
       this.speed=0;
    }
    public void play()
    {
        this.position.setX(470);
        this.speed=save;
    }

    public void MoveBall(ArrayList<MultiballWithCollision> balls) {
        //Check for collision with another ball
        for (MultiballWithCollision b : balls) {
            if (b != this && this.colliding(b))
                this.resolveCollision(b);
        }
        //Check boundaries for the ball
        if (position.getX() > 970)
            Velocity.setX(Velocity.getX() * -1);//right
        else if (position.getX() < 20)
            Velocity.setX(Velocity.getX() * -1);//left
        if (position.getY() > 710)
            position.setY(20);//up
        else if (position.getY() < 20)
            position.setY(710);//down
        //update the ball's current location
        position = position.add(Velocity);
    }

    public void DrawBall(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawOval((int) position.getX() - 21, (int) position.getY() - 21, 2 * (radius) + 2, 2 * (radius) + 2);
        drawCircle(g, (int) position.getX(), (int) position.getY(), radius);
    }

    public void drawCircle(Graphics cg, int xCenter, int yCenter, int r) {
        cg.setColor(color);
        cg.fillOval(xCenter - r, yCenter - r, 2 * r, 2 * r);
    }

    //Original code author Simucal, http://stackoverflow.com/questions/345838/ball-to-ball-collision-detection-and-handling
    //This just checks collision by getting the distance between each ball
    public boolean colliding(MultiballWithCollision ball) {
        float xd = position.getX() - ball.position.getX();
        float yd = position.getY() - ball.position.getY();
        float sumRadius = radius + ball.radius;
        float sqrRadius = sumRadius * sumRadius;
        float distSqr = (xd * xd) + (yd * yd);
        return distSqr <= sqrRadius;
    }

    //What happens if they collide
    public void resolveCollision(MultiballWithCollision ball) {
        // get the mtd
        Vector2D delta = position.subtract(ball.position);
        float d = delta.getLength();
        // minimum translation distance to push balls apart after intersecting
        Vector2D mtd = delta.multiply(((radius + ball.radius) - d) / d);
        // resolve intersection --
        // inverse mass quantities
        float im1 = 1 / 1; //If the balls have different masses you can use this
        float im2 = 1/ 1; //however, my balls  have the same mass
        // push-pull them apart based off their mass
        position = position.add(mtd.multiply(im1 / (im1 + im2)));
        ball.position = ball.position.subtract(mtd.multiply(im2 / (im1 + im2)));
        // impact speed
        Vector2D v = (this.Velocity.subtract(ball.Velocity));
        float vn = v.dot(mtd.normalize());
        // sphere intersecting but moving away from each other already
        if (vn > 0.0f) return;
        // collision impulse
        float i = (-(1.0f + 0.99f) * vn) / (im1 + im2);
        Vector2D impulse = mtd.multiply(i);
        // change in momentum
        this.Velocity = this.Velocity.add(impulse.multiply(im1));
        ball.Velocity = ball.Velocity.subtract(impulse.multiply(im2));
    }
    /*
     * @param cg = graphics that you are using
     * @param xCenter = X Position
     * @param yCenter = Y Position
     * @param r = radius
     * Author:  Siddhartha Bhattacharya
     */
}

class Vector2D {
    private float x;
    private float y;

    public Vector2D() {
        this.setX(0);
        this.setY(0);
    }

    public Vector2D(float x, float y) {
        this.setX(x);
        this.setY(y);
    }

    public void set(float x, float y) {
        this.setX(x);
        this.setY(y);
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    //Specialty method used during calculations of ball to ball collisions.
    public float dot(Vector2D v2) {
        float result;
        result = this.getX() * v2.getX() + this.getY() * v2.getY();
        return result;
    }

    public float getLength() {
        return (float) Math.sqrt(getX() * getX() + getY() * getY());
    }

    public Vector2D add(Vector2D v2) {
        Vector2D result = new Vector2D();
        result.setX(getX() + v2.getX());
        result.setY(getY() + v2.getY());
        return result;
    }

    public Vector2D subtract(Vector2D v2) {
        Vector2D result = new Vector2D();
        result.setX(this.getX() - v2.getX());
        result.setY(this.getY() - v2.getY());
        return result;
    }

    public Vector2D multiply(float scaleFactor) {
        Vector2D result = new Vector2D();
        result.setX(this.getX() * scaleFactor);
        result.setY(this.getY() * scaleFactor);
        return result;
    }

    //Specialty method used during calculations of ball to ball collisions.
    public Vector2D normalize() {
        float length = getLength();
        if (length != 0.0f) {
            this.setX(this.getX() / length);
            this.setY(this.getY() / length);
        } else {
            this.setX(0.0f);
            this.setY(0.0f);
        }
        return this;
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}