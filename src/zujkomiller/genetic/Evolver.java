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

package zujkomiller.genetic;

import java.lang.reflect.*;
import java.util.*;

/**
 * Evolver controls the execution of the genetic algorithm. 
 * It implements the EvolutionObservable interface so that classes 
 * implementing EvolutionObserver may register themselves with it and be 
 * notified when new generations are created and tested for fitness.  
 * The Evolver uses a ProducerFactory to find the correct GenerationProducer 
 * needed to create each generation.  (There are different GenerationProducers
 * for each type of Chromosome.)  After a generation has been created, 
 * the Evolver passes it to an implementation of the FitnessTester interface 
 * which assigns fitness scores to each individual Chromosome in the generation.
 */
public class Evolver implements EvolutionObservable {

    public static final short LINEAR = 0;
    public static final short LEAF_TREE = 1;
    public static final short NODE_TREE = 2;

    private GenerationProducer producer = null;
    private FitnessTester tester = null;
    private short chromType = LINEAR;
    private short alphabetSize = 0;
    private short minChromLength = 0;
    private short maxChromLength = 0;
    private short populationSize = 0;
    private short mutationsPerGen = 0;
    private short crossoversPerGen = 0;
    private short numToReplicate = 0;
    private short numOfTimes = 0;
    private short numOfGenerations = 0;
    private int stopAtScore = 0;
    private boolean doStopAtScore = true;
    private boolean allowDuplicates = false;

    // keep track of all top scoring Chromosomes
    // and what the top score is so far
    private ArrayList topScorers = new ArrayList();
    private int topScoreSoFar = 0;
    private int numOfGenerationsRun = 0;

    // keep track of observers
    private ArrayList observers = new ArrayList();
    private Object observableObject = new Object();

    /**
     * Sets the type of chromosome which will be used.
     * This should be one of the constants defined in
     * this class: LINEAR, LEAF_TREE, or NODE_TREE.
     * A LINEAR chromosome has all of its genes on
     * the same plane, in a sequence.  A LEAF_TREE
     * arranges its genes in a tree shape with all
     * of the gene values at the leaves of the tree
     * and no values at the inner nodes.  A NODE_TREE
     * arranges its genes in a tree shape with
     * the gene values at all of the nodes including
     * the root and the leaves.
     */
    public void setChromosomeType(short type) {
        this.chromType = type;
    }

    /**
     * Sets the number of different values a single
     * gene can hold.  For instance, if size is 2, then
     * a gene will have either a one or a zero for a
     * value.  If the size is 100, then a gene can have
     * a value from zero to ninety-nine.
     */
    public void setAlphabetSize(short size) {
        this.alphabetSize = size;
    }

    /**
     * If the value passed in as a parameter is true, then
     * duplicate genes are allowed in a Chromosome, otherwise
     * duplicate genes will not be allowed.  (If duplicates
     * are not allowed, then the size of the maximum number
     * of genes per chromosome can not be greater than the
     * alphabet size.)
     */
    public void setDuplicateGenesAllowed(boolean allowDuplicates) {
        this.allowDuplicates = allowDuplicates;
    }

    /**
     * Sets minimum and maximum number of genes which
     * can exist in any Chromosome.  To make fixed-length
     * chromosomes, set min and max to the same number.
     */
    public void setNumOfGenesPerChrom(short min, short max) {
        this.minChromLength = min;
        this.maxChromLength = max;
    }

    /**
     * Each generation will have X number of Chromosomes
     * in int where X = the size passed in as a parameter.
     */
    public void setPopulationSize(short size) {
        this.populationSize = size;
    }

    /**
     * Sets the number of mutations to create when
     * producing one generation from a parent generation.
     */
    public void setMutationsPerGeneration(short numOfMutations) {
        this.mutationsPerGen = numOfMutations;
    }

    /**
     * Sets the number of crossovers to do when
     * producing one generation from a parent generation.
     */
    public void setCrossoversPerGeneration(short numOfCrossovers) {
        this.crossoversPerGen = numOfCrossovers;
    }

    /**
     * Sets the number of top-fitness-scoreres in each generation
     * to replicate when creating the next generation.
     */
    public void setNumToReplicate(short numToReplicate) {
        this.numToReplicate = numToReplicate;
    }

    /**
     * Sets the number of times to replicate each top-fitness-scorer
     * in each generation when creating the next generation.
     */
    public void setNumOfTimesToReplicateEach(short numOfTimes) {
        this.numOfTimes = numOfTimes;
    }

    /**
     * This many generations will be produced.
     */
    public void setNumOfGenerationsToCreate(short numOfGenerations) {
        this.numOfGenerations = numOfGenerations;
    }

    /**
     * If this is called, then processing will stop as soon as
     * a Chromosome is created which reaches the score passed in
     * as a parameter.
     */
    public void setStopAtScore(int score) {
        this.stopAtScore = score;
    }

    /**
     * If this is set to true, then processing will end as
     * soon as a Chromosome is created which has a fitness
     * score greater than or equal to the "stopAtScore"
     * values, otherwise, processing will continue until
     * X number of generations have been run where
     * X = this.numOfGenerations.
     */
    public void setDoStopAtScore(boolean doStop) {
        this.doStopAtScore = doStop;
    }

    /**
     * @param tester The fitness tester which will be used to
     *  provide a fitness score to all Chromosomes created.
     */
    public void setFitnessTester(FitnessTester tester) {
        this.tester = tester;
    }

    /**
     * @return An array of Chromosomes which have each scored
     *  the top score which has been achieved so far.  These
     *  Chromosomes may have been created in different generations.
     */
    public Chromosome[] getTopScorers() {
        int size = this.topScorers.size();
        Chromosome[] chroms = new Chromosome[size];
        for (int n=0; n<size; n++) {
            chroms[n] = (Chromosome)this.topScorers.get(n);
        }
        return chroms;
    }

    /**
     * @return The top score that has been achieved by any
     *  Chromosome created so far.
     */
    public int getTopScore() {
        return this.topScoreSoFar;
    }

    /**
     * @return The number of generations which have been run so far.
     */
    public int getNumberOfGenerationsRun() {
        return this.numOfGenerationsRun;
    }

    /**
     * Adds an EvolutionObserver to this EvolutionObservable.
     * When a new generation has been created and tested for
     * fitness, the EvolutionObserver passed in as a parameter
     * will be notified.
     *
     * @param observer An EvolutionObserver which wishes to
     *  be notified when a generation has been created and
     *  tested for fitness.
     */
    public void addEvolutionObserver(EvolutionObserver observer) {
        if (!(observers.contains(observer))) {
            synchronized (this.observableObject) {
                observers.add(observer);
            }
        }
    }

    /**
     * Removes the given EvolutionObserver from the set of
     * observers.
     *
     * @param observer The EvolutionObserver to remove
     *  from the set of observers.
     */
    public synchronized void removeEvolutionObserver(EvolutionObserver observer) {
        synchronized (this.observableObject) {
            observers.remove(observer);
        }
    }

    /**
     * This method begins processing of the generations and
     * returns the final Generation produced.
     */
    public Chromosome[] evolve() {
        Chromosome[] finalGeneration = null;
        if (getReadyToEvolve()) {
            createProducer();
            Chromosome[] initialPopulation =
                this.producer.createInitialGeneration(this.populationSize);
            Chromosome[] nextGen = initialPopulation;
            boolean lastGenerationProcessed = false;
            for (int n=1; n<this.numOfGenerations; n++) {
                // test the population
                for (int i=0; i<nextGen.length; i++) {
                    int score = tester.getFitnessScore(nextGen[i]);
                    nextGen[i].setFitnessScore(score);
                }

                // keep track of the top scoring Chromosomes
                // and what the top score is for each generation
                trackTopScorers(nextGen);

                this.numOfGenerationsRun++;

                boolean isLastGeneration =
                    this.doStopAtScore && this.topScoreSoFar >= this.stopAtScore;

                // let the observers know that a generation was created
                // and has been tested for fitness
                Generation gen = new Generation(nextGen, this.numOfGenerationsRun);
                synchronized (this.observableObject) {
                    int size = this.observers.size();
                    for (int i=0; i<size; i++) {
                        ((EvolutionObserver)observers.get(i)).
                            generationCreated(gen, this.topScoreSoFar,
                                isLastGeneration, this);
                    }
                }

                // if we've reached the stopAtScore value,
                // then break the loop if doStopAtScore is true
                if (isLastGeneration) {
                    lastGenerationProcessed = true;
                    break;
                }

                nextGen = producer.getNextGeneration(nextGen);
            }

            if (!lastGenerationProcessed) {
                // put fitness scores on the finalGeneration
                finalGeneration = nextGen;
                for (int i=0; i<nextGen.length; i++) {
                    int score = tester.getFitnessScore(nextGen[i]);
                    nextGen[i].setFitnessScore(score);
                }
                // keep track of the top scoring Chromosomes
                // and what the top score is for each generation
                trackTopScorers(nextGen);

                this.numOfGenerationsRun++;

                // let the observers know that the finalGeneration
                //  was created and has been tested for fitness
                Generation gen = new Generation(finalGeneration,
                    this.numOfGenerationsRun);
                synchronized (this.observableObject) {
                    int size = this.observers.size();
                    for (int i=0; i<size; i++) {
                        ((EvolutionObserver)observers.get(i)).
                            generationCreated(gen, this.topScoreSoFar,
                                true, this);
                    }
                }
            } // end if lastGenerationProcessed not processed yet
        }
        return finalGeneration;
    }

    private void createProducer() {
        ProducerFactory factory = new ProducerFactory();
        Class producerClass = factory.getProducerClass
            (this.minChromLength, this.maxChromLength,
                this.allowDuplicates, this.chromType);
        Constructor[] constructors = producerClass.getConstructors();
        Constructor constructor = constructors[0];
        Object[] args = new Object[5];
        short[] alphabet = new short[this.alphabetSize];
        for (short n=0; n<this.alphabetSize; n++) {
            alphabet[n] = n;
        }
        try {
            this.producer = (GenerationProducer)constructor.newInstance
                (new Object[]{alphabet, new Integer(this.numToReplicate),
                    new Integer(this.numOfTimes), new Integer(this.crossoversPerGen),
                    new Integer(this.mutationsPerGen), new Integer(this.minChromLength),
                    new Integer(this.maxChromLength)});
        } catch (Exception tie) {
            tie.printStackTrace();
        }
    }

    private boolean getReadyToEvolve() {
        boolean ready = true;
        ready = ready
            && (this.tester != null)
            && (this.alphabetSize > 0)
            && (this.minChromLength > 0)
            && (this.minChromLength <= this.maxChromLength)
            && (this.numOfGenerations > 0)
            && (this.numToReplicate > 0)
            && (this.numOfTimes > 0)
            && (this.populationSize > 0);
        return ready;
    }

    private void trackTopScorers(Chromosome[] generation) {
        if (generation.length > 0) {
            // sort the generation so the top scorers are at the
            // beginning of the array
            Arrays.sort(generation, 0, generation.length, generation[0]);
            int newTopScore = generation[0].getFitnessScore();
            if (newTopScore > this.topScoreSoFar) {
                this.topScorers.clear();
            }
            if (newTopScore >= this.topScoreSoFar) {
                this.topScoreSoFar = newTopScore;
                for (int n=0; n<generation.length; n++) {
                    if (generation[n].getFitnessScore() == this.topScoreSoFar) {
                        if (!(this.topScorers.contains(generation[n]))) {
                            this.topScorers.add(generation[n]);
                        }
                    } else {
                        break;
                    }
                }
            }
        }
    }

}