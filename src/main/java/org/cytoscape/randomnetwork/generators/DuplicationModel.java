/* File: DuplicaionModel.java
Author: Patrick J. Mcsweeney (pjmcswee@syr.edu)
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
package org.cytoscape.randomnetwork.generators;

import java.util.*;

import org.cytoscape.randomnetwork.RandomNetworkImpl;


/**
 *  Barabasi-Albert Model: Creates random networks with the scale-free property.  There are many
 *  nodes with few edges, and only few nodes with many edges.
 *  The algorithm uses the preferential attachment property.  
 * 
 * References: Albert-Laszlo Barabasi & Reka Albert (October 1999).
 *			  "Emergence of scaling in random networks". 
 *			   Science 286: 509�512. doi:10.1126/science.286.5439.509.
 *
 *
 */
public class DuplicationModel extends RandomNetworkModel {

    /**
     * The number of initial nodes.
     */
    private RandomNetworkImpl mInitial;

    /**
     * The number edges to add at each time step.
     */
    private double probability;
    private double mutation;

    /**
     * Creates a model for constructing random graphs according to the
     * Barabasi-Albert model.
     *
     * @param pNumNodes The number of nodes in generated networks
     * @param pDirected Specifice if the generated networks are directed(true) or undirected(false)
     * @param pInit The number of nodes in the seed network.
     * @param pProbability 1 = full, 0<= p < 1 = Parial
     */
    public DuplicationModel(int pNumNodes, double pProbability, double pMutation, RandomNetworkImpl pInit, boolean pDirected, boolean pAllowSelfEdge) {
        super(pNumNodes, UNSPECIFIED, pAllowSelfEdge, pDirected);
		
        mInitial = pInit;
        probability = pProbability;
		mutation = pMutation;
    }

    /**
     * Creates a copy of the RandomNetworkModel.  Required to give each thread its own copy of the generator.
     * @return A copy of the BarabasiAlbertModel
     */
    public DuplicationModel copy() {
        return new DuplicationModel(numNodes, probability, mutation, mInitial, directed, allowSelfEdge);
    }

    /**
     * @return Gets the display name for this generator.
     */
    public String getName() {
        return new String("Duplication Model");
    }

    /**
     *  Generate a network according to the DuplicationModel.
     *
     *
     * @return The generated random network.
     */
    public RandomNetworkImpl generate() {

        RandomNetworkImpl random_network = new RandomNetworkImpl(directed);
        random_network.setNetworkName(getName());

        // Create N nodes
        int[] nodes = new int[numNodes];
        // For each node
        for (int i = 0; i < numNodes; i++) {
            // Save node in array
            nodes[i] = random_network.createNode();
        }
        numEdges = 0;
		int oldN = 1;
/*
        //Copy over the intial nodes!
        //Iterate through all of thie nodes in this network
        IntEnumerator nodeIterator = mInitial.nodes();
        int oldN = nodeIterator.numRemaining();
        //Do pre-processing for each node
        while (nodeIterator.numRemaining() > 0) {

            //Get the next node
            int nodeIndex = nodeIterator.nextInt();

            //Iterate through all of this node's  edges if directed
            //or all edges if the network is undirected.
            IntEnumerator edgeIterator = mInitial.edgesAdjacent(nodeIndex, directed, false, !directed);
            while (edgeIterator.numRemaining() > 0) {
                //Get the next edge
                int edgeIndex = edgeIterator.nextInt();

                //Find the other side of this edge
                int neighborIndex = mInitial.edgeTarget(edgeIndex);

                //If we got the wrong side
                if (neighborIndex == nodeIndex) {
                    //grab the other side
                    neighborIndex = mInitial.edgeSource(edgeIndex);
                }
                random_network.edgeCreate(nodes[nodeIndex], nodes[neighborIndex], directed);
                numEdges++;
            }
        }*/

        //
        for (int i = oldN; i < numNodes; i++) {
            //Chose the node to diplicate
            int chosen = (int) Math.abs(random.nextInt()) % i;

            LinkedList<Integer> connected = new LinkedList<Integer>();

            //Iterate over all of the edges of this node
            for (Integer edgeIndex : random_network.edgesAdjacent(nodes[chosen], directed, false, !directed)) {
            //IntEnumerator edgeIterator = random_network.edgesAdjacent(nodes[chosen], directed, false, !directed);
            //while (edgeIterator.numRemaining() > 0) {
                //Throw a random dart
                double r = Math.abs(random.nextDouble());
                //Get the next edge
                //int edgeIndex = edgeIterator.nextInt();
                //If the dart is under the probability
                if (r <= probability) {

                    //Find the other side of this edge
                    int neighborIndex = random_network.edgeTarget(edgeIndex);

                    //If we got the wrong side
                    if (neighborIndex == nodes[chosen]) {
                        //grab the other side
                        neighborIndex = random_network.edgeSource(edgeIndex);
                    }
                    connected.add(neighborIndex);
                    random_network.createEdge(nodes[i], nodes[neighborIndex], directed);
                    numEdges++;
                }
            }
            for (int j = 0; j < i; j++) {
                double m = Math.abs(random.nextDouble());
                if (m < (mutation / (double)i)) {
                    //if(!connected.contains(j))
                    {
                        //System.out.println("mutation: " +i +"\t" + j);
                        random_network.createEdge(nodes[i], nodes[j], directed);
                    }
                }
                //System.out.println(i + "\t" + j);
            }


            /*
            if(directed)
            {
            IntEnumerator inEdgeIterator = random_network.edgesAdjacent(nodes[chosen], false, directed, false);
            while (inEdgeIterator.numRemaining() > 0) {
            //Throw a random dart
            double r = Math.abs(random.nextDouble());
            //Get the next edge
            int edgeIndex = inEdgeIterator.nextInt();
            //If the dart is under the probability
            if( r <= probability){
            //Find the other side of this edge
            int neighborIndex = random_network.edgeSource(edgeIndex);
            random_network.edgeCreate(nodes[i], nodes[neighborIndex], directed);
            numEdges++;
            }
            }
            }
             */

        }

        return random_network;
    }
}
