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
import zujkomiller.genetic.*;

public class Individual implements Serializable {
    private Chromosome chromosome;
    private int generationNumber;
    private String name = "";
    
    public Individual(int generationNumber, Chromosome chrom) {
        this.generationNumber = generationNumber;
        this.chromosome = chrom;
    }
    
    public int getGenerationNumber() {
        return this.generationNumber;
    }
    
    public Chromosome getChromosome() {
        return this.chromosome;
    }
    
    public void setName(String name){
        this.name = name;        
    }
    
    public String getName() {
        return this.name;
    }
    
    public String toString() {
        return this.name;
    }
    
}