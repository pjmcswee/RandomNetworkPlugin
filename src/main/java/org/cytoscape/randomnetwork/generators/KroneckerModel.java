 /* 
 File: KroneckerModel.java
 Author: Patrick J. McSweeney (pjmcswee@syr.edu)
 Copyright (c) 2006, 2007, The Cytoscape Consortium (www.cytoscape.org)

Journal of Machine Learning Research 11 (2010) 985-1042 Submitted 12/08; Revised 8/09; Published 2/10
Kronecker Graphs: An Approach to Modeling Networks
Jure Leskovec JURE@CS.STANFORD.EDU
Computer Science Department
Stanford University
Stanford, CA 94305
Deepayan Chakrabarti DEEPAY@YAHOO-INC.COM
Yahoo! Research
2821 Mission College Blvd
Santa Clara, CA 95054
Jon Kleinberg KLEINBER@CS.CORNELL.EDU
Computer Science Department
Cornell University
Ithaca, NY 14853
Christos Faloutsos CHRISTOS@CS.CMU.EDU
Computer Science Department
Carnegie Mellon University
Pittsburgh, PA 15213
Zoubin Ghahramani ZOUBIN@ENG.CAM.AC.UK
Department of Engineering
University of Cambridge
Cambridge CB2 1PZ, UK
Editor: Fan Chung


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

package org.cytoscape.randomnetwork.generators;

import java.util.*;

import org.cytoscape.randomnetwork.RandomNetworkImpl;


/**
 * This class generates random networks according to the two Erdos-Renyi models.
 * G(n,m) which generates nodes with a specific number of edges <br>
 * G(n,p) which generates a random network where each edge has the probability p of existing<br>
 *
 * References: <br>
 * Erdos, P.; Renyi A. (1959). "On Random Graphs. I.".  Publications Matheaticae 6: 290-297.<br>
 * Erdos, P.; Renyi A. (1960). "The Evolution of Random Graphs".  Magyar Tud. Akad. Math. Kutato INt. Koxl. 5:17-61.<br>
 * Gilber, E.N. (1959). "Random Graphs".  Annals of Mathematical Statistics 30: 1141 - 1144.<br>
 *
 * @author Patrick J. McSweeney
 * @version 1.0
 */
public class KroneckerModel extends RandomNetworkModel {
	private double seedMatrix[][] =  {{.9,.5},{.5,.1}};
	private int iter = 5;

	/**
	 * Creates a model for constructing random graphs according to the
	 * erdos-renyi model. This constructor will create random graphs with a
	 * given number of edges. Each call to generate will create networks with
	 * the specified number of edges. G(n,m) model.
	 * 
	 * @param pNumNodes  The number of nodes in generated networks.
	 * @param pNumEdges  The number of edges in generated networks.
	 * @param pDirected  Generated networs are directed(TRUE) or undirected(FALSE).
	 * 
	 */
	public KroneckerModel(boolean pAllowSelfEdge, boolean pDirected, double[][] pSeedMatrix, int pIter) {
		super((int)Math.pow(2,pIter),(int)Math.pow(2,pIter), pAllowSelfEdge, pDirected);
		seedMatrix = pSeedMatrix;
		iter = pIter;
	}


	
	
	/**
	 *  Copies the RandomNetworkGenerator.
	 *  This is function is necessary to give each thread its own instance of the RandomNetworkGenerator.
	 *
	 * @return The copy of the calling ErdosRenyiModel
	 */
	public RandomNetworkGenerator copy()
	{
		return new KroneckerModel(allowSelfEdge, directed, seedMatrix, iter);
	}


	/**
	 * @return Gets the display name for this generator.
	 */
	public String getName()
	{
		return new String("Kronecker Model");
	}

	

	/**
	 * Generates a random graph based on the model specified by the constructor:
	 * G(n,m) or G(n,p)
	 *
	 *
	 * @return The generated network, whose properties are specified by member variables.
	 */
	public RandomNetworkImpl generate2() {
		Random rand = new Random();
		
		//Create a new Random Network
		RandomNetworkImpl random_network =  new RandomNetworkImpl(directed);

		//Set the name for the random network
		random_network.setNetworkName(getName());

		// Create N nodes
		int[] nodes = new int[numNodes];
		
		// For each node
		for (int i = 0; i < numNodes; i++) 
		{
			// Create a new node nodeID = i, create = true
			nodes[i] = random_network.createNode();
		}
		
		LinkedList<Integer> existingEdges = new LinkedList<Integer>();
		ArrayList<double[]> probToRCPosV  = new ArrayList<double[]>();
		// prepare cell probability vector
		double sum = 0;
		for (int row = 0; row < seedMatrix.length; row++) 
		{
			for (int col = 0; col < seedMatrix.length; col++) 
			{
				sum += seedMatrix[row][col];
			}
		}
		
		double cumProb = 0.0;
		for (int row = 0; row < seedMatrix.length; row++) 
		{
			for (int col = 0; col < seedMatrix.length; col++) 
			{
				double prob = seedMatrix[row][col];
				if (prob > 0.0) 
				{
					cumProb += prob;
					probToRCPosV.add(new double[]{cumProb/sum, row, col});
				}
			}
		}		
		
		int rng, row, col;
		for (int edges = 0; edges < numNodes; ) 
		{
			rng = numNodes;  
			row = 0;  
			col = 0;
			
			for (int i = 0; i < iter; i++) 
			{
				double p = rand.nextDouble();

				int index = 0; 
				while(p > probToRCPosV.get(index)[0]) 
				{   index++; }
				
				int MtxRow = (int)probToRCPosV.get(index)[1];
				int MtxCol = (int)probToRCPosV.get(index)[2];
				rng /= seedMatrix.length;
				row += MtxRow * rng;
				col += MtxCol * rng;
			}
			
			//If the edge does not exist
			if (!existingEdges.contains(row * numNodes + col))
			{ 
				// allow self-loops
				if ((allowSelfEdge) || (row != col))
				{					
					if(!directed)
					{
						if(!existingEdges.contains(col * numNodes + row))
						{
							existingEdges.add(col * numNodes + row);
							random_network.createEdge(nodes[row], nodes[col], directed);				
						}
					}
					else
					{
						random_network.createEdge(nodes[row], nodes[col], directed);				
					}
				}		
				edges++;	
				
				existingEdges.add(row * numNodes + col);		
				
						
			}
		}														
		//Return this network
		return random_network;
	}	
	
	
	
	public boolean isEdgePlace(double[][] seed, int NId1, int NId2, int pIters,  double ProbTresh)  
	{
		int dim = seedMatrix.length;
		double Prob = 1.0;
		for (int level = 0; level < pIters; level++) 
		{
			Prob *= seed[NId1 % dim][NId2 % dim];
			
			if (ProbTresh > Prob) 
			{	
				return false; 
			}
			NId1 /= dim;  
			NId2 /= dim;
		}
		return true;
	}
	
	
	public RandomNetworkImpl generate() {
		Random rand = new Random();
		
		//Create a new Random Network
		RandomNetworkImpl random_network =  new RandomNetworkImpl(directed);

		//Set the name for the random network
		random_network.setNetworkName(getName());

		// Create N nodes
		int[] nodes = new int[numNodes];
		
		// For each node
		for (int i = 0; i < numNodes; i++) 
		{
			// Create a new node nodeID = i, create = true
			nodes[i] = random_network.createNode();
		}
		
		for (int i = 0; i < numNodes; i++) {
			int start = 0;
			if(!directed)
			{
				start = i+1;
			}
			
			for (int j = start; j < numNodes; j++) {
				if((i!= j) || (allowSelfEdge))
				{
					if (isEdgePlace(seedMatrix,i, j, iter, rand.nextDouble())) {
						random_network.createEdge(nodes[i], nodes[j], directed);		
					}
				}
			}
      
		}
		return random_network;
	}
   	
	
}