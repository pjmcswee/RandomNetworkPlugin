/*  File: WattsStrogatzPanel.java
Copyright (c) 2006, 2007, The Cytoscape Consortium (www.cytoscape.org)

Jure Leskovec, Jon Kleinberg and Christos Faloutsos. Graphs over time: densification laws, shrinking diameters and possible explanations. KDD '05: Proceeding of the eleventh ACM SIGKDD international conference on Knowledge discovery in data mining, 177–187, 2005.

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
import java.awt.event.*;
import javax.swing.*;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.randomnetwork.ConverterUtils;
import org.cytoscape.randomnetwork.RandomNetworkImpl;
import org.cytoscape.randomnetwork.generators.ForestFiresModel;

/**
 * This class is responsible for handling the user interface
 * to generate a random model according to watts-strogatz model
 * 
 */
public class ForestFiresPanel extends RandomNetworkPanel implements ItemListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int defaultNodeValue = 100;
    private static final double defaultForwardValue = 0.1;
    private static final double defaultBackwardValue = 0.1;
    private static final int defaultAmbassadorValue = 1;
    private static final double defaultOrphanValue = .001;
    int mode;
    //TextFields
    private javax.swing.JTextField nodeTextField;
    private javax.swing.JTextField forwardTextField;
    private javax.swing.JTextField backwardTextField;
    private javax.swing.JTextField ambassadorTextField;
    private javax.swing.JTextField orphanTextField;
    //Buttons
    private javax.swing.JCheckBox directedCheckBox;
    private javax.swing.JCheckBox selfEdgeCheckBox;
    //Labels
    private javax.swing.JLabel nodeLabel;
    private javax.swing.JLabel forwardLabel;
    private javax.swing.JLabel backwardLabel;
    private javax.swing.JLabel ambassadorLabel;
    private javax.swing.JLabel orphanLabel;

    /*
     * Default constructor
     */
    public ForestFiresPanel(int pMode) {
        super(null);
        mode = pMode;
        initComponents();
    }

    /*
     * Default constructor
     */
    public ForestFiresPanel(int pMode, RandomNetworkPanel pPrevious) {
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
        return new String("Forest Fires Model");
    }

    /**
     *
     */
    public String getDescription() {
        return new String("Each node, u, is added one at a time and connected to ambassador-many existing nodes, a.  u is connected to incoming/outgoing nodes of a based on (f) and (b).  The same procedure is applied to all the newly cited vertices.");
    }

    /*
     * Initialize the components
     */
    private void initComponents() {

        //Create the TextFields
        nodeTextField = new javax.swing.JTextField();
        forwardTextField = new javax.swing.JTextField();
        backwardTextField = new javax.swing.JTextField();
        ambassadorTextField = new javax.swing.JTextField();
        orphanTextField = new javax.swing.JTextField();

        nodeTextField.setPreferredSize(new Dimension(50, 25));
        forwardTextField.setPreferredSize(new Dimension(50, 25));
        backwardTextField.setPreferredSize(new Dimension(50, 25));
        orphanTextField.setPreferredSize(new Dimension(50, 25));
        ambassadorTextField.setPreferredSize(new Dimension(50, 25));

        nodeTextField.setMinimumSize(new Dimension(50, 25));
        forwardTextField.setMinimumSize(new Dimension(50, 25));
        backwardTextField.setMinimumSize(new Dimension(50, 25));
        orphanTextField.setMinimumSize(new Dimension(50, 25));
        ambassadorTextField.setMinimumSize(new Dimension(50, 25));

        
        
        nodeTextField.setText("" + defaultNodeValue);
        orphanTextField.setText("" + defaultOrphanValue);
        ambassadorTextField.setText("" + defaultAmbassadorValue);
        forwardTextField.setText("" + defaultForwardValue);
        backwardTextField.setText("" + defaultBackwardValue);

        nodeTextField.setHorizontalAlignment(JTextField.RIGHT);
        forwardTextField.setHorizontalAlignment(JTextField.RIGHT);
        backwardTextField.setHorizontalAlignment(JTextField.RIGHT);
        ambassadorTextField.setHorizontalAlignment(JTextField.RIGHT);
        orphanTextField.setHorizontalAlignment(JTextField.RIGHT);

        //Create the buttons
        directedCheckBox = new javax.swing.JCheckBox();
        selfEdgeCheckBox = new javax.swing.JCheckBox();

        //Create the Labels
        nodeLabel = new javax.swing.JLabel();
        backwardLabel = new javax.swing.JLabel();
        forwardLabel = new javax.swing.JLabel();
        ambassadorLabel = new javax.swing.JLabel();
        orphanLabel = new javax.swing.JLabel();

        //Set the text on the labels
        orphanLabel.setText("Orphan Prob:");
        ambassadorLabel.setText("Num. ambass. nodes");
        nodeLabel.setText("Number of Nodes:");
        forwardLabel.setText("Forward Prob:");
        backwardLabel.setText("Backward Prob:");
        selfEdgeCheckBox.setText("Allow reflexive Edges (u,u)");
        directedCheckBox.setText("Undirected");


        selfEdgeCheckBox.setEnabled(false);
        directedCheckBox.setEnabled(true);
        directedCheckBox.addItemListener(this);



        setLayout(new GridBagLayout());

        //Setup the titel
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 10, 0, 0);
        c.gridx = 0;
        c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
        c.anchor = GridBagConstraints.LINE_START;
        add(nodeLabel, c);

        c = null;
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
//		c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.LINE_START;
        add(nodeTextField, c);




        c = new GridBagConstraints();
        c.insets = new Insets(0, 10, 0, 0);
        c.gridx = 0;
        c.gridy = 1;
		c.weightx = 1;
		c.weighty = 1;
        c.anchor = GridBagConstraints.LINE_START;
        add(ambassadorLabel, c);

        c = null;
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
		c.weightx = 1;
		c.weighty = 1;
//		c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.LINE_START;
        add(ambassadorTextField, c);


        c = null;
        c = new GridBagConstraints();
        c.insets = new Insets(0, 10, 0, 0);
        c.gridx = 0;
        c.gridy = 2;
		c.weightx = 1;
		c.weighty = 1;
        c.anchor = GridBagConstraints.LINE_START;
        add(forwardLabel, c);

        c = null;
        c = new GridBagConstraints();
		c.weightx = 1;
		c.weighty = 1;
        c.gridx = 1;
        c.gridy = 2;
        c.anchor = GridBagConstraints.LINE_START;
        add(forwardTextField, c);

        c = null;
        c = new GridBagConstraints();
		c.weightx = 1;
		c.weighty = 1;
        c.insets = new Insets(0, 10, 0, 0);
        c.gridx = 0;
        c.gridy = 3;
        c.anchor = GridBagConstraints.LINE_START;
        add(backwardLabel, c);

        c = null;
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 3;
        c.weightx = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.LINE_START;
        add(backwardTextField, c);

        c = null;
        c = new GridBagConstraints();
		c.weightx = 1;
		c.weighty = 1;
        c.insets = new Insets(0, 10, 0, 0);
        c.gridx = 0;
        c.gridy = 4;
        c.anchor = GridBagConstraints.LINE_START;
        add(orphanLabel, c);

        c = null;
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 4;
        c.weightx = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.LINE_START;
        add(orphanTextField, c);



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
        c.gridy = 5;
        c.weightx = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.LINE_START;
        add(directedCheckBox, c);

        /*
        c = null;
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(0, 10, 0, 0);
        c.gridx = 1;
        c.gridy = 6;
        c.weightx = 1;
        c.weighty = 1;
        add(selfEdgeCheckBox, c);
*/



    }

    /*
     *  Callback for the generate button
     */
    public RandomNetworkPanel next() {
        //The backward of each node
        double backward;

        //How much to interpolate between a lattice
        //and erdos-renyi model
        double forward;

        //The number of nodes in the network
        int numNodes;

        //Whether or not the network is directed
        boolean directed;

        //Whether or not the network allows reflexive edge
        boolean allowSelfEdge;

        int numAmbassador;
        double orphan;

        //Get the strings from the textfields
        String numNodeString = nodeTextField.getText();
        String forwardString = forwardTextField.getText();
        String backwardString = backwardTextField.getText();
        String ambassadorString = ambassadorTextField.getText();
        String orphanString = orphanTextField.getText();




        //Try to read the string into an integer
        try {
            orphan = Double.parseDouble(orphanString);

            if ((orphan < 0) || (orphan > 1)) {
                throw new Exception("Orphan Prob < 0 or 1 < ");
            }

        } catch (Exception e) {
            //If an error occurs than change the colors
            orphanTextField.grabFocus();
            nodeLabel.setForeground(java.awt.Color.BLACK);
            backwardLabel.setForeground(java.awt.Color.BLACK);
            orphanLabel.setForeground(java.awt.Color.RED);
            ambassadorLabel.setForeground(java.awt.Color.BLACK);
            forwardLabel.setForeground(java.awt.Color.BLACK);
            return this;
        }

        //Try to read the string into an integer
        try {
            numNodes = Integer.parseInt(numNodeString);

            if (numNodes < 3) {
                throw new Exception("Nodes must be > 2");
            }

        } catch (Exception e) {
            //If an error occurs than change the colors
            nodeTextField.grabFocus();
            backwardLabel.setForeground(java.awt.Color.BLACK);
            nodeLabel.setForeground(java.awt.Color.RED);
            ambassadorLabel.setForeground(java.awt.Color.BLACK);
            orphanLabel.setForeground(java.awt.Color.BLACK);
            forwardLabel.setForeground(java.awt.Color.BLACK);
            return this;
        }


        //Try to read the string into an integer
        try {

            numAmbassador = Integer.parseInt(ambassadorString);
            if (numAmbassador < 1) {
                throw new Exception("Nodes must be > 1");
            }

        } catch (Exception e) {
            //If an error occurs than change the colors
            ambassadorTextField.grabFocus();
            backwardLabel.setForeground(java.awt.Color.BLACK);
            ambassadorLabel.setForeground(java.awt.Color.RED);
            nodeLabel.setForeground(java.awt.Color.BLACK);
            forwardLabel.setForeground(java.awt.Color.BLACK);
            orphanLabel.setForeground(java.awt.Color.BLACK);

            return this;
        }



        //Try to read string into an double
        try {
            forward = Double.parseDouble(forwardString);

            //Check to make sure forward is a probability
            if ((forward < 0) || (forward > 1)) {
                throw (new Exception("Beta must be a probability"));
            }
        } catch (Exception e) {
            //If an error occurs than change the colors
            forwardTextField.grabFocus();
            backwardLabel.setForeground(java.awt.Color.BLACK);
            nodeLabel.setForeground(java.awt.Color.BLACK);
            forwardLabel.setForeground(java.awt.Color.RED);
            orphanLabel.setForeground(java.awt.Color.BLACK);
            ambassadorLabel.setForeground(java.awt.Color.BLACK);
            return this;
        }

        //Try to read this string into an integer
        try {
            backward = Double.parseDouble(backwardString);
            if ((backward < 0) || (backward > 1)) {
                throw (new Exception("Beta must be a probability"));
            }
        } catch (Exception e) {
            //If an error occurs than change the colors
            backwardTextField.grabFocus();
            backwardLabel.setForeground(java.awt.Color.RED);
            nodeLabel.setForeground(java.awt.Color.BLACK);
            forwardLabel.setForeground(java.awt.Color.BLACK);
            orphanLabel.setForeground(java.awt.Color.BLACK);
            ambassadorLabel.setForeground(java.awt.Color.BLACK);

            return this;
        }


        //If we got this far reset all to black
        backwardLabel.setForeground(java.awt.Color.BLACK);
        nodeLabel.setForeground(java.awt.Color.BLACK);
        forwardLabel.setForeground(java.awt.Color.BLACK);
        ambassadorLabel.setForeground(java.awt.Color.BLACK);

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
        ForestFiresModel gnm = new ForestFiresModel(numNodes, numAmbassador,
                allowSelfEdge, !directed, forward, backward, orphan);




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

    /** Listens to the check boxes. */
    public void itemStateChanged(ItemEvent e) {
        backwardTextField.setEnabled(!directedCheckBox.isSelected());
        backwardLabel.setEnabled(!directedCheckBox.isSelected());
    }
}
