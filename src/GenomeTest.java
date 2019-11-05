import org.junit.Test;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.assertEquals;


public class GenomeTest {

    @Test
    public void constructorTest() {

        // create genome
        Genome g = new Genome(2);

        // get key set
        Set<Key> actualKeys = g.getGenotype().keySet();

        // expected key set
        Set<Key> expectedKeys = new HashSet<>();
        expectedKeys.add(new Key(0, 0));
        expectedKeys.add(new Key(0, 1));
        expectedKeys.add(new Key(0, 2));
        expectedKeys.add(new Key(0, 3));
        expectedKeys.add(new Key(1, 0));
        expectedKeys.add(new Key(1, 1));
        expectedKeys.add(new Key(1, 2));
        expectedKeys.add(new Key(1, 3));
        expectedKeys.add(new Key(2, 0));
        expectedKeys.add(new Key(2, 1));
        expectedKeys.add(new Key(2, 2));
        expectedKeys.add(new Key(2, 3));
        expectedKeys.add(new Key(3, 0));
        expectedKeys.add(new Key(3, 1));
        expectedKeys.add(new Key(3, 2));
        expectedKeys.add(new Key(3, 3));

        // assert key sets match
        assertEquals(expectedKeys, actualKeys);

    }

    @Test
    public void testConstructChild() {

        // create parent 1 genome
        Genome parent1 = new Genome(2);

        // get parent 1 genome
        HashMap<Key, Integer> p1Map = parent1.getGenotype();

        //unit 0, 0
        p1Map.replace(new Key(0, 0), 0);
        p1Map.replace(new Key(0, 1), 0);
        p1Map.replace(new Key(1, 0), 0);
        p1Map.replace(new Key(1, 1), 0);

        //unit 0, 1
        p1Map.replace(new Key(0, 2), 1);
        p1Map.replace(new Key(0, 3), 1);
        p1Map.replace(new Key(1, 2), 1);
        p1Map.replace(new Key(1, 3), 1);

        //unit 1, 0
        p1Map.replace(new Key(2, 0), 2);
        p1Map.replace(new Key(2, 1), 2);
        p1Map.replace(new Key(3, 0), 2);
        p1Map.replace(new Key(3, 1), 2);

        //unit 1, 1
        p1Map.replace(new Key(2, 2), 3);
        p1Map.replace(new Key(2, 3), 3);
        p1Map.replace(new Key(3, 2), 3);
        p1Map.replace(new Key(3, 3), 3);

        parent1.setGenotype(p1Map);

        // create parent 2 genome
        Genome parent2 = new Genome(2);

        // get parent 1 genome
        HashMap<Key, Integer> p2Map = parent2.getGenotype();

        //unit 0, 0
        p2Map.replace(new Key(0, 0), 3);
        p2Map.replace(new Key(0, 1), 3);
        p2Map.replace(new Key(1, 0), 3);
        p2Map.replace(new Key(1, 1), 3);

        //unit 0, 1
        p2Map.replace(new Key(0, 2), 2);
        p2Map.replace(new Key(0, 3), 2);
        p2Map.replace(new Key(1, 2), 2);
        p2Map.replace(new Key(1, 3), 2);

        //unit 1, 0
        p2Map.replace(new Key(2, 0), 1);
        p2Map.replace(new Key(2, 1), 1);
        p2Map.replace(new Key(3, 0), 1);
        p2Map.replace(new Key(3, 1), 1);

        //unit 1, 1
        p2Map.replace(new Key(2, 2), 0);
        p2Map.replace(new Key(2, 3), 0);
        p2Map.replace(new Key(3, 2), 0);
        p2Map.replace(new Key(3, 3), 0);

        parent2.setGenotype(p2Map);

        // create child genome
        Genome child = new Genome(2, parent1, parent2, 1, 1);

        // test
        HashMap<Key, Integer> expectedValues = new HashMap<>();

        //unit 0, 0
        expectedValues.put(new Key(0, 0), 0);
        expectedValues.put(new Key(0, 1), 0);
        expectedValues.put(new Key(1, 0), 0);
        expectedValues.put(new Key(1, 1), 0);

        //unit 0, 1
        expectedValues.put(new Key(0, 2), 1);
        expectedValues.put(new Key(0, 3), 1);
        expectedValues.put(new Key(1, 2), 1);
        expectedValues.put(new Key(1, 3), 1);

        //unit 1, 0
        expectedValues.put(new Key(2, 0), 2);
        expectedValues.put(new Key(2, 1), 2);
        expectedValues.put(new Key(3, 0), 2);
        expectedValues.put(new Key(3, 1), 2);

        //unit 1, 1
        expectedValues.put(new Key(2, 2), 0);
        expectedValues.put(new Key(2, 3), 0);
        expectedValues.put(new Key(3, 2), 0);
        expectedValues.put(new Key(3, 3), 0);

        HashMap<Key, Integer> actualValues = child.getGenotype();

        assertEquals(expectedValues, actualValues);

    }

    @Test
    public void testCalcFitness() throws Exception {

        // create genome
        Genome g = new Genome(2);

        HashMap<Key, Integer> map = g.getGenotype();

        //unit 0,0
        map.replace(new Key(0, 0), 1);
        map.replace(new Key(0, 1), 3);
        map.replace(new Key(1, 0), 2);
        map.replace(new Key(1, 1), 4);

        //unit 0,1
        map.replace(new Key(2, 0), 1);
        map.replace(new Key(2, 1), 3);
        map.replace(new Key(3, 0), 2);
        map.replace(new Key(3, 1), 4);

        //unit 1,0
        map.replace(new Key(0, 2), 2);
        map.replace(new Key(0, 3), 4);
        map.replace(new Key(1, 2), 1);
        map.replace(new Key(1, 3), 3);

        //unit 1,1
        map.replace(new Key(2, 2), 2);
        map.replace(new Key(2, 3), 1);
        map.replace(new Key(3, 2), 4);
        map.replace(new Key(3, 3), 3);

        double expectedScore = 0.5;
        double actualScore = g.calcFitness(2);

        assertEquals(expectedScore, actualScore, 0.0001);

    }

}
