package randoms;

import java.util.List;

/**
 * Class to mock generation of Random values for test cases and driver runs.
 */
public class RandomGeneratorDummy implements Randomizer {
  private List<Integer> values;

  @Override
  public int getRandomInt(int minValue, int maxValue) {
    if (minValue > maxValue) {
      throw new IllegalArgumentException("Lower bound cannot be greater than upper bound.");
    }
    int value = values.get(0);
    this.values.remove(0);
    return value;
  }

  @Override
  public void setDummyRandomValue(List<Integer> values) {
    this.values = values;
  }
}
