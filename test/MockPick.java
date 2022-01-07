import java.util.List;

import dungeon.GameModel;
import dungeon.GameModelReadOnly;

/**
 * Mock model created for testing the pick operations of the controller.
 */
public class MockPick implements GameModel, GameModelReadOnly {

  @Override
  public void refresh() {
    //not used in this mock implementation
  }

  @Override
  public void createPlayer() {
    //not used in this mock implementation
  }

  @Override
  public void addPlayerInDungeon() {
    //not used in this mock implementation
  }

  @Override
  public boolean isPlayerDead() {
    return false;
  }

  @Override
  public boolean isGameOver() {
    return false;
  }

  @Override
  public String getNextPossibleMoves(String loc) {
    return "N E S W";
  }

  @Override
  public String getPlayerDescription() {
    return "Diamonds- 1, Sapphires- 2, Rubies- 2, Arrows- 5";
  }

  @Override
  public int getRubyCount(String loc) {
    return 1;
  }

  @Override
  public int getDiamondCount(String loc) {
    return 2;
  }

  @Override
  public int getSapphireCount(String loc) {
    return 2;
  }

  @Override
  public int getArrowCount(String loc) {
    return 2;
  }

  @Override
  public String getLocationSmell(String loc) {
    return "";
  }

  @Override
  public String getAvailableItems(String loc) {
    String s = "";

    if (getRubyCount(loc) > 0) {
      s += getRubyCount(loc) + " rubies, ";
    }
    if (getDiamondCount(loc) > 0) {
      s += getDiamondCount(loc) + " diamonds, ";
    }
    if (getSapphireCount(loc) > 0) {
      s += getSapphireCount(loc) + " sapphires, ";
    }
    if (getArrowCount(loc) > 0) {
      s += getArrowCount(loc) + " arrows ";
    }
    return s;
  }

  @Override
  public boolean movePlayerTo(String direction) {
    return true;
  }

  @Override
  public void pickItemAtLocation(String item, int count) {
    String loc = getPlayerLocation();
    if ((item.equalsIgnoreCase("R") && count <= getRubyCount(loc))
            || (item.equalsIgnoreCase("D") && count <= getDiamondCount(loc))
            || (item.equalsIgnoreCase("S") && count <= getSapphireCount(loc))
            || (item.equalsIgnoreCase("A") && count <= getArrowCount(loc))) {
      //not used in this mock implementation
    } else {
      throw new IllegalArgumentException("Invalid choice of item.");
    }
  }

  @Override
  public int shootArrow(String direction, int distance) {
    return 0;
  }

  @Override
  public String getStartLocation() {
    return "";
  }

  @Override
  public String getEndLocation() {
    return "";
  }

  @Override
  public List<String> getEdges() {
    return null;
  }

  @Override
  public String getPlayerLocation() {
    return null;
  }

  @Override
  public List<String> getAllVisitedLocations() {
    return null;
  }

  @Override
  public String getLocationType(String loc) {
    return "Cave";
  }

  @Override
  public List<String> getOtyughLocations() {
    return null;
  }
}
