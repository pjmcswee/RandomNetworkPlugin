/* 
File: GenerateRandomPanel.java
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
 * GenerateRandomPanel is used for selecting which random network model to use.
 */
public class GenerateRandomPanel extends RandomNetworkPanel {

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
	// Title Label
	// Group together the different options
	private ButtonGroup group;
	// Checkbox for erdos-renyi model
	private JCheckBox erm;
	// Checkbox for watts-strogatz model
	private JCheckBox wsm;
	// Checkbox for barabasi-albert model
	private JCheckBox bam;
	private JCheckBox gnm;
	private JCheckBox ffm;
	private JCheckBox km;
	private JCheckBox dpm;
	// Label to describe erdos-renyi model
	private JLabel ermExplain;
	// Label to describe watts-strogatz model
	private JLabel wsmExplain;
	// Checkbox for barabasi-albert model
	private JLabel bamExplain;
	private JLabel gnmExplain;
	private JLabel ffmExplain;
	private JLabel kmExplain;
	private JLabel dpmExplain;

	/**
	 * Default constructor
	 */
	public GenerateRandomPanel(int pMode, RandomNetworkPanel pPrevious) {
		super(pPrevious);
		mode = pMode;
		initComponents();
		nextState = -1;
	}

	/**
	 * Default constructor
	 */
	public GenerateRandomPanel(int pMode) {
		super(null);
		mode = pMode;
		initComponents();
		nextState = -1;
	}

	/**
	 * Initialize the components
	 */
	private void initComponents() {

		// Create the group
		group = new javax.swing.ButtonGroup();
		// Create the erdos-renyi checkbox
		erm = new javax.swing.JCheckBox();
		// Create the watts-strogatz checkbox
		wsm = new javax.swing.JCheckBox();
		// Create the barabasi-albert checkbox
		bam = new javax.swing.JCheckBox();

		gnm = new javax.swing.JCheckBox();
		ffm = new javax.swing.JCheckBox();
		km = new javax.swing.JCheckBox();
		dpm = new javax.swing.JCheckBox();

		// Create the model label
		ermExplain = new javax.swing.JLabel();
		wsmExplain = new javax.swing.JLabel();
		bamExplain = new javax.swing.JLabel();
		gnmExplain = new javax.swing.JLabel();
		ffmExplain = new javax.swing.JLabel();
		kmExplain = new javax.swing.JLabel();
		dpmExplain = new javax.swing.JLabel();

		// Set the model texts
		ermExplain.setText("<html><font size=2 face=Verdana>A flat random network.</font></html>");
		wsmExplain.setText(
				"<html><font size=2 face=Verdana>A random network with <br>high clustering coefficient.</font></html>");
		bamExplain.setText("<html><font size=2 face=Verdana>A scale-free random network.</font></html>");
		gnmExplain.setText("<html><font size=2 face=Verdana>A geometric random network.</font></html>");
		ffmExplain.setText("<html><font size=2 face=Verdana>A forest fires random network.</font></html>");
		kmExplain.setText("<html><font size=2 face=Verdana>A Kronecker random network.</font></html>");
		dpmExplain.setText("<html><font size=2 face=Verdana>A random network by node duplication.</font></html>");

		dpmExplain.setPreferredSize(new Dimension(200, 40));
		dpmExplain.setMinimumSize(new Dimension(200, 40));
		ermExplain.setPreferredSize(new Dimension(200, 40));
		wsmExplain.setPreferredSize(new Dimension(200, 40));
		bamExplain.setPreferredSize(new Dimension(200, 40));
		gnmExplain.setPreferredSize(new Dimension(200, 40));
		gnmExplain.setMinimumSize(new Dimension(200, 40));
		ffmExplain.setPreferredSize(new Dimension(200, 40));
		ffmExplain.setMinimumSize(new Dimension(200, 40));
		ermExplain.setMinimumSize(new Dimension(200, 40));
		wsmExplain.setMinimumSize(new Dimension(200, 40));
		bamExplain.setMinimumSize(new Dimension(200, 40));
		kmExplain.setPreferredSize(new Dimension(200, 40));
		kmExplain.setMinimumSize(new Dimension(200, 40));

		// set the labels to opaque
		ermExplain.setOpaque(true);
		wsmExplain.setOpaque(true);
		bamExplain.setOpaque(true);
		gnmExplain.setOpaque(true);
		ffmExplain.setOpaque(true);
		kmExplain.setOpaque(true);
		dpmExplain.setOpaque(true);

		// Set the text for the checkboxes
		erm.setText("<html><font size=3>Erdos-Renyi Model</html>");
		wsm.setText("<html><font size=3>Watts-Strogatz Model</font></html>");
		bam.setText("<html><font size=3>Barabasi-Albert Model</font></html>");
		gnm.setText("<html><font size=3>Geometric Network Model</font></html>");
		ffm.setText("<html><font size=3>Forest firest Network Model</font></html>");
		km.setText("<html><font size=3>Kronecker Network Model</font></html>");
		dpm.setText("<html><font size=3>Duplication Model</font></html>");

		// Make barabasi-albert the default
		erm.setSelected(true);

		// Add each checkbox to the group
		group.add(bam);
		group.add(wsm);
		group.add(erm);
		group.add(gnm);
		group.add(ffm);
		group.add(km);
		group.add(dpm);

		setLayout(new GridBagLayout());

		// Setup the titel
		GridBagConstraints c = new GridBagConstraints();
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 1;
		add(erm, c);

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
		add(ermExplain, c);

		//
		c = null;
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 1;
		c.weighty = 1;
		add(wsm, c);

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
		add(wsmExplain, c);

		//
		c = null;
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 3;
		c.weightx = 1;
		c.weighty = 1;
		add(bam, c);

		//
		c = null;
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 1;
		c.gridy = 3;
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = 5;
		add(bamExplain, c);

		c = null;
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 4;
		c.weightx = 1;
		c.weighty = 1;
		add(gnm, c);

		//
		c = null;
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 1;
		c.gridy = 4;
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = 5;
		add(gnmExplain, c);

		c = null;
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 5;
		c.weightx = 1;
		c.weighty = 1;
		add(ffm, c);

		//
		c = null;
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 1;
		c.gridy = 5;
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = 5;
		add(ffmExplain, c);

		c = null;
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 6;
		c.weightx = 1;
		c.weighty = 1;
		add(dpm, c);

		//
		c = null;
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 1;
		c.gridy = 6;
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = 5;
		add(dpmExplain, c);
	}

	/**
	 *
	 */
	public String getTitle() {
		return new String("Generate Random Network");
	}

	/**
	 *
	 */
	public String getDescription() {
		return new String("Generate a random network according to the one of the models below.");
	}

	/**
	 * Callback for when the "Next" button is pushed
	 */
	public RandomNetworkPanel next() {

		// See which checkbox is selected and then display the appropriate panel
		if (erm.isSelected()) {

			if ((mNext == null) || (nextState != 0)) {
				mNext = null;
				mNext = new ErdosRenyiPanel(mode, this);
			}
			nextState = 0;

		} else if (wsm.isSelected()) {

			if ((mNext == null) || (nextState != 1)) {
				mNext = null;
				mNext = new WattsStrogatzPanel(mode, this);
			}
			nextState = 1;
		} else if (bam.isSelected()) {

			if ((mNext == null) || (nextState != 2)) {
				mNext = null;
				mNext = new BarabasiAlbertPanel(mode, this);
			}
			nextState = 2;
		} else if (gnm.isSelected()) {

			if ((mNext == null) || (nextState != 2)) {
				mNext = null;
				mNext = new GeometricNetworkPanel(mode, this);
			}
			nextState = 3;
		} else if (ffm.isSelected()) {

			if ((mNext == null) || (nextState != 2)) {
				mNext = null;
				mNext = new ForestFiresPanel(mode, this);
			}
			nextState = 3;
		} else if (dpm.isSelected()) {

			if ((mNext == null) || (nextState != 2)) {
				mNext = null;
				mNext = new DuplicationPanel(mode, this);
			}
			nextState = 3;
		}

		return mNext;
	}
}
