package dungeon;

import java.util.List;

/**
 * Represents the game model which can interact with the controller to receive and send data that
 * is required and generated by the Game.
 */
public interface GameModel {

  /**
   * Used to refresh the model when restart game command is initiated by the GUI.
   */
  void refresh();

  /**
   * Creates the player that will be added in the dungeon.
   */
  void createPlayer();

  /**
   * Adds the player to the dungeon.
   */
  void addPlayerInDungeon();

  /**
   * Checks if the player is dead or alive so as to update the game state.
   *
   * @return player life status
   */
  boolean isPlayerDead();

  /**
   * Checks if the game is over based on if player has reached the ned location or player is dead.
   *
   * @return game over status
   */
  boolean isGameOver();

  /**
   * Gets all the next possible moves for the player from its current location.
   *
   * @return directions in which the player can move next
   */
  String getNextPossibleMoves(String loc);

  /**
   * Get the description of the player along with the information of treasure and arrows
   * they have collected till this point in game.
   *
   * @return player description
   */
  String getPlayerDescription();

  /**
   * Get the count of rubies at players current location.
   *
   * @return count of rubies
   */
  int getRubyCount(String loc);

  /**
   * Get the count of diamonds at players current location.
   *
   * @return count of diamonds
   */
  int getDiamondCount(String loc);

  /**
   * Get the count of sapphires at players current location.
   *
   * @return count of sapphires
   */
  int getSapphireCount(String loc);

  /**
   * Get the count of arrows at players current location.
   *
   * @return count of arrows
   */
  int getArrowCount(String loc);

  /**
   * Gets the smell at players current location based on the proximity to otyughs.
   *
   * @return smell at the location
   */
  String getLocationSmell(String loc);

  /**
   * Gets all the items that are available at the current location of the player.
   *
   * @return items present at players current location
   */
  String getAvailableItems(String loc);

  /**
   * moves the player in the direction mentioned.
   *
   * @param direction the direction in which the player is to be moved
   * @return the result of the move
   */
  boolean movePlayerTo(String direction);

  /**
   * Picks the given quantity of given item if it exists at current location.
   *
   * @param item the item to be picked
   * @param count the quantity to be picked
   */
  void pickItemAtLocation(String item, int count);

  /**
   * Shoots the arrow in the specified direction for the specified distance.
   *
   * @param direction the direction in which the arrow is shot
   * @param distance the distance(no of caves) through which the arrow is shot through
   * @return returns how effective the arrow shot was.
   */
  int shootArrow(String direction, int distance);

  /**
   * The start location in the dungeon.
   *
   * @return start location of the maze
   */
  String getStartLocation();

  /**
   * The end location from where the player will exit the dungeon.
   *
   * @return the cave location from where the player can exit the dungeon
   */
  String getEndLocation();

  /**
   * Gets all the edges that for the dungeon.
   *
   * @return List of edges that form the traversal path in the dungeon
   */
  List<String> getEdges();

  /**
   * Gets the current location of the player.
   *
   * @return current location of the player
   */
  String getPlayerLocation();

  /**
   * Gets the type of location (Cave or Tunnel).
   *
   * @param loc the location for which type is to be known
   * @return the type of location
   */
  String getLocationType(String loc);

  /**
   * Gets the list of all otyughs locations in the dungeon.
   *
   * @return list of all otyughs location in the dungeon
   */
  List<String> getOtyughLocations();
}