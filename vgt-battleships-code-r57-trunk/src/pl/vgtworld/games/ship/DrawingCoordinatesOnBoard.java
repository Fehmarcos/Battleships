package pl.vgtworld.games.ship;

import pl.vgtworld.tools.Position;

/**
 * Abstract class containing static methods that connect coordinates on a pixel
 * board to coordinates in the field.
 * 
 * @author VGT
 * @version 1.0
 */
public abstract class DrawingCoordinatesOnBoard
	{
	/**
	 * Value of the margin by which the drawn board will be moved away 
         * from the panel's edge.
	 * 
	 * Value expressed as a percentage of the panel width.
	 */
	static final int iMargins = 5;
	/**
	 * A method that converts the coordinates of a click on a component 
         * to a board's field coordinates.
	 * 
	 * Returns an object containing clicked field's coordinates,
         * or the coordinates (-1, -1) if the click didn't hit any field.
	 * 
	 * @param iPanelWidth Panel width, used to draw the board in pixels.
	 * @param iPanelHeight Panel height.
	 * @param iBoardWidth Board width.
	 * @param iBoardHeight Board height.
	 * @param iX X coordinate of the selected pixel.
	 * @param iY Y coordinate of the selected pixel.
	 * @return Returns a position object with the selected field's coordinates.
	 */
	public static Position pixToField(int iPanelWidth, int iPanelHeight, int iBoardWidth, int iBoardHeight, int iX, int iY)
		{
		Position oPosition = new Position(2);
		oPosition.setX(-1);
		oPosition.setY(-1);
		Position oCoOrdinatesTopLeft;
		Position oCoOrdinatesBottomRight;
		//finding X
		for (int i = 0; i < iBoardWidth; ++i)
			{
			oCoOrdinatesTopLeft = DrawingCoordinatesOnBoard.fieldToPixTopLeft(iPanelWidth, iPanelHeight, iBoardWidth, iBoardHeight, i, 0);
			oCoOrdinatesBottomRight = DrawingCoordinatesOnBoard.fieldToPixBottomRight(iPanelWidth, iPanelHeight, iBoardWidth, iBoardHeight, i, 0);
			if (oCoOrdinatesTopLeft.getX() < iX && oCoOrdinatesBottomRight.getX() > iX)
				{
				oPosition.setX(i);
				break;
				}
			}
		//finding Y
		for (int i = 0; i < iBoardHeight; ++i)
			{
			oCoOrdinatesTopLeft = DrawingCoordinatesOnBoard.fieldToPixTopLeft(iPanelWidth, iPanelHeight, iBoardWidth, iBoardHeight, 0, i);
			oCoOrdinatesBottomRight = DrawingCoordinatesOnBoard.fieldToPixBottomRight(iPanelWidth, iPanelHeight, iBoardWidth, iBoardHeight, 0, i);
			if (oCoOrdinatesTopLeft.getY() < iY && oCoOrdinatesBottomRight.getY() > iY)
				{
				oPosition.setY(i);
				break;
				}
			}
		
		return oPosition;
		}
	/**
	 * The method converts the board's field coordinates to the coordinates
         * of the field's top left pixel in the panel's drawn board.
	 * 
	 * WARNING! These coordinates are already outside the field (the fields
         * are separated by a single row of pixels in order to draw the mesh)
         * and contain the point where the mesh crosses between the fields.
	 * 
	 * @param iPanelWidth Panel width.
	 * @param iPanelHeight Panel height.
	 * @param iBoardWidth Board width.
	 * @param iBoardHeight Board height.
	 * @param iXField X coordinate's converted position (counted from 0).
	 * @param iYField Y coordinate's converted position (counted from 0).
	 * @return Pixel coordinates of a drawn field's top left corner position.
	 */
	public static Position fieldToPixTopLeft(int iPanelWidth, int iPanelHeight, int iBoardWidth, int iBoardHeight, int iXField, int iYField)
		{
		Position oPosition = new Position(2);
		oPosition.setX(-1);
		oPosition.setY(-1);
		//calculation of the margin width in pixels
		int iHorizontalMarginsFx = (int)((float)iPanelWidth * (float)((float)DrawingCoordinatesOnBoard.iMargins / 100)); 
		int iVerticalMarginsFx = (int)((float)iPanelHeight * (float)((float)DrawingCoordinatesOnBoard.iMargins / 100));
		//System.out.println(""+iHorizontalMarginsFx+","+iVerticalMarginsFx);
		//calculating the width and height of the drawn board after subtracting the margins
		int iBoardWidthPx = iPanelWidth - (2 * iHorizontalMarginsFx);
		int iBoardHeightPx = iPanelHeight - (2 * iVerticalMarginsFx);
		//System.out.println(""+iBoardWidthPx+","+iBoardHeightPx);
		//calculation of the x and y coordinates for the upper left corner of the field
		float fX = ((float)(iBoardWidthPx - 1) / iBoardWidth) * iXField;
		float fY = ((float)(iBoardHeightPx - 1) / iBoardHeight) * iYField;
		//System.out.println(""+fX+","+fY);
		//correction of the coordinates of the margin field
		fX+= (float)iHorizontalMarginsFx;
		fY+= (float)iVerticalMarginsFx;
		//entering the coordinates to the position object and returning the object
		oPosition.setX((int)fX);
		oPosition.setY((int)fY);
		return oPosition;
		}
	/**
	 * This method is similar to the last one, except it returns the 
         * field's bottom right pixel coordinates.
	 * 
	 * @param iPanelWidth Panel width.
	 * @param iPanelHeight Panel height.
	 * @param iBoardWidth Board width.
	 * @param iBoardHeight Board height.
	 * @param iXField X coordinate of the converted field (counted from 0).
	 * @param iYField Y coordinate of the converted field (counted from 0).
	 * @return Pixel coordinates of a drawn field's bottom right corner position.
	 */
	public static Position fieldToPixBottomRight(int iPanelWidth, int iPanelHeight, int iBoardWidth, int iBoardHeight, int iXField, int iYField)
		{
		return DrawingCoordinatesOnBoard.fieldToPixTopLeft(iPanelWidth, iPanelHeight, iBoardWidth, iBoardHeight, iXField + 1, iYField + 1);
		}
	}
