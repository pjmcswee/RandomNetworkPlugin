/* 
File: DegreeDistribution.java
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

import java.util.Date;
import java.util.Set;

import org.cytoscape.randomnetwork.RandomNetwork;

import java.io.*;
/**
 *  Compute the alpha value from k^(-alpha).
 *  Most "real-world" scale-free networks have 
 *  alpha value between 2 and 3, sometimes between 1 and 2.
 *
 *
 *  @author Patrick J. McSweeney
 *  @version 1.0
 */
public class DegreeDistributionMetric implements NetworkMetric {

    /**
     *
     */
    class DegreeDistributionResult implements NetworkMetricResult {

        String[] mNames = {"Degree Distribution", "Max. Degree", "Centralization", "Heterogenity"};
        double[] mValues;
        int number = 4;

        /**
         *
         */
        DegreeDistributionResult(double pAlpha, double pMax, double pCentral, double pHetero) {
            mValues = new double[getNumberOfResults()];
            mValues[0] = pAlpha;
            mValues[1] = pMax;
            mValues[2] = pCentral;
            mValues[3] = pHetero;
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
            return number;
        }
    }

    /**
     *
     */
    public NetworkMetric copy() {
        return new DegreeDistributionMetric();
    }

    /**
     *
     */
    public NetworkMetricResult analyze(RandomNetwork network, boolean directed) {
        //The value to store alpha in
        double power = 0;


        //Network max degree
        double max = 0;

        //Network heterogenity
        double hetero = 0;

        //Network centralization
        double central = 0;

        double average = 0;
        //Iterate through all of thie nodes in this network

        //Use as the number of nodes in the network
        int N = network.getNumNodes();

        //Store the degree distribution
        int degree[] = new int[N];
        Set<Integer> nodes = network.nodes();
        //while (nodeIterator.numRemaining() > 0) {
          //  int nodeIndex = nodeIterator.nextInt();
        for (Integer nodeIndex : nodes) {   
            Set<Integer> edges = network.edgesAdjacent(nodeIndex, directed, directed, !directed);
            int nodeDegree = edges.size();
            max = Math.max(max, nodeDegree);
            average += nodeDegree;
            degree[nodeDegree]++;
        }

        average /= ((double) N);
        double variance = 0;

//        nodeIterator = network.nodes();
        //while (nodeIterator.numRemaining() > 0) {
        for (Integer nodeIndex : nodes) {   
        	
          //  int nodeIndex = nodeIterator.nextInt();
        	Set<Integer> edges  = network.edgesAdjacent(nodeIndex, directed, directed, !directed);
            int nodeDegree = edges.size();
            variance += Math.pow(nodeDegree - average, 2.0f);
        }
		
		////DELETE ME!
		/*
		try{
			Date d = new Date();
			DataOutputStream dout = new DataOutputStream(new FileOutputStream(d + ".output"));
			int prev = 0;
			for(int i = N-1; i > 0; i--)
			{
				if(degree[i] > 0)
				{
					dout.writeBytes((Math.log(prev + degree[i])/Math.log(N)) + "\t" + (Math.log(i)/Math.log(10)) + "\n");
					prev += degree[i];
				}
			}
			dout.close();
		}catch(Exception e){e.printStackTrace();}
		/////*/



        hetero = Math.sqrt(variance) / average;
        central = max / ((double) N) - (average / (((double) N) - 1.0));

        return new DegreeDistributionResult(leastSquares(degree)[0], max, central, hetero);
    }

    /**
     *
     * Fits the logarithm distribution/degree to a straight line of the form:
     *	a + b *x which is then interrpreted as a*x^y in the non-logarithmic scale
     *
     * @param dist The distribution of node degrees to fit to a logarithmized straight line
     *
     * @return An array of 4 doubles
     *					index 0:  beta value
     *					index 1:  log(alpha) value (e^alpha for comparisons with NetworkAnalyzer
     *					index 2:  r^2 correlation coefficient
     *					index 3:  covariance
     *
     *  For more see Wolfram Least Squares Fitting
     */
    public double[] leastSquares(int dist[]) {
        //Vararibles to compute
        double SSxx = 0;
        double SSyy = 0;
        double SSxy = 0;

        //Compute the average log(x) value when for positive (>0) values
        double avgX = 0;
        double nonZero = 0;
        for (int i = 1; i < dist.length; i++) {
            if (dist[i] > 0) {
                avgX += Math.log(i);
                nonZero++;
            }
        }
        avgX /= nonZero;

        //compute the variance of log(x)
        for (int i = 1; i < dist.length; i++) {
            if (dist[i] > 0) {
                SSxx += Math.pow(Math.log(i) - avgX, 2);
            }
        }

        //Compute the average log(y) values
        double avgY = 0;
        for (int i = 1; i < dist.length; i++) {
            if (dist[i] > 0) {
                avgY += Math.log(dist[i]);
            }
        }
        avgY /= nonZero;

        //compute the variance over the log(y) values
        for (int i = 1; i < dist.length; i++) {
            if (dist[i] > 0) {
                SSyy += Math.pow(Math.log(dist[i]) - avgY, 2);
            }
        }

        //Compute teh SSxy term
        for (int i = 1; i < dist.length; i++) {
            if (dist[i] > 0) {
                SSxy += (Math.log(i) - avgX) * (Math.log(dist[i]) - avgY);
            }
        }

        //Compute and return the results
        double results[] = new double[4];
        results[0] = SSxy / SSxx;
        results[1] = avgY - results[0] * avgX;
        results[2] = (SSxy * SSxy) / (SSxx * SSyy);
        results[3] = SSxy / nonZero;

        return results;
    }
}
