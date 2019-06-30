/* 
File: GeometricNetworkModel.java
Author: Patrick J. McSweeney (pjmcswee@syr.edu)
Copyright (c) 2006, 2007, The Cytoscape Consortium (www.cytoscape.org)
Penrose, Mathew: Random Geometric Graphs (Oxford Studies in Probability, 5), 2003.

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

import org.cytoscape.randomnetwork.RandomNetworkImpl;

/**
 *	
 */
public class GeometricNetworkModel extends RandomNetworkModel {

    /**  Radius used.*/
    private double mRadius;
    /**	The dimension for the sytem. */
    private int mDimension;

    /**
     *
     */
    public GeometricNetworkModel(int pNumNodes, boolean pAllowSelfEdge,
            boolean pDirected, double pRadius, int pDimension) {
        super(pNumNodes, UNSPECIFIED, pAllowSelfEdge, pDirected);
        mRadius = pRadius;
        mDimension = pDimension;
    }

    /**
     * Creats a copy of the RandomNetworkGenerator.  Used to give each thread their own
     * copy of the generator.
     *
     * @return Returns a copy of this generator
     */
    public RandomNetworkGenerator copy() {
        return new GeometricNetworkModel(numNodes, allowSelfEdge, directed, mRadius, mDimension);
    }

    /**
     * @return Gets the display name for this generator.
     */
    public String getName() {
        return new String("Geometric Network Model");
    }

    /**
     *  Generates random networks according to the Geometric Network model.
     * <br>
     *
     * @return The generated random network
     */
    public RandomNetworkImpl generate() {

        //Create the random graph
        RandomNetworkImpl random_network = new RandomNetworkImpl(directed);

        random_network.setNetworkName(getName());

        //Keep track of the number
        numEdges = 0;

        // Create N nodes
        int[] nodes = new int[numNodes];


        double[][] positions = new double[numNodes][mDimension];

        // For each edge
        for (int i = 0; i < numNodes; i++) {
            // Save node in array
            nodes[i] = random_network.createNode();

            for (int j = 0; j < mDimension; j++) {
                positions[i][j] = Math.abs(random.nextDouble() * 2) - 1.0d;
            }
        }

        //for all pairs of nodes
        for (int i = 0; i < numNodes; i++) {
            for (int j = (i + 1); j < numNodes; j++) {
                double distance = 0;
                for (int k = 0; k < mDimension; k++) {
                    distance += Math.pow(positions[i][k] - positions[j][k], 2);
                }
                distance = Math.sqrt(distance);

                if (distance <= mRadius) {
                    if (directed) {
                        if (random.nextBoolean()) {
                            random_network.createEdge(nodes[i], nodes[j], directed);
                        } else {
                            random_network.createEdge(nodes[j], nodes[i], directed);
                        }
                    } else {
                        random_network.createEdge(nodes[i], nodes[j], directed);
                    }
                }
            }
        }

        //return the result
        return random_network;
    }
}