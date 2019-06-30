/* 
File: ConnectedComponentMetric.java
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
public class ConnectedComponentMetric implements NetworkMetric {

    /**
     *
     */
    class ConnectedComponentResult implements NetworkMetricResult {

        String[] mNames = {"# Connected Components", "Largest Connected Comp."};
        double[] mValues;

        /**
         *
         */
        ConnectedComponentResult(double pConnected, double pLargest) {
            mValues = new double[getNumberOfResults()];
            mValues[0] = pConnected;
            mValues[1] = pLargest;
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
            return 2;
        }
    }

    /**
     *
     *@return A new ConnectedComponentMetric instance.
     */
    public NetworkMetric copy() {
        return new ConnectedComponentMetric();
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
        //Accumlate the distances between all nodes
        double connected = 0;
        double largest = 0;

        //Use as the number of nodes in the network
        int N = pNetwork.getNumNodes();

        int[] color = new int[N];
        for (int i = 0; i < N; i++) {
            if (color[i] == 0) {
                connected++;
                int size = 0;

                LinkedList<Integer> toExplore = new LinkedList<Integer>();
                toExplore.add(new Integer(i));
                color[i] = 1;

                while (!toExplore.isEmpty()) {
                    Integer node = toExplore.remove();
                    size++;
                    
                    Set<Integer> adjacentNodes = pNetwork.edgesAdjacent(node.intValue(), pDirected, pDirected, !pDirected);
                    for (Integer edgeIndex : adjacentNodes) {
                    //while (adjIterator.numRemaining() > 0) {
                        //Get the next edge
                       // int edgeIndex = adjIterator.nextInt();

                        //Find the other side of this edge
                        int k = pNetwork.edgeSource(edgeIndex);

                        //If we got the wrong side
                        if (k == node.intValue()) {
                            //grab the other side
                            k = pNetwork.edgeTarget(edgeIndex);
                        }

                        if (color[k] == 0) {
                            toExplore.add(new Integer(k));
                            color[k] = 1;
                        }
                    }
                    color[node] = 2;
                }

                if (size > largest) {
                    largest = size;
                }
            }
        }

        //return the result
        return new ConnectedComponentResult(connected, largest);
    }
}
