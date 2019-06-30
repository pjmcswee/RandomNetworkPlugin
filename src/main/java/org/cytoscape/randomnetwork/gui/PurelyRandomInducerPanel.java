/* 
File: PurelyRandomInducer.java
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
import org.cytoscape.randomnetwork.RandomNetworkImpl;
import org.cytoscape.randomnetwork.generator.randomizer.PurelyRandomInducer;

/**
 */
public class PurelyRandomInducerPanel extends RandomNetworkPanel implements PropertyChangeListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	//private static final long serialVersionUID = 1L;
	private int mode;
    //Group together the different options
    private javax.swing.ButtonGroup group;
    private javax.swing.JTextField numCreateTextField;
    private javax.swing.JLabel numCreateLabel;
    private javax.swing.JLabel numNodeExplain;
    private javax.swing.JTextField numNodeTextField;
    private javax.swing.JLabel numNodeLabel;
    //Treat this network as directed
    private javax.swing.JCheckBox directedCheckBox;


    /*
     *  Default constructor
     */
    public PurelyRandomInducerPanel(int pMode) {

        super(null);
        mode = pMode;
        initComponents();
       // Cytoscape.getSwingPropertyChangeSupport().addPropertyChangeListener(this);
       // Cytoscape.getDesktop().getNetworkViewManager().getSwingPropertyChangeSupport().addPropertyChangeListener(this);
    }


    /*
     *  Default constructor
     */
    public PurelyRandomInducerPanel(int pMode, RandomNetworkPanel pPrevious) {

        super(pPrevious);
        mode = pMode;
        initComponents();
        //Cytoscape.getSwingPropertyChangeSupport().addPropertyChangeListener(this);
        //Cytoscape.getDesktop().getNetworkViewManager().getSwingPropertyChangeSupport().addPropertyChangeListener(this);
    }

    /**
     *
     */
    public String getTitle() {
        return new String("Random Walk Inducer");
    }

    /**
     *
     */
    public String getDescription() {
        return new String("Creates a sub network using a randomly walking agent.");

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
        directedCheckBox = new javax.swing.JCheckBox();
        directedCheckBox.setText("Treat as undirected?");



        //CyNetwork net = Cytoscape.getCurrentNetwork();
//		CyNode node = Cytoscape.getCurerntNode();


        numCreateTextField = new javax.swing.JTextField();
        numCreateTextField.setText("1");
        numCreateTextField.setPreferredSize(new Dimension(30, 25));
        numCreateTextField.setMinimumSize(new Dimension(30, 25));
        numCreateTextField.setHorizontalAlignment(JTextField.RIGHT);
        numCreateLabel = new javax.swing.JLabel();
        numCreateLabel.setText("Number of networks to create:");

        int N = 0; //net.getNodeCount();
        numNodeTextField = new javax.swing.JTextField();
        numNodeTextField.setText("" + Math.max(1, (int) (.25 * N)));
        numNodeTextField.setPreferredSize(new Dimension(80, 25));
        numNodeTextField.setMinimumSize(new Dimension(80, 25));
        numNodeTextField.setHorizontalAlignment(JTextField.RIGHT);
        numNodeLabel = new javax.swing.JLabel();
        numNodeLabel.setText("Number of Nodes:");
        numNodeExplain = new javax.swing.JLabel();
        numNodeExplain.setText("<html><font size=2 face=Verdana> Randomly selects nodes</font></html>");


        numNodeExplain.setPreferredSize(new Dimension(250, 100));
        numNodeExplain.setMinimumSize(new Dimension(250, 100));

        if (mode == 1) {
            numCreateLabel.setVisible(false);
            numCreateTextField.setVisible(false);
        }

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(0, 5, 0, 5);
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.LINE_START;
        //c.weightx = 1;
        //c.weighty = 1;
        add(numCreateLabel, c);

        c = null;
        c = new GridBagConstraints();
        c.gridx = 2;
        c.gridy = 2;
        c.gridwidth = 1;
        c.insets = new Insets(0, 5, 0, 5);
        c.anchor = GridBagConstraints.LINE_START;
        //c.fill =  GridBagConstraints.HORIZONTAL;
        //c.ipadx = 20;
        //c.weightx = 1;
        //c.weighty = 1;
        add(numCreateTextField, c);


        c = null;
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(0, 5, 0, 5);
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.LINE_START;
        //c.weightx = 1;
        //c.weighty = 1;
        add(numNodeLabel, c);

        c = null;
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 1;
        c.insets = new Insets(0, 5, 0, 5);
        c.anchor = GridBagConstraints.LINE_START;
        //c.ipadx = 20;
        //c.weightx = 1;
        //c.weighty = 1;
        add(numNodeTextField, c);


        c = null;
        c = new GridBagConstraints();
        c.gridx = 2;
        c.gridy = 3;
        c.gridwidth = 2;
        c.insets = new Insets(0, 5, 0, 5);
        c.anchor = GridBagConstraints.LINE_START;
//		c.weightx = 1;
//		c.weighty = 1;
        add(numNodeExplain, c);



        c = null;
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 4;
        c.anchor = GridBagConstraints.LINE_START;
//		c.weightx = 1;
//		c.weighty = 1;
        add(directedCheckBox, c);




    }

    /*
     *  Callback for when the "Next" button is pushed
     */
    public RandomNetworkPanel next() {

        boolean directed = directedCheckBox.isSelected();
        CyNetwork net =  null;//Cytoscape.getCurrentNetwork();

        int N = net.getNodeCount();
        String iterString = numNodeTextField.getText();
        int numNodes;
        try {
            numNodes = Integer.parseInt(iterString);
            if ((numNodes < 1) || (numNodes > N)) {
                throw new Exception("The number of nodes must be realistic.");
            }

        } catch (Exception e) {
            numNodeLabel.setForeground(java.awt.Color.RED);
            return this;
        }

        numNodeLabel.setForeground(java.awt.Color.BLACK);
        RandomNetwork random_network = ConverterUtils.toRandomNetwork(net, !directed);

        PurelyRandomInducer rwi = new PurelyRandomInducer(random_network, !directed, numNodes);
        int visualRounds = 0;

        if (mode == 1) {
            if (mNext == null) {
                mNext = new AnalyzePanel(this, rwi, !directed);
            } else {
                ((AnalyzePanel) mNext).setDirected(!directed);
                ((AnalyzePanel) mNext).setGenerator(rwi);
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
            RandomNetworkImpl randGraph = rwi.generate();
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
        int N = net.getNodeCount();
        numNodeTextField.setText("" + Math.max(1, (int) (.25 * N)));
    }
} 
	
	
