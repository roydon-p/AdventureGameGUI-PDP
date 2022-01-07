import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dungeon.DungeonGame;
import dungeon.GameModel;
import randoms.RandomGenerator;
import randoms.RandomGeneratorDummy;
import randoms.Randomizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Class for testing the Game Model.
 */
public class GameModelTest {

  @Test
  public void testNonWrapMonsterWins() {
    Randomizer r = new RandomGeneratorDummy();
    List<Integer> randomValues = new ArrayList<>();

    int[] kruskals = {17, 3, 8, 2, 4, 16, 0, 14, 4, 0, 5, 7, 6, 6, 2, 2, 7, 3, 2, 1, 1, 1, 0, 0};
    int[] interconnectivity = {4, 7, 5};
    int[] terminal = {0, 7};
    int[] monsters = {14, 3, 1, 4, 9};

    for (int i = 0; i < kruskals.length; i++) {
      randomValues.add(kruskals[i]);
    }
    for (int i = 0; i < interconnectivity.length; i++) {
      randomValues.add(interconnectivity[i]);
    }
    for (int i = 0; i < terminal.length; i++) {
      randomValues.add(terminal[i]);
    }
    for (int i = 0; i < monsters.length; i++) {
      randomValues.add(monsters[i]);
    }
    int[] treasure = {11, 0, 2, 3, 0, 4, 5, 6, 2, 7, 8, 9, 10, 1, 2, 3};
    for (int i = 0; i < treasure.length; i++) {
      randomValues.add(treasure[i]);
    }

    int[] arrows = {11, 2, 3, 1, 5, 2, 7, 1, 0, 3, 0, 1, 12, 2};
    for (int i = 0; i < arrows.length; i++) {
      randomValues.add(arrows[i]);
    }
    r.setDummyRandomValue(randomValues);

    GameModel g = new DungeonGame(4, 4, 3,
            false, 40, r, 2);

    //test for start and end location
    assertEquals("1-3", g.getStartLocation());
    assertEquals("3-0", g.getEndLocation());

    //bfsHelper method returns distance between the caves
    int result = bfsHelper(g.getStartLocation(), g.getEndLocation(), g.getEdges());
    //test min distance between start and end location is 5
    assertTrue(result >= 5);
    g.createPlayer();
    g.addPlayerInDungeon();

    //test that initially the player is at start location
    assertEquals(g.getPlayerLocation(), g.getStartLocation());

    //test that initially player has 3 arrows
    String playerDesc = "Diamonds- 0 | Sapphires- 0 | Rubies- 0 | Arrows- 3";
    assertEquals(playerDesc, g.getPlayerDescription());

    //test that player picks up an arrow successfully
    //before-location has 1 arrow
    String ploc = g.getPlayerLocation();
    String actual = String.format("Diamonds- %d | Sapphires- %d | Rubies- %d | Arrows- %d",
            g.getDiamondCount(ploc), g.getSapphireCount(ploc), g.getRubyCount(ploc),
            g.getArrowCount(ploc));
    assertEquals("Diamonds- 0 | Sapphires- 0 | Rubies- 0 | Arrows- 1", actual);
    //before-player has 3 arrows
    assertTrue(g.getPlayerDescription().contains("Diamonds- 0 | Sapphires- 0 |"
            + " Rubies- 0 | Arrows- 3"));
    //player picks 1 arrow
    g.pickItemAtLocation("A", 1);

    //after-location has 0 arrows
    ploc = g.getPlayerLocation();
    actual = String.format("Diamonds- %d | Sapphires- %d | Rubies- %d | Arrows- %d",
            g.getDiamondCount(ploc), g.getSapphireCount(ploc), g.getRubyCount(ploc),
            g.getArrowCount(ploc));
    assertEquals("Diamonds- 0 | Sapphires- 0 | Rubies- 0 | Arrows- 0", actual);
    //after-player has 4 arrows
    assertTrue(g.getPlayerDescription().contains("Diamonds- 0 | Sapphires- 0 |"
            + " Rubies- 0 | Arrows- 4"));

    //test player moves to south
    assertEquals("1-3", g.getPlayerLocation());
    g.movePlayerTo("S");
    assertEquals("2-3", g.getPlayerLocation());

    //test Bad smell for Otyugh 2 position away
    assertEquals("Bad", g.getLocationSmell(g.getPlayerLocation()));

    //test player moves to west
    assertEquals("2-3", g.getPlayerLocation());
    g.movePlayerTo("W");
    assertEquals("2-2", g.getPlayerLocation());

    //test Terrible smell for Otyugh 1 position away
    assertEquals("Terrible", g.getLocationSmell(g.getPlayerLocation()));

    //test shoot-arrow misses the monster
    assertEquals(0, g.shootArrow("W", 2));
    //test shoot-arrow strikes the monster 1st time
    assertEquals(1, g.shootArrow("W", 1));
    //test shoot-arrow kills the monster
    assertEquals(2, g.shootArrow("W", 1));

    //test No smell for dead otyugh
    assertEquals("", g.getLocationSmell(g.getPlayerLocation()));

    //test arrow count decreased by 3
    assertTrue(g.getPlayerDescription().contains("Diamonds- 0 | Sapphires- 0 |"
            + " Rubies- 0 | Arrows- 1"));

    //test 1 diamond pickup
    //before-location has 1 diamond
    ploc = g.getPlayerLocation();
    actual = String.format("Diamonds- %d | Sapphires- %d | Rubies- %d | Arrows- %d",
            g.getDiamondCount(ploc), g.getSapphireCount(ploc), g.getRubyCount(ploc),
            g.getArrowCount(ploc));
    assertEquals("Diamonds- 1 | Sapphires- 2 | Rubies- 3 | Arrows- 0", actual);
    //before-player has 0 diamonds
    assertTrue(g.getPlayerDescription().contains("Diamonds- 0 | Sapphires- 0 |"
            + " Rubies- 0 | Arrows- 1"));
    //player picks 1 diamond
    g.pickItemAtLocation("D", 1);
    //after-location has 0 diamonds
    ploc = g.getPlayerLocation();
    actual = String.format("Diamonds- %d | Sapphires- %d | Rubies- %d | Arrows- %d",
            g.getDiamondCount(ploc), g.getSapphireCount(ploc), g.getRubyCount(ploc),
            g.getArrowCount(ploc));
    assertEquals("Diamonds- 0 | Sapphires- 2 | Rubies- 3 | Arrows- 0", actual);
    //after-player has 1 diamond
    assertTrue(g.getPlayerDescription().contains("Diamonds- 1 | Sapphires- 0 |"
            + " Rubies- 0 | Arrows- 1"));


    //test 1 sapphire pickup
    //before-location has 2 sapphires
    ploc = g.getPlayerLocation();
    actual = String.format("Diamonds- %d | Sapphires- %d | Rubies- %d | Arrows- %d",
            g.getDiamondCount(ploc), g.getSapphireCount(ploc), g.getRubyCount(ploc),
            g.getArrowCount(ploc));
    assertEquals("Diamonds- 0 | Sapphires- 2 | Rubies- 3 | Arrows- 0", actual);
    //before-player has 0 sapphires
    assertTrue(g.getPlayerDescription().contains("Diamonds- 1 | Sapphires- 0 |"
            + " Rubies- 0 | Arrows- 1"));
    //player picks 1 sapphire
    g.pickItemAtLocation("S", 1);
    //after-location has 1 sapphire
    ploc = g.getPlayerLocation();
    actual = String.format("Diamonds- %d | Sapphires- %d | Rubies- %d | Arrows- %d",
            g.getDiamondCount(ploc), g.getSapphireCount(ploc), g.getRubyCount(ploc),
            g.getArrowCount(ploc));
    assertEquals("Diamonds- 0 | Sapphires- 1 | Rubies- 3 | Arrows- 0", actual);
    //after-player has 1 sapphire
    assertTrue(g.getPlayerDescription().contains("Diamonds- 1 | Sapphires- 1 |"
            + " Rubies- 0 | Arrows- 1"));


    //test 2 ruby pickup
    //before-location has 3 rubies
    ploc = g.getPlayerLocation();
    actual = String.format("Diamonds- %d | Sapphires- %d | Rubies- %d | Arrows- %d",
            g.getDiamondCount(ploc), g.getSapphireCount(ploc), g.getRubyCount(ploc),
            g.getArrowCount(ploc));
    assertEquals("Diamonds- 0 | Sapphires- 1 | Rubies- 3 | Arrows- 0", actual);
    //before-player has 0 rubies
    assertTrue(g.getPlayerDescription().contains("Diamonds- 1 | Sapphires- 1 |"
            + " Rubies- 0 | Arrows- 1"));
    //player picks 2 rubies
    g.pickItemAtLocation("R", 2);
    //after-location has 1 rubies
    ploc = g.getPlayerLocation();
    actual = String.format("Diamonds- %d | Sapphires- %d | Rubies- %d | Arrows- %d",
            g.getDiamondCount(ploc), g.getSapphireCount(ploc), g.getRubyCount(ploc),
            g.getArrowCount(ploc));
    assertEquals("Diamonds- 0 | Sapphires- 1 | Rubies- 1 | Arrows- 0", actual);
    //after-player has 2 rubies
    assertTrue(g.getPlayerDescription().contains("Diamonds- 1 | Sapphires- 1 |"
            + " Rubies- 2 | Arrows- 1"));

    //test player moves to east
    assertEquals("2-2", g.getPlayerLocation());
    g.movePlayerTo("E");
    assertEquals("2-3", g.getPlayerLocation());

    //test player moves to north
    assertEquals("2-3", g.getPlayerLocation());
    g.movePlayerTo("N");
    assertEquals("1-3", g.getPlayerLocation());

    g.movePlayerTo("S");
    g.movePlayerTo("W");
    g.movePlayerTo("S");

    int[] monsterKills = {0, 1};
    for (int i = 0; i < monsterKills.length; i++) {
      randomValues.add(monsterKills[i]);
    }
    r.setDummyRandomValue(randomValues);

    //test Bad smell for Otyugh 2 position away
    assertEquals("Bad", g.getLocationSmell(g.getPlayerLocation()));

    //test Terrible smell for Otyugh 1 position away
    g.movePlayerTo("W");
    assertEquals("Terrible", g.getLocationSmell(g.getPlayerLocation()));

    //test shoot-arrow strikes the monster 1st time
    assertEquals(1, g.shootArrow("W", 1));

    //test injured monster does not eat the player
    g.movePlayerTo("W");
    assertFalse(g.isPlayerDead());

    g.movePlayerTo("E");
    g.movePlayerTo("W");
    g.movePlayerTo("E");

    //test injured monster eats the player
    g.movePlayerTo("W");
    assertTrue(g.isPlayerDead());

    //test game over when monster eats the player
    assertTrue(g.isGameOver());
  }

  @Test
  public void testNonWrapPlayerWins() {
    Randomizer r = new RandomGeneratorDummy();
    List<Integer> randomValues = new ArrayList<>();

    int[] kruskals = {17, 3, 8, 2, 4, 16, 0, 14, 4, 0, 5, 7, 6, 6, 2, 2, 7, 3, 2, 1, 1, 1, 0, 0};
    int[] interconnectivity = {4, 7, 5};
    int[] terminal = {0, 7};
    int[] monsters = {14, 3, 1, 4, 9};

    for (int i = 0; i < kruskals.length; i++) {
      randomValues.add(kruskals[i]);
    }
    for (int i = 0; i < interconnectivity.length; i++) {
      randomValues.add(interconnectivity[i]);
    }
    for (int i = 0; i < terminal.length; i++) {
      randomValues.add(terminal[i]);
    }
    for (int i = 0; i < monsters.length; i++) {
      randomValues.add(monsters[i]);
    }
    int[] treasure = {11, 0, 2, 3, 0, 4, 5, 6, 2, 7, 8, 9, 10, 1, 2, 3};
    for (int i = 0; i < treasure.length; i++) {
      randomValues.add(treasure[i]);
    }

    int[] arrows = {11, 2, 3, 1, 5, 2, 7, 1, 0, 3, 0, 1, 12, 2};
    for (int i = 0; i < arrows.length; i++) {
      randomValues.add(arrows[i]);
    }
    r.setDummyRandomValue(randomValues);

    GameModel g = new DungeonGame(4, 4, 3,
            false, 40, r, 2);

    //test for start and end location
    assertEquals("1-3", g.getStartLocation());
    assertEquals("3-0", g.getEndLocation());

    //bfsHelper method returns distance between the caves
    int result = bfsHelper(g.getStartLocation(), g.getEndLocation(), g.getEdges());
    //test min distance between start and end location is 5
    assertTrue(result >= 5);
    g.createPlayer();
    g.addPlayerInDungeon();

    //test that initially the player is at start location
    assertEquals(g.getPlayerLocation(), g.getStartLocation());

    //test that initially player has 3 arrows
    String playerDesc = "Diamonds- 0 | Sapphires- 0 | Rubies- 0 | Arrows- 3";
    assertEquals(playerDesc, g.getPlayerDescription());

    //test that player picks up an arrow successfully
    //before-location has 1 arrow
    String ploc = g.getPlayerLocation();
    String actual = String.format("Diamonds- %d | Sapphires- %d | Rubies- %d | Arrows- %d",
            g.getDiamondCount(ploc), g.getSapphireCount(ploc), g.getRubyCount(ploc),
            g.getArrowCount(ploc));
    assertEquals("Diamonds- 0 | Sapphires- 0 | Rubies- 0 | Arrows- 1", actual);
    //before-player has 3 arrows
    assertTrue(g.getPlayerDescription().contains("Diamonds- 0 | Sapphires- 0 |"
            + " Rubies- 0 | Arrows- 3"));
    //player picks 1 arrow
    g.pickItemAtLocation("A", 1);

    //after-location has 0 arrows
    ploc = g.getPlayerLocation();
    actual = String.format("Diamonds- %d | Sapphires- %d | Rubies- %d | Arrows- %d",
            g.getDiamondCount(ploc), g.getSapphireCount(ploc), g.getRubyCount(ploc),
            g.getArrowCount(ploc));
    assertEquals("Diamonds- 0 | Sapphires- 0 | Rubies- 0 | Arrows- 0", actual);
    //after-player has 4 arrows
    assertTrue(g.getPlayerDescription().contains("Diamonds- 0 | Sapphires- 0 |"
            + " Rubies- 0 | Arrows- 4"));

    //test player moves to south
    assertEquals("1-3", g.getPlayerLocation());
    g.movePlayerTo("S");
    assertEquals("2-3", g.getPlayerLocation());

    //test Bad smell for Otyugh 2 position away
    assertEquals("Bad", g.getLocationSmell(g.getPlayerLocation()));

    //test Terrible smell for Otyugh 1 position away
    g.movePlayerTo("W");
    assertEquals("Terrible", g.getLocationSmell(g.getPlayerLocation()));

    //test shoot-arrow misses the monster
    assertEquals(0, g.shootArrow("W", 2));
    //test shoot-arrow strikes the monster 1st time
    assertEquals(1, g.shootArrow("W", 1));
    //test shoot-arrow kills the monster
    assertEquals(2, g.shootArrow("W", 1));

    //test No smell for dead otyugh
    assertEquals("", g.getLocationSmell(g.getPlayerLocation()));

    //test arrow count decreased by 3
    assertTrue(g.getPlayerDescription().contains("Diamonds- 0 | Sapphires- 0 |"
            + " Rubies- 0 | Arrows- 1"));

    //test 1 diamond pickup
    //before-location has 1 diamond
    ploc = g.getPlayerLocation();
    actual = String.format("Diamonds- %d | Sapphires- %d | Rubies- %d | Arrows- %d",
            g.getDiamondCount(ploc), g.getSapphireCount(ploc), g.getRubyCount(ploc),
            g.getArrowCount(ploc));
    assertEquals("Diamonds- 1 | Sapphires- 2 | Rubies- 3 | Arrows- 0", actual);
    //before-player has 0 diamonds
    assertTrue(g.getPlayerDescription().contains("Diamonds- 0 | Sapphires- 0 |"
            + " Rubies- 0 | Arrows- 1"));
    //player picks 1 diamond
    g.pickItemAtLocation("D", 1);
    //after-location has 0 diamonds
    ploc = g.getPlayerLocation();
    actual = String.format("Diamonds- %d | Sapphires- %d | Rubies- %d | Arrows- %d",
            g.getDiamondCount(ploc), g.getSapphireCount(ploc), g.getRubyCount(ploc),
            g.getArrowCount(ploc));
    assertEquals("Diamonds- 0 | Sapphires- 2 | Rubies- 3 | Arrows- 0", actual);
    //after-player has 1 diamond
    assertTrue(g.getPlayerDescription().contains("Diamonds- 1 | Sapphires- 0 |"
            + " Rubies- 0 | Arrows- 1"));


    //test 1 sapphire pickup
    //before-location has 2 sapphires
    ploc = g.getPlayerLocation();
    actual = String.format("Diamonds- %d | Sapphires- %d | Rubies- %d | Arrows- %d",
            g.getDiamondCount(ploc), g.getSapphireCount(ploc), g.getRubyCount(ploc),
            g.getArrowCount(ploc));
    assertEquals("Diamonds- 0 | Sapphires- 2 | Rubies- 3 | Arrows- 0", actual);
    //before-player has 0 sapphires
    assertTrue(g.getPlayerDescription().contains("Diamonds- 1 | Sapphires- 0 |"
            + " Rubies- 0 | Arrows- 1"));
    //player picks 1 sapphire
    g.pickItemAtLocation("S", 1);
    //after-location has 1 sapphire
    ploc = g.getPlayerLocation();
    actual = String.format("Diamonds- %d | Sapphires- %d | Rubies- %d | Arrows- %d",
            g.getDiamondCount(ploc), g.getSapphireCount(ploc), g.getRubyCount(ploc),
            g.getArrowCount(ploc));
    assertEquals("Diamonds- 0 | Sapphires- 1 | Rubies- 3 | Arrows- 0", actual);
    //after-player has 1 sapphire
    assertTrue(g.getPlayerDescription().contains("Diamonds- 1 | Sapphires- 1 |"
            + " Rubies- 0 | Arrows- 1"));


    //test 2 ruby pickup
    //before-location has 3 rubies
    ploc = g.getPlayerLocation();
    actual = String.format("Diamonds- %d | Sapphires- %d | Rubies- %d | Arrows- %d",
            g.getDiamondCount(ploc), g.getSapphireCount(ploc), g.getRubyCount(ploc),
            g.getArrowCount(ploc));
    assertEquals("Diamonds- 0 | Sapphires- 1 | Rubies- 3 | Arrows- 0", actual);
    //before-player has 0 rubies
    assertTrue(g.getPlayerDescription().contains("Diamonds- 1 | Sapphires- 1 | "
            + "Rubies- 0 | Arrows- 1"));
    //player picks 2 rubies
    g.pickItemAtLocation("R", 2);
    //after-location has 1 rubies
    ploc = g.getPlayerLocation();
    actual = String.format("Diamonds- %d | Sapphires- %d | Rubies- %d | Arrows- %d",
            g.getDiamondCount(ploc), g.getSapphireCount(ploc), g.getRubyCount(ploc),
            g.getArrowCount(ploc));
    assertEquals("Diamonds- 0 | Sapphires- 1 | Rubies- 1 | Arrows- 0", actual);
    //after-player has 2 rubies
    assertTrue(g.getPlayerDescription().contains("Diamonds- 1 | Sapphires- 1 |"
            + " Rubies- 2 | Arrows- 1"));

    //test player moves to south
    assertEquals("2-2", g.getPlayerLocation());
    g.movePlayerTo("S");
    assertEquals("3-2", g.getPlayerLocation());

    int[] monsterKills = {0, 0};
    for (int i = 0; i < monsterKills.length; i++) {
      randomValues.add(monsterKills[i]);
    }
    r.setDummyRandomValue(randomValues);

    //test Bad smell for Otyugh 2 position away
    assertEquals("Bad", g.getLocationSmell(g.getPlayerLocation()));

    //test Terrible smell for Otyugh 1 position away
    g.movePlayerTo("W");
    assertEquals("Terrible", g.getLocationSmell(g.getPlayerLocation()));

    //test shoot-arrow strikes the monster 1st time
    assertEquals(1, g.shootArrow("W", 1));

    //test injured monster does not eat the player
    g.movePlayerTo("W");
    assertFalse(g.isPlayerDead());

    assertEquals(g.getPlayerLocation(), g.getEndLocation());

    //test game over when player reaches end location
    assertEquals(g.getPlayerLocation(), g.getEndLocation());
    assertTrue(g.isGameOver());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMinMonsterRequired() {
    Randomizer r = new RandomGenerator();
    GameModel g = new DungeonGame(4, 4, 3,
            false, 40, r, 0);
  }

  @Test
  public void testArrowDistribution() {
    Randomizer r = new RandomGeneratorDummy();
    List<Integer> randomValues = new ArrayList<>();

    int[] kruskals = {17, 3, 8, 2, 4, 16, 0, 14, 4, 0, 5, 7, 6, 6, 2, 2, 7, 3, 2, 1, 1, 1, 0, 0};
    int[] interconnectivity = {4, 7, 5};
    int[] terminal = {0, 7};
    int[] monsters = {14, 3, 1, 4, 9};

    for (int i = 0; i < kruskals.length; i++) {
      randomValues.add(kruskals[i]);
    }
    for (int i = 0; i < interconnectivity.length; i++) {
      randomValues.add(interconnectivity[i]);
    }
    for (int i = 0; i < terminal.length; i++) {
      randomValues.add(terminal[i]);
    }
    for (int i = 0; i < monsters.length; i++) {
      randomValues.add(monsters[i]);
    }
    int[] treasure = {11, 0, 2, 3, 0, 4, 5, 6, 2, 7, 8, 9, 10, 1, 2, 3};
    for (int i = 0; i < treasure.length; i++) {
      randomValues.add(treasure[i]);
    }

    int[] arrows = {11, 2, 3, 1, 5, 2, 7, 1, 0, 3, 0, 1, 12, 2};
    for (int i = 0; i < arrows.length; i++) {
      randomValues.add(arrows[i]);
    }
    r.setDummyRandomValue(randomValues);

    GameModel g = new DungeonGame(4, 4, 3,
            false, 40, r, 1);

    g.createPlayer();
    g.addPlayerInDungeon();

    String[] directions = {"N", "E", "S", "W"};
    int moveCounter = 0;
    boolean arrowExists = false;
    int[] moves = {4, 2, 2, 2, 2, 3, 4, 4, 4, 4, 2, 3, 2, 2, 2, 3, 4, 1};
    assertTrue(g.getPlayerDescription().contains("Diamonds- 0 | Sapphires- 0 | Rubies- 0"));

    Set<String> allCaves = new HashSet<>();
    Set<String> treasureCaves = new HashSet<>();

    while (!g.getPlayerLocation().equals(g.getEndLocation()) && moveCounter != 30) {
      if (g.getArrowCount(g.getPlayerLocation()) > 0 && arrowExists) {
        g.movePlayerTo(directions[moves[moveCounter] - 1]);
        g.pickItemAtLocation("A", 0);
      }
      treasureCaves.add(g.getPlayerLocation());
      allCaves.add(g.getPlayerLocation());
      moveCounter++;
    }
    int percent = Math.round(((treasureCaves.size() * 100) / allCaves.size()) / 10) + 30;
    assertEquals(40, percent);
  }

  @Test
  public void testMonsterAtEndAlways() {
    Randomizer r = new RandomGeneratorDummy();
    List<Integer> randomValues = new ArrayList<>();

    int[] kruskals = {17, 3, 8, 2, 4, 16, 0, 14, 4, 0, 5, 7, 6, 6, 2, 2, 7, 3, 2, 1, 1, 1, 0, 0};
    int[] interconnectivity = {4, 7, 5};
    int[] terminal = {0, 7};
    int[] monsters = {14, 3, 1, 4, 9};

    for (int i = 0; i < kruskals.length; i++) {
      randomValues.add(kruskals[i]);
    }
    for (int i = 0; i < interconnectivity.length; i++) {
      randomValues.add(interconnectivity[i]);
    }
    for (int i = 0; i < terminal.length; i++) {
      randomValues.add(terminal[i]);
    }
    for (int i = 0; i < monsters.length; i++) {
      randomValues.add(monsters[i]);
    }
    int[] treasure = {11, 0, 2, 3, 0, 4, 5, 6, 2, 7, 8, 9, 10, 1, 2, 3};
    for (int i = 0; i < treasure.length; i++) {
      randomValues.add(treasure[i]);
    }

    int[] arrows = {11, 2, 3, 1, 5, 2, 7, 1, 0, 3, 0, 1, 12, 2};
    for (int i = 0; i < arrows.length; i++) {
      randomValues.add(arrows[i]);
    }
    r.setDummyRandomValue(randomValues);

    GameModel g = new DungeonGame(4, 4, 3,
            false, 40, r, 1);

    for (String s : g.getOtyughLocations()) {
      assertTrue(s.equals(g.getEndLocation()));
    }
  }

  @Test
  public void testMonsterLocation() {
    Randomizer r = new RandomGenerator();

    GameModel g = new DungeonGame(4, 4, 3,
            false, 40, r, 3);

    for (String s : g.getOtyughLocations()) {
      //never at start location
      assertFalse(s.equals(g.getStartLocation()));

      //never in a tunnel
      assertEquals("Cave", g.getLocationType(s));
    }

    //no of monsters is equal to difficulty level
    assertEquals(3, g.getOtyughLocations().size());
  }

  @Test
  public void testWrap2MonstersSmell() {
    Randomizer r = new RandomGeneratorDummy();
    List<Integer> randomValues = new ArrayList<>();

    int[] kruskals = {1, 24, 8, 1, 12, 8, 23, 11, 3, 1, 17, 10, 6, 10, 2, 1, 2, 3, 8, 11, 2,
                      9, 8, 8, 6, 5, 3, 4, 2, 0, 1, 0};
    int[] interconnectivity = {13, 10, 13};
    int[] terminal = {13};
    int[] monsters = {15, 15, 0, 4, 11, 10, 11, 13, 10, 7, 0, 1, 5, 10, 11, 2};

    for (int i = 0; i < kruskals.length; i++) {
      randomValues.add(kruskals[i]);
    }
    for (int i = 0; i < interconnectivity.length; i++) {
      randomValues.add(interconnectivity[i]);
    }
    for (int i = 0; i < terminal.length; i++) {
      randomValues.add(terminal[i]);
    }
    for (int i = 0; i < monsters.length; i++) {
      randomValues.add(monsters[i]);
    }

    int[] treasure = {6, 9, 8, 3, 3, 5, 1, 8, 13, 7, 6, 0};
    for (int i = 0; i < treasure.length; i++) {
      randomValues.add(treasure[i]);
    }

    int[] arrows = {12, 1, 15, 2, 7, 2, 15, 15, 6, 1, 3, 1, 10, 2, 14, 1, 1, 2};
    for (int i = 0; i < arrows.length; i++) {
      randomValues.add(arrows[i]);
    }
    r.setDummyRandomValue(randomValues);

    GameModel g = new DungeonGame(4, 4, 3,
            true, 50, r, 3);
    g.createPlayer();
    g.addPlayerInDungeon();
    g.movePlayerTo("N");
    g.movePlayerTo("N");

    //test Terrible smell for 2 Otyughs 2 position away
    assertEquals("Terrible", g.getLocationSmell(g.getPlayerLocation()));

    //test shoot-arrow strikes the otyugh 1st time
    assertEquals(1, g.shootArrow("E", 2));
    //test shoot-arrow kills the otyugh
    assertEquals(2, g.shootArrow("E", 2));

    //test Bad smell for the 1 remaining otyugh
    assertEquals("Bad", g.getLocationSmell(g.getPlayerLocation()));

    g.movePlayerTo("E");
    g.pickItemAtLocation("A", 1);

    //test Terrible smell for the 1 remaining otyugh
    assertEquals("Terrible", g.getLocationSmell(g.getPlayerLocation()));

    g.movePlayerTo("E");
    g.movePlayerTo("N");

    //test arrow travels through tunnel without counting distance
    //and arrow changes direction in the tunnel
    //test 2 arrows left
    assertTrue(g.getPlayerDescription().contains("Diamonds- 0 | Sapphires- 0 |"
            + " Rubies- 0 | Arrows- 2"));

    //test shoot-arrow strikes the otyugh 1st time
    assertEquals(1, g.shootArrow("E", 1));

    g.movePlayerTo("E");
    g.movePlayerTo("S");

    //test shoot-arrow kills the otyugh
    assertEquals(2, g.shootArrow("S", 1));

    //test shoot-no more arrows left
    assertEquals(-1, g.shootArrow("S", 1));
    assertTrue(g.getPlayerDescription().contains("Diamonds- 0 | Sapphires- 0 |"
            + " Rubies- 0 | Arrows- 0"));
  }

  @Test
  public void testCaveReachability() {
    Randomizer r = new RandomGeneratorDummy();
    List<Integer> randomValues = new ArrayList<>();

    int[] kruskals = {17, 3, 8, 2, 4, 16, 0, 14, 4, 0, 5, 7, 6, 6, 2, 2, 7, 3, 2, 1, 1, 1, 0, 0};
    int[] interconnectivity = {4, 7, 5};
    int[] terminal = {0, 7};
    int[] monsters = {14, 3, 1, 4, 9};

    for (int i = 0; i < kruskals.length; i++) {
      randomValues.add(kruskals[i]);
    }
    for (int i = 0; i < interconnectivity.length; i++) {
      randomValues.add(interconnectivity[i]);
    }
    for (int i = 0; i < terminal.length; i++) {
      randomValues.add(terminal[i]);
    }
    for (int i = 0; i < monsters.length; i++) {
      randomValues.add(monsters[i]);
    }
    int[] treasure = {11, 0, 2, 3, 0, 4, 5, 6, 2, 7, 8, 9, 10, 1, 2, 3};
    for (int i = 0; i < treasure.length; i++) {
      randomValues.add(treasure[i]);
    }

    int[] arrows = {11, 2, 3, 1, 5, 2, 7, 1, 0, 3, 0, 1, 12, 2};
    for (int i = 0; i < arrows.length; i++) {
      randomValues.add(arrows[i]);
    }
    r.setDummyRandomValue(randomValues);

    GameModel g = new DungeonGame(4, 4, 3,
            false, 40, r, 2);

    //for each cave in the dungeon
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        for (int x = 0; x < 4; x++) {
          for (int y = 0; y < 4; y++) {
            //bfsHelper method returns -1 if a path is not found between 2 nodes
            if (i != x && j != y) {
              int result = bfsHelper("" + i + "-" + j, "" + x + "-" + y,
                      g.getEdges());
              assertNotEquals(-1, result);
            }
          }
        }
      }
    }
  }

  /**
   * Helper method to calculate the distance between the start and end location given all the
   * available paths in the dungeon.
   *
   * @param startLocation start location
   * @param endLocation   end location
   * @param paths         list of all available paths between caves in the dungeon
   * @return
   */
  private int bfsHelper(String startLocation, String endLocation, List<String> paths) {
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
      for (String e : paths) {
        String[] p = e.split("=");
        //if current node is same as p1 vertex of edge, and p2 vertex is not yet visited,
        // and p2 vertex is not already added in the queue
        if (p[0].equals(currLoc) && !visited.contains(p[1])
                && !bfsQueue.containsKey(p[1])) {
          //add the p2 vertex to the queue with new distance
          bfsQueue.put(p[1], currDistance + 1);
          //if p2 vertex is the endLocation , then return the current distance required to reach p2
          if (p[1].equals(endLocation)) {
            return currDistance + 1;
          }
        }
        //if current node is same as p2 vertex of edge, and p1 vertex is not yet visited,
        // and p1 vertex is not already added in the queue
        if (p[1].equals(currLoc) && !visited.contains(p[0])
                && !bfsQueue.containsKey(p[0])) {
          //add the p1 vertex to the queue with new distance
          bfsQueue.put(p[0], currDistance + 1);
          //if p1 vertex is the endLocation , then return the current distance required to reach p1
          if (p[0].equals(endLocation)) {
            return currDistance + 1;
          }
        }
      } //end of for loop
    } //end of while loop
    //if the endlocation was not found then return -1
    return -1;
  }
}