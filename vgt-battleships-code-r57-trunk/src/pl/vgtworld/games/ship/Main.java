package pl.vgtworld.games.ship;

import java.awt.EventQueue;
import pl.vgtworld.games.ship.components.JFrameGameWindowSettings;

/**
 * A class with a startup method main().
 * 
 * @author VGT
 * @version 1.0
 */
public class Main
	{
	/**
	 * Program startup method.
	 */
	public static void main(String[] args)
		{
                    
                new Splash();
                                            
	                   
		EventQueue.invokeLater(new Runnable()
				{
				public void run()
					{
					Settings oSettings = new Settings();
					GameStatus oGameStatus = new GameStatus();
                                        JFrameGameWindowSettings oWindow = new JFrameGameWindowSettings(oGameStatus, oSettings, 800, 600);
					oWindow.setVisible(true);
                                       
					}
				}
			);
   		}
	}
