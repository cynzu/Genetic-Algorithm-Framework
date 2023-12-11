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

package zujkomiller.maze.ui.resultsTool;

import zujkomiller.maze.model.*;
import zujkomiller.maze.results.*;
import zujkomiller.maze.ui.*;

import java.awt.event.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

public class MazeSolutionDisplayer extends MazeDisplayer {
    
    private Maze maze;
    private short[] directions;
    private int directionIndex = 0;
    private Traveler traveler = null;
    private int animateSleepTime = 400;
    private double iconSizeFactor = .2;
    private Rectangle iconRectangle;
    private Grid currentGrid;
    private ArrayList listeners = new ArrayList();
    
    public MazeSolutionDisplayer(Maze maze, Individual individual) {
        super(maze);
        this.maze = maze;
        this.directions = individual.getChromosome().getGenes();
        this.displayStaticPath();
    }
    
    protected void displayStaticPath() {
        Location entryLocation = maze.getEntryLocation(); // logical place
        Grid entryGrid = 
            (Grid)gridsByLocation.get(entryLocation); // graphical representation
        
        Traveler traveler = new Traveler(entryLocation);
                
        // ask the traveler to move in all directions
        Direction nextDirection = null;
        Grid currentGrid = entryGrid;
        Grid nextGrid = null;
        for (int n=0; n<directions.length; n++) {
            nextDirection = GeneTranslator.getDirection(directions[n]);
            drawDirection(currentGrid, nextDirection);
            traveler.travel(nextDirection);
            nextGrid = (Grid)gridsByLocation.get(traveler.getCurrentLocation());
            if (nextGrid != currentGrid) {
               // the traveler didn't hit a wall - yippeee!
                currentGrid = nextGrid;
                
                // the traveler enters the new grid from the opposite
                // direction, so draw the entry
                nextDirection = Direction.getOppositeDirection(nextDirection);
                drawDirection(currentGrid, nextDirection);
            }
        }
    }
    
    protected void drawDirection(Grid grid, Direction direction) {
        if (direction.equals(Direction.NORTH)) {
            grid.drawCenterToNorth();
        } else if (direction.equals(Direction.SOUTH)) {
            grid.drawCenterToSouth();
        } else if (direction.equals(Direction.EAST)) {
            grid.drawCenterToEast();
        } else if (direction.equals(Direction.WEST)) {
            grid.drawCenterToWest();
        }
    }
    
    public void animate() {
        new Thread() {
            public void run() {
                doAnimate();
            }            
        }.start();
    }
    
    public void stepForward() {
        new Thread() {
            public void run() {
               doStepForward(); 
            }            
        }.start();
    }
    
    public void stepBackward() {
        
    }
    
    public void addAnimationListener(AnimationListener listener) {
        synchronized(listeners) {
            if (!listeners.contains(listener)) {
                listeners.add(listener);
            }
        }
    }
    
    public void removeAnimationListener(AnimationListener listener) {
        synchronized(listeners) {
            listeners.remove(listener);
        }
    }
    
    protected void notifyListeners(boolean started) {
        synchronized(listeners) {
            Iterator iterator = listeners.iterator();
            AnimationListener listener = null;
            while (iterator.hasNext()) {
                listener = (AnimationListener)iterator.next();
                if (started) {
                    listener.animationStarted();
                } else {
                    listener.animationStopped();
                }
            }      
        }
    }
    
        
    /**
      * @param time The number of milliseconds during which to wait
      *  between painting images when animating.    
      */
    public void setAnimateSleepTime(int time) {
        this.animateSleepTime = time;
    }
    
    /**
      * @return The number of milliseconds during which to wait
      *  between painting images when animating.    
      */
    public int getAnimateSleepTime() {
        return this.animateSleepTime;
    }

    
    protected void doAnimate() {
        notifyListeners(true);       
        
        // paint an icon in the start location
        Location entryLocation = maze.getEntryLocation(); // logical place
        Grid entryGrid = 
            (Grid)gridsByLocation.get(entryLocation); // graphical representation
        Grid currentGrid = entryGrid;
        paintIconInGrid(currentGrid);
        
        Traveler traveler = new Traveler(entryLocation);
        Direction nextDirection = null;
        Grid nextGrid = null;
          
        // ask the traveler to move in all directions
        for (int n=0; n<directions.length; n++) {
            nextDirection = GeneTranslator.getDirection(directions[n]);
            traveler.travel(nextDirection);
            nextGrid = (Grid)gridsByLocation.get(traveler.getCurrentLocation());
            if (nextGrid != currentGrid) {
                // the traveler didn't hit a wall - yippeee!
                // draw an icon in the center of the grid 
                currentGrid = nextGrid;
                paintIconInGrid(currentGrid);
            } else {               
                // hit a wall
                paintIconHitsWall(currentGrid, nextDirection);
            }
        }
        
        resetAnimation();
        notifyListeners(false);
    }    
    
    protected void doStepForward() {
        notifyListeners(true);

        // make sure the traveler isn't asked to go
        // past the end of the maze
        if (directionIndex >= directions.length) {
            resetAnimation();
        }

        // ask the traveler to move forward
        Direction nextDirection = null;
        Grid nextGrid = null;              

        if (traveler == null) {
            // this is the first time the traveler has been 
            // asked to step forward since resetAnimation() was called
            // so, create a new traveler who starts at the beginning
            Location entryLocation = maze.getEntryLocation(); // logical place
            Grid entryGrid = 
                (Grid)gridsByLocation.get(entryLocation); // graphical representation
            currentGrid = entryGrid;
            traveler = new Traveler(entryLocation); 
        } 

        // draw an icon in the center of the current grid          
        paintIconInGrid(currentGrid);

        // now ask the traveler to move to the next location
        // and increment the directionIndex
        nextDirection = GeneTranslator.getDirection(directions[directionIndex]);
        traveler.travel(nextDirection);
        nextGrid = (Grid)gridsByLocation.get(traveler.getCurrentLocation());
        directionIndex++;

        if (nextGrid != currentGrid) {
            // the traveler didn't hit a wall - yippeee!
            // draw an icon in the center of the current grid 
            currentGrid = nextGrid;
            paintIconInGrid(currentGrid);
        } else {
            // hit a wall
            paintIconHitsWall(currentGrid, nextDirection);
        }
        notifyListeners(false);   
    }
    
    protected void paintIconInGrid(Grid currentGrid) {
        Rectangle currentGridBounds = currentGrid.getParent().getBounds();
        
        // the mainPanel is where the maze is located
        // use it to get the bounds of the maze
        Rectangle mazeRectangle = mainPanel.getBounds();
        double mazeX = mazeRectangle.getX();
        double mazeY = mazeRectangle.getY();
        double mazeWidth = mazeRectangle.getWidth();
        double mazeHeight = mazeRectangle.getHeight();
        
        double x = currentGridBounds.getX() + mazeX;
        double y = currentGridBounds.getY() + mazeY;
        double width = currentGridBounds.getWidth();
        double height = currentGridBounds.getHeight();
        int iconWidth = (int)(width * iconSizeFactor);
        int iconHeight = (int)(height * iconSizeFactor);
        int iconX = (int)(x + ((width/2) - (iconWidth/2)));
        int iconY = (int)(y + ((height/2) - (iconHeight/2)));
        iconRectangle = new Rectangle(iconX, iconY, iconWidth, iconHeight);
        repaint();
        try {
            // sleep so that the animation is slow enough to see
            Thread.currentThread().sleep(animateSleepTime);
        } catch (Exception e) {
            // do nothing
        }   
    }
    
    protected void paintIconHitsWall(Grid currentGrid, Direction nextDirection) {
        Rectangle currentGridBounds = currentGrid.getParent().getBounds();
        
        // the mainPanel is where the maze is located
        // use it to get the bounds of the maze
        Rectangle mazeRectangle = mainPanel.getBounds();
        double mazeX = mazeRectangle.getX();
        double mazeY = mazeRectangle.getY();
        double mazeWidth = mazeRectangle.getWidth();
        double mazeHeight = mazeRectangle.getHeight();
        
        double x = currentGridBounds.getX() + mazeX;
        double y = currentGridBounds.getY() + mazeY;
        double width = currentGridBounds.getWidth();
        double height = currentGridBounds.getHeight();
        int iconWidth = (int)(width * iconSizeFactor);
        int iconHeight = (int)(height * iconSizeFactor);
        int iconX = (int)(x + ((width/2) - (iconWidth/2)));
        int iconY = (int)(y + ((height/2) - (iconHeight/2)));
        
        if (nextDirection.equals(Direction.WEST)) {
            iconX = (int)(iconX - (width/2));
            iconRectangle = new Rectangle(iconX, iconY, iconWidth, iconHeight);   
        } else if (nextDirection.equals(Direction.EAST)) {
            iconX = (int)(iconX + (width/2));
            iconRectangle = new Rectangle(iconX, iconY, iconWidth, iconHeight);
        } else if (nextDirection.equals(Direction.SOUTH)) {
            iconY = (int)(iconY + (height/2));
            iconRectangle = new Rectangle(iconX, iconY, iconWidth, iconHeight);
        } else if (nextDirection.equals(Direction.NORTH)) {
            iconY = (int)(iconY - (height/2));
            iconRectangle = new Rectangle(iconX, iconY, iconWidth, iconHeight);
        } 
        repaint();
        try {
            // sleep so that the animation is slow enough to see
            Thread.currentThread().sleep(animateSleepTime);
        } catch (Exception e) {
            // do nothing
        }
        
        // now move the icon back to the center of the current grid
        iconX = (int)(x + ((width/2) - (iconWidth/2)));
        iconY = (int)(y + ((height/2) - (iconHeight/2)));

        iconRectangle = new Rectangle(iconX, iconY, iconWidth, iconHeight);
        repaint();
        try {
            // sleep so that the animation is slow enough to see
            Thread.currentThread().sleep(animateSleepTime);
        } catch (Exception e) {
            // do nothing
        }
        
    }
    
    protected void resetAnimation() {
        iconRectangle = null;
        currentGrid = null;
        traveler = null;
        directionIndex = 0;
    }
    
        
    public void paint(Graphics g) {
        super.paint(g);
        
        if (iconRectangle != null) {
            g.setColor(Color.red);
            g.fillOval((int)iconRectangle.getX(), (int)iconRectangle.getY(), 
                iconRectangle.width, iconRectangle.height);
        }  
    }
    
}