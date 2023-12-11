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

package zujkomiller.maze;

import zujkomiller.maze.ui.*;
import zujkomiller.maze.ui.resultsTool.*;
import zujkomiller.maze.ui.solverTool.*;

import java.awt.Font;
import javax.swing.*;
import javax.swing.plaf.*;

public class Maze {
    
    private static int defaultFontSize = 20;
    
    public Maze() {
        this(defaultFontSize);
    }

    public Maze(int fontSize) {
        this.setFontSize(fontSize);
        // setup the views and their controllers
        
        // view and controller for the Results Displayer tool
        ResultsView resultsView = new ResultsView();
        ResultsController resultsController = new ResultsController(resultsView);
        
        // view and controller for the Solver tool
        SolverView solverView = new SolverView();
        SolverController solverController = new SolverController(solverView);
        
        // view and controller for the top level view
        MazeFrame view = new MazeFrame(resultsView, solverView);
        MazeController controller = new MazeController(view);
    }
    
    private void setFontSize(int fontSize) {
        UIManager.put("MenuItem.font", new FontUIResource("Dialog", Font.PLAIN, fontSize));
        UIManager.put("Menu.font", new FontUIResource("Dialog", Font.PLAIN, fontSize));
        UIManager.put("Label.font", new FontUIResource("Dialog", Font.PLAIN, fontSize));  
        UIManager.put("Button.font", new FontUIResource("Dialog", Font.PLAIN, fontSize));
        UIManager.put("Tree.font", new FontUIResource("Dialog", Font.PLAIN, fontSize));
        UIManager.put("List.font", new FontUIResource("Dialog", Font.PLAIN, fontSize));
        UIManager.put("InternalFrame.font", new FontUIResource("Dialog", Font.BOLD, fontSize));
        UIManager.put("TextArea.font", new FontUIResource("Dialog", Font.PLAIN, fontSize));
        UIManager.put("TextField.font", new FontUIResource("Dialog", Font.PLAIN, fontSize));
        UIManager.put("ComboBox.font", new FontUIResource("Dialog", Font.PLAIN, fontSize));
        UIManager.put("ToolTip.font", new FontUIResource("Dialog", Font.PLAIN, fontSize));
        UIManager.put("TabbedPane.font", new FontUIResource("Dialog", Font.PLAIN, fontSize));

    }
    
    public static void main(String[] args) {
        int fontSize = defaultFontSize;
        if (args.length > 0 && args[0] != null) {
            try {
                fontSize = (Integer.parseInt(args[0]));
            } catch (NumberFormatException nfe) {
                // do nothing; default will be used
            }
        }
        new Maze(fontSize);
    }
    
}
