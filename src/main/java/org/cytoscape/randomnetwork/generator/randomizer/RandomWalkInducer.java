 /* 
File: RandomWalkInducer.java
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
import org.cytoscape.randomnetwork.generators.RandomNetworkGenerator;

/**
 *
 *  @author Patrick J. McSweeney
 *  @version 1.0
 *
 */
public class RandomWalkInducer extends SubNetworkInducerModel {

    /**
     * The number of nodes in the desired sub network
     */
    private int mNumNodes;
    private Integer mStartingNode;
	
	//0 = Purely Random
	//1 = BFS
	//2 = DFS
	//3 = Walker
	private int type;
	
	
    /**
     *  Constructor
     *
     *  @param  pGraph The DynamicGraph to randomize.
     *  @param pDirected Specifices how to treat the network directed(true) undirected (false).
     */
    public RandomWalkInducer(RandomNetwork pGraph,
            boolean pDirected, int pNodes, int pStartingNode, int pType) {
        super(pGraph, pDirected);
        mNumNodes = pNodes;
        mStartingNode = pStartingNode;
        type = pType;
    }

  
    /**
     * Creates a copy of this NetworkRandomizer
     *
     * @return An exact copy of this NetworkRandomizer
     */
    public RandomNetworkGenerator copy() {
        return new RandomWalkInducer(mOriginal, mDirected, mNumNodes, 
                mStartingNode, type);               
    }

    /**
     * @return Gets the display name for this generator.
     */
    public String getName() {
        return new String("Sub Graph Inducer");
    }

    /**
     * 
     * @return
     */
    public RandomNetworkImpl generate() {
        if (type == 1) {
            return bfs();
        } else if (type == 2) {
            return dfs();
        } else if (type == 3) {
            return walker();
        } else {//if (type == 0) {
			return pureRand();
		}
    }

    /**
     *
     */
    public RandomNetworkImpl walker() {

        //Create an empty network
        RandomNetworkImpl newGraph = new RandomNetworkImpl(mDirected);
        int N = mOriginal.getNumNodes();

        int current = mStartingNode;
        

        //Create a linkedList to hold the node Indicies
        LinkedList<Integer> selectedNodes = new LinkedList<Integer>();

        //Tells whether or not the node has been visited
        boolean visitedNodes[] = new boolean[N];

        int visited = 0;
        while (visited < mNumNodes) {
            selectedNodes.add(current);
            visitedNodes[current] = true;
            visited++;

            int choices = 0;
            Set<Integer> edges = mOriginal.edgesAdjacent(current, mDirected, false, !mDirected);
            //while (edges.numRemaining() > 0) {
            for (Integer edge : edges) {	
                //int edge = edges.nextInt();
                Integer target = mOriginal.edgeTarget(edge);

                if (target == current) {
                    target = mOriginal.edgeSource(edge);
                }
                if (!visitedNodes[target]) {
                    choices++;
                }
            }

            if (choices == 0) {
                //Choose a new staring node
                int rnd = Math.abs(mRandom.nextInt(N - visited + 1));
                int count = 0;
                int index = 0;
                while (count < rnd) {
                    if (!visitedNodes[index % N]) {
                        count++;
                    } else {
                        index++;
                    }
                }
                current = index % N;
            } else {
                int choice = mRandom.nextInt(choices);
                int target = -1;
                int seen = 0;
                edges = mOriginal.edgesAdjacent(current, mDirected, false, !mDirected);
                for (Integer edge : edges) {
                //while (edges.numRemaining() > 0) {
                  //  int edge = edges.nextInt();
                    target = mOriginal.edgeTarget(edge);

                    if (target == current) {
                        target = mOriginal.edgeSource(edge);
                    }

                    if (!visitedNodes[target]) {
                        if (seen == choice) {
                            break;
                        }
                        seen++;
                    }
                }
                current = target;
            }
        }

        //System.out.println(visited + "\t" + selectedNodes.size() + "\t" + mNumNodes);
        return new RandomNetworkImpl(mOriginal, selectedNodes, mDirected);
    }

    /**
     *
     */
    public RandomNetworkImpl dfs() {

        //Create an empty network
        RandomNetworkImpl newGraph = new RandomNetworkImpl(mDirected);
        int N = mOriginal.getNumNodes();

        //Create a linkedList to hold the node Indicies
        LinkedList<Integer> toExploreList = new LinkedList<Integer>();
        LinkedList<Integer> selectedNodes = new LinkedList<Integer>();

        int visitedNodes[] = new int[N];
        toExploreList.add(mStartingNode);
        visitedNodes[mStartingNode] = 1;

        int visited = 0;
        while (visited < mNumNodes) {

            while ((!toExploreList.isEmpty()) && (visited < mNumNodes)) {
                Integer node = toExploreList.remove();
                selectedNodes.add(node);

                visitedNodes[node] = 2;
                Set<Integer> edges = mOriginal.edgesAdjacent(node, mDirected, false, !mDirected);
                for (Integer edge : edges) {
                //while (edges.numRemaining() > 0) {
                  //  Integer edge = edges.nextInt();
                    Integer target = mOriginal.edgeTarget(edge);

                    if (target == node) {
                        target = mOriginal.edgeSource(edge);
                    }

                    if (visitedNodes[target] == 0) {
                        toExploreList.add(target);
                        visitedNodes[target] = 1;
                    }
                }

                visited++;
            }

            if (visited == mNumNodes) {
                break;
            }

            //Choose a new staring node
            int rnd = Math.abs(mRandom.nextInt(N - visited)) + 1;
            int count = 0;
            int index = 0;
            while (count < rnd) {
                if (visitedNodes[index] != 2) {
                    count++;
                } else {
                    index++;
                }
            }
            visitedNodes[index] = 1;
            toExploreList.addFirst(new Integer(index));
        }

        return new RandomNetworkImpl(mOriginal, selectedNodes, mDirected);
    }

    /**
     *
     */
    public RandomNetworkImpl bfs() {

        //Create an empty network
        RandomNetworkImpl newGraph = new RandomNetworkImpl(mDirected);
        int N = mOriginal.getNumNodes();

        //Create a linkedList to hold the node Indicies
        LinkedList<Integer> toExploreList = new LinkedList<Integer>();
        LinkedList<Integer> selectedNodes = new LinkedList<Integer>();

        int visitedNodes[] = new int[N];
        toExploreList.add(mStartingNode);
        visitedNodes[mStartingNode] = 1;

        int visited = 0;
        while (visited < mNumNodes) {
            while ((!toExploreList.isEmpty()) && (visited < mNumNodes)) {
                Integer node = toExploreList.remove();
                selectedNodes.add(node);
                visitedNodes[node] = 2;
                Set<Integer> edges = mOriginal.edgesAdjacent(node, mDirected, false, !mDirected);
                for (Integer edge : edges) {
                //while (edges.numRemaining() > 0) {
                  //  Integer edge = edges.nextInt();
                    Integer target = mOriginal.edgeTarget(edge);

                    if (target == node) {
                        target = mOriginal.edgeSource(edge);
                    }
                    if (visitedNodes[target] == 0) {
                        toExploreList.addLast(target);
                        visitedNodes[target] = 1;
                    }
                }
                visited++;
            }

            if (visited == mNumNodes) {
                break;
            }

            //Choose a new staring node
            int rnd = Math.abs(mRandom.nextInt(N - visited)) + 1;
            int count = 0;
            int index = 0;
            while (count < rnd) {
                if (visitedNodes[index] != 2) {
                    count++;
                } else {
                    index++;
                }
            }
            visitedNodes[index] = 1;
            toExploreList.add(new Integer(index));
        }

        return new RandomNetworkImpl(mOriginal, selectedNodes, mDirected);
    }
	
	
	  /**
     *
     */
    public RandomNetworkImpl pureRand() {

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