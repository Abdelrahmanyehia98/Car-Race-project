package ZeRacing;

import com.sun.opengl.util.*;
import javax.media.opengl.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ZeRacing extends JFrame {
<<<<<<< HEAD
    public static Animator animator;
=======
   public static Animator animator;
>>>>>>> 676f959414c91337765567ee9a2890da8c026ecc


    public static void main(String[] args) {
        new ZeRacing();
    }

    public ZeRacing() {
        ZeRacingGLEventListener listener = new ZeRacingGLEventListener();
        GLCanvas glcanvas = new GLCanvas();
        glcanvas.addGLEventListener(listener);
        glcanvas.addKeyListener(listener);
        glcanvas.addMouseListener(listener);
        getContentPane().add(glcanvas, BorderLayout.CENTER);
<<<<<<< HEAD
        animator = new FPSAnimator(glcanvas, 60);
=======
         animator = new FPSAnimator(glcanvas, 60);
>>>>>>> 676f959414c91337765567ee9a2890da8c026ecc
        animator.start();
        setTitle("ZeRacing: Speed For Need");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 1000);
        setLocationRelativeTo(null);
        setVisible(true);
        setFocusable(true);
        glcanvas.requestFocus();
    }
    public Animator anim(){
        return animator;
    }
}