package dungeon;

/**
 * Represents an Otyugh monster that live in the caves of the dungeons. They can eat the player
 * if the player enters the cave they live in. An otyugh can be killed by a player using arrows.
 * However, the player has to strike the otyugh twice in order to kill it completely. If an otyugh
 * is injured with 1 arrow strike there is still 50% probability that it might eat the player if
 * they enter its cave. The class is kept package private as it will be used only within the
 * dungeon model package.
 */
class Otyugh implements Monster {
  private String location;
  private int health;

  Otyugh(String location, int health) {
    this.location = location;
    this.health = health;
  }

  Otyugh(Otyugh copy) {
    this.location = copy.location;
    this.health = copy.health;
  }

  @Override
  public String getLocation() {
    return location;
  }

  @Override
  public int getHealth() {
    return health;
  }

  @Override
  public void arrowStrike() {
    this.health = this.health - 1;
  }
}
