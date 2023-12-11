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

import java.net.URL;
import java.util.*;

/**
 * The ProducerFactory is used to create a subclass of GenerationProducer
 * which is appropriate for the type of genetic algorithm the user
 * wishes to employ.
 */
public class ProducerFactory {

    private static HashMap classNameMap = new HashMap();
    private static HashMap classMapCache = new HashMap();

    public ProducerFactory() {
        ProducerKey firstKey =
            new ProducerFactory.ProducerKey(true, false, Evolver.LINEAR);
        classNameMap.put(firstKey,
            "zujkomiller.genetic.linear.FixedSetProducer");
        ProducerKey secondKey =
            new ProducerFactory.ProducerKey(true, false, Evolver.LEAF_TREE);
        classNameMap.put(secondKey,
            "zujkomiller.genetic.leafTree.FixedSetLeafProducer");
        ProducerKey thirdKey =
            new ProducerFactory.ProducerKey(true, true, Evolver.LINEAR);
        classNameMap.put(thirdKey,
            "zujkomiller.genetic.linear.FixedBagProducer");
        ProducerKey fourthKey =
            new ProducerFactory.ProducerKey(false, true, Evolver.LINEAR);
        classNameMap.put(fourthKey,
            "zujkomiller.genetic.linear.VariableBagProducer");
    }

    /**
     * Returns the Class of a subclass of GenerationProducer.  The caller
     * of this method should get the Constructor from the Class and use
     * reflection to get an instance of the Class, passing in the appropriate
     * values to the Constructor.
     *
     * @param minChromLength The minimum length of the Chromosome.
     * @param maxChromLength The maximum length of the Chromosome.
     * @param allowDuplicates A boolean indicating whether or not
     *  duplicate values are allowed in the Chromosome.
     * @param chromType The valid values for this are: Evolver.LINEAR,
     *  Evolvler.LEAF_TREE, and Evolver.NODE_TREE.
     */
    public Class getProducerClass(int minChromLength,
            int maxChromLength, boolean allowDuplicates, short chromType) {
        boolean fixedLength = minChromLength == maxChromLength;
        ProducerFactory.ProducerKey key =
            new ProducerFactory.ProducerKey
                (fixedLength, allowDuplicates, chromType);
        Class theClass = getFromCache(key);
        if (theClass == null) {
            theClass = createClass(key);
            classMapCache.put(key, theClass);
        }
        return theClass;
    }

    protected Class createClass(ProducerKey key) {
        Object obj = classNameMap.get(key);
        String className = "";
        Class theClass = null;
        if (obj != null) {
            className = (String)obj;
            try {
                theClass = Class.forName(className);
            } catch (ClassNotFoundException cnfe) {
                // eat it; return null
            }
        }
        return theClass;
    }

    protected Class getFromCache(ProducerKey key) {
        Object obj = classMapCache.get(key);
        Class theClass = null;
        if (obj != null) {
            theClass = (Class) obj;
        }
        return theClass;
    }

    private class ProducerKey {
        private boolean fixedLength;
        private boolean allowDuplicates;
        private short chromType;

        public ProducerKey(boolean fixedLength,
                boolean allowDuplicates,
                short chromType) {
            this.fixedLength = fixedLength;
            this.allowDuplicates = allowDuplicates;
            this.chromType = chromType;
        }

        public boolean equals(Object other) {
            boolean isEqual = false;
            if (other instanceof ProducerKey) {
                ProducerKey otherKey = (ProducerKey)other;
                if (otherKey.fixedLength == this.fixedLength &&
                        otherKey.allowDuplicates == this.allowDuplicates &&
                        otherKey.chromType == this.chromType) {
                    isEqual = true;
                }
            }
            return isEqual;
        }


        public int hashCode() {
            int hash = 0;
            if (fixedLength) {
                hash = hash + (1);
            } else {
                hash = hash + (2);
            }
            if (allowDuplicates) {
                hash = hash + (10 * 3);
            } else {
                hash = hash + (10 * 4);
            }
            if (chromType == Evolver.LINEAR) {
                hash = hash + (100 * 5);
            } else if (chromType == Evolver.LEAF_TREE){
                hash = hash + (100 * 6);
            } else {
                hash = hash + (100 * 7);
            }
            return hash;
        }
    }

}