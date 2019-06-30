/* File: DisplayResultsPanel.java
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

import java.util.*;
import java.awt.*;
import java.io.*;
import javax.swing.table.*;

import org.cytoscape.randomnetwork.RandomNetwork;
import org.cytoscape.randomnetwork.metrics.RandomNetworkAnalyzer;

import javax.swing.*;
import java.awt.Dimension;

/**
 * RandomizeExistingPanel is used for selecting which randomizing network model
 * to use.
 */
public class DisplayResultsPanel extends RandomNetworkPanel {

	/**
	 * Save Button
	 */
	private javax.swing.JLabel exportLabel;
	private javax.swing.JButton exportButton;

	private javax.swing.JLabel nodeLabel;
	private javax.swing.JLabel numNodeLabel;
	private javax.swing.JLabel edgeLabel;
	private javax.swing.JLabel numEdgeLabel;
	private javax.swing.JLabel timeLabel;
	private javax.swing.JLabel timeInfoLabel;
	private javax.swing.JLabel networkLabel;
	private javax.swing.JLabel roundLabel;
	// Treat this network as directed
	private javax.swing.JCheckBox directedCheckBox;
	//
	private javax.swing.JScrollPane scrollPane;
	private String[] columnNames = { "Metric", "Existing Net.", "Rnd. Net. Avg.", "Rnd. Net. Std.", "Norm. P-val" };
	private RandomNetworkAnalyzer rna;

	/**
	 * Default constructor
	 */
	public DisplayResultsPanel(RandomNetworkPanel pPrevious, RandomNetworkAnalyzer pRNA) {

		super(pPrevious);
		rna = pRNA;
		initComponents();
	}

	/**
	 *
	 */
	public String getNextText() {
		return new String("Save");

	}

	/**
	 *
	 */
	public String getTitle() {
		RandomNetwork net = rna.getNetwork();
		return new String("Results for " + net.getNetworkName());
	}

	/**
	 *
	 */
	public String getDescription() {
		return new String("Simuation results");
	}

	public String getNextTitle() {
		return new String("Save");
	}

	/**
	 * Initialize the components
	 */
	private void initComponents() {
		RandomNetwork random_network = rna.getNetwork();
		nodeLabel = new javax.swing.JLabel();
		numNodeLabel = new javax.swing.JLabel();
		edgeLabel = new javax.swing.JLabel();
		numEdgeLabel = new javax.swing.JLabel();
		timeLabel = new javax.swing.JLabel();
		timeInfoLabel = new javax.swing.JLabel();
		nodeLabel.setText("Number of nodes: ");
		edgeLabel.setText("Number of edges: ");
		timeLabel.setText("Time elapsed: ");
		numNodeLabel.setText("" + random_network.getNumNodes());
		numEdgeLabel.setText("" + random_network.getNumEdges());

		exportButton = new javax.swing.JButton("Export");

		exportButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveAll();
			}
		});

		try {
			rna.block();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double size = (rna.getResults().length * rna.getResults()[0].length * rna.getResults()[0][0].length * 12.5)
				/ 1000;
		exportLabel = new javax.swing.JLabel("Export all results (~" + size + "kb)");

		// Set up the Table for displaying
		DefaultTableModel model = new DefaultTableModel(rna.getData(), columnNames);
		JTable table = new JTable(model) {

			public boolean isCellEditable(int rowIndex, int colIndex) {
				return false; // Disallow the editing of any cell
			}

			public TableCellRenderer getCellRenderer(int row, int col) {
				DefaultTableCellRenderer tcrColumn = new DefaultTableCellRenderer();
				if (col == 0) {
					tcrColumn.setHorizontalAlignment(JTextField.LEFT);
				} else {
					tcrColumn.setHorizontalAlignment(JTextField.RIGHT);
				}
				return tcrColumn;

			}

			// Override this method so that it returns the preferred
			// size of the JTable instead of the default fixed size
			public Dimension getPreferredScrollableViewportSize() {
				return getPreferredSize();
			}
		};

		table.setAutoCreateRowSorter(true);

		table.getTableHeader().setReorderingAllowed(false);
		table.setGridColor(java.awt.Color.black);
		scrollPane = new JScrollPane(table);

		table.setPreferredScrollableViewportSize(new Dimension(500, 150));
		scrollPane.setPreferredSize(new Dimension(500, 150));
		scrollPane.setMinimumSize(new Dimension(500, 150));

		setLayout(new GridBagLayout());

		// Setup the titel
		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(0, 0, 0, 0);
		add(nodeLabel, c);

		c = null;
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(0, 0, 0, 0);
		add(numNodeLabel, c);

		c = null;
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 2;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(0, 0, 0, 0);
		add(edgeLabel, c);

		c = null;
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 2;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(0, 0, 0, 0);
		add(numEdgeLabel, c);

		c = null;
		c = new GridBagConstraints();
		c.gridx = 9;
		c.gridy = 1;
		c.anchor = GridBagConstraints.LINE_END;
		c.insets = new Insets(0, 0, 0, 0);
		add(exportLabel, c);

		c = null;
		c = new GridBagConstraints();
		c.gridx = 9;
		c.gridy = 2;
		c.anchor = GridBagConstraints.LINE_END;
		c.insets = new Insets(0, 0, 0, 0);
		add(exportButton, c);

		// Setup the
		c = null;
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 10;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(0, 0, 0, 0);
		c.weightx = 1;
		c.weighty = 1;
		add(scrollPane, c);
	}

	/**
	 *
	 */
	public RandomNetworkPanel next() {
		JFileChooser saveFile = new JFileChooser();
		String title = rna.getNetwork().getNetworkName();

		// rip off the extension
		StringTokenizer strTok = new StringTokenizer(title, ".");

		if (strTok.countTokens() > 1) {
			title = strTok.nextToken();
		}

		saveFile.setSelectedFile(new File(title + ".rn"));
		saveFile.showSaveDialog(this);
		File file = saveFile.getSelectedFile();

		if (file != null) {
			try {
				DataOutputStream dout = new DataOutputStream(new FileOutputStream(file));
				Object[][] data = rna.getData();

				for (int i = 0; i < columnNames.length; i++) {
					dout.writeBytes(columnNames[i] + "\t\t");
				}

				for (int i = 0; i < data.length; i++) {
					dout.writeBytes("\n");
					for (int j = 0; j < data[0].length; j++) {
						dout.writeBytes(data[i][j] + "\t\t");
					}
				}

				dout.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Nothing should change
		return this;
	}

	/**
	 */
	public void saveAll() {
		try {

			JFileChooser saveFile = new JFileChooser();
			String title = rna.getNetwork().getNetworkName();

			// rip off the extension
			StringTokenizer strTok = new StringTokenizer(title, ".");
			if (strTok.countTokens() > 1) {
				title = strTok.nextToken();
			}

			saveFile.setSelectedFile(new File(title + ".csv"));
			saveFile.showSaveDialog(this);
			File file = saveFile.getSelectedFile();

			DataOutputStream dout = new DataOutputStream(new FileOutputStream(file));

			double res[][][] = rna.getResults();
			String names[] = rna.getMetricNames();
			for (int n = 0; n < names.length; n++) {
				dout.writeBytes(names[n] + ", \t");
			}

			dout.writeBytes("\n");

			for (int m = 0; m < res.length; m++) {
				for (int i = 0; i < res[0].length; i++) {
					for (int j = 0; j < res[0][0].length; j++) {
						dout.writeBytes(res[m][i][j] + ",\t");
					}
					dout.writeBytes("\n");
				}
			}

			dout.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
