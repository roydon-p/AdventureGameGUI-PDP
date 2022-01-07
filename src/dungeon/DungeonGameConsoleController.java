package dungeon;

import java.io.IOException;
import java.util.Scanner;

/**
 * This is the controller for the Adventure game in which the player has to traverse through a
 * dungeon that contains monsters called otyughs that can eat the player. The player has to
 * collect arrows which it can use to slay the otyugh as well as collect the treasure that it
 * finds in various locations in the dungeon before reaching the end of the dungeon.
 */
public class DungeonGameConsoleController implements GameController {
  private final Appendable out;
  private final Scanner scan;

  /**
   * Constructor for the controller.
   *
   * @param in  the source to read from
   * @param out the target to print to
   */
  public DungeonGameConsoleController(Readable in, Appendable out) {
    if (in == null || out == null) {
      throw new IllegalArgumentException("Readable and Appendable can't be null");
    }
    this.out = out;
    scan = new Scanner(in);
  }

  @Override
  public void startGame(GameModel g) {
    if (g == null) {
      throw new IllegalArgumentException("Game model cannot be null.");
    }
    //adding player to the dungeon
    g.createPlayer();
    g.addPlayerInDungeon();

    try {
      boolean gameQuit = false;
      while (true) {
        out.append("----------------------------------------------------").append("\n");

        out.append("You are in a " + g.getLocationType(g.getPlayerLocation())).append("\n")
                .append("Doors lead to: " + g.getNextPossibleMoves(g.getPlayerLocation()))
                .append("\n").append("You have: " + g.getPlayerDescription()).append("\n");

        String loc = g.getPlayerLocation();

        String items = g.getAvailableItems(loc);
        if (g.getRubyCount(loc) > 0 || g.getDiamondCount(loc) > 0 || g.getSapphireCount(loc) > 0
                || g.getArrowCount(loc) > 0) {
          out.append("You find: " + items + "here").append("\n");
        }

        String smell = g.getLocationSmell(loc);
        if (!smell.equals("")) {
          out.append("You smell something " + smell + " nearby.").append("\n");
        }

        out.append("\nMove, Pickup, or Shoot (M-P-S) or Q to Quit: ");
        String choice = scan.next();
        boolean invalidSelection;

        switch (choice) {
          case "Q":
          case "q":
            gameQuit = true;
            break;
          case "M":
          case "m":
            invalidSelection = true;
            while (invalidSelection) {
              out.append("Where to (N-E-S-W)?: ");
              String dir = scan.next();
              try {
                if (!g.movePlayerTo(dir)) {
                  out.append("Chomp, chomp, chomp, you are eaten by an Otyugh!\n"
                          + "Better luck next time\n");
                }
                invalidSelection = false;
              } catch (IllegalArgumentException a) {
                out.append("Not a valid direction. ")
                        .append("Valid directions from this location are: "
                                + g.getNextPossibleMoves(g.getPlayerLocation())).append("\n");
              }
            }
            break;
          case "P":
          case "p":
            invalidSelection = true;
            boolean invalidCount = true;
            int count = 0;
            while (invalidSelection) {
              out.append("Pick what (R-D-S-A)?: ");
              String item = scan.next();
              while (invalidCount) {
                out.append("Pick how many?: ");
                try {
                  count = Integer.parseInt(scan.next());
                  invalidCount = false;
                } catch (NumberFormatException n) {
                  out.append("Not a valid count. ")
                          .append("Please ensure that the count is a numerical value.\n");
                }
              }
              try {
                g.pickItemAtLocation(item, count);
                invalidSelection = false;
              } catch (IllegalArgumentException a) {
                invalidCount = true;
                if (g.getRubyCount(loc) > 0 || g.getDiamondCount(loc) > 0
                        || g.getSapphireCount(loc) > 0 || g.getArrowCount(loc) > 0) {
                  out.append("Not a valid item or count. ")
                          .append("Items available at this location are: "
                                  + items).append("\n");
                } else {
                  out.append("No items are available at this location for pick up\n");
                  break;
                }
              }
            }
            break;
          case "S":
          case "s":
            invalidSelection = true;
            boolean invalidDistance = true;
            int distance = 0;
            while (invalidSelection) {
              out.append("Shoot in which direction (N-E-S-W)?: ");
              String arrowDir = scan.next();
              while (invalidDistance) {
                out.append("Shoot how far? (1-5): ");
                try {
                  distance = Integer.parseInt(scan.next());
                  invalidDistance = false;
                } catch (NumberFormatException n) {
                  out.append("Not a valid distance. ")
                          .append("Please ensure that the distance is a numerical value.\n");
                }
              }
              try {
                int damage = g.shootArrow(arrowDir, distance);
                if (damage == -1) {
                  out.append("You are out of arrows, explore to find more\n");
                } else {
                  out.append("You shoot an arrow into the darkness\n");
                }
                if (damage == 1) {
                  out.append("You hear a great howl in the distance\n");
                } else if (damage == 2) {
                  out.append("You killed an Otyugh !\n");
                }
                invalidSelection = false;
              } catch (IllegalArgumentException a) {
                invalidDistance = true;
                out.append("Not a valid direction or distance. ")
                        .append("Please ensure that the distance is between 1 and 5 and direction"
                                + " is valid: " + g.getNextPossibleMoves(loc)).append("\n");
              }
            }
            break;
          default:
            out.append("Please enter a valid command.").append("\n");
        }
        if (gameQuit || g.isGameOver()) {
          break;
        }
      }
      if (!g.isPlayerDead() && g.isGameOver()) {
        out.append("Congratulations !!! You successfully completed the maze.\n");
        out.append("Your Bag contains: " + g.getPlayerDescription()).append("\n");
      } else if (gameQuit) {
        out.append("Game quit! Ending game state:\n");
        out.append("Your Bag contains: " + g.getPlayerDescription()).append("\n");
      }
    } catch (IOException e) {
      throw new IllegalStateException("Append failed", e);
    } catch (IllegalStateException es) {
      throw new IllegalStateException("Player does not exist", es);
    }
  }
}
