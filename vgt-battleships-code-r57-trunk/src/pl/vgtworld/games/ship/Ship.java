package pl.vgtworld.games.ship;

import pl.vgtworld.exceptions.ParameterException;
import pl.vgtworld.exceptions.DeveloperException;
import pl.vgtworld.tools.Position;

/**
 * Class representing a ship.
 * 
 * @author VGT
 * @version 1.0
 */
public class Ship
	{
	/**
	 */
	private Board oBoard;
	/**
	 */
	private int iSize;
	/**
	 * A table containing the ship's coordinates on the board.
	 */
	private Position[] aCoordenates;
	/**
	 * A table containing the ship's hit coordinates.
	 */
	private boolean[] aHits;
	/**
	 * Number of hits the ship received.
	 */
	private int iHitsQuantity;
	/**
	 * Default constructor - creates a ship with a given size.
	 * 
	 * @param iSize The number of spaces a ship takes on the board.
	 * @param oBoard The board object where the ship is placed.
	 */
	public Ship(int iSize, Board oBoard)
		{
		this.oBoard = oBoard;
		this.iSize = iSize;
		aCoordenates = new Position[ iSize ];
		//padding the coordinate table with default values ​​(-1, -1)
		for (int i = 0; i < iSize; ++i)
			{
			aCoordenates[i] = new Position(2);
			aCoordenates[i].setX(-1);
			aCoordenates[i].setY(-1);
			}
		aHits = new boolean[ iSize ];
		for (int i = 0; i < iSize; ++i)
			aHits[i] = false;
		iHitsQuantity = 0;
		}
	/**
	 * Display information about the ship.
	 */
	@Override public String toString()
		{
		String sReturn = "Ship\n";
		sReturn+= "Size: " + iSize + "\n";
		sReturn+= "Position:";
		for(Position oObj: aCoordenates)
			sReturn+= " " + oObj;
		sReturn+= "\nHits: [";
		for(boolean bHit: aHits)
			sReturn+= " " + bHit;
		sReturn+= " ]";
		return sReturn;
		}
	/**
	 * Returns an object containing coordinates with the given number.
	 * 
	 * @param iNumberFields The position whose coordinates are to be
         * returned (counted from 1).
	 * @return coordinates with the given number.
	 * @throws ParameterException Throws an exception if the number is less
         * than 1 or greater than the ship size. 
	 */
	public Position getField(int iNumberFields) throws ParameterException
		{
		if (iNumberFields > iSize || iNumberFields <= 0)
			throw new ParameterException("iNumberFields = " + iNumberFields);
		return aCoordenates[ iNumberFields - 1 ];
		}
	/**
	 * Returns a ship number in case said ship is in a field, otherwise
         * returns 0.
	 * 
	 * @param iX Coordinate X in board.
	 * @param iY Coordinate Y in board.
	 * @return Ship position number (counted from 1).
	 * @throws ParameterException Throws an exception if the coordinates are
         * outside the board's range.
	 */
	public int getNumberPosition(int iX, int iY) throws ParameterException
		{
		if (iX + 1 > oBoard.getWidth() || iX < 0)
			throw new ParameterException("iX = " + iX);
		if (iY + 1 > oBoard.getHeight() || iY < 0)
			throw new ParameterException("iY = " + iY);
		for (int i = 0; i < iSize; ++i)
			if (aCoordenates[i].getX() == iX && aCoordenates[i].getY() == iY)
				return (i + 1);
		return 0;
		}
	/**
	 * Returns the ship size.
	 * 
	 * @return Ship size.
	 */
	public int getSize()
		{
		return iSize;
		}
	/**
	 * Returns the ship parts that were hit.
	 * 
	 * @return Number of ship parts hit.
	 */
	public int getNumberOfHits()
		{
		return iHitsQuantity;
		}
	/**
	 * Returns whether the ship has not received any hits.
	 * 
	 * @return Returns TRUE if the ship is intact, or FALSE otherwise.
	 */
	public boolean getUntouched()
		{
		if (iHitsQuantity == 0)
			return true;
		else
			return false;
		}
	/**
	 * Returns information whether the ship has been hit at least once.
	 * 
	 * @return Returns TRUE if the ship has been hit, or FALSE otherwise.
	 */
	public boolean getHits()
		{
		if (iHitsQuantity > 0)
			return true;
		else
			return false;
		}
	/**
	 * Returns information whether the ship has sunk.
	 * 
	 * @return Returns TRUE if all ship positions are hit, or FALSE otherwise.
	 */
	public boolean getSunk()
		{
		if (iHitsQuantity > 0 && iHitsQuantity == iSize)
			return true;
		else
			return false;
		}
	/**
	 * Returns a reference of the board object it is on.
	 * 
	 * @return The board where the ship is placed.
	 */
	public Board getBoard()
		{
		return oBoard;
		}
	/**
	 * Lets you set coordinates with a given number.
	 * 
	 * It also marks the ship's occupied spaces on the board.
	 * 
	 * @param iNumberFields Number of ship spaces (counted from 1).
	 * @param iX X coordinate (counted from 0).
	 * @param iY Y coordinate (counted from 0).
	 * @throws ParameterException Throws an exception if the number
         * is outside a ship space's range, when coordinates are outside the
         * board range, or when it's not possible to place a ship on the board.
	 */
	public void setField(int iNumberFields, int iX, int iY) throws ParameterException
		{
		if (iNumberFields > iSize)
			throw new ParameterException("iNumberFields = " + iNumberFields);
		if (iX + 1 > oBoard.getWidth() || iX < -1)
			throw new ParameterException("iX = " + iX);
		if (iY + 1 > oBoard.getHeight() || iY < -1)
			throw new ParameterException("iY = " + iY);
		if (iX >= 0 && iY >= 0 && oBoard.getField(iX, iY) != FieldTypeBoard.BOARD_FIELD_EMPTY)
			throw new ParameterException("iX, iY - pole niepuste");
		if (aCoordenates[ iNumberFields - 1].getX() == -1 && aCoordenates[ iNumberFields - 1 ].getY() == -1)
			{
			//first coordinate setting
			if (iX >= 0 && iY >= 0)
				{
				aCoordenates[ iNumberFields - 1 ].setX(iX);
				aCoordenates[ iNumberFields - 1 ].setY(iY);
				oBoard.setField(iX, iY, FieldTypeBoard.SHIP_BOARD);
				}
			}
		else
			{
			//the field already has co-ordinates set
			//resetting the coordinates
			oBoard.setField(aCoordenates[ iNumberFields - 1 ].getX(), aCoordenates[ iNumberFields - 1 ].getY(), FieldTypeBoard.BOARD_FIELD_EMPTY);
			aCoordenates[ iNumberFields - 1 ].setX(-1);
			aCoordenates[ iNumberFields - 1 ].setY(-1);
			//resetting the coordinates
			setField(iNumberFields, iX, iY);
			}
		}
	/**
	 * Sets the coordinates of all ship parts to the default values ​​(-1, -1).
         * @throws ParameterException 
	 */
	public void resetFields()
        {
			try
			{
               for (int i = 1; i <= iSize; ++i)
                    setField(i, -1, -1);
			}
			catch (ParameterException e)
			{
			   throw new DeveloperException(e);
			}
        }
	/**
	 * Checks whether a shot within a certain coordinate hits a ship.
	 * 
	 * If so, marks the ship's space as hit and returns TRUE, otherwise it
         * returns FALSE.
	 * 
	 * It also marks the shot fields on a board.
	 * 
	 * @param iX X coordinate shot.
	 * @param iY Y coordinate shot.
	 * @return Returns TRUE for a hit, or FALSE otherwise.
	 * @throws ParameterException Throws an exception if the coordinates
         * are outside the board's range.
	 */
	public boolean shot(int iX, int iY) throws ParameterException
		{
		if (iX < 0 || iX >= oBoard.getWidth())
			throw new ParameterException("iX = " + iX);
		if (iY < 0 || iY >= oBoard.getHeight())
			throw new ParameterException("iY = " + iY);
		try
			{
			for (int i = 0; i < iSize; ++i)
				if (aCoordenates[i].getX() == iX && aCoordenates[i].getY() == iY && aHits[i] == false)
					{
					//there was a hit
					aHits[i] = true;
					++iHitsQuantity;
					oBoard.setField(iX, iY, FieldTypeBoard.CUSTOMS_SHOT_BOARD);
					if (getSunk() == true)
						{
						//marking the fields adjacent to the ship as unavailable
						for (int j = 1; j <= iSize; ++j)
							{
							Position oField = getField(j);
							for (int k = -1; k <= 1; ++k)
								for (int l = -1; l <= 1; ++l)
									{
									Position oAdjacentField = new Position(2);
									oAdjacentField.setX(oField.getX() + k);
									oAdjacentField.setY(oField.getY() + l);
									if (oAdjacentField.getX() < 0 || oAdjacentField.getX() >= oBoard.getWidth()
										|| oAdjacentField.getY() < 0 || oAdjacentField.getY() >= oBoard.getHeight()
										)
										continue;
									if (oBoard.getField(oAdjacentField.getX(), oAdjacentField.getY()) == FieldTypeBoard.BOARD_FIELD_EMPTY)
										oBoard.setField(oAdjacentField.getX(), oAdjacentField.getY(), FieldTypeBoard.BOARD_FIELD_UNAVAILABLE);
									}
							}
						}
					return true;
					}
			oBoard.setField(iX, iY, FieldTypeBoard.BOARD_SHOT_FALSE);
			return false;
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	/**
	 * Returns whether the ship has sunk.
	 * 
	 * @deprecated replaced by method {@link #getSunk()}
	 * @return returns TRUE if the ship is sunk, FALSE otherwise
	 */
	public boolean isSunken()
		{
		if (iSize > iHitsQuantity)
			return false;
		else
			return true;
		}
	}
