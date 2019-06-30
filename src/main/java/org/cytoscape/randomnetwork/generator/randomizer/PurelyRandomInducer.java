 /* 
File: PurelyRandomInducer.java
Author: Patrick J. McSweeney (pjmcswee@syr.edu)
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
package org.cytoscape.randomnetwork.generator.randomizer;

import java.util.*;

import org.cytoscape.randomnetwork.RandomNetwork;
import org.cytoscape.randomnetwork.RandomNetworkImpl;

/**
 *
 *  @author Patrick J. McSweeney
 *  @version 1.0
 *
 */
public class PurelyRandomInducer extends SubNetworkInducerModel {

    /**
     * The number of nodes in the desired sub network
     */
    private int mNumNodes;
    //private int mNumEdges;
   // private boolean onNodes;

    /**
     *  Constructor
     *
     *  @param  pGraph The DynamicGraph to randomize.
     *  @param pDirected Specifices how to treat the network directed(true) undirected (false).
     */
    public PurelyRandomInducer(RandomNetwork pGraph,
            boolean pDirected, int pNodes) {
        super(pGraph, pDirected);
        mNumNodes = pNodes;
    }

    /**
     * Creates a copy of this NetworkRandomizer
     *
     * @return An exact copy of this NetworkRandomizer
     */
    public PurelyRandomInducer copy() {

        return new PurelyRandomInducer(mOriginal, mDirected, mNumNodes);
    }

    /**
     * @return Gets the display name for this generator.
     */
    public String getName() {
        return new String("Purely Random Inducer");
    }

    /**
     *
     */
    public RandomNetworkImpl generate() {

        //Create an empty network
        RandomNetworkImpl newGraph = new RandomNetworkImpl(mDirected);
        int N = mOriginal.getNumNodes();


        //Create a linkedList to hold the node Indicies
        LinkedList<Integer> selectedNodes = new LinkedList<Integer>();
        boolean visitedNodes[] = new boolean[N];

        int visited = 0;
        while (visited < mNumNodes) {
            //Choose a new staring node
            int rnd = Math.abs(mRandom.nextInt(N));
            while (visitedNodes[rnd % N]) {
                rnd++;
            }
            visited++;
            visitedNodes[rnd % N] = true;
            selectedNodes.add(new Integer(rnd % N));
        }
        return new RandomNetworkImpl(mOriginal, selectedNodes, mDirected);
    }
}
