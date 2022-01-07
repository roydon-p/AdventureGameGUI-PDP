package dungeon;

import randoms.RandomGenerator;
import randoms.Randomizer;

import java.awt.event.KeyEvent;

/**
 * This controller is created to implement the Controller for the GUI written using Java Swing
 * for the adventure game implementation. This controller handles all the key events as well
 * as the mouse click events that happen on the GUI of the adventure game.
 */
public class DungeonGameSwingController implements GameControllerSwing {
  private DungeonGameView view;
  private GameModel model;

  /**
   * Constructor for the controller.
   */
  public DungeonGameSwingController(DungeonGameView view) {
    this.view = view;
  }

  @Override
  public void setupViewListeners() {
    this.view.makeVisible();
    this.view.addClickListener(this);
    this.view.addModelInputListener(this);
    this.view.addRestartGameListener(this);
    this.view.addKeyListener(this);
  }

  @Override
  public void createModel(int rowCount, int colCount, int degCount, boolean isWrap, int percentage,
                          int otyughCount) {
    Randomizer r = new RandomGenerator();
    model = new DungeonGame(rowCount, colCount, degCount, isWrap, percentage, r, otyughCount);
    view.setModel((GameModelReadOnly) model);
    startGame(model);
  }

  private void startGame(GameModel g) {
    if (g == null) {
      throw new IllegalArgumentException("Game model cannot be null.");
    }
    //adding player to the dungeon
    g.createPlayer();
    g.addPlayerInDungeon();
    view.refresh("Start Playing..");
  }

  @Override
  public void restartGame() {
    model.refresh();
    startGame(model);
  }

  @Override
  public void handleComboKeyEvent(KeyEvent k) {
    String message = "";
    int v = view.addShootDistanceListener();
    switch (k.getKeyCode()) {
      case 37:
        try {
          int damage = model.shootArrow("W", v);

          if (damage == -1) {
            message = "You are out of arrows, explore to find more";
          } else if (damage == 1) {
            message = "You hear a great howl in the distance";
          } else if (damage == 2) {
            message = "You killed an Otyugh";
          }
        } catch (IllegalArgumentException a) {
          message = "Not a valid direction or distance";
        }
        break;
      case 38:
        try {
          int damage = model.shootArrow("N", v);

          if (damage == -1) {
            message = "You are out of arrows, explore to find more";
          } else if (damage == 1) {
            message = "You hear a great howl in the distance";
          } else if (damage == 2) {
            message = "You killed an Otyugh";
          }
        } catch (IllegalArgumentException a) {
          message = "Not a valid direction or distance";
        }
        break;
      case 39:
        try {
          int damage = model.shootArrow("E", v);

          if (damage == -1) {
            message = "You are out of arrows, explore to find more";
          } else if (damage == 1) {
            message = "You hear a great howl in the distance";
          } else if (damage == 2) {
            message = "You killed an Otyugh";
          }
        } catch (IllegalArgumentException a) {
          message = "Not a valid direction or distance";
        }
        break;
      case 40:
        try {
          int damage = model.shootArrow("S", v);

          if (damage == -1) {
            message = "You are out of arrows, explore to find more ";
          } else if (damage == 1) {
            message = "You hear a great howl in the distance";
          } else if (damage == 2) {
            message = "You killed an Otyugh";
          }
        } catch (IllegalArgumentException a) {
          message = "Not a valid direction or distance";
        }
        break;
      default:
        message = "";
    }
    view.refresh(message);
  }

  @Override
  public void handleSingleKeyEvent(KeyEvent k) {
    String message;
    switch (k.getKeyCode()) {
      case 37:
        try {
          if (!model.movePlayerTo("W")) {
            message = "You were eaten by an Otyugh...GAME OVER !!!";
          } else {
            message = "You moved West";
          }
        } catch (IllegalArgumentException a) {
          message = "Not a valid direction";
        }
        break;
      case 38:
        try {
          if (!model.movePlayerTo("N")) {
            message = "You were eaten by an Otyugh...GAME OVER !!!";
          } else {
            message = "You moved North";
          }
        } catch (IllegalArgumentException a) {
          message = "Not a valid direction";
        }
        break;
      case 39:
        try {
          if (!model.movePlayerTo("E")) {
            message = "You were eaten by an Otyugh...GAME OVER !!!";
          } else {
            message = "You moved East";
          }
        } catch (IllegalArgumentException a) {
          message = "Not a valid direction";
        }
        break;
      case 40:
        try {
          if (!model.movePlayerTo("S")) {
            message = "You were eaten by an Otyugh...GAME OVER !!!";
          } else {
            message = "You moved South";
          }
        } catch (IllegalArgumentException a) {
          message = "Not a valid direction";
        }
        break;
      case 65:
        try {
          model.pickItemAtLocation("AllArrows", 0);
          message = "Arrows picked";
        } catch (IllegalArgumentException a) {
          message = "No arrows here";
        }
        break;
      case 84:
        try {
          model.pickItemAtLocation("AllTreasure", 0);
          message = "Treasure picked";
        } catch (IllegalArgumentException a) {
          message = "No Treasure here";
        }
        break;
      default:
        message = "";
    }
    view.refresh(message);
  }

  @Override
  public void handleCellClick(int x, int y, boolean isWrap, int rowCount, int colCount) {
    String message = "";
    try {
      int col = x / 64;
      int row = y / 64;

      String[] s = model.getPlayerLocation().split("-");
      int playerRow = Integer.parseInt(s[0]);
      int playerCol = Integer.parseInt(s[1]);

      if ((playerRow - 1) == row && playerCol == col
              || (isWrap && playerRow == 0 && playerCol == col && row == rowCount - 1)) {
        if (!model.movePlayerTo("N")) {
          message = "You were eaten by an Otyugh...GAME OVER !!!";
        } else {
          message = "You moved North";
        }
      }
      if ((playerRow + 1) == row && playerCol == col
              || (isWrap && playerRow == rowCount - 1 && playerCol == col && row == 0)) {
        if (!model.movePlayerTo("S")) {
          message = "You were eaten by an Otyugh...GAME OVER !!!";
        } else {
          message = "You moved South";
        }
      }
      if ((playerCol - 1) == col && playerRow == row
              || (isWrap && playerCol == 0 && playerRow == row && col == colCount - 1)) {
        if (!model.movePlayerTo("W")) {
          message = "You were eaten by an Otyugh...GAME OVER !!!";
        } else {
          message = "You moved West";
        }
      }
      if ((playerCol + 1) == col && playerRow == row
              || (isWrap && playerCol == colCount - 1 && playerRow == row && col == 0)) {
        if (!model.movePlayerTo("E")) {
          message = "You were eaten by an Otyugh...GAME OVER !!!";
        } else {
          message = "You moved East";
        }
      }
    } catch (IllegalArgumentException a) {
      message = "Not a valid direction";
    }
    view.refresh(message);
  }
}
