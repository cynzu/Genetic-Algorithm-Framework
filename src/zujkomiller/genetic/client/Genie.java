/*
 * Genetic Algorithm Framework
 * Copyright (c) 2001, 2002, 2003 by Cynthia Zujko-Miller
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 2 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * for more information, contact the author: cynzu@yahoo.com
 */

package zujkomiller.genetic.client;

import zujkomiller.genetic.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

/**
 * This is the main class used to start the program.
 * Its main method should be passed the full path name
 * to the .properties file which lists the parameters
 * used to run the program.
 */
public class Genie implements EvolutionObserver {

    private final Properties initParams = new Properties();
    private String initParamsFileName = "";
    private Evolver evolver;

    /** where to output the results */
    private File outputFile = null;
    private FileWriter fileWriter = null;

    /**
     * if outputResultOnly is true, then the
     * value of outputGenerations is ignored
     */
    private boolean outputResultOnly = false;

    /** print every 50th generation */
    private int outputGenerations = 50;

    /**
     * if showProgressOnScreen is true, then
     * the number of generations produced and the top
     * core created so far will be printed to the screen
     * everytime a generation is created and scored.
     */
    private boolean showProgressOnScreen = true;

    /**
     * @param fileName The full path name to the .properties
     *  file listing the parameters under which the program
     *  will run.
     */
    public Genie(String fileName) {
        try {
            initParamsFileName = fileName;
            initParams.load(new FileInputStream(fileName));
            this.loadOutputInfo();
            writeParamsToOutput();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        }

        this.createEvolver();
        this.evolver.addEvolutionObserver(this);
        this.beginEvolution();
    }

    private void loadOutputInfo() throws IOException {
        String outputFileName = initParams.getProperty("outputFile");
        this.outputFile = new File(outputFileName);
        if (!(this.outputFile.exists())) {
            outputFile.createNewFile();
        }
        this.fileWriter = new FileWriter(this.outputFile);
        this.outputResultOnly = ((initParams.getProperty("outputResultOnly").
            equalsIgnoreCase("true")));
        this.outputGenerations = Integer.parseInt(initParams.
            getProperty("outputGenerations"));
        this.showProgressOnScreen = ((initParams.
            getProperty("showProgressOnScreen").equalsIgnoreCase("true")));
    }

    private void writeParamsToOutput() throws IOException,
            FileNotFoundException {
        FileReader fileReader = new FileReader(initParamsFileName);
        BufferedReader reader = new BufferedReader(fileReader);
        try {
            this.fileWriter.write("***** PARAMETERS *****\n\n");
            String line = reader.readLine();
            while (line != null) {
                if (!(line.startsWith("#")) && !(line.trim().length() == 0)) {
                    this.fileWriter.write(line + "\n");
                    this.fileWriter.flush();
                }
                line = reader.readLine();
            }
            this.fileWriter.write("\n***** END OF PARAMETERS *****\n\n");
            this.fileWriter.flush();
        } catch (EOFException eof) {
            // do nothing, reached the end of the file
        }
    }

    private void createEvolver() {
        try {
            short chromType =
                getChromType(initParams.getProperty("chromType"));
            short alphabetSize =
                Short.parseShort(initParams.getProperty("alphabetSize"));
            short minChromLength =
                Short.parseShort(initParams.getProperty("minChromLength"));
            short maxChromLength =
                Short.parseShort(initParams.getProperty("maxChromLength"));
            short populationSize =
                Short.parseShort(initParams.getProperty("populationSize"));
            short mutationsPerGen =
                Short.parseShort(initParams.getProperty("mutationsPerGen"));
            short crossoversPerGen =
                Short.parseShort(initParams.getProperty("crossoversPerGen"));
            short numToReplicate =
                Short.parseShort(initParams.getProperty("numToReplicate"));
            short numOfTimes =
                Short.parseShort(initParams.getProperty("numOfTimes"));
            short numOfGenerations =
                Short.parseShort(initParams.getProperty("numOfGenerations"));
            short stopAtScore =
                Short.parseShort(initParams.getProperty("stopAtScore"));
            boolean allowDuplicates = ((initParams.getProperty("allowDuplicates")
                .equalsIgnoreCase("true")));
            boolean doStopAtScore = ((initParams.getProperty("doStopAtScore")
                .equalsIgnoreCase("true")));
            String testerClassName = initParams.getProperty("fitnessTester");
            Class theClass = Class.forName(testerClassName);
            FitnessTester fitnessTester = (FitnessTester)theClass.newInstance();

            this.evolver = new Evolver();
            evolver.setAlphabetSize(alphabetSize);
            evolver.setChromosomeType(chromType);
            evolver.setCrossoversPerGeneration(crossoversPerGen);
            evolver.setDuplicateGenesAllowed(allowDuplicates);
            evolver.setFitnessTester(fitnessTester);
            evolver.setMutationsPerGeneration(mutationsPerGen);
            evolver.setNumOfGenerationsToCreate(numOfGenerations);
            evolver.setNumOfGenesPerChrom(minChromLength, maxChromLength);
            evolver.setNumOfTimesToReplicateEach(numOfTimes);
            evolver.setNumToReplicate(numToReplicate);
            evolver.setPopulationSize(populationSize);
            evolver.setStopAtScore(stopAtScore);
            evolver.setDoStopAtScore(doStopAtScore);

            String observers = initParams.getProperty("observers");
            if (observers != null && !(observers.equalsIgnoreCase("none"))) {
                StringTokenizer tokenizer = new StringTokenizer(observers, ",");
                String token = "";
                while(tokenizer.hasMoreTokens()) {
                    token = tokenizer.nextToken();
                    Class observerClass = Class.forName(token.trim());
                    EvolutionObserver observer =
                        (EvolutionObserver)observerClass.newInstance();
                    evolver.addEvolutionObserver(observer);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void beginEvolution() {
        evolver.evolve();
    }

    private short getChromType(String property) {
        short type = Evolver.LINEAR;
        if (property.equalsIgnoreCase("LINEAR")) {
            type = Evolver.LINEAR;
        } else if (property.equalsIgnoreCase("LEAF_TREE")) {
            type = Evolver.LEAF_TREE;
        } else if (property.equalsIgnoreCase("NODE_TREE")) {
            type = Evolver.NODE_TREE;
        } else {
            // >>> THROW AN EXCEPTION HERE
            System.out.println("Invalid chromType in the properties file.");
        }
        return type;
    }

    /**
     * This method is called by an EvolutionObservable
     * when a new generations has been created and tested
     * for fitness.
     *
     * @param generation The generation that was just created.
     * @param generationNumber The number of the generation (ex:
     *  if this is the second generation created, the generationNumber
     *  will be 2).
     * @param topScoreSoFar The top score achieved so far by any
     *  Chromosome in any generation.
     * @param isLastGeneration This will be true only when no other
     *  generations will be created after the one passed in as a param.
     * @param observabe The EvoluitonObservable object which called
     *  this method.  It can be queried for more information.
     */
    public void generationCreated(Generation gen, int topScoreSoFar,
            boolean isLastGeneration, EvolutionObservable observable) {
        int generationNumber = gen.getGenerationNumber();
        Chromosome[] generation = gen.getIndividuals();
        if (this.showProgressOnScreen) {
            System.out.println("generationNumber: " + generationNumber);
            System.out.println("topScoreSoFar: " + topScoreSoFar);
        }
        if (this.outputResultOnly == false && !isLastGeneration) {
            if (generationNumber % this.outputGenerations == 0 ||
                    generationNumber == 1) {
                try {
                    Arrays.sort(generation, 0, generation.length, generation[0]);
                    this.fileWriter.write("Generation " + generationNumber + ": \n");
                    for (int j=0; j<generation.length; j++) {
                        this.fileWriter.write("\t" + (j+1) + " - " + generation[j] +
                             " score: " + generation[j].getFitnessScore() + "\n");
                    }
                    this.fileWriter.write("\n");
                    this.fileWriter.flush();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        if (isLastGeneration) {
            try {
                Arrays.sort(generation, 0, generation.length, generation[0]);
                this.fileWriter.write("Generation " + generationNumber + ": \n");
                for (int j=0; j<generation.length; j++) {
                    this.fileWriter.write("\t" + (j+1) + " - " + generation[j] +
                         " score: " + generation[j].getFitnessScore() + "\n");
                }
                this.fileWriter.write("\n");

                int numOfGensRun = evolver.getNumberOfGenerationsRun();
                Chromosome[] topScorers = evolver.getTopScorers();
                int topScore = evolver.getTopScore();

                this.fileWriter.write("\nnumber of generations run: " +
                    numOfGensRun + "\n");
                this.fileWriter.write("topScore: " + topScore + "\n");
                this.fileWriter.write("chromosomes which scored " + topScore + ": \n");
                Chromosome chrom = null;
                for (int n=0; n<topScorers.length; n++) {
                    chrom = topScorers[n];
                    this.fileWriter.write("\t" + topScorers[n] + "\n");
                }

                this.fileWriter.flush();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please pass the name of the .properties " +
                "file as an argument when starting this program.");
        } else {
            new Genie(args[0]);
        }

    }

}