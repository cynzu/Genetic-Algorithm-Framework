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
 * A FixedBagChromosome has a fixed number of values,
 * and its values may contain duplicates -
 * it is a bag rather than a set.
 */
public class FixedBagChromosome extends LinearChromosome {

    public FixedBagChromosome(short[] argGenes) {
        super(argGenes);
    }

    // currently, no need for it to do anything
    // differently from LinearChromosome

}