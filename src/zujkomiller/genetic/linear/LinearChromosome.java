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

package zujkomiller.genetic.linear;

import zujkomiller.genetic.*;
import java.util.Arrays;

/**
 * A LinearChromosome has its genes arranged in
 * a linear sequence.
 */
public class LinearChromosome implements Chromosome {

    protected short[] genes = null;
    protected int score = -1;

    public LinearChromosome(short[] argGenes) {
        this.genes = argGenes;
    }

    public void setGenes(short[] genes) {
        this.genes = genes;
    }

    public short[] getGenes() {
        return this.genes;
    }

    public void setGeneAtIndex(short gene, int index) {
        genes[index] = gene;
    }

    public short getGeneAtIndex(int index) {
        return genes[index];
    }

    public void setFitnessScore(int score) {
        this.score = score;
    }

    public int getFitnessScore() {
        return this.score;
    }

    /**
     * @return The number of genes in the Chromosome.
     */
    public int getSize() {
        return genes.length;
    }

    /**
     * @return The genes in this Chromosome separated by cammas
     */
    public String toString() {
        String string = "";
        for (int n=0; n<genes.length; n++) {
            string = string + genes[n];
            if (n < (genes.length - 1)) {
                string = string + ", ";
            }
        }
        return string;
    }

    /**
     * Implementation of Comparator interface.
     * Allows sorting of Chromosomes based on their fitness scores:
     * those with a higher fitness score are listed first.
     */
    public int compare(Object chrom1, Object chrom2) {
        Chromosome c1 = (Chromosome)chrom1;
        Chromosome c2 = (Chromosome)chrom2;
        int score1 = c1.getFitnessScore();
        int score2 = c2.getFitnessScore();
        int comparison = 0;

        // For sorting purposes, they're equal if their
        // scores are the same.
        if (score1 == score2) {
            comparison = 0;
        }
        // high scorers get listed first
        // low scorers get put at the end of the list
        else if (score1 < score2) {
            comparison = 1;
        } else if (score1 > score2) {
            comparison = -1;
        }
        return comparison;
    }

    /**
     * LinearChromosomes are logically equal if they have the same
     * fitness score and consist of the same genes.
     */
    public boolean equals(Object other) {
        LinearChromosome chrom = (LinearChromosome)other;
        boolean isEqual = false;
        if (chrom.score == score && Arrays.equals(chrom.genes, this.genes)) {
           isEqual = true;
        }
        return isEqual;
    }

    /**
     * @return a LinearChromosome which contains a cloned copy of this
     *  LinearChromosome's genes, and does not yet have a fitness score.
     */
    public Chromosome getUnscoredClone() {
        short[] clonedGenes = new short[this.genes.length];
        clonedGenes = (short[])this.genes.clone();
        LinearChromosome clone = new LinearChromosome(clonedGenes);
        return clone;
    }

}