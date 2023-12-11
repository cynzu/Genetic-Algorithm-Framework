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

public class Direction {

    private static final short N = 0;
    private static final short S = 1;
    private static final short E = 2;
    private static final short W = 3;

    public static final Direction NORTH = new Direction(N);
    public static final Direction SOUTH = new Direction(S);
    public static final Direction EAST = new Direction(E);
    public static final Direction WEST = new Direction(W);

    private short direction;

    private Direction(short dir) {
        direction = dir;
    }

    public static Direction getOppositeDirection(Direction dir) {
        Direction opposite = null;
        switch (dir.direction) {
            case N :
                opposite = SOUTH;
                break;
            case S :
                opposite = NORTH;
                break;
            case E :
                opposite = WEST;
                break;
            case W :
                opposite = EAST;
                break;
        }
        return opposite;
    }

    public boolean equals(Object other) {
        boolean isEqual = false;
        if (other instanceof Direction) {
            Direction otherDirection = (Direction)other;
            if (otherDirection.direction == this.direction) {
                isEqual = true;
            }
        }
        return isEqual;
    }
    
    public static char toChar(Direction d) {
        char c = 'N';
        switch (d.direction) {
            case (N) : c = 'N'; break;
            case (S) : c = 'S'; break;
            case (E) : c = 'E'; break;
            case (W) : c = 'W'; break;            
        }
        return c;
    }

}