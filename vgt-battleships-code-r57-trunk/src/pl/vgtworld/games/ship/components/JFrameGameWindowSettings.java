package pl.vgtworld.games.ship.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Properties;
import java.util.Scanner;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import pl.vgtworld.components.about.JDialogAbout;
import pl.vgtworld.exceptions.ParameterException;
import pl.vgtworld.exceptions.DeveloperException;
import pl.vgtworld.games.ship.Board;
import pl.vgtworld.games.ship.FieldTypeBoard;
import pl.vgtworld.games.ship.DrawingCoordinatesOnBoard;
import pl.vgtworld.games.ship.ShipIterator;
import pl.vgtworld.games.ship.ShipPositioner;
import pl.vgtworld.games.ship.GameStatus;
import pl.vgtworld.games.ship.Settings;
import pl.vgtworld.games.ship.ai.Ai;
import pl.vgtworld.games.ship.ai.AiFactory;
import pl.vgtworld.tools.Position;
//import sun.audio.AudioPlayer;
//import sun.audio.AudioStream;

/**
 * Main game Window .
 * 
 * @author VGT
 * @version 1.0
 */
public class JFrameGameWindowSettings
	extends JFrame
	{
	/**
	 * Variable storing the version of the program read from File.
	 */
	public static String sVersion;
	/**
	 * Constant that stores the minimum Width of the main game window.
	 */
	public static final int MIN_WIDTH = 640;
	/**
	 * Constant that stores the minimum Height of the main game window.
	 */
	public static final int MIN_Height = 480;
	/**
	 * Language file.
	 */
	public static Properties LANG;
	/**
	 * Game status object.
	 */
	private GameStatus oGameStatus;
	/**
	 * Game setting object.
	 */
	private Settings oSettings;
	/**
	 * Game settings window.
	 */
	private JDialogSettings oWindowSettings;
	/**
	 * Author information window.
	 */
	private JDialogAbout oWindowAbout;
	/**
	 * Main game window menu.
	 */
	private JMenuBar oMenu;
	/**
	 * Panel storing boards displayed during the game.
	 */
	private JPanel oBoardPanelContainer;
	/**
	 * The panel displayed after starting a new game. Performs the handling of the deployment of ships by the player.
	 */
	private JPanelMarkingShips oShipSelectionPanel;
	/**
	 * The start panel displayed after starting the program with buttons to start the game,
	 * change settings and end the game.
	 */
	private JPanel oButtonsPanel;
	/**
	 * Component displayed at the top of the window presenting messages about board events of individual players.
	 */
	private JComponentEvents oEventsComponent;
	/**
	 * Component displayed at the bottom of the window that presents information about the current game state.
	 */
	private JComponentGameStatus oGameStatusComponent;
	/**
	 * The player's ship container.
	 */
	private ShipIterator oPlayerShips;
	/**
	 * Computer vessel container.
	 */
	private ShipIterator oComputerShips;
	/**
	 * Artificial computer intelligence.
	 */
	private Ai oAi;
	/**
	 * Variable whether it is the player's turn to take the shot. <br />
	 *
	 * Used to prevent a shot from being taken when it is the computer's turn or the game is over
	 * victory of some player.
	 */
	private boolean bPlayerTurn;
	/**
	 * A private class containing action handlers to start a new game.
	 */
	private class ActionNewGame
		extends AbstractAction
		{
		public ActionNewGame()
			{
			putValue(Action.NAME, JFrameGameWindowSettings.LANG.getProperty("action.newGame"));
			putValue(Action.SHORT_DESCRIPTION, JFrameGameWindowSettings.LANG.getProperty("action.newGame.desc"));
			}
		public void actionPerformed(ActionEvent event)
			{
			oGameStatus.startNewGame();
			oButtonsPanel.setVisible(false);
			oBoardPanelContainer.setVisible(false);
			oShipSelectionPanel.setVisible(true);
			add(oShipSelectionPanel, BorderLayout.CENTER);
			oShipSelectionPanel.ClearBoard();
			repaint();
			}
		}
	/**
	 * Private class that handles the action of calling the game settings window.
	 */
	private class ActionSettings
		extends AbstractAction
		{
		public ActionSettings()
			{
			putValue(Action.NAME, JFrameGameWindowSettings.LANG.getProperty("action.settings"));
			putValue(Action.SHORT_DESCRIPTION, JFrameGameWindowSettings.LANG.getProperty("action.settings.desc"));
			}
		public void actionPerformed(ActionEvent oEvent)
			{
			oWindowSettings.reset();
			oWindowSettings.setVisible(true);
			}
		}
	/**
	 * Private class containing the program termination action handler.
	 */
	private class ActionFinish
		extends AbstractAction
		{
		public ActionFinish()
			{
			putValue(Action.NAME, JFrameGameWindowSettings.LANG.getProperty("action.exit"));
			putValue(Action.SHORT_DESCRIPTION, JFrameGameWindowSettings.LANG.getProperty("action.exit.desc"));
			}
		public void actionPerformed(ActionEvent event)
			{
			System.exit(0);
			}
		}
	/**
	 * Private class that handles the display of the author info window.
	 */
	private class ActionAbout
		extends AbstractAction
		{
		public ActionAbout()
			{
			putValue(Action.NAME, JFrameGameWindowSettings.LANG.getProperty("action.about"));
			putValue(Action.SHORT_DESCRIPTION, JFrameGameWindowSettings.LANG.getProperty("action.about.desc"));
			}
		public void actionPerformed(ActionEvent oEvent)
			{
			oWindowAbout.centerPosition();
			oWindowAbout.setVisible(true);
			}
		}
	/**
	 * A private class that supports the course of a single game cycle. <br />
	 * 
	 * - giving the shot by the player by clicking on the computer board performed by the mousePressed () method. <br />
	 * - sending a shot by the computer to the player's board called by the actionPerformed () method using a timer.
	 */
	private class GameplayMouseListener
		extends MouseAdapter
		implements ActionListener
		{
		private Board oBoard;
		private JComponentBoard oBoardComponent;
		private Timer oTimer;
		public GameplayMouseListener(Board oBoard, JComponentBoard oBoardComponent)
			{
			this.oBoard = oBoard;
			this.oBoardComponent = oBoardComponent;
			oTimer = new Timer(1000, this);
			oTimer.setRepeats(false);
			}
		public void setComponent(JComponentBoard oBoardComponent)
			{
			this.oBoardComponent = oBoardComponent;
			}
		public boolean isSetComponent()
			{
			if (oBoardComponent == null)
				return false;
			else
				return true;
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
				if (bPlayerTurn == true
					&& oClickedField.getX() >= 0 && oClickedField.getX() < iBoardWidth
					&& oClickedField.getY() >= 0 && oClickedField.getY() < iBoardHeight
					&& (oBoard.getField(oClickedField.getX(), oClickedField.getY()) == FieldTypeBoard.BOARD_FIELD_EMPTY
						|| oBoard.getField(oClickedField.getX(), oClickedField.getY()) == FieldTypeBoard.SHIP_BOARD
						)
					)
					{
					bPlayerTurn = false;
					int iQuantitySunkenBeforeShot = oComputerShips.getNumberOfUndamagedShips();
					
					//shot on a computer board
					boolean bHit;
					bHit = oComputerShips.shot(oClickedField.getX(), oClickedField.getY());
					JComponentBoard oBoardComponent = (JComponentBoard)oBoardPanelContainer.getComponent(1);
					oBoardComponent.activateHighlight(oClickedField);
					//support for checking if game over
					if (bHit == true && oComputerShips.getNumberOfShips() == oComputerShips.getNumberOfUndamagedShips())
						{
						oGameStatus.playerVictory();
						oGameStatusComponent.updateData();
						JOptionPane.showMessageDialog(JFrameGameWindowSettings.this, LANG.getProperty("message.win"));
						return;
						}
					else if (bHit == true)
						{
                                                    
                                                //COLOCAR AUDIO DE EXPLOSÃO
                                                
                                                InputStream in;
                                                
                                                    try {
                                                        
                                                        in = new FileInputStream(new File ("src/pl/vgtworld/games/shpi/components/explosion.mp3"));
//                                                        AudioStream ad = new AudioStream(in);
//                                                        AudioPlayer.player.start(ad);
                                                  
                                                    } catch (Exception e) {
                                                    }
                                                    
						oGameStatusComponent.updateData();
						if (iQuantitySunkenBeforeShot != oComputerShips.getNumberOfUndamagedShips())
							oEventsComponent.setRightMessage(LANG.getProperty("message.hit2"));
						else
							oEventsComponent.setRightMessage(LANG.getProperty("message.hit1"));
						}
					
					oTimer.start();
					}
				}
			catch (ParameterException e)
				{
				throw new DeveloperException(e);
				}
			}
		public void actionPerformed(ActionEvent oEvent)
			{
			int iQuantitySunkenBeforeShot = oPlayerShips.getNumberOfUndamagedShips();
			// shot on the player's board
			boolean bHit = oAi.shot(oPlayerShips);
			JComponentBoard oBoardComponent = (JComponentBoard)oBoardPanelContainer.getComponent(0);
			oBoardComponent.activateHighlight(oPlayerShips.getLastShot());
			// handle checking if game over
			if (bHit == true && oPlayerShips.getNumberOfShips() == oPlayerShips.getNumberOfUndamagedShips())
				{
				oGameStatus.computerVictory();
				oGameStatusComponent.updateData();
				int iComponentsQuantity = oBoardPanelContainer.getComponentCount();
				JComponentBoard oBoardComponent;
				for (int i = 0; i < iComponentsQuantity; ++i)
					{
					oBoardComponent = (JComponentBoard)oBoardPanelContainer.getComponent(i);
					if (oBoardComponent != null)
						oBoardComponent.setViewShips(true);
					}
				oBoardPanelContainer.repaint();
				JOptionPane.showMessageDialog(JFrameGameWindowSettings.this, LANG.getProperty("message.lose"));
				return;
				}
			else if (bHit == true)
				{
				oGameStatusComponent.updateData();
				if (iQuantitySunkenBeforeShot != oPlayerShips.getNumberOfUndamagedShips())
					oEventsComponent.setLeftMessage(LANG.getProperty("message.hit2"));
				else
					oEventsComponent.setLeftMessage(LANG.getProperty("message.hit1"));
				}
			
			bPlayerTurn = true;
			
			}
		}
	/**
	 * Constructor.
	 * 
	 * @param oGameStatus Object that stores information about the current status of the game.
	 * @param oSettings Object that stores game settings.
	 */
	public JFrameGameWindowSettings(GameStatus oGameStatus, Settings oSettings)
		{
		this(oGameStatus, oSettings, MIN_WIDTH, MIN_Height);
		}
	/**
	 * Overloaded constructor allowing to define the Size of the game window.
	 * 
	 * @param oGameStatus Object that stores information about the current status of the game.
	 * @param oSettings Object that stores game settings.
	 * @param iWidth Width Window games w pixel.
	 * @param iHeight Height okna games w pixel.
	 */
	public JFrameGameWindowSettings(GameStatus oGameStatus, Settings oSettings, int iWidth, int iHeight)
		{
		InputStream oFile = getClass().getResourceAsStream("/wersja.txt");
		if (oFile != null)
			{
			Scanner oVer = new Scanner(oFile);
			sVersion = oVer.nextLine();
			}
		else
			sVersion = null;
		
		Properties oDefaultLang = new Properties();
		try
			{
			InputStream oDefaultLangStream = getClass().getResourceAsStream("/pl/vgtworld/games/ship/lang/en_US.lang");
			if (oDefaultLangStream == null)
				{
				System.err.println("default language file not found");
				JOptionPane.showMessageDialog(null, "default language file not found", "Error", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
				}
			oDefaultLang.load(oDefaultLangStream);
			InputStream oLangStream = getClass().getResourceAsStream("/pl/vgtworld/games/ship/lang/" + Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry() + ".lang");
			LANG = new Properties(oDefaultLang);
			if (oLangStream != null)
				{
				BufferedReader oBuffereLangStream = new BufferedReader(new InputStreamReader(oLangStream, "UTF-8"));
				LANG.load(oBuffereLangStream);
				}
			}
		catch (IOException e)
			{
			System.err.println("error reading file");
			JOptionPane.showMessageDialog(null, "error reading file", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
			}

		//setMinimumSize(new Dimension(MIN_WIDTH, MIN_Height));
		
		setTitle(JFrameGameWindowSettings.LANG.getProperty("mainWindow.title"));
		//setSize(iWidth, iHeight);
                setExtendedState(JFrameGameWindowSettings.MAXIMIZED_BOTH);
                setUndecorated(true);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		this.oGameStatus = oGameStatus;
		this.oSettings = oSettings;
		oWindowSettings = new JDialogSettings(this, oSettings);
		oWindowSettings.setLocationRelativeTo(this);
		oWindowAbout = new JDialogAbout(this, JFrameGameWindowSettings.LANG.getProperty("mainWindow.title"));
		if (sVersion != null)
			{
			oWindowAbout.setVersion(sVersion);
			oWindowAbout.rebuild();
			}
		
		//panel with player boards
		oBoardPanelContainer = new JPanel();
		oBoardPanelContainer.setLayout(new GridLayout());
		if (oGameStatus.getGameLaunched() == true && oGameStatus.getShipsArranged() == true)
			add(oBoardPanelContainer, BorderLayout.CENTER);
		
		//panel with the board for selecting ships after the start of the game
		oShipSelectionPanel = new JPanelMarkingShips(oSettings, this);
		if (oGameStatus.getGameLaunched() == true && oGameStatus.getShipsArranged() == false)
			add(oShipSelectionPanel, BorderLayout.CENTER);
		
		//action objects
		ActionNewGame oActionNewGame = new ActionNewGame();
		ActionFinish oActionFinish = new ActionFinish();
		ActionSettings oActionSettings = new ActionSettings();
		
		//panel replacing the board before the game starts
		oButtonsPanel = new JPanel();
		oButtonsPanel.setBackground(Color.BLACK);
		//oButtonsPanel.setLayout(new GridLayout());
		JButton oButtonNewGame = new JButton(oActionNewGame);
		JButton oButtonSettings = new JButton(oActionSettings);
		JButton oButtonFinish = new JButton(oActionFinish);
		oButtonsPanel.add(oButtonNewGame);
		oButtonsPanel.add(oButtonSettings);
		oButtonsPanel.add(oButtonFinish);
		if (oGameStatus.getGameLaunched() == false)
			add(oButtonsPanel, BorderLayout.CENTER);
		
		//event draw area
		oEventsComponent = new JComponentEvents();
		add(oEventsComponent, BorderLayout.PAGE_START);
		
		//game status drawing area
		oGameStatusComponent = new JComponentGameStatus(this.oGameStatus);
		add(oGameStatusComponent, BorderLayout.PAGE_END);
		
		//menu
		oMenu = new JMenuBar();
		setJMenuBar(oMenu);
		
		//menu "ships"
		JMenu oShipsMenu = new JMenu(JFrameGameWindowSettings.LANG.getProperty("menu.game"));
		JMenuItem oShipsMenuNew = new JMenuItem(oActionNewGame);
		JMenuItem oShipsMenuClose = new JMenuItem(oActionFinish);
		oShipsMenu.add(oShipsMenuNew);
		oShipsMenu.add(oShipsMenuClose);
		oMenu.add(oShipsMenu);

		//menu "options"
		JMenu oOptionsMenu = new JMenu(JFrameGameWindowSettings.LANG.getProperty("menu.options"));
		JMenuItem oOptionsMenuConf = new JMenuItem(oActionSettings);
		oOptionsMenu.add(oOptionsMenuConf);
		oMenu.add(oOptionsMenu);
		
		//menu "help"
		JMenu oHelpMenu = new JMenu(JFrameGameWindowSettings.LANG.getProperty("menu.help"));
		JMenuItem oHelpMenuAbout = new JMenuItem(new ActionAbout());
		oHelpMenu.add(oHelpMenuAbout);
		oMenu.add(oHelpMenu);
		
		//map enter
		InputMap oIMap = oButtonsPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		oIMap.put(KeyStroke.getKeyStroke("F2"), "action.NewGame");
		
		//map share
		ActionMap oAMap = oButtonsPanel.getActionMap();
		oAMap.put("action.NewGame", oActionNewGame);
		}
	/**
	 * The method adds charts provided in the charts parameter to the container.
	 * 
	 * @param oBoard Board to be displayed in the board container.
	 * @param bViewShips Variable whether or not missed ship positions are to be displayed on board.
	 */
	public void addBoards(Board oBoard, boolean bViewShips)
		{
		addBoards(oBoard, bViewShips, null);
		}
	/**
	 * The method adds the boards passed in the boards parameter to the container. <br />
	 * 
	 * The overloaded version, which additionally allows you to pass the listener of clicks to the board.
	 * 
	 * @param oBoard Board to be displayed in the board container.
	 * @param bViewShips Variable whether or not missed ship positions are to be displayed on board.
	 * @param oMouseListener Click event handler object for the added board.
	 */
	public void addBoards(Board oBoard, boolean bViewShips, GameplayMouseListener oMouseListener)
		{
		JComponentBoard oBoardComponent = new JComponentBoard(oBoard);
		if (oMouseListener != null)
			{
			if (oMouseListener.isSetComponent() == false)
				oMouseListener.setComponent(oBoardComponent);
			oBoardComponent.addMouseListener(oMouseListener);
			}
		oBoardComponent.setViewShips(bViewShips);
		oBoardPanelContainer.add(oBoardComponent);
		}
	/**
	 * Method called by the ship placement panel after the player has confirmed the correct placement of the player's ships.
	 */
	public void startGameplay()
		{
		oPlayerShips = oShipSelectionPanel.getShips();
		oComputerShips = JFrameGameWindowSettings.generatePlayer(oSettings);
		oGameStatusComponent.setPlayerShips(oPlayerShips);
		oGameStatusComponent.setComputerShips(oComputerShips);
		oGameStatusComponent.updateData();
		oAi = AiFactory.getAi(oSettings.getDifficultyLevel(), oSettings.getStraightLines(), oComputerShips);
		ShipPositioner oPositioner = new ShipPositioner();
		boolean bSuccessfulDeployment = false;
		boolean bContinue = true;
		try
			{
			while (bSuccessfulDeployment == false && bContinue == true)
				{
				bSuccessfulDeployment = oPositioner.shipSpaces(oComputerShips, oSettings.getStraightLines());
				if (bSuccessfulDeployment == false)
					{
					if (JOptionPane.showConfirmDialog(this, LANG.getProperty("errorMsg.shipPlacement.computerShipPlacementError"), LANG.getProperty("errorMsg.windowTitle"), JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
						bContinue = false;
					}
				}
			}
		catch(ParameterException e)
			{
			throw new DeveloperException(e);
			}
		if (bSuccessfulDeployment == false)
			{
			JOptionPane.showMessageDialog(this, LANG.getProperty("errorMsg.shipPlacement.computerShipPlacementFail"));
			}
		else
			{
			bPlayerTurn = true;
			oBoardPanelContainer.removeAll();
			if (oBoardPanelContainer.getComponentCount() == 0)
				{
				addBoards(oPlayerShips.getBoard(), true);
				addBoards(oComputerShips.getBoard(), false, new GameplayMouseListener(oComputerShips.getBoard(), null));
				}
			oButtonsPanel.setVisible(false);
			oShipSelectionPanel.setVisible(false);
			oBoardPanelContainer.setVisible(true);
			add(oBoardPanelContainer, BorderLayout.CENTER);
			repaint();
			}
		}
	/**
	 * The method called by the Settings Window when there are changes to the game settings
	 * (change of board size, number and / or size of ships, difficulty level) <br />
	 *
 	 * Corrects the required objects to match the new settings and if a game was started, cancels it and starts a new one.
	 */
	public void changeSettings()
		{
		oShipSelectionPanel.resetBoard();
		oShipSelectionPanel.resetDescription();
		//oShipSelectionPanel.repaint();
		if (oGameStatus.getGameLaunched() == true)
			{
			Timer oTimer = new Timer(10, new ActionNewGame());
			oTimer.setRepeats(false);
			oTimer.start();
			}
		}
	/**
	 * The method creates a new board and a new container containing the player's ships and returns the container object. <br />
	 *
	 * The size board, the number and the Size of the ships are determined by the game settings given in the parameter.
	 * 
	 * @param oSettings Main game settings.
	 * @return Returns the player's ship container.
	 */
	public static ShipIterator generatePlayer(Settings oSettings)
		{
		Board oBoard = new Board(oSettings.getBoardWidth(), oSettings.getBoardHeight());
		ShipIterator oShips = new ShipIterator(oBoard);
		int[] aShipsList = oSettings.getShips();
		for (int iSize: aShipsList)
			oShips.addAShip(iSize);
		return oShips;
		}
	}
