package pl.vgtworld.games.ship;

import pl.vgtworld.exceptions.ParameterException;
import pl.vgtworld.exceptions.DeveloperException;
import pl.vgtworld.tools.Position;

/**
 * A container class that stores the player's ships. <br />
 *
 * <p>
 * updates: <br />
 * 1.2 <br />
 * - added bStraightLines parameter to the {@link #verifyApplication (boolean)} method. <br />
 * </p>
 * 
 * @author VGT
 * @version 1.2
 */
public class ShipIterator
	{
	/**
	 * A reference to the board where the ships are stored.
	 */
	private Board oBoard;
	/**
	 * Array that holds the ships.
	 */
	private Ship[] aShips;
	/**
	 * Number of ships currently stored at the facility.
	 */
	private int iNumberOfShips;
	/**
	 * The object that stores the co-ordinates of the last handled shot.
	 */
	private Position oLastShot;
	/**
	 * Default constructor.
	 * 
	 * @param oBoard A reference to the board object where the ships will be placed.
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
	 * Display a list of the ships held by the facility on standard output.
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
	 * The method returns a reference to the ship object with the given number.
	 * 
	 * @param iNumber Ship number to be returned (counted from 1).
	 * @return Returns a reference to a ship with the given number.
	 * @throws ParameterException Rolls an exception if the ship number is less than or equal to 0,
	 * or greater than the number of ships stored at the facility.
	 */
	public Ship getShip(int iNumber) throws ParameterException
		{
		if (iNumber > iNumberOfShips || iNumber <= 0)
			throw new ParameterException("iNumber = " + iNumber);
		return aShips[iNumber - 1];
		}
	/**
	 * Returns the co-ordinates containing the field with the number given in the second parameter
	 * belonging to the ship with the number provided in the first parameter.
	 * 
	 * @param iShipNumber Ship number to be returned (counted from 1).
	 * @param iFieldNumber The position number to be returned (counted from 1).
	 * @return Returns an object containing the co-ordinates of the retrieved position.
	 * @throws ParameterException Throw an exception if the ship number or position no. Is out of the available range.
	 */
	public Position getField(int iShipNumber, int iFieldNumber) throws ParameterException
		{
		if (iShipNumber > iNumberOfShips || iShipNumber <= 0)
			throw new ParameterException("iShipNumber = " + iShipNumber);
		return aShips[ iShipNumber - 1 ].getField(iFieldNumber);
		}
	/**
	 * Returns a reference to the board object where the ships held by the object are placed.
	 * 
	 * @return A reference to the board.
	 */
	public Board getBoard()
		{
		return oBoard;
		}
	/**
	 * Returns the co-ordinates of the last shot.
	 * 
	 * @return co-ordinates the last shot.
	 */
	public Position getLastShot()
		{
		return oLastShot;
		}
	/**
	 * Returns the number of ships currently stored at the facility
	 * (it does not matter if the ships are placed on board).
	 * 
	 * @return Number of ships.
	 */
	public int getNumberOfShips()
		{
		return iNumberOfShips;
		}
	/**
	* Returns the number of ships currently stored in the Sizeze object given in the parameter
	* (it does not matter if the ships are placed on board).
	*
	* @param iSize Size of ships to be counted.
	* @return Number of ships with the given Sizeze.
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
	 * The method returns the number of ships hit but not sunk.
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
	 * The method returns the number of sunken ships.
	 *
	 * @return Number of ships sunk.
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
	 * Returns information about the number of ships that have not yet been hit.
	 *
	 * @return Number of ships missed.
	 * @since 1.1 
	 */
	public int getNumberOfUndamagedShips()
		{
		return getNumberOfShips() - getNumberOfShipsHit() - getNumberOfSunkenShips();
		}
	/**
	 * Returns the Size of the largest ship currently stored in the container.
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
	 * The method calculates the total number of fields taken on board by individual ships. <br />
	 *
	 * It does not matter if positions have been placed on the board - the method calculates the required, not the actual number of poles on the board
	 * occupied by ships.
	 *
	 * @return Total size of all ships.
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
	 * The method calculates the total number of hits for all ships stored by the container.
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
	 * The method allows you to set board co-ordinates for the selected ship position.
	 *
	 * @param iShipNumber Number of the ship for which the co-ordinates position (counted from 1) are set.
	 * @param iFieldNumber The position number of the given vessel for which the co-ordinates (counted from 1) are set.
	 * @param iX Coordinate X position on the board where the field should be set (counted from 0).
	 * @param iY Coordinate Y position on the board where the field should be set (counted from 0).
	 * @throws ParameterException Throws an exception in case of exceeding the numbering range of ships, fields of a given ship, or giving
	 * coordinates outside the board range.
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
	 * The method sets all positions for all ships to the starting position (-1, -1).
	 */
	public void resetFields()
		{
		for (int i = 0; i < iNumberOfShips; ++i)
			aShips[i].resetFields();
		}
	/**
	 * Main method of communicating shot information to all objects that require it. <br />
*
* The information about the shots is transmitted consecutively to all ships in the container, as long as
* will not report a successful hit. Ship facilities handle this information for their own needs,
* and also make appropriate markings on the board.
*
* @param iX Coordinate X position on the board where the shot is taken.
* @param iY Coordinate Y position on the board where the shot is taken.
* @return Returns TRUE if any ship was hit or FALSE if the shot was missed.
* @throws ParameterException Throws an exception if the given co-ordinates are outside the board dimensions.
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
	 * Adds a ship of the given size to the container. <br />
	 *
	 * After the ship is created, all ship positions are set to the default (-1, -1).
	 *
	 * @param iSize Size of the vessel being created.
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
	 * @throws ParameterException It rolls an exception if the ship number is outside the range of the available ships.
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
	* The method checks that all ships have been placed on the board and that their placement complies with the rules of the game. <br />
	*
	* They are checked in sequence: whether all positions are on the board, are the positions combined into one element,
	* and that no half is sideways or a corner with another ship.
	*	
	* updates: <br />
	*
	* 1.2 - adding the first parameter
	*
	* @param bStraightLines Specifies whether ships can only be vertical / horizontal lines.
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
