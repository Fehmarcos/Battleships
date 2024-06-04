package pl.vgtworld.games.ship.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pl.vgtworld.exceptions.ParameterException;
import pl.vgtworld.exceptions.DeveloperException;
import pl.vgtworld.games.ship.Settings;

/**
 * Game settings window.
 * 
 * @author VGT
 * @version 1.0
 */
public class JDialogSettings
	extends JDialog
	{
	/**
	 * Settings window width.
	 */
	public static final int Width = 600;
	/**
	 * Settings window height.
	 */
	public static final int Height = 480; 
	/**
	 * Reference to the main game window.
	 */
	private JFrameGameWindowSettings oMainWindow;
	/**
	 * Object that stores game settings.
	 */
	private Settings oSettings;
	/**
	 * Slider that allows you to set the Width board.
	 */
	private JSlider oBoardWidthSlider;
	/**
	 * Slider pozwalajacy ustawic Height board.
	 */
	private JSlider oBoardHeightSlider;
	/**
	 * Slider that allows you to set the computer difficulty level.
	 */
	private JSlider oDifficultyLevel;
	/**
	 * Text field storing Width board.
	 */
	private JTextField oBoardWidth;
	/**
	 * Text field for the Height board.
	 */
	private JTextField oBoardHeight;
	/**
	 * Checkbox with information if ships can only be straight lines.
	 */
	private JCheckBox oShipsStraightLines;
	/**
	 * Button for saving changes to settings.
	 */
	private JButton oButtonSave;
	/**
	 * Button to cancel changes to the settings.
	 */
	private JButton oButtonCancel;
	/**
	 * Panel for creating a list of ships.
	 */
	private JPanelShipListSettings oShipList;
	/**
	 * A checkbox that determines whether to save the current settings as default.
	 */
	private JCheckBox oSaveSettings;
	/**
	 * Private class handling the sliding action of the slider specifying Width board.
	 */
	private class ActionWidthSlider
		implements ChangeListener
		{
		public void stateChanged(ChangeEvent oEvent)
			{
			JSlider oSource = (JSlider)oEvent.getSource();
			oBoardWidth.setText(String.valueOf(oSource.getValue()));
			}
		}
	/**
	 * Klasa prywatna zawierajaca obsluge akcji przesuniecia slidera okreslajacego Height board.
	 */
	private class ActionHeightSlider
		implements ChangeListener
		{
		public void stateChanged(ChangeEvent oEvent)
			{
			JSlider oSource = (JSlider)oEvent.getSource();
			oBoardHeight.setText(String.valueOf(oSource.getValue()));
			}
		}
	/**
	 * Private class containing handling of the action of pressing the button canceling changes in the settings.
	 */
	private class ActionCancel
		extends AbstractAction
		{
		public ActionCancel()
			{
			putValue(Action.NAME, JFrameGameWindowSettings.LANG.getProperty("action.settings.cancel"));
			putValue(Action.SHORT_DESCRIPTION, JFrameGameWindowSettings.LANG.getProperty("action.settings.cancel.desc"));
			}
		public void actionPerformed(ActionEvent oEvent)
			{
			setVisible(false);
			}
		}
	/**
	 * Private class containing handling of the action of pressing the button confirming changes in the settings.
	 */
	private class ActionSave
		extends AbstractAction
		{
		public ActionSave()
			{
			putValue(Action.NAME, JFrameGameWindowSettings.LANG.getProperty("action.settings.save"));
			putValue(Action.SHORT_DESCRIPTION, JFrameGameWindowSettings.LANG.getProperty("action.settings.save.desc"));
			}
		public void actionPerformed(ActionEvent oEvent)
			{
			try
				{
				// check for incorrect data in values ​​of settings
				int iBoardWidth;
				int iBoardHeight;
				try
					{
					iBoardWidth = Integer.parseInt(oBoardWidth.getText());
					iBoardHeight = Integer.parseInt(oBoardHeight.getText());
					}
				catch (NumberFormatException e)
					{
					JOptionPane.showMessageDialog(JDialogSettings.this, JFrameGameWindowSettings.LANG.getProperty("errorMsg.settings.invalidBoardSize"));
					return;
					}
				if (iBoardWidth < 1 || iBoardHeight < 1)
					{
					JOptionPane.showMessageDialog(JDialogSettings.this, JFrameGameWindowSettings.LANG.getProperty("errorMsg.settings.invalidBoardSize"));
					return;
					}
				if (oShipList.getShipList().getNumberOfShips() == 0)
					{
					JOptionPane.showMessageDialog(JDialogSettings.this, JFrameGameWindowSettings.LANG.getProperty("errorMsg.settings.noShip"));
					return;
					}
				// save settings
				oSettings.setBoardWidth(iBoardWidth);
				oSettings.setBoardHeight(iBoardHeight);
				oSettings.setDifficultyLevel(oDifficultyLevel.getValue());
				if (oShipsStraightLines.isSelected() == true)
					oSettings.setStraightLines(true);
				else
					oSettings.setStraightLines(false);
				oSettings.removeAllShips();
				int[] aLista = oShipList.getShipList().getShipList();
				for (int iSize: aLista)
					oSettings.addShip(iSize);
				
				oMainWindow.changeSettings();
				
				if (oSaveSettings.isSelected())
					oSettings.saveDefaultSettings();
				}
			catch (ParameterException e)
				{
				throw new DeveloperException(e);
				}
			setVisible(false);
			}
		}
	/**
	 * Constructor.
	 */
	public JDialogSettings(JFrameGameWindowSettings oMainWindow, Settings oSettings)
		{
		super(oMainWindow, true);
		this.oMainWindow = oMainWindow;
		this.oSettings = oSettings;
		//Width board
		JLabel oBoardWidthLabel = new JLabel(JFrameGameWindowSettings.LANG.getProperty("settings.boardWidth"), JLabel.CENTER);
		oBoardWidthSlider = new JSlider(5, 25, oSettings.getBoardWidth());
		oBoardWidthSlider.addChangeListener(new ActionWidthSlider());
		oBoardWidth = new JTextField(5);
		oBoardWidth.setHorizontalAlignment(JTextField.RIGHT);
		oBoardWidth.setText(String.valueOf(oBoardWidthSlider.getValue()));
		//Height board
		JLabel oBoardHeightLabel = new JLabel(JFrameGameWindowSettings.LANG.getProperty("settings.boardHeight"), JLabel.CENTER);
		oBoardHeightSlider = new JSlider(5, 25, oSettings.getBoardHeight());
		oBoardHeightSlider.addChangeListener(new ActionHeightSlider());
		oBoardHeight = new JTextField(5);
		oBoardHeight.setHorizontalAlignment(JTextField.RIGHT);
		oBoardHeight.setText(String.valueOf(oBoardHeightSlider.getValue()));
		//difficulty level
		JLabel oDifficultyLevelLabel = new JLabel(JFrameGameWindowSettings.LANG.getProperty("settings.difficulty"), JLabel.CENTER);
		oDifficultyLevel = new JSlider(1, 100, oSettings.getDifficultyLevel());
		//shape of ships
		JLabel oShipsStraightLinesLabel = new JLabel(JFrameGameWindowSettings.LANG.getProperty("settings.shipsShape"), JLabel.CENTER);
		oShipsStraightLines = new JCheckBox(JFrameGameWindowSettings.LANG.getProperty("settings.shipsShapeCheckbox"));
		if (oSettings.getStraightLines() == true)
			oShipsStraightLines.setSelected(true);
		//ship list
		oShipList = new JPanelShipListSettings();
		int[] aShips = oSettings.getShips();
		try
			{
			for (int iSize: aShips)
				oShipList.getShipList().addList(iSize);
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		
		// put elements into the frame
		setLayout(new BorderLayout());
		JPanel oPanelLeft = new JPanel();
		oPanelLeft.setLayout(new GridLayout(4, 1));
		
		JPanel oBoardWidthContainer2 = new JPanel();
		JPanel oBoardWidthContainer = new JPanel();
		oBoardWidthContainer.setLayout(new GridLayout(3, 1));
		oBoardWidthContainer.add(oBoardWidthLabel);
		JPanel oBoardWidthTextfieldContainer = new JPanel();
		oBoardWidthTextfieldContainer.add(oBoardWidth);
		oBoardWidthContainer.add(oBoardWidthSlider);
		oBoardWidthContainer.add(oBoardWidthTextfieldContainer);
		oBoardWidthContainer2.add(oBoardWidthContainer);
		oPanelLeft.add(oBoardWidthContainer2);
		
		JPanel oBoardHeightContainer2 = new JPanel();
		JPanel oBoardHeightContainer = new JPanel();
		oBoardHeightContainer.setLayout(new GridLayout(3, 1));
		oBoardHeightContainer.add(oBoardHeightLabel);
		JPanel oBoardHeightTextfieldContainer = new JPanel();
		oBoardHeightTextfieldContainer.add(oBoardHeight);
		oBoardHeightContainer.add(oBoardHeightSlider);
		oBoardHeightContainer.add(oBoardHeightTextfieldContainer);
		oBoardHeightContainer2.add(oBoardHeightContainer);
		oPanelLeft.add(oBoardHeightContainer2);
		
		JPanel oDifficultyLevelContainer2 = new JPanel();
		JPanel oDifficultyLevelContainer = new JPanel();
		oDifficultyLevelContainer.setLayout(new GridLayout(2,1));
		oDifficultyLevelContainer.add(oDifficultyLevelLabel);
		JPanel oDifficultyLevelSliderContainer = new JPanel();
		oDifficultyLevelSliderContainer.add(oDifficultyLevel);
		oDifficultyLevelContainer.add(oDifficultyLevelSliderContainer);
		oDifficultyLevelContainer2.add(oDifficultyLevelContainer);
		oPanelLeft.add(oDifficultyLevelContainer2);
		
		JPanel oShipsShapeContainer2 = new JPanel();
		JPanel oShipsShapeContainer = new JPanel();
		oShipsShapeContainer.setLayout(new GridLayout(2, 1));
		
                // REMOVIDA OPÇÃO QUE PERMITE O POSICIONAMENTO DOS NAVIOS DE FORMA QUE NÃO SEJA VERTICAL OU HORIZONTAL
                //oShipsShapeContainer.add(oShipsStraightLinesLabel);
		//oShipsShapeContainer.add(oShipsStraightLines);
                
		oShipsShapeContainer2.add(oShipsShapeContainer);
		oPanelLeft.add(oShipsShapeContainer2);
		
		JPanel oPanelRight = new JPanel();
		oPanelRight.setLayout(new BorderLayout());
		oPanelRight.add(oShipList, BorderLayout.CENTER);
		
		JPanel oPanels = new JPanel();
		oPanels.setLayout(new GridLayout(1, 2));
		oPanels.add(oPanelLeft);
		oPanels.add(oPanelRight);
		add(oPanels, BorderLayout.CENTER);
		JPanel oButtons = new JPanel();
		oButtonCancel = new JButton(new ActionCancel());
		oButtonSave = new JButton(new ActionSave());
		oButtons.add(oButtonSave);
		oButtons.add(oButtonCancel);
		
		JPanel oSaveSettingsPanel = new JPanel();
		oSaveSettings = new JCheckBox(JFrameGameWindowSettings.LANG.getProperty("settings.saveAsDefault"));
		oSaveSettingsPanel.add(oSaveSettings);
		
		JPanel oBottomOptionsPanel = new JPanel();
		oBottomOptionsPanel.setLayout(new GridLayout(2, 1));
		oBottomOptionsPanel.add(oSaveSettingsPanel);
		oBottomOptionsPanel.add(oButtons);
		
		add(oBottomOptionsPanel, BorderLayout.PAGE_END);
		
		
		// other settings
		setMinimumSize(new Dimension(Width, Height));
		setTitle(JFrameGameWindowSettings.LANG.getProperty("settingsWindow.title"));
		setLocationRelativeTo(null);
		setResizable(true);
		}
	/**
	 * The method restores the settings of all sliders and Text fields to Values ​​from the settings object.
	 */
	public void reset()
		{
		//window position restoration
		int iPositionX = oMainWindow.getX() + (oMainWindow.getWidth() - Width) / 2;
		int iPositionY = oMainWindow.getY() + (oMainWindow.getHeight() - Height) / 2;
		setBounds(iPositionX, iPositionY, Width, Height);
		//reset settings
		oBoardWidth.setText(String.valueOf(oSettings.getBoardWidth()));
		oBoardWidthSlider.setValue(oSettings.getBoardWidth());
		oBoardHeight.setText(String.valueOf(oSettings.getBoardHeight()));
		oBoardHeightSlider.setValue(oSettings.getBoardHeight());
		oDifficultyLevel.setValue(oSettings.getDifficultyLevel());
		if (oSettings.getStraightLines() == true)
			oShipsStraightLines.setSelected(true);
		else
			oShipsStraightLines.setSelected(false);
		oShipList.getShipList().clearList();
		int[] aShips = oSettings.getShips();
		try
			{
			for (int iSize: aShips)
				oShipList.getShipList().addList(iSize);
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	}
