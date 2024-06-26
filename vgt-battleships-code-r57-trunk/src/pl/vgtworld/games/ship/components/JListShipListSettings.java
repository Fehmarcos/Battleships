package pl.vgtworld.games.ship.components;

import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import pl.vgtworld.exceptions.ParameterException;

/**
 * List object used in the settings window to present a list of defined ships.
 * 
 * @author VGT
 * @version 1.0
 */
public class JListShipListSettings
	extends JList
	{
	/**
	 * An array that holds size of ships.
	 */
	private ArrayList<Integer> oListInt;
	/**
	 * List model storing size of ships.
	 */
	private DefaultListModel oJListList;
	/**
	 * Constructor default.
	 */
	public JListShipListSettings()
		{
		oListInt = new ArrayList<Integer>();
		oJListList = new DefaultListModel();
		setModel(oJListList);
		}
	/**
	 * Returns the number of ships stored in the list.
	 * 
	 * @return Number of ships on the list.
	 */
	public int getNumberOfShips()
		{
		return oListInt.size();
		}
	/**
	 * Returns an int array containing the Size of all the ships stored in the list.
	 * 
	 * @return An array of the sizes of the ships stored in the list.
	 */
	public int[] getShipList()
		{
		int[] aLista = new int[ oListInt.size() ];
		for (int i = 0; i < oListInt.size(); ++i)
			aLista[i] = oListInt.get(i);
		return aLista;
		}
	/**
	 * Adds a ship with the given Size to the list.
	 * 
	 * @param iSize The size of the added vessel.
	 * @throws ParameterException Throws an exception if the supplied Size is less than from 1.
	 */
	public void addList(int iSize) throws ParameterException
		{
		if (iSize < 1)
			throw new ParameterException("iSize = " + iSize);
		oListInt.add(iSize);
		oJListList.addElement(JListShipListSettings.shipName(iSize));
		}
	/**
	 * Changes the size of a ship with the given index.
	 * 
	 * @param iIndex index on the list (counted from 0).
	 * @param iSize New Ship Size.
	 * @throws ParameterException Throws an exception if index is outside the range of the existing ship list, or Size is less from 1.
	 */
	public void changeList(int iIndex, int iSize) throws ParameterException
		{
		if (iIndex < 0 || iIndex >= oListInt.size())
			throw new ParameterException("iIndex = " + iIndex);
		if (iSize < 1)
			throw new ParameterException("iSize = " + iSize);
		oListInt.set(iIndex, iSize);
		oJListList.set(iIndex, JListShipListSettings.shipName(iSize));
		}
	/**
	 * Increases by 1 the Size of the ship of the given index.
	 * 
	 * @param iIndex Ship index in the list (counted from 0).
	 * @throws ParameterException Throws an exception if index is outside the range of the existing ship list.
	 */
	public void zoomList(int iIndex) throws ParameterException
		{
		if (iIndex < 0 || iIndex >= oListInt.size())
			throw new ParameterException("iIndex = " + iIndex);
		oListInt.set(iIndex, oListInt.get(iIndex) + 1);
		oJListList.set(iIndex, JListShipListSettings.shipName(oListInt.get(iIndex)));
		}
	/**
	 * Reduces by 1 the Size of the ship of the given index.
	 * 
	 * @param iIndex Ship index in the list (counted from 0).
	 * @throws ParameterException Throws an exception if index is outside the range of the existing ship list.
	 */
	public void zoomOutList(int iIndex) throws ParameterException
		{
		if (iIndex < 0 || iIndex >= oListInt.size())
			throw new ParameterException("iIndex = " + iIndex);
		if (oListInt.get(iIndex) > 1)
			{
			oListInt.set(iIndex, oListInt.get(iIndex) - 1);
			oJListList.set(iIndex, JListShipListSettings.shipName(oListInt.get(iIndex)));
			}
		}
        
        /**
         * Method used to change the name of a certain ship.
         * 
         * @param iIndex Ship index (counted from 0).
         */
        public void renameShip(int iIndex) throws ParameterException
                {
                    if (iIndex < 0 || iIndex >= oListInt.size())
			throw new ParameterException("iIndex = " + iIndex);
                    String newName = JOptionPane.showInputDialog(this, JFrameGameWindowSettings.LANG.getProperty("action.settings.shipList.rename.desc"));
                    if (newName == null){
                        oJListList.set(iIndex, JListShipListSettings.shipName(oListInt.get(iIndex)));
                    } else {
                        oJListList.set(iIndex, JListShipListSettings.newShipName(newName, oListInt.get(iIndex)));
                    }
                }
        
	/**
	 * Removes a ship with the given index from the list.
	 * 
	 * @param iIndex Ship index in the list (counted from 0).
	 * @throws ParameterException Throws an exception if index is outside the range of the existing ship list.
	 */
	public void deleteLista(int iIndex) throws ParameterException
		{
		if (iIndex < 0 || iIndex >= oListInt.size())
			throw new ParameterException("iIndex = " + iIndex);
		oListInt.remove(iIndex);
		oJListList.remove(iIndex);
		}
	/**
	 * Removes all ships from the list.
	 */
	public void clearList()
		{
		oListInt.clear();
		oJListList.clear();
		}
	/**
	 * Method generating the name of the ship to be displayed in the list based on the given Size.
	 * 
	 * @param iSize Ship size.
	 * @return Ship name.
	 */
	private static String shipName(int iSize) throws ParameterException
		{
		if (iSize < 1)
			throw new ParameterException("iSize = " + iSize);
		int iShipClass = iSize > 8 ? 8 : iSize;
		return JFrameGameWindowSettings.LANG.getProperty("shipName.size" + iShipClass) + " ( " + iSize + " )";
		}
        /**
         * Method to give a new name to a ship
         * 
         * @param namer
         * @return New ship name.
         */
        private static String newShipName(String namer, int iSize) throws ParameterException
                {
                return namer + " ( " + iSize + " )";
                }
	}
