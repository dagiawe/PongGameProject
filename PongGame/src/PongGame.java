import javax.swing.JFrame;

public class PongGame {
    public static void main(String arg[]) {

        //set the jFrame
        //  started the menu and the game by calling setup() and draw()
        JFrame frame = new JFrame(" Pong Game");
        frame.setSize(1000, 770);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        BasicsPanel Panel = new BasicsPanel(1000, 770);
        frame.add(Panel);
        frame.setVisible(true);
        Panel.setup();
        Panel.draw();

    }
}
