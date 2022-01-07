package dungeon;

import randoms.Randomizer;

/**
 * Represents a cave or tunnel in the dungeon. If the location has 2 entry points, then it is
 * classified as tunnel. A tunnel cannot have treasure, whereas a cave can contain treasure.
 * Each cave/tunnel maintains the directions which can be used to move to a neighbouring cave.
 * The class is kept package private as it will be used only within the dungeon model package.
 */
class Cave {
  private final boolean moveNorth;
  private final boolean moveSouth;
  private final boolean moveEast;
  private final boolean moveWest;
  private final String location;
  private boolean isTunnel;
  private int diamondCount;
  private int sapphireCount;
  private int rubyCount;
  private int arrowCount;
  private final int minArrowCount = 1;
  private final int maxArrowCount = 3;
  Randomizer r;

  /**
   * Creates a cave/tunnel and sets its initial values.
   *
   * @param location  location of the cave in the dungeon
   * @param moveNorth true if the door to north side is open, false if there is no door
   * @param moveSouth true if the door to south side is open, false if there is no door
   * @param moveEast  true if the door to east side is open, false if there is no door
   * @param moveWest  true if the door to west side is open, false if there is no door
   */
  Cave(String location, boolean moveNorth, boolean moveSouth, boolean moveEast,
       boolean moveWest, Randomizer r) {
    this.r = r;
    this.location = location;
    this.moveNorth = moveNorth;
    this.moveSouth = moveSouth;
    this.moveEast = moveEast;
    this.moveWest = moveWest;
    setIsTunnel();
  }

  Cave(Cave copy) {
    this.moveNorth = copy.moveNorth;
    this.moveSouth = copy.moveSouth;
    this.moveEast = copy.moveEast;
    this.moveWest = copy.moveWest;
    this.location = copy.location;
    this.isTunnel = copy.isTunnel;
    this.diamondCount = copy.diamondCount;
    this.sapphireCount = copy.sapphireCount;
    this.rubyCount = copy.rubyCount;
    this.arrowCount = copy.arrowCount;
  }

  private void setIsTunnel() {
    int openDoorCounter = 0;
    if (moveNorth) {
      openDoorCounter++;
    }
    if (moveSouth) {
      openDoorCounter++;
    }
    if (moveEast) {
      openDoorCounter++;
    }
    if (moveWest) {
      openDoorCounter++;
    }
    if (openDoorCounter == 2) {
      isTunnel = true;
    } else {
      isTunnel = false;
    }
  }

  protected boolean isMoveNorth() {
    return moveNorth;
  }

  protected boolean isMoveSouth() {
    return moveSouth;
  }

  protected boolean isMoveEast() {
    return moveEast;
  }

  protected boolean isMoveWest() {
    return moveWest;
  }

  protected boolean isTunnel() {
    return isTunnel;
  }

  protected void assignInitialTreasure() {
    this.diamondCount = Treasure.DIAMONDS.getRandomQuantity(r);
    this.sapphireCount = Treasure.SAPPHIRES.getRandomQuantity(r);
    this.rubyCount = Treasure.RUBIES.getRandomQuantity(r);
  }

  protected void assignInitialArrows() {
    this.arrowCount = r.getRandomInt(minArrowCount, maxArrowCount);
  }

  protected void updatePickedRubyStatus(int count) {
    this.rubyCount = this.rubyCount - count;
  }

  protected void updatePickedDiamondStatus(int count) {
    this.diamondCount = this.getDiamondCount() - count;
  }

  protected void updatePickedSapphireStatus(int count) {
    this.sapphireCount = this.sapphireCount - count;
  }

  protected void updatePickedArrowStatus(int count) {
    this.arrowCount = this.arrowCount - count;
  }

  protected int getDiamondCount() {
    return this.diamondCount;
  }

  protected int getSapphireCount() {
    return this.sapphireCount;
  }

  protected int getRubyCount() {
    return this.rubyCount;
  }

  protected int getArrowCount() {
    return this.arrowCount;
  }

  protected String getLocation() {
    return location;
  }
}

