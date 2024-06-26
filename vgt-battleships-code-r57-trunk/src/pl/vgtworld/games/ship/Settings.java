package pl.vgtworld.games.ship;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import javax.swing.JOptionPane;
import pl.vgtworld.exceptions.ParameterException;
import pl.vgtworld.games.ship.components.JFrameGameWindowSettings;

/**
 * The class that stores the main game settings..
 * 
 * @author VGT
 * @version 1.1
 */
public class Settings
	{
	/**
	 * It stores the name of the file where the default settings are stored.
	 */
	public static final String DEFAULT_SETTINGS = "settings.xml";
	/**
	 * Game board width.
	 */
	private int iBoardWidth;
	/**
	 * The height of the game board.
	 */
	private int iBoardHeight;
	/**
	 * AI difficulty level.
	 */
	private int iDifficultyLevel;
	/**
	 * Ship shape limited to vertical / horizontal lines.
	 * 
	 * @since 1.1
	 */
	private boolean bStraightLines;
	/**
	 * A container that stores the size of individual ships.
	 */
	private ArrayList<Integer> aShips;
	/**
	 * Default constructor.
	 */
	public Settings()
		{
		try
			{
			// default configuration from xml file
			FileInputStream oStream = new FileInputStream(System.getProperty("user.dir") + System.getProperty("file.separator") + DEFAULT_SETTINGS);
			Properties oDefault = new Properties();
			oDefault.loadFromXML(oStream);
			iBoardWidth = Integer.parseInt(oDefault.getProperty("plansza_Width"));
			iBoardHeight = Integer.parseInt(oDefault.getProperty("plansza_Height"));
			iDifficultyLevel = Integer.parseInt(oDefault.getProperty("poziom_trudnosci"));
			if ("tak".equals(oDefault.getProperty("proste_linie")))
				bStraightLines = true;
			else
				bStraightLines = true;
			aShips = new ArrayList<Integer>();
			int iNumberOfShips = Integer.parseInt(oDefault.getProperty("ilosc_statkow"));
			for (int i = 1; i <= iNumberOfShips; ++i)
				aShips.add(Integer.parseInt(oDefault.getProperty("statek"+i)));
			}
		catch(IOException e)
			{
			//Default configuration
			iBoardWidth = 10;
			iBoardHeight = 10;
			iDifficultyLevel = 50;
			bStraightLines = true;
			aShips = new ArrayList<Integer>(10);
			aShips.add(1);
			aShips.add(1);
			aShips.add(1);
			aShips.add(1);
			aShips.add(2);
			aShips.add(2);
			aShips.add(2);
			aShips.add(3);
			aShips.add(3);
			aShips.add(4);
			}
		}
	/**
	 * The method returns the width of the board.
	 * 
	 * @return Returns the number of fields that are the width of the board.
	 */
	public int getBoardWidth()
		{
		return iBoardWidth;
		}
	/**
	 * The method returns the height of the board.
	 * 
	 * @return Returns the number of fields that are the height of the board.
	 */
	public int getBoardHeight()
		{
		return iBoardHeight;
		}
	/**
	 * The method returns the AI difficulty level.
	 * 
	 * @return Returns a number between 1-100 that is the AI difficulty level.
	 */
	public int getDifficultyLevel()
		{
		return iDifficultyLevel;
		}
	/**
	 * Returns information if the ships can only be vertical / horizontal lines.
	 * 
	 * @return Returns TRUE if the ships can only be lines, FALSE otherwise.
	 * @since 1.1
	 */
	public boolean getStraightLines()
		{
		return bStraightLines;
		}
	/**
	 * Returns an array containing the size of the individual ships.
	 * 
	 * @return An int table containing the size of each ship.
	 */
	public int[] getShips()
		{
		int[] aData = new int[ aShips.size() ];
		for (int i = 0; i < aShips.size(); ++i)
			aData[i] = aShips.get(i);
		return aData;
		}
	/**
	 * Returns the total number of ships.
	 * 
	 * @return Total number of ships.
	 */
	public int getNumbeOfShips()
		{
		return (int)aShips.size();
		}
	/**
	 * Returns the number of ships of the given size.
	 * 
	 * @param iSize Size of the ships to be counted.
	 * @return Number of ships of the given size.
	 */
	public int getNumberOfShips(int iSize)
		{
		int iQuantity = 0;
		for (int iShip: aShips)
			if (iShip == iSize)
				++iQuantity;
		return iQuantity;
		}
	/**
	 * Returns the size of the largest ship.
	 * 
	 * @return The size of the largest ship.
	 */
	public int getMaxShipSize()
		{
		int iMax = 0;
		for (int iShip: aShips)
			if (iShip > iMax)
				iMax = iShip;
		return iMax;
		}
	/**
	 * Lets you set a new size for the game board.
	 * 
	 * @param iWidth New board width.
	 * @param iHeight New board height.
	 */
	public void setBoardSize(int iWidth, int iHeight)
		{
		iBoardWidth = iWidth;
		iBoardHeight = iHeight;
		}
	/**
	 * It allows you to set a new board width.
	 * 
	 * @param iWidth New board width.
	 */
	public void setBoardWidth(int iWidth)
		{
		iBoardWidth = iWidth;
		}
	/**
	 * It allows you to set a new height of the board.
	 * 
	 * @param iHeight New board height.
	 */
	public void setBoardHeight(int iHeight)
		{
		iBoardHeight = iHeight;
		}
	/**
	 * Lets you set a property that determines the shape of ships allowed.
	 * 
	 * @param bStaightLines TRUE means that ships can only be vertical / horizontal lines.
	 * @since 1.1
	 */
	public void setStraightLines(boolean bStaightLines)
		{
		this.bStraightLines = bStaightLines;
		}
	/**
	 * Allows you to set a new difficulty level.
	 * 
	 * @param iDifficultyLevel AI difficulty level.
	 * @throws ParameterException Throws an exception if the set severity level is not in the range 1-100
	 */
	public void setDifficultyLevel(int iDifficultyLevel) throws ParameterException
		{
		if (iDifficultyLevel < 1 || iDifficultyLevel > 100)
			throw new ParameterException("iDifficultyLevel = " + iDifficultyLevel);
		this.iDifficultyLevel = iDifficultyLevel;
		}
	/**
	 * Adds another ship of the specified size to the ship list.
	 * 
	 * @param iSize Ship size to add.
	 * @throws ParameterException Throws an exception if the given size is less than 1.
	 */
	public void addShip(int iSize) throws ParameterException
		{
		if (iSize < 1)
			throw new ParameterException("iSize = " + iSize);
		aShips.add(iSize);
		}
	/**
	 * Removes a ship with the given index from a container.
	 * 
	 * @param iIndex Index ship to be removed (counted from 0).
	 * @throws ParameterException Throws an exception if the ship with the given index does not exist.
	 */
	public void removeShip(int iIndex) throws ParameterException
		{
		if (iIndex >= aShips.size() || iIndex < 0)
			throw new ParameterException("iIndex = " + iIndex);
		aShips.remove(iIndex);
		}
	/**
	 * Removes all ships from the table.
	 */
	public void removeAllShips()
		{
		aShips.clear();
		}
	/**
	 * saves the current settings to a file where the default settings loaded when creating the object are stored
	 */
	public void saveDefaultSettings()
		{
		try
			{
			Properties oDefault = new Properties();
			oDefault.setProperty("plansza_Width", String.valueOf(iBoardWidth));
			oDefault.setProperty("plansza_Height", String.valueOf(iBoardHeight));
			oDefault.setProperty("poziom_trudnosci", String.valueOf(iDifficultyLevel));
			if (bStraightLines)
				oDefault.setProperty("proste_linie", "tak");
			else
				oDefault.setProperty("proste_linie", "nie");
			int[] aShip = getShips();
			oDefault.setProperty("ilosc_statkow", String.valueOf(aShip.length));
			for (int i = 0; i < aShip.length; ++i)
				oDefault.setProperty("statek"+(i+1), String.valueOf(aShip[i]));
			FileOutputStream oStream = new FileOutputStream(System.getProperty("user.dir") + System.getProperty("file.separator") + DEFAULT_SETTINGS);
			oDefault.storeToXML(oStream, null);
			}
		catch (IOException e)
			{
			JOptionPane.showMessageDialog(null, JFrameGameWindowSettings.LANG.getProperty("errorMsg.settings.saveDefault") , JFrameGameWindowSettings.LANG.getProperty("errorMsg.windowTitle"), JOptionPane.ERROR_MESSAGE);
			}
		}
	}
