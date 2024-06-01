package pl.vgtworld.games.ship.components;

import static pl.vgtworld.games.ship.DrawingCoordinatesOnBoard.*;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import pl.vgtworld.exceptions.ParameterException;
import pl.vgtworld.exceptions.DeveloperException;
import pl.vgtworld.games.ship.Board;
import pl.vgtworld.games.ship.FieldTypeBoard;
import pl.vgtworld.tools.Position;

/**
 * Component that handles the display of board.
 * 
 * @author VGT
 * @version 1.0
 */
public class JComponentBoard
	extends JComponent
	implements ActionListener
	{
	/**
	 * Board to display the panel.
	 */
	private Board oBoard;
	/**
	 * Property storing information whether missed ship positions are also to be displayed on board.
	 */
	private boolean bViewShips;
	/**
	 * Ship position color.
	 */
	private Color oShipColor;
	/**
	 * The color of the position after the shot was off target.
	 */
	private Color oCustomShotColor;
	/**
	 * The color of the field after the shots on target.
	 */
	private Color oOutOfTargetColor;
	/**
	 * The color of the line breaks the position.
	 */
	private Color oGridColor;
	/**
	 * Background color (used in case of failure to load graphic background).
	 */
	private Color oBackgroundColor;
	/**
	 * A board storing the colors of the animation of the shot on the field with the ship.
	 */
	private Color[] aHighlightColorOfShips;
	/**
	 * A board that stores the colors of the shot animation on the empty field.
	 */
	private Color[] aHighlightColorEmptyField;
	/**
	 * The index of the currently displayed highlight color.
	 */
	private int iHighlightColorNumber;
	/**
	 * co-ordinates position displayed highlighted.
	 */
	private Position oHighlightedField;
	/**
	 * Picture for background board.
	 */
	private static Image oBackgroundImg;
	/**
	 * Timer to handle position highlight animation.
	 */
	private Timer oTimer;
	static
		{
		JComponentBoard.oBackgroundImg = null;
		}
	/**
	 * Constructor.
	 * 
	 * @param oBoard Board to be displayed on the panel.
	 */
	public JComponentBoard(Board oBoard)
		{
		this.oBoard = oBoard;
		bViewShips = true;
		oShipColor = new Color(0, 0, 255, 127);
		oCustomShotColor = new Color(0, 0, 0);
		oOutOfTargetColor = new Color(150, 150, 50, 192);
		oGridColor = new Color(0, 0, 0, 96);
		aHighlightColorOfShips = new Color[100];
		aHighlightColorEmptyField = new Color[100];
		for (int i = 0; i < 100; ++i)
			{
			aHighlightColorOfShips[i] = new Color(255, 0, 0, (int)(((double)i / 100) * 255));
			aHighlightColorEmptyField[i] = new Color(0, 255, 0, (int)(((double)i / 100) * 255));
			}
		iHighlightColorNumber = 0;
		oHighlightedField = new Position(2);
		oHighlightedField.setX(-1);
		oHighlightedField.setY(-1);
		if (JComponentBoard.oBackgroundImg == null)
			{
			URL oImgUrl = getClass().getResource("/pl/vgtworld/games/ship/img/map-bg.jpg");
			
			if (oImgUrl != null)
				{
				try
					{
					JComponentBoard.oBackgroundImg = ImageIO.read(oImgUrl);
					}
				catch (IOException e)
					{
					JComponentBoard.oBackgroundImg = null;
					}
				}
			if (oImgUrl == null || JComponentBoard.oBackgroundImg == null)
				{
				JComponentBoard.oBackgroundImg = null;
				oOutOfTargetColor = new Color(255, 255, 255, 127);
				oBackgroundColor = new Color(190, 160, 110);
				}
			}
		oTimer = new Timer(1000, this);
		oTimer.setRepeats(false);
		}
	/**
	 * The method allows you to define whether the panel should display also the wrong positions of the ships.
	 * 
	 * @param bStan If TRUE, the panel will display missed ship positions.
	 */
	public void setViewShips(boolean bStan)
		{
		bViewShips = bStan;
		}
	public void activateHighlight(Position oPosition)
		{
		activateHighlight(oPosition.getX(), oPosition.getY());
		}
	public void activateHighlight(int iX, int iY)
		{
		if (oTimer.isRunning())
			oTimer.stop();
		oHighlightedField.setX(iX);
		oHighlightedField.setY(iY);
		iHighlightColorNumber = 99;
		oTimer.start();
		repaint();
		}
	public void actionPerformed(ActionEvent oEvent)
		{
		iHighlightColorNumber-= 100;
		if (iHighlightColorNumber < 0)
			{
			iHighlightColorNumber = 0;
			}
		repaint();
		}
	/**
	 * Overloaded drawac method panel content.
	 */
	@Override public void paintComponent(Graphics g)
		{
		super.paintComponent(g);
		int iWidth = getWidth();
		int iHeight = getHeight();
		//background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, iWidth - 1, iHeight - 1);
		int iBoardXStart = fieldToPixTopLeft(iWidth, iHeight, oBoard.getWidth(), oBoard.getHeight(), 0, 0).getX();
		int iBoardYStart = fieldToPixTopLeft(iWidth, iHeight, oBoard.getWidth(), oBoard.getHeight(), 0, 0).getY();
		int iBoardXWidth = fieldToPixTopLeft(iWidth, iHeight, oBoard.getWidth(), oBoard.getHeight(), oBoard.getWidth(), oBoard.getHeight()).getX() - iBoardXStart + 1;
		int iBoardYWidth = fieldToPixTopLeft(iWidth, iHeight, oBoard.getWidth(), oBoard.getHeight(), oBoard.getWidth(), oBoard.getHeight()).getY() - iBoardYStart + 1;
		if (JComponentBoard.oBackgroundImg != null)
			{
			g.drawImage(JComponentBoard.oBackgroundImg, 0, 0, iWidth, iHeight, null);
			}
		else
			{
			g.setColor(oBackgroundColor);
			g.fillRect(iBoardXStart, iBoardYStart, iBoardXWidth, iBoardYWidth);
			}
		//preparation of variables
		Position oCross;
		Position oCross2;
		int iPositionX = 0;
		int iPositionY = 0;
		int iPositionWidth = 0;
		int iPositionHeight = 0;
		int iCrossSize = 0;
		boolean bdraw = false;
		for (int i = 0; i < oBoard.getWidth(); ++i)
			for (int j = 0; j < oBoard.getHeight(); ++j)
				{
				// calculate the necessary data
				oCross = fieldToPixTopLeft(iWidth, iHeight, oBoard.getWidth(), oBoard.getHeight(), i, j);
				oCross2 = fieldToPixBottomRight(iWidth, iHeight, oBoard.getWidth(), oBoard.getHeight(), i, j);
				iPositionX = oCross.getX() + 1;
				iPositionY = oCross.getY() + 1;
				iPositionWidth = oCross2.getX() - iPositionX;
				iPositionHeight = oCross2.getY() - iPositionY;
				// intersections between fields
				if (iCrossSize == 0)
					{
					iCrossSize = (int)((iPositionWidth + iPositionHeight) * 0.03);
					if (iCrossSize < 2)
						iCrossSize = 2;
					}
				g.setColor(oGridColor);
				g.drawLine(oCross.getX() - iCrossSize, oCross.getY(), oCross.getX() + iCrossSize, oCross.getY());
				g.drawLine(oCross.getX(), oCross.getY() - iCrossSize, oCross.getX(), oCross.getY() + iCrossSize);
				if (i + 1 == oBoard.getWidth())
					{
					g.drawLine(oCross2.getX() - iCrossSize, oCross.getY(), oCross2.getX() + iCrossSize, oCross.getY());
					g.drawLine(oCross2.getX(), oCross.getY() - iCrossSize, oCross2.getX(), oCross.getY() + iCrossSize);
					}
				if (j + 1 == oBoard.getHeight())
					{
					g.drawLine(oCross.getX() - iCrossSize, oCross2.getY(), oCross.getX() + iCrossSize, oCross2.getY());
					g.drawLine(oCross.getX(), oCross2.getY() - iCrossSize, oCross.getX(), oCross2.getY() + iCrossSize);
					}
				if (i + 1 == oBoard.getWidth() && j + 1 == oBoard.getHeight())
					{
					g.drawLine(oCross2.getX() - iCrossSize, oCross2.getY(), oCross2.getX() + iCrossSize, oCross2.getY());
					g.drawLine(oCross2.getX(), oCross2.getY() - iCrossSize, oCross2.getX(), oCross2.getY() + iCrossSize);
					}
				// content of position
				try
					{
					FieldTypeBoard eTyp = oBoard.getField(i, j);
					bdraw = false;
					switch (eTyp)
						{
						case SHIP_BOARD:
							g.setColor(oShipColor);
							if (bViewShips)
								bdraw = true;
							break;
						case CUSTOMS_SHOT_BOARD:
							g.setColor(oCustomShotColor);
							bdraw = true;
							break;
						case BOARD_FIELD_UNAVAILABLE:
						case BOARD_SHOT_FALSE:
							g.setColor(oOutOfTargetColor);
							bdraw = true;
							break;
						}
					if (bdraw)
						{
						g.fillRect(iPositionX, iPositionY, iPositionWidth, iPositionHeight);
						}
					// highlight position
					if (iHighlightColorNumber > 0 && oHighlightedField.getX() == i && oHighlightedField.getY() == j)
						{
						if (oBoard.getField(i, j) == FieldTypeBoard.SHIP_BOARD || oBoard.getField(i, j) == FieldTypeBoard.CUSTOMS_SHOT_BOARD)
							g.setColor(aHighlightColorOfShips[iHighlightColorNumber]);
						else
							g.setColor(aHighlightColorEmptyField[iHighlightColorNumber]);
						g.fillRect(iPositionX, iPositionY, iPositionWidth, iPositionHeight);
						}
					}
				catch (ParameterException e)
					{
					throw new DeveloperException(e);
					}
				}
		}
	}
