import java.util.List;

import dungeon.GameModel;
import dungeon.GameModelReadOnly;

/**
 * Mock model created for testing the shoot operations of the controller.
 */
public class MockShoot implements GameModel, GameModelReadOnly {
  private boolean monsterDead = false;
  private int arrowCount = 3;

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
    return "N S W";
  }

  @Override
  public String getPlayerDescription() {
    return "Diamonds- 0, Sapphires- 0, Rubies- 0, Arrows- " + arrowCount;
  }

  @Override
  public int getRubyCount(String loc) {
    return 0;
  }

  @Override
  public int getDiamondCount(String loc) {
    return 0;
  }

  @Override
  public int getSapphireCount(String loc) {
    return 0;
  }

  @Override
  public int getArrowCount(String loc) {
    return 0;
  }

  @Override
  public String getLocationSmell(String loc) {
    if (!monsterDead) {
      return "Terrible";
    } else {
      return "";
    }
  }

  @Override
  public String getAvailableItems(String loc) {
    return "";
  }

  @Override
  public boolean movePlayerTo(String direction) {
    return false;
  }

  @Override
  public void pickItemAtLocation(String item, int count) {
    //not used in this mock implementation
  }

  @Override
  public int shootArrow(String direction, int distance) {
    if (direction.equals("S") && distance == 1) {
      arrowCount--;
      return 1;
    } else if (direction.equals("S") && distance == 2) {
      arrowCount--;
      monsterDead = true;
      return 2;
    } else if (direction.equals("S") && distance == 3) {
      arrowCount--;
      return 0;
    } else if (direction.equals("W")) {
      return -1;
    } else {
      throw new IllegalArgumentException("Invalid move");
    }
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

