package pl.vgtworld.games.ship.components;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import pl.vgtworld.exceptions.ParameterException;
import pl.vgtworld.exceptions.DeveloperException;


/**
 * The panel containing the tools for managing the list of ships in the settings.
 * 
 * @author VGT
 * @version 1.0
 */
public class JPanelShipListSettings
	extends JPanel
	{
	/**
	 * List object holding ships.
	 */
	private JListShipListSettings oShipList;
	/**
	 * A private class that performs the action of adding a ship.
	 */
	private class ActionAdd
		extends AbstractAction
		{
		public ActionAdd()
			{
			putValue(Action.SHORT_DESCRIPTION, JFrameGameWindowSettings.LANG.getProperty("action.settings.shipList.add.desc"));
			URL oImgUrl = getClass().getResource("/pl/vgtworld/games/ship/img/button-add.png");
			if (oImgUrl == null)
				putValue(Action.NAME, JFrameGameWindowSettings.LANG.getProperty("action.settings.shipList.add"));
			else
				{
				Image oImg = Toolkit.getDefaultToolkit().getImage(oImgUrl);
				putValue(Action.SMALL_ICON, new ImageIcon(oImg));
				}
			}
		public void actionPerformed(ActionEvent oEvent)
			{
			try
				{
				oShipList.addList(1);
				}
			catch (ParameterException e)
				{
				throw new DeveloperException(e);
				}
			}
		}
	/**
	 * A private class that carries out the action of removing the selected ships.
	 */
	private class ActionRemove
		extends AbstractAction
		{
		public ActionRemove()
			{
			putValue(Action.SHORT_DESCRIPTION, JFrameGameWindowSettings.LANG.getProperty("action.settings.shipList.delete.desc"));
			URL oImgUrl = getClass().getResource("/pl/vgtworld/games/ship/img/button-delete.png");
			if (oImgUrl == null)
				putValue(Action.NAME, JFrameGameWindowSettings.LANG.getProperty("action.settings.shipList.delete"));
			else
				{
				Image oImg = Toolkit.getDefaultToolkit().getImage(oImgUrl);
				putValue(Action.SMALL_ICON, new ImageIcon(oImg));
				}
			}
		public void actionPerformed(ActionEvent oEvent)
			{
			int[] aChecked = oShipList.getSelectedIndices();
			if (aChecked.length == 0)
				JOptionPane.showMessageDialog(JPanelShipListSettings.this, JFrameGameWindowSettings.LANG.getProperty("errorMsg.settings.shipList.noShipSelected"));
			try
				{
				for (int i = aChecked.length - 1; i >= 0; --i)
					oShipList.deleteLista(aChecked[i]);
				}
			catch (ParameterException e)
				{
				throw new DeveloperException(e);
				}
			}
		}
	/**
	 * A private class that performs the action of increasing the Size of selected ships.
	 */
	private class ActionEnlarge
		extends AbstractAction
		{
		public ActionEnlarge()
			{
			putValue(Action.SHORT_DESCRIPTION, JFrameGameWindowSettings.LANG.getProperty("action.settings.shipList.increase.desc"));
			URL oImgUrl = getClass().getResource("/pl/vgtworld/games/ship/img/button-up.png");
			if (oImgUrl == null)
				putValue(Action.NAME, JFrameGameWindowSettings.LANG.getProperty("action.settings.shipList.increase"));
			else
				{
				Image oImg = Toolkit.getDefaultToolkit().getImage(oImgUrl);
				putValue(Action.SMALL_ICON, new ImageIcon(oImg));
				}
			}
		public void actionPerformed(ActionEvent oEvent)
			{
			int[] aChecked = oShipList.getSelectedIndices();
			if (aChecked.length == 0)
				JOptionPane.showMessageDialog(JPanelShipListSettings.this, JFrameGameWindowSettings.LANG.getProperty("errorMsg.settings.shipList.noShipSelected"));
			try
				{
				for (int iSelected: aChecked)
					oShipList.zoomList(iSelected);
				}
			catch (ParameterException e)
				{
				throw new DeveloperException(e);
				}
			}
		}
	/**
	 * Private class carrying out actions to reduce the Size of selected ships.
	 */
	private class ActionZoomOut
		extends AbstractAction
		{
		public ActionZoomOut()
			{
			//putValue(Action.NAME, "Zoom out selected");
			putValue(Action.SHORT_DESCRIPTION, JFrameGameWindowSettings.LANG.getProperty("action.settings.shipList.decrease.desc"));
			URL oImgUrl = getClass().getResource("/pl/vgtworld/games/ship/img/button-down.png");
			if (oImgUrl == null)
				putValue(Action.NAME, JFrameGameWindowSettings.LANG.getProperty("action.settings.shipList.decrease"));
			else
				{
				Image oImg = Toolkit.getDefaultToolkit().getImage(oImgUrl);
				putValue(Action.SMALL_ICON, new ImageIcon(oImg));
				}
			}
		public void actionPerformed(ActionEvent oEvent)
			{
			int[] aChecked = oShipList.getSelectedIndices();
			if (aChecked.length == 0)
				JOptionPane.showMessageDialog(JPanelShipListSettings.this, JFrameGameWindowSettings.LANG.getProperty("errorMsg.settings.shipList.noShipSelected"));
			try
				{
				for (int iSelected: aChecked)
					oShipList.zoomOutList(iSelected);
				}
			catch (ParameterException e)
				{
				throw new DeveloperException(e);
				}
			}
		}
        /**
         * Private class responsible for changing a ship's name.
         */
        
        private class ActionRename
                extends AbstractAction
                {
                public ActionRename()
                    {
                        putValue(Action.SHORT_DESCRIPTION, JFrameGameWindowSettings.LANG.getProperty("action.settings.shipList.rename.desc"));
			URL oImgUrl = getClass().getResource("/pl/vgtworld/games/ship/img/button-rename.png");
                        if (oImgUrl == null)
				putValue(Action.NAME, JFrameGameWindowSettings.LANG.getProperty("action.settings.shipList.rename"));
			else
				{
				Image oImg = Toolkit.getDefaultToolkit().getImage(oImgUrl);
				putValue(Action.SMALL_ICON, new ImageIcon(oImg));
				}
                    }
                public void actionPerformed(ActionEvent oEvent)
                    {
                        int[] aChecked = oShipList.getSelectedIndices();
			if (aChecked.length == 0)
				JOptionPane.showMessageDialog(JPanelShipListSettings.this, JFrameGameWindowSettings.LANG.getProperty("errorMsg.settings.shipList.noShipSelected"));
			try
				{
				for (int iSelected: aChecked)
					oShipList.renameShip(iSelected);
				}
			catch (ParameterException e)
				{
				throw new DeveloperException(e);
				}
                    }
                }
        
	/**
	 * The default constructor.
	 */
	public JPanelShipListSettings()
		{
		setLayout(new BorderLayout());
		
		JLabel oShipListLabel = new JLabel(JFrameGameWindowSettings.LANG.getProperty("settings.shipList.title"), JLabel.CENTER);
		oShipList = new JListShipListSettings();
		JScrollPane oShipListScroll = new JScrollPane(oShipList);
		// button panel
		JPanel oButtonsPanel = new JPanel();
		oButtonsPanel.setLayout(new GridLayout(1, 5));
		oButtonsPanel.add(new JButton(new ActionAdd()));
		oButtonsPanel.add(new JButton(new ActionRemove()));
		oButtonsPanel.add(new JButton(new ActionEnlarge()));
		oButtonsPanel.add(new JButton(new ActionZoomOut()));
                oButtonsPanel.add(new JButton(new ActionRename()));
		
		add(oShipListLabel, BorderLayout.PAGE_START);
		add(oShipListScroll, BorderLayout.CENTER);
		add(oButtonsPanel, BorderLayout.PAGE_END);
		}
	/**
	 * A method that returns a ship list object.
	 * 
	 * @return Ship list
	 */
	public JListShipListSettings getShipList()
		{
		return oShipList;
		}
	}
