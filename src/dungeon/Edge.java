package dungeon;

/**
 * Represents an edge in a graph. Each edge contains 2 vertices p1 and p2. The interface and
 * all its methods are kept package private as they will be used only within the dungeon model
 * package.
 */
interface Edge {

  /**
   * Gets the p1 vertex of the edge.
   *
   * @return the vertex of the edge
   */
  String getP1();

  /**
   * Gets the p2 vertex of the edge.
   *
   * @return the vertex of the edge
   */
  String getP2();

}
