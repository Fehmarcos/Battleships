package pl.vgtworld.games.ship;

import pl.vgtworld.exceptions.ParameterException;
import pl.vgtworld.exceptions.DeveloperException;
import pl.vgtworld.tools.Position;

/**
 * A class that checks whether a ship meets various conditions
 * regarding its structure or placement on the board. <br />
 *
 * <p>
 * updates: <br />
 * 1.1 <br />
 * - added bStraightLines parameter to {@link #fieldsConnected (boolean)} method. <br />
 * </p>
 *
 * @author VGT
 * @version 1.1
 */
public class ShipVerification
	{
	/**
	 * Ship facility to be verified.
	 */
	private Ship oShip;
	/**
	 * Board on which the ship has been verified.

	 */
	private Board oBoard;
	/**
	 * Default constructor.
	 */
	public ShipVerification()
		{
		oShip = null;
		oBoard = null;
		}
	/**
	 * Import of the vessel for which the tests are to be performed.
	 * 
	 * @param oShip Ship facility for testing.
	 */
	public void importShip(Ship oShip)
		{
		this.oShip = oShip;
		oBoard = oShip.getBoard();
		}
	/**
	 * The method checks if all of the ship's positions are on the board.
	 * 
	 * @return Returns TRUE if the ship is fully boarded, FALSE otherwise.
	 */
	public boolean spacesOnBoard()
		{
		if (oShip == null)
			throw new DeveloperException("Ship item not imported");
		try
			{
			for (int i = 1; i <= oShip.getSize(); ++i)
				{
				Position oField = oShip.getField(i);
				if (oField.getX() == -1 || oField.getX() >= oBoard.getWidth()
					|| oField.getY() == -1 || oField.getY() >= oBoard.getHeight()
					)
					return false;
				}
			return true;
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	/**
	 * @deprecated
	 */
	public boolean fieldsConnected()
		{
		return fieldsConnected(false);
		}
	/**
	 * The method checks if all positions of a given vessel form a uniform structure
	 * (they touch the edges and do not form two or more unconnected areas on the board) <br />
	 *
	 * updates: <br />
	 *
	 * 1.1 - adding the first parameter
	 *
	 * @param bStraightLines Specifies whether positions must be on a single vertical or horizontal line.
	 * @return Returns TRUE if the ship is properly constructed, FALSE otherwise.
	 */
	public boolean fieldsConnected(boolean bStraightLines)
		{
		if (oShip == null)
			throw new DeveloperException("Ship item not imported");
		try
			{
			int iQuantityValid = 0;
			boolean[] aCorrect = new boolean[ oShip.getSize() ];
			for (int i = 0; i < oShip.getSize(); ++i)
				aCorrect[i] = false;
			// the first field of the ship valid from the automat
			++iQuantityValid;
			aCorrect[0] = true;
			boolean bChanges = true;
			// loop running until there are any changes to the number of valid fields
			while (bChanges)
				{
				bChanges = false;
				for (int i = 0; i < oShip.getSize(); ++i)
					if (aCorrect[i])
						{
						Position oValidField = oShip.getField(i+1);
						for (int j = -1; j <= 1; ++j)
							for (int k = -1; k <= 1; ++k)
								{
								if (oValidField.getX() + j < 0 || oValidField.getX() + j >= oBoard.getWidth()
									|| oValidField.getY() + k < 0 || oValidField.getY() + k >= oBoard.getHeight()
									)
									continue;
								int iNumberFields = oShip.getNumberPosition(oValidField.getX() + j, oValidField.getY() + k);
								if (iNumberFields > 0 && !aCorrect[iNumberFields - 1])
									{
									bChanges = true;
									++iQuantityValid;
									aCorrect[iNumberFields - 1] = true;
									}
								}
						}
				}
			
			if (iQuantityValid == oShip.getSize())
				{
				// further checking if position is making lines if required
				if (bStraightLines)
					{
					int iX = -1;
					int iY = -1;
					boolean bLevel = true;
					boolean bVertical = true;
					for (int i = 1; i <= iQuantityValid; ++i)
						{
						if (iX == -1)
							iX = oShip.getField(i).getX();
						else if (iX != oShip.getField(i).getX())
							bLevel = false;
						if (iY == -1)
							iY = oShip.getField(i).getY();
						else if (iY != oShip.getField(i).getY())
							bVertical = false;
						}
					if (bLevel || bVertical)
						return true;
					else
						return false;
					}
				else
					return true;
				}
			else
				return false;
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	/**
	 * The method checks if any of a ship's square is in contact with another ship.
	 * 
	 * @return Returns FALSE if there is a contact with another ship, or TRUE otherwise.
	 */
	public boolean NoNeighbors()
		{
		if (oShip == null)
			throw new DeveloperException("Ship item not imported");
		try
			{
			for (int i = 1; i <= oShip.getSize(); ++i)
				{
				Position oField = oShip.getField(i);
				for (int j = -1; j <= 1; ++j)
					for (int k = -1; k <=1; ++k)
						{
						Position oAdjacentField = new Position(2);
						oAdjacentField.setX(oField.getX() + j);
						oAdjacentField.setY(oField.getY() + k);
						// reject checking any fields that are loading outside the scope board
						if (oAdjacentField.getX() < 0 || oAdjacentField.getX() >= oBoard.getWidth()
							|| oAdjacentField.getY() < 0 || oAdjacentField.getY() >= oBoard.getHeight()
							)
							continue;
						if (oBoard.getField(oAdjacentField.getX(), oAdjacentField.getY()) == FieldTypeBoard.SHIP_BOARD
							&& oShip.getNumberPosition(oAdjacentField.getX(), oAdjacentField.getY()) == 0
							)
							return false;
						}
				}
			return true;
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	}
