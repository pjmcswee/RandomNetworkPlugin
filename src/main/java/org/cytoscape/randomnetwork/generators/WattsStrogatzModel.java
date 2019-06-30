/* File: WattsStrogatzModel.java
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

//import cytoscape.util.intr.IntEnumerator;
import java.util.*;

import org.cytoscape.randomnetwork.RandomNetworkImpl;

/**
 * This model is used to create random networks with the small world property. A
 * regular lattics is formed, and then randomly perturbed according to the
 * variable beta. Creates a linear interpolation between an erdos-renyi graph
 * and a regular lattics.
 *
 *
 * References: Watts, D.J.; Strogatz, S.H. (1998). "Collective dynamics of
 * 'small-world' networks.". Nature 393 (6684): 409�10. doi:10.1038/30918.
 */
public class WattsStrogatzModel extends RandomNetworkModel {

	/**
	 * Used to linearly interpolate between lattice and erdos-renyi graph
	 */
	private double beta;
	/**
	 * The number of edges to add
	 */
	private int degree;

	/**
	 * Creates a model for constructing random graphs according to the
	 * watts-strogatz model.
	 *
	 * @param pNumNodes
	 *            The number of nodes in generated networks.
	 * @param pDirected
	 *            Specifices if generated networks are directed(true) or
	 *            undirected(false)
	 * @param pBeta
	 *            Interpolates between erdos-renyi graph and a regular lattice.
	 *
	 */
	public WattsStrogatzModel(int pNumNodes, boolean pAllowSelfEdge, boolean pDirected, double pBeta, int pDegree) {
		super(pNumNodes, UNSPECIFIED, pAllowSelfEdge, pDirected);
		degree = pDegree;
		beta = pBeta;
	}

	/**
	 * Creats a copy of the RandomNetworkGenerator. Used to give each thread their
	 * own copy of the generator.
	 *
	 * @return Returns a copy of this generator
	 */
	public RandomNetworkGenerator copy() {
		return new WattsStrogatzModel(numNodes, allowSelfEdge, directed, beta, degree);
	}

	/**
	 * @return Gets the display name for this generator.
	 */
	public String getName() {
		return new String("Watts-Strogatz Model");
	}

	/**
	 * Generates random networks according to the Watts-Strogatz Model. <br>
	 * The algorithm works in two phases:<br>
	 * (1) Create a regular lattice, where each node is connected to its
	 * (degree)-many nearest neighbors. (2) Perturb each edge created in step (1)
	 * with probability Beta. Notices that we do not add any edges in this step.
	 *
	 * @return The generated random network
	 */
	public RandomNetworkImpl generate() {

		// Create the random graph
		RandomNetworkImpl random_network = new RandomNetworkImpl(directed);

		random_network.setNetworkName(getName());

		// Keep track of the number
		numEdges = 0;

		// Create N nodes
		int[] nodes = new int[numNodes];

		// For each edge
		for (int i = 0; i < numNodes; i++) {
			// Save node in array
			nodes[i] = random_network.createNode();
		}

		// Create a linked list to store edges
		// as we create edges and then change them
		// with probability beta

		// Make sure that the degree chosen is feasible
		if (directed && allowSelfEdge && (2 * degree > numNodes)) {
			degree = numNodes / 2;
		} else if (directed && (2 * degree > (numNodes - 1) / 2)) {
			degree = (numNodes - 1) / 2;
		}

		// Make sure that the degree chosen is feasible
		if (!directed && allowSelfEdge && (2 * degree > numNodes)) {
			degree = numNodes / 2;
		} else if (!directed && (2 * degree > (numNodes - 1) / 2)) {
			degree = (numNodes - 1) / 2;
		}

		// for all pairs of nodes
		for (int i = 0; i < numNodes; i++) {
			int start = i - degree;
			if (start < 0) {
				start = numNodes + start;
			}

			int count = 0;
			int stop = 2 * degree;

			while (count < stop) {
				if ((i != start) || (allowSelfEdge)) {
					if (((!directed) && (nodes[i] <= nodes[start])) || (directed)) {
						random_network.createEdge(nodes[i], nodes[start], directed);
						numEdges++;
					}
					if (i != start) {
						count++;
					}
				}
				start = (start + 1) % numNodes;
			}
		}

		// Save a local copy of the edge indices
		List<Integer> edgeList = new ArrayList<Integer>();
		Set<Integer> edges = random_network.edges();
		for (Integer edge : edges) {
			edgeList.add(edge);
		}

		// For each edge
		while (edgeList.size() > 0) {
		//for (Integer nextEdge : edges) {
			// Get the next edge
			int nextEdge = edgeList.remove(0);
			int source = random_network.edgeSource(nextEdge);
			int target = random_network.edgeTarget(nextEdge);

			// Throw a random dart
			double percent = random.nextDouble();

			// If this should be shuffled
			if (percent <= beta) {

				// Choose a new node
				int k = 0;
				boolean candidate = false;

				// Keep looping until we have a suitable candidate
				while (!candidate) {
					// choose a new node
					k = Math.abs(random.nextInt() % numNodes);
					// reset variable for this pass
					candidate = true;

					// Make sure we are not create a self loop
					candidate = candidate && (nodes[k] != source) || (allowSelfEdge);
					candidate = candidate && (nodes[k] != target);

					// Check to see if this edge already exists
					int edgeCount = 0;
					Set<Integer> adjacent = random_network.edgesAdjacent(source, directed, false, !directed);
					for (Integer edgeIndex : adjacent) {
						int neighbor = random_network.edgeTarget(edgeIndex);
						if (neighbor == source) {
							neighbor = random_network.edgeSource(edgeIndex);
						}

						// If this edge already exists
						candidate = candidate && (neighbor != nodes[k]);
						edgeCount++;
					}

					// If edgeCount indicates that this node is connected to all other nodes than we
					// should stop trying
					// there is no way to switch it (no node that we are not connected to).
					if ((edgeCount == numNodes) && (allowSelfEdge)) {
						candidate = false;
						break;
					} else if ((edgeCount == (numNodes - 1)) && (!allowSelfEdge)) {
						candidate = false;
						break;
					}

				}

				// If this is still a candidate, then swap the edges
				if (candidate) {
					
					// Switch in the new Edge
					random_network.removeEdge(nextEdge);
					random_network.createEdge(source, nodes[k], directed);

				}
			}
		}

		// return the result
		return random_network;
	}
}
