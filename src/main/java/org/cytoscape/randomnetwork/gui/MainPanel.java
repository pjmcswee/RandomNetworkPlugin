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

import java.awt.*;
import javax.swing.*;

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


import java.io.IOException;

/**
 * This provides the buttons and a main panel
 */
public class MainPanel extends JPanel implements CytoPanelComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RandomNetworkPanel mAlgorithmPanel;
	private JButton mNextButton;
	private JButton mBackButton;
	private JButton mHelpButton;
	private JLabel mTitle;
	private JLabel mDescription;

	CyNetworkFactory networkFactory;
	CyNetworkViewFactory networkViewFactory;
	CyNetworkViewManager networkViewManager;

	/**
	 * Main Constructor
	 */
	public MainPanel(RandomNetworkPanel pPanel, CyApplicationManager manager, CyAppAdapter adapter,
			OpenBrowser openBrowser, CyEventHelper eventHelper, CyNetworkFactory networkFactory,
			CyNetworkManager networkManager, CyNetworkViewFactory networkViewFactory,
			CyNetworkViewManager networkViewManager, VisualMappingManager visualMappingManager) {
		super(new GridBagLayout());
		mAlgorithmPanel = pPanel;
		initComponents();
		algorithmPanelChange();
	}

	/**
	 * Initialize all swing components
	 */
	private void initComponents() {

		mTitle = new JLabel();
		mTitle.setFont(new java.awt.Font("Sans-Serif", Font.BOLD, 14));
		mTitle.setPreferredSize(new Dimension(500, 20));
		mTitle.setMinimumSize(new Dimension(500, 20));

		mDescription = new JLabel();
		mDescription.setPreferredSize(new Dimension(350, 80));
		mDescription.setMinimumSize(new Dimension(350, 80));

		// Set up the run button
		mNextButton = new JButton();
		mNextButton.setText("Next");
		mNextButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				nextButtonActionPerformed(evt);
			}
		});

		// Set up the cancel button
		mBackButton = new JButton();
		mBackButton.setText("Back");
		mBackButton.setVisible(false);
		mBackButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				backButtonActionPerformed(evt);
			}
		});

		mHelpButton = new JButton();
		mHelpButton.setText("Help");
		mHelpButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				helpButtonActionPerformed(evt);
			}
		});

		makeLayout();
	}

	private void helpButtonActionPerformed(java.awt.event.ActionEvent evt) {
		openUrl("http://sites.google.com/site/randomnetworkplugin");
	}

	/**
	 * backButtonActionPerformed call back when the cancel button is pushed
	 */
	private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {
		mAlgorithmPanel = mAlgorithmPanel.getPrevious();
		algorithmPanelChange();
	}

	/**
	 * Callback for when the "Next" button is pushed
	 */
	private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {
		mAlgorithmPanel = mAlgorithmPanel.next();
		algorithmPanelChange();
	}

	/**
	 * Update parameters when the algorithmPanel has been changed
	 */
	private void algorithmPanelChange() {
		mNextButton.setText(mAlgorithmPanel.getNextText());
		if (mAlgorithmPanel.getPrevious() == null) {
			mBackButton.setVisible(false);
		} else {
			mBackButton.setVisible(true);
		}

		mNextButton.setText(mAlgorithmPanel.getNextText());
		mTitle.setText(mAlgorithmPanel.getTitle());
		mDescription.setText("<html><font size=2 face=Verdana>" + mAlgorithmPanel.getDescription() + "</font></html>");
		removeAll();
		makeLayout();
		repaint();
	}

	/**
	 *
	 */
	private void makeLayout() {
		setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(476, 5));

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		c.insets = new Insets(20, 10, 10, 0);
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		add(mTitle, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 3;
		c.weightx = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 10, 20, 0);
		add(mDescription, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(0, 10, 0, 10);
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 3;
		add(mAlgorithmPanel, c);

		c = null;
		c = new GridBagConstraints();
		c.insets = new Insets(10, 10, 10, 10);
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(mHelpButton, c);

		c = null;
		c = new GridBagConstraints();
		c.insets = new Insets(10, 10, 10, 10);
		c.gridx = 1;
		c.weighty = 1;
		c.gridy = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		add(mBackButton, c);

		c = null;
		c = new GridBagConstraints();
		c.insets = new Insets(10, 10, 10, 10);
		c.gridx = 2;
		c.weighty = 1;
		c.gridy = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		add(mNextButton, c);
	}

	/**
	 * Method to Open the Broser with Given URL
	 * 
	 * @param url
	 */
	public static void openUrl(String url) {
		String os = System.getProperty("os.name");
		Runtime runtime = Runtime.getRuntime();
		try {
			// Block for Windows Platform
			if (os.startsWith("Windows")) {
				String cmd = "rundll32 url.dll,FileProtocolHandler " + url;
				Process p = runtime.exec(cmd);
			}
			// Block for Mac OS
			else if (os.startsWith("Mac OS")) {
				String[] args = { "osascript", "-e", "open location \"" + url + "\"" };
				try {
					Process process = runtime.exec(args);
				} catch (IOException e) {
					// do what you want with this
				}
			}
			// Block for UNIX Platform
			else {
				String[] browsers = { "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape" };
				String browser = null;
				for (int count = 0; count < browsers.length && browser == null; count++)
					if (runtime.exec(new String[] { "which", browsers[count] }).waitFor() == 0)
						browser = browsers[count];
				if (browser == null)
					throw new Exception("Could not find web browser");
				else
					runtime.exec(new String[] { browser, url });
			}
		} catch (Exception x) {
			System.err.println("Exception occurd while invoking Browser!");
			x.printStackTrace();
		}

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