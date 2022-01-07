package dungeon;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the player that is added to the master dungeon. This player will move through the
 * dungeon and collect treasures from the caves in the dungeon. The player also collects arrows
 * whcih they can use to slay the otyughs. The class is kept package private
 * as it will be used only within the dungeon model package.
 */
class MasterDungeonPlayer implements Player {
  private int diamondCount;
  private int sapphireCount;
  private int rubyCount;
  private String location;
  private int arrowCount;
  private List<String> visitedLocations;

  /**
   * Creates a player that will be entered into the dungeon. The player is created with 0 treasure
   * and dungeon start location as its initial location.
   *
   * @param diamondCount  count of diamonds that the player has before starting the game
   * @param sapphireCount count of sapphires that the player has before starting the game
   * @param rubyCount     count of rubies that the player has before starting the game
   * @param location      current location of the player
   * @param arrowCount    count of arrows that the player has before starting the game
   */
  MasterDungeonPlayer(int diamondCount, int sapphireCount, int rubyCount, String location,
                      int arrowCount) {
    if (location == null) {
      throw new IllegalArgumentException("Location cannot be null");
    }
    this.diamondCount = diamondCount;
    this.sapphireCount = sapphireCount;
    this.rubyCount = rubyCount;
    this.location = location;
    this.arrowCount = arrowCount;
    visitedLocations = new ArrayList<>();
    visitedLocations.add(location);
  }

  @Override
  public int getRubyCount() {
    return rubyCount;
  }

  @Override
  public int getDiamondCount() {
    return diamondCount;
  }

  @Override
  public int getSapphireCount() {
    return sapphireCount;
  }

  @Override
  public int getArrowCount() {
    return arrowCount;
  }

  @Override
  public String getLocation() {
    return location;
  }

  @Override
  public void setLocation(String location) {
    if (location == null) {
      throw new IllegalArgumentException("Location cannot be null");
    }
    this.location = location;
    if (!visitedLocations.contains(location)) {
      visitedLocations.add(location);
    }
  }

  @Override
  public void addRuby(int rubyCount) {
    this.rubyCount += rubyCount;
  }

  @Override
  public void addDiamond(int diamondCount) {
    this.diamondCount += diamondCount;
  }

  @Override
  public void addSapphire(int sapphireCount) {
    this.sapphireCount += sapphireCount;
  }

  @Override
  public void addArrows(int arrowCount) {
    this.arrowCount += arrowCount;
  }

  @Override
  public void deductArrow() {
    this.arrowCount -= 1;
  }

  @Override
  public List<String> getVisitedLocations() {
    return this.visitedLocations;
  }
}
