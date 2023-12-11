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

package zujkomiller.maze.persistence;

import zujkomiller.maze.results.MazeEvolutionData;
import zujkomiller.maze.model.Maze;
import zujkomiller.maze.options.RunOptions;
import java.io.*;
import java.net.URL;
import java.text.DateFormat;
import java.util.*;

public abstract class MazeAccessor {

    private static final String PROP_FILE = "maze/bin/MazeGenetic.properties";
    private static final String MAZE_FILE_EXT = ".maze";
    private static final String RESULT_FILE_EXT = ".result";
    private static final String OPT_FILE_EXT = ".opt";
    
    private static String directory;

    static {
        try {
            // the storage directory as always relative
            // to the MazeGenetic.properties file
            URL url = ClassLoader.getSystemResource(PROP_FILE);
            File propFile = new File(url.getFile()); 
            String parent = propFile.getParentFile().getParent();
            String separator = File.separator;
            directory = parent + separator +"storage" + separator;
            
            // now make sure the directory exists
            // if it doesn't, then create it
            File storageDir = new File(directory);
            if (!storageDir.exists()) {
                storageDir.mkdir();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void store(Maze maze, String name) throws IOException {
        maze.setName(name);
        FileOutputStream fileOut =
            new FileOutputStream(directory + name + MAZE_FILE_EXT);
        ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
        objOut.writeObject(maze);
        objOut.flush();
        objOut.close();
        fileOut.close();
    }

    public static Maze load(String name) {
        Maze theMaze = null;
        FileInputStream fileIn = null;
        ObjectInputStream objIn = null;
        try {
            fileIn = new FileInputStream(directory + name + MAZE_FILE_EXT);
            objIn = new ObjectInputStream(fileIn);
            theMaze = (Maze)objIn.readObject();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            // do nothing - will return a null Maze
        } catch (ClassCastException cce) {
            cce.printStackTrace();
            // do nothing - will return a null Maze
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            // do nothing - will return a null Maze
        } finally {
            try {
                if (objIn != null) {
                    objIn.close();
                }
                if (fileIn != null) {
                    fileIn.close();
                }
            } catch (IOException ioe) {
                // do nothing
            }
        }
        return theMaze;
    }
    
    public static String[] getMazeNames() {
        File[] files = new File[0];
        try {
            File dir = new File(directory);           
            files = dir.listFiles(new FileFilter() {
                public boolean accept(File testFile) {
                    boolean ok = false;
                    if (testFile != null &&
                            testFile.getName().endsWith(MAZE_FILE_EXT)) {
                        ok = true;
                    }
                    return ok;
                }
            });
        } catch (Exception ex) {
            // do nothing, will return an empty array
        }
        if (files == null) {
            files = new File[0];
        }
        String[] names = new String[files.length];
        String fileName = "";
        int indexOfExt = -1;
        for (int n=0; n<names.length; n++) {
            fileName = files[n].getName();
            indexOfExt = fileName.indexOf(MAZE_FILE_EXT);
            names[n] = fileName.substring(0, indexOfExt);
        }
        return names;
    }

    public static void store(MazeEvolutionData data) throws IOException {
        FileOutputStream fileOut = null;
        ObjectOutputStream objOut = null;
        try {
            DateFormat formatter = DateFormat.
                getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
            String time = formatter.format(new Date());
            time = time.replace(' ', '_');
            time = time.replace('/', '_');
            time = time.replace(':', '_');
            String name = data.getMaze().getName() + "_" + time;
            fileOut = new FileOutputStream(directory + name + RESULT_FILE_EXT);
            objOut = new ObjectOutputStream(fileOut);
            objOut.writeObject(data);
            objOut.flush();
        } finally {
            if (objOut != null) {
                objOut.close();
            }
            if (fileOut != null) {
                fileOut.close();
            }
        }
    }

    public static MazeEvolutionData loadResults(String name) {
        return loadResults(new File(name));
    }
    
    public static MazeEvolutionData loadResults(File resultsFile) {
        MazeEvolutionData data = null;
        FileInputStream fileIn = null;
        ObjectInputStream objIn = null;
        try {
            fileIn = new FileInputStream(resultsFile);
            objIn = new ObjectInputStream(fileIn);
            data = (MazeEvolutionData)objIn.readObject();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            // do nothing - will return a null Maze
        } catch (ClassCastException cce) {
            cce.printStackTrace();
            // do nothing - will return a null Maze
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            // do nothing - will return a null Maze
        } finally {
            try {
                if (objIn != null) {
                    objIn.close();
                }
                if (fileIn != null) {
                    fileIn.close();
                }
            } catch (IOException ioe) {
                // do nothing
            }
        }
        return data;
    }
    
    public static File[] getResultsFileNames() {
        File[] files = new File[0];
        try {
            File dir = new File(directory);           
            files = dir.listFiles(new FileFilter() {
                public boolean accept(File testFile) {
                    boolean ok = false;
                    if (testFile != null &&
                            testFile.getName().endsWith(RESULT_FILE_EXT)) {
                        ok = true;
                    }
                    return ok;
                }
            });
        } catch (Exception ex) {
            // do nothing, will return an empty array
        }
        if (files == null) {
            files = new File[0];
        }
        return files;
    }
    
    
    public static void store(RunOptions options, String name) throws IOException {
        options.setName(name);
        FileOutputStream fileOut =
            new FileOutputStream(directory + name + OPT_FILE_EXT);
        ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
        objOut.writeObject(options);
        objOut.flush();
        objOut.close();
        fileOut.close();
    }
    
    public static RunOptions loadOptions(String fileName) {
        return loadOptions(new File(fileName));
    }            

    public static RunOptions loadOptions(File resultsFile) {
        RunOptions options = null;
        FileInputStream fileIn = null;
        ObjectInputStream objIn = null;
        try {
            fileIn = new FileInputStream(resultsFile);
            objIn = new ObjectInputStream(fileIn);
            options = (RunOptions)objIn.readObject();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            // do nothing - will return a null Maze
        } catch (ClassCastException cce) {
            cce.printStackTrace();
            // do nothing - will return a null Maze
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            // do nothing - will return a null Maze
        } finally {
            try {
                if (objIn != null) {
                    objIn.close();
                }
                if (fileIn != null) {
                    fileIn.close();
                }
            } catch (IOException ioe) {
                // do nothing
            }
        }
        return options;
    }
    
    public static File[] getOptionsFileNames() {
        File[] files = new File[0];
        try {
            File dir = new File(directory);           
            files = dir.listFiles(new FileFilter() {
                public boolean accept(File testFile) {
                    boolean ok = false;
                    if (testFile != null &&
                            testFile.getName().endsWith(OPT_FILE_EXT)) {
                        ok = true;
                    }
                    return ok;
                }
            });
        } catch (Exception ex) {
            // do nothing, will return an empty array
        }
        if (files == null) {
            files = new File[0];
        }
        return files;
    }
}