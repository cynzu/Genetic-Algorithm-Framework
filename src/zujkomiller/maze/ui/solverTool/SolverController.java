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

package zujkomiller.maze.ui.solverTool;

import zujkomiller.maze.ui.*;
import zujkomiller.maze.persistence.MazeAccessor;
import zujkomiller.maze.options.*;
import java.awt.event.*;
import java.io.*;

public class SolverController {
    
    private SolverView view;
    private ActionListener openListener;
    private ActionListener previewListener;
    
    public SolverController(SolverView view) {
        this.view = view;
        this.view.setExitActionListener(MazeController.getExitListener());
        this.view.setExitToolActionListener(MazeController.getExitToolListener());
        this.view.setOpenActionListener(getOpenListener());
        this.view.setPreviewActionListener(getPreviewListener());
        this.view.setMazeNames(MazeAccessor.getMazeNames());
    }
    
    public ActionListener getOpenListener() {
        if (this.openListener == null) {
            this.openListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    File[] files = MazeAccessor.getOptionsFileNames();
                    File toOpen = view.getOpenFileName(files);
                    if (toOpen != null) {
                        RunOptions ops = MazeAccessor.loadOptions(toOpen);
                        view.displayOptions(ops);
                    } 

                }                
            };
        }
        return this.openListener;
    }
    
    public ActionListener getPreviewListener() {
        if (this.previewListener == null) {
            previewListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String mazeName = view.getSelectedMaze();
                    view.previewMaze(new MazeDisplayer
                        (MazeAccessor.load(mazeName)));                    
                }  
            };          
        }       
        return previewListener;
    }
   
    
}