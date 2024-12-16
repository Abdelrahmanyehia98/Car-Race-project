package ZeRacing;

//TODO : INITIAL PROJECT BY HAZEM MOSTAFA , SinglePlayer mode

import Texture.TextureReader;

import java.awt.event.*;
import java.io.IOException;
import javax.media.opengl.*;

import java.util.BitSet;
import java.util.Random;
import javax.media.opengl.glu.GLU;

public class ZeRacingGLEventListener extends ZeRacingListener {
    int maxWidth = 100;
    int maxHeight = 100;
    double x = 70, y = 7; // x is in middle of screen(edited)
    int blueX = 20, blueY = 7; // x is in middle of screen(edited)
    double rotate = 0;
    double rotateB = 0;
    int randomNumberForSwallow;
    int randomNumberForHP;
    int randomNumberForNitro;


    //all attributes taken from animgleventlistener
    String[] textureNames = {"Road.png", "RaceLines1.png", "GrassBackground.png", "Start.png"
            ,"Bush.png", "Soil_Tile.png", "RaceLines.png", "Bush.png", "Rock.png", "Tree.png"
            , "Barrel.png", "Oil.png",
            "BlueD0.png","BlueD1.png","BlueD2.png","BlueD3.png","BlueD4.png"
            ,"CarD0.png","CarD1.png","CarD2.png","CarD3.png","CarD4.png"
            ,"HP_Bonus.png","Nitro.png"
    };
    /*

    Road index: 0
    WhiteLines: 1
    GrassBackground: 2
    Start:3

    Soil_Tile(map to add) : 5
    RaceLines: 6
    Bush: 7
    Rock: 8
    Tree: 9
    Barrel:10
    Oil: 11
    blue car(100%):12
    blue car(80%):13
    blue car(60%):14
    blue car(40%):15
    blue car(20%):16

    red car(100%):17
    red car(80%):18
    red car(60%):19
    red car(40%):20
    red car(20%):21

    HP_Bonus:22
    Nitro:23



     */
    TextureReader.Texture[] texture = new TextureReader.Texture[textureNames.length];
    int[] textures = new int[textureNames.length];
    /*
     5 means gun in array pos
     x and y coordinate for gun
     */

    public void init(GLAutoDrawable gld) {

        GL gl = gld.getGL();
        gl.glClearColor(ToRGB(77), ToRGB(77), ToRGB(77), 1.0f);    //This Will Clear The Background Color To Black

        gl.glEnable(GL.GL_TEXTURE_2D);  // Enable Texture Mapping
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);

        for (int i = 0; i < textureNames.length; i++) {
            try {
                texture[i] = TextureReader.readTexture(assetsFolderName + "//" + textureNames[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);

//                mipmapsFromPNG(gl, new GLU(), texture[i]);
                new GLU().gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA, // Internal Texel Format,
                        texture[i].getWidth(), texture[i].getHeight(),
                        GL.GL_RGBA, // External format from image,
                        GL.GL_UNSIGNED_BYTE,
                        texture[i].getPixels() // ImageData
                );
            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
        randomNumberForSwallow = (int) (Math.random() * (40)) + 20;
        randomNumberForHP = (int) (Math.random() * (40)) + 20;
        randomNumberForNitro = (int) (Math.random() * (40)) + 20;
    }

    /*
    Notes:
    -we need to decrease road image size
     */
    double timer = 0;

    public void display(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();


        Grass(gl);
        WhiteLines(gl);//Created by Mohamed Magdy , idea by:all Teammates , First Touch:Abdulrahman
        theSideLines(gl);//Created by Abdelrahman ,Idea by: By All Teammates
        Tree(gl);//FirstTouch: By Hazem , Idea by : Abdulrahman , Edited by : all Teammates
//        Rock(gl);
        Barrel(gl);
        Health(gl);
        Start(gl); // Created By : All teammates (so easy)
        Nitro(gl);
        Car(gl); // From Initial Project
        CarBlue(gl); // Created by : Magdy alone and From my Perspective it's prefect //Hazem
//        IncreaseSpeed();

        System.out.println(timer += 0.5);

    }

    double[] y1 = {0};
    double[] speed = {0.49};
    double[] speed_for_rock={(speed[0]*(0.269/speed[0]))};
    double[] Rock_movement = {500};
    double[] Tree_movement = {2000}; // 14 second , khaled count it
    //Finish Line ---> 12858
    double[] Barrel_Movement = {250};
    double[] Health_Movement= {1000};
    double[] Nitro_Movement={600};
    boolean isBarrelCollisionActiveRed = false;
    boolean isBarrelCollisionActiveBlue=false;
    boolean isHealthCollisionActiveRed = false;
    boolean isHealthCollisionActiveBlue=false;
    int Car_Red_Index=17;
    int Car_Blue_Index=12;
    public void Nitro(GL gl){
        Movement_For_Obstacles(gl,randomNumberForNitro,23,1f,1f,speed[0]+1,100,400,12,Nitro_Movement);
//        System.out.println("Nitro X: "+ randomNumberForNitro+ ", Nitro Y: " +(int)Nitro_Movement[0]+" , Car X: "+blueX+" , Car Y: "+blueY);

        if (isColliding(randomNumberForNitro,(int)Nitro_Movement[0]-312,10,10,x,y,10,10)
                ||  isColliding(randomNumberForNitro,(int)Nitro_Movement[0]+84,10,10,x,y,10,10)){
            y+=10;
        }
        if (isColliding(randomNumberForNitro,(int)Nitro_Movement[0]-320,10,10,blueX,blueY,10,10)
                ||  isColliding(randomNumberForNitro,(int)Nitro_Movement[0]+90,10,10,blueX,blueY,10,10)){
            blueY+=10;
        }

        if (timer % 250 == 0) randomNumberForNitro = (int) (Math.random() * (50)) + 20;
    }
    public void Barrel(GL gl) {
        Movement_For_Obstacles(gl, randomNumberForSwallow, 10, 1f, 1f, speed[0]+1, 100, 700, 20, Barrel_Movement);
        if(isColliding(randomNumberForSwallow,((int)Barrel_Movement[0]+100),10,10,x,y,10,10)) {
            if (!isBarrelCollisionActiveRed && Car_Red_Index < 21) {
                Car_Red_Index++;
                isBarrelCollisionActiveRed = true;
            }
        }
        else isBarrelCollisionActiveRed = false;

        if(isColliding(randomNumberForSwallow,(int)(Barrel_Movement[0]+80),10,10,blueX,blueY,10,10)) {
            if (!isBarrelCollisionActiveBlue && Car_Blue_Index < 16) {
                Car_Blue_Index++;
                isBarrelCollisionActiveBlue = true;
            }
        }
        else isBarrelCollisionActiveBlue = false;

        if (timer % 500 == 0) randomNumberForSwallow = (int) (Math.random() * (50)) + 20;
    }

    //DRY : don't repeat ur self
    //RY
    public void Health(GL gl) {
        Movement_For_Obstacles(gl, randomNumberForHP, 22, 1f, 1f,  speed[0]+1, 300, 700, 20, Health_Movement);

        if(isColliding(randomNumberForHP,((int)Health_Movement[0]-420),10,10,x,y,10,10) ||
                isColliding(randomNumberForHP,((int)Health_Movement[0]+285),10,10,x,y,10,10)) {
            if (!isHealthCollisionActiveRed && Car_Red_Index > 17) {
                Car_Red_Index--;
                isHealthCollisionActiveRed = true;
            }
        } else isHealthCollisionActiveRed = false;

        System.out.println("Health X: "+ randomNumberForHP+ ", Health Y: " +(int)Health_Movement[0]+" , Car X: "+blueX+" , Car Y: "+blueY);



        if (timer % 1000 == 0) randomNumberForHP = (int) (Math.random() * (50)) + 20;
    }
    public boolean isColliding(double x1, double y1, double width1, double height1, double x2, double y2, double width2, double height2) {
        return x1 < x2 + width2 &&
                x1 + width1 > x2 &&
                y1 < y2 + height2 &&
                y1 + height1 > y2;
    }
    public void Tree(GL gl) {
        Movement(gl, 3, 88, 9, 1f, 1f,  speed[0]+1, 100, 70, 12, Tree_movement);
    }

    public void Rock(GL gl) {
        Movement_For_Rock(gl, 3, 88, 8, 1f, 1f,  speed_for_rock[0], 100, 50, 12, Rock_movement, 390);
    }

    public void Grass(GL gl) {
        Movement(gl, 2, 88, 2, 1.5f, 25f,   speed[0], 100, 50, 20, y1);
    }

    public void WhiteLines(GL gl) {
        Movement(gl, 30, 60, 1, 0.2f, 1.3f,   speed[0], 100, 50, 12, y1);
    }

    public void theSideLines(GL gl) {
        Movement(gl, 10, 80, 6, 0.2f, 11f,   speed[0], 100, 50, 12, y1);
    }

    public void Movement(GL gl, double x1, double x2, int index, float scaleX, float scaleY, double speed, int Y_initial, int sp, int noOfLines, double[] y1) {
        for (int i = 0; i < noOfLines; i++) {
            double currentY = Y_initial - (i * sp) + y1[0]; // currentY =100 , y1[0] =0
            DrawSprite(gl, x1, (int) currentY, index, scaleX, scaleY);
            DrawSprite(gl, x2, (int) currentY, index, scaleX, scaleY);
        }
        y1[0] = y1[0] - speed; // ---->0.2 - - - - - ----> y=-50 ---> y=0
        if (y1[0] < -sp) {
            y1[0] = 0;
        }
    }

    //DRY : Don't repeat your self

    public void Movement_For_Obstacles(GL gl, double x1, int index, float scaleX, float scaleY, double speed, int Y_initial, int sp, int noOfLines, double[] y1) {
        for (int i = 0; i < noOfLines; i++) {
            double currentY = Y_initial - (i * sp) + y1[0]; // currentY =100 , y1[0] =0
            DrawSprite(gl, x1, (int) currentY, index, scaleX, scaleY);
        }
        y1[0] = y1[0] - speed; // ---->0.2 - - - - - ----> y=-50 ---> y=0
        if (y1[0] < -sp) {
            y1[0] = 0;
        }
    }

    public void Movement_For_Rock(GL gl, double x1, double x2, int index, float scaleX, float scaleY, double speed, int Y_initial, int sp, int noOfLines, double[] y1, int max) {
        for (int i = 0; i < noOfLines; i++) {
            double currentY = Y_initial - (i * sp) + y1[0]; // currentY =100 , y1[0] =0
            DrawSprite(gl, x1, (int) currentY, index, scaleX, scaleY);
            DrawSprite(gl, x2, (int) currentY, index, scaleX, scaleY);
        }
        y1[0] = y1[0] - speed; // ---->0.2 - - - - - ----> y=-50 ---> y=0
        if (y1[0] < -sp && timer < max) {
            y1[0] = 0;
        }
    }

    double y2 = 0;

    public void Start(GL gl) {

        if (y1[0] < 1) {
            y2 -= 0.01;
        }

        gl.glPushMatrix();
        gl.glTranslated(0, y2, 0);
        DrawSprite(gl, 45, 7, 3, 7f, 3f);
        gl.glPopMatrix();
    }
    public void Car(GL gl) {
        handleKeyPress();
        DrawSprite_Car(gl, x, y, Car_Red_Index, 1f, 2f);
    }
    Ai_Player2 select=new Ai_Player2();
    public void CarBlue(GL gl) {
        select.HandlePlayer2();
        DrawSprite_CarForAi(gl, blueX, blueY, Car_Blue_Index, 1f, 2f);
    }


    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    public void DrawSprite(GL gl, double x, double y, int index, float scaleX, float scaleY) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);    // Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.1 * scaleX, 0.1 * scaleY, 1);
        //System.out.println(x +" " + y);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawSprite_Car(GL gl, double x, double y, int index, float scaleX, float scaleY) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);    // Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glRotated(rotate, 0, 0, 1);
        gl.glScaled(0.1 * scaleX, 0.1 * scaleY, 1);
        //System.out.println(x +" " + y);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawSprite_CarForAi(GL gl, double x, double y, int index, float scaleX, float scaleY) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);    // Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glRotated(rotateB, 0, 0, 1);
        gl.glScaled(0.1 * scaleX, 0.1 * scaleY, 1);
        //System.out.println(x +" " + y);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawBackground(GL gl) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[4]);    // Turn Blending On

        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    /*
     * KeyListener
     */

    public void handleKeyPress() {
        if (isKeyPressed(KeyEvent.VK_LEFT)) { //37
            if (x > 14) {
                x--;
                rotate += 0.5;
            }
        }
        if (isKeyPressed(KeyEvent.VK_RIGHT)) { //39
            if (x < 75) {
                x++;
                rotate -= 0.5;
            }
        }
    }

    class Ai_Player2 {


        public void HandlePlayer2(){
            if (isKeyPressed(KeyEvent.VK_A)) { //37
                if (blueX > 14) {
                    blueX--;
                    rotateB += 0.5;
                }
            }
            if (isKeyPressed(KeyEvent.VK_D)) { //39
                if (blueX < 75) {
                    blueX++;
                    rotateB -= 0.5;
                }
            }
        }

        public void moveALone() {
            if (noChange() == 37) {
                if (blueX > 14) {
                    blueX--;
                    rotateB += 0.5;
                }
            }

            if (noChange() == 39) {
                if (blueX < 75) {
                    blueX++;
                    rotateB -= 0.5;
                }
            }
            if (currentNumber != 37 && currentNumber != 39) {
                rotateB = 0;
            }


        }

        public static int getRandomNumber() {
            Random random = new Random();
            return 37 + random.nextInt(10);
        }

        private static int currentNumber = getRandomNumber(); //35
        private static int cnt = 0;

        public static int noChange() {
            if (cnt == 10) {
                currentNumber = getRandomNumber();
                cnt = 0;
            }
            cnt++;
            return currentNumber;
        }
    }

    public BitSet keyBits = new BitSet(256);

    @Override
    public void keyPressed(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.set(keyCode);

    }

    @Override
    public void keyReleased(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.clear(keyCode);
        rotate = 0;
        rotateB=0;
    }

    @Override
    public void keyTyped(final KeyEvent event) {
        // don't care
    }

    public boolean isKeyPressed(final int keyCode) {
        return keyBits.get(keyCode);
    }

    public float ToRGB(float x) {
        return (x / 255f);
    }
}