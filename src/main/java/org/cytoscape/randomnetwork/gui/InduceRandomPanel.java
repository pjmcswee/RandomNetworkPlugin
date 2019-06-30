/* 
File: InduceRandomPanel.java
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

/**
 * GenerateRandomPanel is used for selecting which random 
 * network model to use.
 */
public class InduceRandomPanel extends RandomNetworkPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * Provides state information for the panel
     */
    private int mode;
    /**
     *
     */
    private int nextState;
    //Title Label
    //Group together the different options
    private ButtonGroup group;
    //Checkbox for random walker model
    private JCheckBox rwm;
    //Checkbox for purley random model
    private JCheckBox prm;
    //Label to describe erdos-renyi model
    private JLabel rwmExplain;
    //Label to describe watts-strogatz model
    private JLabel prmExplain;

    /**
     *  Default constructor
     */
    public InduceRandomPanel(int pMode, RandomNetworkPanel pPrevious) {

        super(pPrevious);
        mode = pMode;
        initComponents();
        nextState = -1;
    }

    /**
     *  Default constructor
     */
    public InduceRandomPanel(int pMode) {

        super(null);
        mode = pMode;
        initComponents();
        nextState = -1;
    }

    /**
     * Initialize the components
     */
    private void initComponents() {

        //Create the group
        group = new javax.swing.ButtonGroup();


        //Create the random walker checkBox
        rwm = new javax.swing.JCheckBox();
        //Create the purelyRandom
        prm = new javax.swing.JCheckBox();


        //Create the random walker label
        rwmExplain = new javax.swing.JLabel();
        //Create the purley random label
        prmExplain = new javax.swing.JLabel();


        //Set the random walker
        rwmExplain.setText("<html><font size=2 face=Verdana>Generate a sub network via a random walking agent.</font></html>");

        //Set the purely
        prmExplain.setText("<html><font size=2 face=Verdana>Generate a random network by randomly selecting nodes.</font></html>");



        rwmExplain.setPreferredSize(new Dimension(200, 40));
        prmExplain.setPreferredSize(new Dimension(200, 40));
        rwmExplain.setMinimumSize(new Dimension(200, 40));
        prmExplain.setMinimumSize(new Dimension(200, 40));


        //set the labels to opaque
        rwmExplain.setOpaque(true);
        prmExplain.setOpaque(true);

        //Set the text for the checkboxes
        rwm.setText("Random walker Model");
        prm.setText("Purely random Model");

        //Make barabasi-albert the default
        rwm.setSelected(true);

        //Add each checkbox to the group
        group.add(rwm);
        group.add(prm);

        setLayout(new GridBagLayout());

        //Setup the titel
        GridBagConstraints c = new GridBagConstraints();
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        add(rwm, c);

        //
        c = null;
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 5;
        c.weightx = 1;
        c.weighty = 1;
        add(rwmExplain, c);

        //
        c = null;
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 1;
        c.weighty = 1;
        add(prm, c);

        //
        c = null;
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 5;
        c.weightx = 1;
        c.weighty = 1;
        add(prmExplain, c);


    }

    /**
     *
     */
    public String getTitle() {
        return new String("Induce a Random SubNetwork");
    }

    /**
     *
     */
    public String getDescription() {
        return new String("Induce a random  sub network according to the one of the models below.");
    }

    /**
     *  Callback for when the "Next" button is pushed
     */
    public RandomNetworkPanel next() {

        //See which checkbox is selected and then display the appropriate panel
        if (rwm.isSelected()) {

            if ((mNext == null) || (nextState != 0)) {
                mNext = null;
                mNext = new RandomWalkInducerPanel(mode, this);
            }
            nextState = 0;

        } else if (prm.isSelected()) {

            if ((mNext == null) || (nextState != 1)) {
                mNext = null;
                mNext = new PurelyRandomInducerPanel(mode, this);
            }
            nextState = 1;
        }

        return mNext;
    }
}
