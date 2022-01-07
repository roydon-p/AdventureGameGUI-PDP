package dungeon;

/**
 * Represents a monster that lives in the caves of the dungeons. They can eat the player
 * if the player enters the cave they live in. A Monster can be killed by a player using arrows.
 * The interface is kept package private as it will be used only within the dungeon model package.
 */
interface Monster {

  String getLocation();

  int getHealth();

  void arrowStrike();
}
