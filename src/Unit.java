import java.util.*;


/************************************************************************
 * A Sudoku board unit. Contains digits 1...N^2.
 ***********************************************************************/
public class Unit {

    private HashMap<Key, Integer> unitMap;
    private Random r;


    /********************************************************************
     * Construct a new random unit.
     *******************************************************************/
    public Unit(int N, int colOffset, int rowOffset) {

        // Make randomly ordered list of values 1...N^2
        ArrayList<Integer> values = new ArrayList<>();
        for (int i = 1; i <= N * N; i++) {
            values.add(i);
        }
        Collections.shuffle(values);

        // Add values to map with Key composed of row and column number
        this.unitMap = new HashMap<>();
        int valuesCount = 0;

        for (int col = 0; col < N; col++) {
            for (int row = 0; row < N; row++) {

                //generate square's key
                Key k = new Key(col + colOffset, row + rowOffset);

                //add to unit with value
                unitMap.put(k, values.get(valuesCount));

                valuesCount++;

            }
        }

    }

//    /********************************************************************
//     * Mutate unit.
//     *******************************************************************/
//
//    //FIXME doesn't work with unit values just being adding to overall genome map
//    public void mutate(int N) {
//
//        // randomly select two chosen values in the unit
//        r = new Random();
//        int val1 = 0;
//        int val2 = 0;
//
//        while (val1 == val2) {
//            val1 = r.nextInt(N);
//            val2 = r.nextInt(N);
//        }
//
//        // swap their locations
//        Key key1 = getKeyByValue(val1);
//        Key key2 = getKeyByValue(val2);
//
//        unitMap.remove(key1);
//        unitMap.remove(key2);
//
//        unitMap.put(key1, val2);
//        unitMap.put(key2, val1);
//
//    }
//
//
//    /********************************************************************
//     * Looks up the key that corresponds to a value in a 1:1 mapping.
//     *******************************************************************/
//    private Key getKeyByValue(Integer value) {
//        for (Map.Entry<Key, Integer> entry : unitMap.entrySet()) {
//            if (Objects.equals(value, entry.getValue())) {
//                return entry.getKey();
//            }
//        }
//        return null;
//    }


    /********************************************************************
     * Getters & Setters
     *******************************************************************/
    public HashMap<Key, Integer> getUnitMap() {
        return unitMap;
    }

    public void setUnitMap(HashMap<Key, Integer> unitMap) {
        this.unitMap = unitMap;
    }

}
