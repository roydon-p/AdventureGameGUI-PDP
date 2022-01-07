package dungeon;

import randoms.Randomizer;

import java.util.ArrayList;
import java.util.List;

/**
 * DungeonGame represents the class that will perform all the operations in the dungeon
 * game. This model is responsible for creating the dungeon, otyughs and the player. It adds
 * the player to the dungeon and provides the interactivity between the controller and other
 * concrete classes in the model to move the player through the dungeon collecting treasure
 * and slaying the otyughs.
 */
public class DungeonGame implements GameModel, GameModelReadOnly {
  private Dungeon d;
  private final Dungeon copyD;
  private Player p;
  private boolean playerDead = false;

  /**
   * Creates an instance of a game that will create the dungeon.
   *
   * @param rowCount                the no. of rows in the dungeon grid
   * @param colCount                the no. of columns in the dungeon grid
   * @param degOfInterconnectivity  the no of paths that can be added to the kruskals output
   *                                minimum spanning tree.
   * @param isWrap                  true if the dungeon has paths wrapping type
   * @param percentCavesForTreasure the percentage of caves to which treasure is to be assigned.
   *                                Also indicates the percentage of locations to which arrows will
   *                                be assigned.
   * @param r                       the randomizer object
   * @param monsterCount            indicates the difficulty level of the game based on the
   *                                number of otyughs present in the dungeon.
   */
  public DungeonGame(int rowCount, int colCount, int degOfInterconnectivity, boolean isWrap,
                     int percentCavesForTreasure, Randomizer r, int monsterCount) {
    this.d = new MasterDungeon(rowCount, colCount, degOfInterconnectivity, isWrap,
            percentCavesForTreasure, r, monsterCount);
    this.copyD = new MasterDungeon((MasterDungeon) this.d);
  }

  @Override
  public void refresh() {
    this.d = new MasterDungeon((MasterDungeon) this.copyD);
    this.p = null;
    playerDead = false;
  }

  @Override
  public void createPlayer() {
    if (this.p == null) {
      p = new MasterDungeonPlayer(0, 0, 0, "",
              3);
    } else {
      throw new IllegalStateException("Player is already created for this game.");
    }
  }

  @Override
  public void addPlayerInDungeon() {
    if (this.p == null) {
      throw new IllegalStateException("Player does not exist for this game.");
    }
    d.addPlayer(p);
  }

  @Override
  public boolean isPlayerDead() {
    return playerDead;
  }

  @Override
  public boolean isGameOver() {
    if (this.p == null) {
      throw new IllegalStateException("Player does not exist for this game.");
    }
    return p.getLocation().equals(d.getEndLocation()) || playerDead;
  }

  @Override
  public String getNextPossibleMoves(String loc) {
    if (this.p == null) {
      throw new IllegalStateException("Player does not exist for this game.");
    }
    return d.getNextPossibleMoves(loc);
  }

  @Override
  public String getPlayerDescription() {
    if (this.p == null) {
      throw new IllegalStateException("Player does not exist for this game.");
    }
    return String.format("Diamonds- %d | Sapphires- %d | Rubies- %d | Arrows- %d",
            p.getDiamondCount(), p.getSapphireCount(), p.getRubyCount(), p.getArrowCount());
  }

  @Override
  public int getRubyCount(String loc) {
    if (this.p == null) {
      throw new IllegalStateException("Player does not exist for this game.");
    }
    for (Cave c : d.getCaves()) {
      if (c.getLocation().equals(loc)) {
        return c.getRubyCount();
      }
    }
    return 0;
  }

  @Override
  public int getDiamondCount(String loc) {
    if (this.p == null) {
      throw new IllegalStateException("Player does not exist for this game.");
    }
    for (Cave c : d.getCaves()) {
      if (c.getLocation().equals(loc)) {
        return c.getDiamondCount();
      }
    }
    return 0;
  }

  @Override
  public int getSapphireCount(String loc) {
    if (this.p == null) {
      throw new IllegalStateException("Player does not exist for this game.");
    }
    for (Cave c : d.getCaves()) {
      if (c.getLocation().equals(loc)) {
        return c.getSapphireCount();
      }
    }
    return 0;
  }

  @Override
  public int getArrowCount(String loc) {
    if (this.p == null) {
      throw new IllegalStateException("Player does not exist for this game.");
    }
    for (Cave c : d.getCaves()) {
      if (c.getLocation().equals(loc)) {
        return c.getArrowCount();
      }
    }
    return 0;
  }

  @Override
  public String getLocationSmell(String loc) {
    if (this.p == null) {
      throw new IllegalStateException("Player does not exist for this game.");
    }
    return d.getLocationSmell(loc);
  }

  @Override
  public String getAvailableItems(String loc) {
    String s = "";

    if (getDiamondCount(loc) > 0) {
      s += "Diamonds- " + getDiamondCount(loc) + " | ";
    }
    if (getSapphireCount(loc) > 0) {
      s += "Sapphires- " + getSapphireCount(loc) + " | ";
    }
    if (getRubyCount(loc) > 0) {
      s += "Rubies- " + getRubyCount(loc) + " | ";
    }
    if (getArrowCount(loc) > 0) {
      s += "Arrows- " + getArrowCount(loc) + " | ";
    }
    return s;
  }

  @Override
  public boolean movePlayerTo(String direction) {
    if (this.p == null) {
      throw new IllegalStateException("Player does not exist for this game.");
    }
    Direction dir = Direction.valueOf(direction.toUpperCase());
    int moveResult = d.move(p, dir);
    if (moveResult == -1) {
      throw new IllegalArgumentException("Invalid move");
    } else if (moveResult == 0) {
      playerDead = true;
      return false;
    }
    return true;
  }

  @Override
  public void pickItemAtLocation(String item, int count) {
    boolean throwE = true;
    if (this.p == null) {
      throw new IllegalStateException("Player does not exist for this game.");
    }
    String loc = p.getLocation();
    switch (item) {
      case "R":
      case "r":
        d.pickRuby(p, count);
        break;
      case "D":
      case "d":
        d.pickDiamond(p, count);
        break;
      case "S":
      case "s":
        d.pickSapphire(p, count);
        break;
      case "A":
      case "a":
        d.pickArrows(p, count);
        break;
      case "AllTreasure":
        try {
          d.pickRuby(p, getRubyCount(loc));
          throwE = false;
        } catch (IllegalArgumentException e) {
          //common exception thrown after all pick operations are done
        }
        try {
          d.pickDiamond(p, getDiamondCount(loc));
          throwE = false;
        } catch (IllegalArgumentException e) {
          //common exception thrown after all pick operations are done
        }
        try {
          d.pickSapphire(p, getSapphireCount(loc));
          throwE = false;
        } catch (IllegalArgumentException e) {
          //common exception thrown after all pick operations are done
        }
        if (throwE) {
          throw new IllegalArgumentException("Invalid Count");
        }
        break;
      case "AllArrows":
        d.pickArrows(p, getArrowCount(loc));
        break;
      default:
        throw new IllegalArgumentException("Invalid choice of item.");
    }
  }

  @Override
  public int shootArrow(String direction, int distance) {
    if (this.p == null) {
      throw new IllegalStateException("Player does not exist for this game.");
    }
    Direction dir = Direction.valueOf(direction.toUpperCase());
    return d.shootArrow(p, dir, distance);
  }

  @Override
  public String getStartLocation() {
    return d.getStartLocation();
  }

  @Override
  public String getEndLocation() {
    return d.getEndLocation();
  }

  @Override
  public List<String> getEdges() {
    List<String> dungeonPaths = new ArrayList<>();
    for (Edge e : d.getEdges()) {
      String s = e.toString();
      dungeonPaths.add(s);
    }
    return dungeonPaths;
  }

  @Override
  public String getPlayerLocation() {
    if (this.p == null) {
      throw new IllegalStateException("Player does not exist for this game.");
    }
    return p.getLocation();
  }

  @Override
  public List<String> getAllVisitedLocations() {
    if (this.p == null) {
      throw new IllegalStateException("Player does not exist for this game.");
    }
    return p.getVisitedLocations();
  }

  @Override
  public String getLocationType(String loc) {
    return d.getLocationType(loc);
  }

  @Override
  public List<String> getOtyughLocations() {
    List<String> otyughLocations = new ArrayList<>();
    for (Monster o : d.getOtyughs()) {
      String s = o.getLocation();
      if (o.getHealth() > 0) {
        otyughLocations.add(s);
      }
    }
    return otyughLocations;
  }
}
