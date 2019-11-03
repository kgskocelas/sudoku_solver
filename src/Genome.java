import java.util.*;


/************************************************************************
 * A Sudoku board with N^2 columns and rows.
 ***********************************************************************/
public class Genome implements Comparable {

    private HashMap<Key, Integer> genotype;
    private double fitnessScore;
    private double maxError;
    private Random r;


    /********************************************************************
     * Construct a new random genome (sudoku board).
     *******************************************************************/
    public Genome(int N) {

        this.maxError = Math.pow(N, 4);
        this.genotype = new HashMap<>();

        int nSq = N * N;

        // Generate N^2 random units & add to their values to the genome
        for (int col = 0; col < nSq; col += N) {
            for (int row = 0; row < nSq; row += N) {

                // offsets
                int colOffset = col;
                int rowOffset = row;

                //generate unit
                Unit u = new Unit(N, colOffset, rowOffset);

                //add unit values to genome
                this.genotype.putAll(u.getUnitMap());

            }

        }

        // Calculate initial fitness score
        try {
            calcFitness(N);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }


    /********************************************************************
     * Construct a new child genome (sudoku board) from two parents.
     *******************************************************************/
    public Genome(int N, Genome parent1, Genome parent2, int crossoverUnitCol, int crossoverUnitRow) {

        this.maxError = Math.pow(N, 4);
        this.genotype = new HashMap<>();
        int nSq = N * N;

        // Get units from parents & add to their values to the genome
        HashMap<Key, Integer> parentMap1 = parent1.getGenotype();
        HashMap<Key, Integer> parentMap2 = parent2.getGenotype();

        // Add values to child
        this.genotype.putAll(parentMap1); // start with parent 1

        int colStart = crossoverUnitCol * N;
        int rowStart = crossoverUnitRow * N;

        for (int col = colStart; col < colStart + nSq; col++) {
            for (int row = rowStart; row < rowStart + nSq; row++) {

                //generate square's key
                Key k = new Key(col, row);

                //add parent 2's values to map
                this.genotype.replace(k, parentMap2.get(k)); // change part of child to match parent 2

            }
        }

        // Calculate initial fitness score
        try {
            calcFitness(N);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }


    /********************************************************************
     * Calculates genome's fitness score.
     *******************************************************************/
    public double calcFitness(int N) throws Exception {

        int nSq = N * N;

        // calc error
        double actualError = 0;

        // check for row errors
        for (int row = 0; row < (N * N); row++) {

            Set<Integer> rowSet = new HashSet<>();

            for (int col = 0; col < nSq; col++) {

                int value = genotype.get(new Key(col, row));
                rowSet.add(value);

            }

            actualError += nSq - rowSet.size();

        }

        // check for column errors
        for (int col = 0; col < nSq; col++) {

            Set<Integer> colSet = new HashSet<>();

            for (int row = 0; row < nSq; row++) {

                int value = genotype.get(new Key(col, row));
                colSet.add(value);

            }

            actualError += (N*N) - colSet.size();

        }

        // calc fitness
        double fitness = maxError - actualError;
        if (fitness < 0) throw new Exception("Fitness score cannot be negative.");

        this.fitnessScore = fitness / maxError;
        return  fitnessScore;
    }


    /********************************************************************
     * Mutate genotype.
     *******************************************************************/
    public void mutate(int N) {

        // randomly select a unit to mutate
        r = new Random();
        int unitRow = r.nextInt(N);
        int unitCol = r.nextInt(N);

        // randomly select two chosen locations in the unit
        int row1 = 0;
        int row2 = 0;
        int col1 = 0;
        int col2 = 0;

        while (row1 == row2 && col1 == col2) {
            row1 = r.nextInt(N) + (unitRow * N);
            row2 = r.nextInt(N) + (unitRow * N);
            col1 = r.nextInt(N) + (unitCol * N);
            col2 = r.nextInt(N) + (unitCol * N);
        }

        // swap their values
        Key k1 = new Key(row1, col1);
        Key k2 = new Key(row2, col2);

        int val1 = genotype.get(k1);
        int val2 = genotype.get(k2);

        genotype.replace(k1, val2);
        genotype.replace(k2, val1);

        try {
            calcFitness(N);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }


    /********************************************************************
     * Print genotype to terminal.
     *******************************************************************/
    public void printGenotype(int N) {

        int nSq = N * N;

        for (int row = 0; row < nSq; row++) {
            System.out.println();
            if (row % N == 0 && row != 0) {
                for (int i = 0; i < nSq; i++) {
                    if (i % N == 0 && i!= 0) System.out.print("+");
                    System.out.print("-");
                }
                System.out.println();
            }
            for (int col = 0; col < nSq; col++) {
                if (col % N == 0 && col != 0) System.out.print("|");
                System.out.print(genotype.get(new Key(col, row)));
            }
        }
        System.out.println();
        System.out.println();

    }


    /********************************************************************
     * Getters & Setters
     *******************************************************************/
    public HashMap<Key, Integer> getGenotype() {
        return genotype;
    }

    public void setGenotype(HashMap<Key, Integer> genotype) {
        this.genotype = genotype;
    }

    public double getFitnessScore() {
        return fitnessScore;
    }

    @Override
    public int compareTo(Object o) {
        Genome g2 = (Genome) o;
        return (int)(g2.getFitnessScore() * maxError) - (int)(fitnessScore * maxError);
    }

}
