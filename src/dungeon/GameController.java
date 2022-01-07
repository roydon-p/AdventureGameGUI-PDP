package dungeon;

/**
 * This is the controller for the Adventure game in which the player has to traverse through a
 * dungeon that contains monsters called otyughs that can eat the player. The player has to
 * collect arrows which it can use to slay the otyugh as well as collect the treasure that it
 * finds in various locations in the dungeon before reaching the end of the dungeon.
 */
public interface GameController {

  /**
   * Start the adventure game by passing the model of the game to the controller.
   *
   * @param g model of the game
   */
  void startGame(GameModel g);
}
