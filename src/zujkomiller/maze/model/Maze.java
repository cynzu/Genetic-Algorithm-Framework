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

import java.io.Serializable;

public class Maze implements Serializable {

    private GridLocation[][] locations;
    private int width = 0;
    private int height = 0;
    private GridLocation entryLocation = null;
    private GridLocation destination = null;
    private String name = "";

    public Maze(int width, int height) {
        this.width = width;
        this.height = height;
        this.locations = new GridLocation[width][height];
        this.initializeLocations();
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }

    private void initializeLocations() {
        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                locations[x][y] = new GridLocation(x, y);
            }
        }
        setupNeighbors();
    }

    private void setupNeighbors() {
        GridLocation location = null;
        MazeElement north = null;
        MazeElement south = null;
        MazeElement east = null;
        MazeElement west = null;

        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                location = locations[x][y];

                // find north
                if (y-1 >= 0) {
                    north = locations[x][y-1];
                } else {
                    north = Wall.getInstance();
                }

                // find south
                if (y+1 < height) {
                    south = locations[x][y+1];
                } else {
                    south = Wall.getInstance();
                }

                // find east
                if (x+1 < width) {
                    east = locations[x+1][y];
                } else {
                    east = Wall.getInstance();
                }

                // find west
                if (x-1 >= 0) {
                    west = locations[x-1][y];
                } else {
                    west = Wall.getInstance();
                }

                location.setNeighbor(north, Direction.NORTH);
                location.setNeighbor(south, Direction.SOUTH);
                location.setNeighbor(east, Direction.EAST);
                location.setNeighbor(west, Direction.WEST);
            }
        }

    }

    public void setEntryLocation(int x, int y) {
        this.entryLocation = locations[x][y];
    }

    public void setDestination(int x, int y) {
        this.destination = locations[x][y];
    }

    public Location getEntryLocation() {
        return this.entryLocation;
    }

    public Location getDestination() {
        return this.destination;
    }

    public Location[][] getLocations() {
        return this.locations;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public void addWall(int xCoord, int yCoord, Direction direction) {
        GridLocation location = locations[xCoord][yCoord];
        GridLocation formerNeighbor = null;
        if (location != null) {
            // also add the wall to the adjacent neighbor
            MazeElement element = location.getAdjacentElement(direction);
            if (element instanceof GridLocation) {
                formerNeighbor = (GridLocation)element;
                location.setNeighbor(Wall.getInstance(), direction);
                formerNeighbor.setNeighbor(Wall.getInstance(),
                    Direction.getOppositeDirection(direction));
            }
        }
    }


    private class GridLocation implements Location {
        int xCoord;
        int yCoord;
        MazeElement northNeighbor;
        MazeElement southNeighbor;
        MazeElement eastNeighbor;
        MazeElement westNeighbor;

        public GridLocation(int x, int y) {
            xCoord = x;
            yCoord = y;
        }

        public int getXCoord() {
            return xCoord;
        }

        public int getYCoord() {
            return yCoord;
        }

        public MazeElement getAdjacentElement(Direction direction) {
            MazeElement neighbor = null;
            if (direction.equals(Direction.NORTH)) {
                neighbor = northNeighbor;
            } else if (direction.equals(Direction.SOUTH)) {
                neighbor = southNeighbor;
            } else if (direction.equals(Direction.EAST)) {
                neighbor = eastNeighbor;
            } else if (direction.equals(Direction.WEST)) {
                neighbor = westNeighbor;
            }
            return neighbor;
        }

        private void setNeighbor(MazeElement neighbor, Direction direction) {
            if (direction.equals(Direction.NORTH)) {
                northNeighbor = neighbor;
            } else if (direction.equals(Direction.SOUTH)) {
                southNeighbor = neighbor;
            } else if (direction.equals(Direction.EAST)) {
                eastNeighbor = neighbor;
            } else if (direction.equals(Direction.WEST)) {
                westNeighbor = neighbor;
            }
        }

        public boolean equals(Object other) {
            boolean isEqual = false;
            if (other instanceof Location) {
                Location otherLoc = (Location)other;
                if (otherLoc.getXCoord() == xCoord &&
                        otherLoc.getYCoord() == yCoord) {
                    isEqual = true;
                }
            }
            return isEqual;
        }

    } // end of GridLocation inner class

    public void print() {
        GridLocation[][] locs = (GridLocation[][])this.getLocations();

        GridLocation location = null;
        MazeElement north = null;
        MazeElement south = null;
        MazeElement east = null;
        MazeElement west = null;
        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                location = locs[x][y];
                try {
                    north = location.getAdjacentElement(Direction.NORTH);
                    south = location.getAdjacentElement(Direction.SOUTH);
                    east = location.getAdjacentElement(Direction.EAST);
                    west = location.getAdjacentElement(Direction.WEST);

                    System.out.println("Location: ("
                        + location.getXCoord() + ", " + location.getYCoord() + ")");

                    if (north instanceof Location) {
                        System.out.println("\tnorthNeighbor: ("
                            + ((Location)north).getXCoord() + ", " + ((Location)north).getYCoord() + ")");
                    } else {
                        System.out.println("\tnorthNeighbor: (WALL)");
                    }

                    if (south instanceof Location) {
                        System.out.println("\tsouthNeighbor: ("
                            + ((Location)south).getXCoord() + ", " + ((Location)south).getYCoord() + ")");
                    } else {
                        System.out.println("\tsouthNeighbor: (WALL)");
                    }

                    if (east instanceof Location) {
                        System.out.println("\teastNeighbor: ("
                            + ((Location)east).getXCoord() + ", " + ((Location)east).getYCoord() + ")");
                    } else {
                        System.out.println("\teastNeighbor: (WALL)");
                    }

                    if (west instanceof Location) {
                        System.out.println("\twestNeighbor: ("
                            + ((Location)west).getXCoord() + ", " + ((Location)west).getYCoord() + ")");
                    } else {
                        System.out.println("\twestNeighbor: (WALL)");
                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

}