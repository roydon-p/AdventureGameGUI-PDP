import dungeon.DungeonGame;
import dungeon.DungeonGameConsoleController;
import dungeon.DungeonGameSwingController;
import dungeon.DungeonGameSwingView;
import dungeon.DungeonGameView;
import dungeon.GameControllerSwing;
import dungeon.GameModel;
import randoms.RandomGenerator;
import randoms.Randomizer;

import java.io.InputStreamReader;

/**
 * The driver that will run the game using the controller.
 */
public class GameDriver {

  static int rowCount;
  static int colCount;
  static int deg;
  static boolean isWrapping;
  static int otCount;

  /**
   * The main method that creates the model of the game and assigns it to the controller to
   * perform all the remaining operations of the game.
   *
   * @param args passes parameters used to create the dungeon in a string array through the
   *             console window. The order is no. of Rows, no. of columns, degree of
   *             interconnectivity, is dungeon wrapping(Y/N), percentage of caves/locations to
   *             which treasure/arrows are to be assigned, difficulty level/no of otyughs.
   */
  public static void main(String[] args) {
    if (args.length == 0) {
      DungeonGameView view = new DungeonGameSwingView();
      GameControllerSwing controller = new DungeonGameSwingController(view);
      controller.setupViewListeners();
    } else {
      if (args.length < 6) {
        throw new IllegalArgumentException("Not enough inputs arguments");
      }
      //process command line input
      rowCount = Integer.parseInt(args[0]);
      colCount = Integer.parseInt(args[1]);
      deg = Integer.parseInt(args[2]);
      String wrap = args[3];
      isWrapping = wrap.equals("Y") || wrap.equals("y");
      int tp = Integer.parseInt(args[4]);
      Randomizer r = new RandomGenerator();
      otCount = Integer.parseInt(args[5]);
      Readable input = new InputStreamReader(System.in);
      Appendable output = System.out;
      try {
        GameModel g = new DungeonGame(rowCount, colCount, deg, isWrapping, tp, r, otCount);
        new DungeonGameConsoleController(input, output).startGame(g);
      } catch (IllegalArgumentException e) {
        System.out.println("Error occurred while generating the model for the game. "
                + "Please check the values for maze creation and try again");
      } catch (IllegalStateException s) {
        System.out.println("Error occurred in the Controller. Please verify that all the "
                + "methods are defined in proper order.");
      }
    }
  }
}
