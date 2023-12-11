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

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;

/**
 * A Generation is an array of Chromosomes and includes an int indicating 
 * this generation's number as well as the top score achieved by any
 * individual Chromosome in this generation.
 */
public class Generation implements Serializable, Comparator {

    protected Chromosome[] individuals;
    protected int generationNumber;
    protected int topScore;

    public Generation(Chromosome[] individuals, int generationNumber) {
        this.individuals = individuals;
        this.generationNumber = generationNumber;
        calculateTopScore();
    }

    protected void calculateTopScore() {
        Arrays.sort(individuals, 0, individuals.length, individuals[0]);
        this.topScore = individuals[0].getFitnessScore();
    }

    /**
     * @return The array of Chromosomes comprising the individuals within
     *  this Generation.
     */
    public Chromosome[] getIndividuals() {
        return this.individuals;
    }

    /**
     * @return An int indicating this Generation's number which shows when this
     *  Generation was produced relative to other Generations.
     */
    public int getGenerationNumber() {
        return this.generationNumber;
    }

    /**
     * @return An int indicating the top score received by any individual
     *  Chromosome in this Generation.
     */
    public int getTopScoreOfGeneration() {
        return this.topScore;
    }
    
    /**
     * Implementation of the Comparator interface.
     * The comparison is based on the generationNumber of each
     * Generation passed in as a parameter, so that those Generations created
     * earlier on will precede those created later.
     */   
    public int compare (Object o1, Object o2) {
        int comparison = 0;
        if (o1 instanceof Generation && 
                o2 instanceof Generation) {
           Generation g1 = (Generation)o1;
           Generation g2 = (Generation)o2;
           if (g1.generationNumber < g2.generationNumber) {
                comparison = -1;
           } else if (g1.generationNumber > g2.generationNumber) {
                comparison = 1;
           } else if (g1.generationNumber == g2.generationNumber) {
                comparison = 0;
           } 
        }
        return comparison;
    }
    
    /**
     * Generations are considered logically equal if they contain the same set
     * of individual Chromosomes and they have the same generationNumber.
     */
    public boolean equals (Object obj) {
        boolean isEqual = false;
        if (obj instanceof Generation) {
            Generation other = (Generation)obj;
            if (other.generationNumber == this.generationNumber &&
                    other.individuals.equals(this.individuals)) {
                isEqual = true;
            }
        }
        return isEqual;
    }

}