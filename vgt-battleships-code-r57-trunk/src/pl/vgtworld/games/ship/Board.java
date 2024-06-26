package pl.vgtworld.games.ship;

import pl.vgtworld.exceptions.ParameterException;

/**
 * A class representing the game board.
 * 
 * @author VGT
 * @version 1.0
 */
public class Board
	{
	
	private int iWidth;
	
	private int iHeight;
	/**
	 * Number of fields that the opponent hasn't shot at yet.
	 */
	private int iUnknownFields;
	/**
	 * A board storing the state of individual fields of the board.
	 */
	FieldTypeBoard[][] aBoard;
	/**
	 * Default constructor - creates boards with a default size of 10x10.
	 */
	public Board()
		{
		this(10, 10);
		}
	/**
	 * Overload constructor - creates square-shaped boards of a given length.
	 * 
	 * @param iSize A value specifying the board's width and height.
	 */
	public Board(int iSize)
		{
		this(iSize, iSize);
		}
	/**
	 * Overload constructor - creates boards of any width and height.
	 * 
	 * @param iWidth A value specifying the board's width.
	 * @param iHeight A value specifying the board's height.
	 */
	public Board(int iWidth, int iHeight)
		{
		this.iWidth = iWidth;
		this.iHeight = iHeight;
		iUnknownFields = iWidth * iHeight;
		aBoard = new FieldTypeBoard[ iWidth ][ iHeight ];
		clean();
		}
	/**
	 * Displaying the board.
	 */
	@Override public String toString()
		{
		String sReturn = "Board\n";
		sReturn+= "Size: [ " + iWidth + ", " + iHeight + " ]\n";
		for (int j = 0; j < iHeight; ++j)
			{
			sReturn+= "|";
			for (int i = 0; i < iWidth; ++i)
				{
				FieldTypeBoard eField = aBoard[i][j];
				switch (eField)
					{
					case BOARD_FIELD_EMPTY:
						sReturn+= "_";
						break;
					case SHIP_BOARD:
						sReturn+= "S";
						break;
					case CUSTOMS_SHOT_BOARD:
						sReturn+= "X";
						break;
					case BOARD_SHOT_FALSE:
						sReturn+= "-";
						break;
					case BOARD_FIELD_UNAVAILABLE:
						sReturn+= "!";
						break;
					default:
						sReturn+= "?";
						break;
					}
				sReturn+= "|";
				}
			if (j + 1 < iHeight)
				sReturn+="\n";
			}
		return sReturn;
		}
	/**
	 * 
	 * @return The size of the board horizontally.
	 */
	public int getWidth()
		{
		return iWidth;
		}
	/**
	 * 
	 * @return The size of the board vertically.
	 */
	public int getHeight()
		{
		return iHeight;
		}
	/**
	 * 
	 * @return Number of unknown fields.
	 */
	public int getUnknownQuantity()
		{
		return iUnknownFields;
		}
	/**
	 * 
	 * @param iX The board's X coordinate (counted from 0).
	 * @param iY The board's Y coordinate (counted from 0).
	 * @return Returns the field type for the given coordinates.
	 * @throws ParameterException Throws an exception when the coordinates 
         * are out of the board's range.
	 */
	public FieldTypeBoard getField(int iX, int iY) throws ParameterException
		{
		if (iX >= iWidth || iX < 0)
			throw new ParameterException("iX = " + iX);
		if (iY >= iHeight || iY < 0)
			throw new ParameterException("iY = " + iY);
		return aBoard[iX][iY];
		}
	/**
         * 
         * This method sets the field type to the coordinates, and updates the
         * Unknown Fields variable.
	 * 
	 * @param iX X coordinate on the board (counted from 0).
	 * @param iY Y coordinate on the board (counted from 0).
	 * @param eType Field type, on which the field with the given
         * coordinates is set.
	 * @throws ParameterException Throws an exception when the coordinates
         * are out of the board's range.
	 */
	public void setField(int iX, int iY, FieldTypeBoard eType) throws ParameterException
		{
		if (iX + 1 > iWidth)
			throw new ParameterException("iX = " + iX);
		if (iY + 1 > iHeight)
			throw new ParameterException("iY = " + iY);
		FieldTypeBoard eOldType = aBoard[iX][iY];
		aBoard[iX][iY] = eType;
		//determining whether the number of fields unknown has changed and possible correction of this value
		if (
			(eOldType == FieldTypeBoard.BOARD_FIELD_EMPTY || eOldType == FieldTypeBoard.SHIP_BOARD)
			&& (eType == FieldTypeBoard.BOARD_SHOT_FALSE || eType == FieldTypeBoard.CUSTOMS_SHOT_BOARD || eType == FieldTypeBoard.BOARD_FIELD_UNAVAILABLE)
			)
			{
			--iUnknownFields;
			}
		else if (
			(eOldType == FieldTypeBoard.BOARD_SHOT_FALSE || eOldType == FieldTypeBoard.CUSTOMS_SHOT_BOARD || eOldType == FieldTypeBoard.BOARD_FIELD_UNAVAILABLE)
			&& (eType == FieldTypeBoard.BOARD_FIELD_EMPTY || eType == FieldTypeBoard.SHIP_BOARD)
			)
			{
			++iUnknownFields;
			}
		}
	/**
	 * Lets you resize an already created board.
	 * 
	 * To avoid information loss, only use on an empty board.
	 * After resizing, the {@link #clear ()} method is called.
         * 
	 * @throws ParameterException Throws an exception if width and/or height is less than 1.
	 */
	 //TODO zmienSize
	public void zmienSize(int iWidth, int iHeight) throws ParameterException
		{
		if (iWidth < 1)
			throw new ParameterException("iWidth = " + iWidth);
		if (iHeight < 1)
			throw new ParameterException("iHeight = " + iHeight);
		this.iWidth = iWidth;
		this.iHeight = iHeight;
		iUnknownFields = iWidth * iHeight;
		aBoard = new FieldTypeBoard[ iWidth ][ iHeight ];
		clean();
		}
	/**
	 * Clears the whole board, setting all fields to "empty".
	 */
	public void clean()
		{
		for (int i = 0; i < iWidth; ++i)
			for (int j = 0; j < iHeight; ++j)
				aBoard[i][j] = FieldTypeBoard.BOARD_FIELD_EMPTY;
		iUnknownFields = iWidth * iHeight;
		}
	}
