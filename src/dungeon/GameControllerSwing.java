package dungeon;

import java.awt.event.KeyEvent;

/**
 * Represents a Controller for Dungeon Adventure Game: it handles user moves by executing them
 * using the model. This controller handles all the key events as well as the mouse click
 * events that happen on the GUI of the adventure game.
 */
public interface GameControllerSwing {
  /**
   * Setups up all the listeners for the Swing view that will generate the calls to cobntroller
   * methods.
   */
  void setupViewListeners();

  /**
   * Creates a new game model using the parameters supplied by the user so the game can be started.
   *
   * @param rowCount                the no. of rows in the dungeon grid
   * @param colCount                the no. of columns in the dungeon grid
   * @param degCount  the no of paths that can be added to the kruskals output minimum
   *                                spanning tree.
   * @param isWrap                  true if the dungeon has paths wrapping type
   * @param percentage the percentage of caves to which treasure is to be assigned.
   *                                Also indicates the percentage of locations to which arrows will
   *                                be assigned.
   * @param otyughCount             indicates the difficulty level of the game based on the
   *                                number of otyughs present in the dungeon.
   */
  void createModel(int rowCount, int colCount, int degCount, boolean isWrap, int percentage,
                   int otyughCount);

  /**
   * Restarts the game using the same settings as previous game. Resets all the progress made by
   * the player in the previous game. All the features of the dungeon remain the same.
   */
  void restartGame();

  /**
   * Handles the key event when combination of 2 keys is pressed.
   *
   * @param k the key event
   */
  void handleComboKeyEvent(KeyEvent k);

  /**
   * Handles the key event when only 1 key is pressed.
   *
   * @param k the key event
   */
  void handleSingleKeyEvent(KeyEvent k);

  /**
   * Handles the mouse click event when the user clicks on a cell in the dungeon on the GUI.
   *
   * @param x The X-coordinate of the position where mouse was clicked
   * @param y The Y-coordinate of the position where mouse was clicked
   * @param isWrap indicates the wrpping property of the dungeon
   * @param rowCount indicates the no of rows in the dungeon
   * @param colCount indicates the no of columns in the dungeon
   */
  void handleCellClick(int x, int y, boolean isWrap, int rowCount, int colCount);
}
