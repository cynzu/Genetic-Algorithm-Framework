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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * This class is responsible for creating an initial population
 * of FixedBagChromosomes (Chromosomes which have a fixed length
 * and which can have gene values repeated within them) and
 * for creating a child Generation of FixedBagChromosomes from
 * a parent Generation by replicating the best individuals from 
 * the parent, dropping the worst individuals, applying crossovers,
 * and applying mutations.
 */
public class FixedBagProducer extends GenerationProducer {

    protected Random random = new Random();

    public FixedBagProducer(short[] alphabet, int numToReplicate,
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
        short[] genes = new short[this.maxLength];
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
        short[] newGenes1 = new short[originalGenes1.length];
        short[] newGenes2 = new short[originalGenes2.length];

        // start index is the place to start the crossover
        int startIndex = random.nextInt(originalGenes1.length-1);

        // index keeps track of the crossing over process
        int index = 0;

        if (startIndex == 0) {
            startIndex = 1; // starting at 0 does nothing
        }

        // first fill each with their original counterpart's
        // whole set of genes
        newGenes1 = (short[])originalGenes1.clone();
        newGenes2 = (short[])originalGenes2.clone();

        // then, from the startIndex, fill each with the
        // other's subset of genes
        for (int n=0; n<startIndex; n++) {
            newGenes1[n] = originalGenes2[n];
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
            individualToMutate = random.nextInt(max+1);
            mutate(population[individualToMutate]);
        }
    }

    /**
     * alter the value of a randomly chosen gene
     */
    private void mutate(Chromosome chrom) {
        int chromSize = chrom.getSize();
        if (chromSize >= 1) {
            int randomIndex = random.nextInt(chromSize);
            short randomValue = getRandomValue();
            chrom.setGeneAtIndex(randomValue, randomIndex);
        }
    }


}