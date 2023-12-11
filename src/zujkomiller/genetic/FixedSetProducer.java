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
 * of FixedSetChromosomes (Chromosomes which have a fixed length
 * and which can not have gene values repeated within them) and
 * for creating a child Generation of FixedSetChromosomes from
 * a parent Generation by replicating the best individuals from 
 * the parent, dropping the worst individuals, applying crossovers,
 * and applying mutations.
 */
public class FixedSetProducer  extends GenerationProducer {

    protected Random random = new Random();

    public FixedSetProducer(short[] alphabet, int numToReplicate,
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
        // The set of remaining values initially includes
        // all of the values in the alphabet.
        int alphabetSize = alphabet.length;
        this.remainingValues = new ArrayList(alphabetSize);
        for (int n=0; n<alphabetSize; n++) {
            remainingValues.add(new Short(alphabet[n]));
        }
        short[] genes = new short[this.maxLength];
        for (int n=0; n<genes.length; n++) {
            genes[n] = getRandomValue();
        }
        return new FixedSetChromosome(genes);
    }

    /**
     * Returns one of the remaining values and also reduces
     * the size of the remainingValues by one, removing the
     * value that is returned from this method.
     */
    private short getRandomValue() {
        int size = remainingValues.size();
        int selectedOne = 0;
        if (size - 1 > 0) {
            selectedOne = random.nextInt(size);
        }
        Short theValue = (Short)remainingValues.get(selectedOne);
        short value = theValue.shortValue();
        remainingValues.remove(selectedOne);
        return value;
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
        // numOfReplicationsEac because the top scorers are already
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
        nextGen[nextGen.length - 1] = highestScorer;

        return nextGen;
    }

    protected void doCrossovers(Chromosome[] population) {
        // never allow the last chromosome to be crossed-over
        int max = population.length - 2;
        int individualToCross = 0;
        for (int n=0; n<this.crossOversPerGen; n++) {
            individualToCross = random.nextInt(max+1);
            doSelfCrossover(population[individualToCross]);
        }
    }

    /**
     * Because FixedSetChromosomes can not have repeated
     * gene values, they can not be crossed with other
     * Chromosomes, but they can do a "self" crossover, in
     * which the end of the chromosome is exchanged with the
     * the start of the chromosome at a random point.
     */
    protected void doSelfCrossover(Chromosome chrom) {
        short[] originalGenes = chrom.getGenes();
        short[] newGenes = new short[originalGenes.length];
        
        // start index is the place to start the crossover
        int startIndex = random.nextInt(originalGenes.length-1);
        
        // index keeps track of the crossing over process
        int index = 0;

        if (startIndex == 0) {
            startIndex = 1; // starting at 0 does nothing
        }

        for(int n=startIndex; n<newGenes.length; n++){
            newGenes[index] = originalGenes[n];
            index++;
        }
        for(int n=0; n<startIndex; n++){
            newGenes[index] = originalGenes[n];
            index++;
        }
        chrom.setGenes(newGenes);

    }

    protected void doMutations(Chromosome[] population) {
        // never allow the last chromosome to be mutated
        int max = population.length - 2;
        int individualToMutate = 0;
        for (int n=0; n<this.mutationsPerGen; n++) {
            individualToMutate = random.nextInt(max+1);
            mutate(population[individualToMutate]);
        }
    }

    /**
     * Swap a pair of genes.
     */
    private void mutate(Chromosome chrom) {
        short[] genes = chrom.getGenes();
        if (genes.length >= 3) {
            int random1 = random.nextInt(genes.length);
            int random2 = random.nextInt(genes.length);
            short temp = genes[random1];
            genes[random1] = genes[random2];
            genes[random2] = temp;
        }
        chrom.setGenes(genes);
    }


}