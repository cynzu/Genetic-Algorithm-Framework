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

/**
 * Classes implementing EvolutionObserver may register themselves with 
 * classes which implement this interface, EvolutionObservable, so that they
 * will be notified when new generations are created and tested for fitness.
 */
public interface EvolutionObservable {

    /**
     * Adds an EvolutionObserver to this EvolutionObservable.
     * When a new generation has been created and tested for
     * fitness, the EvolutionObserver passed in as a parameter
     * will be notified.
     *
     * @param observer An EvolutionObserver which wishes to
     *  be notified when a generation has been created and
     *  tested for fitness.
     */
    public void addEvolutionObserver(EvolutionObserver observer);

    /**
     * Removes the given EvolutionObserver from the set of
     * observers.
     *
     * @param observer The EvolutionObserver to remove
     *  from the set of observers.
     */
    public void removeEvolutionObserver(EvolutionObserver observer);

    /**
     * @return The top score attained by any Chromosome so far.
     */
    public int getTopScore();

    /**
     * @return An array of Chromosome which have each attained
     *  a fitness score equal to the top score attained so far.
     *  These Chromosomes may have been created in different
     *  generations.
     */
    public Chromosome[] getTopScorers();

    /**
     * @return The number of generations created so far.
     */
    public int getNumberOfGenerationsRun();

}