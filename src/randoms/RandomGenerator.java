package randoms;

import java.util.List;
import java.util.Random;

/**
 * Class to generate the Random values required by different methods.
 */
public class RandomGenerator implements Randomizer {
  private Random r;

  /**
   * Constructs an instance of Random Generator class which initializes the java Random object.
   */
  public RandomGenerator() {
    r = new Random();
  }

  @Override
  public int getRandomInt(int minValue, int maxValue) {
    if (minValue > maxValue) {
      throw new IllegalArgumentException("Lower bound cannot be greater than upper bound.");
    }
    return r.nextInt(maxValue - minValue) + minValue;
  }

  @Override
  public void setDummyRandomValue(List<Integer> r) {
    // This method is supposed to be empty as it is used to set the values for Randomizer object
    // only for testing. The overridden method in RandomGeneratorDummy class will set a specific
    // value for random when testing.
  }
}
