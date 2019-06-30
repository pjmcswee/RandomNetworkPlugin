/*  
File: GeometricNetworkPanel.java
Penrose, Mathew: Random Geometric Graphs (Oxford Studies in Probability, 5), 2003.

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

import java.awt.*;
import javax.swing.*;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.randomnetwork.ConverterUtils;
import org.cytoscape.randomnetwork.RandomNetworkImpl;
import org.cytoscape.randomnetwork.generators.GeometricNetworkModel;

/**
 * This class is responsible for handling the user interface
 * to generate a random model according to watts-strogatz model
 * 
 */
public class GeometricNetworkPanel extends RandomNetworkPanel {

    private static final int defaultNodeValue = 100;
    private static final double defaultRadiusValue = .5;
    private static final int defaultDimensionValue = 3;
    int mode;
    //TextFields
    private javax.swing.JTextField nodeTextField;
    private javax.swing.JTextField radiusTextField;
    private javax.swing.JTextField dimensionTextField;
    //Buttons
    private javax.swing.JCheckBox directedCheckBox;
    private javax.swing.JCheckBox selfEdgeCheckBox;
    //Labels
    private javax.swing.JLabel nodeLabel;
    private javax.swing.JLabel radiusLabel;
    private javax.swing.JLabel dimensionLabel;

    /*
     * Default constructor
     */
    public GeometricNetworkPanel(int pMode) {
        super(null);
        mode = pMode;
        initComponents();
    }

    /*
     * Default constructor
     */
    public GeometricNetworkPanel(int pMode, RandomNetworkPanel pPrevious) {
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

    /**
     *
     */
    public String getTitle() {
        return new String("Geometric Network Model");
    }

    /**
     *
     */
    public String getDescription() {
        return new String("The Geometric Network Model uniformly & randomly places nodes in the unit square, cube, etc, depending upon the dimensionality specified.  An undirected edge is drawn between two nodes if the euclidean distance betwen the points is less than the specified radius.");
    }

    /*
     * Initialize the components
     */
    private void initComponents() {

        //Create the TextFields
        nodeTextField = new javax.swing.JTextField();
        radiusTextField = new javax.swing.JTextField();
        dimensionTextField = new javax.swing.JTextField();
        nodeTextField.setPreferredSize(new Dimension(50, 25));
        radiusTextField.setPreferredSize(new Dimension(50, 25));
        dimensionTextField.setPreferredSize(new Dimension(50, 25));
        nodeTextField.setText("" + defaultNodeValue);
        radiusTextField.setText("" + defaultRadiusValue);
        dimensionTextField.setText("" + defaultDimensionValue);
        nodeTextField.setHorizontalAlignment(JTextField.RIGHT);
        radiusTextField.setHorizontalAlignment(JTextField.RIGHT);
        dimensionTextField.setHorizontalAlignment(JTextField.RIGHT);


        //Create the buttons
        directedCheckBox = new javax.swing.JCheckBox();
        selfEdgeCheckBox = new javax.swing.JCheckBox();

        //Create the Labels
        nodeLabel = new javax.swing.JLabel();
        dimensionLabel = new javax.swing.JLabel();
        radiusLabel = new javax.swing.JLabel();


        //Set the text on the labels
        nodeLabel.setText("Number of Nodes:");
        radiusLabel.setText("Radius:");
        dimensionLabel.setText("Dimension:");
        selfEdgeCheckBox.setText("Allow reflexive Edges (u,u)");
        directedCheckBox.setText("Undirected");


        directedCheckBox.setSelected(true);
        selfEdgeCheckBox.setEnabled(false);
        directedCheckBox.setEnabled(true);

        setLayout(new GridBagLayout());

        //Setup the titel
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 10, 0, 0);
        c.gridx = 0;
        c.gridy = 0;
//		c.weightx = 1;
//		c.weighty = 1;
        c.weightx = 1;
        c.weighty = 1;

        c.anchor = GridBagConstraints.LINE_START;
        add(nodeLabel, c);

        c = null;
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
//		c.weightx = 1;
//		c.weighty = 1;
        c.weightx = 1;
        c.weighty = 1;

//		c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.LINE_START;
        add(nodeTextField, c);


        c = null;
        c = new GridBagConstraints();
        c.insets = new Insets(0, 10, 0, 0);
        c.gridx = 0;
        c.gridy = 1;
//		c.weightx = 1;
//		c.weighty = 1;
        c.weightx = 1;
        c.weighty = 1;

        c.anchor = GridBagConstraints.LINE_START;
        add(radiusLabel, c);

        c = null;
        c = new GridBagConstraints();
//		c.weightx = 1;
//		c.weighty = 1;
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;

        c.anchor = GridBagConstraints.LINE_START;
        add(radiusTextField, c);

        c = null;
        c = new GridBagConstraints();
//		c.weightx = 1;
//		c.weighty = 1;
        c.insets = new Insets(0, 10, 0, 0);
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 1;
        c.weighty = 1;

        c.anchor = GridBagConstraints.LINE_START;
        add(dimensionLabel, c);

        c = null;
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.LINE_START;
        add(dimensionTextField, c);

/*
        //add the line
        c = null;
        c = new GridBagConstraints();
        c.insets = new Insets(0, 10, 0, 10);
        //c.anchor = GridBagConstraints.CENTER;
        c.gridx = 2;
        c.gridheight = 5;
        c.gridy = 0;
        c.fill = GridBagConstraints.VERTICAL;
        //c.weightx = 1;
        //c.weighty = 1;
        add(new JSeparator(SwingConstants.VERTICAL), c);
*/

        c = null;
        c = new GridBagConstraints();
        c.insets = new Insets(0, 10, 0, 0);
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.LINE_START;
        add(directedCheckBox, c);
        /*
        c = null;
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(0,10,0,0);
        c.gridx = 3;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        add(selfEdgeCheckBox,c);

         */


    }

    /*
     *  Callback for the generate button
     */
    public RandomNetworkPanel next() {
        //The dimension of each node
        int dimension;

        //How much to interpolate between a lattice
        //and erdos-renyi model
        double radius;

        //The number of nodes in the network
        int numNodes;

        //Whether or not the network is directed
        boolean directed;

        //Whether or not the network allows reflexive edge
        boolean allowSelfEdge;



        //Get the strings from the textfields
        String numNodeString = nodeTextField.getText();
        String radiusString = radiusTextField.getText();
        String dimensionString = dimensionTextField.getText();

        //Try to read the string into an integer
        try {
            numNodes = Integer.parseInt(numNodeString);

            if (numNodes < 3) {
                throw new Exception("Nodes must be > 2");
            }

        } catch (Exception e) {
            //If an error occurs than change the colors
            nodeTextField.grabFocus();
            dimensionLabel.setForeground(java.awt.Color.BLACK);
            nodeLabel.setForeground(java.awt.Color.RED);
            radiusLabel.setForeground(java.awt.Color.BLACK);
            return this;
        }

        //Try to read string into an double
        try {
            radius = Double.parseDouble(radiusString);

            //Check to make sure radius is a probability
            if ((radius < 0) || (radius > 1)) {
                throw (new Exception("Beta must be a probability"));
            }
        } catch (Exception e) {
            //If an error occurs than change the colors
            radiusTextField.grabFocus();
            dimensionLabel.setForeground(java.awt.Color.BLACK);
            nodeLabel.setForeground(java.awt.Color.BLACK);
            radiusLabel.setForeground(java.awt.Color.RED);
            return this;
        }

        //Try to read this string into an integer
        try {
            dimension = Integer.parseInt(dimensionString);
            if (dimension < 1) {
                throw new Exception("Degree must be positive.");
            }

        } catch (Exception e) {
            //If an error occurs than change the colors
            dimensionTextField.grabFocus();
            dimensionLabel.setForeground(java.awt.Color.RED);
            nodeLabel.setForeground(java.awt.Color.BLACK);
            radiusLabel.setForeground(java.awt.Color.BLACK);
            return this;
        }


        //If we got this far reset all to black
        dimensionLabel.setForeground(java.awt.Color.BLACK);
        nodeLabel.setForeground(java.awt.Color.BLACK);
        radiusLabel.setForeground(java.awt.Color.BLACK);


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
        GeometricNetworkModel gnm = new GeometricNetworkModel(numNodes,
                allowSelfEdge, !directed, radius, dimension);




        if (mode == 1) {

            gnm.setCreateView(false);

            if (mNext == null) {
                mNext = new AnalyzePanel(this, gnm, gnm.getDirected());
            } else {
                ((AnalyzePanel) mNext).setDirected(gnm.getDirected());
                ((AnalyzePanel) mNext).setGenerator(gnm);
            }

            return mNext;
        }


        //Generate the random network
        RandomNetworkImpl network = gnm.generate();
        CyNetwork randomNet = ConverterUtils.toCyNetwork(network);


        //Go up to the Dialog and close this window
        return this;

    }
}
