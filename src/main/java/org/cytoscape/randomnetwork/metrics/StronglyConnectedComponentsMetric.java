/* 
File: StronglyConnectedComponentMetric.java
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
package org.cytoscape.randomnetwork.metrics;

import java.util.*;

import org.cytoscape.randomnetwork.RandomNetwork;


/**
 *
 * @author Patrick J. McSweeney
 * @version 1.0
 */
public class StronglyConnectedComponentsMetric implements NetworkMetric {

    /**
     *
     */
    class StronglyConnectedComponentsResult implements NetworkMetricResult {

        String[] mNames = {"# Strongly Connected Components"};
        double[] mValues;

        /**
         *
         */
        StronglyConnectedComponentsResult(double pConnected) {
            mValues = new double[getNumberOfResults()];
            mValues[0] = pConnected;
        }

        /**
         * @return the double value of this result.
         */
        public double getResult(int pIndex) {
            return mValues[pIndex];
        }

        /**
         * @return the name of this result.
         */
        public String getName(int pIndex) {
            return mNames[pIndex];
        }

        /**
         * @return the number of the values.
         */
        public int getNumberOfResults() {
            return 1;
        }
    }

    /**
     *
     *@return A new ConnectedComponentMetric instance.
     */
    public NetworkMetric copy() {
        return new StronglyConnectedComponentsMetric();
    }

    /**
     *
     *
     *
     * @param pNetwork The network to compute the average distances over.
     * @param pDirected Treat this network as directed or not.
     * @return The average all-pairs graph distance of nodes in the network.
     *
     */
    public NetworkMetricResult analyze(RandomNetwork pNetwork, boolean pDirected) {
        //Use as the number of nodes in the network
        int N = pNetwork.getNumNodes();

        //Accumlate the distances between all nodes
        double strongly = 0;
        int[] indicies = new int[N];
        int[] low_indicies = new int[N];
        int index = 0;
        for (int i = 0; i < N; i++) {
            indicies[i] = -1;
            low_indicies[i] = -1;
        }

        for (int i = 0; i < N; i++) {
            if (indicies[i] == -1) {
				LinkedList<Integer> stack = new LinkedList<Integer>();
                strongly += tarjan(pNetwork, i, index, indicies, low_indicies, stack, pDirected);
            }
        }

        //return the result
        return new StronglyConnectedComponentsResult(strongly);
    }

    /**
     *
     *
     */
    public int tarjan(RandomNetwork pNetwork, int node, int index, int[] indicies, int[] low_indicies, LinkedList<Integer> stack, boolean pDirected) {
        indicies[node] = index;
        low_indicies[node] = index;
        index++;
        stack.addFirst(node);
        int components = 0;

        Set<Integer> adjIterator = pNetwork.edgesAdjacent(node, pDirected, false, false);
        //while (adjIterator.numRemaining() > 0) {
            //Get the next edge
          //  int edgeIndex = adjIterator.nextInt();
        for (Integer edgeIndex : adjIterator) {
            //Find the other side of this edge
            int k = pNetwork.edgeSource(edgeIndex);

            //If we got the wrong side
            if (k == node) {
                //grab the other side
                k = pNetwork.edgeTarget(edgeIndex);
            }
            if (indicies[k] == -1) {
                components += tarjan(pNetwork, k, index, indicies, low_indicies, stack, pDirected);
                low_indicies[node] = Math.min(low_indicies[node], low_indicies[k]);
            } else if (stack.contains(k)) {
                low_indicies[node] = Math.min(low_indicies[node], indicies[k]);
            }
        }
        if (low_indicies[node] == indicies[node]) {
			int next = -1;
			while((next = stack.removeFirst()) != node);
			
            components++;
        }
        return components;
    }
}
