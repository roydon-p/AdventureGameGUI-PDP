import java.io.IOException;

import dungeon.DungeonGameView;
import dungeon.GameControllerSwing;
import dungeon.GameModelReadOnly;

/**
 * Mock view created for testing the operations of the controller.
 */
public class MockView implements DungeonGameView {
  private Appendable log;

  /**
   * Mock constructor created for appending string for test.
   *
   * @param log message log
   */
  public MockView(Appendable log) {
    this.log = log;
  }

  @Override
  public void refresh(String message) {
    try {
      log.append("Refresh called\n");
    } catch (IOException e) {
      // do nothing
    }
  }

  @Override
  public void makeVisible() {
    try {
      log.append("Make Visible called\n");
    } catch (IOException e) {
      // do nothing
    }
  }

  @Override
  public void setModel(GameModelReadOnly model) {
    try {
      log.append("Set Model called\n");
    } catch (IOException e) {
      // do nothing
    }
  }

  @Override
  public void addClickListener(GameControllerSwing listener) {
    try {
      log.append("addClickListener called\n");
    } catch (IOException e) {
      // do nothing
    }
  }

  @Override
  public void addModelInputListener(GameControllerSwing listener) {
    try {
      log.append("addModelInputListener called\n");
    } catch (IOException e) {
      // do nothing
    }
  }

  @Override
  public void addRestartGameListener(GameControllerSwing listener) {
    try {
      log.append("addRestartGameListener called\n");
    } catch (IOException e) {
      // do nothing
    }
  }

  @Override
  public void addKeyListener(GameControllerSwing dungeonGameSwingController) {
    try {
      log.append("addKeyListener called\n");
    } catch (IOException e) {
      // do nothing
    }
  }

  @Override
  public int addShootDistanceListener() {
    try {
      log.append("addShootDistanceListener called\n");
    } catch (IOException e) {
      // do nothing
    }
    return 1;
  }
}