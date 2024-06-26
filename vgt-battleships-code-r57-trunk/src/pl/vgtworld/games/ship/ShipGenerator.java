package pl.vgtworld.games.ship;

import pl.vgtworld.exceptions.ParameterException;
import pl.vgtworld.exceptions.DeveloperException;
import pl.vgtworld.tools.Position;

/**
 * Class that creates a ship object on a provided board, with marked ship positions.
 * 
 * @author VGT
 * @version 1.0
 */
public class ShipGenerator
	{
	/**
	 */
	private Board oBoard;
	/**
	 * Ship container.
	 */
	private ShipIterator oShips;
	/**
	 * Loads the positions of all board fields with ships.
	 */
	private Position[] aShipSpaces;
	/**
	 * Number of fields stored in the aShipSpaces array.
	 */
	private int iNumberOfShipsField;
	/**
	 * Default constructor.
	 * 
	 * @param oBoard Board with ships deployed.
	 */
	public ShipGenerator(Board oBoard)
		{
		this.oBoard = oBoard;
		oShips = null;
		aShipSpaces = new Position[0];
		iNumberOfShipsField = 0;
		}
	/**
	 * Starts the process of creating a ship container on the board.
	 * 
	 * @return Returns the created ship container.
	 */
	public ShipIterator generateShips()
		{
		try
			{
			//look for marked fields on the board and clear the board
			findField();
			//creation of a ship container

			oShips = new ShipIterator(oBoard);
			int iNumberOfShips = 0;
			// filling the container with ships
			while (iNumberOfShipsField > 0)
				{
				Position[] aShip = generateShip();
				oShips.addAShip( aShip.length );
				++iNumberOfShips;
				for (int i = 0; i < aShip.length; ++i)
					oShips.getShip(iNumberOfShips).setField(i+1, aShip[i].getX(), aShip[i].getY());
				}
			//creation of a ship container
			return this.oShips;
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	/**
	 * Searches for ships on a board position.
	 * 
	 * Their positions are written in the aShipSpaces array, and the number
         * of fields to iNumberOfShipsField. It also cleans the board's marked
         * fields to prepare it as part of the ship container being created.
	 */
	private void findField()
		{
		try
			{
			aShipSpaces = new Position[0];
			iNumberOfShipsField = 0;
			for (int i = 0; i < oBoard.getWidth(); ++i)
				for (int j = 0; j < oBoard.getHeight(); ++j)
					if (oBoard.getField(i, j) == FieldTypeBoard.SHIP_BOARD)
						{
						Position[] aNewList = new Position[ iNumberOfShipsField + 1 ];
						//rewriting the existing list
						for (int k = 0; k < iNumberOfShipsField; k++)
							aNewList[k] = aShipSpaces[k];
						//adding a new element at the end
						Position oObj = new Position(2);
						oObj.setX(i);
						oObj.setY(j);
						aNewList[iNumberOfShipsField] = oObj;
						++iNumberOfShipsField;
						aShipSpaces = aNewList;
						}
			//blurring fields on the board
			for (int i = 0; i < aShipSpaces.length; ++i)
				oBoard.setField(aShipSpaces[i].getX(), aShipSpaces[i].getY(), FieldTypeBoard.BOARD_FIELD_EMPTY);
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	/**
	 * Takes and removes the position from the aShipSpaces array, generating
         * a list of fields for a ship.
	 * 
	 * After fetching the first position, it scans the list until it obtains
         * an array with all the positions linked with eachother.
	 * 
	 * @return Returns an array containing a list of fields for a ship.
	 */
	private Position[] generateShip()
		{
		if (aShipSpaces.length == 0)
			throw new DeveloperException("No fields on the list");
		try
			{
			//creating an array that can hold a list of all the fields currently on the board
			//(can be converted into a container in the future)
			int iSize = 0;
			Position[] aField = new Position[ aShipSpaces.length ];
			// get first position from board
			aField[ iSize++ ] =  getField(0);
			// loop to take subsequent positions until some neighbors are found
			boolean bNewNeighbor = true;
			while (bNewNeighbor)
				{
				bNewNeighbor = false;
				for (int i = 0; i < iSize; ++i)
					{
					int iNumberOfSearchedNeighbor = findNeighbor(aField[i]);
					if (iNumberOfSearchedNeighbor != -1)
						{
						aField[ iSize++ ] = getField(iNumberOfSearchedNeighbor);
						bNewNeighbor = true;
						}
					}
				}
			// create a new array with Sizes clipped to the found ship, rewrite pol and return to it
			Position[] aReturn = new Position[iSize];
			for (int i = 0; i < aReturn.length; ++i)
				aReturn[i] = aField[i];
			return aReturn;
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	/**
	 * Checks if there are adjacent fields to the fields passed in
         * the parameter.
	 * 
	 * If so, returns its index in the aShipSpaces array.
	 * If there's more than one field, returns the first position's index.
	 * If this position has no neighbors, returns -1.
	 * 
	 * @param oPosition coordinates to look for adjacent fields.
	 * @return Returns the index position of the neighbor,
         * or -1 if none is found.
	 */
	private int findNeighbor(Position oPosition)
		{
		for (int i = 0; i < aShipSpaces.length; ++i)
			{
			if (
				(aShipSpaces[i].getX() == oPosition.getX() - 1 && aShipSpaces[i].getY() == oPosition.getY()) ||
				(aShipSpaces[i].getX() == oPosition.getX() + 1 && aShipSpaces[i].getY() == oPosition.getY()) ||
				(aShipSpaces[i].getX() == oPosition.getX() && aShipSpaces[i].getY() == oPosition.getY() - 1) ||
				(aShipSpaces[i].getX() == oPosition.getX() && aShipSpaces[i].getY() == oPosition.getY() + 1)
				)
				return i;
			}
		return -1;
		}
	/**
	 * Removes the element with a given index from aShipSpaces and
         * returns it.
	 * 
	 * @param iIndex Index position to be removed.
	 * @return Returns the field removed.
	 * @throws ParameterException Throws an exception if index is outside
         * the available field range.
	 */
	private Position getField(int iIndex) throws ParameterException
		{
		if (iIndex >= iNumberOfShipsField || iIndex < 0)
			throw new ParameterException("iIndex = " + iIndex);
		Position[] aNewList = new Position[iNumberOfShipsField - 1];
		// rewrite elements to new array, skipping deleted one
		int iCounter = 0;
		for (int i = 0; i < iNumberOfShipsField; ++i)
			if (i != iIndex)
				{
				aNewList[iCounter] = aShipSpaces[i];
				++iCounter;
				}
		// save the item to be removed and replace the field array in the object
		Position oReturn = aShipSpaces[iIndex];
		aShipSpaces = aNewList;
		--iNumberOfShipsField;
		
		return oReturn;
		}
	}
