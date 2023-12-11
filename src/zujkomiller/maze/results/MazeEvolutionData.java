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

package zujkomiller.maze.results;

import zujkomiller.maze.model.*;
import zujkomiller.genetic.*;
import java.io.Serializable;
import java.util.*;

public class MazeEvolutionData implements Serializable {

    protected Maze maze = null;
    protected Properties properties = null;
    protected int finalTopScore = -1;
    protected Chromosome[] topScorers = null;
    protected Generation[] generations = null;
    protected transient ArrayList tempGenerations = null;
    protected GenerationScoreData[] scoreData = null;

    public MazeEvolutionData(Maze maze, Properties properties,
            int maxNumOfGenerationsToStore) {
        this.maze = maze;
        this.properties = properties;
        tempGenerations = new ArrayList(maxNumOfGenerationsToStore);
    }
    
    protected void createGenerations() {
        this.tempGenerations.trimToSize();
        this.generations = new Generation[tempGenerations.size()];
        this.tempGenerations.toArray(generations);
    }
    
    public void buildScoreData() {
        createGenerations();
        if (generations.length > 0) {
            scoreData = new GenerationScoreData[generations.length];
            Arrays.sort(generations, generations[0]);
            Cohort[] cohorts = null;
            for (int n=0; n<generations.length; n++) {
                cohorts = findCohorts(generations[n]);
                if (cohorts.length > 0) {
                    Arrays.sort(cohorts, cohorts[0]);
                }
                scoreData[n] = new GenerationScoreData
                    (generations[n].getGenerationNumber(), cohorts);
            } 
        }        
    }
    
    protected Cohort[] findCohorts(Generation gen) {
        Chromosome[] chroms = gen.getIndividuals();
        HashMap chromsByScore = new HashMap();

        for (int n=0; n<chroms.length; n++) {
            int score = chroms[n].getFitnessScore();
            Integer key = new Integer(score);
            if (chromsByScore.containsKey(key)) {
                // this score has already been found, so
                // get the ArrayList which contains chroms
                // with this score and add the current chrom to it
                ArrayList list = (ArrayList)chromsByScore.get(key);
                list.add(chroms[n]);
            } else {
                // this is the first time that score has been found, so
                // create a new ArrayList to hold all chroms with this
                // score, add the current chrom to it, and stuff it
                // into the HashMap
                ArrayList list = new ArrayList();
                list.add(chroms[n]);
                chromsByScore.put(key, list);
            }  
        }
        
        // now create a Cohort for each score 
        int generationNumber = gen.getGenerationNumber();
        Cohort[] cohorts = new Cohort[chromsByScore.size()];
        Set keys = chromsByScore.keySet();
        Iterator iterator = keys.iterator();
        int index = 0;
        ArrayList chromos = null;
        Individual[] individuals = new Individual[0];
        while(iterator.hasNext()) {
            Integer key = (Integer)iterator.next();
            int score = key.intValue();
            chromos = (ArrayList)chromsByScore.get(key);
            int size = chromos.size();
            individuals = new Individual[size];
            for (int n=0; n<size; n++) {
                individuals[n] = new Individual(generationNumber,
                    (Chromosome)chromos.get(n));    
                individuals[n].setName("solution " + (n+1));
            }
            
            cohorts[index] = new Cohort(score, generationNumber, individuals);
            index++;
        }
        return cohorts;
    }
    
    public GenerationScoreData[] getScoreData() {
        return this.scoreData;
    }

    public Maze getMaze() {
        return this.maze;
    }

    public Properties getProperties() {
        return this.properties;
    }

    public int getFinalTopScore() {
        return this.finalTopScore;
    }

    public Chromosome[] getTopScorers() {
        return this.topScorers;
    }

    public Generation[] getGenerations() {
        return this.generations;
    }

    public void setFinalTopScore(int score) {
        this.finalTopScore = score;
    }

    public void setTopScorers(Chromosome[] chroms) {
        this.topScorers = chroms;
    }

    public void addGeneration(Generation gen) {
        this.tempGenerations.add(gen);
    }
    
}