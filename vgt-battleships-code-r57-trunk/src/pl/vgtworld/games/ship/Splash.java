/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.vgtworld.games.ship;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;

/**
 *
 * @author danie
 */
public class Splash {
    
    private static final int IMGWIDTH  = 1000;
    private static final int IMGHEIGHT = 900;
    private static final int SPLASHTIME = 6000;
    private static final String IMGPATH = "/pl/vgtworld/games/ship/img/splash.png";
    
    public Splash(){
        
        JWindow windowSplash = new JWindow();
        
        windowSplash.getContentPane().add(
            new JLabel(
                    "", new ImageIcon(getClass().getResource(IMGPATH)),
                    SwingConstants.CENTER)
        );
        
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        
        windowSplash.setBounds(
                (dimension.width - IMGWIDTH) / 2, 
                (dimension.height - IMGHEIGHT) / 2, 
                IMGWIDTH,
                IMGHEIGHT);
        
        windowSplash.setVisible(true);
        
        try{
            Thread.sleep(SPLASHTIME);
        }catch(InterruptedException e){}
        
        windowSplash.dispose(); 
        
    }
        
}
