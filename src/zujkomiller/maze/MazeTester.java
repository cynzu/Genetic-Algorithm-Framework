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

package zujkomiller.maze;

import zujkomiller.genetic.*;
import zujkomiller.maze.model.*;
import zujkomiller.maze.results.*;
import zujkomiller.maze.persistence.MazeAccessor;

import java.io.IOException;
import java.util.*;

public class MazeTester implements FitnessTester, EvolutionObserver {

    private static final String PROP_FILE = "maze.bin.MazeGenetic";

    private static zujkomiller.maze.model.Maze maze = null;
    private static MazeEvolutionData data = null;
    private static Location entry = null;
    private static Location destination = null;
    private static int destinationX = 0;
    private static int destinationY = 0;
    private static int maxMovesPossible = 0;
    private static int bonus = 0;
    private static boolean outputResultOnly = false;
    private static int outputGenerations = 1;

    static {
        ResourceBundle resources = ResourceBundle.getBundle(PROP_FILE);
        Properties properties = getProperties(resources);
        try {
            String mazeName = resources.getString("maze");
            maze = MazeAccessor.load(mazeName);
            int numOfGenerations =
                Integer.parseInt(resources.getString("numOfGenerations"));
            int outputGens = 
                Integer.parseInt(resources.getString("outputGenerations"));
            int maxToStore = 1;
            if (outputGens != 0 && numOfGenerations != 0) {
                maxToStore = numOfGenerations/outputGens;
            }
            data = new MazeEvolutionData(maze, properties, maxToStore);
            entry = maze.getEntryLocation();
            destination = maze.getDestination();
            destinationX = destination.getXCoord();
            destinationY = destination.getYCoord();
            maxMovesPossible =
                Integer.parseInt(resources.getString("maxChromLength"));
            bonus =
                Integer.parseInt(resources.getString("bonus"));
            String oro = resources.getString("outputResultOnly");
            if (oro != null && oro.equalsIgnoreCase("true")) {
                outputResultOnly = true;
            }
            outputGenerations =
                Integer.parseInt(resources.getString("outputGenerations"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected static Properties getProperties(ResourceBundle bundle) {
        Properties props = new Properties();
        Enumeration keys = bundle.getKeys();
        String key = "";
        while (keys.hasMoreElements()) {
            key = (String)keys.nextElement();
            props.setProperty(key, bundle.getString(key));
        }
        return props;
    }

    /**
     * Returns an int indicating the fitness of the
     * Chromosome passed in as a parameter.
     */
    public int getFitnessScore(Chromosome chromosome) {
        int score = 0;
        Traveler traveler = new Traveler(entry);
        short[] genes = chromosome.getGenes();
        Location currentLocation = null;
        boolean reachedDestination = false;
        int numOfGenesUsed = 0;

        for (numOfGenesUsed=0; numOfGenesUsed<genes.length; numOfGenesUsed++) {
            traveler.travel(GeneTranslator.getDirection(genes[numOfGenesUsed]));
            currentLocation = traveler.getCurrentLocation();
            if (currentLocation.equals(destination)) {
                reachedDestination = true;
                break;
            }
        }

        int numOfMovesTaken = traveler.getNumOfTimesMoved();
        int numTimesHitWall = traveler.getNumOfTimesHitWall();


        // Formula for calculating the score:
        // the fewer moves the better, so subtract the
        // number of actual moves from number of possible moves,
        // then subtract number of times a wall was hit,
        // add bonus points if the destination was reached, then
        // subtract points for any extra genes at the end of
        // the chromosome which weren't used.
        // If the destination wasn't reached, then the score
        // is determined by how close to the destination it got.
        if (!reachedDestination) {
            score = 0;
            int xDistance = Math.abs(destinationX - currentLocation.getXCoord());
            int yDistance = Math.abs(destinationY - currentLocation.getYCoord());
            score = maxMovesPossible - (xDistance + yDistance);
        } else {
            score = 0;
            score = score + maxMovesPossible - numOfMovesTaken;
            score = score - numTimesHitWall;
            score = score + bonus;
            score = (numOfGenesUsed != (genes.length - 1)) ?
                (score - (genes.length-numOfGenesUsed)) : score;
        }

        return score;
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

        // stuff completed generations into the
        // MazeEvolutionData object
        if ((!outputResultOnly &&
                (generationNumber % this.outputGenerations == 0)) ||
                    isLastGeneration || generationNumber == 1) {
            data.addGeneration(gen);
        }

        // stuff final data into the
        // MazeEvolutionData object
        if (isLastGeneration) {
            data.setFinalTopScore(observable.getTopScore());
            data.setTopScorers(observable.getTopScorers());
            data.buildScoreData();
            try {
                MazeAccessor.store(data);
            } catch (IOException ioe) {
                System.out.println("error saving the results");
                ioe.printStackTrace();
            }
        }
    }

}