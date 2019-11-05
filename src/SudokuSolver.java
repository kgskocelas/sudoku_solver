import java.io.IOException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/************************************************************************************
 * Solves for a valid sudoku board of the specified dimensions.
 ***********************************************************************************/
public class SudokuSolver {


    /********************************************************************************
     * Evolves the population over the set number of generations & outputs results.
     *******************************************************************************/
    public static void evolveGenomes(int N, int maxNumberGenerations, int numberOfRuns, String filename) throws Exception {

        //totals to be used for calculating avg best fitness at each epoch
        double[] bestFitness = new double[maxNumberGenerations];

        double sumGenSolved = 0;
        int minGen = maxNumberGenerations;
        int maxGen = 0;
        double sumTimeSolved = 0;
        double minTime = Double.MAX_VALUE;
        double maxTime = 0;
        double sumFinalMutationRate = 0;

        for (int run = 0; run < numberOfRuns; run++) {

            long startTime = System.nanoTime();

            // reset GA
            GeneticAlgorithm genAlg = new GeneticAlgorithm(N);
            int generationCounter = 1;

            double centenialBest = 0;

            // evolve population until solved
            while (generationCounter < maxNumberGenerations && genAlg.getSolutionFound() == false) {

                // execute 1 epoch
                genAlg.epoch();

                // results
                double best = genAlg.getFittestGenome().getFitnessScore();

                bestFitness[generationCounter] += best;
                System.out.println("generation " + generationCounter + " best fitness: " + best);

                if (generationCounter % 100 == 0) {

                    //compare to last centenialBest
                    if (centenialBest == best) {
                        genAlg.incrementMutationRate();
                    }
                    else {
                        centenialBest = best;
                    }
                }

                if (genAlg.getSolutionFound() == false) generationCounter++;

            }

            long endTime = System.nanoTime();

            // finish filling out results with 1's if ended before max # generations
            for (int j = generationCounter + 1; j < maxNumberGenerations; j++) {
                bestFitness[j] += 1.0;
            }

            // update reporting values
            long timeSolved = (endTime - startTime);
            sumTimeSolved += timeSolved;
            if (timeSolved < minTime) minTime = timeSolved;
            if (timeSolved > maxTime) maxTime = timeSolved;

            sumGenSolved += generationCounter;
            if (generationCounter < minGen) minGen = generationCounter;
            if (generationCounter > maxGen) maxGen = generationCounter;

            sumFinalMutationRate += genAlg.getMutationRate();

            // output results
            System.out.println("final mutation rate: " + genAlg.getMutationRate());
            System.out.println("Best genotype fitness: " + genAlg.getFittestGenome().getFitnessScore());
            System.out.println("Best solution genotype: ");
            genAlg.getFittestGenome().printGenotype(N);

        }

        // output reporting values
        double avgGensToSolve = sumGenSolved / numberOfRuns;
        double avgTimeToSolve = sumTimeSolved / numberOfRuns;
        double avgFinalMutationRate = sumFinalMutationRate / numberOfRuns;

        System.out.println("Avg time to solve: " + avgTimeToSolve);
        System.out.println("min time " + minTime);
        System.out.println("max time " + maxTime);
        System.out.println("Avg gen to solve: " + avgGensToSolve);
        System.out.println("min gen " + minGen);
        System.out.println("max gen " + maxGen);
        System.out.println("avg final mut rate: " + avgFinalMutationRate);

        // write results to csv file
        System.out.println("writing results to csv...");

        // save avg best fitness for each epoch to csv
        List<List<String>> results = new ArrayList<>();

        for(int epoch = 1; epoch<maxNumberGenerations;epoch++) {

            double avgBestFitness = bestFitness[epoch] / numberOfRuns;

            List<String> epochResults = Arrays.asList(
                    Integer.toString(epoch),
                    Double.toString(avgBestFitness)
            );
            results.add(epochResults);
        }

        try {
            writeToCSV(filename, results);
        } catch(IOException e) {
            e.printStackTrace();
        }

    }


    /********************************************************************************
     * Write results to CSV file.
     *******************************************************************************/
    public static void writeToCSV(String filename,List<List<String>> results) throws IOException {

        FileWriter csvWriter = null;
        try {
            csvWriter = new FileWriter(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //add column headers
        csvWriter.append("epoch");
        csvWriter.append(",");
        csvWriter.append("avg best fitness");
        csvWriter.append("\n");

        //add data
        for (List<String> rowData : results) {
            csvWriter.append(String.join(",", rowData));
            csvWriter.append("\n");
        }

        csvWriter.flush();
        csvWriter.close();

    }


    /********************************************************************
     * Run the solver.
     *******************************************************************/
    public static void main(String[] args) {

        try {
            evolveGenomes(3, 10000, 20, "results.csv");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}

