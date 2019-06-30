/* 
File: RandomWalkInducer.java
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

import java.util.*;
import org.cytoscape.*;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.randomnetwork.ConverterUtils;
import org.cytoscape.randomnetwork.RandomNetwork;
import org.cytoscape.randomnetwork.generator.randomizer.RandomWalkInducer;
import org.cytoscape.view.*;
import org.cytoscape.view.model.CyNetworkView;

import java.awt.*;
//import giny.view.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;

/**
 */
public class RandomWalkInducerPanel extends RandomNetworkPanel implements PropertyChangeListener {

    private int mode;
    //Group together the different options
    private javax.swing.ButtonGroup group;
    private javax.swing.JTextField numCreateTextField;
    private javax.swing.JLabel numCreateLabel;

    private javax.swing.JTextField numNodeTextField;
    private javax.swing.JLabel numNodeLabel;
    //Treat this network as directed
    private javax.swing.JCheckBox directedCheckBox;


    private javax.swing.JCheckBox walker;
	private javax.swing.JCheckBox bfs;
	private javax.swing.JCheckBox dfs;
	private javax.swing.JCheckBox pure;
    private javax.swing.JLabel walkerExplain;
	private javax.swing.JLabel bfsExplain;
	private javax.swing.JLabel dfsExplain;
	private javax.swing.JLabel pureExplain;
    
	private javax.swing.JCheckBox randNodeCheckBox;

    /*
     *  Default constructor
     */
    public RandomWalkInducerPanel(int pMode) {
        super(null);
        mode = pMode;
        initComponents();
        //Cytoscape.getSwingPropertyChangeSupport().addPropertyChangeListener(this);
        //Cytoscape.getDesktop().getNetworkViewManager().getSwingPropertyChangeSupport().addPropertyChangeListener(this);
    }


    /*
     *  Default constructor
     */
    public RandomWalkInducerPanel(int pMode, RandomNetworkPanel pPrevious) {

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
        return new String("Sub-Graph Walk Inducer");
    }

    /**
     *
     */
    public String getDescription() {
        return new String("Creates a sub-graph.");

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
        walker = new javax.swing.JCheckBox();
        //Create the barabasi-albert  label
        walkerExplain = new javax.swing.JLabel();
		bfs = new javax.swing.JCheckBox();
		dfs= new javax.swing.JCheckBox();
		pure= new javax.swing.JCheckBox();
		bfsExplain = new javax.swing.JLabel();
		dfsExplain = new javax.swing.JLabel();
		pureExplain = new javax.swing.JLabel();

		group = new ButtonGroup();
		group.add(walker);
		group.add(bfs);
		group.add(dfs);
		group.add(pure);


        //Set the erdos-renyi text
        walkerExplain.setPreferredSize(new Dimension(250, 50));
        walkerExplain.setMinimumSize(new Dimension(250, 50));
        walkerExplain.setText("<html><font size=2 face=Verdana> Select subgraph with a random walker.</font></html>");		
        bfsExplain.setPreferredSize(new Dimension(250, 50));
        bfsExplain.setMinimumSize(new Dimension(250, 50));
		bfsExplain.setText("<html><font size=2 face=Verdana> Select subgraph via breadth-first search.</font></html>");
        dfsExplain.setPreferredSize(new Dimension(250, 50));
        dfsExplain.setMinimumSize(new Dimension(250, 50));
		dfsExplain.setText("<html><font size=2 face=Verdana> Select subgraph via depth-first search.</font></html>");		
        pureExplain.setPreferredSize(new Dimension(250, 50));
        pureExplain.setMinimumSize(new Dimension(250, 50));
		pureExplain.setText("<html><font size=2 face=Verdana> Select subgraph via random node selection.</font></html>");		

		JSeparator line = new JSeparator(SwingConstants.VERTICAL);

		randNodeCheckBox = new javax.swing.JCheckBox();
		randNodeCheckBox.setText("Select starting node");

        directedCheckBox = new javax.swing.JCheckBox();
        directedCheckBox.setText("Undirected");

        //Make barabasi-albert the default
        walker.setSelected(true);

        CyNetwork net = null;//Cytoscape.getCurrentNetwork();

		int N = net.getNodeCount();
        numNodeTextField = new javax.swing.JTextField();
        numNodeTextField.setText("" + Math.max(1, (int) (.5 * N)));
        numNodeTextField.setPreferredSize(new Dimension(80, 25));
        numNodeTextField.setMinimumSize(new Dimension(80, 25));
        numNodeTextField.setHorizontalAlignment(JTextField.RIGHT);
        numNodeLabel = new javax.swing.JLabel();
        numNodeLabel.setText("Nodes:");


        setLayout(new GridBagLayout());

        //
        GridBagConstraints c = null; 
        c = null;
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
       // c.insets = new Insets(0, 5, 0, 5);
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.LINE_START;
        //c.weightx = 1;
        //c.weighty = 1;
        add(numNodeLabel, c);

        c = null;
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 1;
        //c.insets = new Insets(0, 5, 0, 5);
        c.anchor = GridBagConstraints.LINE_START;
        //c.ipadx = 20;
        //c.weightx = 1;
        //c.weighty = 1;
        add(numNodeTextField, c);

		c = null;
        c = new GridBagConstraints();
        c.insets = new Insets(5, 0, 0, 0);
        c.gridx = 2;
        c.gridheight = 5;
        c.gridy = 0;
        c.fill = GridBagConstraints.VERTICAL;
        add(line, c);
				
		c = new GridBagConstraints();		
        c.gridx = 3;
        c.gridy = 0;
        c.gridwidth = 1;		
//        c.insets = new Insets(0, 5, 0, 5);
        c.anchor = GridBagConstraints.LINE_START;
        //c.weightx = 1;
        //c.weighty = 1;
        add(walker, c);

        //
        c = null;
        c = new GridBagConstraints();
        c.gridx = 4;
        c.gridy = 0;
        c.gridwidth = 1;
  //      c.insets = new Insets(0, 5, 0, 5);
        c.anchor = GridBagConstraints.LINE_START;
        //c.weightx = 1;
        //c.weighty = 1;
        add(walkerExplain, c);

	
		c = new GridBagConstraints();		
        c.gridx = 3;
        c.gridy = 1;
    //    c.insets = new Insets(0, 5, 0, 5);
        c.anchor = GridBagConstraints.LINE_START;
        //c.weightx = 1;
        //c.weighty = 1;
        add(bfs, c);

        //
        c = null;
        c = new GridBagConstraints();
        c.gridx = 4;
        c.gridy = 1;
        c.gridwidth = 1;
   //     c.insets = new Insets(0, 5, 0, 5);
        c.anchor = GridBagConstraints.LINE_START;
        //c.weightx = 1;
        //c.weighty = 1;
        add(bfsExplain, c);

        c = null;
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.LINE_START;
//		c.weightx = 1;
//		c.weighty = 1;
        add(directedCheckBox, c);


		
		c = new GridBagConstraints();		
        c.gridx = 3;
        c.gridy = 2;
        c.gridwidth = 1;		
//        c.insets = new Insets(0, 5, 0, 5);
        c.anchor = GridBagConstraints.LINE_START;
        //c.weightx = 1;
        //c.weighty = 1;
        add(dfs, c);

        //
        c = null;
        c = new GridBagConstraints();
        c.gridx = 4;
        c.gridy = 2;
        c.gridwidth = 1;
       // c.insets = new Insets(0, 5, 0, 5);
        c.anchor = GridBagConstraints.LINE_START;
        //c.weightx = 1;
        //c.weighty = 1;
        add(dfsExplain, c);




		c = null;
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.LINE_START;
//		c.weightx = 1;
//		c.weighty = 1;
        add(randNodeCheckBox, c);
		




		c = new GridBagConstraints();		
        c.gridx = 3;
        c.gridy = 3;
		        c.gridwidth = 1;
//        c.insets = new Insets(0, 5, 0, 5);
        c.anchor = GridBagConstraints.LINE_START;
        //c.weightx = 1;
        //c.weighty = 1;
        add(pure, c);

        //
        c = null;
        c = new GridBagConstraints();
        c.gridx = 4;
        c.gridy = 3;
        c.gridwidth = 1;
//        c.insets = new Insets(0, 5, 0, 5);
        c.anchor = GridBagConstraints.LINE_START;
        //c.weightx = 1;
        //c.weighty = 1;
        add(pureExplain, c);
		
		


    }

    /*
     *  Callback for when the "Next" button is pushed
     */
    public RandomNetworkPanel next() {

        boolean directed = directedCheckBox.isSelected();
        boolean select = randNodeCheckBox.isSelected();
		
		CyNetwork net = null;//Cytoscape.getCurrentNetwork();
		
        CyNetworkView view = null;//Cytoscape.getCurrentNetworkView();
        CyNode cynode = null;

        //can't continue if either of these is null
        if ((net != null) && (view != null)) {
			if(select)
			{
				/*
				//put up a dialog if there are no selected nodes
				if (view.getSelectedNodes().size() == 0) {
					JOptionPane.showMessageDialog(view.getComponent(),
							"Please select one node.");
					return this;
				}
			
				//iterate over every node view
				for (Iterator i = view.getSelectedNodes().iterator(); i.hasNext();) {
					NodeView nView = (NodeView) i.next();
					//first get the corresponding node in the network
					cynode = (CyNode) nView.getNode();
				}*/
			}
			else{
				Random r = new Random();
				int dart = Math.abs(r.nextInt(net.getNodeCount()));
				//Get the iterator for the CyNodes
				Iterator netIter = null;//net.nodesIterator();
				int count = 0;
				//For each Node
				while (netIter.hasNext()) {
					//Get the next node
					cynode = (CyNode) netIter.next();
					if(dart == count){
						break;
					}
					count++;
				}
			}
		}

/////////////



/////////
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

		//Get the node index of the selected cynode
        Integer node = null;
        if (cynode != null) {
            node = null;//random_network.lookUpNode(cynode);
        }
		
		int type = -1;
		if(walker.isSelected())
		{
			type = 3;
		}
		else if(bfs.isSelected()){
			type = 1;		
		}
		else if(dfs.isSelected()){
			type = 2;
		}
		
		else if(pure.isSelected()){
			type = 0;
		}


		//Create the walker
        RandomWalkInducer rwi = new RandomWalkInducer(random_network, !directed, numNodes, node, type);

		//If in analyze mode
        if (mode == 1) {
            if (mNext == null) {
                mNext = new AnalyzePanel(this, rwi, !directed);
            } else {
                ((AnalyzePanel) mNext).setDirected(!directed);
                ((AnalyzePanel) mNext).setGenerator(rwi);
            }
            return mNext;
		}
		            RandomNetwork randGraph = rwi.generate();
		CyNetwork randNetwork = ConverterUtils.toCyNetwork(randGraph);
        
		
        //Go up through the parents to the main window
        return this;
    }

    /**
     *
     */
    public void propertyChange(PropertyChangeEvent event) {
        CyNetwork net = null;//Cytoscape.getCurrentNetwork();
        int N = net.getNodeCount();
        numNodeTextField.setText("" + Math.max(1, (int) (.5 * N)));
    }
} 
	
	
