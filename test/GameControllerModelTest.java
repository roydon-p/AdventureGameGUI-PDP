import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import dungeon.DungeonGame;
import dungeon.DungeonGameConsoleController;
import dungeon.GameController;
import dungeon.GameModel;
import randoms.RandomGeneratorDummy;
import randoms.Randomizer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Class for testing the game controller using deterministic models of the game.
 */
public class GameControllerModelTest {

  @Test(expected = IllegalStateException.class)
  public void testFailingAppendable() {
    GameModel m = createDeterministicModelHelper();
    StringReader input = new StringReader("X M S Q");
    Appendable gameLog = new FailingAppendable();
    GameController g = new DungeonGameConsoleController(input, gameLog);
    g.startGame(m);
  }

  @Test
  public void testIncorrectChoice() {
    GameModel m = createDeterministicModelHelper();
    StringReader input = new StringReader("X M S Q");
    Appendable gameLog = new StringBuilder();
    GameController g = new DungeonGameConsoleController(input, gameLog);
    g.startGame(m);
    assertTrue(gameLog.toString().contains("Please enter a valid command."));
  }

  @Test
  public void testMoveInvalidDirection() {
    GameModel m = createDeterministicModelHelper();
    StringReader input = new StringReader("M E W Q");
    Appendable gameLog = new StringBuilder();
    GameController g = new DungeonGameConsoleController(input, gameLog);
    g.startGame(m);
    assertTrue(gameLog.toString().contains("Not a valid direction. Valid directions from this "
            + "location are:"));
  }

  @Test
  public void testMoveValidDirection() {
    GameModel m = createDeterministicModelHelper();
    StringReader input = new StringReader("M W Q");
    Appendable gameLog = new StringBuilder();
    GameController g = new DungeonGameConsoleController(input, gameLog);
    g.startGame(m);
    assertFalse(gameLog.toString().contains("Not a valid direction. Valid directions from this "
            + "location are:"));
  }

  @Test
  public void testMoveIncorrectFormatDirection() {
    GameModel m = createDeterministicModelHelper();
    StringReader input = new StringReader("M abc W Q");
    Appendable gameLog = new StringBuilder();
    GameController g = new DungeonGameConsoleController(input, gameLog);
    g.startGame(m);
    assertTrue(gameLog.toString().contains("Not a valid direction. Valid directions from this "
            + "location are:"));
  }

  @Test
  public void testPickValidArrowCount() {
    GameModel m = createDeterministicModelHelper();
    StringReader input = new StringReader("P A 1 Q");
    Appendable gameLog = new StringBuilder();
    GameController g = new DungeonGameConsoleController(input, gameLog);
    g.startGame(m);
    assertTrue(gameLog.toString().contains("You have: Diamonds- 0 | Sapphires- 0 | Rubies- 0 |"
            + " Arrows- 4"));
  }

  @Test
  public void testPickInvalidArrowCount() {
    GameModel m = createDeterministicModelHelper();
    StringReader input = new StringReader("P A 2 A 1 Q");
    Appendable gameLog = new StringBuilder();
    GameController g = new DungeonGameConsoleController(input, gameLog);
    g.startGame(m);
    assertTrue(gameLog.toString().contains("Not a valid item or count. Items available at this "
            + "location are:"));
  }

  @Test
  public void testPickValidTreasureItem() {
    GameModel m = createDeterministicModelHelper();
    StringReader input = new StringReader("M S P R 2 Q");
    Appendable gameLog = new StringBuilder();
    GameController g = new DungeonGameConsoleController(input, gameLog);
    g.startGame(m);
    assertTrue(gameLog.toString().contains("You have: Diamonds- 0 | Sapphires- 0 | Rubies- 2 |"
            + " Arrows- 3"));
  }

  @Test
  public void testPickInvalidTreasureItem() {
    GameModel m = createDeterministicModelHelper();
    StringReader input = new StringReader("M S P D 1 R 1 Q");
    Appendable gameLog = new StringBuilder();
    GameController g = new DungeonGameConsoleController(input, gameLog);
    g.startGame(m);
    assertTrue(gameLog.toString().contains("Not a valid item or count. Items available at this "
            + "location are: Sapphires- 2 | Rubies- 3 | Arrows- 2 | "));
  }

  @Test
  public void testPickInvalidTreasureItemCount() {
    GameModel m = createDeterministicModelHelper();
    StringReader input = new StringReader("M S P R 5 R 2 Q");
    Appendable gameLog = new StringBuilder();
    GameController g = new DungeonGameConsoleController(input, gameLog);
    g.startGame(m);
    assertTrue(gameLog.toString().contains("Not a valid item or count. Items available at this "
            + "location are: Sapphires- 2 | Rubies- 3 | Arrows- 2 | "));
  }

  @Test
  public void testPickIncorrectFormatCount() {
    GameModel m = createDeterministicModelHelper();
    StringReader input = new StringReader("M S P R abc 2 Q");
    Appendable gameLog = new StringBuilder();
    GameController g = new DungeonGameConsoleController(input, gameLog);
    g.startGame(m);
    assertTrue(gameLog.toString().contains("Not a valid count. Please ensure that the count is"
            + " a numerical value."));
  }

  @Test
  public void testNoItemsAvailableForPickup() {
    GameModel m = createDeterministicModelHelper();
    StringReader input = new StringReader("M S M S P R 2 Q");
    Appendable gameLog = new StringBuilder();
    GameController g = new DungeonGameConsoleController(input, gameLog);
    g.startGame(m);
    assertTrue(gameLog.toString().contains("No items are available at this location for pick up"));
  }

  @Test
  public void testShootValidDirection() {
    GameModel m = createDeterministicModelHelper();
    StringReader input = new StringReader("S S 1 Q");
    Appendable gameLog = new StringBuilder();
    GameController g = new DungeonGameConsoleController(input, gameLog);
    g.startGame(m);
    assertFalse(gameLog.toString().contains("Not a valid direction or distance. Please ensure"
            + " that the distance is between 1 and 5 and direction is valid:"));
  }

  @Test
  public void testShootInvalidDirection() {
    GameModel m = createDeterministicModelHelper();
    StringReader input = new StringReader("S E 1 S 1 Q");
    Appendable gameLog = new StringBuilder();
    GameController g = new DungeonGameConsoleController(input, gameLog);
    g.startGame(m);
    assertTrue(gameLog.toString().contains("Not a valid direction or distance. Please ensure"
            + " that the distance is between 1 and 5 and direction is valid: N S W"));
  }

  @Test
  public void testShootValidDistance() {
    GameModel m = createDeterministicModelHelper();
    StringReader input = new StringReader("S S 2 Q");
    Appendable gameLog = new StringBuilder();
    GameController g = new DungeonGameConsoleController(input, gameLog);
    g.startGame(m);
    assertFalse(gameLog.toString().contains("Not a valid direction or distance. Please ensure"
            + " that the distance is between 1 and 5 and direction is valid: N S W"));
  }

  @Test
  public void testShootInvalidDistance() {
    GameModel m = createDeterministicModelHelper();
    StringReader input = new StringReader("S S 6 S 2 Q");
    Appendable gameLog = new StringBuilder();
    GameController g = new DungeonGameConsoleController(input, gameLog);
    g.startGame(m);
    assertTrue(gameLog.toString().contains("Not a valid direction or distance. Please ensure"
            + " that the distance is between 1 and 5 and direction is valid: N S W"));
  }

  @Test
  public void testShootIncorrectDistanceFormat() {
    GameModel m = createDeterministicModelHelper();
    StringReader input = new StringReader("S S abc 2 Q");
    Appendable gameLog = new StringBuilder();
    GameController g = new DungeonGameConsoleController(input, gameLog);
    g.startGame(m);
    assertTrue(gameLog.toString().contains("Not a valid distance. Please ensure that the "
            + "distance is a numerical value."));
  }

  @Test
  public void testMonsterBadSmell() {
    GameModel m = createDeterministicModelHelper();
    StringReader input = new StringReader("M S Q");
    Appendable gameLog = new StringBuilder();
    GameController g = new DungeonGameConsoleController(input, gameLog);
    g.startGame(m);
    assertTrue(gameLog.toString().contains("You smell something Bad nearby."));
  }

  @Test
  public void testMonsterTerribleSmell() {
    GameModel m = createDeterministicModelHelper();
    StringReader input = new StringReader("M S M W Q");
    Appendable gameLog = new StringBuilder();
    GameController g = new DungeonGameConsoleController(input, gameLog);
    g.startGame(m);
    assertTrue(gameLog.toString().contains("You smell something Terrible nearby."));
  }

  @Test
  public void testShootMonsterMissed() {
    GameModel m = createDeterministicModelHelper();
    StringReader input = new StringReader("M S M W S W 2 Q");
    Appendable gameLog = new StringBuilder();
    GameController g = new DungeonGameConsoleController(input, gameLog);
    g.startGame(m);
    assertTrue(gameLog.toString().contains("You smell something Terrible nearby."));
    assertTrue(gameLog.toString().contains("You shoot an arrow into the darkness"));
    assertFalse(gameLog.toString().contains("You hear a great howl in the distance"));
    assertFalse(gameLog.toString().contains("You killed an Otyugh !"));
  }

  @Test
  public void testShootMonsterOnce() {
    GameModel m = createDeterministicModelHelper();
    StringReader input = new StringReader("M S M W S W 1 Q");
    Appendable gameLog = new StringBuilder();
    GameController g = new DungeonGameConsoleController(input, gameLog);
    g.startGame(m);
    assertTrue(gameLog.toString().contains("You smell something Terrible nearby."));
    assertTrue(gameLog.toString().contains("You shoot an arrow into the darkness"));
    assertTrue(gameLog.toString().contains("You hear a great howl in the distance"));
    assertFalse(gameLog.toString().contains("You killed an Otyugh !"));
  }

  @Test
  public void testShootMonsterTwice() {
    GameModel m = createDeterministicModelHelper();
    StringReader input = new StringReader("M S M W S W 1 S W 1 Q");
    Appendable gameLog = new StringBuilder();
    GameController g = new DungeonGameConsoleController(input, gameLog);
    g.startGame(m);
    assertTrue(gameLog.toString().contains("You smell something Terrible nearby."));
    assertTrue(gameLog.toString().contains("You shoot an arrow into the darkness"));
    assertTrue(gameLog.toString().contains("You hear a great howl in the distance"));
    assertTrue(gameLog.toString().contains("You killed an Otyugh !"));
  }

  @Test
  public void testShootArrowsOver() {
    GameModel m = createDeterministicModelHelper();
    StringReader input = new StringReader("S S 2 S S 1 S S 3 S S 4 Q");
    Appendable gameLog = new StringBuilder();
    GameController g = new DungeonGameConsoleController(input, gameLog);
    g.startGame(m);
    assertTrue(gameLog.toString().contains("You are out of arrows, explore to find more"));
  }

  @Test
  public void testPlayerWins() {
    GameModel m = createDeterministicModelHelper();
    StringReader input = new StringReader("P A 1 M S M W S W 1 S W 1 M S M W S W 1 S W 1 M W");
    Appendable gameLog = new StringBuilder();
    GameController g = new DungeonGameConsoleController(input, gameLog);
    g.startGame(m);
    assertTrue(gameLog.toString().contains("Congratulations !!! You successfully "
            + "completed the maze.\nYour Bag contains: Diamonds- 0 | Sapphires- 0 | "
            + "Rubies- 0 | Arrows- 0\n"));
  }

  @Test
  public void testPlayerLosses() {
    GameModel m = createDeterministicModelHelper();
    StringReader input = new StringReader("P A 1 M S M W S W 1 S W 1 M S M W M W");
    Appendable gameLog = new StringBuilder();
    GameController g = new DungeonGameConsoleController(input, gameLog);
    g.startGame(m);
    assertTrue(gameLog.toString().contains("Chomp, chomp, chomp, you are eaten by an Otyugh!\n"
            + "Better luck next time"));
  }

  @Test
  public void testQuitGame() {
    GameModel m = createDeterministicModelHelper();
    StringReader input = new StringReader("M S Q");
    Appendable gameLog = new StringBuilder();
    GameController g = new DungeonGameConsoleController(input, gameLog);
    g.startGame(m);
    assertTrue(gameLog.toString().contains("Game quit! Ending game state:\n"
            + "Your Bag contains: Diamonds- 0 | Sapphires- 0 | Rubies- 0 | Arrows- 3"));
  }

  private GameModel createDeterministicModelHelper() {
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

    return g;
  }
}
