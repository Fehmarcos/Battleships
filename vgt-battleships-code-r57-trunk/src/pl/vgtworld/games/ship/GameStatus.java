package pl.vgtworld.games.ship;

import pl.vgtworld.exceptions.DeveloperException;

/**
 * The class that stores information about the current game status.<br />
 *
 * <p>
 * Updates:<br />
 * 1.1<br />
 * - adding methods {@link #getPlayerPoints ()} and {@link #getComputerPoints ()}.<br />
 * </p>
 * 
 * @author VGT
 * @version 1.1
 */
public class GameStatus
	{
	/**
	 * Determines whether the game has started.
	 */
	private boolean bGameLaunched;
	/**
	 * Determines if the game has ended.
	 */
	private boolean bGameIsOver;
	/**
	 * Determines whether the player's ship placement is completed and
         * approved.
	 */
	private boolean bShipsArranged;
	/**
	 * Determines whether the player has won.
	 */
	private boolean bWinTheplayer;
	/**
	 * Determines if the computer won.
	 */
	private boolean bWinTheComputer;
	/**
	 * The total number of player wins since the program was launched.
	 */
	private int iPlayerPoints;
	/**
	 * The total number of computer wins since the program was launched.
	 */
	private int iComputerPoints;
	/**
	 * Default constructor.
	 */
	public GameStatus()
		{
		iPlayerPoints = 0;
		iComputerPoints = 0;
		resetSettings();
		}
	/**
	 * Returns whether the game started.
	 * 
	 * @return Returns TRUE if the game is in progress or FALSE otherwise.
	 */
	public boolean getGameLaunched()
		{
		return bGameLaunched;
		}
	/**
	 * Returns whether the game is over.
	 * 
	 * @return Returns TRUE if the game is over (all player or computer 
         * ships hit), or FALSE otherwise.
	 */
	public boolean getGameOver()
		{
		return bGameIsOver;
		}
	/**
	 * Returns information as to whether the player's ships have been 
         * correctly placed on the board.
	 * 
	 * @return Returns TRUE if the player has placed the ships and they 
         * have been verified, or FALSE otherwise.
	 */
	public boolean getShipsArranged()
		{
		return bShipsArranged;
		}
	/**
	 * Returns whether the player has won the current game.
	 * 
	 * @return Returns TRUE if the player has sunk all the computer's 
         * ships, FALSE otherwise.
	 */
	public boolean getWinThePlayer()
		{
		return bWinTheplayer;
		}
	/**
	 * Returns whether the computer has won the current game.
	 * 
	 * @return Returns TRUE if the computer has sunk all of the player's 
         * ships, FALSE otherwise.
	 */
	public boolean getWinTheComputer()
		{
		return bWinTheComputer;
		}
	/**
	 * Returns the number of player points.
	 * 
	 * @return The number of points scored by the player.
	 * @since 1.1
	 */
	public int getPlayerPoints()
		{
		return iPlayerPoints;
		}
	/**
	 * Returns the number of computer points.
	 * 
	 * @return The number of points scored by the computer.
	 * @since 1.1
	 */
	public int getComputerPoints()
		{
		return iComputerPoints;
		}
	/**
	 * Method called when starting a new game.
	 * Sets the properties of an object to the required values.
	 */
	public void startNewGame()
		{
		resetSettings();
		bGameLaunched = true;
		}
	/**
	 * Method called when the player wins.
	 * Sets the properties of an object to the required values.
	 */
	public void playerVictory()
		{
		++iPlayerPoints;
		bGameLaunched = false;
		bGameIsOver = true;
		bWinTheplayer = true;
		}
	/**
	 * Method called when the computer wins.
	 * Sets the properties of an object to the required values.
	 */
	public void computerVictory()
		{
		++iComputerPoints;
		bGameLaunched = false;
		bGameIsOver = true;
		bWinTheComputer = true;
		}
	/**
	 * Method called when the player's ship placement is approved.
	 * Sets the properties of an object to the required values.
	 */
	public void approveTheLocationOfShips()
		{
		if (!bGameLaunched || bShipsArranged || bGameIsOver)
			throw new DeveloperException();
		bShipsArranged = true;
		}
	/**
	 * A method that resets the object's settings to their initial values.
	 */
	private void resetSettings()
		{
		bGameLaunched = false;
		bGameIsOver = false;
		bShipsArranged = false;
		bWinTheplayer = false;
		bWinTheComputer = false;
		}
	}
