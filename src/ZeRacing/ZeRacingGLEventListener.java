package ZeRacing;

//TODO : INITIAL PROJECT BY HAZEM MOSTAFA , SinglePlayer mode

import Texture.TextureReader;
import com.sun.opengl.util.Animator;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.media.opengl.*;

import java.util.BitSet;
import java.util.Random;
import javax.media.opengl.glu.GLU;
import javax.sound.sampled.*;
import javax.swing.*;

public class ZeRacingGLEventListener extends ZeRacingListener implements MouseListener,MouseMotionListener {
    /*
    Tasks:
          - Sound (0)
          - map   (0) replace grass index with sand index
          -

     */
    Animator animator =ZeRacing.animator;
    int maxWidth = 100;
    int maxHeight = 100;
    double x = 70, y = 7; // x is in middle of screen(edited)
    double blueX = 20, blueY = 7; // x is in middle of screen(edited)
    double rotate = 0;
    double rotateB = 0;
    int randomNumberForSwallow;
    int randomNumberForHP;
    int randomNumberForNitro;
    Sound CarSound = new Sound("CarSound.wav");



    //all attributes taken from animgleventlistener
    String[] textureNames = {"Road.png", "RaceLines1.png", "GrassBackground.png", "Start.png"
            ,"Bush.png", "Soil_Tile.png", "RaceLines.png", "Bush.png", "Rock.png", "Tree.png"
            , "Barrel.png", "Oil.png",
            "BlueD0.png","BlueD1.png","BlueD2.png","BlueD3.png","BlueD4.png"
            ,"CarD0.png","CarD1.png","CarD2.png","CarD3.png","CarD4.png"
            ,"HP_Bonus.png","Nitro.png","Finish.png","Menu.png","Instructions.png"
            ,"RedWins.png","BlueWins.png","Draw.png",
            "RedHP100.png","RedHP80.png","RedHP60.png","RedHP40.png","RedHP20.png","RedHP0.png",
          "BlueHP100.png","BlueHP80.png","BlueHP60.png","BlueHP40.png","BlueHP20.png","BlueHP0.png",
            "Back1.png"
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

    HP_Bonus : 22
    Nitro : 23
    Finish : 24
    Background : 25
    Instructions : 26
    Red Wins : 27
    Blue Wins : 28
    Draw : 29

    Red 100%:30
    Red 80% :31
    Red 60% :32
    Red 40% :33
    Red 20% :34
    Red 0%  :35

    Blue 100%:36
    Blue 80% :37
    Blue 60% :38
    Blue 40% :39
    Blue 20% :40
    Blue 0%  :41
    Back1 button : 42


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
    boolean isPressed_1P_VS_CPU=false;
    boolean isPressed_1P_VS_2P=false;
    boolean l=false;
    int cnt=0;
    private String gameState = "MainMenu";
    int index_photo=25;

    public void display(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();

//        System.out.println(New_x + " " + New_y);
        switch (gameState) {
            case "MainMenu":
                DrawSprite(gl, 45, 45, index_photo, 10, 10);
                break;
            case "1P_VS_CPU":
                Display_1P_VS_CPU(gl);
                break;
            case "1P_VS_2P":
                Display_1P_VS_2P(gl);
                break;
            case "Instructions":
                DrawSprite(gl,45,45,index_photo+1,10,10);
                break;
            default:
                System.out.println();
        }
        System.out.println(
        "Finish X: "+ 45+" , Finish Y: "+FinishY[0]+" ,Red Car X: "+x+" ,Red Car Y: "+y +" ,Blue Car Y: "+blueY+" ,Blue Car X: "+blueX
        );

        timer += 0.5;
    }
/*
______________________
 Display_1P_VS_CPU(gl);

Left_Down_Edge : (370,750)
Right_Down_Edge : (583,750)
Left_Up_Edge : (378,660)
Right_Up_Edge : (588,693)
__________________
Display 1P Vs 2P

Left_Down_Edge : (373,785)
Right_Down_Edge : (593,817)
Left_Up_Edge : (481,785)
Right_Up_Edge : (491,850)
___________________
Exit Game

left (0,0)
right(137,0)

left down(0,56)
right down(134,56)
____________________
instructions
left down(354,956)
left up (354,884)
 354 <= x <= 600
 884 <= y <= 956
 */
    public void Display_1P_VS_CPU(GL gl){
        Grass(gl);
        WhiteLines(gl);//Created by Mohamed Magdy , idea by:all Teammates , First Touch:Abdulrahman
        theSideLines(gl);//Created by Abdelrahman ,Idea by: By All Teammates
        Tree(gl);//FirstTouch: By Hazem , Idea by : Abdulrahman , Edited by : all Teammates
//        Rock(gl);
        Barrel(gl);
        Health(gl);
        Start(gl); // Created By : All teammates (so easy)
        Nitro(gl);
        Finish(gl);
        RedHealth(gl);
        BlueHealth(gl);
        Car(gl); // From Initial Project
        CarBlueAi(gl); // Created by : Magdy alone and From my Perspective it's prefect //Hazem
//        IncreaseSpeed();


    }
    public void Display_1P_VS_2P(GL gl){
        Grass(gl);
        WhiteLines(gl);//Created by Mohamed Magdy , idea by:all Teammates , First Touch:Abdulrahman
        theSideLines(gl);//Created by Abdelrahman ,Idea by: By All Teammates
        Tree(gl);//FirstTouch: By Hazem , Idea by : Abdulrahman , Edited by : all Teammates
//        Rock(gl);
        Barrel(gl);
        Health(gl);
        Start(gl); // Created By : All teammates (so easy)
        Nitro(gl);
        Finish(gl);
        RedHealth(gl);
        BlueHealth(gl);
        Car(gl); // From Initial Project
        CarBluePlayer(gl); // Created by : Magdy alone and From my Perspective it's prefect //Hazem
//        IncreaseSpeed();


    }

    double[] y1 = {0};
    double[] speed = {1};
    double[] Tree_movement = {2000}; // 14 second , khaled count it
    double[] Finish_Movement={50}; // 1 min
    double[] Barrel_Movement = {250};
    double[] Health_Movement= {1000};
    double[] Nitro_Movement={600};
    boolean isBarrelCollisionActiveRed = false;
    boolean isBarrelCollisionActiveBlue=false;
    boolean isHealthCollisionActiveRed = false;
    boolean isHealthCollisionActiveBlue=false;
    int Car_Red_Index=17;
    int Car_Blue_Index=12;
    double[] FinishY={6850}; // 1650 --> 14 second . khaled count it again , 6850 give us 50 second
    public void Finish(GL gl) {
        Movement_FINISH(gl, 45, 24, 7f, 3f, speed[0] + 2, 100, 10, 1, FinishY);

        if (isColliding(45, (int)FinishY[0]+110, 60, 40, (int)x, (int)y, 8, 8)
        &&  !isColliding(45, (int)FinishY[0]+110, 60, 40, (int)blueX, (int)blueY, 8, 8)) {
            ZeRacing.animator.stop();
            CarSound.stop();
            DrawSprite(gl,45,70,27,10f,10f);
            DrawSprite(gl,90,10,42,1f,1f);
        }
        if (!isColliding(45, (int)FinishY[0]+110, 60, 40, (int)x, (int)y, 8, 8)
          && isColliding(45, (int)FinishY[0]+110, 60, 40, (int)blueX, (int)blueY, 8, 8)) {
            ZeRacing.animator.stop();
            CarSound.stop();
            DrawSprite(gl,45,70,28,10f,10f);
            DrawSprite(gl,90,10,42,1f,1f);
        }
        if (isColliding(45, (int)FinishY[0]+110, 60, 40, (int)x, (int)y, 8, 8)
                && isColliding(45, (int)FinishY[0]+110, 60, 40, (int)blueX, (int)blueY, 8, 8)) {
            ZeRacing.animator.stop();
            CarSound.stop();
            DrawSprite(gl,45,70,29,10f,10f);
            DrawSprite(gl,90,10,42,1f,1f);
        }
    }
    public void Nitro(GL gl){
        Movement_For_Obstacles(gl,randomNumberForNitro,23,1f,1f,speed[0]+2,100,400,12,Nitro_Movement);

        if (isColliding(randomNumberForNitro,(int)Nitro_Movement[0]-312,10,10,x,y,8,8)
        ||  isColliding(randomNumberForNitro,(int)Nitro_Movement[0]+84,10,10,x,y,8,8)){
            if(y<100) y+=1;
        }
        if (isColliding(randomNumberForNitro,(int)Nitro_Movement[0]-320,10,10,blueX,blueY,8,8)
        ||  isColliding(randomNumberForNitro,(int)Nitro_Movement[0]+90,10,10,blueX,blueY,8,8)){
            if(blueY<100) blueY+=1;
        }

        if (timer % 250 == 0) randomNumberForNitro = (int) (Math.random() * (50)) + 20;
    }
    public void Barrel(GL gl) {
        Movement_For_Obstacles(gl, randomNumberForSwallow, 10, 1f, 1f, speed[0]+2, 100, 700, 20, Barrel_Movement);
        if(isColliding(randomNumberForSwallow,((int)Barrel_Movement[0]+100),8,8,x,y,8,8)) {
            if (!isBarrelCollisionActiveRed && Car_Red_Index < 21 && Red_Health_Index<35) {
                Car_Red_Index++;
                Red_Health_Index++;
                isBarrelCollisionActiveRed = true;
            }
            if(Car_Red_Index==21){
                ZeRacing.animator.stop();
                CarSound.stop();
                DrawSprite(gl,45,70,28,10f,10f);
                DrawSprite(gl,90,10,42,1f,1f);
                //By Mbappé
            }
            if(y>0) y-=1;
        }
        else isBarrelCollisionActiveRed = false;

        if(isColliding(randomNumberForSwallow,(int)(Barrel_Movement[0]+80),8,8,blueX,blueY,8,8)) {
            if (!isBarrelCollisionActiveBlue && Car_Blue_Index < 16 && Blue_Health_Index<41) {
                Car_Blue_Index++;
                Blue_Health_Index++;
                isBarrelCollisionActiveBlue = true;
            }
            if(Car_Blue_Index==16){
                ZeRacing.animator.stop();
                CarSound.stop();
                DrawSprite(gl,45,70,27,10f,10f);
                DrawSprite(gl,90,10,42,1f,1f);
                //by Eslam Rasmy
            }
            if(blueY>0) blueY-=1;
        }
        else isBarrelCollisionActiveBlue = false;

        if (timer % 500 == 0) randomNumberForSwallow = (int) (Math.random() * (50)) + 20;
    }

    //DRY : don't repeat ur self
    //RY
    public void Health(GL gl) {
        Movement_For_Obstacles(gl, randomNumberForHP, 22, 1f, 1f,  speed[0]+2, 300, 700, 20, Health_Movement);
        //RED
        if(isColliding(randomNumberForHP,((int)Health_Movement[0]-420),8,8,x,y,8,8) ||
                isColliding(randomNumberForHP,((int)Health_Movement[0]+285),8,8,x,y,8,8)) {
            if (!isHealthCollisionActiveRed && Car_Red_Index > 17 && Red_Health_Index>30) {
                Car_Red_Index--;
                Red_Health_Index--;
                isHealthCollisionActiveRed = true;
            }
        } else isHealthCollisionActiveRed = false;
        //BLUE
        if(isColliding(randomNumberForHP,((int)Health_Movement[0]-420),8,8,blueX,blueY,8,8) ||
                isColliding(randomNumberForHP,((int)Health_Movement[0]+285),8,8,blueX,blueY,8,8)) {
            if (!isHealthCollisionActiveBlue && Car_Blue_Index > 12 && Blue_Health_Index>36) {
                Car_Blue_Index--;
                Blue_Health_Index--;
                isHealthCollisionActiveBlue = true;
            }
        } else isHealthCollisionActiveBlue = false;


        if (timer % 1000 == 0) randomNumberForHP = (int) (Math.random() * (50)) + 20;
    }
    public boolean isColliding(double x1, double y1, double width1, double height1, double x2, double y2, double width2, double height2) {
        return x1 < x2 + width2 &&
                x1 + width1 > x2 &&
                y1 < y2 + height2 &&
                y1 + height1 > y2;
    }
    public void Tree(GL gl) {
        Movement(gl, 3, 88, 9, 1f, 1f,  speed[0]+2, 100, 70, 12, Tree_movement);
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
    int Red_Health_Index=30;
    int Blue_Health_Index=36;
    public void RedHealth(GL gl){
        DrawSprite(gl,22,90,Red_Health_Index,1f,0.2f);
    }
    public void BlueHealth(GL gl){
        DrawSprite(gl,22,85,Blue_Health_Index,1f,0.2f);
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

    public void Movement_FINISH(GL gl, double x, int index, float scaleX, float scaleY, double speed, int Y_initial, int sp, int noOfLines, double[] y1) {
        for (int i = 0; i < noOfLines; i++) {
            double currentY = Y_initial - (i * sp) + y1[0]; // currentY =100 , y1[0] =0
            DrawSprite(gl, x, (int) currentY, index, scaleX, scaleY);
        }
        y1[0] = y1[0] - speed; // ---->0.2 - - - - - ----> y=-50 ---> y=0
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
    public void CarBlueAi(GL gl) {
        select.moveALone();
        DrawSprite_CarForAi(gl, blueX, blueY, Car_Blue_Index, 1f, 2f);
    }

    public void CarBluePlayer(GL gl) {
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
    double New_x=0;
    double New_y=0;
/*
        1p vs cpu:
       358 <= x <= 599
       683 <= y <= 753

        1p vs 2p:
        361 <= x <= 594
        783 <= y <= 852

        instructions:
       339 <= x <= 617
       881 <= y <= 967

 */
    @Override
    public void mouseClicked(MouseEvent e) {
        New_x = e.getX();
        New_y = e.getY();
        if (gameState.equals("MainMenu")) {
            if (358 <= New_x && New_x <= 599 && 683 <= New_y && New_y <= 753) {
                CarSound.start();
                gameState = "1P_VS_CPU";
            }
            else if (361 <= New_x && New_x <= 594 && 783 <= New_y && New_y <= 852) {
                CarSound.start();
                gameState = "1P_VS_2P";
            }
            else if (339 <= New_x && New_x <=617 && 881 <= New_y && New_y <= 967) {
                gameState="Instructions";
            }
            else if (0 < New_x && New_x < 137 && 0 < New_y && New_y < 136) {
                System.exit(0);
            }
        }
        else if (gameState.equals("Instructions")) {
            if (828 <= New_x && New_x <= 1000 && 1 <= New_y && New_y <= 50) {
                gameState = "MainMenu";
                }
            }
        else if (gameState.equals("1P_VS_CPU")){
            if (828 <= New_x && New_x <= 1000 && 1 <= New_y && New_y <= 50) {
                gameState = "MainMenu";
                }
            }
        }

    /*

     354 <= x <= 600
     884 <= y <= 956

     828 <= x <= 1000
        1 <= y <= 50

       358 <= x <= 599
       683 <= y <= 753
     */

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

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
        if(isKeyPressed(KeyEvent.VK_ENTER)){
            l=true;
        }
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


    static class Sound {
        private Clip clip;

        public Sound(String soundFileName) {
            try {
                File soundFile = new File(soundFileName);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
                AudioFormat baseFormat = audioStream.getFormat();
                AudioFormat targetFormat = new AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED,
                        baseFormat.getSampleRate(),
                        16,
                        baseFormat.getChannels(),
                        baseFormat.getChannels() * 2,
                        baseFormat.getSampleRate(),
                        false
                );
                AudioInputStream convertedStream = AudioSystem.getAudioInputStream(targetFormat, audioStream);
                clip = AudioSystem.getClip();
                clip.open(convertedStream);

            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }

        public void start() {
            if (clip != null) {
                clip.setFramePosition(0);
                clip.start();
            }
        }

        public void stop() {
            if (clip != null && clip.isRunning()) {
                clip.stop();
            }
        }
    }
}