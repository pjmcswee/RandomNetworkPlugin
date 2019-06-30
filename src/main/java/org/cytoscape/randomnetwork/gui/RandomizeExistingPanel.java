/* 
File: RandomizeExistingPanel.java
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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.randomnetwork.ConverterUtils;
import org.cytoscape.randomnetwork.RandomNetwork;
import org.cytoscape.randomnetwork.generator.randomizer.DegreePreservingNetworkRandomizer;
import org.cytoscape.application.CyApplicationManager;


/**
 * RandomizeExistingPanel is used for selecting which randomizing 
 * network model to use.
 */
public class RandomizeExistingPanel extends RandomNetworkPanel implements PropertyChangeListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int mode;
    private int visualRounds = 1;
    //Group together the different options
    private javax.swing.ButtonGroup group;
    private javax.swing.JTextField numCreateTextField;
    private javax.swing.JLabel numCreateLabel;
    private javax.swing.JLabel numIterExplain;
    private javax.swing.JTextField numIterTextField;
    private javax.swing.JLabel numIterLabel;
    //Treat this network as directed
    private javax.swing.JCheckBox directedCheckBox;
    //Checkbox for erdos-renyi model
    private javax.swing.JCheckBox degreePreserving;
    //Checkbox for barabasi-albert model
    private javax.swing.JLabel degreePreservingExplain;

    private CyApplicationManager applicationManager;

    /*
     *  Default constructor
     */
    public RandomizeExistingPanel(int pMode, CyApplicationManager pApplicationManager) {
        super(null);
        mode = pMode;
        applicationManager = pApplicationManager;
        initComponents();
    }


    /*
     *  Default constructor
     */
    public RandomizeExistingPanel(int pMode, RandomNetworkPanel pPrevious, CyApplicationManager pApplicationManager) {
        super(pPrevious);
        mode = pMode;
        applicationManager = pApplicationManager;
        initComponents();
    }

    /**
     *
     */
    public String getTitle() {
        return new String("Degree Preserving Random Shuffle");
    }

    /**
     *
     */
    public String getDescription() {
        return new String("Randomly selects two edges: (u,v) and (s,t). "
                + "Where u &#x2260; v &#x2260; s &#x2260; t and (u,t),(s,v) are not currently edges. "
                + "(u,v),(s,t) are deleted and (u,t),(s,v) are inserted to the network. ");

    }

    public String getNextText() {
        if (mode == 0) {
            return "Randomize";
        } else {
            return "Next";
        }
    }

    /**
     * Initialize the components
     */
    private void initComponents() {

        //Create the erdos-renyi checkbox
        degreePreserving = new javax.swing.JCheckBox();


        //Create the barabasi-albert  label
        degreePreservingExplain = new javax.swing.JLabel();

        //Set the erdos-renyi text
        degreePreservingExplain.setText("<html><font size=2 face=Verdana>Shuffle edges while keeping in/out degree of each node.</font></html>");
        degreePreservingExplain.setPreferredSize(new Dimension(150, 50));
        degreePreservingExplain.setMinimumSize(new Dimension(150, 50));

        degreePreserving.setVisible(false);
        degreePreservingExplain.setVisible(false);
        directedCheckBox = new javax.swing.JCheckBox();
        directedCheckBox.setText("Treat as undirected?");
        //set the labels to opaque
        degreePreservingExplain.setOpaque(true);

        //Set the text for the checkboxes
        degreePreserving.setText("Edge Shuffle");

        //Make barabasi-albert the default
        degreePreserving.setSelected(true);


        numCreateTextField = new javax.swing.JTextField();
        numCreateTextField.setText("1");
        numCreateTextField.setPreferredSize(new Dimension(30, 25));
        numCreateTextField.setHorizontalAlignment(JTextField.RIGHT);
        numCreateLabel = new javax.swing.JLabel();
        numCreateLabel.setText("Number of networks to create:");

        CyNetwork net = applicationManager.getCurrentNetwork();
        int count = 10;
        if (net != null) {
        	count *= net.getEdgeCount();
        }
        numIterTextField = new javax.swing.JTextField();
        numIterTextField.setText("" + count );
        numIterTextField.setPreferredSize(new Dimension(80, 25));
        numIterTextField.setMinimumSize(new Dimension(80, 25));

        numIterTextField.setHorizontalAlignment(JTextField.RIGHT);
        numIterLabel = new javax.swing.JLabel();
        numIterLabel.setText("Num Shuffles:");
        numIterExplain = new javax.swing.JLabel();
        numIterExplain.setText("<html><font size=2 face=Verdana>Every edge is not guaranteed to be shuffled. "
                + "As the number of shuffles increases, the resulting randomized network becomes more random. "
                + "By default the number of edges from the currently selected network is automatically entered.</font></html>");

        numIterExplain.setPreferredSize(new Dimension(250, 100));
        numIterExplain.setMinimumSize(new Dimension(250, 100));

        if (mode == 1) {
            numCreateLabel.setVisible(false);
            numCreateTextField.setVisible(false);
        }

        setLayout(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0, 5, 0, 5);
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.LINE_START;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(numCreateLabel, c);

        c = null;
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 1;
        c.insets = new Insets(0, 5, 0, 5);
        c.anchor = GridBagConstraints.LINE_START;
        c.fill =  GridBagConstraints.HORIZONTAL;
        add(numCreateTextField, c);

        c = null;
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(0, 5, 0, 5);
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.LINE_START;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(numIterLabel, c);

        c = null;
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.insets = new Insets(0, 5, 0, 5);
        c.anchor = GridBagConstraints.LINE_START;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(numIterTextField, c);

        c = null;
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.insets = new Insets(0, 5, 0, 5);
        c.anchor = GridBagConstraints.LINE_START;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(numIterExplain, c);

        c = null;
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 4;
        c.anchor = GridBagConstraints.LINE_START;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(directedCheckBox, c);
    }

    /*
     *  Callback for when the "Next" button is pushed
     */
    public RandomNetworkPanel next() {

        boolean directed = directedCheckBox.isSelected();
        CyNetwork net = applicationManager.getCurrentNetwork();

        if (net == null) {
        	JOptionPane.showMessageDialog(this, "Please select a network first.");
        	return this;
        }
        String iterString = numIterTextField.getText();
        int numIter;
        try {
            numIter = Integer.parseInt(iterString);
            if (numIter < 1) {
                throw new Exception("The number of iterations must be greater than 0.");
            }

        } catch (Exception e) {
            numIterLabel.setForeground(java.awt.Color.RED);
            return this;
        }

        numIterLabel.setForeground(java.awt.Color.BLACK);
        RandomNetwork random_network = ConverterUtils.toRandomNetwork(net, !directed);
        DegreePreservingNetworkRandomizer dpnr = new DegreePreservingNetworkRandomizer(random_network, !directed, numIter);

        if (mode == 1) {
            if (mNext == null) {
                mNext = new AnalyzePanel(this, dpnr, !directed);
            } else {
                ((AnalyzePanel) mNext).setDirected(!directed);
                ((AnalyzePanel) mNext).setGenerator(dpnr);
            }
            return mNext;
        }

        try {
            String value = numCreateTextField.getText().trim();
            value = value.trim();
            visualRounds = Integer.parseInt(value);
            if (visualRounds < 1) {
                throw new Exception("Visual rounds must be grater than 1.");
            }
        } catch (Exception e) {
            numCreateLabel.setForeground(java.awt.Color.RED);
            return this;
        }
        numCreateLabel.setForeground(java.awt.Color.BLACK);


        for (int i = 0; i < visualRounds; i++) {
            RandomNetwork randGraph = dpnr.generate();
            CyNetwork randNetwork = ConverterUtils.toCyNetwork(randGraph);
        }

        //Go up through the parents to the main window
        return this;
    }

    /**
     *
     */
    public void propertyChange(PropertyChangeEvent event) {
        CyNetwork net = null;//Cytoscape.getCurrentNetwork();
        int E = net.getEdgeCount();
        numIterTextField.setText("" + (10 * E));
    }
} 
	
	
