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

import zujkomiller.maze.options.*;
import zujkomiller.maze.ui.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.tree.*;
import javax.swing.plaf.*;

public class SolverView extends JPanel {
    
    // menu widgets
    private JMenuBar menuBar;
    private JMenuItem openMenuItem;
    private JMenuItem saveMenuItem;
    private JMenuItem exitToolMenuItem;
    private JMenuItem exitMenuItem;
    
    // options chooser widgets
    private JDialog optionFileChooser;
    private JList optionFileList;
    private File selectedOptionFile;
    private int screenWidth;
    private int screenHeight;
    
    // widgets for setting run options
    private String[] mazeNames;
    private JComboBox cmbxMazeNames = new JComboBox();
    private JTextField txtBonusPoints = new JTextField();
    private JTextField txtMaxChromLength = new JTextField();
    private JPanel previewPanel = new JPanel();
    private JTextField txtNumOfGens = new JTextField();
    private JTextField txtPopSize = new JTextField();
    private JTextField txtNumToReplicate = new JTextField();
    private JTextField txtNumOfTimes = new JTextField();
    private JTextField txtNumOfCrossovers = new JTextField();
    private JTextField txtNumOfMutations = new JTextField();
    private JTextField txOutputFile = new JTextField();
    private JComboBox cmbxResultsOnly = new JComboBox();
    private JTextField txtNth = new JTextField();
    
    // widgets for running the genetic algorithm
    private JButton btnRun = new JButton("Run");
    private JProgressBar progressBar = 
        new JProgressBar(SwingConstants.HORIZONTAL, 0, 100);
    
    public SolverView() {
        this.setupMenuBar();
        this.buildOptionFileChooser();
        this.buildOptionsPanel();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.screenWidth = screenSize.width;
        this.screenHeight = screenSize.height;
    }
    
    private void setupMenuBar() {
        this.menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        
        JMenuItem newMenuItem = new JMenuItem("New Pathfinder Options");
        this.openMenuItem = new JMenuItem("Open Pathfinder Options"); 
        this.saveMenuItem = new JMenuItem("Save"); 
        this.exitToolMenuItem = new JMenuItem("Exit Pathfinder Tool");
        this.exitMenuItem = new JMenuItem("Exit Maze");
        
        newMenuItem.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
                showNewOptionsForm();
           }            
        });
        
        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitToolMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);
        
        menuBar.add(fileMenu);
    }
    
    public JMenuBar getSolverToolMenuBar() {
        return this.menuBar;
    }
    
        
    private void buildOptionFileChooser() {
        this.optionFileChooser = new JDialog(new Frame(),
            "Choose Options", true);
        this.optionFileList = new JList();
        optionFileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(optionFileList);
        
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 2, 2));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        JPanel buttonSizerPanel = new JPanel();
        buttonSizerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonSizerPanel.add(buttonPanel);
        
        JLabel label = new JLabel("Please select a set of options to open.");
        
        JPanel dialogContents = new JPanel();
        dialogContents.setLayout(new BorderLayout());
        dialogContents.add(label, BorderLayout.NORTH);
        dialogContents.add(scrollPane, BorderLayout.CENTER);
        dialogContents.add(buttonSizerPanel, BorderLayout.SOUTH);
        dialogContents.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                
        Container contentPane = this.optionFileChooser.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(dialogContents, BorderLayout.CENTER);
        
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Object selection = optionFileList.getSelectedValue();
                if (selection != null) {
                    FileChoice choice = (FileChoice)selection;
                    selectedOptionFile = choice.getFile();
                } else {
                    selectedOptionFile = null;
                }
                optionFileChooser.setVisible(false);
            }            
        });
         
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                optionFileChooser.setVisible(false);
            }            
        });  
    
        optionFileList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = optionFileList.locationToIndex
                        (new Point(e.getX(), e.getY()));
                    if (index > -1 && optionFileList.isSelectedIndex(index)) {
                        Object selection = optionFileList.getSelectedValue();
                        if (selection != null) {
                            FileChoice choice = (FileChoice)selection;
                            selectedOptionFile = choice.getFile();
                        } else {
                            selectedOptionFile = null;
                        }
                        optionFileChooser.setVisible(false);
                    }
                }
            }
        });
    
        
        optionFileChooser.pack();
        optionFileChooser.setVisible(false);
    }
    
    private void buildOptionsPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Maze", getMazePanel());
        tabbedPane.addTab("Genetic Algorithm", getGenerationsPanel());
        tabbedPane.addTab("Output File", getOutputPanel());     
        
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BorderLayout());
        optionsPanel.add(tabbedPane, BorderLayout.CENTER);
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        
        // the run button
        JPanel buttonSizer = new JPanel();
        buttonSizer.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        buttonSizer.add(this.btnRun);
       
        // the run button with the progress bar
        JPanel runPanel = new JPanel();
        runPanel.setLayout(new BorderLayout());
        runPanel.add(buttonSizer, BorderLayout.WEST);
        runPanel.add(this.progressBar, BorderLayout.CENTER);
        
        this.setLayout(new BorderLayout());
        this.add(optionsPanel, BorderLayout.CENTER);
        this.add(runPanel, BorderLayout.SOUTH);
        this.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }
       
    private JPanel getMazePanel() {
        JPanel mazePanel = new JPanel();
        JLabel lblMaze = new JLabel("1. Maze");
        JLabel lblBonus = new JLabel("2. Bonus Points");
        JLabel lblMaxSteps = new JLabel("3. Maximum Number of Steps");  
        
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new GridLayout(3, 1, 5, 5));
        labelPanel.add(lblMaze);
        labelPanel.add(lblBonus);
        labelPanel.add(lblMaxSteps);
        
        this.txtBonusPoints.setColumns(3);
        this.txtMaxChromLength.setColumns(3);
        Border orig = this.txtBonusPoints.getBorder();
        this.txtBonusPoints.setInputVerifier
            (new IntRangeVerifier(0, 500, orig));
        this.txtMaxChromLength.setInputVerifier
            (new IntRangeVerifier(4, 5000, orig));
        
        JPanel widgetPanel = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        widgetPanel.setLayout(gbl);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbl.setConstraints(this.cmbxMazeNames, gbc);
        widgetPanel.add(this.cmbxMazeNames);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbl.setConstraints(this.txtBonusPoints, gbc);
        widgetPanel.add(this.txtBonusPoints);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbl.setConstraints(this.txtMaxChromLength, gbc);
        widgetPanel.add(this.txtMaxChromLength);
        
        Box widgetsBox = Box.createHorizontalBox();
        widgetsBox.add(labelPanel);
        widgetsBox.add(Box.createHorizontalStrut(10));
        widgetsBox.add(widgetPanel);
        
        JPanel sizerPanel = new JPanel();
        sizerPanel.setLayout(new GridBagLayout());
        sizerPanel.add(widgetsBox);
        sizerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 50, 0));
        
        JPanel instructionsPanel = new JPanel();
        JTextArea txtInstructions = new JTextArea();
        txtInstructions.setColumns(20);
        txtInstructions.setWrapStyleWord(true);
        txtInstructions.setLineWrap(true);
        txtInstructions.setEditable(false);
        txtInstructions.setOpaque(false);
        JScrollPane instructionsScroller = new JScrollPane(txtInstructions);  
        instructionsPanel.setLayout(new BorderLayout());
        instructionsPanel.add(instructionsScroller, BorderLayout.CENTER);
        txtInstructions.setBorder(BorderFactory.createEmptyBorder(25, 20, 25, 20));
        txtInstructions.setText("Step 1.\nSelect a maze to solve." +
            "\n\nStep 2.\nSelect the number of bonus points.  These " +
            "points will be added to the fitness score of any solution " +
            "which reaches the end of the maze.  (The fitness score is " +
            "used to determine which solutions are likely to be reproduced " +
            "in the next generation of solutions.) " +
            "\n\nStep 3.\nSelect the maximum number of steps which a solution " +
            "may take in an attempt to solve the maze.");
        
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(sizerPanel, BorderLayout.SOUTH);
        rightPanel.add(this.previewPanel, BorderLayout.CENTER);
        JScrollPane widgetScroller = new JScrollPane(rightPanel);
        
        mazePanel.setLayout(new BorderLayout());
        mazePanel.add(instructionsScroller, BorderLayout.WEST);
        mazePanel.add(widgetScroller, BorderLayout.CENTER);
        
        return mazePanel;
    }  
        
    private JPanel getGenerationsPanel() {
        JPanel genPanel = new JPanel();
        JLabel lblNumOfGens = new JLabel("4. Number of Generations");
        JLabel lblPopSize = new JLabel("5. Population Size");
        JLabel lblNumToReplicate = 
            new JLabel("6. Number of Top Scorers to Replicate");
        JLabel lblNumOfTimes = 
            new JLabel("7. Number of Times to Replicate Each Top Scorer");
        JLabel lblNumOfCrossovers = new JLabel("8. Number of Crossovers");
        JLabel lblNumOfMutations = new JLabel("9. Number of Mutations");  
        
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new GridLayout(6, 1, 5, 5));
        labelPanel.add(lblNumOfGens);
        labelPanel.add(lblPopSize);
        labelPanel.add(lblNumToReplicate);
        labelPanel.add(lblNumOfTimes);
        labelPanel.add(lblNumOfCrossovers);
        labelPanel.add(lblNumOfMutations);
        
        this.txtNumOfGens.setColumns(3);
        this.txtPopSize.setColumns(3);
        this.txtNumToReplicate.setColumns(3);
        this.txtNumOfTimes.setColumns(3);
        this.txtNumOfCrossovers.setColumns(3);
        this.txtNumOfMutations.setColumns(3);
        
        Border orig = this.txtNumOfGens.getBorder();
        this.txtNumOfGens.setInputVerifier
            (new IntRangeVerifier(1, 5000, orig));
        this.txtPopSize.setInputVerifier
            (new IntRangeVerifier(1, 5000, orig));
        this.txtNumToReplicate.setInputVerifier
            (new IntRangeVerifier(0, 500, orig));
        this.txtNumOfTimes.setInputVerifier
            (new IntRangeVerifier(0, 10, orig));
        this.txtNumOfCrossovers.setInputVerifier
            (new IntRangeVerifier(0, 500, orig));
        this.txtNumOfMutations.setInputVerifier
            (new IntRangeVerifier(0, 5000, orig));
        
        JPanel widgetPanel = new JPanel();
        widgetPanel.setLayout(new GridLayout(6, 1, 5, 5));
        widgetPanel.add(this.txtNumOfGens);
        widgetPanel.add(this.txtPopSize);
        widgetPanel.add(this.txtNumToReplicate);
        widgetPanel.add(this.txtNumOfTimes);
        widgetPanel.add(this.txtNumOfCrossovers);
        widgetPanel.add(this.txtNumOfMutations);
        
        Box widgetsBox = Box.createHorizontalBox();
        widgetsBox.add(labelPanel);
        widgetsBox.add(Box.createHorizontalStrut(10));
        widgetsBox.add(widgetPanel);
        
        JPanel sizerPanel = new JPanel();
        sizerPanel.setLayout(new GridBagLayout());
        sizerPanel.add(widgetsBox);
        sizerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 50, 0));
        
        JPanel instructionsPanel = new JPanel();
        JTextArea txtInstructions = new JTextArea();
        txtInstructions.setColumns(20);
        txtInstructions.setWrapStyleWord(true);
        txtInstructions.setLineWrap(true);
        txtInstructions.setEditable(false);
        txtInstructions.setOpaque(false);
        JScrollPane instructionsScroller = new JScrollPane(txtInstructions);  
        instructionsPanel.setLayout(new BorderLayout());
        instructionsPanel.add(instructionsScroller, BorderLayout.CENTER);
        txtInstructions.setBorder(BorderFactory.createEmptyBorder(25, 20, 25, 20));
        txtInstructions.setText("Step 4.\nEnter the number of generations " +
            "to create. \n\nStep 5.\nEnter the population size.  This determines " +
            "how many individuals exist in each generation.\n\nStep 6.\n" +
            "Select the number of top scorers from one generation to " +
            "replicate in the next generation.\n\nStep7.\n" +
            "Choose the number of times each top scorer will be replicated " +
            "in the next generation.  In Step 6 you selected how many " +
            "top scorers will be reproduced in the next generation. " +
            "In this step, you determine the number of times that each " +
            "of those individuals will be replicated." + 
            "\n\nStep 8.\nTo create one generation from another, the top " +
            "scoring individuals from the parent generation are mated " +
            "with one another, in hopes that the best characteristics of each " +
            "will be present in their children in the next generation. Select " +
            "the number of matings, or crossovers, which should take place. " +
            "\n\nStep 9.\nIn addition to crossovers, the next generation " +
            "differs from the parent generation because random variations " +
            "are introduced.  These are mutations.  Select the number of " +
            "mutations which should occur in each generation."
            
            );
        
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(sizerPanel, BorderLayout.CENTER);
        JScrollPane widgetScroller = new JScrollPane(rightPanel);
        
        genPanel.setLayout(new BorderLayout());
        genPanel.add(instructionsScroller, BorderLayout.WEST);
        genPanel.add(widgetScroller, BorderLayout.CENTER);
        
        return genPanel;
    }   
    
    private JPanel getOutputPanel() {
        JPanel genPanel = new JPanel();
        JLabel lblOutputFile = new JLabel("10. Output File");
        JLabel lblResultsOnly = new JLabel("11. Output Last Generation " +
            "Only?");
        JLabel lblNth = 
            new JLabel("12. Output Every Nth Generation Where N = ");
        
        this.txOutputFile.setColumns(20);
        this.cmbxResultsOnly.addItem("true");
        this.cmbxResultsOnly.addItem("false");
        this.cmbxResultsOnly.setSelectedItem("true");
        this.txtNth.setColumns(3);
        this.txtNth.setInputVerifier
            (new IntRangeVerifier(1, 1000, txtNth.getBorder()));
        this.txtNth.setEnabled(false);
        
        // when the user selects false - do not output only
        // the results, then enable the txtNth text field
        // which allows the user to specify which generations
        // to output in addition to the final results
        this.cmbxResultsOnly.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
                String text = (String)cmbxResultsOnly.getSelectedItem();
                if (text.equalsIgnoreCase("false")) {
                    txtNth.setEnabled(true);
                } else {
                    txtNth.setEnabled(false);
                }
           }
        });
        
        JButton btnBrowse = new JButton("Browse");
        btnBrowse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.setDialogTitle("Output File");
                int returnVal = chooser.showOpenDialog(txOutputFile);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File f = chooser.getSelectedFile();
                    txOutputFile.setText(f.toString());
                }
            }
        });
                   
        Box outputFileBox = Box.createHorizontalBox();
        outputFileBox.add(lblOutputFile);
        outputFileBox.add(Box.createHorizontalStrut(5));
        outputFileBox.add(this.txOutputFile);
        outputFileBox.add(Box.createHorizontalStrut(5));
        outputFileBox.add(btnBrowse);
        
        JPanel widgetPanel = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        widgetPanel.setLayout(gbl);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbl.setConstraints(outputFileBox, gbc);
        widgetPanel.add(outputFileBox);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbl.setConstraints(this.cmbxResultsOnly, gbc);
        widgetPanel.add(this.cmbxResultsOnly);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbl.setConstraints(this.txtNth, gbc);
        widgetPanel.add(this.txtNth);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbl.setConstraints(lblResultsOnly, gbc);
        widgetPanel.add(lblResultsOnly);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbl.setConstraints(lblNth, gbc);
        widgetPanel.add(lblNth);       
                
        JPanel sizerPanel = new JPanel();
        sizerPanel.setLayout(new GridBagLayout());
        sizerPanel.add(widgetPanel);
        sizerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 50, 0));
        
        JPanel instructionsPanel = new JPanel();
        JTextArea txtInstructions = new JTextArea();
        txtInstructions.setColumns(20);
        txtInstructions.setWrapStyleWord(true);
        txtInstructions.setLineWrap(true);
        txtInstructions.setEditable(false);
        txtInstructions.setOpaque(false);
        JScrollPane instructionsScroller = new JScrollPane(txtInstructions);  
        instructionsPanel.setLayout(new BorderLayout());
        instructionsPanel.add(instructionsScroller, BorderLayout.CENTER);
        txtInstructions.setBorder(BorderFactory.createEmptyBorder(25, 20, 25, 20));
        txtInstructions.setText("Step 10.\nThe results of the genetic " +
            "algorithm will be stored in a text file.  Enter the path " +
            "to that file here.\n\nStep 11.\n" +
            "If you would like to see only the final generation printed " +
            "to the output file, select true.  If you select false, then " +
            "in step 12 you can specify which generations will be printed " +
            "to the output file.\n\nStep 12.\n" +
            "If you selected false in Step 11, then the output file will " +
            "contain more generations than just the final generation produced. " +
            "Here you can determine which generations will be included in " +
            "the output file.");
        
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(sizerPanel, BorderLayout.CENTER);
        JScrollPane widgetScroller = new JScrollPane(rightPanel);
        
        genPanel.setLayout(new BorderLayout());
        genPanel.add(instructionsScroller, BorderLayout.WEST);
        genPanel.add(widgetScroller, BorderLayout.CENTER);
        
        return genPanel;
    }
            
    public void previewMaze(JPanel mazePanel) {
        this.previewPanel.removeAll();
        previewPanel.setLayout(new GridBagLayout());
        previewPanel.add(mazePanel);
        this.validateTree();
        this.doLayout();
    }
    
    private void updateMazeNamesBox() {
        this.cmbxMazeNames.removeAllItems();
        for (int n=0; n<this.mazeNames.length; n++) {
            cmbxMazeNames.addItem(mazeNames[n]);
        }
    }
                     
    public void setExitToolActionListener(ActionListener listener) {
        this.exitToolMenuItem.addActionListener(listener);
    }
             
    public void setExitActionListener(ActionListener listener) {
        this.exitMenuItem.addActionListener(listener);
    }   
    
    public void setSaveActionListener(ActionListener listener) {
        this.exitMenuItem.addActionListener(listener);
    }   
    
    public void setMazeNames(String[] names) {
        this.mazeNames = names;
        updateMazeNamesBox();
    }
    
    public void setPreviewActionListener(ActionListener listener) {
        this.cmbxMazeNames.addActionListener(listener);
    }   
    
    public void setOpenActionListener(ActionListener listener) {
        this.openMenuItem.addActionListener(listener);
    }   
    
    private void showNewOptionsForm() {
        System.out.println("showing new options");
        
    }    
    
    public String getSelectedMaze() {
        return (String)(this.cmbxMazeNames.getSelectedItem());
    }
    
    public void setSelectedMaze(String mazeName) {
        try {
            this.cmbxMazeNames.setSelectedItem(mazeName);
        } catch (Exception ex) {
            // do nothing
        }
    }
    
    public int getBonusPoints() {
        int points = 100; // default
        try {
            String bp = this.txtBonusPoints.getText();
            points = Integer.parseInt(bp);
        } catch (NumberFormatException nfe) {
            // do nothing; use default
        }
        return points;
    }
    
    public void setBonusPoints(int points) {
        this.txtBonusPoints.setText("" + points);        
    }
    
    public int getMaxChromLength() {
        int length = 50; // default
        try {
            String l = this.txtMaxChromLength.getText();
            length = Integer.parseInt(l);
        } catch (NumberFormatException nfe) {
            // do nothing; use default
        }
        return length;
    }
    
    public void setMaxChromLength(int length) {
        this.txtMaxChromLength.setText("" + length);
    }
    
    public void displayOptions(RunOptions options) {
        this.setSelectedMaze(options.getMazeName());
        this.setBonusPoints(options.getBonusPoints());
        this.setMaxChromLength(options.getMaxChromLength());
    }
    
    public File getOpenFileName(File[] filesToChooseFrom) {
        FileChoice[] choices = new FileChoice[filesToChooseFrom.length];        
        File f = null;
        if (filesToChooseFrom != null) {
            for (int n=0; n<filesToChooseFrom.length; n++) {
                choices[n] = new FileChoice(filesToChooseFrom[n]);
            }
        }   
        showOptionFileChooser(choices);
        return this.selectedOptionFile;
    }
    
    private void showOptionFileChooser(FileChoice[] choices) {
        optionFileList.setListData(choices);
        optionFileChooser.setLocationRelativeTo(this);
        Dimension dialogDim = optionFileChooser.getPreferredSize();
        optionFileChooser.setLocation((screenWidth/2) - (dialogDim.width/2),
            (screenHeight/2) - (dialogDim.height/2));
        optionFileChooser.pack();
        optionFileChooser.setVisible(true);
    }
    
    // FileChoice objects are displayed in the optionFileChooser
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