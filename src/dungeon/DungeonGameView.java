package dungeon;

/**
 * A view for Dungeon Game: It displays the birds eye vie of the dungeon. The user is able
 * to see only those locations which have been explored by the player. Rest of the locations
 * are dark. The user can see the locations where treasure and/or arrows exist. The view also
 * gives real-time update of the players bag and the location items along with the outcome
 * of players moves.
 */
public interface DungeonGameView {

  /**
   * This method refreshes the view every time an operation is performed by a user. The refreshed
   * view displays the updated state of the game.
   *
   * @param message The message to be displayed to the user in the information panel about the
   *                action performed.
   */
  void refresh(String message);

  /**
   * Make the view visible to start the game session.
   */
  void makeVisible();

  /**
   * Pass the read-only version of the model to be used by the view to update the GUI.
   *
   * @param model the read-only version of the game model
   */
  void setModel(GameModelReadOnly model);

  /**
   * Set up the controller to handle click events in this view.
   *
   * @param listener the controller
   */
  void addClickListener(GameControllerSwing listener);

  /**
   * Set up the controller to handle new game creation event in this view.
   *
   * @param listener the controller
   */
  void addModelInputListener(GameControllerSwing listener);

  /**
   * Set up the controller to handle restart game event in this view.
   * @param listener the controller
   */
  void addRestartGameListener(GameControllerSwing listener);

  /**
   * Set up the controller to handle key events in this view.
   *
   * @param dungeonGameSwingController the controller
   */
  void addKeyListener(GameControllerSwing dungeonGameSwingController);

  /**
   * Set up the controller to get shooting distance input from an event in this view.
   *
   * @return shooting distance for the arrow
   */
  int addShootDistanceListener();
}
