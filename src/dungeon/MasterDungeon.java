package dungeon;

import randoms.Randomizer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents the MasterDungeon that is created for the player to move and collect treasure
 * and arrows. This dungeon contains caves and tunnels using which the player can move from start
 * position to end position. Both the start and end positions are caves in the tunnels.
 * The dungeon is created based on the dimensions given by the user and the paths are created
 * based on the minimum spanning tree created using Kruskals algorithm and degree of
 * interconnectivity. The dungeons can be wrapping or non wrapping as well. The dungeon contains
 * monsters called otyughs that can eat the player if the player gets in the same cave as them.
 * The player can slay the monster using the arrows that they collect while exploring the maze.
 * The class is kept package private as it will be used only within the dungeon model package.
 */
class MasterDungeon implements Dungeon {
  private final int rowCount;
  private final int colCount;
  private final int degOfInterconnectivity;
  private final boolean isWrap;
  private final int pickableItemFrequency;
  private final int otyughCount;
  private final Randomizer r;
  private Edge e;
  private List<Edge> updatedEdges = new ArrayList<>();
  private List<Edge> interconnectivityEdges = new ArrayList<>();
  private List<Edge> edges = new ArrayList<>();
  private List<Set> listOfSets = new ArrayList();
  private List<Cave> caves = new ArrayList<>();
  private List<Monster> otyughs = new ArrayList<>();
  private String startLocation = "";
  private String endLocation = "";

  /**
   * Creates an instance of a dungeon that the player can enter and play.
   *
   * @param rowCount                the no. of rows in the dungeon grid
   * @param colCount                the no. of columns in the dungeon grid
   * @param degOfInterconnectivity  the no of paths that can be added to the kruskals output minimum
   *                                spanning tree.
   * @param isWrap                  true if the dungeon has paths wrapping type
   * @param percentCavesForTreasure the percentage of caves to which treasure is to be assigned.
   *                                Also indicates the percentage of locations to which arrows will
   *                                be assigned.
   * @param r                       the randomizer object
   * @param otyughCount             indicates the difficulty level of the game based on the
   *                                number of otyughs present in the dungeon.
   */
  MasterDungeon(int rowCount, int colCount, int degOfInterconnectivity, boolean isWrap,
                int percentCavesForTreasure, Randomizer r, int otyughCount) {
    if (rowCount < 3 || colCount < 3) {
      throw new IllegalArgumentException("Minimum dimension of the dungeon should be 3X3.");
    }
    if (degOfInterconnectivity < 0
            || !isDegOfInterconnectivityValid(degOfInterconnectivity, rowCount, colCount, isWrap)) {
      throw new IllegalArgumentException("Degree of interconnectivity is Invalid.");
    }
    if (percentCavesForTreasure < 0) {
      throw new IllegalArgumentException("Percentage of caves to get treasure cannot be negative.");
    }
    if (otyughCount < 1) {
      throw new IllegalArgumentException("Minimum acceptable number of Otyughs is 1.");
    }
    this.rowCount = rowCount;
    this.colCount = colCount;
    this.degOfInterconnectivity = degOfInterconnectivity;
    this.isWrap = isWrap;
    this.pickableItemFrequency = percentCavesForTreasure;
    this.otyughCount = otyughCount;
    this.r = r;
    createDungeon();
  }

  MasterDungeon(MasterDungeon copy) {
    this.rowCount = copy.rowCount;
    this.colCount = copy.colCount;
    this.degOfInterconnectivity = copy.degOfInterconnectivity;
    this.isWrap = copy.isWrap;
    this.pickableItemFrequency = copy.pickableItemFrequency;
    this.otyughCount = copy.otyughCount;
    this.r = copy.r;
    this.updatedEdges = copy.updatedEdges;
    for (Cave c : copy.caves) {
      Cave c1 = new Cave(c);
      this.caves.add(c1);
    }
    this.startLocation = copy.startLocation;
    this.endLocation = copy.endLocation;
    for (Monster m : copy.otyughs) {
      Monster m1 = new Otyugh((Otyugh) m);
      this.otyughs.add(m1);
    }
  }

  private boolean isDegOfInterconnectivityValid(int degOfInterconnectivity, int rows, int cols,
                                                boolean isWrap) {
    int max;
    if (isWrap) {
      max = (rows * cols * 2) - ((rows * cols) - 1);
    } else {
      max = ((rows * cols * 2) - rows - cols) - ((rows * cols) - 1);
    }
    return degOfInterconnectivity <= max;
  }

  private void createDungeon() {
    //initialize all the edges in the maze
    initializeAllEdges();

    //create maze using kruskals algorithm
    createKruskalsMaze();

    //use degree of interconnectivity to added additional traversal paths to the maze
    applyInterconnectivity();

    //create the caves in the dungeon
    createCaves();

    //set the Start and End Locations
    setTerminals();

    //assign Otyughs to the caves
    assignOtyughs();

    //assign treasure to the specified percentage of caves
    assignTreasure();

    //assign arrows to the specified percentage of locations
    assignArrows();
  }

  private void initializeAllEdges() {
    //check if it is wrapping type dungeon or not, accordingly create the edges.
    //creating edges for wrapping dungeon
    if (isWrap) {
      for (int i = 0; i < rowCount; i++) {
        for (int j = 0; j < colCount; j++) {
          if (j == colCount - 1) {
            e = new DungeonEdge("" + i + "-" + j, "" + i + "-" + "0");
          } else {
            e = new DungeonEdge("" + i + "-" + j, "" + i + "-" + (j + 1));
          }
          edges.add(e);
        }
      }
      for (int i = 0; i < rowCount; i++) {
        for (int j = 0; j < colCount; j++) {
          if (i == rowCount - 1) {
            e = new DungeonEdge("" + i + "-" + j, "" + "0" + "-" + j);
          } else {
            e = new DungeonEdge("" + i + "-" + j, "" + (i + 1) + "-" + j);
          }
          edges.add(e);
        }
      }
    }
    //creating edges for non-wrapping dungeon
    else {
      for (int i = 0; i < rowCount; i++) {
        for (int j = 0; j < colCount - 1; j++) {
          e = new DungeonEdge("" + i + "-" + j, "" + i + "-" + (j + 1));
          edges.add(e);
        }
      }
      for (int i = 0; i < rowCount - 1; i++) {
        for (int j = 0; j < colCount; j++) {
          e = new DungeonEdge("" + i + "-" + j, "" + (i + 1) + "-" + j);
          edges.add(e);
        }
      }
    }
    updatedEdges = edges;
  }

  private void createKruskalsMaze() {
    updatedEdges = new ArrayList<>();
    //create sets for each node 00-ij
    for (int i = 0; i < rowCount; i++) {
      for (int j = 0; j < colCount; j++) {
        Set<String> a = new HashSet<String>();
        a.add("" + i + "-" + j);
        listOfSets.add(a);
      }
    }
    //for all edges in the edges list
    while (edges.size() > 0) {
      boolean skipMerging = false;
      Set<String> mergedSet = new HashSet<String>();
      //select a random edge from edges list
      int r_index = r.getRandomInt(0, edges.size());
      String p1 = edges.get(r_index).getP1();
      String p2 = edges.get(r_index).getP2();
      //check if p1 and p2 are in same set
      for (Set<String> s : listOfSets) {
        //if yes, then add the edge to interconnectivity list
        if (s.contains(p1) && s.contains(p2)) {
          interconnectivityEdges.add(edges.get(r_index));
          skipMerging = true;
        }
        //if no, then merge the sets containing p1 and p2
        else {
          if (s.contains(p1)) {
            mergedSet.addAll(s);
          }
          if (s.contains(p2)) {
            mergedSet.addAll(s);
          }
        }
      }
      if (!skipMerging) {
        //remove the original sets from the list
        List<Set> l_copy = new ArrayList();
        for (Set<String> s : listOfSets) {
          l_copy.add(s);
        }
        for (Set<String> s : l_copy) {
          if (s.contains(p1)) {
            listOfSets.remove(s);
          }
          if (s.contains(p2)) {
            listOfSets.remove(s);
          }
        }
        //add the new merged set to the list
        listOfSets.add(mergedSet);
        updatedEdges.add(edges.get(r_index));
      }
      //remove the edge from edges list
      edges.remove(r_index);
    }
  }

  private void applyInterconnectivity() {
    for (int i = 0; i < degOfInterconnectivity; i++) {
      int index = r.getRandomInt(0, interconnectivityEdges.size());
      updatedEdges.add(interconnectivityEdges.get(index));
      interconnectivityEdges.remove(index);
    }
  }

  private void createCaves() {
    for (int i = 0; i < rowCount; i++) {
      for (int j = 0; j < colCount; j++) {
        boolean northOpen = false;
        boolean southOpen = false;
        boolean westOpen = false;
        boolean eastOpen = false;
        for (Edge e : updatedEdges) {
          //open the doors for available path (when current location is p1 vertex of edge)
          if (e.getP1().equals("" + i + "-" + j)) {
            if (e.getP2().equals("" + (i - 1) + "-" + j)) {
              northOpen = true;
            }
            if (e.getP2().equals("" + i + "-" + (j + 1))) {
              eastOpen = true;
            }
            if (e.getP2().equals("" + (i + 1) + "-" + j)) {
              southOpen = true;
            }
            if (e.getP2().equals("" + i + "-" + (j - 1))) {
              westOpen = true;
            }
          }
          //open the doors for available path (when current location is p2 vertex of edge)
          if (e.getP2().equals("" + i + "-" + j)) {
            if (e.getP1().equals("" + (i - 1) + "-" + j)) {
              northOpen = true;
            }
            if (e.getP1().equals("" + i + "-" + (j + 1))) {
              eastOpen = true;
            }
            if (e.getP1().equals("" + (i + 1) + "-" + j)) {
              southOpen = true;
            }
            if (e.getP1().equals("" + i + "-" + (j - 1))) {
              westOpen = true;
            }
          }
          //for last row and last column edges (wrapping special case)
          if (isWrap && e.getP1().equals("" + i + "-" + j) && ((j == colCount - 1)
                  || (i == rowCount - 1))) {
            if (e.getP2().equals("" + i + "-" + "0")) {
              eastOpen = true;
            }
            if (e.getP2().equals("" + "0" + "-" + j)) {
              southOpen = true;
            }
            if (e.getP2().equals("" + (i - 1) + "-" + j)) {
              northOpen = true;
            }
            if (e.getP2().equals("" + i + "-" + (j - 1))) {
              westOpen = true;
            }
          }
          if (isWrap && e.getP2().equals("" + i + "-" + j) && ((j == colCount - 1)
                  || (i == rowCount - 1))) {
            if (e.getP1().equals("" + i + "-" + "0")) {
              eastOpen = true;
            }
            if (e.getP1().equals("" + "0" + "-" + j)) {
              southOpen = true;
            }
            if (e.getP1().equals("" + (i - 1) + "-" + j)) {
              northOpen = true;
            }
            if (e.getP1().equals("" + i + "-" + (j - 1))) {
              westOpen = true;
            }
          }
          //for first row and first column edges(wrapping special case)
          if (isWrap && e.getP1().equals("" + i + "-" + j) && ((j == 0) || (i == 0))) {
            if (e.getP2().equals("" + i + "-" + (j + 1))) {
              eastOpen = true;
            }
            if (e.getP2().equals("" + (i + 1) + "-" + j)) {
              southOpen = true;
            }
            if (e.getP2().equals("" + (rowCount - 1) + "-" + j)) {
              northOpen = true;
            }
            if (e.getP2().equals("" + i + "-" + (colCount - 1))) {
              westOpen = true;
            }
          }
          if (isWrap && e.getP2().equals("" + i + "-" + j) && ((j == 0) || (i == 0))) {
            if (e.getP1().equals("" + i + "-" + (j + 1))) {
              eastOpen = true;
            }
            if (e.getP1().equals("" + (i + 1) + "-" + j)) {
              southOpen = true;
            }
            if (e.getP1().equals("" + (rowCount - 1) + "-" + j)) {
              northOpen = true;
            }
            if (e.getP1().equals("" + i + "-" + (colCount - 1))) {
              westOpen = true;
            }
          }
        }
        boolean duplicate = false;
        for (Cave c : caves) {
          if (c.getLocation().equals("" + i + "-" + j)) {
            duplicate = true;
            break;
          }
        }
        if (!duplicate) {
          Cave c = new Cave("" + i + "-" + j, northOpen, southOpen, eastOpen, westOpen, r);
          caves.add(c);
        }
      }
    }
  }

  private void setTerminals() {
    int caveIndex = 0;
    int randomLoopCounter = 0;
    boolean terminalsFound = false;
    String startLocation = "";
    String endLocation = "";
    while (!terminalsFound && randomLoopCounter < 50) {
      caveIndex = r.getRandomInt(0, caves.size());
      randomLoopCounter++;
      if (!caves.get(caveIndex).isTunnel()) {
        //set start location
        startLocation = caves.get(caveIndex).getLocation();
        //for all other locations find shortest distance from start location
        for (Cave c : caves) {
          if (!c.isTunnel()) {
            //select an end location
            endLocation = c.getLocation();
            //check that start location is not same as end location
            if (!endLocation.equals(startLocation)) {
              //find shortest path from start to end location
              int movementCounter = bfs(startLocation, endLocation);
              //if shortest path >= 5, then select this end location
              if (movementCounter >= 5) {
                terminalsFound = true;
                break;
              }
            }
          }
        }
      }
    }
    if (terminalsFound) {
      this.startLocation = startLocation;
      this.endLocation = endLocation;
    } else {
      throw new IllegalStateException("No path of length 5 or more can be constructed between "
              + "any of the existing cave locations.");
    }
  }

  private int bfs(String startLocation, String endLocation) {
    //create queue to store node and distance required to reach the node
    Map<String, Integer> bfsQueue = new LinkedHashMap<String, Integer>();

    //create visited hashmap to store the visited nodes
    List<String> visited = new ArrayList<>();

    //add the start node to queue with distance 0
    bfsQueue.put(startLocation, 0);

    //do this while queue is not empty
    while (!bfsQueue.isEmpty()) {
      //get the location of first element in the queue
      String currLoc = bfsQueue.keySet().toArray()[0].toString();

      //get the distance of current node from start location
      int currDistance = bfsQueue.get(currLoc);

      //add the current node to visited list
      visited.add(currLoc);

      //remove the current node from the queue
      bfsQueue.remove(currLoc);

      //find the next locations to traverse from current node
      for (Edge e : updatedEdges) {
        //if current node is same as p1 vertex of edge, and p2 vertex is not yet visited,
        // and p2 vertex is not already added in the queue
        if (e.getP1().equals(currLoc) && !visited.contains(e.getP2())
                && !bfsQueue.containsKey(e.getP2())) {
          //add the p2 vertex to the queue with new distance
          bfsQueue.put(e.getP2(), currDistance + 1);
          //if p2 vertex is the endLocation , then return the current distance required to reach p2
          if (e.getP2().equals(endLocation)) {
            return currDistance + 1;
          }
        }
        //if current node is same as p2 vertex of edge, and p1 vertex is not yet visited,
        // and p1 vertex is not already added in the queue
        if (e.getP2().equals(currLoc) && !visited.contains(e.getP1())
                && !bfsQueue.containsKey(e.getP1())) {
          //add the p1 vertex to the queue with new distance
          bfsQueue.put(e.getP1(), currDistance + 1);
          //if p1 vertex is the endLocation , then return the current distance required to reach p1
          if (e.getP1().equals(endLocation)) {
            return currDistance + 1;
          }
        }
      } //end of for loop
    } //end of while loop
    //if the endlocation was not found then return -1
    return -1;
  }

  private void assignOtyughs() {
    //throw exception if count of Otyugh is greater than no of caves(excluding the start cave)
    if (otyughCount > caveCounter() - 1) {
      throw new IllegalArgumentException("Number of Otyughs cannot be greater than the number "
              + "of caves in the dungeon");
    }

    //initialize the otyugh counter
    int oCounter = otyughCount;

    //create a copy of the caves list
    List<Cave> cavesCopy = new ArrayList<>();
    for (Cave c : caves) {
      cavesCopy.add(c);
    }

    //assign an otyugh to the end cave
    for (Cave c : cavesCopy) {
      if (c.getLocation().equals(endLocation)) {
        //assign a cave location to the otyugh
        Monster o = new Otyugh(c.getLocation(), 2);
        otyughs.add(o);
        //decrease the counter for no of otyughs pending to be assigned to a cave
        oCounter--;
        //remove the cave from cave copy list to ensure that it is not selected again
        cavesCopy.remove(c);
        break;
      }
    }
    //while no of otyughs pending to be assigned to a cave is not 0, do the following
    while (oCounter != 0) {
      //select a random cave index
      int caveIndex = r.getRandomInt(0, caves.size());
      //check if the cave is not a tunnel or start cave and has not been assigned before
      if (!caves.get(caveIndex).isTunnel() && cavesCopy.contains(caves.get(caveIndex))
              && !caves.get(caveIndex).getLocation().equals(startLocation)) {
        //assign a cave location to the otyugh
        Otyugh o = new Otyugh(caves.get(caveIndex).getLocation(), 2);
        otyughs.add(o);
        //decrease the counter for no of otyughs pending to be assigned to a cave
        oCounter--;
        //remove the cave from cave copy list to ensure that it is not selected again
        cavesCopy.remove(caves.get(caveIndex));
      }
    }
  }

  private void assignTreasure() {
    //throw exception if input percentage is negative
    if (pickableItemFrequency < 0 || pickableItemFrequency > 100) {
      throw new IllegalArgumentException("Please enter a valid percentage(Range: 0-100) of caves "
              + "for which treasure is to be assigned");
    }

    //create a copy of the caves list
    List<Cave> cavesCopy = new ArrayList<>();
    for (Cave c : caves) {
      cavesCopy.add(c);
    }
    //calculate the no of caves to which treasure will be assigned
    int treasureCaveCounter = Math.round(pickableItemFrequency * caveCounter() / 100);
    //check that no of caves is greater than 0
    if (treasureCaveCounter > 0) {
      //while no of caves pending to be assigned treasure is not 0, do the following
      while (treasureCaveCounter != 0) {
        //select a random cave index
        int caveIndex = r.getRandomInt(0, caves.size());
        //check if the cave is not a tunnel and has not been assigned treasure before
        if (!caves.get(caveIndex).isTunnel() && cavesCopy.contains(caves.get(caveIndex))) {
          //assign treasure to the cave
          caves.get(caveIndex).assignInitialTreasure();
          //remove the cave from cave copy list to ensure that it is not selected again
          cavesCopy.remove(caves.get(caveIndex));
          //decrease the counter for no of caves pending to be assigned treasure
          treasureCaveCounter--;
        }
      }
    } else {
      throw new IllegalArgumentException("Percentage too low. Please enter a higher percentage "
              + "of caves for which treasure is to be assigned");
    }
  }

  private int caveCounter() {
    int counter = 0;
    for (Cave c : caves) {
      if (!c.isTunnel()) {
        counter++;
      }
    }
    return counter;
  }

  private void assignArrows() {
    //throw exception if input percentage is negative
    if (pickableItemFrequency < 0 || pickableItemFrequency > 100) {
      throw new IllegalArgumentException("Please enter a valid percentage(Range: 0-100) of "
              + "locations to which arrows have to be assigned");
    }
    //create a copy of the caves list
    List<Cave> cavesCopy = new ArrayList<>();
    for (Cave c : caves) {
      cavesCopy.add(c);
    }
    //calculate the no of locations to which arrows will be assigned
    int arrowLocationCounter = Math.round(pickableItemFrequency * rowCount * colCount / 100);
    //check that no of locations is greater than 0
    if (arrowLocationCounter > 0) {
      //while no of locations pending to be assigned arrows is not 0, do the following
      while (arrowLocationCounter != 0) {
        //select a random location index
        int caveIndex = r.getRandomInt(0, caves.size());
        //check that the location has not been assigned arrows before
        if (cavesCopy.contains(caves.get(caveIndex))) {
          //assign arrows to the location
          caves.get(caveIndex).assignInitialArrows();
          //remove the location from cave copy list to ensure that it is not selected again
          cavesCopy.remove(caves.get(caveIndex));
          //decrease the counter for no of caves pending to be assigned treasure
          arrowLocationCounter--;
        }
      }
    } else {
      throw new IllegalArgumentException("Percentage too low. Please enter a higher percentage "
              + "of locations to which arrows have to be assigned");
    }
  }

  @Override
  public void addPlayer(Player p) {
    p.setLocation(startLocation);
  }

  @Override
  public int move(Player p, Direction direction) {
    String[] s = p.getLocation().split("-");
    int i = Integer.parseInt(s[0]);
    int j = Integer.parseInt(s[1]);
    String nextLoc = "";
    boolean invalidMove = true;
    for (Cave c : caves) {
      if (c.getLocation().equals(p.getLocation())) {
        if (direction == Direction.N && c.isMoveNorth()) {
          invalidMove = false;
          if (isWrap && i == 0) {
            i = rowCount;
          }
          nextLoc = "" + (i - 1) + "-" + j;
        } else if (direction == Direction.S && c.isMoveSouth()) {
          invalidMove = false;
          if (isWrap && i == rowCount - 1) {
            i = -1;
          }
          nextLoc = "" + (i + 1) + "-" + j;
        } else if (direction == Direction.W && c.isMoveWest()) {
          invalidMove = false;
          if (isWrap && j == 0) {
            j = colCount;
          }
          nextLoc = "" + i + "-" + (j - 1);
        } else if (direction == Direction.E && c.isMoveEast()) {
          invalidMove = false;
          if (isWrap && j == colCount - 1) {
            j = -1;
          }
          nextLoc = "" + i + "-" + (j + 1);
        }
      }
    }
    if (invalidMove) {
      return -1;
    } else {
      p.setLocation(nextLoc);
      int otyughHealth = getOtyughHealthStatus(nextLoc);
      if (otyughHealth > 0) {
        //if health is 1 then use random else return player is dead
        if (otyughHealth == 1) {
          if (r.getRandomInt(0, 2) == 1) {
            return 0;
          } else {
            return 1;
          }
        } else {
          return 0;
        }
      } else {
        return 1;
      }
    }
  }

  @Override
  public void pickRuby(Player p, int count) {
    for (Cave c : caves) {
      if (c.getLocation().equals(p.getLocation())) {
        if (count <= c.getRubyCount() && count > 0) {
          p.addRuby(count);
          c.updatePickedRubyStatus(count);
        } else {
          throw new IllegalArgumentException("Invalid number of items.");
        }
      }
    }
  }

  @Override
  public void pickDiamond(Player p, int count) {
    for (Cave c : caves) {
      if (c.getLocation().equals(p.getLocation())) {
        if (count <= c.getDiamondCount() && count > 0) {
          p.addDiamond(count);
          c.updatePickedDiamondStatus(count);
        } else {
          throw new IllegalArgumentException("Invalid number of items.");
        }
      }
    }
  }

  @Override
  public void pickSapphire(Player p, int count) {
    for (Cave c : caves) {
      if (c.getLocation().equals(p.getLocation())) {
        if (count <= c.getSapphireCount() && count > 0) {
          p.addSapphire(count);
          c.updatePickedSapphireStatus(count);
        } else {
          throw new IllegalArgumentException("Invalid number of items.");
        }
      }
    }
  }

  @Override
  public void pickArrows(Player p, int count) {
    for (Cave c : caves) {
      if (c.getLocation().equals(p.getLocation())) {
        if (count <= c.getArrowCount() && count > 0) {
          p.addArrows(count);
          c.updatePickedArrowStatus(count);
        } else {
          throw new IllegalArgumentException("Invalid number of items.");
        }
      }
    }
  }

  @Override
  public int shootArrow(Player p, Direction dir, int distance) {
    if (distance < 1 || distance > 5) {
      throw new IllegalArgumentException("Enter a valid distance.");
    }
    if (p.getArrowCount() <= 0) {
      return -1;
    }
    String currLoc = p.getLocation();
    String nextLoc = "";
    String[] s = currLoc.split("-");
    int curr_i = Integer.parseInt(s[0]);
    int curr_j = Integer.parseInt(s[1]);
    //move arrow from current location to next location based on direction
    for (Cave c : caves) {
      if (c.getLocation().equals(currLoc)) {
        if (dir == Direction.N && c.isMoveNorth()) {
          int i = curr_i;
          int j = curr_j;
          if (isWrap && curr_i == 0) {
            i = rowCount;
          }
          nextLoc = "" + (i - 1) + "-" + j;
        } else if (dir == Direction.S && c.isMoveSouth()) {
          int i = curr_i;
          int j = curr_j;
          if (isWrap && curr_i == rowCount - 1) {
            i = -1;
          }
          nextLoc = "" + (i + 1) + "-" + j;
        } else if (dir == Direction.W && c.isMoveWest()) {
          int i = curr_i;
          int j = curr_j;
          if (isWrap && curr_j == 0) {
            j = colCount;
          }
          nextLoc = "" + i + "-" + (j - 1);
        } else if (dir == Direction.E && c.isMoveEast()) {
          int i = curr_i;
          int j = curr_j;
          if (isWrap && curr_j == colCount - 1) {
            j = -1;
          }
          nextLoc = "" + i + "-" + (j + 1);
        }
        break;
      }
    }
    //if next location is not found in first step then the direction entered was invalid
    if (nextLoc.equals("")) {
      throw new IllegalArgumentException("Enter a valid direction.");
    }
    //if next location is found find the subsequent locations based on the distance entered
    else {
      for (Cave c : caves) {
        if (c.getLocation().equals(nextLoc) && !c.isTunnel()) {
          distance--;
        }
      }
      //use getNextDirection to get the next location for the arrow
      while (distance != 0) {
        String prevLoc = currLoc;
        currLoc = nextLoc;
        nextLoc = getNextLocation(prevLoc, currLoc);
        if (nextLoc.equals("-1--1")) {
          break;
        }
        for (Cave c : caves) {
          if (c.getLocation().equals(nextLoc) && !c.isTunnel()) {
            distance--;
          }
        }
      }
      p.deductArrow();
      //set the last found location as current location
      currLoc = nextLoc;
      //check if an otyugh exists at this location and act accordingly
      int otyughHealth = getOtyughHealthStatus(currLoc);
      //if otyugh's health level is 2, then it indicates 1st hit
      if (otyughHealth == 2) {
        slayOtyugh(currLoc);
        return 1;
      }
      //if otyugh's health level is 1, then it indicates 2nd hit
      else if (otyughHealth == 1) {
        slayOtyugh(currLoc);
        return 2;
      }
      //else there is no otyugh in this cave
      else {
        return 0;
      }
    }
  }

  private String getNextLocation(String prevLoc, String currLoc) {
    String[] s = prevLoc.split("-");
    int prev_i = Integer.parseInt(s[0]);
    int prev_j = Integer.parseInt(s[1]);

    s = currLoc.split("-");
    int curr_i = Integer.parseInt(s[0]);
    int curr_j = Integer.parseInt(s[1]);

    int next_i = -1;
    int next_j = -1;

    for (Cave c : caves) {
      //when current location is a cave
      if (c.getLocation().equals(currLoc) && !c.isTunnel()) {
        //prev location was east of curr location, so next location will be west of curr location
        // OR
        //prev location(first column) was east of curr location(last column), so next location
        //will be west of curr location(second last column)
        if ((curr_i == prev_i && curr_j == prev_j - 1 && c.isMoveWest())
                || (isWrap && curr_i == prev_i && curr_j == colCount - 1 && prev_j == 0
                && c.isMoveWest())) {
          if (isWrap && curr_j == 0) {
            curr_j = colCount;
          }
          next_i = curr_i;
          next_j = curr_j - 1;
        }
        //prev location was west of curr location, so next location will be east of curr location
        //OR
        //prev location(last column) was west of curr location(first column), so next location
        //will be east of curr location(second column)
        else if ((curr_i == prev_i && curr_j == prev_j + 1 && c.isMoveEast())
                || (isWrap && curr_i == prev_i && curr_j == 0 && prev_j == colCount - 1
                && c.isMoveEast())) {
          if (isWrap && curr_j == colCount - 1) {
            curr_j = -1;
          }
          next_i = curr_i;
          next_j = curr_j + 1;
        }
        //prev location was south of curr location, so next location will be north of curr location
        //OR
        //prev location(first row) was south of curr location(last row), so next location
        //will be north of curr location(second last row)
        else if ((curr_i == prev_i - 1 && curr_j == prev_j && c.isMoveNorth())
                || (isWrap && prev_i == 0 && curr_i == rowCount - 1 && curr_j == curr_j
                && c.isMoveNorth())) {
          if (isWrap && curr_i == 0) {
            curr_i = rowCount;
          }
          next_i = curr_i - 1;
          next_j = curr_j;
        }
        //prev location was north of curr location, so next location will be south of curr location
        //OR
        //prev location(last row) was north of curr location(first row), so next location
        //will be south of curr location(second row)
        else if ((curr_i == prev_i + 1 && curr_j == prev_j && c.isMoveSouth())
                || (isWrap && prev_i == rowCount - 1 && curr_i == 0 && curr_j == prev_j
                && c.isMoveSouth())) {
          if (isWrap && curr_i == rowCount - 1) {
            curr_i = -1;
          }
          next_i = curr_i + 1;
          next_j = curr_j;
        }
      }
      //when current location is a tunnel
      if (c.getLocation().equals(currLoc) && c.isTunnel()) {
        for (Edge e : updatedEdges) {
          //if P1 vertex of edge is current location and p2 vertex of edge is not the previous
          //location, then p2 is the next location
          if (e.getP1().equals(currLoc) && !e.getP2().equals(prevLoc)) {
            s = e.getP2().split("-");
            next_i = Integer.parseInt(s[0]);
            next_j = Integer.parseInt(s[1]);
            break;
          }
          //if P2 vertex of edge is current location and p1 vertex of edge is not the previous
          //location, then p1 is the next location
          else if (e.getP2().equals(currLoc) && !e.getP1().equals(prevLoc)) {
            s = e.getP1().split("-");
            next_i = Integer.parseInt(s[0]);
            next_j = Integer.parseInt(s[1]);
            break;
          }
        }
      }
    }
    return "" + next_i + "-" + next_j;
  }

  private void slayOtyugh(String loc) {
    for (Monster o : otyughs) {
      if (o.getLocation().equals(loc)) {
        o.arrowStrike();
        break;
      }
    }
  }

  @Override
  public String getLocationSmell(String loc) {
    String smell = "";
    if (loc == null) {
      throw new IllegalArgumentException("Location cannot be null");
    }

    //check for immediate neighbouring locations
    int immediateCount = getNeighbouringOtyughCount(loc);

    if (immediateCount > 0) {
      return "Terrible";
    } else {
      //check for next neighbours of immediate neighbours
      int otyughCounter = 0;

      String[] s = loc.split("-");
      int curr_i = Integer.parseInt(s[0]);
      int curr_j = Integer.parseInt(s[1]);
      String nextLoc;
      for (Cave c : caves) {
        if (c.getLocation().equals(loc)) {
          if (c.isMoveNorth()) {
            int i = curr_i;
            int j = curr_j;
            if (isWrap && curr_i == 0) {
              i = rowCount;
            }
            nextLoc = "" + (i - 1) + "-" + j;
            otyughCounter += getNeighbouringOtyughCount(nextLoc);
          }
          if (c.isMoveSouth()) {
            int i = curr_i;
            int j = curr_j;
            if (isWrap && curr_i == rowCount - 1) {
              i = -1;
            }
            nextLoc = "" + (i + 1) + "-" + j;
            otyughCounter += getNeighbouringOtyughCount(nextLoc);
          }
          if (c.isMoveWest()) {
            int i = curr_i;
            int j = curr_j;
            if (isWrap && curr_j == 0) {
              j = colCount;
            }
            nextLoc = "" + i + "-" + (j - 1);
            otyughCounter += getNeighbouringOtyughCount(nextLoc);
          }
          if (c.isMoveEast()) {
            int i = curr_i;
            int j = curr_j;
            if (isWrap && curr_j == colCount - 1) {
              j = -1;
            }
            nextLoc = "" + i + "-" + (j + 1);
            otyughCounter += getNeighbouringOtyughCount(nextLoc);
          }
          break;
        }
      }
      if (otyughCounter > 1) {
        smell = "Terrible";
      } else if (otyughCounter == 1) {
        smell = "Bad";
      }
    }
    return smell;
  }

  private int getNeighbouringOtyughCount(String location) {
    String[] s = location.split("-");

    int curr_i = Integer.parseInt(s[0]);
    int curr_j = Integer.parseInt(s[1]);
    String nextLoc;
    int otyughCounter = 0;

    for (Cave c : caves) {
      if (c.getLocation().equals(location)) {
        if (c.isMoveNorth()) {
          int i = curr_i;
          int j = curr_j;
          if (isWrap && curr_i == 0) {
            i = rowCount;
          }
          nextLoc = "" + (i - 1) + "-" + j;
          if (getOtyughHealthStatus(nextLoc) > 0) {
            otyughCounter++;
          }
        }
        if (c.isMoveSouth()) {
          int i = curr_i;
          int j = curr_j;
          if (isWrap && curr_i == rowCount - 1) {
            i = -1;
          }
          nextLoc = "" + (i + 1) + "-" + j;
          if (getOtyughHealthStatus(nextLoc) > 0) {
            otyughCounter++;
          }
        }
        if (c.isMoveWest()) {
          int i = curr_i;
          int j = curr_j;
          if (isWrap && curr_j == 0) {
            j = colCount;
          }
          nextLoc = "" + i + "-" + (j - 1);
          if (getOtyughHealthStatus(nextLoc) > 0) {
            otyughCounter++;
          }
        }
        if (c.isMoveEast()) {
          int i = curr_i;
          int j = curr_j;
          if (isWrap && curr_j == colCount - 1) {
            j = -1;
          }
          nextLoc = "" + i + "-" + (j + 1);
          if (getOtyughHealthStatus(nextLoc) > 0) {
            otyughCounter++;
          }
        }
        break;
      }
    }
    return otyughCounter;
  }

  private int getOtyughHealthStatus(String location) {
    for (Monster o : otyughs) {
      if (o.getLocation().equals(location)) {
        return o.getHealth();
      }
    }
    return 0;
  }

  @Override
  public String getNextPossibleMoves(String loc) {
    String moves = "";
    for (Cave c : caves) {
      if (c.getLocation().equals(loc)) {
        if (c.isMoveNorth() && !moves.contains("N")) {
          moves += "N ";
        }
        if (c.isMoveSouth() && !moves.contains("S")) {
          moves += "S ";
        }
        if (c.isMoveEast() && !moves.contains("E")) {
          moves += "E ";
        }
        if (c.isMoveWest() && !moves.contains("W")) {
          moves += "W ";
        }
      }
    }
    return moves;
  }

  @Override
  public String getEndLocation() {
    return endLocation;
  }

  @Override
  public List<Cave> getCaves() {
    return caves;
  }

  @Override
  public List<Monster> getOtyughs() {
    return otyughs;
  }

  @Override
  public List<Edge> getEdges() {
    return updatedEdges;
  }

  @Override
  public String getStartLocation() {
    return startLocation;
  }

  @Override
  public String getLocationType(String loc) {
    if (loc == null) {
      throw new IllegalArgumentException("Location cannot be null");
    }
    for (Cave c : caves) {
      if (c.getLocation().equals(loc)) {
        if (c.isTunnel()) {
          return "Tunnel";
        } else {
          return "Cave";
        }
      }
    }
    return "Location Not Found. Please try entering a valid location";
  }
}
