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

package zujkomiller.maze.options;

import zujkomiller.maze.persistence.*;
import java.io.*;

public class DefaultOptionsWriter {
    
    public DefaultOptionsWriter() {
        createOptions();
    }
    
    protected void createOptions() {
        RunOptions ops = new RunOptions();
        try {
            ops.setBonusPoints(100);
            ops.setCrossoversPerGen(6);
            ops.setMaxChromLength(50);
            ops.setMazeName("difficult");
            ops.setMutationsPerGen(50);
            ops.setNumOfGenerations(50);
            ops.setNumOfTimes(2);
            ops.setNumToReplicate(4);
            ops.setOutputFile("c:/temp/difficultOutput.txt");
            ops.setOutputGenerations(5);
            ops.setOutputResultOnly(false);
            ops.setPopulationSize(20);
            MazeAccessor.store(ops, "default");
        } catch (Exception e) {
            e.printStackTrace();
        }        
    }
    
    public static void main(String[] args) {
        new DefaultOptionsWriter();        
    }
    
    
}