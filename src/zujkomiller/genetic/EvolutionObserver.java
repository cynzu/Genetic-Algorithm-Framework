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
 * Classes implementing this interface, EvolutionObserver, may register 
 * themselves with classes which implement EvolutionObservable so that they
 * will be notified when new generations are created and tested for fitness.
 */
public interface EvolutionObserver {

    /**
     * This method is called by an EvolutionObservable
     * when a new generations has been created and tested
     * for fitness.
     *
     * @param generation The generation that was just created.
     * @param generationNumber The number of the generation (ex:
     *  if this is the second generation created, the generationNumber
     *  will be 2).
     * @param topScoreSoFar The top score achieved so far by any
     *  Chromosome in any generation.
     * @param isLastGeneration This will be true only when no other
     *  generations will be created after the one passed in as a param.
     * @param observable The EvoluitonObservable object which called
     *  this method.  It can be queried for more information.
     */
    public void generationCreated(Generation generation,
        int topScoreSoFar, boolean isLastGeneration,
        EvolutionObservable observable);

}