package org.cytoscape.randomnetwork;

import java.util.Set;

public interface RandomNetwork {

	
	
	/**
	 * Creates a new node in this graph. Returns node id. Nodes are always
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
	public int createNode();

	
	public Set<Integer> nodes();
	
	
	
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
	public int createEdge(int sourceNode, int targetNode, boolean directed);

	
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
	public boolean removeEdge(int edgeId);
	
	
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
	public boolean edgeType(int edgeId);
	
	
	/**
	 * Determines the source node of an edge. Returns the source node of specified
	 * edge or -1 if specified edge does not exist in this graph.
	 *
	 * @param edge
	 *            the edge in this graph whose source node we're seeking.
	 * @return the source node of specified edge or -1 if specified edge does not
	 *         exist in this graph.
	 */
	public int edgeSource(int edgeId);
	
	/**
	 * Determines the target node of an edge. Returns the target node of specified
	 * edge or -1 if specified edge does not exist in this graph.
	 *
	 * @param edge
	 *            the edge in this graph whose target node we're seeking.
	 * @return the target node of specified edge or -1 if specified edge does not
	 *         exist in this graph.
	 */
	public int edgeTarget(int edgeId);
	
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
	public Set<Integer> edgesAdjacent(int nodeId, boolean outgoing, boolean incoming, boolean undirected);
	
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
	public Set<Integer> edgesConnecting(int node0, int node1, boolean outgoing, boolean incoming, boolean undirected);

	public Set<Integer> edges();

	/**
	 * @return The number of nodes in the network
	 */
	public int getNumNodes();

	/**
	 * @return The number of edges in this network
	 */
	public int getNumEdges();
		
	public boolean getDirected();
	
	public String getNetworkName();
	
	public VisualData getVisualData();
}
