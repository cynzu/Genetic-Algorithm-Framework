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

package zujkomiller.maze.persistence;

import zujkomiller.maze.model.*;
import zujkomiller.maze.persistence.*;
import zujkomiller.maze.ui.MazeDisplayer;


public class MazeWriter {

    public MazeWriter() {
        writeNoWallsMaze();
        writeEasyMaze();
        writeDifficultMaze();
    }
    
    private void writeNoWallsMaze() {
        zujkomiller.maze.model.Maze maze = 
            new zujkomiller.maze.model.Maze(5, 5);

        maze.setEntryLocation(4, 4);
        maze.setDestination(0, 0);
        
        try {
            MazeAccessor.store(maze, "noWalls");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void writeEasyMaze() {
        zujkomiller.maze.model.Maze maze = 
            new zujkomiller.maze.model.Maze(5, 5);

        maze.addWall(0, 3, Direction.SOUTH);
        maze.addWall(1, 3, Direction.SOUTH);
        maze.addWall(2, 2, Direction.EAST);
        maze.addWall(2, 2, Direction.EAST);
        maze.addWall(2, 3, Direction.EAST);
        maze.addWall(2, 4, Direction.EAST);
        
        maze.setEntryLocation(0, 0);
        maze.setDestination(4, 4);
        
       // MazeDisplayer displayer = new MazeDisplayer(maze);
        
        try {
            MazeAccessor.store(maze, "easy");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void writeDifficultMaze() {
        zujkomiller.maze.model.Maze maze = 
            new zujkomiller.maze.model.Maze(5, 5);

        maze.addWall(1, 1, Direction.NORTH);
        maze.addWall(1, 1, Direction.WEST);
        maze.addWall(3, 1, Direction.NORTH);
        maze.addWall(3, 1, Direction.EAST);
        maze.addWall(2, 2, Direction.NORTH);
        maze.addWall(2, 2, Direction.WEST);
        maze.addWall(1, 3, Direction.SOUTH);
        maze.addWall(1, 3, Direction.WEST);
        maze.addWall(3, 3, Direction.SOUTH);
        maze.addWall(3, 3, Direction.EAST);
        maze.addWall(2, 2, Direction.EAST);
        maze.addWall(1, 2, Direction.WEST);
        
        maze.setEntryLocation(0, 0);
        maze.setDestination(4, 4);
        
        try {
            MazeAccessor.store(maze, "difficult");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    
    public static void main(String[] args) {
        new MazeWriter();        
    }

}
