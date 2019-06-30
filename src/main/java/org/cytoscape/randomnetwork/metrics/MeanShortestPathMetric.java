/* 
File: MeanShortestPath.java
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

import java.util.Set;

import org.cytoscape.randomnetwork.RandomNetwork;

//import cytoscape.util.intr.IntEnumerator;

/**
 *  Computes the avearge shortest path between all pairs of nodes (i,j), where
 *  i != j.
 *  <p>
 *  The algorithm is an implementation of Dijkstra's algorithm, but does not keep the entire adjacency matrix for memory use.
 *
 * @author Patrick J. McSweeney
 * @version 1.0
 */
public class MeanShortestPathMetric implements NetworkMetric {

    /**
     *
     */
    class MeanShortestPathResult implements NetworkMetricResult {

        String[] mNames = {"Diameter", "Mean Shortest Path", "# of Isolated nodes", "# of Shortest Paths", "Self-Loops", "Radius"};
        double[] mValues;

        /**
         *
         */
        MeanShortestPathResult(double pDiameter, double pMeanShortestPath, double pIsolated, double pShortest, double self, double radius) {
            mValues = new double[getNumberOfResults()];
            mValues[0] = pDiameter;
            mValues[1] = pMeanShortestPath;
            mValues[2] = pIsolated;
            mValues[3] = pShortest;
            mValues[4] = self;
            mValues[5] = radius;
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
            return 6;
        }
    }

    /**
     *
     *@return A new MeanShortestPathMetric instance.
     */
    public NetworkMetric copy() {
        return new MeanShortestPathMetric();
    }

    /**
     * Computes the average distance between all pairs of nodes in the network.  This
     * is an implmentation of Dijkstra's algorithm (runs N^3).
     *
     *
     *
     * @param pNetwork The network to compute the average distances over.
     * @param pDirected Treat this network as directed or not.
     * @return The average all-pairs graph distance of nodes in the network.
     *
     */
    public NetworkMetricResult analyze(RandomNetwork pNetwork, boolean pDirected) {
        // Accumlate the distances between all nodes
        double averageShortestPath = 0;

        //Use as the number of nodes in the network
        int N = pNetwork.getNumNodes();

        //Network diameter
        double diameter = 0;

        //Number of isolated nodes
        double isolated = 0;

        //Number of connected components
        double connected = 0;

        //Number of unreachable paths
        int invalidPaths = 0;

        int selfLoops = 0;

        //Shortest non-zero eccentricity
        int radius = Integer.MAX_VALUE;

        //Below is an implementation of Dijkstra's algorithm
        //Everything above creates the adjacency matrix
        for (int i = 0; i < N; i++) {
            //determine the distance from i to every other node
            int distance[] = new int[N];
            //Keep track of which nodes have been used so far
            boolean used[] = new boolean[N];

            //Initialize the variables for every node
            for (int j = 0; j < N; j++) {
                //Pretened the distanc is infinite
                distance[j] = Integer.MAX_VALUE;
            }

            Set<Integer> edges = pNetwork.edgesAdjacent(i, pDirected, pDirected, !pDirected);
            if (edges.size() == 0) {
                isolated++;
            }

            //Iterate through all of thie nodes in this network
            edges = pNetwork.edgesAdjacent(i, pDirected, false, !pDirected);


            //while (edgeIterator.numRemaining() > 0) {
                //Get the next edge
              //  int edgeIndex = edgeIterator.nextInt();
            for (Integer edgeIndex : edges) {

                //Find the other side of this edge
                int neighborIndex = pNetwork.edgeSource(edgeIndex);

                //If we got the wrong side
                if (neighborIndex == i) {
                    //grab the other side
                    neighborIndex = pNetwork.edgeTarget(edgeIndex);
                }

                if (neighborIndex == i) {
                    selfLoops++;
                }
                //set its distance as 1
                distance[neighborIndex] = 1;
            }

            //Nodes that can be used on a path from i to j
            //can only be used if the distance from i to k is
            //less than or equal to allowed
            for (int allowed = 1; allowed < N; allowed++) {
                //Find the closest node
                int min = Integer.MAX_VALUE;
                int index = 0;
                for (int j = 0; j < N; j++) {
                    if ((min > distance[j]) && (!used[j])) {
                        min = distance[j];
                        index = j;
                    }
                }

                //Mark the closest node as used
                used[index] = true;

                Set<Integer> adjIterator = pNetwork.edgesAdjacent(index, pDirected, false, !pDirected);
                //while (adjIterator.numRemaining() > 0) {

                    //Get the next edge
                    //int edgeIndex = adjIterator.nextInt();
                for (Integer edgeIndex :adjIterator) {
                    //Find the other side of this edge
                    int k = pNetwork.edgeSource(edgeIndex);

                    //If we got the wrong side
                    if (k == index) {
                        //grab the other side
                        k = pNetwork.edgeTarget(edgeIndex);
                    }

                    //If k was not used
                    if (!used[k]) {
                        //recalculate the distances to
                        //connected nodes
                        int sum = distance[index] + 1;
                        if (sum < distance[k]) {
                            distance[k] = sum;
                        }
                    }
                }
            }


            int eccentricity = 0;
            //Add the distances to the total sum
            for (int j = 0; j < N; j++) {
                //Don't add the distance from a node to another node
                if (i != j) {
                    ///Make sure that the value is real (connceted)
                    if ((distance[j] < Integer.MAX_VALUE) && (distance[j] > 0)) {
                        averageShortestPath += distance[j];
                        diameter = Math.max(diameter, distance[j]);
                        eccentricity = Math.max(distance[j], eccentricity);

                    } else {
                        //keep track of the number paths that are invalid
                        invalidPaths++;
                    }
                }
            }

            if (eccentricity > 0) {
                radius = Math.min(radius, eccentricity);
            }

        }

        //return the average distance between nodes,
        return new MeanShortestPathResult(diameter, averageShortestPath / ((double) (N * (N - 1.0d) - invalidPaths)), isolated, (N * (N - 1) - invalidPaths), selfLoops, radius);
    }
}