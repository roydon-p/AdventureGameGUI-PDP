package dungeon;

import java.util.List;

/**
 * Represents the read-only game model which can be used by the view to display changes in
 * the game to the GUI.
 */
public interface GameModelReadOnly {

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
   * Gets the current location of the player.
   *
   * @return current location of the player
   */
  String getPlayerLocation();

  /**
   * Keeps track of all the locations visited by the player in current instance of the game.
   *
   * @return list of locations
   */
  List<String> getAllVisitedLocations();

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
