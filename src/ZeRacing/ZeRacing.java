package ZeRacing;

import com.sun.opengl.util.*;
import javax.media.opengl.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ZeRacing extends JFrame {
    public static Animator animator;


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
        animator = new FPSAnimator(glcanvas, 60);
        animator.start();
        setTitle("ZeRacing: Speed For Need");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 1000);
        setLocationRelativeTo(null);
        setVisible(true);
        setFocusable(true);
        glcanvas.requestFocus();
    }
}