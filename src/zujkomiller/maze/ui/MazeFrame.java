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

import zujkomiller.maze.ui.resultsTool.*;
import zujkomiller.maze.ui.solverTool.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MazeFrame extends JFrame {

    private Container contentPane;
    private JPanel initialPanel;
    private JMenuBar initialMenu;
    private JMenuItem exitMenuItem;
    private JMenuItem creatorToolMenuItem;
    private JMenuItem solverToolMenuItem;
    private JMenuItem resultsToolMenuItem;
    
    // helper view classes for the different tools
    private ResultsView resultsView;
    private SolverView solverView;
    
    
    public MazeFrame(ResultsView resultsView, SolverView solverView) {
        super("Maze");
        this.contentPane = this.getContentPane();
        this.initialPanel = new JPanel();
        this.resultsView = resultsView;
        this.solverView = solverView;
        this.addWindowListener(new WindowAdapter() {
           public void windowClosing(WindowEvent e) {
                System.exit(0);
           }            
        });
        this.sizeAndPlace();
        this.setupInitialMenu();
        this.showInitialMenu();
    }
    
    private void sizeAndPlace() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int thisWidth = (int)(screenWidth * .75);
        int thisHeight = (int)(screenHeight * .75);        
        this.setSize(new Dimension(thisWidth, thisHeight));
        this.setLocation(screenWidth/2 - thisWidth/2,
            screenHeight/2 - thisHeight/2);
    }
    
    private void setupInitialMenu() {
        this.initialMenu = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu toolsMenu = new JMenu("Tools");
        
        this.exitMenuItem = new JMenuItem("Exit");
        fileMenu.add(exitMenuItem);
        
        this.creatorToolMenuItem = new JMenuItem("Maze Creator");
        this.solverToolMenuItem = new JMenuItem("Maze Pathfinder");
        this.resultsToolMenuItem = new JMenuItem("Results Displayer");
        toolsMenu.add(creatorToolMenuItem);
        toolsMenu.add(solverToolMenuItem);
        toolsMenu.add(resultsToolMenuItem);
        
        initialMenu.add(fileMenu);
        initialMenu.add(toolsMenu);
    }

    
    public void setExitActionListener(ActionListener listener) {
        this.exitMenuItem.addActionListener(listener);
    }
    
    public void setCreatorToolActionListener(ActionListener listener) {
        this.creatorToolMenuItem.addActionListener(listener);
    }
    
        
    public void setSolverToolActionListener(ActionListener listener) {
        this.solverToolMenuItem.addActionListener(listener);
    }
    
        
    public void setResultsToolActionListener(ActionListener listener) {
        this.resultsToolMenuItem.addActionListener(listener);
    }
        
    public void showInitialMenu() {
        this.setTitle("Maze");
        this.setCurrentMenuBar(this.initialMenu);
        this.setCurrentContent(initialPanel);
    }
    
    public void showResultsTool() {
        this.setTitle("Maze - Results Displayer");
        this.setCurrentMenuBar(this.resultsView.getResultsToolMenuBar()); 
        this.setCurrentContent(this.resultsView);
    }
    
    public void showSolverTool() {
        this.setTitle("Maze - Pathfinder");
        this.setCurrentMenuBar(this.solverView.getSolverToolMenuBar());
        this.setCurrentContent(this.solverView);
    }
    
    private void setCurrentMenuBar(JMenuBar menuBar) {
        this.setJMenuBar(menuBar);
        this.validateTree();
    }
    
    private void setCurrentContent(JPanel panel) {
        this.contentPane.removeAll();
        this.contentPane.setLayout(new BorderLayout());
        this.contentPane.add(panel, BorderLayout.CENTER);
        this.doLayout();
        this.repaint();
        this.validateTree();
    }
    
}