package pl.vgtworld.games.statki.components;

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
public class JPanelZaznaczanieStatkow
	extends JPanel
	{
	/**
	 * Obiekt ustawien rozgrywki.
	 */
	private Settings oUstawienia;
	/**
	 * Board, na ktorej gracz zaznacza statki.
	 */
	private Board oPlansza;
	/**
	 * Kontener statkow tworzony dla gracza po zakonczeniu rozmieszczania statkow.
	 */
	private ShipIterator oStatki;
	/**
	 * Referencja do glownego okna gry.
	 */
	private JFrameGameWindowSettings oOkno;
	/**
	 * Komponent, na ktorym wyswietlana jest plansza.
	 */
	private JComponentPlansza oComponentPlansza;
	/**
	 * Panel wyswietlajacy informacje na temat ilosci statkow poszczegolnych rozmiarow,
	 * ktore nalezy umiescic na planszy.
	 */
	private JPanelZaznaczanieStatkowLista oListaStatkowInfo;
	/**
	 * Panel zawierajacy informacje o wymaganych statkach oraz przyciski akcji.
	 */
	private JPanel oPanelPrawy;
	/**
	 * Obiekt obslugi akcji klikniecia myszki na planszy.
	 */
	private ZaznaczanieStatkowMouseListener oMouseListener;
	/**
	 * Klasa prywatna zawierajaca obsluge akcji wcisniecia przycisku zatwierdzajacego rozmieszczenie statkow na planszy.
	 */
	private class ActionZatwierdzStatki
		extends AbstractAction
		{
		public ActionZatwierdzStatki()
			{
			putValue(Action.NAME, JFrameGameWindowSettings.LANG.getProperty("action.shipPlacement.accept"));
			putValue(Action.SHORT_DESCRIPTION, JFrameGameWindowSettings.LANG.getProperty("action.shipPlacement.accept.desc"));
			}
		@Override public void actionPerformed(ActionEvent event)
			{
			ShipGenerator oGenerator = new ShipGenerator(oPlansza);
			oStatki = oGenerator.generujStatki();
			boolean bOK = true;
			//sprawdzenie, kolejnych warunkow rozmieszczenia statkow
			if (oStatki.getIloscStatkow() != oUstawienia.getNumbeOfShips())
				bOK = false;
			for (int i = oStatki.getMaxRozmiarStatku(); i >= 1; --i)
				if (oStatki.getIloscStatkow(i) != oUstawienia.getIloscStatkow(i))
					bOK = false;
			if (oStatki.weryfikujRozmieszczenie(oUstawienia.getStraightLines()) == false)
				bOK = false;
			//commit
			if (bOK == false)
				{
				JOptionPane.showMessageDialog(JPanelZaznaczanieStatkow.this, JFrameGameWindowSettings.LANG.getProperty("errorMsg.shipPlacement.invalidShipPlacement"));
				oStatki = null;
				}
			else
				oOkno.rozpocznijRozgrywke();
			}
		}
	/**
	 * Klasa prywatna zawierajaca obsluge akcji wcisniecia przycisku usuwajacego wszystkie statki z planszy.
	 */
	private class ActionWyczysc
		extends AbstractAction
		{
		public ActionWyczysc()
			{
			putValue(Action.NAME, JFrameGameWindowSettings.LANG.getProperty("action.shipPlacement.clear"));
			putValue(Action.SHORT_DESCRIPTION, JFrameGameWindowSettings.LANG.getProperty("action.shipPlacement.clear.desc"));
			}
		@Override public void actionPerformed(ActionEvent event)
			{
			try
				{
				for (int i = 0; i < oPlansza.getWidth(); ++i)
					for (int j = 0; j < oPlansza.getHeight(); ++j)
						if (oPlansza.getPole(i, j) == FieldTypeBoard.SHIP_BOARD)
							oPlansza.setPole(i, j, FieldTypeBoard.BOARD_FIELD_EMPTY);
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
	private class ActionRozmiescLosowoStatkiGracza
		extends AbstractAction
		{
		public ActionRozmiescLosowoStatkiGracza()
			{
			putValue(Action.NAME, JFrameGameWindowSettings.LANG.getProperty("action.shipPlacement.random"));
			putValue(Action.SHORT_DESCRIPTION, JFrameGameWindowSettings.LANG.getProperty("action.shipPlacement.random.desc"));
			}
		@Override public void actionPerformed(ActionEvent event)
			{
			try
				{
				for (int i = 0; i < oPlansza.getWidth(); ++i)
					for (int j = 0; j < oPlansza.getHeight(); ++j)
						if (oPlansza.getPole(i, j) == FieldTypeBoard.SHIP_BOARD)
							oPlansza.setPole(i, j, FieldTypeBoard.BOARD_FIELD_EMPTY);
				ShipIterator oKontener = new ShipIterator(oPlansza);
				int[] aStatki = oUstawienia.getShips();
				for (int iRozmiar: aStatki)
					oKontener.dodajStatek(iRozmiar);
				ShipPositioner oPozycjoner = new ShipPositioner();
				if (oPozycjoner.rozmiescStatki(oKontener, oUstawienia.getStraightLines()) == false)
					JOptionPane.showMessageDialog(JPanelZaznaczanieStatkow.this, JFrameGameWindowSettings.LANG.getProperty("errorMsg.shipPlacement.randomShipPlacementFail"));
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
	private class ZaznaczanieStatkowMouseListener
		extends MouseAdapter
		{
		public ZaznaczanieStatkowMouseListener()
			{
			}
		@Override public void mousePressed(MouseEvent event)
			{
			int iPlanszaSzerokosc = oPlansza.getWidth();
			int iPlanszaWysokosc = oPlansza.getHeight();
			int iKomponentSzerokosc = oComponentPlansza.getWidth();
			int iKomponentWysokosc = oComponentPlansza.getHeight();
			int iClickX = event.getX();
			int iClickY = event.getY();
			Position oKliknietePole;
			oKliknietePole = DrawingCoordinatesOnBoard.pixToField(iKomponentSzerokosc, iKomponentWysokosc, iPlanszaSzerokosc, iPlanszaWysokosc, iClickX, iClickY);
			try
				{
				if (oKliknietePole.getX() >= 0 && oKliknietePole.getX() < iPlanszaSzerokosc
					&& oKliknietePole.getY() >= 0 && oKliknietePole.getY() < iPlanszaWysokosc
					)
					{
					if (oPlansza.getPole(oKliknietePole.getX(), oKliknietePole.getY()) == FieldTypeBoard.BOARD_FIELD_EMPTY)
						{
						oPlansza.setPole(oKliknietePole.getX(), oKliknietePole.getY(), FieldTypeBoard.SHIP_BOARD);
//						oComponentPlansza.aktywujWyroznienie(oKliknietePole.getX(), oKliknietePole.getY());
						}
					else if (oPlansza.getPole(oKliknietePole.getX(), oKliknietePole.getY()) == FieldTypeBoard.SHIP_BOARD)
						{
						oPlansza.setPole(oKliknietePole.getX(), oKliknietePole.getY(), FieldTypeBoard.BOARD_FIELD_EMPTY);
//						oComponentPlansza.aktywujWyroznienie(oKliknietePole.getX(), oKliknietePole.getY());
						}
					Position oWspTopLeft;
					Position oWspBottomRight;
					oWspTopLeft = DrawingCoordinatesOnBoard.fieldToPixTopLeft(iKomponentSzerokosc, iKomponentWysokosc, iPlanszaSzerokosc, iPlanszaWysokosc, oKliknietePole.getX(), oKliknietePole.getY());
					oWspBottomRight = DrawingCoordinatesOnBoard.fieldToPixBottomRight(iKomponentSzerokosc, iKomponentWysokosc, iPlanszaSzerokosc, iPlanszaWysokosc, oKliknietePole.getX(), oKliknietePole.getY());
					oComponentPlansza.repaint(oWspTopLeft.getX(), oWspTopLeft.getY(), oWspBottomRight.getX()-oWspTopLeft.getX(), oWspBottomRight.getY() - oWspTopLeft.getY());
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
	 * @param oUstawienia Glowne ustawienia gry.
	 * @param oOkno Glowne okno gry.
	 */
	public JPanelZaznaczanieStatkow(Settings oUstawienia, JFrameGameWindowSettings oOkno)
		{
		setLayout(new GridLayout(1, 2));
		this.oUstawienia = oUstawienia;
		this.oOkno = oOkno;
		oPlansza = new Board(oUstawienia.getBoardWidth(), oUstawienia.getBoardHeight());
		oStatki = null;
		oComponentPlansza = new JComponentPlansza(oPlansza);
		oMouseListener = new ZaznaczanieStatkowMouseListener();
		
		addMouseListener(oMouseListener);
		
		//lewa polowka
		oComponentPlansza = new JComponentPlansza(oPlansza);
		add(oComponentPlansza);
		
		//prawa polowka
		oPanelPrawy = new JPanel();
		oPanelPrawy.setLayout(new BorderLayout());
		oListaStatkowInfo = new JPanelZaznaczanieStatkowLista(oUstawienia);
		JScrollPane oListaStatkowInfoScroll = new JScrollPane(oListaStatkowInfo);
		oListaStatkowInfoScroll.setBorder(null);
		
		JPanel oButtonContainer = new JPanel();
		oButtonContainer.setLayout(new GridLayout(1,3));
		JButton oButtonZatwierdz = new JButton(new ActionZatwierdzStatki());
		JButton oButtonWyczysc = new JButton(new ActionWyczysc());
		JButton oButtonLosuj = new JButton(new ActionRozmiescLosowoStatkiGracza());
		oButtonContainer.add(oButtonZatwierdz);
		oButtonContainer.add(oButtonLosuj);
		oButtonContainer.add(oButtonWyczysc);
		oPanelPrawy.add(oButtonContainer, BorderLayout.PAGE_END);
		oPanelPrawy.add(oListaStatkowInfoScroll, BorderLayout.CENTER);
		add(oPanelPrawy);
		}
	/**
	 * Metoda zwraca plansze, na ktorej zaznaczane sa statki.
	 * 
	 * @return Board z zaznaczonymi statkami.
	 */
	public Board getPlansza()
		{
		return oPlansza;
		}
	/**
	 * Metoda zwraca obiekt kontenera statkow stworznych przez gracza.<br />
	 * 
	 * Jesli statki nie zostaly rozmieszczone, lub zostaly rozmieszczone nieprawidlowo, metoda zwroci pusta referencje.
	 * 
	 * @return Kontener statkow gracza.
	 */
	public ShipIterator getStatki()
		{
		return oStatki;
		}
	/**
	 * Metoda usuwa wszystkie statki umieszczone na planszy.
	 */
	public void wyczyscPlansze()
		{
		oPlansza.clean();
		}
	/**
	 * Metoda zmienia rozmiar planszy na podstawie aktualnego stanu obiektu ustawien.<br />
	 * 
	 * Wywolywana po zmianie ustawien rozgrywki.
	 */
	public void resetujPlansze()
		{
		//oPlansza = new Board(oUstawienia.getBoardWidth(), oUstawienia.getBoardHeight());
		try
			{
			if (oUstawienia.getBoardWidth() != oPlansza.getWidth() || oUstawienia.getBoardHeight() != oPlansza.getHeight())
				oPlansza.zmienRozmiar(oUstawienia.getBoardWidth(), oUstawienia.getBoardHeight());
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	/**
	 * Metoda aktualizuje panel z informacjami na temat ilosci wymaganych do umieszczenia statkow na planszy. 
	 */
	public void resetujOpis()
		{
		oListaStatkowInfo.odswiez();
		}
	}
