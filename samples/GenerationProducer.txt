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

import java.util.*;

/**
 * Subclasses of GenerationProducer are responsible for 
 * creating an initial population of Chromosomes, and then
 * for creating a child Generation from a parent Generation
 * by replicating the best individuals from the parent,
 * dropping the worst individuals, performing crossovers, and
 * performing mutations.
 */
public abstract class GenerationProducer {

    /** The list of possible values for each gene. */
    protected final short[] alphabet;

    /**
     * If duplicates are not allowed, remainingValues
     * will list all of the values which have not yet
     * been assigned to genes.
     */
    protected ArrayList remainingValues;

    /** number of top scorers to replicate */
    protected final int numToReplicate;

    /**
     * how many times to replicate each Chromosome
     * represented in numToReplicate
     */
    protected final int numOfReplicationsEach;

    /** number of crossovers per generation */
    protected final int crossOversPerGen;

    /** number of mutations per generation */
    protected final int mutationsPerGen;

    /** minimum length of the Chromosome */
    protected final int minLength;

    /** maximum length of the Chromosome*/
    protected final int maxLength;

    public GenerationProducer(short[] alphabet, int numToReplicate,
            int numOfReplicationsEach, int crossOvers, int mutations,
            int minLength, int maxLength) {

        if (((numToReplicate * numOfReplicationsEach) + 1) > maxLength/2) {
            throw new IllegalArgumentException("The number of top scorers to " +
                "replicate multiplied by the number of replications must be " +
                "less than half of the size of the maximum length of the chromosome.");
        }

        this.alphabet = alphabet;
        this.numToReplicate = numToReplicate;
        this.numOfReplicationsEach = numOfReplicationsEach;
        this.crossOversPerGen = crossOvers;
        this.mutationsPerGen = mutations;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    /**
     * Creates an initial Generation of X number of
     * Chromosomes where X = the size passed in as
     * a parameter.
     */
    public abstract Chromosome[] createInitialGeneration(int size);

    /**
     * Get the next generation of Chromosomes given a parent
     * generation which has already been scored for fitness.
     */
     public Chromosome[] getNextGeneration(Chromosome[] parent) {
        Chromosome[] nextGen = new Chromosome[parent.length];
        if (nextGen.length >= 1) {
            // First, replace bottom-scorers with top scorers
            // in the next generation.
            nextGen = doReplications(parent);

            // Then do crossovers.
            doCrossovers(nextGen);

            // Then do mutations.
            doMutations(nextGen);

        } // end if the population has at least one member
        return nextGen;
    }

    /**
     * Use numToReplicate and numOfReplicationsEach to copy
     * the top scorers in the parent generation into the
     * child generation.
     *
     * @param The parent generation from which to create the child generation.
     * @return The child generation created from the parent, with the top
     *  scorers in the parent replicated in the child and the bottom scorers
     *  in the parent removed from the child.
     */
    protected abstract Chromosome[] doReplications(Chromosome[] parent);

    /**
     * Modify the nextGen passed in as a parameter by performing
     * crossovers on some of the Chromosomes.  Use crossOversPerGen
     * to determine how many crossovers to perform.
     */
    protected abstract void doCrossovers(Chromosome[] nextGen);

    /**
     * Modify the nextGen passed in as a parameter by performing
     * mutations on some of the genes.  Use mutationsPerGen
     * to determine how many mutations to perform.
     */
    protected abstract void doMutations(Chromosome[] nextGen);

}