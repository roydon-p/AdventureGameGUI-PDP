import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import randoms.RandomGenerator;
import randoms.RandomGeneratorDummy;
import randoms.Randomizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Class for testing the Randomizer Interface.
 */
public class RandomizerTest {
  Randomizer r = new RandomGenerator();
  Randomizer r1 = new RandomGeneratorDummy();

  @Test
  public void getRandomIntRangeCheck() {
    assertTrue(r.getRandomInt(1, 4) >= 1 && r.getRandomInt(1, 4) <= 4);
    assertTrue(r.getRandomInt(0, 10) >= 0 && r.getRandomInt(0, 10) <= 10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getRandomIntMinValueCheck() {
    r.getRandomInt(5, 2);
  }

  @Test
  public void setDummyRandomValue() {
    List<Integer> r = new ArrayList<>();
    r.add(1);
    r.add(2);
    r.add(3);
    r.add(4);
    r.add(5);
    r1.setDummyRandomValue(r);
    assertEquals(1, r1.getRandomInt(1, 6));
    assertEquals(2, r1.getRandomInt(1, 6));
    assertEquals(3, r1.getRandomInt(1, 6));
    assertEquals(4, r1.getRandomInt(1, 6));
    assertEquals(5, r1.getRandomInt(1, 6));
  }

  @Test(expected = IllegalArgumentException.class)
  public void getRandomIntMinValueCheckDummy() {
    r1.getRandomInt(5, 2);
  }
}