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

package zujkomiller.maze.ui;

import zujkomiller.maze.model.*;

import java.awt.event.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;


public class MazeDisplayer extends JPanel {

    protected Grid[][] grids;
    protected HashMap gridsByLocation = new HashMap();
    protected JPanel mainPanel;

    public MazeDisplayer(Maze displayMe) {
        this.displayMaze(displayMe);
        this.setVisible(true);
    }

    public void displayMaze(Maze maze) {
        int height = maze.getHeight();
        int width = maze.getWidth();

        grids = new Grid[width][height];
        Location[][] mazeGrids = maze.getLocations();
        
        int gridHeight = 75;
        int gridWidth = 75;
        
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(width, height));

        Grid grid = null;
        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                grid = new Grid(mazeGrids[x][y], gridWidth, gridHeight);
                gridsByLocation.put(mazeGrids[x][y], grid);
                grids[x][y] = grid;
                JPanel gridPanel = new JPanel();
                gridPanel.setLayout(new BorderLayout());
                gridPanel.add(grid, BorderLayout.CENTER);
                gridPanel.setBorder(BorderFactory.createLineBorder(Color.black));
                mainPanel.add(gridPanel);
            }
        }
        
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        this.setLayout(new GridBagLayout());
        this.add(mainPanel);
        
        Location entry = maze.getEntryLocation();
        JPanel entryPanel = grids[entry.getXCoord()][entry.getYCoord()];
        entryPanel.setLayout(new BorderLayout());
        JLabel start = new JLabel("start", JLabel.CENTER);
        start.setOpaque(false);
        entryPanel.add(start, BorderLayout.SOUTH);

        Location destination = maze.getDestination();
        JPanel destinationPanel =
            grids[destination.getXCoord()][destination.getYCoord()];
        destinationPanel.setLayout(new BorderLayout());
        JLabel end = new JLabel("end", JLabel.CENTER);
        end.setOpaque(false);
        destinationPanel.add(end, BorderLayout.SOUTH);
    }


}