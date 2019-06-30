/* 
File: ForestFiresModel.java
Author: Patrick J. McSweeney (pjmcswee@syr.edu)
Copyright (c) 2006, 2007, The Cytoscape Consortium (www.cytoscape.org)

Jure Leskovec, Jon Kleinberg and Christos Faloutsos. Graphs over time: densification laws, shrinking diameters and possible explanations. KDD '05: Proceeding of the eleventh ACM SIGKDD international conference on Knowledge discovery in data mining, 177–187, 2005. 

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
 *	
 */
public class ForestFiresModel extends RandomNetworkModel {

    /**  Radius used.*/
    private double mForward;
    /**	The dimension for the sytem. */
    private double mBackward;
    /**		*/
    private int mNumAmbassador = 1;
    private double mForwardAlpha;
    private double mBackwardAlpha;
    private double mOrphanProb = 0;

    /**
     *
     */
    public ForestFiresModel(int pNumNodes, int pNumAbassador, boolean pAllowSelfEdge,
            boolean pDirected, double pForward, double pBackward, double orphan) {
        super(pNumNodes, UNSPECIFIED, pAllowSelfEdge, pDirected);
        mNumAmbassador = pNumAbassador;
        mForward = pForward;
        mBackward = pBackward;
        mForwardAlpha = pForward;
        mBackwardAlpha = pBackward;
        mOrphanProb = orphan;
    }

    /**
     * Creats a copy of the RandomNetworkGenerator.  Used to give each thread their own
     * copy of the generator.
     *
     * @return Returns a copy of this generator
     */
    public RandomNetworkGenerator copy() {
        return new ForestFiresModel(numNodes, mNumAmbassador, allowSelfEdge, directed, mForward, mBackward, mOrphanProb);
    }

    /**
     * @return Gets the display name for this generator.
     */
    public String getName() {
        return new String("Forest Fires Model");
    }

    public int geom(double p) {
        return (int) Math.floor(Math.log(1.0 - Math.random()) / Math.log(1.0 - (1.0 - (p))));
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
        LinkedList<Integer>[] adjList = new LinkedList[numNodes];
        LinkedList<Integer>[] invAdjList = new LinkedList[numNodes];
        nodes[0] = random_network.createNode();
        adjList[0] = new LinkedList<Integer>();
        invAdjList[0] = new LinkedList<Integer>();


        for (int i = 1; i < numNodes; i++) {
            nodes[i] = random_network.createNode();
            adjList[i] = new LinkedList<Integer>();
            invAdjList[i] = new LinkedList<Integer>();
        }

        //for all pairs of nodes
        for (int i = 1; i < numNodes; i++) {
            LinkedList<Integer> seen = new LinkedList<Integer>();
            if (!allowSelfEdge) {
                seen.add(new Integer(i));
            }
            if (!directed) {
                seen.addAll(adjList[i]);
            }
            //Create orphans
            if (Math.random() < mOrphanProb) {
                continue;
            }
            LinkedList<Integer> toExplore = new LinkedList<Integer>();

            for (int j = 0; j < mNumAmbassador; j++) {
                int ambassador = Math.abs(random.nextInt() % i);
                if (!seen.contains(new Integer(ambassador))) {
                    random_network.createEdge(nodes[i], nodes[ambassador], directed);
                    seen.add(new Integer(ambassador));

                    adjList[i].add(new Integer(ambassador));
                    if (!directed) {
                        adjList[ambassador].add(new Integer(i));
                    }

                    toExplore.add(new Integer(ambassador));
                }
            }
            while (toExplore.size() > 0) {
                int w = toExplore.removeFirst().intValue();
                int x = geom(mForwardAlpha);
                int y = geom(mForwardAlpha * mBackwardAlpha);

                if (adjList[w].size() > 0) {
                    Collections.shuffle(adjList[w], random);
                }
                if (invAdjList[w].size() > 0) {
                    Collections.shuffle(invAdjList[w], random);
                }

                x = Math.min(x, adjList[w].size());
                y = Math.min(y, invAdjList[w].size());


                int count = 0;
                for (Integer node : adjList[w]) {
                    if (count == x) {
                        break;
                    }

                    if (!seen.contains(node)) {
                        adjList[i].add(node);
                        if (!directed) {
                            adjList[node].add(i);
                        }
                        seen.add(node);
                        toExplore.add(node);
                        random_network.createEdge(nodes[i], nodes[node.intValue()], directed);
                        count++;
                    }
                }

                if (directed) {
                    count = 0;
                    for (Integer node : invAdjList[w]) {
                        if (count == y) {
                            break;
                        }
                        if (!seen.contains(node)) {
                            invAdjList[i].add(node);
                            seen.add(node);
                            random_network.createEdge(nodes[node.intValue()], nodes[i], directed);
                            count++;
                        }
                    }
                }
            }
        }
        return random_network;
    }
}
