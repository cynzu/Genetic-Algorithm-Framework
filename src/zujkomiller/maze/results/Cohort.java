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

import zujkomiller.genetic.*;
import java.io.Serializable;
import java.util.Comparator;

/**
 * A Cohort is a group of Individuals which all
 * have the same fitness score and all belong to
 * the same Generation. 
 */
public class Cohort implements Serializable, Comparator {
    int score;
    int generationNumber;
    Individual[] individuals;
    
    public Cohort(int score, int genNum, Individual[] individuals) {
        // >>> could do some validation here . . .
        this.score = score;
        this.generationNumber = genNum;
        this.individuals = individuals;
    }
    
    public int getScore() {
        return this.score;
    }
    
    public int getGenerationNumber() {
        return this.generationNumber;
    }
    
    public Individual[] getIndividuals() {
        return this.individuals;
    }
        
    public int compare (Object o1, Object o2) {
        int comparison = 0;
        if (o1 instanceof Cohort && 
                o2 instanceof Cohort) {
           Cohort cohort1 = (Cohort)o1;
           Cohort cohort2 = (Cohort)o2;
           if (cohort1.score < cohort2.score) {
                comparison = -1;
           } else if (cohort1.score > cohort2.score) {
                comparison = 1;
           } else if (cohort1.score == cohort2.score) {
                comparison = 0;
           } 
        }
        return comparison;
    }
    
    public boolean equals (Object obj) {
        boolean isEqual = false;
        if (obj instanceof Cohort) {
            Cohort other = (Cohort)obj;
            if (other.generationNumber == this.generationNumber &&
                    other.score == this.score &&
                    other.individuals.equals(this.individuals)) {
                isEqual = true;
            }
        }
        
        return isEqual;
    }

    
}