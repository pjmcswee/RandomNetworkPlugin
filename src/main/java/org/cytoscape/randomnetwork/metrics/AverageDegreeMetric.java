/* 
File: AverageDegreeMetric.java
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


/**
 *  This NetworkMetric determines the ratio of edges over nodes.
 *
 * @author Patrick J. McSweeney
 * @version 1.0
 */
public class AverageDegreeMetric implements NetworkMetric {

    /**
     *
     */
    class AverageDegreeResult implements NetworkMetricResult {

        String[] mNames = {"Average Degree", "Density"};
        double[] mValues;

        /**
         *
         */
        AverageDegreeResult(double pAv, double pDensity) {
            mValues = new double[getNumberOfResults()];
            mValues[0] = pAv;
            mValues[1] = pDensity;
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
     * @return A new AverageDegreeMetric instace.
     */
    public NetworkMetric copy() {
        return new AverageDegreeMetric();
    }

    /**
     * Calculates the average of edges over nodes.
     *
     * @param pNetwork The network to analyze.
     * @param pDirected Specifies how to treat the network directed or undirected.
     *                  The pDirected parameter is not used here, but is required by the interface.
     * @return The ratio of edges over nodes.
     */
    public NetworkMetricResult analyze(RandomNetwork pNetwork, boolean pDirected) {
        //The value to be returned
        double averageDegree = 0;
        double density = 0;

        //The number of nodes
        double N = pNetwork.getNumNodes();

        //The number of edges
        double E = pNetwork.getNumEdges();

        density = E / (N * (N - 1));
        if (!pDirected) {
            density *= 2;
        }

        //Every edge whether directed or undirected has two edge-endpoints (or degrees)
        E *= 2.0d;

        //compute the ratio
        averageDegree = E / N;

        //Return the result
        return new AverageDegreeResult(averageDegree, density);
    }
}