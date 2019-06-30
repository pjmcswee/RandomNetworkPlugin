/*  
File: GeometricNetworkPanel.java
Author: Patrick J. McSweeney (pjmcswee@syr.edu)
Copyright (c) 2006, 2007, The Cytoscape Consortium (www.cytoscape.org)

The Cytoscape Consortium is:
- Institute for Systems Biology
- University of California San Diego
- Memorial Sloan-Kettering Cancer Center
- Institut Pasteur
- Agilent Technologies

This library is free software; you can redistribute it and/or modify it
under the terms of the GNU Lesser General Public License as published
by the Free Software Foundation; either version 2.1 of the License, or
any later version.

This library is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF
MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.  The software and
documentation provided hereunder is on an "as is" basis, and the
Institute for Systems Biology and the Whitehead Institute
have no obligations to provide maintenance, support,
updates, enhancements or modifications.  In no event shall the
Institute for Systems Biology and the Whitehead Institute
be liable to any party for direct, indirect, special,
incidental or consequential damages, including lost profits, arising
out of the use of this software and its documentation, even if the
Institute for Systems Biology and the Whitehead Institute
have been advised of the possibility of such damage.  See
the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with this library; if not, write to the Free Software Foundation,
Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 */
package org.cytoscape.randomnetwork.gui;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.randomnetwork.*;
import org.cytoscape.randomnetwork.generators.KroneckerModel;

import java.awt.*;
import javax.swing.table.*;
import java.util.*;
import javax.swing.*;

/**
 * This class is responsible for handling the user interface
 * to generate a random model according to watts-strogatz model
 * 
 */
public class KroneckerPanel extends RandomNetworkPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Default values
    private static final int defaultseedMatrixDimension = 2;
    private static final double[][] defaultseedMatrix = {{.9, .5}, {.5, .1}};
    private static final int defaultIterValue = 7;
    //Global Variables
    private int seedMatrixDimension;
    private double[][] values;
    //Table model class
    private TableModel dataModel = new AbstractTableModel() {

        public int getColumnCount() {
            return seedMatrixDimension;
        }

        public int getRowCount() {
            return seedMatrixDimension;
        }

        public boolean isCellEditable(int row, int col) {
            return true;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            return new Double(values[rowIndex][columnIndex]);
        }

        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            try {
                double d = Double.parseDouble((String) aValue);
                if ((d > 1) || (d < 0)) {
                    throw new Exception("Nodes must be a probability!");
                } else {
                    values[rowIndex][columnIndex] = d;
                }
            } catch (Exception e) {
                e.printStackTrace();
                //If an error occurs than change the colors
                seedLabel.setForeground(java.awt.Color.RED);
            }

        }
    };
    int mode;
    //Table variables
    private JTable table;
    private JScrollPane scrollPane;
    //TextFields
    private javax.swing.JTextField iterTextField;
    private javax.swing.JTextField dimensionTextField;
    //Buttons
    private javax.swing.JCheckBox directedCheckBox;
    private javax.swing.JCheckBox selfEdgeCheckBox;
    private javax.swing.JButton resizeButton;
    private javax.swing.JButton randomFillButton;
    //Labels
    private javax.swing.JLabel iterLabel;
    private javax.swing.JLabel seedLabel;
    private javax.swing.JLabel dimensionLabel;

    /*
     * Default constructor
     */
    public KroneckerPanel(int pMode) {
        super(null);
        mode = pMode;
        initComponents();
    }

    /*
     * Default constructor
     */
    public KroneckerPanel(int pMode, RandomNetworkPanel pPrevious) {
        super(pPrevious);
        mode = pMode;
        initComponents();
    }

    /**
     *
     */
    public String getNextText() {
        if (mode == 0) {
            return new String("Generate");
        } else {
            return new String("Next");
        }

    }

    public void randomFill() {
        Random rand = new Random();
        for (int i = 0; i < seedMatrixDimension; i++) {
            for (int j = 0; j < seedMatrixDimension; j++) {
                values[i][j] = rand.nextDouble();
            }
        }
        table.doLayout();
        remove(scrollPane);
        table = new JTable(dataModel);
        table.getTableHeader().setReorderingAllowed(false);
        table.setGridColor(java.awt.Color.black);
        scrollPane = new JScrollPane(table);
        table.setPreferredScrollableViewportSize(new Dimension(150, 50));
        scrollPane.setPreferredSize(new Dimension(150, 50));
        scrollPane.setMinimumSize(new Dimension(150, 50));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.LINE_START;
        add(scrollPane, c);
        validate();


    }

    /**
     *
     */
    public String getTitle() {
        return new String("Kronecker Model");
    }

    /**
     *
     */
    public String getDescription() {
        return new String("The Kronecker ...");
    }

    /**
     *
     */
    private void resizeAction(int dim) {
        System.out.println(dim);
        seedMatrixDimension = dim;
        double[][] temp = new double[dim][dim];
        for (int i = 0; i < Math.min(dim, values.length); i++) {
            for (int j = 0; j < Math.min(dim, values.length); j++) {
                temp[i][j] = values[i][j];
            }
        }
        values = temp;

//		table.resizeAndRepaint();
//		table.fireTableModelChanged();

        remove(scrollPane);
        table = new JTable(dataModel);
        table.getTableHeader().setReorderingAllowed(false);
        table.setGridColor(java.awt.Color.black);
        scrollPane = new JScrollPane(table);
        table.setPreferredScrollableViewportSize(new Dimension(150, 50));
        scrollPane.setPreferredSize(new Dimension(150, 50));
        scrollPane.setMinimumSize(new Dimension(150, 50));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.LINE_START;
        add(scrollPane, c);
        validate();
    }

    /*
     * Initialize the components
     */
    private void initComponents() {
        seedMatrixDimension = defaultseedMatrixDimension;
        values = new double[seedMatrixDimension][seedMatrixDimension];

        //Instantiate values for the seed matrix
        for (int i = 0; i < seedMatrixDimension; i++) {
            for (int j = 0; j < seedMatrixDimension; j++) {
                values[i][j] = defaultseedMatrix[i][j];
            }
        }


        //Create the TextFields
        iterTextField = new javax.swing.JTextField();
        iterTextField.setPreferredSize(new Dimension(50, 25));
        iterTextField.setMinimumSize(new Dimension(50, 25));
        iterTextField.setText("" + defaultIterValue);
        iterTextField.setHorizontalAlignment(JTextField.RIGHT);


        dimensionTextField = new javax.swing.JTextField();
        dimensionTextField.setPreferredSize(new Dimension(50, 25));
        dimensionTextField.setMinimumSize(new Dimension(50, 25));
        dimensionTextField.setText("" + seedMatrixDimension);
        dimensionTextField.setHorizontalAlignment(JTextField.RIGHT);

        table = new JTable(dataModel);

        randomFillButton = new javax.swing.JButton();

        randomFillButton = new javax.swing.JButton();
        randomFillButton.setText("Random Fill");
        randomFillButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                randomFill();
            }
        });



        resizeButton = new javax.swing.JButton();
        resizeButton.setText("Resize");
        resizeButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    Integer d = Integer.parseInt(dimensionTextField.getText());
                    if ((d < 1) || (d > 100)) {
                        throw new Exception("Dimension too big! > 100");
                    } else {
                        resizeAction(d);
                    }
                } catch (Exception e) {
                }
            }
        });


        //Create the buttons
        directedCheckBox = new javax.swing.JCheckBox();
        selfEdgeCheckBox = new javax.swing.JCheckBox();

        //Create the Labels
        iterLabel = new javax.swing.JLabel();
        seedLabel = new javax.swing.JLabel();
        dimensionLabel = new javax.swing.JLabel();

        //Set the text on the labels
        seedLabel.setText("Seed Matrix:");
        iterLabel.setText("# Iterations:");
        dimensionLabel.setText("Matrix Dimensions:");
        selfEdgeCheckBox.setText("Allow reflexive Edges (u,u)");
        directedCheckBox.setText("Undirected");

        selfEdgeCheckBox.setEnabled(true);
        directedCheckBox.setEnabled(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.setGridColor(java.awt.Color.black);
        scrollPane = new JScrollPane(table);
        table.setPreferredScrollableViewportSize(new Dimension(150, 50));
        scrollPane.setPreferredSize(new Dimension(150, 50));
        scrollPane.setMinimumSize(new Dimension(150, 50));
        setLayout(new GridBagLayout());

        //Setup the titel
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        add(seedLabel, c);

        c = null;
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.LINE_START;
        add(scrollPane, c);


        c = null;
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.LINE_START;
        add(dimensionLabel, c);

        c = null;
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.LINE_START;
        add(dimensionTextField, c);


        c = null;
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.LINE_START;
        add(resizeButton, c);

        c = null;
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.LINE_START;
        add(randomFillButton, c);




        c = null;
        c = new GridBagConstraints();
//		c.weightx = 1;
//		c.weighty = 1;
        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 5;
        c.anchor = GridBagConstraints.LINE_START;
        add(iterLabel, c);

        c = null;
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 5;
        c.weightx = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.LINE_START;
        add(iterTextField, c);


        //add the line
        c = null;
        c = new GridBagConstraints();
        c.insets = new Insets(0, 10, 0, 10);
        //c.anchor = GridBagConstraints.CENTER;
        c.gridx = 3;
        c.gridheight = 5;
        c.gridy = 0;
        c.fill = GridBagConstraints.VERTICAL;
        add(new JSeparator(SwingConstants.VERTICAL), c);


        c = null;
        c = new GridBagConstraints();
        c.insets = new Insets(0, 10, 0, 0);
        c.gridx = 4;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.LINE_START;
        add(directedCheckBox, c);

        c = null;
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(0, 10, 0, 0);
        c.gridx = 4;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        add(selfEdgeCheckBox, c);
    }

    /*
     *  Callback for the generate button
     */
    public RandomNetworkPanel next() {
        //The iter of each node
        int iter;

        //Whether or not the network is directed
        boolean directed;

        //Whether or not the network allows reflexive edge
        boolean allowSelfEdge;

        //Get the strings from the textfields
        String iterString = iterTextField.getText();

        //Try to read this string into an integer
        try {
            iter = Integer.parseInt(iterString);
            if (iter < 1) {
                throw new Exception("Iterations must be positive.");
            }

        } catch (Exception e) {
            //If an error occurs than change the colors
            iterTextField.grabFocus();
            iterLabel.setForeground(java.awt.Color.RED);
            return this;
        }


        //If we got this far reset all to black
        iterLabel.setForeground(java.awt.Color.BLACK);


        //Get the directed/undirected from the checkbox
        directed = false;
        if (directedCheckBox.isSelected()) {
            directed = true;
        }

        //Get the boolean for set
        allowSelfEdge = false;
        if (selfEdgeCheckBox.isSelected()) {
            allowSelfEdge = true;
        }


        //Create the model
        KroneckerModel km = new KroneckerModel(allowSelfEdge, !directed, values, iter);

        if (mode == 1) {
            km.setCreateView(false);

            if (mNext == null) {
                mNext = new AnalyzePanel(this, km, km.getDirected());
            } else {
                ((AnalyzePanel) mNext).setDirected(km.getDirected());
                ((AnalyzePanel) mNext).setGenerator(km);
            }
            return mNext;
        }

        //Generate the random network
        RandomNetworkImpl network = km.generate();
        CyNetwork randomNet = ConverterUtils.toCyNetwork(network);

        //Go up to the Dialog and close this window
        return this;
    }
}