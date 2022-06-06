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
 * Panel wykorzystywany do obslugi rozmieszczenia statkow na planszy przez gracza.
 * 
 * @author VGT
 * @version 1.0
 */
public class JPanelMarkingShips
	extends JPanel
	{
	/**
	 * Obiekt ustawien rozgrywki.
	 */
	private Settings oSettings;
	/**
	 * Board, na ktorej gracz zaznacza statki.
	 */
	private Board oBoard;
	/**
	 * Kontener statkow tworzony dla gracza po zakonczeniu rozmieszczania statkow.
	 */
	private ShipIterator oShips;
	/**
	 * Referencja do glownego okna gry.
	 */
	private JFrameGameWindowSettings oWindow;
	/**
	 * Komponent, na ktorym wyswietlana jest plansza.
	 */
	private JComponentBoard oBoardComponent;
	/**
	 * Panel wyswietlajacy informacje na temat ilosci statkow poszczegolnych Sizeow,
	 * ktore nalezy umiescic na planszy.
	 */
	private JPanelMarkingShipsOnList oShipListInfo;
	/**
	 * Panel zawierajacy informacje o wymaganych statkach oraz przyciski akcji.
	 */
	private JPanel oPanelRight;
	/**
	 * Obiekt obslugi akcji klikniecia myszki na planszy.
	 */
	private SelectingShipsMouseListener oMouseListener;
	/**
	 * Klasa prywatna zawierajaca obsluge akcji wcisniecia przycisku zatwierdzajacego rozmieszczenie statkow na planszy.
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
			//sprawdzenie, kolejnych warunkow rozmieszczenia statkow
			if (oShips.getNumberOfShips() != oSettings.getNumbeOfShips())
				bOK = false;
			for (int i = oShips.getMaxShipSize(); i >= 1; --i)
				if (oShips.getNumberOfShips(i) != oSettings.getNumberOfShips(i))
					bOK = false;
			if (oShips.verifyApplication(oSettings.getStraightLines()) == false)
				bOK = false;
			//commit
			if (bOK == false)
				{
				JOptionPane.showMessageDialog(JPanelMarkingShips.this, JFrameGameWindowSettings.LANG.getProperty("errorMsg.shipPlacement.invalidShipPlacement"));
				oShips = null;
				}
			else
				oWindow.startGameplay();
			}
		}
	/**
	 * Klasa prywatna zawierajaca obsluge akcji wcisniecia przycisku usuwajacego wszystkie statki z planszy.
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
	 * Klasa prywatna zawierajaca obsluge wcisniecia przycisku rozmieszczajacego statki gracza losowo na planszy.
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
				if (oPositioner.shipSpaces(oContainer, oSettings.getStraightLines()) == false)
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
	 * Klasa prywatna zawierajaca obsluge klikniecia gracza na planszy (zaznaczanie/odznaczanie pol statkow).
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
	 * Konstruktor.
	 * 
	 * @param oSettings Glowne ustawienia gry.
	 * @param oWindow Glowne Window gry.
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
		
		//lewa polowka
		oBoardComponent = new JComponentBoard(oBoard);
		add(oBoardComponent);
		
		//prawa polowka
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
	 * Metoda zwraca plansze, na ktorej zaznaczane sa statki.
	 * 
	 * @return Board z zaznaczonymi statkami.
	 */
	public Board getBoard()
		{
		return oBoard;
		}
	/**
	 * Metoda zwraca obiekt kontenera statkow stworznych przez gracza.<br />
	 * 
	 * Jesli statki nie zostaly rozmieszczone, lub zostaly rozmieszczone nieprawidlowo, metoda zwroci pusta referencje.
	 * 
	 * @return Kontener statkow gracza.
	 */
	public ShipIterator getShips()
		{
		return oShips;
		}
	/**
	 * Metoda usuwa wszystkie statki umieszczone na planszy.
	 */
	public void ClearBoard()
		{
		oBoard.clean();
		}
	/**
	 * Metoda zmienia Size planszy na podstawie aktualnego stanu obiektu ustawien.<br />
	 * 
	 * Wywolywana po zmianie ustawien rozgrywki.
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
	 * Metoda aktualizuje panel z informacjami na temat ilosci wymaganych do umieszczenia statkow na planszy. 
	 */
	public void resetDescription()
		{
		oShipListInfo.refresh();
		}
	}
