package dungeon;

import java.util.List;

/**
 * Represents a player that can be added to a dungeon. The player can collect the treasures and
 * arrows from the locations they visit in the dungeon. The interface and all its methods are kept
 * package private as they will be used only within the dungeon model package.
 */
interface Player {
  /**
   * get the count of rubies collected by the player.
   *
   * @return count of rubies collected
   */
  int getRubyCount();

  /**
   * get the count of diamonds collected by the player.
   *
   * @return count of diamonds collected
   */
  int getDiamondCount();

  /**
   * get the count of sapphires collected by the player.
   *
   * @return count of sapphires collected
   */
  int getSapphireCount();

  /**
   * get the count of arrows collected by the player.
   *
   * @return count of arrows collected
   */
  int getArrowCount();

  /**
   * Get the current location of the player.
   *
   * @return location of the player
   */
  String getLocation();

  /**
   * Set the location of the player.
   *
   * @param location location set by the dungeon model.
   */
  void setLocation(String location);

  /**
   * Add the newly picked ruby count values to existing ruby values.
   *
   * @param rubyCount the count of rubies picked from current location
   */
  void addRuby(int rubyCount);

  /**
   * Add the newly picked diamond count values to existing diamond values.
   *
   * @param diamondCount the count of diamonds picked from current location
   */
  void addDiamond(int diamondCount);

  /**
   * Add the newly picked sapphire count values to existing sapphire values.
   *
   * @param sapphireCount the count of sapphires picked from current location
   */
  void addSapphire(int sapphireCount);

  /**
   * Add the newly picked arrow count values to existing arrow values.
   *
   * @param arrowCount the count of arrows picked from current location
   */
  void addArrows(int arrowCount);

  /**
   * deduct 1 arrow from the players' collection after every shot.
   */
  void deductArrow();

  List<String> getVisitedLocations();

}
