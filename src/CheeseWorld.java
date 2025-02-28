//Import Section
//Add Java libraries needed for the game

import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.*;
import javax.swing.*;

public class CheeseWorld implements Runnable {

    //Variable Definition Section

    //Sets the width and height of the program window
    final int WIDTH = 1000;
    final int HEIGHT = 700;

    //Declare the variables needed for the graphics
    public JFrame frame;
    public Canvas canvas;
    public JPanel panel;
    public BufferStrategy bufferStrategy;

    public boolean gameStart = false;

    //Declare the character objects
    public Mouse jerry;
    public Cheese cheese;
    public Cat tom;

    public String winner = "";
    public boolean gameOver = false;

    // Main method definition
    // This is the code that runs first and automatically
    public static void main(String[] args) {
        CheeseWorld myApp = new CheeseWorld();   //creates a new instance of the game
        new Thread(myApp).start();               //creates a threads & starts up the code in the run( ) method
    }

    // Constructor Method - setup portion of the program
    // Initialize your variables and construct your program objects here.
    public CheeseWorld() {

        setUpGraphics();

        //create (construct) the objects needed for the game
        jerry = new Mouse(200, 300, 0, 0);
        cheese = new Cheese(400, 300, 3, -4);
        tom = new Cat(650, 250, 0, 0);

        //load images
        cheese.pic = Toolkit.getDefaultToolkit().getImage("cheese.gif");
        jerry.pic = Toolkit.getDefaultToolkit().getImage("jerry.gif");
        tom.pic = Toolkit.getDefaultToolkit().getImage("tomCat.png");

    } // CheeseWorld()


//*******************************************************************************

    // main thread
    // this is the code that plays the game after you set things up
    public void run() {
        while (true) {
            if (gameOver == false) {
                moveThings();           //move all the game objects
            }
            checkIntersections();   // check character crashes
            render();               // paint the graphics
            pause(20);         // sleep for 20 ms
        }
    }

    public void moveThings() {
        jerry.move();
        cheese.move();
        tom.move();
    }

    public void checkIntersections() {
        if (tom.rec.intersects(jerry.rec)) {
            jerry.isAlive = false;
            winner = "Tom";
            gameOver = true;
        }
        if (jerry.rec.intersects(cheese.rec)) {
            cheese.isAlive = false;
            winner = "Jerry";
            gameOver = true;
        }
        if (tom.rec.intersects(cheese.rec)) {
            tom.isAlive = false;
            winner = "cheese";
            gameOver = true;
        }
    }

    //paints things on the screen using bufferStrategy
    public void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);

        int textX = 50;
        int textY = 50;
        g.setFont(new Font("TimesRoman", Font.BOLD, 20));
        g.drawString("Tom tries to get Jerry and avoid cheese", textX, textY);
        g.drawString("Jerry tries to eat the cheese and avoid Tom", textX, textY + 30);
        g.drawString("First one to succeed wins!",textX, textY + 60);

        // draw characters to the screen (only draw if they are alive)
        if (jerry.isAlive == true) {
            g.drawImage(jerry.pic, jerry.xpos, jerry.ypos, jerry.width, jerry.height, null);
        }
        if (cheese.isAlive == true) {
            g.drawImage(cheese.pic, cheese.xpos, cheese.ypos, cheese.width, cheese.height, null);
        }
        if (tom.isAlive == true) {
            g.drawImage(tom.pic, tom.xpos, tom.ypos, tom.width, tom.height, null);
        }

        if (gameOver == true) {
            g.drawString(winner + " wins!!!", 450, 350);
        }

        g.dispose();
        bufferStrategy.show();
    }

    //Graphics setup method
    public void setUpGraphics() {
        frame = new JFrame("CheeseWorld");   //Create the program window or frame.  Names it.

        panel = (JPanel) frame.getContentPane();  //sets up a JPanel which is what goes in the frame
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));  //sizes the JPanel
        panel.setLayout(null);   //set the layout

        // creates a canvas which is a blank rectangular area of the screen onto which the application can draw
        // and trap input events (Mouse and Keyboard events)
        canvas = new Canvas();
        canvas.setBounds(0, 0, WIDTH, HEIGHT);
        canvas.setIgnoreRepaint(true);

        panel.add(canvas);  // adds the canvas to the panel.

        // frame operations
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //makes the frame close and exit nicely
        frame.pack();  //adjusts the frame and its contents so the sizes are at their default or larger
        frame.setResizable(false);   //makes it so the frame cannot be resized
        frame.setVisible(true);      //IMPORTANT!!!  if the frame is not set to visible it will not appear on the screen!

        // sets up things so the screen displays images nicely.
        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        canvas.requestFocus();
        System.out.println("DONE graphic setup");

    }

    //Pauses or sleeps the computer for the amount specified in milliseconds
    public void pause(int time) {
        //sleep
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {

        }
    }

} // end of class
