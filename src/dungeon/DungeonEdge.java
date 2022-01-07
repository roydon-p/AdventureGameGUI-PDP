package dungeon;

/**
 * Represents an edge in the dungeon. Each edge can be used to represent the path
 * between 2 locations in the dungeon. The class is kept package private as it
 * will be used only within the dungeon model package.
 */
class DungeonEdge implements Edge {
  private final String p1;
  private final String p2;

  /**
   * Creates 1 edge between 2 neighbouring locations in the dungeon.
   *
   * @param p1 location 1 in the dungeon
   * @param p2 location 2 in the dungeon
   */
  DungeonEdge(String p1, String p2) {
    this.p1 = p1;
    this.p2 = p2;
  }

  @Override
  public String getP1() {
    return p1;
  }

  @Override
  public String getP2() {
    return p2;
  }

  @Override
  public String toString() {
    return p1 + "=" + p2;
  }
}
