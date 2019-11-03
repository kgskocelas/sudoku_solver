/************************************************************************************
 * A key object composed of a column number & row number
 ***********************************************************************************/
public class Key {

    private final int colNum;
    private final int rowNum;


    /********************************************************************************
     * Constructs a key.
     *
     * @param colNum
     * @param rowNum
     *******************************************************************************/
    public Key (int colNum, int rowNum) {
        this.colNum = colNum;
        this.rowNum = rowNum;
    }


    /********************************************************************************
     * Determines if two keys are the same.
     *
     * @return true if strings match
     *******************************************************************************/
    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (!(o instanceof Key)) return false;

        Key key = (Key) o;

        return (colNum == key.colNum) && (rowNum == key.rowNum);
    }


    /********************************************************************************
     * Hashcode generator.
     *
     * @return hashcode - same for two objects if they have the same row # & col #
     *******************************************************************************/
    @Override
    public int hashCode() {
        return 31 * colNum + rowNum;
    }


    /********************************************************************************
     * String generator.
     *
     * @return key values in readable format
     *******************************************************************************/
    @Override
    public String toString() {
        return "col: " + colNum + " row: " + rowNum;
    }

}