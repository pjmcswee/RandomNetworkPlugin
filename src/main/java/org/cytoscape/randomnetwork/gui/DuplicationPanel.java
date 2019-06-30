/*  File: DuplicationPanel.java
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
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.awt.event.*;
import javax.swing.*;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.randomnetwork.ConverterUtils;
import org.cytoscape.randomnetwork.RandomNetworkImpl;
import org.cytoscape.randomnetwork.generators.BarabasiAlbertModel;
import org.cytoscape.randomnetwork.generators.DuplicationModel;

/**
 * 
 *
 */
public class DuplicationPanel extends RandomNetworkPanel implements ActionListener, PropertyChangeListener { 

    /**
     *
     */
    private int mode;
    /**
     * Default values for the variables
     */
    private static final int defaultNodeValue = 100;
    private static final double defaultProbValue = 1.0;
	private static final int defaultInitValue = 0;
	private static final double defaultMutationtValue = 0.06;	
    //TextFields
    private javax.swing.JTextField nodeTextField;
    private javax.swing.JTextField mutationTextField;
	private javax.swing.JTextField initTextField;
    private javax.swing.JTextField probabilityTextField;
    //Buttons
    private javax.swing.JCheckBox directedCheckBox;
    private javax.swing.JCheckBox selfEdgeCheckBox;
	private javax.swing.JCheckBox useCurrentCheckBox;
    private javax.swing.ButtonGroup group;
    private javax.swing.JCheckBox full;
    private javax.swing.JCheckBox partial;

    //Labels
	private javax.swing.JLabel initLabel;
    private javax.swing.JLabel nodeLabel;
	private javax.swing.JLabel mutationLabel;
    private javax.swing.JLabel probabilityLabel;
    private javax.swing.JLabel partialExplain;
    private javax.swing.JLabel fullExplain;
    private javax.swing.JSeparator line;

    /**
     * Default Contructor
     *
     * @param pMode Gives the dialog context.  If mode == 0, then the dialog creates a
     */
    public DuplicationPanel(int pMode) {
        super(null);
        mode = pMode;
        initComponents();
    }

    /**
     * Default Contructor
     *
     * @param pMode Gives the dialog context.  If mode == 0, then the dialog creates a
     */
    public DuplicationPanel(int pMode, RandomNetworkPanel pPrevious) {
        super(pPrevious);
        mode = pMode;
        initComponents();
	//	Cytoscape.getSwingPropertyChangeSupport().addPropertyChangeListener(this);
     //   Cytoscape.getDesktop().getNetworkViewManager().getSwingPropertyChangeSupport().addPropertyChangeListener(this);

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
        return new String("Duplication Model");
    }

    /**
     *
     */
    public String getDescription() {
        return new String("Generates an network according to the duplication models."
                + "  Either full or partial duplication is allowed."
                + "  Constraints:  0 &#8804; probability &#8804; 1, 0 < Nodes.");
    }

    /**
     * Initialize the components
     */
    private void initComponents() {
        //Create TextFields
        nodeTextField = new javax.swing.JTextField();
        probabilityTextField = new javax.swing.JTextField();
        initTextField = new javax.swing.JTextField();
		mutationTextField = new javax.swing.JTextField();

        //Set up the default values for the textfields
        nodeTextField.setText("" + defaultNodeValue);
        nodeTextField.setHorizontalAlignment(JTextField.RIGHT);
        probabilityTextField.setText("" + defaultProbValue);
		initTextField.setText("" + defaultInitValue);
		initTextField.setHorizontalAlignment(JTextField.RIGHT);	
        probabilityTextField.setHorizontalAlignment(JTextField.RIGHT);
        probabilityTextField.setPreferredSize(new Dimension(50, 25));
        nodeTextField.setPreferredSize(new Dimension(50, 25));
		initTextField.setPreferredSize(new Dimension(50, 25));
		probabilityTextField.setMinimumSize(new Dimension(50, 25));
		mutationTextField.setText("" + defaultMutationtValue);
		mutationTextField.setPreferredSize(new Dimension(50, 25));
		mutationTextField.setMinimumSize(new Dimension(50, 25));

        nodeTextField.setMinimumSize(new Dimension(50, 25));
		initTextField.setMinimumSize(new Dimension(50, 25));	


        //Buttons
        directedCheckBox = new javax.swing.JCheckBox();
        selfEdgeCheckBox = new javax.swing.JCheckBox();
		useCurrentCheckBox = new javax.swing.JCheckBox();
        group = new javax.swing.ButtonGroup();
        full = new javax.swing.JCheckBox();
        partial = new javax.swing.JCheckBox();

        //Labels
        fullExplain = new javax.swing.JLabel();
        partialExplain = new javax.swing.JLabel();
        probabilityLabel = new javax.swing.JLabel();
        nodeLabel = new javax.swing.JLabel();
		initLabel = new javax.swing.JLabel();
		mutationLabel = new javax.swing.JLabel();
		mutationLabel.setText("Mutation Rate: ");
        initLabel.setText("Init. Num. of Nodes (s):");

        //Set up the group
        fullExplain.setOpaque(true);
        partialExplain.setOpaque(true);
        full.addActionListener(this);
		partial.addActionListener(this);
		useCurrentCheckBox.addActionListener(this);
        full.setSelected(true);
        group.add(full);
        group.add(partial);

        JSeparator hLine = new JSeparator(SwingConstants.HORIZONTAL);
        line = new JSeparator(SwingConstants.VERTICAL);

        //Set the text for the labels
		useCurrentCheckBox.setText("Use current network as seed");
        directedCheckBox.setText("Undirected");
        selfEdgeCheckBox.setText("Allow Reflexive Edges (u,u)");
        partial.setText("Partial");
        full.setText("Full");
        nodeLabel.setText("Number of Nodes (n):");
        probabilityLabel.setText("Edge Probability (p):   ");

        //Add the descriptions for each mode
        fullExplain.setText("<html><font size=2 face=Verdana>Generate a network by duplicating a given node with all of its edges "
                + "Nodes are selected by preferential attachement.</font></html>");
        partialExplain.setText("<html><font size=2 face=Verdana>Generate a network by duplicating a given node with some of its edges"
                + "Each edge has probability p to be included.</font></html>");
        fullExplain.setPreferredSize(new Dimension(350, 40));
        partialExplain.setPreferredSize(new Dimension(350, 40));
        fullExplain.setMinimumSize(new Dimension(350, 40));
        partialExplain.setMinimumSize(new Dimension(350, 40));


        //Initially turn off this textfield
        probabilityTextField.setEnabled(false);
        probabilityTextField.setBackground(Color.LIGHT_GRAY);




        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(0, 5, 0, 5);
        c.gridx = 0;
        c.gridy = 0;
        add(full, c);

        c = null;
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(0, 5, 0, 5);
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 5;
        c.weightx = 1;
        c.weighty = 1;
        add(fullExplain, c);

        c = null;
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(0, 5, 0, 5);
        c.gridx = 0;
        c.gridy = 1;
        add(partial, c);

        c = null;
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(15, 5, 0, 5);
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 5;
        c.weightx = 1;
        c.weighty = 1;
        add(partialExplain, c);


        c = null;
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 20;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.weighty = 1;
        add(hLine, c);


        c = null;
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(0, 10, 0, 0);
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 3;
        add(nodeLabel, c);

        c = null;
        c = new GridBagConstraints();
        c.insets = new Insets(0, 10, 0, 0);
        c.anchor = GridBagConstraints.LINE_START;
        c.gridx = 3;
        c.gridy = 3;
        add(nodeTextField, c);


        c = null;
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(0, 10, 0, 10);
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 3;
        add(probabilityLabel, c);

        c = null;
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(0, 10, 5, 10);
        c.gridx = 3;
        c.gridy = 4;
        c.gridwidth = 1;
        c.gridheight = 1;
        add(probabilityTextField, c);
		
		 c = null;
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(0, 10, 0, 10);
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 3;
        add(initLabel, c);

        c = null;
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(0, 10, 5, 10);
        c.gridx = 3;
        c.gridy = 5;
        c.gridwidth = 1;
        c.gridheight = 1;
        add(initTextField, c);
		
		
		
		//////
		c = null;
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(0, 10, 0, 10);
        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 3;
        add(mutationLabel, c);

        c = null;
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(0, 10, 5, 10);
        c.gridx = 3;
        c.gridy = 6;
        c.gridwidth = 1;
        c.gridheight = 1;
        add(mutationTextField, c);
		
		
		//////
	/*
        //add the line
        c = null;
        c = new GridBagConstraints();
        c.insets = new Insets(5, 0, 0, 0);
        c.gridx = 4;
        c.gridheight = 5;
        c.gridy = 2;
        c.fill = GridBagConstraints.VERTICAL;
        add(line, c);
*/



        //Set up the directed checkbox
        c = null;
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(0, 10, 0, 0);
        c.gridx = 0;
        c.gridy = 7;
        c.weightx = 1;
        c.weighty = 1;
        add(directedCheckBox, c);

        //Set up the allow self edge checkbox
        c = null;
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(0, 10, 0, 0);
        c.gridx = 0;
        c.gridy = 7;
        c.weightx = 1;
        c.weighty = 1;
        add(selfEdgeCheckBox, c);

		c = null;
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(0, 10, 0, 0);
        c.gridx = 0;
        c.gridy = 8;
        c.weightx = 1;
        c.weighty = 1;
        add(useCurrentCheckBox, c);

    }

    /**
     * Call back for the generate button
     */
    public RandomNetworkPanel next() {

        // number of nodes
        int numNodes;

        //Edge probability
        double probability = 1;

        //Is directed?
        boolean directed = directedCheckBox.isSelected();

        //Allow reflexive edges?
        boolean allowSelfEdge = selfEdgeCheckBox.isSelected();

		//Use the current network as a starting point
		boolean useCurrentNet = useCurrentCheckBox.isSelected();
	
		DuplicationModel dm = null;

        //Get the string from the node text field
        String numNodeString = nodeTextField.getText();

        //Try to parse the string as an integer
        //If there is an error color the label and exit
        try {
            numNodes = Integer.parseInt(numNodeString);
			
            if (numNodes < 0) {
                throw new Exception("There must be at least one node in the network: " + numNodes);
            }
        } catch (Exception e) {
            nodeLabel.setForeground(java.awt.Color.RED);
            return this;
        }

		double mutationRate = 0;
		try {
			String mutationString = mutationTextField.getText();
            mutationRate = Double.parseDouble(mutationString);
			
            if ((mutationRate < 0)||(mutationRate > 1.0)) {
                throw new Exception("Mutation Rate must be a probability in the range: [0, 1]");
            }
        } catch (Exception e) {
            mutationLabel.setForeground(java.awt.Color.RED);
            return this;
        }

        //If we passed this set the label to black
        nodeLabel.setForeground(java.awt.Color.BLACK);

    
		if(partial.isSelected()){
			//Get the string from the probability text field
            String probabilityString = probabilityTextField.getText();

            //try to parse the string as a double
            //If there is an error color the label and exit
            try {
                probability = Double.parseDouble(probabilityString);
                if ((probability < 0) || (probability > 1)) {
                    throw new Exception("Must be a probability");
                }

            } catch (Exception e) {
                probabilityLabel.setForeground(java.awt.Color.RED);
                return this;
            }

            probabilityLabel.setForeground(java.awt.Color.BLACK);
		}
	
		
	
        //Create a mode
       if(!useCurrentNet)
	   {
			int numInit = 0;
			
			//Get the string from the probability text field
            String initialString = initTextField.getText();

            //try to parse the string as a double
            //If there is an error color the label and exit
            try {
                numInit = Integer.parseInt(initialString);
                if ((numInit < 2)) {
                    throw new Exception("Initial > 2");
                }
            } catch (Exception e) {
                initLabel.setForeground(java.awt.Color.RED);
                return this;
            }
//			System.out.println("Num INIT:" + numInit);
            initLabel.setForeground(java.awt.Color.BLACK);
			BarabasiAlbertModel bam = new BarabasiAlbertModel(numInit, allowSelfEdge, !directed, 3, 2);
			RandomNetworkImpl graph = bam.generate();
			dm = new DuplicationModel(numNodes, probability,mutationRate, graph, !directed, allowSelfEdge);
		}
		else
		{			  
			/*
			CyNetwork net = Cytoscape.getCurrentNetwork();
			if(net == null)
			{
				useCurrentCheckBox.setForeground(java.awt.Color.RED);
				return this;
			}
			RandomNetwork random_network = new RandomNetwork(net, !directed);
			dm = new DuplicationModel(numNodes, probability, mutationRate, random_network, !directed, allowSelfEdge);
			*/
		}
		
		useCurrentCheckBox.setForeground(java.awt.Color.BLACK);
        
        if (mode == 1) {
            dm.setCreateView(false);
            if (mNext == null) {
                mNext = new AnalyzePanel(this, dm, dm.getDirected());
            } else {
                ((AnalyzePanel) mNext).setDirected(dm.getDirected());
                ((AnalyzePanel) mNext).setGenerator(dm);
            }
            return mNext;
        }


        //Generate the network
        RandomNetworkImpl graph = dm.generate();
        CyNetwork network = ConverterUtils.toCyNetwork(graph);
        graph = null;
        return this;
    }

    /*
     * Call back for when one of the gnm or gnp buttons is selected
     *
     */
    public void actionPerformed(ActionEvent e) {
        //When pushed turn off one of the three
        //TextFields, according to the model that was selected
        if ("Full".equals(e.getActionCommand())) {
			probabilityTextField.setText("1.0");
            probabilityTextField.setEnabled(false);
            probabilityTextField.setBackground(Color.LIGHT_GRAY);
        } else if ("Partial".equals(e.getActionCommand())) {
		    probabilityTextField.setText(".75");
            probabilityTextField.setEnabled(true);
            probabilityTextField.setBackground(Color.WHITE);
        }
		else if("Use current network as seed".equals(e.getActionCommand())) {
			if(useCurrentCheckBox.isSelected()){
			//	CyNetwork net = Cytoscape.getCurrentNetwork();				
			//	nodeTextField.setText((net.getNodeCount() * 2) + "");
				initTextField.setEnabled(false);
				initTextField.setBackground(Color.LIGHT_GRAY);
			}
			else{
				nodeTextField.setText(""+ defaultNodeValue);
				initTextField.setEnabled(true);
				initTextField.setBackground(Color.WHITE);			
			}
		}
    }
	
	 /**
     *
     */
    public void propertyChange(PropertyChangeEvent event) {
       if(useCurrentCheckBox.isSelected()){
			//	CyNetwork net = Cytoscape.getCurrentNetwork();				
			//	nodeTextField.setText((net.getNodeCount() * 2) + "");
			}   
		}
	
}