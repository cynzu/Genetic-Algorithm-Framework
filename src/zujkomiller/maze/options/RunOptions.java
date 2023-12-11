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

package zujkomiller.maze.options;

import java.io.*;

/**
 * The RunOptions class provides a means for serializing
 * the parameters used for running a Genetic Algorithm
 * used to find the solution to a Maze.  Each member variable
 * corresponds to an entry in the .properties file which is
 * passed to the zujkomiller.genetic.client.Genie class which
 * is the starting point for running the genetic algorithm.
 */
public class RunOptions implements Serializable {
    // the identifier for this set of options
    private String name;
    
    // these three paramters are specific to the Maze application
    private String mazeName = "difficult";
    private int bonusPoints = 100;
    
    // the rest of the paramters are common to all genetic
    // algorithms run by the Genie application
    
    // these three parameters can not be changed by the end user
    // all Maze algorithms are linear and have an alphabet size
    // of four (N, S, E, and W), and therefore also have a minimum
    // chromosome length of four.
    private static final String chromType = "LINEAR"; 
    private static final int alphabetSize = 4; 
    private static final int minChromLength = 4;
    
    // If doStopAtScore is set to true, then the evolution process will
    // be halted as soon as the "stopAtScore" value is reached. If it is
    // set to false, then the "stopAtScore" value is ignored.
    private static final boolean doStopAtScore = false;
    private static final int stopAtScore = 100; // irrelevant

    // If duplicates are allowed then the Chromosome represents a "bag"
    // rather than a set in that its genes may contain duplicate values, 
    // for ex: {a, c, b, a, b, c}.  If duplicates are not allowed, then
    // the Chromosome represents a "set" in that its genes may not contain
    // any duplicate values, for ex: {a, b, c, d, e}.
    private static final boolean allowDuplicates = true;

    // fitnessTester should point to the fully qualified class name
    // which implements the zujkomiller.genetic.FitnessTester interface.
    // This class will be used to provide fiteness scores to each
    // Chromosome in every generation.
    private static final String fitnessTester = "zujkomiller.maze.MazeTester";

    // observers should point to either the value "none" or to a list of
    // the fully qualified class names of classes which implement the 
    // zujkomiller.genetic.EvolutionObserver interface. The list should
    // be separated by cammas.  These classes will be informed every time
    // a new generation is created.  They can be used to track progress
    // or to interpret results either as each generation is produced or
    // at the end of the evolution process.
    private static final String observers = "zujkomiller.maze.MazeTester";
    
        // If showProgressOnScreen is set to true then
    // the number of generations produced and the top
    // score created so far will be printed to the screen
    // everytime a generation is created and tested for fitness.
    private static final boolean showProgressOnScreen = false;

    // maxChromLength is the maximum length of a chromosome.
    private int maxChromLength = 50;
    
    // populationSize indicates the number of Chromosomes which will be
    // produced during each generation.
    private int populationSize = 20;
    
    // mutationsPerGen indicates the number of mutations which occur
    // when producing a child generation from a parent generation.
    private int mutationsPerGen = 50;
    
    // crossoversPerGen indicates the number of crossovers which occur
    // when producing a child generation from a parent generation.
    private int crossoversPerGen = 6;

    // numToReplicate indicates the top-scoring chromosomes from the
    // parent generation to replicate in the child generation before
    // mutations and crossovers are done.
    private int numToReplicate = 4;

    // numOfTimes indicates number of times that each of the top-scoring
    // chromosomes from the parent generation should be replicated in the
    // child generation before mutations and crossovers are done.
    private int numOfTimes = 2;

    // numOfGenerations indicates the maximum number of generations to
    // run.  If "doStopAtScore" is set to true, then the actual number
    // of generations produced may be less than numOfGenerations if
    // a "stopAtScore" value is achieved early on.
    private int numOfGenerations = 50;
    
    // Give the file name where the results will be printed.
    // This file must already exist before the program is run.
    // It will be over-written each time the program is run.
    private String outputFile = "../../maze/output/output.txt";

    // If outputResultOnly is set to true, then only the final
    // generation and top scorers will be printed to the outputFile,
    // and the value for outputGenerations (below) will be ignored.
    private boolean outputResultOnly = false;

    // If outputResultOnly (above) is set to false, then
    // every Nth generation will be printed to the outputFile
    // where N = the value specified by outputGenerations
    // (ex: if outputGenerations = 50 then every 50th
    // generation will be printed to the outputFile).
    // The final generation and top scorers will always
    // be printed to the outputFile, regardless of the value
    // of outputGenerations.
    private int outputGenerations = 5;


    public void setBonusPoints(int points) {
        this.bonusPoints = points;
    }
    
    public int getBonusPoints() {
        return this.bonusPoints;
    }
    
    public void setCrossoversPerGen(int num) {
        this.crossoversPerGen = num;
    }
    
    public int getCrossoversPerGen() {
        return this.crossoversPerGen;
    }
    
    public void setMaxChromLength(int max) throws InvalidOptionException {
        if (max < this.minChromLength) {
            throw new InvalidOptionException("The maximum chromosome length " +
                "must be greater than or equal to " + this.minChromLength);
        }
        this.maxChromLength = max;
    }
    
    public int getMaxChromLength() {
        return this.maxChromLength;
    }
    
    public void setMazeName(String name) {
        this.mazeName = name;
    }
    
    public String getMazeName() {
        return this.mazeName;
    }
    
    public void setMutationsPerGen(int num) throws InvalidOptionException {
        if (num < 0) {
            throw new InvalidOptionException("The number of mutations " +
                "per generation must be a positive number.");
        }
        this.mutationsPerGen = num;
    }

    public int getMutationsPerGen() {
        return this.mutationsPerGen;
    }
    
    public void setNumToReplicate(int num) throws InvalidOptionException {
        if (num < 0) {
            throw new InvalidOptionException("The number to replicate " +
                "must be a positive number.");
        }
        this.numToReplicate = num;
    }
    
    public int getNumToReplicate() {
        return this.numToReplicate;
    }
    
    public void setNumOfTimes(int num) throws InvalidOptionException {
        if (num <= 0) {
            throw new InvalidOptionException("The number times to replicate " +
                "must be at least one.");
        }
        this.numOfTimes = num;
    }
    
    public int getNumOfTimes() {
        return this.numOfTimes;
    }
    
    public void setNumOfGenerations(int num) throws InvalidOptionException {
        if (num <= 0) {
            throw new InvalidOptionException("The number of generations " +
                "must be at least one.");
        }
        this.numOfGenerations = num;
    }
    
    public int getNumOfGenerations() {
        return this.numOfGenerations;
    }
    
    public void setOutputFile(String fileName) {
        this.outputFile = fileName;
    }
    
    public String getOutputFile() {
        return this.outputFile;        
    }
    
    public void setOutputGenerations(int num) throws InvalidOptionException {
        if (num < 0) {
            throw new InvalidOptionException("The output generations value " +
                "must be a positive number.");
        }
        this.outputGenerations = num;
    }
    
    public int getOutputGenerations() {
        return this.outputGenerations;
    }
    
    public void setOutputResultOnly(boolean resultOnly) {
        this.outputResultOnly = resultOnly;
    }
    
    public boolean getOutputResultOnly() {
        return this.outputResultOnly;
    }
    
    public void setPopulationSize(int size) throws InvalidOptionException {
        if (size < 1) {
            throw new InvalidOptionException("The population size " +
                "must be at least one.");
        }
        this.populationSize = size;
    }
    
    public int getPopulationSize() {
        return this.populationSize;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
}