package pl.vgtworld.games.ship.components;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import pl.vgtworld.exceptions.ParameterException;
import pl.vgtworld.exceptions.DeveloperException;
import pl.vgtworld.games.ship.Board;
import pl.vgtworld.games.ship.FieldTypeBoard;
import pl.vgtworld.games.ship.DrawingCoordinatesOnBoard;
import pl.vgtworld.games.ship.ShipIterator;
import pl.vgtworld.games.ship.ShipGenerator;
import pl.vgtworld.games.ship.ShipPositioner;
import pl.vgtworld.games.ship.Settings;
import pl.vgtworld.tools.Position;

/**
 * The panel used to support the placement of ships on board by the player.
 * 
 * @author VGT
 * @version 1.0
 */
public class JPanelMarkingShips
	extends JPanel
	{
	/**
	 * Game setting object.
	 */
	private Settings oSettings;
	/**
	 * The board where the player marks the ships.
	 */
	private Board oBoard;
	/**
	 * A ship container is created for the player after completing the ship placement.
	 */
	private ShipIterator oShips;
	/**
	 * Reference to the main game window.
	 */
	private JFrameGameWindowSettings oWindow;
	/**
	 * Component on which the board is displayed.
	 */
	private JComponentBoard oBoardComponent;
	/**
	 * Panel displaying information on the number of ships of particular sizes,
	 * which should be placed on the board.
	 */
	private JPanelMarkingShipsOnList oShipListInfo;
	/**
	 * A panel with information about the required ships and action buttons.
	 */
	private JPanel oPanelRight;
	/**
	 * Object handling the action of clicking the mouse on the board.
	 */
	private SelectingShipsMouseListener oMouseListener;
	/**
	 * Private class that supports the action of pressing the button confirming the placement of ships on board.
	 */
	private class ActionAproveShips
		extends AbstractAction
		{
		public ActionAproveShips()
			{
			putValue(Action.NAME, JFrameGameWindowSettings.LANG.getProperty("action.shipPlacement.accept"));
			putValue(Action.SHORT_DESCRIPTION, JFrameGameWindowSettings.LANG.getProperty("action.shipPlacement.accept.desc"));
			}
		@Override public void actionPerformed(ActionEvent event)
			{
			ShipGenerator oGenerator = new ShipGenerator(oBoard);
			oShips = oGenerator.generateShips();
			boolean bOK = true;
			// checking the successive conditions for the arrangement of ships
			if (oShips.getNumberOfShips() != oSettings.getNumbeOfShips())
				bOK = false;
			for (int i = oShips.getMaxShipSize(); i >= 1; --i)
				if (oShips.getNumberOfShips(i) != oSettings.getNumberOfShips(i))
					bOK = false;
			if (!oShips.verifyApplication(oSettings.getStraightLines()))
				bOK = false;
			//commit
			if (!bOK)
				{
				JOptionPane.showMessageDialog(JPanelMarkingShips.this, JFrameGameWindowSettings.LANG.getProperty("errorMsg.shipPlacement.invalidShipPlacement"));
				oShips = null;
				}
			else
				oWindow.startGameplay();
			}
		}
	/**
	 * Private class that handles the action of pressing the button that removes all ships from the board.
	 */
	private class ActionClear
		extends AbstractAction
		{
		public ActionClear()
			{
			putValue(Action.NAME, JFrameGameWindowSettings.LANG.getProperty("action.shipPlacement.clear"));
			putValue(Action.SHORT_DESCRIPTION, JFrameGameWindowSettings.LANG.getProperty("action.shipPlacement.clear.desc"));
			}
		@Override public void actionPerformed(ActionEvent event)
			{
			try
				{
				for (int i = 0; i < oBoard.getWidth(); ++i)
					for (int j = 0; j < oBoard.getHeight(); ++j)
						if (oBoard.getField(i, j) == FieldTypeBoard.SHIP_BOARD)
							oBoard.setField(i, j, FieldTypeBoard.BOARD_FIELD_EMPTY);
				repaint();
				}
			catch (ParameterException e)
				{
				throw new DeveloperException(e);
				}
			}
		}
	/**
	 * A private class that supports the pressing of a button that places the player's ships randomly on the board.
	 */
	private class ActionRandomlyPlacePlayersShips
		extends AbstractAction
		{
		public ActionRandomlyPlacePlayersShips()
			{
			putValue(Action.NAME, JFrameGameWindowSettings.LANG.getProperty("action.shipPlacement.random"));
			putValue(Action.SHORT_DESCRIPTION, JFrameGameWindowSettings.LANG.getProperty("action.shipPlacement.random.desc"));
			}
		@Override public void actionPerformed(ActionEvent event)
			{
			try
				{
				for (int i = 0; i < oBoard.getWidth(); ++i)
					for (int j = 0; j < oBoard.getHeight(); ++j)
						if (oBoard.getField(i, j) == FieldTypeBoard.SHIP_BOARD)
							oBoard.setField(i, j, FieldTypeBoard.BOARD_FIELD_EMPTY);
				ShipIterator oContainer = new ShipIterator(oBoard);
				int[] aShips = oSettings.getShips();
				for (int iSize: aShips)
					oContainer.addAShip(iSize);
				ShipPositioner oPositioner = new ShipPositioner();
				if (!oPositioner.shipSpaces(oContainer, oSettings.getStraightLines()))
					JOptionPane.showMessageDialog(JPanelMarkingShips.this, JFrameGameWindowSettings.LANG.getProperty("errorMsg.shipPlacement.randomShipPlacementFail"));
				}
			catch (ParameterException e)
				{
				throw new DeveloperException(e);
				}
			repaint();
			}
		}
	/**
	 * A private class that includes handling of clicking a player on a board (marking / unchecking half of the ships).
	 */
	private class SelectingShipsMouseListener
		extends MouseAdapter
		{
		public SelectingShipsMouseListener()
			{
			}
		@Override public void mousePressed(MouseEvent event)
			{
			int iBoardWidth = oBoard.getWidth();
			int iBoardHeight = oBoard.getHeight();
			int iComponentWidth = oBoardComponent.getWidth();
			int iComponentHeight = oBoardComponent.getHeight();
			int iClickX = event.getX();
			int iClickY = event.getY();
			Position oClickedField;
			oClickedField = DrawingCoordinatesOnBoard.pixToField(iComponentWidth, iComponentHeight, iBoardWidth, iBoardHeight, iClickX, iClickY);
			try
				{
				if (oClickedField.getX() >= 0 && oClickedField.getX() < iBoardWidth
					&& oClickedField.getY() >= 0 && oClickedField.getY() < iBoardHeight
					)
					{
					if (oBoard.getField(oClickedField.getX(), oClickedField.getY()) == FieldTypeBoard.BOARD_FIELD_EMPTY)
						{
						oBoard.setField(oClickedField.getX(), oClickedField.getY(), FieldTypeBoard.SHIP_BOARD);
//						oBoardComponent.activateHighlight(oClickedField.getX(), oClickedField.getY());
						}
					else if (oBoard.getField(oClickedField.getX(), oClickedField.getY()) == FieldTypeBoard.SHIP_BOARD)
						{
						oBoard.setField(oClickedField.getX(), oClickedField.getY(), FieldTypeBoard.BOARD_FIELD_EMPTY);
//						oBoardComponent.activateHighlight(oClickedField.getX(), oClickedField.getY());
						}
					Position oWspTopLeft;
					Position oWspBottomRight;
					oWspTopLeft = DrawingCoordinatesOnBoard.fieldToPixTopLeft(iComponentWidth, iComponentHeight, iBoardWidth, iBoardHeight, oClickedField.getX(), oClickedField.getY());
					oWspBottomRight = DrawingCoordinatesOnBoard.fieldToPixBottomRight(iComponentWidth, iComponentHeight, iBoardWidth, iBoardHeight, oClickedField.getX(), oClickedField.getY());
					oBoardComponent.repaint(oWspTopLeft.getX(), oWspTopLeft.getY(), oWspBottomRight.getX()-oWspTopLeft.getX(), oWspBottomRight.getY() - oWspTopLeft.getY());
					}
				}
			catch (ParameterException e)
				{
				throw new DeveloperException(e);
				}
			}
		}
	/**
	 * Constructor.
	 * 
	 * @param oSettings Main game settings.
	 * @param oWindow Game Window Glowne.
	 */
	public JPanelMarkingShips(Settings oSettings, JFrameGameWindowSettings oWindow)
		{
		setLayout(new GridLayout(1, 2));
		this.oSettings = oSettings;
		this.oWindow = oWindow;
		oBoard = new Board(oSettings.getBoardWidth(), oSettings.getBoardHeight());
		oShips = null;
		oBoardComponent = new JComponentBoard(oBoard);
		oMouseListener = new SelectingShipsMouseListener();
		
		addMouseListener(oMouseListener);
		
		// left half
		oBoardComponent = new JComponentBoard(oBoard);
		add(oBoardComponent);
		
		// right half
		oPanelRight = new JPanel();
		oPanelRight.setLayout(new BorderLayout());
		oShipListInfo = new JPanelMarkingShipsOnList(oSettings);
		JScrollPane oShipListInfoScroll = new JScrollPane(oShipListInfo);
		oShipListInfoScroll.setBorder(null);
		
		JPanel oButtonContainer = new JPanel();
		oButtonContainer.setLayout(new GridLayout(1,3));
		JButton oButtonZatwierdz = new JButton(new ActionAproveShips());
		JButton oButtonClear = new JButton(new ActionClear());
		JButton oButtonLosuj = new JButton(new ActionRandomlyPlacePlayersShips());
		oButtonContainer.add(oButtonZatwierdz);
		oButtonContainer.add(oButtonLosuj);
		oButtonContainer.add(oButtonClear);
		oPanelRight.add(oButtonContainer, BorderLayout.PAGE_END);
		oPanelRight.add(oShipListInfoScroll, BorderLayout.CENTER);
		add(oPanelRight);
		}
	/**
	 * The method returns the board on which the ships are marked.
	 * 
	 * @return Board with marked ships.
	 */
	public Board getBoard()
		{
		return oBoard;
		}
	/**
	 * The method returns a container object for player-created ships. <br />
	 *
 	 * If the ships have not been deployed, or have been deployed incorrectly, the method will return a blank reference.
	 *
	 * @return The player's ship container.
	 */
	public ShipIterator getShips()
		{
		return oShips;
		}
	/**
	 * The method removes all ships placed on board.
	 */
	public void ClearBoard()
		{
		oBoard.clean();
		}
	/**
	 * The method changes the Size board based on the current state of the setting object. <br />
	 *
	 * Called when gameplay settings have been changed.
	 */
	public void resetBoard()
		{
		//oBoard = new Board(oSettings.getBoardWidth(), oSettings.getBoardHeight());
		try
			{
			if (oSettings.getBoardWidth() != oBoard.getWidth() || oSettings.getBoardHeight() != oBoard.getHeight())
				oBoard.zmienSize(oSettings.getBoardWidth(), oSettings.getBoardHeight());
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	/**
	 * The method updates the panel with information about the quantities required to place ships on board.
	 */
	public void resetDescription()
		{
		oShipListInfo.refresh();
		}
	}
