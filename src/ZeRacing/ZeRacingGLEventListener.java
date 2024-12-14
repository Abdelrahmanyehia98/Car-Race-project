package ZeRacing;

//TODO : INITIAL PROJECT BY HAZEM MOSTAFA , SinglePlayer mode

import Texture.TextureReader;
import java.awt.event.*;
import java.io.IOException;
import javax.media.opengl.*;

import java.util.ArrayList;
import java.util.BitSet;
import javax.media.opengl.glu.GLU;

public class ZeRacingGLEventListener extends ZeRacingListener {
    int maxWidth = 100;
    int maxHeight = 100;
    int x = maxWidth/2-5, y = 7; // x is in middle of screen(edited)
    double rotate=0;
    //all attributes taken from animgleventlistener
    String[] textureNames = {"Road.png" ,"RaceLines1.png","GrassBackground.png","Start.png"
            ,"CarD0.png","Soil_Tile.png","RaceLines.png" ,"Bush.png","Rock.png","Tree.png"
    };
    /*

    Road index: 0
    WhiteLines: 1
    GrassBackground: 2
    Start:3
    Our Red Car(100% Health): 4
    Soil_Tile(map to add) : 5
    RaceLines: 6
    Bush: 7
    Rock: 8
    Tree: 9



     */
    TextureReader.Texture[] texture = new TextureReader.Texture[textureNames.length];
    int[]textures = new int[textureNames.length];
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

        for(int i = 0; i < textureNames.length; i++){
            try {
                texture[i] = TextureReader.readTexture(assetsFolderName + "//" + textureNames[i] , true);
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
            } catch( IOException e ) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
    }
    /*
    Notes:
    -we need to decrease road image size
     */

    public void display(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();


        Grass(gl);
        WhiteLines(gl);//Created by Mohamed Magdy , idea by:all Teammates , First Touch:Abdulrahman
        theSideLines(gl);//Created by Abdelrahman ,Idea by: By All Teammates
        Tree(gl);//FirstTouch: By Hazem , Idea by : Abdulrahman , Edited by : all Teammates
        Rock(gl);
        Start(gl); // Created By : All teammates (so easy)
        Car(gl); // From Initial Project


    }
    double[] y1={0};
    double speed=0.5;
    double[] Rock_movement = {0};
    double[] Bush_movement = {0};

    public void Tree(GL gl){
        Movement(gl, 3, 88, 9, 1f, 1f, 1.5, 100, 70, 12, Rock_movement);
    }

    public void Rock(GL gl){
        Movement(gl, 3, 88, 8, 1f, 1f, 1.5, 100, 50, 12, Bush_movement);
    }
    public void Grass(GL gl){
        Movement(gl,2,88,2,1.5f,25f,speed,100,50,12,y1);
    }
    public void WhiteLines(GL gl) {
        Movement(gl,30,60,1,0.2f,1.3f,speed,100,50,12,y1);
    }
    public void theSideLines(GL gl) {
        Movement(gl,10,80,6,0.2f,11f,speed,100,50,12,y1);
    }
    public void Movement(GL gl , int x1 , int x2 ,int index , float scaleX , float scaleY ,double speed,int Y_initial,int sp,int noOfLines,double[] y1){
        for (int i = 0; i < noOfLines; i++) {
            double currentY = Y_initial - (i * sp)+y1[0];
            DrawSprite(gl,  x1, (int)currentY,  index,  scaleX,  scaleY);
            DrawSprite(gl,  x2,  (int)currentY,  index,  scaleX,  scaleY);
        }
        y1[0] -= speed;
        if (y1[0] < -sp) {
            y1[0] = 0;
        }
    }
    double y2=0;
    public void Start(GL gl){

        if(y1[0]<1){
            y2-=0.01;
        }

        gl.glPushMatrix();
        gl.glTranslated(0,y2,0);
        DrawSprite(gl,45,7,3,7f,3f);
        gl.glPopMatrix();
    }

    public void Car(GL gl){
        handleKeyPress();
        DrawSprite_Car(gl,x,y,4,1f,2f);
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    public void DrawSprite(GL gl,int x, int y, int index, float scaleX, float scaleY){
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated( x/(maxWidth/2.0) - 0.9, y/(maxHeight/2.0) - 0.9, 0);
        gl.glScaled(0.1*scaleX, 0.1*scaleY, 1);
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
    public void DrawSprite_Car(GL gl,int x, int y, int index, float scaleX, float scaleY){
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated( x/(maxWidth/2.0) - 0.9, y/(maxHeight/2.0) - 0.9, 0);
        gl.glRotated(rotate,0,0,1);
        gl.glScaled(0.1*scaleX, 0.1*scaleY, 1);
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

    public void DrawBackground(GL gl){
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[4]);	// Turn Blending On

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
        if (isKeyPressed(KeyEvent.VK_LEFT)) {
            if (x > 11+5-4) {
                x--;
                rotate+=0.5;
            }
        }
        if (isKeyPressed(KeyEvent.VK_RIGHT)) {
            if (x < 79-5+4) {
                x++;
                rotate-=0.5;
            }
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
        rotate=0;

    }

    @Override
    public void keyTyped(final KeyEvent event) {
        // don't care
    }

    public boolean isKeyPressed(final int keyCode) {
        return keyBits.get(keyCode);
    }
    public float ToRGB(float x) {
        return (x /255f);
    }
}