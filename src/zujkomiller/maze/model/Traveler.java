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

package zujkomiller.maze.model;

import java.util.*;

public class Traveler {
    private Location currentLocation;
    private int numTimesHitWall = 0;
    private int numTimesMoved = 0;

    public Traveler(Location entryLocation) {
        this.currentLocation = entryLocation;
    }

    public void travel (Direction direction) {
        MazeElement nextLocation =
            currentLocation.getAdjacentElement(direction);
        if (nextLocation instanceof Wall) {
            numTimesHitWall++;
        } else if (nextLocation instanceof Location) {
            currentLocation = (Location)nextLocation;
            numTimesMoved++;
        }
    }

    public int getNumOfTimesHitWall() {
        return this.numTimesHitWall;
    }

    public int getNumOfTimesMoved() {
        return this.numTimesMoved;
    }

    public Location getCurrentLocation() {
        return this.currentLocation;
    }

}