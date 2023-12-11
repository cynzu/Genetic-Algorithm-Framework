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

import java.awt.event.*;

public class MazeController {

    private static MazeFrame mazeFrame;
    private static ActionListener exitListener;
    private static ActionListener exitToolListener;
    private ActionListener creatorToolListener;
    private ActionListener solverToolListener;
    private ActionListener resultsToolListener;
    
    public MazeController(MazeFrame view) {
        this.mazeFrame = view;
        this.mazeFrame.setExitActionListener(getExitListener());
        this.mazeFrame.setCreatorToolActionListener(getCreatorToolListener());
        this.mazeFrame.setSolverToolActionListener(getSolverToolListener());
        this.mazeFrame.setResultsToolActionListener(getResultsToolListener());
        this.mazeFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                closeMazeFrame();
            }
        });
        this.mazeFrame.setVisible(true); 
    }
    
    public static MazeFrame getMazeFrame() {
        return mazeFrame;
    }
    
    private static void closeMazeFrame() {
        mazeFrame.dispose();
        System.exit(0);
    }
    
    public static ActionListener getExitListener() {
        if (exitListener == null) {
            exitListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    closeMazeFrame();
                }
            };
        }
        return exitListener;
    }

    public static ActionListener getExitToolListener() {
        if (exitToolListener == null) {
            exitToolListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    mazeFrame.showInitialMenu();
                }
            };
        }
        return exitToolListener;
    }
    
    public ActionListener getCreatorToolListener() {
        if (creatorToolListener == null) {
            creatorToolListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.out.println("creator");
                }
            };
        }
        return creatorToolListener;
    }
    
     
    public ActionListener getSolverToolListener() {
        if (solverToolListener == null) {
            solverToolListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    mazeFrame.showSolverTool();
                }
            };
        }
        return solverToolListener;
    }
    
    
    public ActionListener getResultsToolListener() {
        if (resultsToolListener == null) {
            resultsToolListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    mazeFrame.showResultsTool();
                }
            };
        }
        return resultsToolListener;
    }
}
