import org.junit.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class UnitTest {

    @Test
    public void constructorTest() {

        // create unit
        Unit u = new Unit(2, 1, 2);

        // get key set
        Set<Key> actualKeys = u.getUnitMap().keySet();

        // expected key set
        Set<Key> expectedKeys = new HashSet<>();
        expectedKeys.add(new Key(1, 2));
        expectedKeys.add(new Key(1, 3));
        expectedKeys.add(new Key(2, 2));
        expectedKeys.add(new Key(2, 3));

        // assert key sets match
        assertEquals(expectedKeys, actualKeys);

    }

}
