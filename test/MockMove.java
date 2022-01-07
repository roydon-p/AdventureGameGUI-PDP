import java.util.List;

import dungeon.GameModel;
import dungeon.GameModelReadOnly;

/**
 * Mock model created for testing the move operations of the controller.
 */
public class MockMove implements GameModel, GameModelReadOnly {
  private boolean playerDead = false;

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
    return playerDead;
  }

  @Override
  public boolean isGameOver() {
    return playerDead;
  }

  @Override
  public String getNextPossibleMoves(String loc) {
    return "N S W";
  }

  @Override
  public String getPlayerDescription() {
    return "Diamonds- 0, Sapphires- 0, Rubies- 0, Arrows- 3";
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
    return "Terrible";
  }

  @Override
  public String getAvailableItems(String loc) {
    return "";
  }

  @Override
  public boolean movePlayerTo(String direction) {
    if (direction.equals("S") || direction.equals("W")) {
      return true;
    } else if (direction.equals("N")) {
      playerDead = true;
      return false;
    } else {
      throw new IllegalArgumentException("Invalid move");
    }
  }

  @Override
  public void pickItemAtLocation(String item, int count) {
    //not used in this mock implementation
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
