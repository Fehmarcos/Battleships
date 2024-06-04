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
 * Class that stores the main game settings.
 * 
 * @author VGT
 * @version 1.1
 */
public class Settings
	{
	/**
	 * Stores the filename containing the default settings.
	 */
	public static final String DEFAULT_SETTINGS = "settings.xml";
	/**
	 */
	private int iBoardWidth;
	/**
	 */
	private int iBoardHeight;
	/**
	 * AI difficulty level.
	 */
	private int iDifficultyLevel;
	/**
	 * Determines whether ship shapes must be straight.
	 * 
	 * @since 1.1
	 */
	private boolean bStraightLines;
	/**
	 * A container representing the player's fleet.
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
	 * Returns the board's width.
	 * 
	 * @return Returns the number of fields that are the board's width.
	 */
	public int getBoardWidth()
		{
		return iBoardWidth;
		}
	/**
	 * Returns the board's height.
	 * 
	 * @return Returns the number of fields that are the board's height.
	 */
	public int getBoardHeight()
		{
		return iBoardHeight;
		}
	/**
	 * Returns the AI difficulty level.
	 * 
	 * @return Returns a number between 1-100 that is the AI difficulty level.
	 */
	public int getDifficultyLevel()
		{
		return iDifficultyLevel;
		}
	/**
	 * Returns whether the ships have to be in straight lines.
	 * 
	 * @return Returns TRUE if the ships must be lines, FALSE otherwise.
	 * @since 1.1
	 */
	public boolean getStraightLines()
		{
		return bStraightLines;
		}
	/**
	 * Returns an array containing a fleet and their sizes.
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
	 * Returns the number of ships of a given size.
	 * 
	 * @param iSize Ship size.
	 * @return Number of ships of a given size.
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
	 * Returns the largest ship size.
	 * 
	 * @return the largest ship size.
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
	 * Lets you set a new board size.
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
	 * Lets you set a new board width.
	 * 
	 * @param iWidth New board width.
	 */
	public void setBoardWidth(int iWidth)
		{
		iBoardWidth = iWidth;
		}
	/**
	 * Lets you set a new board height.
	 * 
	 * @param iHeight New board height.
	 */
	public void setBoardHeight(int iHeight)
		{
		iBoardHeight = iHeight;
		}
	/**
	 * Lets you set a property that limits allowed ship shapes.
	 * 
	 * @param bStaightLines TRUE means that ships can only be straight lines.
	 * @since 1.1
	 */
	public void setStraightLines(boolean bStaightLines)
		{
		this.bStraightLines = bStaightLines;
		}
	/**
	 * Lets you set a new difficulty level.
	 * 
	 * @param iDifficultyLevel AI difficulty level.
	 * @throws ParameterException Throws an exception if the AI level isn't
         * between 1-100.
	 */
	public void setDifficultyLevel(int iDifficultyLevel) throws ParameterException
		{
		if (iDifficultyLevel < 1 || iDifficultyLevel > 100)
			throw new ParameterException("iDifficultyLevel = " + iDifficultyLevel);
		this.iDifficultyLevel = iDifficultyLevel;
		}
	/**
	 * Adds a ship to the fleet.
	 * 
	 * @param iSize Ship size.
	 * @throws ParameterException Throws an exception if the given size is less than 1.
	 */
	public void addShip(int iSize) throws ParameterException
		{
		if (iSize < 1)
			throw new ParameterException("iSize = " + iSize);
		aShips.add(iSize);
		}
	/**
	 * Removes a given ship from the fleet.
	 * 
	 * @param iIndex Index ship to be removed (counted from 0).
	 * @throws ParameterException Throws an exception if a ship's given index doesn't exist.
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
	 * Saves the current settings to the file.
	 */
	public void saveDefaultSettings()
		{
		try
			{
			Properties oDefault = new Properties();
			oDefault.setProperty("plansza_Width", String.valueOf(iBoardWidth));
			oDefault.setProperty("plansza_Height", String.valueOf(iBoardHeight));
			oDefault.setProperty("poziom_trudnosci", String.valueOf(iDifficultyLevel));
			if (bStraightLines == true)
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
