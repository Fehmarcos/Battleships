package pl.vgtworld.games.ship;

import pl.vgtworld.exceptions.ParameterException;
import pl.vgtworld.exceptions.DeveloperException;
import pl.vgtworld.tools.Position;

/**
 * Class that checks whether a ship meets various conditions regarding its
 * structure or board placement.
 *
 * <p>
 * updates:
 * 1.1
 * - added bStraightLines parameter to {@link #fieldsConnected (boolean)} method.
 * </p>
 *
 * @author VGT
 * @version 1.1
 */
public class ShipVerification
	{
	/**
	 */
	private Ship oShip;
	/**
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
	 * Importing the ship which will be verified.
	 * 
	 * @param oShip Ship for testing.
	 */
	public void importShip(Ship oShip)
		{
		this.oShip = oShip;
		oBoard = oShip.getBoard();
		}
	/**
	 * Checks if all of the ship's positions are on the board.
	 * 
	 * @return Returns TRUE if the ship is fully on board, FALSE otherwise.
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
	 * Checks if all positions of a given ship form a uniform structure
	 * (they touch the edges and do not form unconnected areas on the board)
	 *
	 * updates:
	 *
	 * 1.1 - adding the first parameter
	 *
	 * @param bStraightLines Whether positions must be on a straight line.
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
			while (bChanges == true)
				{
				bChanges = false;
				for (int i = 0; i < oShip.getSize(); ++i)
					if (aCorrect[i] == true)
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
								if (iNumberFields > 0 && aCorrect[iNumberFields - 1] == false)
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
				if (bStraightLines == true)
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
					if (bLevel == true || bVertical == true)
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
	 * Checks if any of a ship's fields are in contact with another ship.
	 * 
	 * @return Returns FALSE if there is a contact with another ship,
         * or TRUE otherwise.
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
