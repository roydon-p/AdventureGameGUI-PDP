package dungeon;

/**
 * Represents the Directions that a player can travel in the dungeon.
 */
public enum Direction {
  N("N"), E("E"), S("S"), W("W");

  private final String direction;

  Direction(String direction) {
    this.direction = direction;
  }

  @Override
  public String toString() {
    return direction;
  }
}
