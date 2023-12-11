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

import zujkomiller.maze.ui.*;
import zujkomiller.maze.model.*;
import zujkomiller.maze.results.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.tree.*;
import javax.swing.plaf.*;

public class ResultsView extends JPanel implements AnimationListener {
    
    private static final String PROP_FILE = 
        "zujkomiller.maze.ui.resultsTool.results";
    private static final String SHOW_INDEX = "Show Index Window";
    private static final String HIDE_INDEX = "Hide Index Window";
    private static final String SHOW_SOLUTION = "Show Solution Window";
    private static final String HIDE_SOLUTION = "Hide Solution Window";
    
    private static String scoreAlgorithm;
    
    // top-level widgets
    private JDesktopPane desktopPane;   
    
    // widgets to show the results index
    private JInternalFrame indexFrame;
    private JScrollPane indexScroller;
    private JTree indexTree;
    private MouseListener indexTreeListener;
    
    // widgets to display a single solution
    private MazeSolutionDisplayer solutionDisplayer;
    private JInternalFrame solutionFrame;
    private JScrollPane solutionScroller;
    private JTextArea solutionText;
    private JButton btnAnimate;
    private JButton btnStepForward;
    private JButton btnStepBackward;
    private ActionListener animateListener;
    private JComboBox speedChooser;
    private int baseAnimationSleep = 500;
    
    // widgets to show explanation of scores
    private JInternalFrame scoreFrame;
    
    // menu widgets
    private JMenuBar menuBar;
    private JMenuItem exitMenuItem;
    private JMenuItem exitToolMenuItem;
    private JMenuItem openMenuItem;
    private JMenuItem closeMenuItem;
    private JMenuItem showHideIndexMenuItem;
    private JMenuItem showHideSolutionMenuItem;
    private JMenuItem animateMenuItem;
    
    // results chooser widgets
    private JDialog resultFileChooser;
    private JList resultFileList;
    private File selectedResultFile;
    private int screenWidth;
    private int screenHeight;
    
    // no results chosen widgets
    private JOptionPane noResultsWarning;
    
    static {
        ResourceBundle resources = ResourceBundle.getBundle(PROP_FILE);
        scoreAlgorithm = resources.getString("scoringRules");      
    }
    
    public ResultsView() {
        this.setLayout(new BorderLayout());
        this.desktopPane = new JDesktopPane();
        this.desktopPane.setOpaque(false);
        this.add(desktopPane, BorderLayout.CENTER);
        this.setupMenuBar();
        this.setupSpeedChooser();
        this.setBackground(Color.darkGray);
        this.buildResultFileChooser();
        this.buildIndexFrame();
        this.buildSolutionFrame();
        this.buildScoreFrame();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.screenWidth = screenSize.width;
        this.screenHeight = screenSize.height;
    }
    
    public JMenuBar getResultsToolMenuBar() {
        return this.menuBar;
    }
    
    private void setupMenuBar() {
        this.menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu viewMenu = new JMenu("View");
        
        this.openMenuItem = new JMenuItem("Open");
        this.closeMenuItem = new JMenuItem("Close");
        closeMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeCurrentResults();
            }
        });
        this.exitToolMenuItem = new JMenuItem("Exit Results Tool");
        this.exitMenuItem = new JMenuItem("Exit Maze");
        fileMenu.add(this.openMenuItem);
        fileMenu.add(this.closeMenuItem);
        fileMenu.add(this.exitToolMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(this.exitMenuItem);
        fileMenu.add(exitMenuItem);
        
        JMenuItem scoreExplainMenuItem = new JMenuItem("Scoring Rules");
        scoreExplainMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showExplainScores();
            }
        });
        
        this.showHideIndexMenuItem = new JMenuItem(SHOW_INDEX);
        showHideIndexMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showHideIndex();
            }
        });
        this.showHideSolutionMenuItem = new JMenuItem(SHOW_SOLUTION);
        showHideSolutionMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showHideSolution();
            }
        });
        this.showHideIndexMenuItem.setEnabled(false);
        this.showHideSolutionMenuItem.setEnabled(false);
        this.animateMenuItem = new JMenuItem("Animate Results");
        animateMenuItem.addActionListener(this.getAnimateListener());
        this.animateMenuItem.setEnabled(false);
       
        viewMenu.add(scoreExplainMenuItem);
        viewMenu.addSeparator();
        viewMenu.add(this.showHideIndexMenuItem);
        viewMenu.add(this.showHideSolutionMenuItem);
        viewMenu.addSeparator();
        viewMenu.add(this.animateMenuItem);
        
        this.menuBar.add(fileMenu);
        this.menuBar.add(viewMenu);
    }
    
    private void setupSpeedChooser() {        
        speedChooser = new JComboBox();
        speedChooser.addItem("slow");      
        speedChooser.addItem("medium");
        speedChooser.addItem("fast");
        speedChooser.setSelectedItem("slow");
        speedChooser.setToolTipText("speed");
        speedChooser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateAnimationSpeed();
            }
        });
    }
    
    private void buildResultFileChooser() {
        this.resultFileChooser = new JDialog(new Frame(),
            "Choose Results", true);
        this.resultFileList = new JList();
        resultFileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(resultFileList);
        
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 2, 2));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        JPanel buttonSizerPanel = new JPanel();
        buttonSizerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonSizerPanel.add(buttonPanel);
        
        JLabel label = new JLabel("Please select a set of results to view.");
        
        JPanel dialogContents = new JPanel();
        dialogContents.setLayout(new BorderLayout());
        dialogContents.add(label, BorderLayout.NORTH);
        dialogContents.add(scrollPane, BorderLayout.CENTER);
        dialogContents.add(buttonSizerPanel, BorderLayout.SOUTH);
        dialogContents.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                
        Container contentPane = this.resultFileChooser.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(dialogContents, BorderLayout.CENTER);
        
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Object selection = resultFileList.getSelectedValue();
                if (selection != null) {
                    FileChoice choice = (FileChoice)selection;
                    selectedResultFile = choice.getFile();
                } else {
                    selectedResultFile = null;
                }
                resultFileChooser.setVisible(false);
            }            
        });
         
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resultFileChooser.setVisible(false);
            }            
        });  
    
        resultFileList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = resultFileList.locationToIndex
                        (new Point(e.getX(), e.getY()));
                    if (index > -1 && resultFileList.isSelectedIndex(index)) {
                        Object selection = resultFileList.getSelectedValue();
                        if (selection != null) {
                            FileChoice choice = (FileChoice)selection;
                            selectedResultFile = choice.getFile();
                        } else {
                            selectedResultFile = null;
                        }
                        resultFileChooser.setVisible(false);
                    }
                }
            }
        });
    
        
        resultFileChooser.pack();
        resultFileChooser.setVisible(false);
    }

    
    public void setOpenActionListener(ActionListener listener) {
        this.openMenuItem.addActionListener(listener);
    }
                 
    public void setExitToolActionListener(ActionListener listener) {
        this.exitToolMenuItem.addActionListener(listener);
    }
             
    public void setExitActionListener(ActionListener listener) {
        this.exitMenuItem.addActionListener(listener);
    }    
    
    public void setIndexTreeListener(MouseAdapter listener) {
        this.indexTreeListener = listener;
        if (this.indexTree != null) {
            this.indexTree.addMouseListener(listener);
        }
    }
    
    public File getOpenFileName(File[] filesToChooseFrom) {
        FileChoice[] choices = new FileChoice[filesToChooseFrom.length];        
        File f = null;
        if (filesToChooseFrom != null) {
            for (int n=0; n<filesToChooseFrom.length; n++) {
                choices[n] = new FileChoice(filesToChooseFrom[n]);
            }
        }
        
        showResultFileChooser(choices);
        return this.selectedResultFile;
    }
    
    private void showResultFileChooser(FileChoice[] choices) {
        resultFileList.setListData(choices);
        resultFileChooser.setLocationRelativeTo(this);
        Dimension dialogDim = resultFileChooser.getPreferredSize();
        resultFileChooser.setLocation(screenWidth/2 - dialogDim.width/2,
            screenHeight/2 - dialogDim.height/2);;
        resultFileChooser.pack();
        resultFileChooser.setVisible(true);
    }
    
    public void showNoResultsMsg() {
        JOptionPane.showMessageDialog(this,
            "No results have been chosen for display.");
    }
    
    public void closeCurrentResults() {
        desktopPane.removeAll();
        this.showHideIndexMenuItem.setEnabled(false);
        this.showHideSolutionMenuItem.setEnabled(false);
        this.animateMenuItem.setEnabled(false);
        this.showHideIndexMenuItem.setText(SHOW_INDEX);
        this.showHideSolutionMenuItem.setText(SHOW_SOLUTION);
        doLayout();
        repaint();
        validate();
    }
    
    public void showResultsIndex(GenerationScoreData[] scoreData) {
        this.indexTree = buildIndexTree(scoreData);
        if (indexTree != null) {
            if (indexTreeListener != null) {
                indexTree.addMouseListener(indexTreeListener);
            }
            indexTree.setRootVisible(true);
            this.indexScroller.setViewportView(indexTree);
            this.solutionScroller.setViewportView(new JPanel());
        }
        this.showHideIndexMenuItem.setEnabled(true);
        this.showHideSolutionMenuItem.setEnabled(true);
        if (this.solutionFrame != null) {
            this.solutionFrame.setTitle("");
        }
        showIndexFrame();
    }
    
    protected JTree buildIndexTree(GenerationScoreData[] scoreData) {
        JTree tree = new JTree(scoreData);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode
            (this.selectedResultFile.getName());
        
        GenerationScoreData data = null;
        for (int n=0; n<scoreData.length; n++) {
            data = scoreData[n]; 
            int genNum = data.getGenerationNumber();
            DefaultMutableTreeNode genNode = 
                new DefaultMutableTreeNode("Generation " + genNum);
            Cohort[] cohorts = data.getCohorts();
            
            for (int x=0; x<cohorts.length; x++) {
                DefaultMutableTreeNode cohortNode = 
                    new DefaultMutableTreeNode("score: " + cohorts[x].getScore());
                Individual[] individuals = cohorts[x].getIndividuals();
                
                for (int y=0; y<individuals.length; y++) {
                    DefaultMutableTreeNode indivNode = 
                        new DefaultMutableTreeNode(individuals[y]);
                    cohortNode.add(indivNode);
                }
                
                genNode.add(cohortNode);
            }
            root.add(genNode);
        }
        
        tree = new JTree(new DefaultTreeModel(root));
        return tree;
    }
    
    protected void showIndexFrame() {
        desktopPane.remove(indexFrame);
        this.showHideIndexMenuItem.setText(HIDE_INDEX);
        Dimension dim = this.desktopPane.getSize();
        indexFrame.setSize(
            new Dimension( ((int)(dim.width * .25)) , dim.height));
        desktopPane.add(indexFrame, JLayeredPane.DEFAULT_LAYER);
        indexFrame.setLocation(0, 0);
        indexFrame.setVisible(true);        
    }
    
    protected void showHideIndex() {
        if (indexFrame.isVisible()) {
            indexFrame.setVisible(false);
            desktopPane.remove(indexFrame);
            this.showHideIndexMenuItem.setText(SHOW_INDEX);
        } else {
            showIndexFrame();
        }
    }
       
    protected void showHideSolution() {
        if (solutionFrame.isVisible()) {
            solutionFrame.setVisible(false);
            desktopPane.remove(solutionFrame);
            this.showHideSolutionMenuItem.setText(SHOW_SOLUTION);
        } else {
            showSolutionFrame();
        }
    }
    
    protected void showSolutionFrame() {        
        desktopPane.remove(solutionFrame);
        this.showHideSolutionMenuItem.setText(HIDE_SOLUTION);
        Dimension dim = this.desktopPane.getSize();
        solutionFrame.setSize(
            new Dimension( ((int)(dim.width * .75)) , dim.height));
        desktopPane.add(solutionFrame, JLayeredPane.DEFAULT_LAYER);
        int xCoord = dim.width - solutionFrame.getSize().width;
        solutionFrame.setLocation(xCoord, 0);
        solutionFrame.setVisible(true); 
    }
    
    protected void buildIndexFrame() {
        this.indexFrame = new JInternalFrame("Index", true, false, true, true);
        Container indexContentPane = indexFrame.getContentPane();
        this.indexScroller = new JScrollPane
            (ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        indexContentPane.setLayout(new BorderLayout());
        indexContentPane.add(indexScroller, BorderLayout.CENTER); 
    }
    
    protected void buildSolutionFrame() {
        this.solutionFrame = new JInternalFrame("Solution", true, false, true, true);
        Container solutionContentPane = solutionFrame.getContentPane();
        this.solutionScroller = new JScrollPane
            (ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        solutionContentPane.setLayout(new BorderLayout());
        solutionContentPane.add(solutionScroller, BorderLayout.CENTER); 
    }
    
    protected void buildScoreFrame() {
        JTextArea scoreText = new JTextArea();
        scoreText.setText(scoreAlgorithm);
        scoreText.setWrapStyleWord(true);
        scoreText.setAutoscrolls(true);
        scoreText.setEditable(false);
        scoreText.setLineWrap(true);
        scoreText.setRows(10);
        scoreText.setColumns(50);
        scoreText.setOpaque(false); 
        scoreText.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        JScrollPane scoreScroller = new JScrollPane(scoreText);
        
        this.scoreFrame = new JInternalFrame
            ("How Scores are Determined", true, true, true, true);
        scoreFrame.getContentPane().add(scoreScroller, BorderLayout.CENTER);
        scoreFrame.pack();   
    }
    
    public void handleTreePathSelected(TreePath path, Maze maze) {
        Object obj = path.getLastPathComponent();
        if (obj instanceof DefaultMutableTreeNode) {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)obj;
            obj = selectedNode.getUserObject();
            if (obj instanceof Individual) {
                Individual selectedIndividual = (Individual)obj;
                this.displayIndividual(selectedIndividual, maze);
            }
        }
        
    }
    
    private void displayIndividual(Individual individual, Maze maze) {
        this.solutionScroller.setViewportView
            (buildSolutionPanel(individual, maze));
        showSolutionFrame();
        this.animateMenuItem.setEnabled(true);
    }
    
    private JPanel buildSolutionPanel(Individual individual, Maze maze) {
        // in the title of the frame, show text describing which
        // individual is being displayed
        this.solutionFrame.setTitle("Generation " + 
            individual.getGenerationNumber() +  
            ", " + "score " + individual.getChromosome().getFitnessScore() +
            ", " + individual.getName());
        
        // area for showing text data about the solution
        solutionText = new JTextArea();
        solutionText.setWrapStyleWord(true);
        solutionText.setAutoscrolls(true);
        solutionText.setEditable(false);
        solutionText.setLineWrap(true);
        solutionText.setRows(3);
        solutionText.setColumns(45);
        solutionText.setOpaque(false);
        String directions = translateChrom(individual);
        solutionText.setText(directions);
        solutionText.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        JScrollPane textScroller = new JScrollPane(solutionText);
        textScroller.setBorder(BorderFactory.createLineBorder(Color.black));
        
        JPanel directionsPanel = new JPanel();
        directionsPanel.setLayout(new BorderLayout());
        directionsPanel.add(new JLabel("Directions Traveled"), BorderLayout.NORTH);
        directionsPanel.add(textScroller, BorderLayout.CENTER);
        directionsPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 10, 20));

        // buttons for animating the maze        
        this.btnAnimate = new JButton("Animate");
        btnAnimate.addActionListener(this.getAnimateListener());
        this.btnStepForward = new JButton("Step");
        btnStepForward.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stepForward();
            }
        });

        // animate and step buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 5, 5));
        buttonPanel.add(btnAnimate);
        buttonPanel.add(btnStepForward);
        JPanel buttonSizer = new JPanel();
        buttonSizer.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttonSizer.add(buttonPanel);
       
        // animation speed chooser
        JPanel speedSizer = new JPanel();
        speedSizer.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        speedSizer.add(speedChooser);
        speedSizer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        
        // all animiation controls
        JPanel animatePanel = new JPanel();
        animatePanel.setLayout(new BorderLayout());
        animatePanel.add(buttonSizer, BorderLayout.CENTER); 
        animatePanel.add(speedSizer, BorderLayout.WEST); 
        JPanel animateSizer = new JPanel();
        animateSizer.setLayout(new FlowLayout(FlowLayout.CENTER));
        animateSizer.add(animatePanel);
        animatePanel.setBorder(BorderFactory.createLineBorder(Color.black));
        
        // the maze itself
        solutionDisplayer = 
            new MazeSolutionDisplayer(maze, individual);
        solutionDisplayer.addAnimationListener(this);
        solutionDisplayer.setAnimateSleepTime(baseAnimationSleep);
        
        // put the animation controls below the maze
        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.ipadx = 20;
        gbc.ipady = 20;
        gbc.gridx = 0;
        gbc.gridy = 0;
        displayPanel.add(solutionDisplayer, gbc);
        gbc.insets = new Insets(0, 20, 5, 20);
        gbc.gridx = 0;
        gbc.gridy = 1;
        displayPanel.add(animateSizer, gbc);
        
        // now put the text below the maze 
        // with its animation controls
        JPanel solutionPanel = new JPanel();
        solutionPanel.setLayout(new BorderLayout());
        solutionPanel.add(displayPanel, BorderLayout.CENTER);
        solutionPanel.add(directionsPanel, BorderLayout.SOUTH);
        solutionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        return solutionPanel;        
    }
    
    
    private String translateChrom(Individual individual) {
        short[] genes = individual.getChromosome().getGenes();
        StringBuffer directions = new StringBuffer();
        for (int n=0; n<genes.length; n++) {
            directions.append(Direction.toChar((GeneTranslator.getDirection(genes[n]))));
            if (n != genes.length - 1) {
                directions.append(", ");
            }    
        }
        return directions.toString();       
    }
    
    private void showExplainScores() { 
        desktopPane.remove(this.scoreFrame);
        
        Dimension dim = this.desktopPane.getSize();
        desktopPane.add(scoreFrame, JLayeredPane.DEFAULT_LAYER, 0);
     //   desktopPane.setLayer(scoreFrame, JLayeredPane.DEFAULT_LAYER, 0);
        Dimension scoreDim = scoreFrame.getPreferredSize();

        scoreFrame.setLocation((int)(dim.width/2 - scoreDim.width/2),
            (int)(dim.height/2 - scoreDim.height/2));
        scoreFrame.setVisible(true);    
    }
    
    public void animate() {
        updateAnimationSpeed();
        this.solutionDisplayer.animate();
    }
    
    public void stepForward() {
        updateAnimationSpeed();
        this.solutionDisplayer.stepForward();
    }
    
    public void stepBackward() {
        updateAnimationSpeed();
        this.solutionDisplayer.stepBackward();
    }
    
    public void animationStarted() {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    enableAnimationWidgets(false);
                }
            });
        } catch (Exception e) {
            // do nothing
        }
    }
    
    public void animationStopped() {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    enableAnimationWidgets(true);
                }
            });
        } catch (Exception e) {
            // do nothing
        }
    }
    
    public void updateAnimationSpeed() {
        String speed = (String)speedChooser.getSelectedItem();
        if (speed.equalsIgnoreCase("slow")){
            solutionDisplayer.setAnimateSleepTime((int)(baseAnimationSleep * 2)); 
        } else if (speed.equalsIgnoreCase("medium")){
            solutionDisplayer.setAnimateSleepTime(baseAnimationSleep);
        } else if (speed.equalsIgnoreCase("fast")){
            solutionDisplayer.setAnimateSleepTime((int)(baseAnimationSleep * .5));
        } 
    }
    
    private void enableAnimationWidgets(boolean enabled) {
        btnAnimate.setEnabled(enabled);
        btnStepForward.setEnabled(enabled);
        btnStepBackward.setEnabled(enabled);
        animateMenuItem.setEnabled(enabled);
        solutionFrame.setResizable(enabled);
    }
        
    private ActionListener getAnimateListener() {
        if (animateListener == null) {
            animateListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    animate();
                }
            };            
        }
        return animateListener;
    }
    
    // FileChoice objects are displayed in the resultFileChooser
    private class FileChoice {
        private File file;
        
        public FileChoice(File f) {
            file = f;
        }
        
        public File getFile() {
            return file;
        }
        
        public String toString() {
            // display only the short file name
            String name = "";
            if (file != null) {
                name = file.getName();
            }
            return name;
        }
    }
        
}

