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

package zujkomiller.maze.ui.util;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;

public class IntRangeVerifier extends InputVerifier {
    
    protected int minValue = 0;
    protected int maxValue = 0;
    protected Border errorBorder = BorderFactory.createLineBorder(Color.red);
    protected Border originalBorder = null;
    
    public IntRangeVerifier(int min, int max, Border originalBorder) {
        this.minValue = min;
        this.maxValue = max;
        this.originalBorder = originalBorder;
    }
    
    public boolean verify(JComponent input) {
        boolean isValid = false;
        if (input instanceof JTextComponent) {
            String text = ((JTextComponent)input).getText();
            try {
                int i = Integer.parseInt(text.trim());
                if (i >= minValue && i <= maxValue) {
                    isValid = true;
                }
            } catch (NumberFormatException nfe) {
                // do nothing; will return false
            }
        }
        return isValid;
    }

    public boolean shouldYieldFocus(JComponent input) {
        boolean isValid = verify(input);
        if (!isValid) {
            input.setBorder(this.errorBorder);
            JOptionPane.showMessageDialog(input, 
                "Please enter a value between " + this.minValue + 
                    " and " + this.maxValue, "ERROR", 
                        JOptionPane.ERROR_MESSAGE);
        } else {
            input.setBorder(this.originalBorder);
            input.repaint();
        }
        return isValid;
    }
    
}