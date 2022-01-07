import org.junit.Test;

import java.awt.event.KeyEvent;
import java.io.StringReader;

import javax.swing.JLabel;

import dungeon.DungeonGameConsoleController;
import dungeon.DungeonGameSwingController;
import dungeon.DungeonGameView;
import dungeon.GameController;
import dungeon.GameControllerSwing;
import dungeon.GameModel;

import static org.junit.Assert.assertEquals;

/**
 * Class for testing the game controller using mock models of the game.
 */
public class GameContollerMockTest {

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidModel() {
    GameModel m = null;
    StringReader input = new StringReader("");
    Appendable gameLog = new StringBuilder();
    GameController g = new DungeonGameConsoleController(input, gameLog);
    g.startGame(m);
  }

  @Test
  public void testSafeMove() {
    GameModel m = new MockMove();
    Readable fakeInput = new StringReader("M S q");
    Appendable outputLog = new StringBuilder();
    GameController c = new DungeonGameConsoleController(fakeInput, outputLog);
    c.startGame(m);
    String expected = "----------------------------------------------------\n"
            + "You are in a Cave\nDoors lead to: N S W\n"
            + "You have: Diamonds- 0, Sapphires- 0, Rubies- 0, Arrows- 3\n"
            + "You smell something Terrible nearby.\n\n"
            + "Move, Pickup, or Shoot (M-P-S) or Q to Quit: Where to (N-E-S-W)?: "
            + "----------------------------------------------------\n"
            + "You are in a Cave\nDoors lead to: N S W\n"
            + "You have: Diamonds- 0, Sapphires- 0, Rubies- 0, Arrows- 3\n"
            + "You smell something Terrible nearby.\n\n"
            + "Move, Pickup, or Shoot (M-P-S) or Q to Quit: Game quit! Ending game state:\n"
            + "Your Bag contains: Diamonds- 0, Sapphires- 0, Rubies- 0, Arrows- 3\n";
    assertEquals(expected, outputLog.toString());
  }

  @Test
  public void testUnsafeMove() {
    GameModel m = new MockMove();
    Readable fakeInput = new StringReader("M N q");
    Appendable outputLog = new StringBuilder();
    GameController c = new DungeonGameConsoleController(fakeInput, outputLog);
    c.startGame(m);
    String expected = "----------------------------------------------------\n"
            + "You are in a Cave\nDoors lead to: N S W\n"
            + "You have: Diamonds- 0, Sapphires- 0, Rubies- 0, Arrows- 3\n"
            + "You smell something Terrible nearby.\n\n"
            + "Move, Pickup, or Shoot (M-P-S) or Q to Quit: Where to (N-E-S-W)?: "
            + "Chomp, chomp, chomp, you are eaten by an Otyugh!\n"
            + "Better luck next time\n";
    assertEquals(expected, outputLog.toString());
  }

  @Test
  public void testInvalidDirection() {
    GameModel m = new MockMove();
    Readable fakeInput = new StringReader("M E abc 123 S q");
    Appendable outputLog = new StringBuilder();
    GameController c = new DungeonGameConsoleController(fakeInput, outputLog);
    c.startGame(m);
    String expected = "----------------------------------------------------\n"
            + "You are in a Cave\nDoors lead to: N S W\n"
            + "You have: Diamonds- 0, Sapphires- 0, Rubies- 0, Arrows- 3\n"
            + "You smell something Terrible nearby.\n\n"
            + "Move, Pickup, or Shoot (M-P-S) or Q to Quit: Where to (N-E-S-W)?: Not a valid"
            + " direction. Valid directions from this location are: N S W\nWhere to (N-E-S-W)?: "
            + "Not a valid direction. Valid directions from this location are: N S W\n"
            + "Where to (N-E-S-W)?: Not a valid direction. Valid directions from this location"
            + " are: N S W\nWhere to (N-E-S-W)?: "
            + "----------------------------------------------------\n"
            + "You are in a Cave\nDoors lead to: N S W\n"
            + "You have: Diamonds- 0, Sapphires- 0, Rubies- 0, Arrows- 3\n"
            + "You smell something Terrible nearby.\n\n"
            + "Move, Pickup, or Shoot (M-P-S) or Q to Quit: Game quit! Ending game state:\n"
            + "Your Bag contains: Diamonds- 0, Sapphires- 0, Rubies- 0, Arrows- 3\n";
    assertEquals(expected, outputLog.toString());
  }

  @Test
  public void testValidPickup() {
    GameModel m = new MockPick();
    Readable fakeInput = new StringReader("P R 1 P D 2 P S 2 q");
    Appendable outputLog = new StringBuilder();
    GameController c = new DungeonGameConsoleController(fakeInput, outputLog);
    c.startGame(m);
    String expected = "----------------------------------------------------\n"
            + "You are in a Cave\nDoors lead to: N E S W\n"
            + "You have: Diamonds- 1, Sapphires- 2, Rubies- 2, Arrows- 5\n"
            + "You find: 1 rubies, 2 diamonds, 2 sapphires, 2 arrows here\n\n"
            + "Move, Pickup, or Shoot (M-P-S) or Q to Quit: Pick what (R-D-S-A)?: Pick how many?:"
            + " ----------------------------------------------------\n"
            + "You are in a Cave\nDoors lead to: N E S W\n"
            + "You have: Diamonds- 1, Sapphires- 2, Rubies- 2, Arrows- 5\n"
            + "You find: 1 rubies, 2 diamonds, 2 sapphires, 2 arrows here\n\n"
            + "Move, Pickup, or Shoot (M-P-S) or Q to Quit: Pick what (R-D-S-A)?: Pick how many?:"
            + " ----------------------------------------------------\n"
            + "You are in a Cave\nDoors lead to: N E S W\n"
            + "You have: Diamonds- 1, Sapphires- 2, Rubies- 2, Arrows- 5\n"
            + "You find: 1 rubies, 2 diamonds, 2 sapphires, 2 arrows here\n\n"
            + "Move, Pickup, or Shoot (M-P-S) or Q to Quit: Pick what (R-D-S-A)?: Pick how many?:"
            + " ----------------------------------------------------\n"
            + "You are in a Cave\nDoors lead to: N E S W\n"
            + "You have: Diamonds- 1, Sapphires- 2, Rubies- 2, Arrows- 5\n"
            + "You find: 1 rubies, 2 diamonds, 2 sapphires, 2 arrows here\n\n"
            + "Move, Pickup, or Shoot (M-P-S) or Q to Quit: Game quit! Ending game state:\n"
            + "Your Bag contains: Diamonds- 1, Sapphires- 2, Rubies- 2, Arrows- 5\n";
    assertEquals(expected, outputLog.toString());
  }

  @Test
  public void testInvalidCountPickup() {
    GameModel m = new MockPick();
    Readable fakeInput = new StringReader("P R 2 R 1 q");
    Appendable outputLog = new StringBuilder();
    GameController c = new DungeonGameConsoleController(fakeInput, outputLog);
    c.startGame(m);
    String expected = "----------------------------------------------------\n"
            + "You are in a Cave\nDoors lead to: N E S W\n"
            + "You have: Diamonds- 1, Sapphires- 2, Rubies- 2, Arrows- 5\n"
            + "You find: 1 rubies, 2 diamonds, 2 sapphires, 2 arrows here\n\n"
            + "Move, Pickup, or Shoot (M-P-S) or Q to Quit: Pick what (R-D-S-A)?: Pick how many?: "
            + "Not a valid item or count. Items available at this location are: "
            + "1 rubies, 2 diamonds, 2 sapphires, 2 arrows \n"
            + "Pick what (R-D-S-A)?: Pick how many?: "
            + "----------------------------------------------------\n"
            + "You are in a Cave\nDoors lead to: N E S W\n"
            + "You have: Diamonds- 1, Sapphires- 2, Rubies- 2, Arrows- 5\n"
            + "You find: 1 rubies, 2 diamonds, 2 sapphires, 2 arrows here\n\n"
            + "Move, Pickup, or Shoot (M-P-S) or Q to Quit: Game quit! Ending game state:\n"
            + "Your Bag contains: Diamonds- 1, Sapphires- 2, Rubies- 2, Arrows- 5\n";
    assertEquals(expected, outputLog.toString());
  }

  @Test
  public void testInvalidInputPickup() {
    GameModel m = new MockPick();
    Readable fakeInput = new StringReader("P R abc 1 q");
    Appendable outputLog = new StringBuilder();
    GameController c = new DungeonGameConsoleController(fakeInput, outputLog);
    c.startGame(m);
    String expected = "----------------------------------------------------\n"
            + "You are in a Cave\nDoors lead to: N E S W\n"
            + "You have: Diamonds- 1, Sapphires- 2, Rubies- 2, Arrows- 5\n"
            + "You find: 1 rubies, 2 diamonds, 2 sapphires, 2 arrows here\n\n"
            + "Move, Pickup, or Shoot (M-P-S) or Q to Quit: Pick what (R-D-S-A)?: Pick how many?: "
            + "Not a valid count. Please ensure that the count is a numerical value.\n"
            + "Pick how many?: ----------------------------------------------------\n"
            + "You are in a Cave\nDoors lead to: N E S W\n"
            + "You have: Diamonds- 1, Sapphires- 2, Rubies- 2, Arrows- 5\n"
            + "You find: 1 rubies, 2 diamonds, 2 sapphires, 2 arrows here\n\n"
            + "Move, Pickup, or Shoot (M-P-S) or Q to Quit: Game quit! Ending game state:\n"
            + "Your Bag contains: Diamonds- 1, Sapphires- 2, Rubies- 2, Arrows- 5\n";
    assertEquals(expected, outputLog.toString());
  }

  @Test
  public void testArrowShoot() {
    GameModel m = new MockShoot();
    Readable fakeInput = new StringReader("S S 1 S S 2 S S 3 S W 1 S W abc 1 q");
    Appendable outputLog = new StringBuilder();
    GameController c = new DungeonGameConsoleController(fakeInput, outputLog);
    c.startGame(m);
    String expected = "----------------------------------------------------\n"
            + "You are in a Cave\nDoors lead to: N S W\n"
            + "You have: Diamonds- 0, Sapphires- 0, Rubies- 0, Arrows- 3\n"
            + "You smell something Terrible nearby.\n\n"
            + "Move, Pickup, or Shoot (M-P-S) or Q to Quit: Shoot in which direction (N-E-S-W)?:"
            + " Shoot how far? (1-5): You shoot an arrow into the darkness\n"
            + "You hear a great howl in the distance\n"
            + "----------------------------------------------------\n"
            + "You are in a Cave\nDoors lead to: N S W\n"
            + "You have: Diamonds- 0, Sapphires- 0, Rubies- 0, Arrows- 2\n"
            + "You smell something Terrible nearby.\n\n"
            + "Move, Pickup, or Shoot (M-P-S) or Q to Quit: Shoot in which direction (N-E-S-W)?:"
            + " Shoot how far? (1-5): You shoot an arrow into the darkness\n"
            + "You killed an Otyugh !\n"
            + "----------------------------------------------------\n"
            + "You are in a Cave\nDoors lead to: N S W\n"
            + "You have: Diamonds- 0, Sapphires- 0, Rubies- 0, Arrows- 1\n\n"
            + "Move, Pickup, or Shoot (M-P-S) or Q to Quit: Shoot in which direction (N-E-S-W)?:"
            + " Shoot how far? (1-5): You shoot an arrow into the darkness\n"
            + "----------------------------------------------------\n"
            + "You are in a Cave\nDoors lead to: N S W\n"
            + "You have: Diamonds- 0, Sapphires- 0, Rubies- 0, Arrows- 0\n\n"
            + "Move, Pickup, or Shoot (M-P-S) or Q to Quit: Shoot in which direction (N-E-S-W)?:"
            + " Shoot how far? (1-5): You are out of arrows, explore to find more\n"
            + "----------------------------------------------------\n"
            + "You are in a Cave\nDoors lead to: N S W\n"
            + "You have: Diamonds- 0, Sapphires- 0, Rubies- 0, Arrows- 0\n\n"
            + "Move, Pickup, or Shoot (M-P-S) or Q to Quit: Shoot in which direction (N-E-S-W)?:"
            + " Shoot how far? (1-5): Not a valid distance. "
            + "Please ensure that the distance is a numerical value.\n"
            + "Shoot how far? (1-5): You are out of arrows, explore to find more\n"
            + "----------------------------------------------------\n"
            + "You are in a Cave\nDoors lead to: N S W\n"
            + "You have: Diamonds- 0, Sapphires- 0, Rubies- 0, Arrows- 0\n\n"
            + "Move, Pickup, or Shoot (M-P-S) or Q to Quit: Game quit! Ending game state:\n"
            + "Your Bag contains: Diamonds- 0, Sapphires- 0, Rubies- 0, Arrows- 0\n";
    assertEquals(expected, outputLog.toString());
  }

  @Test
  public void testMockViewSetupViewListeners() {
    StringBuilder log = new StringBuilder();
    DungeonGameView view = new MockView(log);
    GameControllerSwing controller = new DungeonGameSwingController(view);
    controller.setupViewListeners();
    assertEquals("Make Visible called\naddClickListener called\n"
            + "addModelInputListener called\naddRestartGameListener called\n"
            + "addKeyListener called\n", log.toString());
  }

  @Test
  public void testMockViewSetModel() {
    StringBuilder log = new StringBuilder();
    DungeonGameView view = new MockView(log);
    GameControllerSwing controller = new DungeonGameSwingController(view);
    controller.createModel(5, 5, 5, true,
            20, 5);
    assertEquals("Set Model called\nRefresh called\n", log.toString());
  }

  @Test
  public void testMockViewRestartGame() {
    StringBuilder log = new StringBuilder();
    DungeonGameView view = new MockView(log);
    GameControllerSwing controller = new DungeonGameSwingController(view);
    controller.createModel(5, 5, 5, true,
            20, 5);
    controller.restartGame();
    assertEquals("Set Model called\nRefresh called\nRefresh called\n", log.toString());
  }

  @Test
  public void testMockViewHandleComboKeyEvent() {
    StringBuilder log = new StringBuilder();
    DungeonGameView view = new MockView(log);
    GameControllerSwing controller = new DungeonGameSwingController(view);
    controller.createModel(5, 5, 5, true,
            20, 1);
    controller.handleComboKeyEvent(new KeyEvent(new JLabel(), 1, 2, 3,
            38, 'A'));
    assertEquals("Set Model called\nRefresh called\naddShootDistanceListener called\n"
            + "Refresh called\n", log.toString());
  }

  @Test
  public void testMockViewHandleSingleKeyEvent() {
    StringBuilder log = new StringBuilder();
    DungeonGameView view = new MockView(log);
    GameControllerSwing controller = new DungeonGameSwingController(view);
    controller.createModel(5, 5, 5, true,
            20, 1);
    controller.handleSingleKeyEvent(new KeyEvent(new JLabel(), 1, 2, 3,
            38, 'A'));
    assertEquals("Set Model called\nRefresh called\nRefresh called\n", log.toString());
  }

  @Test
  public void testMockViewHandleCellClickEvent() {
    StringBuilder log = new StringBuilder();
    DungeonGameView view = new MockView(log);
    GameControllerSwing controller = new DungeonGameSwingController(view);
    controller.createModel(5, 5, 5, true,
            20, 1);
    controller.handleCellClick(1, 2, true, 5, 5);
    assertEquals("Set Model called\nRefresh called\nRefresh called\n", log.toString());
  }
}
