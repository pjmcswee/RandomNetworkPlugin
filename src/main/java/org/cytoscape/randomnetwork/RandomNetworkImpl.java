/* 
File: RandomNetwork.java
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
package org.cytoscape.randomnetwork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class Edge {
	private int source;
	private int target;
	private boolean directed;

	public Edge(int source, int target, boolean directed) {
		this.source = source;
		this.target = target;
		this.directed = directed;
	}

	public int source() {
		return source;
	}

	public int target() {
		return target;
	}

	public boolean directed() {
		return directed;
	}
}

/**
 * This class wraps a implements a simple network topology. You can create a
 * RandomNetwork object using a CyNetwork, so strictly speaking every
 * RandomNetwork is not really random, however, every really random network is a
 * RandomNetwork object.
 *
 * @author Patrick J. McSweeney
 * @version 2.0
 */
public class RandomNetworkImpl implements RandomNetwork {

	// Map Nodes to a list of Edge Ids.
	Map<Integer, Set<Integer>> mAdjList;

	// Used to track incoming edges for directed edges
	Map<Integer, Set<Integer>> mSecondaryAdjList;

	// Edge id to source & target
	Map<Integer, Edge> mEdges;

	/**
	 * The name of the network
	 */
	private String mNetworkName;
	/**
	 * Whether or not this network is directed
	 */
	private boolean mDirected;
	/**
	 * The number of nodes in the random graph
	 */
	private int mNumNodes;
	/**
	 * The number of edges in the random graph
	 */
	private int mNumEdges;

	private int nextEdgeId;
	/**
	 *
	 */
	VisualData visualData = null;

	/**
	 * @return the visualData
	 */
	public VisualData getVisualData() {
		return visualData;
	}

	public void setVisualData(VisualData data) {
		this.visualData = data;
	}

	public boolean getDirected() {
		return mDirected;
	}

	public String getNetworkName() {
		return mNetworkName;
	}

	public void setNetworkName(String name) {
		mNetworkName = name;
	}

	/**
	 * Default Constructor. Create an empty graph with no data.
	 *
	 */
	public RandomNetworkImpl(boolean pDirected) {

		// Create the topology graph
		mAdjList = new HashMap<Integer, Set<Integer>>();
		mSecondaryAdjList = new HashMap<Integer, Set<Integer>>();
		mEdges = new HashMap<Integer, Edge>();

		// Mark the direction of edges
		mDirected = pDirected;

		// Nodes and edges are empty
		mNumNodes = 0;
		mNumEdges = 0;
		nextEdgeId = 0;
	}

	/**
	 *
	 *
	 */
	public RandomNetworkImpl(RandomNetwork pNetwork, boolean pDirected) {

		mDirected = pDirected;
		mAdjList = new HashMap<Integer, Set<Integer>>();
		mSecondaryAdjList = new HashMap<Integer, Set<Integer>>();

		mEdges = new HashMap<Integer, Edge>();

		// How many edges are there
		int N = pNetwork.getNumNodes();

		boolean directedToUndirected = ((pNetwork.getDirected()) && (!mDirected));
		boolean undirectedToDirected = ((!pNetwork.getDirected()) && (mDirected));

		mNetworkName = pNetwork.getNetworkName();

		// Is this necessary, can we just save a pointer to a single object?
		if (pNetwork.getVisualData() != null) {
			visualData = new VisualData(pNetwork.getVisualData());
		}

		// For each node
		for (int i = 0; i < N; i++) {
			createNode();
		}

		List<Integer> existing = null;
		if (directedToUndirected) {
			existing = new ArrayList<Integer>();
		}

		Set<Integer> edges = pNetwork.edges();
		for (Integer edgeId : edges) {
			// iterate over all of the edges
			int source = pNetwork.edgeSource(edgeId);
			int target = pNetwork.edgeTarget(edgeId);

			if (directedToUndirected) {
				if (existing.contains(target * N + source)) {
					continue;
				}
			}

			createEdge(source, target, mDirected);
			if (directedToUndirected) {
				if (existing.add(source * N + target)) {
					if (undirectedToDirected) {
						createEdge(target, source, mDirected);
					}
				}
			}
		}
	}	

	/**
	 *
	 */
	public RandomNetworkImpl(RandomNetwork pNetwork, List<Integer> pSelectedNodes, boolean pDirected) {
		mDirected = pDirected;

		// How many edges are there
		int N = pSelectedNodes.size();

		boolean[] visited = new boolean[pNetwork.getNumNodes()];

		for (Integer node : pSelectedNodes) {
			visited[node] = true;
		}

		boolean directedToUndirected = ((pNetwork.getDirected()) && (!mDirected));
		boolean undirectedToDirected = ((!pNetwork.getDirected()) && (mDirected));

		mNetworkName = pNetwork.getNetworkName();
		// Is this necessary, can we just save a pointer to a single object?
		if (pNetwork.getVisualData() != null) {
			visualData = new VisualData(pNetwork.getVisualData());
		}

		// For each node
		int index = 0;
		int map[] = new int[visited.length];
		for (int i = 0; i < visited.length; i++) {
			if (visited[i]) {
				createNode();
				map[i] = index;
				index++;

			}
		}

		// iterate over all of the edges
		List<Integer> existing = null;
		if (directedToUndirected) {
			existing = new ArrayList<Integer>();
		}

		// iterate over all of the edges
		for (Edge edge : mEdges.values()) {

			// int edge = edgeIter.nextInt();
			int source = edge.source();
			int target = edge.target();

			if ((!visited[source]) || (!visited[target])) {
				continue;
			}

			if (directedToUndirected) {
				if (existing.contains(target * N + source)) {
					continue;
				}
			}

			createEdge(map[source], map[target], mDirected);
			if (directedToUndirected) {
				if (existing.add(source * N + target)) {
					if (undirectedToDirected) {
						createEdge(map[target], map[source], mDirected);
					}
				}
			}
		}
	}

	/**
	 * Creates a new node in this graph. Returns the new node. Nodes are always
	 * non-negative.
	 * <p>
	 * Implementations may create nodes with arbitrarily large values. Even if
	 * implementations initially create nodes with small values, nodes may take
	 * ever-increasing values when nodes are continually being removed and created.
	 * Or, implementations may choose to re-use node values as nodes are removed and
	 * added again.
	 *
	 * @return the newly created node.
	 */
	public int createNode() {
		int nodeId = mNumNodes++;
		mAdjList.put(nodeId, new HashSet<Integer>());
		return nodeId;
	}

	public Set<Integer> nodes() {
		return mAdjList.keySet();
	}

	/**
	 * Creates a new edge in this graph, having source node, target node, and
	 * directedness specified. Returns the new edge, or -1 if either the source or
	 * target node does not exist in this graph. Edges are always non-negative.
	 * <p>
	 * Implementations may create edges with arbitrarily large values. Even if
	 * implementations initially create edges with small values, edges may take
	 * ever-increasing values when edges are continually being removed and created.
	 * Or, implementations may choose to re-use edge values as edges are removed and
	 * added again.
	 *
	 * @param sourceNode
	 *            the source node that the new edge is to have.
	 * @param targetNode
	 *            the target node that the new edge is to have.
	 * @param directed
	 *            the new edge will be directed if and only if this value is true.
	 * @return the newly created edge or -1 if either the source or target node
	 *         specified does not exist in this graph.
	 */
	public int createEdge(int sourceNode, int targetNode, boolean directed) {
		Set<Integer> sourceList = this.mAdjList.get(sourceNode);
		Set<Integer> targetList = this.mAdjList.get(targetNode);

		//System.out.println("Creating edge: " + sourceNode + "\t" + targetNode);

		// If either the sourceNode or targetNode doesn't exist.
		if (sourceList == null || targetList == null) {
			throw new RuntimeException("Source LIsts are null!" + sourceNode + " \t " + targetNode);
			// return -1;
		}
		int edgeId = nextEdgeId++;
		mNumEdges++;

		// Create the edge (source, target)
		Edge edge = new Edge(sourceNode, targetNode, directed);
		// Enter it into the map of edges
		mEdges.put(edgeId, edge);
		// Add the edge to both of the adjacency lists
		sourceList.add(edgeId);
		if (!directed) {
			targetList.add(edgeId);
		} else {
			Set<Integer> incoming = this.mSecondaryAdjList.get(targetNode);
			if (incoming == null) {
				incoming = new HashSet<Integer>();
				this.mSecondaryAdjList.put(targetNode, incoming);
			}
			incoming.add(edgeId);
		}
		return edgeId;
	}

	/**
	 * Removes the specified edge from this graph. Returns true if and only if the
	 * specified edge was in this graph at the time this method was called. A return
	 * value of true implies that the specified edge has been successfully removed
	 * from this graph.
	 * <p>
	 * Note that removing an edge does not cause that edge's endpoint nodes to be
	 * removed from this graph.
	 *
	 * @param edge
	 *            the edge that is to be removed from this graph.
	 * @return true if and only if the specified edge existed in this graph at the
	 *         time this operation was started.
	 */
	public boolean removeEdge(int edgeId) {
		Edge edge = mEdges.get(edgeId);
		if (edge == null) {
			return false;
		}
		mEdges.remove(edgeId);
		Set<Integer> sourceSet = this.mAdjList.get(edge.source());
		sourceSet.remove(edgeId);

		if (!edge.directed()) {
			Set<Integer> targetSet = this.mAdjList.get(edge.target());
			targetSet.remove(edgeId);
		} else {
			Set<Integer> secondarySet = this.mSecondaryAdjList.get(edge.target());
			secondarySet.remove(edgeId);
		}
		mNumEdges--;
		return true;
	}

	
	/**
	 * Determines the existence and directedness of an edge. Returns -1 if specified
	 * edge does not exist in this graph, otherwise returns DIRECTED_EDGE or
	 * UNDIRECTED_EDGE.
	 *
	 * @param edge
	 *            the edge in this graph whose existence and/or directedness we're
	 *            seeking.
	 * @return DIRECTED_EDGE if specified edge is directed, UNDIRECTED_EDGE if
	 *         specified edge is undirected, and -1 if specified edge does not exist
	 *         in this graph.
	 */
	public boolean edgeType(int edgeId) {
		Edge edge = mEdges.get(edgeId);
		return edge.directed();
	}

	/**
	 * Determines the source node of an edge. Returns the source node of specified
	 * edge or -1 if specified edge does not exist in this graph.
	 *
	 * @param edge
	 *            the edge in this graph whose source node we're seeking.
	 * @return the source node of specified edge or -1 if specified edge does not
	 *         exist in this graph.
	 */
	public int edgeSource(int edgeId) {
		Edge edge = mEdges.get(edgeId);
		return edge.source();
	}

	/**
	 * Determines the target node of an edge. Returns the target node of specified
	 * edge or -1 if specified edge does not exist in this graph.
	 *
	 * @param edge
	 *            the edge in this graph whose target node we're seeking.
	 * @return the target node of specified edge or -1 if specified edge does not
	 *         exist in this graph.
	 */
	public int edgeTarget(int edgeId) {
		Edge edge = mEdges.get(edgeId);
		return edge.target();
	}

	/**
	 * Returns a non-repeating enumeration of edges adjacent to a node. The three
	 * boolean input parameters define what is meant by "adjacent edge". If all
	 * three boolean input parameters are false, the returned enumeration will have
	 * zero elements.
	 * <p>
	 * The returned enumeration becomes invalid as soon as any
	 * graph-topology-modifying method on this graph is called. Calling methods on
	 * an invalid enumeration will result in undefined behavior of that enumeration.
	 * <p>
	 * This method returns null if and only if the specified node does not exist in
	 * this graph. Therefore, this method can be used to test the existence of a
	 * node in this graph.
	 *
	 * @param node
	 *            the node in this graph whose adjacent edges we're seeking.
	 * @param outgoing
	 *            all directed edges whose source is the node specified are included
	 *            in the returned enumeration if this value is true; otherwise, not
	 *            a single such edge is included in the returned enumeration.
	 * @param incoming
	 *            all directed edges whose target is the node specified are included
	 *            in the returned enumeration if this value is true; otherwise, not
	 *            a single such edge is included in the returned enumeration.
	 * @param undirected
	 *            all undirected edges touching the specified node are included in
	 *            the returned enumeration if this value is true; otherwise, not a
	 *            single such edge is included in the returned enumeration.
	 * @return an enumeration of edges adjacent to the node specified or null if
	 *         specified node does not exist in this graph.
	 */
	public Set<Integer> edgesAdjacent(int nodeId, boolean outgoing, boolean incoming, boolean undirected) {
		Set<Integer> edgeIds = new HashSet<Integer>();
		if (this.mAdjList.get(nodeId) == null) {
			return edgeIds;
		}

		edgeIds.addAll(this.mAdjList.get(nodeId));
		if (incoming && this.mSecondaryAdjList.get(nodeId) != null) {
			edgeIds.addAll(this.mSecondaryAdjList.get(nodeId));
		}
		return edgeIds;
	}

	/**
	 * Returns a non-repeating iteration of edges connecting two nodes. The three
	 * boolean input parameters define what is meant by "connecting edge". If all
	 * three boolean input parameters are false, the returned iteration will have no
	 * elements.
	 * <p>
	 * The returned iteration becomes invalid as soon as any
	 * graph-topology-modifying method on this graph is called. Calling methods on
	 * an invalid iteration will result in undefined behavior of that iteration.
	 * <p>
	 * I'd like to discuss the motivation behind this interface method. I assume
	 * that most implementations of this interface will implement this method in
	 * terms of edgesAdjacent(). Why, then, is this method necessary? Because some
	 * implementations may choose to optimize the implementation of this method by
	 * using a search tree or a hashtable, for example. This method is a hook to
	 * provide such optimization.
	 * <p>
	 * This method returns an IntIterator as opposed to an IntEnumerator so that
	 * non-optimized implementations would not be required to pre-compute the number
	 * of edges being returned.
	 *
	 * @param node0
	 *            one of the nodes in this graph whose connecting edges we're
	 *            seeking.
	 * @param node1
	 *            one of the nodes in this graph whose connecting edges we're
	 *            seeking.
	 * @param outgoing
	 *            all directed edges whose source is node0 and whose target is node1
	 *            are included in the returned iteration if this value is true;
	 *            otherwise, not a single such edge is included in the returned
	 *            iteration.
	 * @param incoming
	 *            all directed edges whose source is node1 and whose target is node0
	 *            are included in the returned iteration if this value is true;
	 *            otherwise, not a single such edge is included in the returned
	 *            iteration.
	 * @param undirected
	 *            all undirected edges E such that E's endpoints are node0 and node1
	 *            are included in the returned iteration if this value is true;
	 *            otherwise, not a single such edge is incuded in the returned
	 *            iteration.
	 * @return an iteration of edges connecting node0 with node1 in a fashion
	 *         specified by boolean input parameters or null if either of node0 or
	 *         node1 does not exist in this graph.
	 */
	public Set<Integer> edgesConnecting(int node0, int node1, boolean outgoing, boolean incoming, boolean undirected) {
		Set<Integer> edges = new HashSet<Integer>();
		if (mAdjList.get(node0) != null) {
			for (Integer edgeId : mAdjList.get(node0)) {
				Edge e = mEdges.get(edgeId);
				if (e.target() == node1) {
					edges.add(edgeId);
				}
			}
		}
		if (undirected && mAdjList.get(node1) != null) {
			for (Integer edgeId : mAdjList.get(node1)) {
				Edge e = mEdges.get(edgeId);
				if (e.target() == node0) {
					edges.add(edgeId);
				}
			}
		}
		if (outgoing && mSecondaryAdjList.get(node0) != null) {
			for (Integer edgeId : mSecondaryAdjList.get(node0)) {
				Edge e = mEdges.get(edgeId);
				if (e.target() == node1) {
					edges.add(edgeId);
				}
			}
		}
		return edges;
	}

	public Set<Integer> edges() {
		return mEdges.keySet();
	}

	/**
	 * @return The number of nodes in the network
	 */
	public int getNumNodes() {
		return mNumNodes;
	}

	/**
	 * @return The number of edges in this network
	 */
	public int getNumEdges() {
		return mNumEdges;
	}

	public boolean hasVisualData() {
		// TODO Auto-generated method stub
		return this.visualData != null;
	}
}
