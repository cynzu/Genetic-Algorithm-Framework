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
import zujkomiller.maze.persistence.*;

import java.io.File;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;

public class ResultsController {
    
    private MazeEvolutionData model;
    private ResultsView view;
    
    private ActionListener openListener;
    private MouseAdapter treeListener;

    public ResultsController (ResultsView view) {
        this.view = view;     
        this.view.setExitActionListener(MazeController.getExitListener());
        this.view.setExitToolActionListener(MazeController.getExitToolListener());
        this.view.setOpenActionListener(getOpenListener());
        this.view.setIndexTreeListener(getTreeListener());
    }
    
    public ActionListener getOpenListener() {
        if (openListener == null) {
            openListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    File resultsFile = view.getOpenFileName
                        (MazeAccessor.getResultsFileNames());
                    if (resultsFile != null) {
                        model = MazeAccessor.loadResults(resultsFile);
                        view.showResultsIndex(model.getScoreData());
                    } else {
                        view.showNoResultsMsg();
                    }
                }
            };            
        }
        return openListener;
    }
     
    
    public MouseAdapter getTreeListener() {
        if (treeListener == null) {
            treeListener = new MouseAdapter() {
                public void mouseClicked (MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        TreePath path = ((JTree)e.getComponent()).
                            getPathForLocation(e.getX(), e.getY());
                        if (path != null) {
                            view.handleTreePathSelected(path, getMaze());
                        }
                    }
                }
            };            
        }
        return treeListener;        
    }
    
    public Maze getMaze() {
        return this.model.getMaze();
    }

}
