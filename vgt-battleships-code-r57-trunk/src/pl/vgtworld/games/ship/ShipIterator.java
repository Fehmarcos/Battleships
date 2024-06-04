package pl.vgtworld.games.ship;

import pl.vgtworld.exceptions.ParameterException;
import pl.vgtworld.exceptions.DeveloperException;
import pl.vgtworld.tools.Position;

/**
 * Container class that stores the player's ships.
 *
 * <p>
 * updates:
 * 1.2
 * - added bStraightLines parameter to the {@link #verifyApplication (boolean)} method.
 * </p>
 * 
 * @author VGT
 * @version 1.2
 */
public class ShipIterator
	{
	/**
	 */
	private Board oBoard;
	/**
	 * Array that holds the ships.
	 */
	private Ship[] aShips;
	/**
	 * Number of ships currently stored.
	 */
	private int iNumberOfShips;
	/**
	 * Stores the coordinates of the last shot.
	 */
	private Position oLastShot;
	/**
	 * Default constructor.
	 * 
	 * @param oBoard The board object where the ships will be placed.
	 */
	public ShipIterator(Board oBoard)
		{
		this.oBoard = oBoard;
		aShips = new Ship[0];
		iNumberOfShips = 0;
		oLastShot = new Position(2);
		oLastShot.setX(-1);
		oLastShot.setY(-1);
		}
	/**
	 * Display a list of the currently stored ships.
	 */
	@Override public String toString()
		{
		String sReturn = "Ship Iterator\n";
		sReturn+= "Number of Ships: " + iNumberOfShips + "\n";
		sReturn+= "=================\n";
		for (int i = 0; i < iNumberOfShips; ++i)
			sReturn+= aShips[i] + "\n";
		return sReturn;
		}
	/**
	 * Returns a reference to the ship object with a given number.
	 * 
	 * @param iNumber Ship number to be returned (counted from 1).
	 * @return Returns a reference to a ship with a given number.
	 * @throws ParameterException Throws an exception if the ship number
         * is less than or equal to 0, or greater than the number of ships stored.
	 */
	public Ship getShip(int iNumber) throws ParameterException
		{
		if (iNumber > iNumberOfShips || iNumber <= 0)
			throw new ParameterException("iNumber = " + iNumber);
		return aShips[iNumber - 1];
		}
	/**
	 * Returns the coordinates of the position in the second parameter
	 * belonging to the ship provided in the first parameter.
	 * 
	 * @param iShipNumber Ship number to be returned (counted from 1).
	 * @param iFieldNumber Field position to be returned (counted from 1).
	 * @return Returns an object containing the coordinates of the
         * retrieved position.
	 * @throws ParameterException Throws an exception if the ship or
         * position number is out of range.
	 */
	public Position getField(int iShipNumber, int iFieldNumber) throws ParameterException
		{
		if (iShipNumber > iNumberOfShips || iShipNumber <= 0)
			throw new ParameterException("iShipNumber = " + iShipNumber);
		return aShips[ iShipNumber - 1 ].getField(iFieldNumber);
		}
	/**
	 * Returns a reference to the board object where the ships are placed.
	 * 
	 * @return A reference to the board.
	 */
	public Board getBoard()
		{
		return oBoard;
		}
	/**
	 * Returns the coordinates of the last shot.
	 * 
	 * @return coordinates the last shot.
	 */
	public Position getLastShot()
		{
		return oLastShot;
		}
	/**
	 * Returns the number of ships currently stored
	 * (it doesn't matter if the ships are placed on board).
	 * 
	 * @return Number of ships.
	 */
	public int getNumberOfShips()
		{
		return iNumberOfShips;
		}
	/**
	* Returns the number of ships of a certain size currently stored
	* (it doesn't matter if the ships are placed on board).
	*
	* @param iSize Ship size to be counted.
	* @return Number of ships with the given size.
	*/
	public int getNumberOfShips(int iSize)
		{
		int iQuantity = 0;
		for (int i = 0; i < iNumberOfShips; ++i)
			if (aShips[i].getSize() == iSize)
				++iQuantity;
		return iQuantity;
		}
	/**
	 * Returns the number of ships hit but not sunk.
	 *	
	 * @return Number of ships hit but not sunk.
	 */
	public int getNumberOfShipsHit()
		{
		int iHitNotSunk = 0;
		for (Ship oShip: aShips)
			{
			if (oShip.getHits() == true && oShip.getSunk() == false)
				++iHitNotSunk;
			}
		return iHitNotSunk;
		}
	/**
	 * Returns the number of sunken ships.
	 *
	 * @return Number of sunken ships.
	 */
	public int getNumberOfSunkenShips()
		{
		int iSunken = 0;
		for (Ship oShip: aShips)
			{
			if (oShip.getSunk() == true)
				++iSunken;
			}
		return iSunken;
		}
	/**
	 * Returns information about the number of intact ships.
	 *
	 * @return Number of intact ships.
	 * @since 1.1 
	 */
	public int getNumberOfUndamagedShips()
		{
		return getNumberOfShips() - getNumberOfShipsHit() - getNumberOfSunkenShips();
		}
	/**
	 * Returns the size of the largest ship currently stored.
	 */
	public int getMaxShipSize()
		{
		int iMax = 0;
		for (int i = 0; i < iNumberOfShips; ++i)
			if (aShips[i].getSize() > iMax)
				iMax = aShips[i].getSize();
		return iMax;
		}
	/**
	 * Calculates the total number of fields taken by ships
	 *(it does not matter if positions have been placed on the board).
	 *
	 * @return Total field number of all ships.
	 */
	public int getTotalShipSize()
		{
		try
			{
			int iSize = 0;
			for (int i = 1; i <= getNumberOfShips(); ++i)
				iSize+= getShip(i).getSize();
			return iSize;
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	/**
	 * Calculates the total number of hits for all ships stored.
	 *
	 * @return Total fields hit by all ships.
	 */
	public int getTotalHits()
		{
		int iHits = 0;
		try
			{
			for (int i = 1; i <= iNumberOfShips; ++i)
				iHits+= getShip(i).getNumberOfHits();
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		return iHits;
		}
	/**
	 * Lets you set board coordinates for the selected ship position.
	 *
	 * @param iShipNumber Ship number for which the coordinates
         * (counted from 1) are set.
	 * @param iFieldNumber Ship's field number for which the co-ordinates
         * (counted from 1) are set.
	 * @param iX Coordinate X position where the field's set (counted from 0).
	 * @param iY Coordinate Y position where the field's set (counted from 0).
	 * @throws ParameterException Throws an exception in case of exceeding
         * the range of a ship or the fields of a given ship, or by 
         * giving coordinates outside the board's range.
	 */
	public void setField(int iShipNumber, int iFieldNumber, int iX, int iY) throws ParameterException
		{
		if (iShipNumber > iNumberOfShips || iShipNumber <= 0)
			throw new ParameterException("iShipNumber = " + iShipNumber);
		if (iX >= oBoard.getWidth() || iX < -1)
			throw new ParameterException("iX = " + iX);
		if (iY >= oBoard.getHeight() || iY < -1)
			throw new ParameterException("iY = " + iY);
		aShips[ iShipNumber - 1 ].setField(iFieldNumber, iX, iY);
		}
	/**
	 * Sets all ships to the starting position (-1, -1).
	 */
	public void resetFields()
		{
		for (int i = 0; i < iNumberOfShips; ++i)
			aShips[i].resetFields();
		}
	/**
	 * Main method of communicating shot information.

* 
* The shot information is transmitted to all ships in a board, as long as it's
* not a successful hit. The board also marks said field using this information.
*
* @param iX Coordinate X position of a shot.
* @param iY Coordinate Y position of a shot.
* @return Returns TRUE if any ship was hit or FALSE otherwise.
* @throws ParameterException Throws an exception if the given coordinates are
* outside the board's range.
	 */
	public boolean shot(int iX, int iY) throws ParameterException
		{
		if (iX >= oBoard.getWidth() || iX < 0)
			throw new ParameterException("Ix = " + iX);
		if (iY >= oBoard.getHeight() || iY < 0)
			throw new ParameterException("iY = " + iY);
		if (oBoard.getField(iX, iY) != FieldTypeBoard.BOARD_FIELD_EMPTY && oBoard.getField(iX, iY) != FieldTypeBoard.SHIP_BOARD)
			throw new DeveloperException();
		oLastShot.setX(iX);
		oLastShot.setY(iY);
		for (int i = 0; i < iNumberOfShips; ++i)
			if (aShips[i].shot(iX, iY) == true)
				return true;
		return false;
		}
	/**
	 * Adds a ship of a given size to the container.
	 *
	 * After it is created, all ship positions are set to the default (-1, -1).
	 *
	 * @param iSize Ship size.
	 */
	public void addAShip(int iSize)
		{
		// creating a new table of ships and rewriting the existing referees to the new table
		Ship[] aNewShips = new Ship[ iNumberOfShips + 1 ];
		for (int i = 0; i < iNumberOfShips; ++i)
			aNewShips[i] = aShips[i];
		// create a new ship
		Ship oObj = new Ship(iSize, oBoard);
		aNewShips[iNumberOfShips] = oObj;
		++iNumberOfShips;
		aShips = null;
		aShips = aNewShips;
		}
	/**
	 * Deletes a ship with the given number.
	 * 
	 * @param iNumber Ship number to be removed (counted from 1).
	 * @throws ParameterException Throws an exception if the ship number's
         * out of range.
	 */
	public void removeShip(int iNumber) throws ParameterException
		{
		if (iNumber > iNumberOfShips || iNumber <= 0)
			throw new ParameterException("iNumber = " + iNumber);
		// create a new ship table and rewrite the referee without the deleted one
		Ship[] aNewShips = new Ship[ iNumberOfShips - 1 ];
		int iLocalIndex = 0;
		for (int i = 0; i < iNumberOfShips; ++i)
			{
			if (i + 1 == iNumber)
				aShips[i] = null;
			else
				aNewShips[iLocalIndex++] = aShips[i];
			}
		--iNumberOfShips;
		aShips = null;
		aShips = aNewShips;
		}
	/**
	* Checks that all ships have been placed on the board and their
        * placement complies with the game rules.
	*
	* They are checked in sequence: whether all ships are on the board,
        * the positions are combined into one element, that ship parts are
        * set according to the straight lines rule and that no ships are in
        * contact with eachother.
	*
	* @param bStraightLines Specifies whether ships have to be in straight lines.
	* @return Returns TRUE if a correct ship placement is found, or FALSE if an error is found.
	 */
	public boolean verifyApplication(boolean bStraightLines)
		{
		try
			{
			// loop performed for each subsequent ship
			for (int iShipNumber = 1; iShipNumber <= iNumberOfShips; ++iShipNumber)
				{
				Ship oShip = getShip(iShipNumber);
				ShipVerification oVerifier = new ShipVerification();
				oVerifier.importShip(oShip);
				//checking if all positions are on the board
				if (oVerifier.spacesOnBoard() == false)
					return false;
				//checking if all positions create a uniform structure (they touch each other)
				if (oVerifier.fieldsConnected(bStraightLines) == false)
					return false;
				//checking that the ship is not touching its side or corner with another ship
				if (oVerifier.NoNeighbors() == false)
					return false;
				}
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		return true;
		}
	}
