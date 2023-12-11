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

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import javax.swing.*;
import javax.swing.border.*;

public class Grid extends JPanel {
    
    private Location mazeGrid;
    private JPanel topLeftCorner;
    private JPanel topRightCorner;
    private JPanel bottomLeftCorner;
    private JPanel bottomRightCorner;
    private JPanel topBar;
    private JPanel bottomBar;
    private JPanel leftBar;
    private JPanel rightBar;
    private Color backgroundColor = new Color(204, 204, 255);
    private Color wallColor = Color.black;
    
    private boolean drawCenterToEast = false;
    private boolean drawCenterToWest = false;
    private boolean drawCenterToNorth = false;
    private boolean drawCenterToSouth = false;
    
    public Grid(Location loc, int width, int height) {
        this.mazeGrid = loc;
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(backgroundColor);
        this.setupCornersAndBars();
        this.paintBorder();
        
    }
    
    private void setupCornersAndBars() {
        this.topLeftCorner = new JPanel();
        this.topRightCorner = new JPanel();
        this.bottomLeftCorner = new JPanel();
        this.bottomRightCorner = new JPanel();
        this.topBar = new JPanel();
        this.bottomBar = new JPanel();
        this.leftBar = new JPanel();
        this.rightBar = new JPanel();
        
        topLeftCorner.setBackground(backgroundColor);
        topRightCorner.setBackground(backgroundColor);
        bottomLeftCorner.setBackground(backgroundColor);
        bottomRightCorner.setBackground(backgroundColor);
        topBar.setBackground(backgroundColor);
        bottomBar.setBackground(backgroundColor);
        leftBar.setBackground(backgroundColor);
        rightBar.setBackground(backgroundColor);
        
        Dimension gridDim = this.getPreferredSize();
        int gridWidth = gridDim.width;
        int gridHeight = gridDim.height;
        int cornerWidth = (int)(gridWidth * .05);   
        int cornerHeight = (int)(gridHeight * .05);
        
        this.setLayout(null);
        this.add(topLeftCorner);
        this.add(topRightCorner);
        this.add(bottomLeftCorner);
        this.add(bottomRightCorner);
        this.add(topBar);
        this.add(bottomBar);
        this.add(leftBar);
        this.add(rightBar);
        
        topLeftCorner.setLocation(0, 0);
        topLeftCorner.setSize(cornerWidth, cornerHeight);
        
        topRightCorner.setLocation(gridWidth - cornerWidth, 0);
        topRightCorner.setSize(cornerWidth, cornerHeight);
    
        bottomLeftCorner.setLocation(0, gridHeight - cornerHeight);
        bottomLeftCorner.setSize(cornerWidth, cornerHeight);
    
        bottomRightCorner.setLocation(gridWidth - cornerWidth,
            gridHeight - cornerHeight);
        bottomRightCorner.setSize(cornerWidth, cornerHeight);
        
        topBar.setLocation(cornerWidth, 0);
        topBar.setSize(gridWidth - (cornerWidth * 2), cornerHeight);
    
        bottomBar.setLocation(cornerWidth, gridHeight - cornerHeight);
        bottomBar.setSize(gridWidth - (cornerWidth * 2), cornerHeight);
    
        leftBar.setLocation(0, cornerHeight);
        leftBar.setSize(cornerHeight, gridHeight - (cornerHeight * 2));

        rightBar.setLocation(gridWidth - cornerWidth, cornerHeight);
        rightBar.setSize(cornerHeight, gridHeight - (cornerHeight * 2));    
    }
    
    private void paintBorder() {       
        MazeElement northNeighbor = mazeGrid.getAdjacentElement(Direction.NORTH);
        MazeElement southNeighbor = mazeGrid.getAdjacentElement(Direction.SOUTH);
        MazeElement eastNeighbor = mazeGrid.getAdjacentElement(Direction.EAST);
        MazeElement westNeighbor = mazeGrid.getAdjacentElement(Direction.WEST);
        
        boolean southIsWall = southNeighbor.equals(Wall.getInstance());
        boolean eastIsWall = eastNeighbor.equals(Wall.getInstance());
        boolean westIsWall = westNeighbor.equals(Wall.getInstance());
        boolean northIsWall = northNeighbor.equals(Wall.getInstance());
        
        if (southIsWall) {
            this.bottomBar.setBackground(wallColor);
            this.bottomLeftCorner.setBackground(wallColor);
            this.bottomRightCorner.setBackground(wallColor);
        }
        
        if (eastIsWall) {
            this.rightBar.setBackground(wallColor);
            this.topRightCorner.setBackground(wallColor);
            this.bottomRightCorner.setBackground(wallColor);
        }
        
        if (westIsWall) {
            this.leftBar.setBackground(wallColor);
            this.topLeftCorner.setBackground(wallColor);
            this.bottomLeftCorner.setBackground(wallColor);
        }
        
        if (northIsWall) {
            this.topBar.setBackground(wallColor);
            this.topLeftCorner.setBackground(wallColor);
            this.topRightCorner.setBackground(wallColor);
        }
        
        // now determine if we need to fill in corners
        if (northNeighbor instanceof Location && 
                westNeighbor instanceof Location) {
            if ((((Location)northNeighbor).getAdjacentElement(Direction.WEST).
                equals(Wall.getInstance()) && ((Location)westNeighbor).
                    getAdjacentElement(Direction.NORTH).
                        equals(Wall.getInstance()))) {
                topLeftCorner.setBackground(wallColor);
            }            
        }   
        if (northNeighbor instanceof Location && 
                eastNeighbor instanceof Location) {
            if ((((Location)northNeighbor).getAdjacentElement(Direction.EAST).
                equals(Wall.getInstance()) && ((Location)eastNeighbor).
                    getAdjacentElement(Direction.NORTH).
                        equals(Wall.getInstance()))) {
                topRightCorner.setBackground(wallColor);
            }            
        } 
        if (southNeighbor instanceof Location && 
                eastNeighbor instanceof Location) {
            if ((((Location)southNeighbor).getAdjacentElement(Direction.EAST).
                equals(Wall.getInstance()) && ((Location)eastNeighbor).
                    getAdjacentElement(Direction.SOUTH).
                        equals(Wall.getInstance()))) {
                bottomRightCorner.setBackground(wallColor);
            }            
        } 
        if (southNeighbor instanceof Location && 
                westNeighbor instanceof Location) {
            if ((((Location)southNeighbor).getAdjacentElement(Direction.WEST).
                equals(Wall.getInstance()) && ((Location)westNeighbor).
                    getAdjacentElement(Direction.SOUTH).
                        equals(Wall.getInstance()))) {
                bottomLeftCorner.setBackground(wallColor);
            }            
        } 
    }
    
    public void drawCenterToEast() {
        drawCenterToEast = true;
        repaint();
    }
    
    public void drawCenterToWest() {
        drawCenterToWest = true;
        repaint();
    }

    public void drawCenterToNorth() {
        drawCenterToNorth = true;
        repaint();
    }
    
    public void drawCenterToSouth() {
        drawCenterToSouth = true;
        repaint();
    }
    
    public void clearDrawing() {
        drawCenterToEast = false;
        drawCenterToWest = false;
        drawCenterToNorth = false;
        drawCenterToSouth = false;
        repaint();
    }
    
    public void paint(Graphics g) {
        super.paint(g);
        Dimension size = this.getSize();
        int width = size.width;// - (this.leftBar.getSize().width  + this.rightBar.getSize().width);
        int height = size.height;// - (this.topBar.getSize().width * 2);
        int centerX = ((int)width/2);
        int centerY = ((int)height/2);
     
        if (drawCenterToEast) {
            g.setColor(Color.red);
            g.drawLine(centerX, centerY - 1, width - 1, centerY - 1);
            g.drawLine(centerX, centerY, width, centerY);
            g.drawLine(centerX, centerY + 1, width + 1, centerY + 1);
        }
        
        if (drawCenterToWest) {
            g.setColor(Color.red);
            g.drawLine(centerX, centerY - 1, 0, centerY - 1);
            g.drawLine(centerX, centerY, 0, centerY);
            g.drawLine(centerX, centerY + 1, 0, centerY + 1);
        }
      
        if (drawCenterToNorth) {
            g.setColor(Color.red);
            g.drawLine(centerX - 1, centerY, centerY - 1, 0);
            g.drawLine(centerX, centerY, centerY, 0);
            g.drawLine(centerX + 1, centerY, centerY + 1, 0);
        }
        
        if (drawCenterToSouth) {
            g.setColor(Color.red);
            g.drawLine(centerX - 1, centerY, centerY - 1, height);
            g.drawLine(centerX, centerY, centerY, height);
            g.drawLine(centerX + 1, centerY, centerY + 1, height);
        }
    }   
        
}