package pl.vgtworld.games.ship.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import pl.vgtworld.games.ship.ShipIterator;
import pl.vgtworld.games.ship.GameStatus;

/**
 * Component that displays the current gameplay statistics under the boards.
 * 
 * @author VGT
 * @version 1.0
 */
public class JComponentGameStatus
	extends JComponent
	{
	private static final int FONT_SIZE_LABEL = 16;
	private static final int FONT_SIZE_VALUES = 14;
	private static final Color COLOR_INFO = Color.WHITE;
	private static final Color COLOR_NEUTRAL = Color.white;
	private static final Color COLOR_POSITIVE = Color.GREEN;
	private static final Color COLOR_NEGATIVE = Color.RED;
	private GameStatus oGameStatus;
	private ShipIterator oPlayerShips;
	private ShipIterator oComputerShips;
	private JComponentShipGameStatus oPlayerShipList;
	private JComponentShipGameStatus oComputerShipList;
	private JLabel oPlayerPoints;
	private JLabel oComputerPoints;
	private Image oBackgroundImg;
	/**
	 * Constructor.
	 * 
	 * @param oGameStatus Object containing information about the current status of the game.
	 */
	public JComponentGameStatus(GameStatus oGameStatus)
		{
		super();
		this.oGameStatus = oGameStatus;
		setPreferredSize(new Dimension(100, 100));
		setLayout(new BorderLayout());
		oPlayerShips = null;
		oComputerShips = null;
		Font oFontLabels = new Font("Arial", Font.BOLD, FONT_SIZE_LABEL);
		Font oFontValues = new Font("Arial", Font.BOLD, FONT_SIZE_VALUES);
		//Background img
		URL oImgUrl = getClass().getResource("/pl/vgtworld/games/ship/img/game-status-bg.png");
		if (oImgUrl != null)
			{
			try
				{
				oBackgroundImg = ImageIO.read(oImgUrl);
				}
			catch (IOException e)
				{
				oBackgroundImg = null;
				}
			}
		else
			oBackgroundImg = null;
		//Labels
		oPlayerPoints = new JLabel("" + oGameStatus.getPlayerPoints(), JLabel.CENTER);
		oComputerPoints = new JLabel("" + oGameStatus.getPlayerPoints(), JLabel.CENTER);
		JLabel oPlayerLabel = new JLabel(JFrameGameWindowSettings.LANG.getProperty("gameStatus.Player"), JLabel.CENTER);
		JLabel oComputerLabel = new JLabel(JFrameGameWindowSettings.LANG.getProperty("gameStatus.AI"), JLabel.CENTER);
		JLabel oLabelScore = new JLabel(JFrameGameWindowSettings.LANG.getProperty("gameStatus.points"), JLabel.CENTER);
		JLabel oFleetLabel = new JLabel(JFrameGameWindowSettings.LANG.getProperty("gameStatus.fleet"), JLabel.LEFT);
		//colors
		oPlayerLabel.setForeground(COLOR_INFO);
		oComputerLabel.setForeground(COLOR_INFO);
		oLabelScore.setForeground(COLOR_INFO);
		oFleetLabel.setForeground(COLOR_INFO);
		if (oGameStatus.getPlayerPoints() == oGameStatus.getComputerPoints())
			{
			oPlayerPoints.setForeground(COLOR_NEUTRAL);
			oComputerPoints.setForeground(COLOR_NEUTRAL);
			}
                else if (oGameStatus.getPlayerPoints() > oGameStatus.getComputerPoints())
			{
			oPlayerPoints.setForeground(COLOR_POSITIVE);
			oComputerPoints.setForeground(COLOR_NEGATIVE);
			}
		else
			{
			oPlayerPoints.setForeground(COLOR_NEGATIVE);
			oComputerPoints.setForeground(COLOR_POSITIVE);
			}
		//font
		oPlayerLabel.setFont(oFontLabels);
		oComputerLabel.setFont(oFontLabels);
		oFleetLabel.setFont(oFontLabels);
		oPlayerPoints.setFont(oFontValues);
		oComputerPoints.setFont(oFontValues);
		
		JPanel oPointsPanel = new JPanel();
		oPointsPanel.setOpaque(false);
		oPointsPanel.setLayout(new GridLayout(3, 2));
		oPointsPanel.add(new JLabel());
		oPointsPanel.add(oPlayerLabel);
		oPointsPanel.add(oComputerLabel);
		
		try
			{
			oPlayerShipList = new JComponentShipGameStatus(oPlayerShips);
			oComputerShipList = new JComponentShipGameStatus(oComputerShips);
	
			JPanel oShipsPanel = new JPanel();
			oShipsPanel.setOpaque(false);
			oShipsPanel.setLayout(new GridLayout(3, 1));
			oShipsPanel.add(oFleetLabel);
			oShipsPanel.add(oPlayerShipList);
			oShipsPanel.add(oComputerShipList);
			
			add(oShipsPanel, BorderLayout.CENTER);
			}
		catch (IOException e)
			{}
		
		add(oPointsPanel, BorderLayout.WEST);
		}
	/**
	 * Setting the player's ship container object.
	 * 
	 * @param oShips The player's ship container.
	 */
	public void setPlayerShips(ShipIterator oShips)
		{
		oPlayerShips = oShips;
		oPlayerShipList.setShips(oShips);
		}
	/**
	 * Setting up the computer's vessel container object.
	 * 
	 * @param oShips Computer vessel container.
	 */
	public void setComputerShips(ShipIterator oShips)
		{
		oComputerShips = oShips;
		oComputerShipList.setShips(oShips);
		}
	/**
	 * data refreshes in a component based on the game status object.
	 */
	public void updateData()
		{
		oPlayerPoints.setText("" + oGameStatus.getPlayerPoints());
		oComputerPoints.setText("" + oGameStatus.getComputerPoints());
		if (oGameStatus.getPlayerPoints() == oGameStatus.getComputerPoints())
			{
			oPlayerPoints.setForeground(COLOR_NEUTRAL);
			oComputerPoints.setForeground(COLOR_NEUTRAL);
			}
		else if (oGameStatus.getPlayerPoints() > oGameStatus.getComputerPoints())
			{
			oPlayerPoints.setForeground(COLOR_POSITIVE);
			oComputerPoints.setForeground(COLOR_NEGATIVE);
			}
		else
			{
			oPlayerPoints.setForeground(COLOR_NEGATIVE);
			oComputerPoints.setForeground(COLOR_POSITIVE);
			}
		repaint();
		}
	/**
	 * Overloaded drawac panel method.
	 */
	@Override public void paintComponent(Graphics g)
		{
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, getWidth()-1, getHeight()-1);
		if (oBackgroundImg != null)
			{
			int iStartX = 0;
			while (iStartX < getWidth())
				{
				g.drawImage(oBackgroundImg, iStartX, 0, null);
				iStartX+= oBackgroundImg.getWidth(null);
				}
			}
		}
	}
