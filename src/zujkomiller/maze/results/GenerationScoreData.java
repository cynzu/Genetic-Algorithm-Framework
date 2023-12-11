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

import java.io.Serializable;
import java.util.Comparator;

public class GenerationScoreData implements Serializable, Comparator {
    
    private int generationNumber;
    private Cohort[] cohorts;
    
    public GenerationScoreData(int genNum, Cohort[] cohorts) {
        this.generationNumber = genNum;
        this.cohorts = cohorts;
    }
    
    public int getGenerationNumber() {
        return this.generationNumber;
    }
    
    public Cohort[] getCohorts() {
        return this.cohorts;
    }
    
    public int compare (Object o1, Object o2) {
        int comparison = 0;
        if (o1 instanceof GenerationScoreData && 
                o2 instanceof GenerationScoreData) {
           GenerationScoreData gsd1 = (GenerationScoreData)o1;
           GenerationScoreData gsd2 = (GenerationScoreData)o2;
           if (gsd1.generationNumber < gsd2.generationNumber) {
                comparison = -1;
           } else if (gsd1.generationNumber > gsd2.generationNumber) {
                comparison = 1;
           } else if (gsd1.generationNumber == gsd2.generationNumber) {
                comparison = 0;
           } 
        }
        return comparison;
    }
    
    public boolean equals (Object obj) {
        boolean isEqual = false;
        if (obj instanceof GenerationScoreData) {
            GenerationScoreData other = (GenerationScoreData)obj;
            if (other.generationNumber == this.generationNumber &&
                    other.cohorts.equals(this.cohorts)) {
                isEqual = true;
            }
        }
        
        return isEqual;
    }

    
}