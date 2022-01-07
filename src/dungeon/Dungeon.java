package dungeon;

import java.util.List;

/**
 * Represents the dungeon that contains the caves/tunnels and controls the players movement
 * and state while they are moving in the dungeon. The dungeon is built such that every location
 * within the dungeon can be reached from every other location in the dungeon. The interface and
 * all its methods are kept package private as they will be used only within the dungeon model
 * package.
 */
interface Dungeon {

  /**
   * Adds the player to the dungeon.
   *
   * @param p the player that is to be added to the dungeon
   */
  void addPlayer(Player p);

  /**
   * Gets the next possible moves a player can take from the given location.
   *
   * @param loc the input location
   * @return the next possible moves
   */
  String getNextPossibleMoves(String loc);

  /**
   * Gets the smell at given location based on the proximity to otyughs.
   *
   * @param loc the location at which smell needs to be known
   * @return smell at the loction
   */
  String getLocationSmell(String loc);

  /**
   * Gets the List of all locations in the dungeon.
   *
   * @return List of all locations
   */
  List<Cave> getCaves();

  /**
   * moves the player in the dungeon.
   *
   * @param p         the player that is being moved
   * @param direction the direction in which the player is to be moved
   * @return the result of the move
   */
  int move(Player p, Direction direction);

  /**
   * picks the specified count of rubies if it exists at the current location of the player.
   *
   * @param p the player to whom the rubies will be assigned.
   * @param count the number of rubies to be picked.
   */
  void pickRuby(Player p, int count);

  /**
   * picks the specified count of diamonds if it exists at the current location of the player.
   *
   * @param p the player to whom the diamonds will be assigned.
   * @param count the number of diamonds to be picked.
   */
  void pickDiamond(Player p, int count);

  /**
   * picks the specified count of sapphires if it exists at the current location of the player.
   *
   * @param p the player to whom the sapphires will be assigned.
   * @param count the number of sapphires to be picked.
   */
  void pickSapphire(Player p, int count);

  /**
   * picks the specified count of arrows if it exists at the current location of the player.
   *
   * @param p the player to whom the arrows will be assigned.
   * @param count the number of arrows to be picked.
   */
  void pickArrows(Player p, int count);

  /**
   * Shoots the arrow in the specified direction for the specified distance.
   *
   * @param p the player shooting the arrow
   * @param dir the direction in which the arrow is shot
   * @param distance the distance(no of caves) through which the arrow is shot through
   * @return returns how effective the arrow shot was.
   */
  int shootArrow(Player p, Direction dir, int distance);

  /**
   * Gets all the edges that for the dungeon.
   *
   * @return List of edges that form the traversal path in the dungeon
   */
  List<Edge> getEdges();

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
   * Gets the type of location (Cave or Tunnel).
   *
   * @param loc the location for which type is to be known
   * @return the type of location
   */
  String getLocationType(String loc);

  /**
   * Gets the list of all otyughs in the dungeon.
   *
   * @return list of all otyughs in the dungeon
   */
  List<Monster> getOtyughs();
}
