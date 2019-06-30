/*  
File: MainPanel.java
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

import javax.swing.*;
import java.awt.*;

import org.cytoscape.app.CyAppAdapter;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.event.CyEventHelper;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.util.swing.OpenBrowser;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingManager;



/**
 * This provides the buttons and a main panel
 */
public class RandomNetworkMainPanel extends JPanel implements CytoPanelComponent {

	JTabbedPane tabbedPane;	
	MainPanel generate;
	MainPanel randomize;
	MainPanel analyze;
	/**
	 * TODO: Move this to a central location so as to not thread them all over
	 * the god damn place.  Kind of ridiculous.
	 */
	private static final long serialVersionUID = 1L;
	CyNetworkFactory networkFactory;
	CyNetworkViewFactory networkViewFactory;
	CyNetworkViewManager networkViewManager;

	/**
	 * Main Constructor
	 */
	public RandomNetworkMainPanel(
			CyApplicationManager manager, CyAppAdapter adapter, OpenBrowser openBrowser, CyEventHelper eventHelper,
			CyNetworkFactory networkFactory, CyNetworkManager networkManager, CyNetworkViewFactory networkViewFactory,
			CyNetworkViewManager networkViewManager, VisualMappingManager visualMappingManager) {
		super(new GridBagLayout());
		this.setBounds(0, 0, 400, 200);
		generate = new MainPanel(new GenerateRandomPanel(0), manager, adapter, openBrowser, eventHelper, networkFactory, networkManager,
                networkViewFactory, networkViewManager, visualMappingManager);

		randomize = new MainPanel(new RandomizeExistingPanel(0, manager), manager, adapter, openBrowser, eventHelper, networkFactory, networkManager,
                networkViewFactory, networkViewManager, visualMappingManager);
		
		analyze = new MainPanel(new RandomComparisonPanel(manager), manager, adapter, openBrowser, eventHelper, networkFactory, networkManager,
                networkViewFactory, networkViewManager, visualMappingManager);
		
		initializeComponents();
		makeLayout();
	}
	
	private void initializeComponents() {
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addTab("Generate", null, generate,
                "Does nothing");
		tabbedPane.addTab("Randomize", null, randomize,
                "Does nothing");
		tabbedPane.addTab("Compare", null, analyze,
                "Does nothing");
	
	}
	
	private void makeLayout() {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 0, 0, 0);
		c.anchor = GridBagConstraints.NORTHEAST;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		add(tabbedPane, c);
	}

	// Obligatory components for CytoPanelComponent
	public Component getComponent() {
		return this;
	}

	public CytoPanelName getCytoPanelName() {
		return CytoPanelName.WEST;
	}

	public String getTitle() {
		return "Random Networks";
	}

	public Icon getIcon() {
		return null;
	}
}
