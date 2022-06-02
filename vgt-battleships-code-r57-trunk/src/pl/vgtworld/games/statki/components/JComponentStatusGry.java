package pl.vgtworld.games.statki.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import pl.vgtworld.games.ship.ShipIterator;
import pl.vgtworld.games.ship.GameStatus;

/**
 * Komponent wyswietlajacy aktualne statystyki rozgrywki pod planszami.
 * 
 * @author VGT
 * @version 1.0
 */
public class JComponentStatusGry
	extends JComponent
	{
	private static final int ETYKIETY_FONT_WIELKOSC = 16;
	private static final int WARTOSCI_FONT_WIELKOSC = 14;
	private static final Color KOLOR_INFO = Color.WHITE;
	private static final Color KOLOR_NEUTRALNY = Color.white;
	private static final Color KOLOR_POZYTYWNY = Color.GREEN;
	private static final Color KOLOR_NEGATYWNY = Color.RED;
	private GameStatus oStatusGry;
	private ShipIterator oShipsGracz;
	private ShipIterator oShipsKomputer;
	private JComponentStatusGryStatki oListaStatkowGracz;
	private JComponentStatusGryStatki oListaStatkowKomputer;
	private JLabel oPunktyGracz;
	private JLabel oPunktyKomputer;
	private Image oImgTlo;
	/**
	 * Konstruktor.
	 * 
	 * @param oStatusGry Obiekt zawierajacy informacje na temat aktualnego statusu gry.
	 */
	public JComponentStatusGry(GameStatus oStatusGry)
		{
		super();
		this.oStatusGry = oStatusGry;
		setPreferredSize(new Dimension(100, 100));
		setLayout(new BorderLayout());
		oShipsGracz = null;
		oShipsKomputer = null;
		Font oFontEtykiety = new Font("Arial", Font.BOLD, ETYKIETY_FONT_WIELKOSC);
		Font oFontWartosci = new Font("Arial", Font.BOLD, WARTOSCI_FONT_WIELKOSC);
		//tlo img
		URL oImgUrl = getClass().getResource("/pl/vgtworld/games/statki/img/game-status-bg.png");
		if (oImgUrl != null)
			{
			try
				{
				oImgTlo = ImageIO.read(oImgUrl);
				}
			catch (IOException e)
				{
				oImgTlo = null;
				}
			}
		else
			oImgTlo = null;
		//etykiety
		oPunktyGracz = new JLabel("" + oStatusGry.getPlayerPoints(), JLabel.CENTER);
		oPunktyKomputer = new JLabel("" + oStatusGry.getPlayerPoints(), JLabel.CENTER);
		JLabel oEtykietaGracz = new JLabel(JFrameGameWindowSettings.LANG.getProperty("gameStatus.Player"), JLabel.CENTER);
		JLabel oEtykietaKomputer = new JLabel(JFrameGameWindowSettings.LANG.getProperty("gameStatus.AI"), JLabel.CENTER);
		JLabel oEtykietaPunktacja = new JLabel(JFrameGameWindowSettings.LANG.getProperty("gameStatus.points"), JLabel.CENTER);
		JLabel oEtykietaFlota = new JLabel(JFrameGameWindowSettings.LANG.getProperty("gameStatus.fleet"), JLabel.LEFT);
		//kolory
		oEtykietaGracz.setForeground(KOLOR_INFO);
		oEtykietaKomputer.setForeground(KOLOR_INFO);
		oEtykietaPunktacja.setForeground(KOLOR_INFO);
		oEtykietaFlota.setForeground(KOLOR_INFO);
		if (oStatusGry.getPlayerPoints() == oStatusGry.getComputerPoints())
			{
			oPunktyGracz.setForeground(KOLOR_NEUTRALNY);
			oPunktyKomputer.setForeground(KOLOR_NEUTRALNY);
			}
                else if (oStatusGry.getPlayerPoints() > oStatusGry.getComputerPoints())
			{
			oPunktyGracz.setForeground(KOLOR_POZYTYWNY);
			oPunktyKomputer.setForeground(KOLOR_NEGATYWNY);
			}
		else
			{
			oPunktyGracz.setForeground(KOLOR_NEGATYWNY);
			oPunktyKomputer.setForeground(KOLOR_POZYTYWNY);
			}
		//font
		oEtykietaGracz.setFont(oFontEtykiety);
		oEtykietaKomputer.setFont(oFontEtykiety);
		//oEtykietaPunktacja.setFont(oFontEtykiety);
		oEtykietaFlota.setFont(oFontEtykiety);
		oPunktyGracz.setFont(oFontWartosci);
		oPunktyKomputer.setFont(oFontWartosci);
		
		JPanel oPanelPunkty = new JPanel();
		oPanelPunkty.setOpaque(false);
		oPanelPunkty.setLayout(new GridLayout(3, 2));
		oPanelPunkty.add(new JLabel());
		//oPanelPunkty.add(oEtykietaPunktacja);
		oPanelPunkty.add(oEtykietaGracz);
		//oPanelPunkty.add(oPunktyGracz);
		oPanelPunkty.add(oEtykietaKomputer);
		//oPanelPunkty.add(oPunktyKomputer);
		
		try
			{
			oListaStatkowGracz = new JComponentStatusGryStatki(oShipsGracz);
			oListaStatkowKomputer = new JComponentStatusGryStatki(oShipsKomputer);
	
			JPanel oPanelStatki = new JPanel();
			oPanelStatki.setOpaque(false);
			oPanelStatki.setLayout(new GridLayout(3, 1));
			oPanelStatki.add(oEtykietaFlota);
			oPanelStatki.add(oListaStatkowGracz);
			oPanelStatki.add(oListaStatkowKomputer);
			
			add(oPanelStatki, BorderLayout.CENTER);
			}
		catch (IOException e)
			{}
		
		add(oPanelPunkty, BorderLayout.WEST);
		}
	/**
	 * Ustawienie obiektu kontenera statkow gracza.
	 * 
	 * @param oShips Kontener statkow gracza.
	 */
	public void setStatkiGracz(ShipIterator oShips)
		{
		oShipsGracz = oShips;
		oListaStatkowGracz.setStatki(oShips);
		}
	/**
	 * Ustawienie obiektu kontenera statkow komputera.
	 * 
	 * @param oShips Kontener statkow komputera.
	 */
	public void setStatkiKomputer(ShipIterator oShips)
		{
		oShipsKomputer = oShips;
		oListaStatkowKomputer.setStatki(oShips);
		}
	/**
	 * Odswiezenie danych w komponencie na podstawie obiektu statusu gry.
	 */
	public void aktualizujDane()
		{
		oPunktyGracz.setText("" + oStatusGry.getPlayerPoints());
		oPunktyKomputer.setText("" + oStatusGry.getComputerPoints());
		if (oStatusGry.getPlayerPoints() == oStatusGry.getComputerPoints())
			{
			oPunktyGracz.setForeground(KOLOR_NEUTRALNY);
			oPunktyKomputer.setForeground(KOLOR_NEUTRALNY);
			}
		else if (oStatusGry.getPlayerPoints() > oStatusGry.getComputerPoints())
			{
			oPunktyGracz.setForeground(KOLOR_POZYTYWNY);
			oPunktyKomputer.setForeground(KOLOR_NEGATYWNY);
			}
		else
			{
			oPunktyGracz.setForeground(KOLOR_NEGATYWNY);
			oPunktyKomputer.setForeground(KOLOR_POZYTYWNY);
			}
		repaint();
		}
	/**
	 * Przeciazona metoda rysujaca panel.
	 */
	@Override public void paintComponent(Graphics g)
		{
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, getWidth()-1, getHeight()-1);
		if (oImgTlo != null)
			{
			int iStartX = 0;
			while (iStartX < getWidth())
				{
				g.drawImage(oImgTlo, iStartX, 0, null);
				iStartX+= oImgTlo.getWidth(null);
				}
			}
		}
	}
