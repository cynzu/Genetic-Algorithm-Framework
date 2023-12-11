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
import java.util.Comparator;

/**
 * A Chromosome is a list of genes represented by an array of short values.
 */
public interface Chromosome extends Comparator, Serializable {

    /**
     * @param genes The short array containing
     *  the genes for this Chromosome.
     */
    public void setGenes(short[] genes);

    /**
     * @return The short array constituting the
     *  genes in the Chromosome.
     */
    public short[] getGenes();

    /**
     * Sets the gene at the given index.
     *
     * @param gene The value of the gene to set.
     * @param index The index of the gene in the Chromosome.
     */
    public void setGeneAtIndex(short gene, int index);

    /**
     * @param index The index of the requested gene.
     * @return The value of the gene at the given index.
     */
    public short getGeneAtIndex(int index);

    /**
     * Sets the fitness score of this Chromosome.
     * @param score The fitness score for this
     *  Chromosome.
     */
    public void setFitnessScore(int score);

    /**
     * @return The fitness score for this Chromosome.
     */
    public int getFitnessScore();

    /**
     * @return The number of genes in the Chromosome.
     */
    public int getSize();

    /**
     * @return A Clone of this Chromosome which has the same
     *  genes as this Chromosome, but which does not have any
     *  fitness score yet.
     */
    public Chromosome getUnscoredClone();

}