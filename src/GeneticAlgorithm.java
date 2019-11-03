import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


/************************************************************************************
 * A genetic algorithm
 ***********************************************************************************/
public class GeneticAlgorithm {

    // size of sudoku board
    private int N;

    // GA parameters
    private final int POP_SIZE = 10000;
    private final int TOURNAMENT_SIZE = 5;
    private final int MUTATION_RATE = 80;

    // Genomes
    private ArrayList<Genome> population; // entire population of genomes

    // Genome population stats
    private double bestFitness;
    private Genome fittestGenome;
    private boolean solutionFound;

    private static Random r;


    /********************************************************************************
     * Constructor. Initializes stats & params and creates the starting
     * population of genomes.
     *******************************************************************************/
    public GeneticAlgorithm(int N){

        this.N = N;

        this.population = new ArrayList<>();

        this.bestFitness = 0;
        this.fittestGenome = null;
        this.solutionFound = false;

        this.r = new Random();

        // create random initial population
        Genome g;

        for (int i = 0; i < POP_SIZE; i++) {
            g = new Genome(N);
            population.add(g);
        }

        Collections.sort(population);

    }


    /********************************************************************
     * Evolve population.
     *******************************************************************/
    public void epoch() throws Exception {

        double previousBest = bestFitness;

//            if (genCount % 100 == 0) {
//                // report results
//                System.out.println("generations: " + genCount + " best fitness: " + bestFitness);
//            }

            //reset the stat reporting variables
            bestFitness = 0;

            // Recombination
            ArrayList<Genome> tempPop = new ArrayList<>(population); //start with existing pop
            int numChildrenToCreate = POP_SIZE / 2;

            while (tempPop.size() < (double) population.size() + numChildrenToCreate) {
                ArrayList<Genome> winners = tournament(population);
                ArrayList<Genome> children = recombination(winners);
                tempPop.addAll(children);
            }

            // make sure to keep top 1% (or 1 if 1% is less than 1)
            int onePercent = (int) (POP_SIZE * 0.01);
            if (onePercent < 1) onePercent = 1;

            ArrayList<Genome> theOnePercent = null;

            try {
                theOnePercent = new ArrayList<>(grabNBest(onePercent, 1, tempPop));
            } catch (Exception e) {
                e.printStackTrace();
            }

            tempPop.removeAll(theOnePercent);

            // chose the rest of the survivors via tournament selection
            ArrayList<Genome> survivors = new ArrayList<>();

            while (POP_SIZE - onePercent > survivors.size()) {
                ArrayList<Genome> winners = tournament(tempPop);
                survivors.addAll(winners);
            }

            population = new ArrayList<>(survivors);

            // Randomly select some survivors to mutate
            for (Genome g : population) {
                if (r.nextInt(101) <= MUTATION_RATE); {
                    g.mutate(N);
                }
            }

            //add the top performers back in
            population.addAll(theOnePercent);

            Collections.sort(population);

            //calculate best, worst, average and total fitness
            calculateBestWorstAvgTotal();

            //catch errors
            if (previousBest > bestFitness) throw new Exception ("previous best: " + previousBest + " new best: "
                    + bestFitness);

            if (population.size() != POP_SIZE) throw new Exception("Population size: " + population.size()
                    + " Population should equal " + POP_SIZE);

            if (population.get(0).getFitnessScore() == 1) {
                solutionFound = true;
            }

    }


    /********************************************************************
     * Tournament selection. Returns top 2.
     *******************************************************************/
    private ArrayList<Genome> tournament(ArrayList<Genome> tempPop) {

        ArrayList<Genome> tournamentGroup = new ArrayList<>();
        r = new Random();

        while (tournamentGroup.size() < TOURNAMENT_SIZE) {
            int i = r.nextInt(tempPop.size());
            if (!tournamentGroup.contains(tempPop.get(i))) {
                tournamentGroup.add(tempPop.get(i));
            }
        }

        Collections.sort(tournamentGroup);

        ArrayList<Genome> winners = new ArrayList<>();
        winners.add(tournamentGroup.get(0));
        winners.add(tournamentGroup.get(1));

        return winners;

    }


    /********************************************************************
     * Recombination helper method for creating children.
     * Swaps units of 2 parents for 2 children.
     *******************************************************************/
    private ArrayList<Genome> recombination(ArrayList<Genome> parents) {

        Genome parent1 = parents.get(0);
        Genome parent2 = parents.get(1);

        // choose random crossover col and row
        r = new Random();
        int crossoverUnitCol = r.nextInt(N);
        int crossoverUnitRow = r.nextInt(N);

        // create children
        Genome child1 = new Genome(N, parent1, parent2, crossoverUnitCol, crossoverUnitRow);
        Genome child2 = new Genome(N, parent2, parent1, crossoverUnitCol, crossoverUnitRow);

        ArrayList<Genome> children = new ArrayList<>();
        children.add(child1);
        children.add(child2);

        return children;
    }


    /********************************************************************************
     * Get N Best chromosomes. Inserts numCopies copies of the nBest most fittest
     * genomes into a population vector.
     ************a*******************************************************************/
    public ArrayList<Genome> grabNBest(int nBest, int numCopies,
                                    ArrayList<Genome> population) throws Exception {

        if (population.size() == 0) throw new Exception("population must be " +
                "greater than 0");

        if (nBest > population.size()) throw new Exception("nBest cannot be " +
                "larger than the population size");

        //add the required amount of copies of the n most fittest to the supplied vector
        ArrayList<Genome> newPop = new ArrayList<>();

        Collections.sort(population);

        int index = 0;

        while (newPop.size() < nBest) {

            int copiesCount = 0;
            while(copiesCount < numCopies && newPop.size() < nBest) {
                newPop.add(population.get(index));
                copiesCount++;
            }

            index++;

        }

        return newPop;
    }


    /********************************************************************************
     * Calculate best, worst, average and total fitness scores
     *******************************************************************************/
    private void calculateBestWorstAvgTotal() {

        double highestSoFar = 0;
        double lowestSoFar = Double.MAX_VALUE;

        for (Genome g : population) {

            double fitness = g.getFitnessScore();

            //update fittest if necessary
            if (fitness > highestSoFar) {
                fittestGenome = g;
                highestSoFar = fitness;
            }

            //update worst if necessary
            if (fitness < lowestSoFar) lowestSoFar = fitness;

        }

        bestFitness	 = highestSoFar;

    }


    /********************************************************************************
     * Getters & Setters
     *******************************************************************************/
    public ArrayList<Genome> getPopulation() {
        return population;
    }

    public Genome getFittestGenome() {
        return fittestGenome;
    }

    public boolean getSolutionFound() {
        return  solutionFound;
    }

}
