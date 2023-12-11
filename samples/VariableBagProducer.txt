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
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class is responsible for creating an initial population
 * of VariableBagChromosomes (Chromosomes which can be of variable
 * length and which can have gene values repeated within them) and
 * for creating a child Generation of VariableBagChromosomes from
 * a parent Generation by replicating the best individuals from 
 * the parent, dropping the worst individuals, applying crossovers,
 * and applying mutations.
 */
public class VariableBagProducer  extends GenerationProducer {

    // these are used when deciding how to mutate a chromosome
    private static final int LEAVE_LENGTH_SAME = 0;
    private static final int DECREASE_LENGTH = 1;
    private static final int INCREASE_LENGTH = 2;

    private Random random = new Random();

    public VariableBagProducer(short[] alphabet, int numToReplicate,
            int numOfReplicationsEach, int crossOvers, int mutations,
            int minLength, int maxLength) {
        super(alphabet, numToReplicate, numOfReplicationsEach, crossOvers,
            mutations, minLength, maxLength);
    }

    /**
     * Creates an initial Generation of X number of
     * Chromosomes where X = the size passed in as
     * a parameter.
     */
    public Chromosome[] createInitialGeneration(int size) {
        Chromosome[] chroms = new Chromosome[size];
        for (int n=0; n<size; n++) {
            chroms[n] = createChromosome();
        }
        return chroms;
    }

    private Chromosome createChromosome() {
        int chromLength = random.nextInt(this.maxLength + 1);

        // Chromosomes must be at least 2 genes long
        while (chromLength < 2) {
            chromLength = random.nextInt(this.maxLength + 1);
        }

        short[] genes = new short[chromLength];
        for (int n=0; n<genes.length; n++) {
            genes[n] = getRandomValue();
        }
        return new FixedBagChromosome(genes);
    }

    /**
     * Returns one of the possible values.
     */
    private short getRandomValue() {
        int size = this.alphabet.length;
        int selectedOne = 0;
        if (size - 1 > 0) {
            selectedOne = random.nextInt(size);
        }
        return this.alphabet[selectedOne];
    }

    protected Chromosome[] doReplications(Chromosome[] parent) {
        Chromosome[] nextGen = new Chromosome[parent.length];

        // First sort the parent Chromosome array so that
        // we can locate the top and bottom scorers.
        Arrays.sort(parent, 0, parent.length, parent[0]);

        // Store all of the top scorers in the parent generation
        // so that they will be replicated in the next generation.
        Chromosome[] topScorers = new Chromosome[this.numToReplicate];
        for (int n=0; n<this.numToReplicate; n++) {
            topScorers[n] = parent[n].getUnscoredClone();
        }

        // Save the very top scorer to make sure it goes into the next
        // generation without any alteration.
        Chromosome highestScorer = topScorers[0].getUnscoredClone();

        // The number of low-scorers to drop from the end of the list
        // equals the number of top scorers to replicate times
        // numOfReplicationsEach - 1.  We subtract one from
        // numOfReplicationsEach because the top scorers are already
        // in the list once, at the very beginning. To this we
        // add one to drop so that the highest scorer can take
        // that place without it being modified in any way (no mutations
        // and no crossovers).
        int numToDropFromBottom =
            (this.numToReplicate * (this.numOfReplicationsEach - 1)) + 1;

        // Now fill the nextGen array with new Chromosomes based
        // on the parent Chromosomes, replacing the bottom scorers
        // with replicants of the top scorers
        for (int n=0; n<nextGen.length - numToDropFromBottom; n++) {
            nextGen[n] = parent[n].getUnscoredClone();
        }
        int counter = 0;
        for (int n=nextGen.length - numToDropFromBottom;
                n<(nextGen.length-1); n++) {
            if (counter > topScorers.length - 1) {
                counter = 0;
            }
            nextGen[n] = topScorers[counter];
            counter++;
        }

        // keep the highest scorer from the parent at the last index
        nextGen[nextGen.length - 1] = highestScorer;

        return nextGen;
    }

    protected void doCrossovers(Chromosome[] population) {
        // never allow the last chromosome to be crossed-over
        // because it holds the top-scorer from the parent generation
        int max = this.crossOversPerGen * 2;
        int indexOfTopScorer = population.length - 1;
        for (int n=0; n<max; n=n+2) {
            if (n == indexOfTopScorer || (n + 1) == indexOfTopScorer) {
                break;
            }
            doCrossover(population[n], population[n+1]);
        }
    }

    protected void doCrossover(Chromosome chrom1, Chromosome chrom2) {
        short[] originalGenes1 = chrom1.getGenes();
        short[] originalGenes2 = chrom2.getGenes();

        // swap lengths
        short[] newGenes1 = new short[originalGenes2.length];
        short[] newGenes2 = new short[originalGenes1.length];

        // use the shortest chromosome to choose the
        // crossover point
        short[] shortestLengthGenes = originalGenes1;
        if (originalGenes1.length > originalGenes2.length) {
            shortestLengthGenes = originalGenes2;
        }

        // start index is the place to start the crossover
        int startIndex = 1;
        int maxIndex = shortestLengthGenes.length-1;
        if (maxIndex > 0) {
            startIndex = random.nextInt(maxIndex + 1);
        }
        // starting at 0 is meaningless, so keep trying
        while (startIndex == 0) {
            startIndex = random.nextInt(maxIndex + 1);
        }

        // first fill the newGenes arrays up to the startIndex
        // with the genes from the original sets of genes
        for (int n=0; n<startIndex; n++) {
            newGenes1[n] = originalGenes1[n];
            newGenes2[n] = originalGenes2[n];
        }

        // now do the crossovers by filling the rest of the
        // newGenes arrays with values from the other sets
        // of original genes
        for (int n=startIndex; n<newGenes1.length; n++) {
            newGenes1[n] = originalGenes2[n];
        }
        for (int n=startIndex; n<newGenes2.length; n++) {
            newGenes2[n] = originalGenes1[n];
        }

        chrom1.setGenes(newGenes1);
        chrom2.setGenes(newGenes2);
    }

    protected void doMutations(Chromosome[] population) {
        // never allow the last chromosome to be mutated
        // because it holds the top-scorer from the parent generation
        int max = population.length - 2;
        int individualToMutate = 0;
        for (int n=0; n<this.mutationsPerGen; n++) {
            individualToMutate = random.nextInt(max + 1);
            mutate(population[individualToMutate]);
        }
    }

    /**
     * Either increase the chromosome's size by inserting
     * a gene, decrease the chromosome's size by
     * removing a gene, or leave the size the same but
     * alter the value of a randomly chosen gene.
     */
    private void mutate(Chromosome chrom) {
        int chromSize = chrom.getSize();
        boolean canBeShortened = (chromSize > this.minLength);
        boolean canBeLengthened = (chromSize < this.maxLength);

        int whatToDo = this.LEAVE_LENGTH_SAME;
        if (canBeShortened && canBeLengthened) {
            whatToDo = random.nextInt(this.INCREASE_LENGTH + 1);
        } else if (!canBeShortened && canBeLengthened) {
            // choose between 0 and 1; either leave the
            // length the same or increase it
            whatToDo = random.nextInt(2);
            if (whatToDo == 1) { // can't shorten, so
                whatToDo = this.INCREASE_LENGTH; // lengthen the chrom
            }
        } else if (canBeShortened && !canBeLengthened) {
            // choose between 0 and 1; either leave the
            // length the same or decrease it
            whatToDo = random.nextInt(2);
        }

        switch (whatToDo) {
            case LEAVE_LENGTH_SAME :
                if (chromSize >= 1) {
                    int randomIndex = random.nextInt(chromSize);
                    short randomValue = getRandomValue();
                    chrom.setGeneAtIndex(randomValue, randomIndex);
                }
                break;

            case DECREASE_LENGTH :
                if (chromSize >= 1) {
                    int randomIndex = random.nextInt(chromSize);
                    short[] newGenes = new short[chromSize-1];
                    boolean removedGene = false;
                    for (int n=0; n<=newGenes.length; n++) {
                        if (n == randomIndex) {
                            // found the gene we're going to remove
                            // so don't put it into the newGenes array
                            removedGene = true;
                            continue;
                        } else {
                            int index = (removedGene) ? (n - 1) : n;
                            newGenes[index] = chrom.getGeneAtIndex(n);
                        }
                    }
                    chrom.setGenes(newGenes);
                }
                break;

            case INCREASE_LENGTH :
                if (chromSize >= 1) {
                    int randomIndex = random.nextInt(chromSize);
                    short[] newGenes = new short[chromSize+1];
                    boolean insertedNewGene = false;
                    for (int n=0; n<newGenes.length; n++) {
                        if (n == randomIndex) {
                            // found the place to insert a new gene
                            short value = getRandomValue();
                            newGenes[n] = value;
                            insertedNewGene = true;
                            continue;
                        } else {
                            int index = (insertedNewGene) ? (n - 1) : n;
                            newGenes[n] = chrom.getGeneAtIndex(index);
                        }
                    }
                    chrom.setGenes(newGenes);
                }
                break;
        }
    }

}