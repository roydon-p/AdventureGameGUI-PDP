package randoms;

import java.util.List;

/**
 * Generates the Random values required by different methods.
 */
public interface Randomizer {
  /**
   * Generate the random value based on the supplied min and max values.
   * @param minValue lower bound for the random value
   * @param maxValue upper bound for the random value
   * @return the randomly generated value for the given range
   */
  public int getRandomInt(int minValue, int maxValue);

  /**
   * Sets the random value to be used during testing.
   * @param r input random value
   */
  public void setDummyRandomValue(List<Integer> r);
}
