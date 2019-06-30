package org.cytoscape.randomnetwork;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import javax.swing.SwingConstants;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.event.CyEventHelper;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.layout.CyLayoutAlgorithm;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.model.View;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.view.presentation.property.NodeShapeVisualProperty;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.work.Task;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.swing.DialogTaskManager;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
class HeadlessTaskMonitor implements TaskMonitor {
	
//	private static final Logger logger = LoggerFactory.getLogger(HeadlessTaskMonitor.class);

	private String taskName = "";

	public void setTask(final Task task) {
		this.taskName = "Task (" + task.toString() + ")";
	}

	
	@Override
	public void setTitle(final String title) {
	//	logger.info(taskName + " title: " + title);
	}

	
	@Override
	public void setStatusMessage(final String statusMessage) {
	//	logger.info(taskName + " status: " + statusMessage);
	}

	
	@Override
	public void setProgress(final double progress) {
	}


//	@Override
	//public void showMessage(Level level, String message) {
//	}
}


public class ConverterUtils {

	private static int sNumNets = 0;
	private static CyNetworkFactory networkFactory;
	private static CyNetworkManager networkManager;
	private static CyNetworkViewFactory networkViewFactory;
	private static CyNetworkViewManager networkViewManager;
	private static VisualMappingManager visualMappingManager;
	
	private static CyLayoutAlgorithm layoutManager;// = layoutManager.getLayout("customLayout");
	private static CyLayoutAlgorithmManager layoutAlgorithmManager;
	
	private static CyEventHelper eventHelper;
	private static CyApplicationManager applicationManager;
	
	private static DialogTaskManager dialogTaskManager;
	public static CyApplicationManager getApplicationManager() {
		return applicationManager;
	}
	
	public static DialogTaskManager getDialogTaskManager() {
		return dialogTaskManager;
	}
	
	public static void init(CyNetworkFactory pNetworkFactory,
							CyNetworkManager pNetworkManager,
							CyNetworkViewFactory pNetworkViewFactory,
							CyNetworkViewManager pNetworkViewManager,
							CyEventHelper pEventHelper,
							CyLayoutAlgorithm pLayoutManager,
							CyLayoutAlgorithmManager pLayoutAlgorithmManager,
							CyApplicationManager pApplicationManager,
							DialogTaskManager pDialogTaskManager) {
		networkFactory = pNetworkFactory;
		networkViewFactory = pNetworkViewFactory;
		networkViewManager = pNetworkViewManager;
		networkManager = pNetworkManager;
		eventHelper = pEventHelper;
		layoutManager = pLayoutManager;
		layoutAlgorithmManager = pLayoutAlgorithmManager;
		applicationManager = pApplicationManager;
		dialogTaskManager  = pDialogTaskManager;
	}
	
	/**
	 * Create an graph from a CyNetwork. (1) Transfer the topology into a
	 * RandomNetwork (2) Save the node labels. (3) Save the edge labels. (4) Save
	 * the node positions. (5) Save the Visual Style. (6) Record the number of edges
	 * and nodes.
	 *
	 *
	 * @param pCyNetwork
	 *            The CyNetwork to convert into a RandomNetwork.
	 * @param pDirected
	 *            How to treat the CyNetwork's edges.
	 */

	public static RandomNetwork toRandomNetwork(CyNetwork pCyNetwork, boolean pDirected) {

		// Create an empty dynamic graph representation
		RandomNetworkImpl result = new RandomNetworkImpl(pDirected);

		// determine the number of nodes
		int N = pCyNetwork.getNodeCount();

		// Create the array to hold the node labels
		// mNodeLabels = new String[N];

		// Create the array to hold edge labels
		// mEdgeLabels = new String[N];

		// Get the view from the original network
		// CyNetworkView orgView = pCyNetwork.
		// Cytoscape.createNetworkView(pCyNetwork);

		// Get the visual style from the view
		// mVisualStyle = orgView.getVisualStyle();

		// Get the name of network
		result.setNetworkName(pCyNetwork.getRow(pCyNetwork).get(CyNetwork.NAME, String.class));

		// Save the Node Positions
		// mNodePositions = new double[N][2];

		// Save the node indicies
		int nodeIndicies[] = new int[N];

		// Save the cyNodes
		CyNode cynodes[] = new CyNode[N];

		// set this value = 0

		// determine the number of edges

		// created from a CyNetwork
		// mHasData = true;

		//result.setVisualData(new VisualData(pCyNetwork, null));
		// Create the appropriate number of nodes
		int i = 0;

		List<CyNode> nodes = pCyNetwork.getNodeList();
		// Get the iterator for the CyNodes

		Map<CyNode, Integer> nodeMap = new HashMap<CyNode, Integer>();
		// For each Node
		for (CyNode current : nodes) {

			// Get the next iter
			// NodeView nodeView = orgView.getNodeView(current.getRootGraphIndex());

			// Save this node
			cynodes[i] = current;
			nodeMap.put(current, i);
			// Create a node in the dynamic graph for this CyNode
			nodeIndicies[i] = result.createNode();

			// Save the node positions
			// mNodePositions[nodeIndicies[i]][0] = nodeView.getXPosition();
			// mNodePositions[nodeIndicies[i]][1] = nodeView.getYPosition();

			// Save the CyNodes identifier
			// nodeLabels[nodeIndicies[i]] = current.getSUID();//.getIdentifier();

			i++;
		}

		List<Long> edgeList = new ArrayList<Long>();
		CyEdge.Type edgeType = CyEdge.Type.UNDIRECTED;
		if (pDirected) {
			edgeType = CyEdge.Type.OUTGOING;
		}
		// Iterate over all of the nodes in the network
		for (i = 0; i < N; i++) {
			// Get the next node id
			int nodeIndex = nodeIndicies[i];

			// Get the next node
			CyNode current = cynodes[i];

			// Iterate through the neighborhood
			Iterable<CyEdge> edges = pCyNetwork.getAdjacentEdgeIterable(current, edgeType);// pCyNetwork.getAdjacentEdgeIndicesArray(current.getRootGraphIndex(),
																							// true, false, true);

			for (CyEdge adjEdge : edges) {
				// for (int k = 0; k < edges.length; k++) {
				// Get the edges
				// CyEdge adjEdge = pCyNetwork.getEdge(edges[k]);

				// Get the target of this edge
				CyNode next = adjEdge.getTarget();

				// Switch to Source (undirected)
				if (next == current) {
					next = adjEdge.getSource();
				}

				// Find the index of this node
				int nextIndex = nodeMap.get(next);

				// If the network is undirected only create one instance of the edge
				// This may be a problem for multi-edge situations!!!
				if (!pDirected) {

					boolean exists = false;

					// Iterate through all of the existing undirected edges that touch nodeIndex
					Set<Integer> edgeIter = result.edgesAdjacent(nodeIndex, false, false, true);
					for (Integer edge : edgeIter) {
						// Check to see if nodeIndex is already connected to nextIndex
						if ((result.edgeSource(edge) == nextIndex) || (result.edgeTarget(edge) == nextIndex)) {
							exists = true;
						}
					}

					// If this edge already exists
					if (exists) {
						System.out.println("Edge already exists");
						// do not create it again
						continue;
					}

					// If this edge does not exist, then create it
					int edgeIndex = result.createEdge(nodeIndex, nextIndex, pDirected);
					edgeList.add(adjEdge.getSUID());
				} // If the network is directed
				else {
					// Create the edge
					System.out.println("2 Creating edge:" + nodeIndex + ", "+ nextIndex);
					int edgeIndex = result.createEdge(nodeIndex, nextIndex, pDirected);
					edgeList.add(adjEdge.getSUID());// .getIdentifier());
				}
			}
		}
		/*
		 * ListIterator<String> iter = edgeList.listIterator(); mEdgeLabels = new
		 * String[edgeList.size()]; int edgeCount = 0; while (iter.hasNext()) { String
		 * label = iter.next(); mEdgeLabels[edgeCount] = label; edgeCount++; }
		 */
		return result;
	}

	/**
	 * Exports the RandomNetwork as a CyNetwork
	 *
	 *
	 * @return Returns the RandomNetwork
	 */
	public static CyNetwork toCyNetwork(RandomNetwork pNetwork) {
		// Create a new CyNetwork
		//String title = new String(pNetwork.getNetworkName());

//		if (pNetwork.hasVisualData()) {
///			title += "'";
		//}

	//	title += " : " + sNumNets++;

		CyNetwork cynetwork = networkFactory.createNetwork();
		networkManager.addNetwork(cynetwork);

	//	cynetwork.getRow(cynetwork).set(CyNetwork.NAME, title);
		// CyNetwork cynetwork = Cytoscape.createNetwork(title, false);

		// Create the nodes indices array
		int nodeIndicies[] = new int[pNetwork.getNumNodes()];

		// Create an array of CyNode objects
		CyNode nodes[] = new CyNode[pNetwork.getNumNodes()];

		// The number of CyNodes that have been created.
		int nodeCount = 0;

		// Iterate through all of thie nodes in this network
		Set<Integer> nodeIterator = pNetwork.nodes();

		// Iterate through all of our nodes
		// while (nodeIterator.numRemaining() > 0) {
		for (Integer nodeIndex : nodeIterator) {
			// Save the nodeIndex for this node
			nodeIndicies[nodeCount] = nodeIndex;

			// Get the Cytoscape node using the NodeIds (string labels)
			// Add this node to our network
			CyNode node = cynetwork.addNode();
			/*
			 * if (pNetwork.hasVisualData()) { // Get the CyNode node =
			 * Cytoscape.getCyNode(mNodeLabels[nodeIndicies[nodeCount]], false); } else { //
			 * Create the CyNode if labels do not exist node = Cytoscape.getCyNode("" +
			 * nodeIndicies[nodeCount], true); }
			 */

			// 		

			// Save node in array
			nodes[nodeCount] = node;

			// increment the number of nodes we have processed
			nodeCount++;
		}

		// Keep track of the number of edges we process
		int edgeCount = 0;

		// iterate through all of the edges
		Set<Integer> edgeIterator = pNetwork.edges();
		System.out.println("NUmEdegs: " + edgeIterator.size());
	
		for (Integer edgeIndex : edgeIterator) {
			// while (edgeIterator.numRemaining() > 0) {
			// Get the next edge
			// int edgeIndex = edgeIterator.nextInt();

			// Get the source and target of the edge
			int source = pNetwork.edgeSource(edgeIndex);
			int target = pNetwork.edgeTarget(edgeIndex);

			// Get the type of this edge
			boolean directed = pNetwork.edgeType(edgeIndex);

			/*
			 * This code makes sure that indicies are linear... if nodes have been deleted
			 * (not sure why we would do that), then they might not be linear. I'm pretty
			 * sure we can remove this code pjm 8/6/08
			 */
			if (nodeIndicies[source] != source) {
				for (int i = 0; i < pNetwork.getNumNodes(); i++) {
					if (nodeIndicies[i] == source) {
						source = i;
						break;
					}
				}
			}

			/*
			 * This code makes sure that indicies are linear... if nodes have been deleted
			 * (not sure why we would do that), then they might not be linear. I'm pretty
			 * sure we can remove this code pjm 8/6/08
			 */
			if (nodeIndicies[target] != target) {
				for (int i = 0; i < pNetwork.getNumNodes(); i++) {
					if (nodeIndicies[i] == target) {
						target = i;
						break;
					}
				}

			}

			// A string that acts a flag to ensure that multi-edges are not lost
			String duplicate = "";

			// iterate through the existing edges in the network
			int existsCount = 0;
			CyEdge.Type edgeType = CyEdge.Type.OUTGOING;
			if (!directed) {
				edgeType = CyEdge.Type.UNDIRECTED;
			}
			Iterable<CyEdge> edges = cynetwork.getAdjacentEdgeIterable(nodes[source], edgeType);
			// cynetwork.getAdjacentEdgeIndicesArray(nodes[source].getRootGraphIndex(),
			// !directed, false,
			// directed);

			for (CyEdge next : edges) {
				// for (int e = 0; e < edges.length; e++) {
				// get the next edge
				// Edge next = cynetwork.getEdge(edges[e]);

				// If this edge already exists
				if (next.getTarget() == nodes[target]) {
					duplicate = "dup :";
					// Keep track of how many times this edge exists
					existsCount++;
				}
			}

			// Change the name if there are more than one other instances of this edge
			if (existsCount > 0) {
				duplicate += "(" + existsCount + ")";
			}

			// Create the edge between these two nodes
			System.out.println("CyEdge: (" + nodes[source] + "," + nodes[target] + ")");
			CyEdge edge = cynetwork.addEdge(nodes[source], nodes[target], directed);
			// Cytoscape.getCyEdge(nodes[source], nodes[target], Semantics.INTERACTION,
			// new String(duplicate + "(" + Math.min(source, target) + "," +
			// Math.max(source, target) + ")"), true,
			// directed);

			// Add this edge to the network
			// cynetwork.addEdge(edge);

			// increment the number of edges we have processed
			edgeCount++;
		}

		// Create a new network view
		CyNetworkView view = networkViewFactory.createNetworkView(cynetwork);

		// Add view to Cytoscape
		//CyNetworkViewManager networkViewManager = getService(bc, CyNetworkViewManager.class);
		networkViewManager.addNetworkView(view);
//		view.
		//  CyEventHelper eventHelper = adapter.getCyEventHelper();
		// eventHelper.flushPayloadEvents(); // will cause node views to be created...
		//
		// Now that the network is complete, create a view for it
		//CyNetworkView view = Cytoscape.createNetworkView(cynetwork);

		// Iterate through all of our nodes
		for (int i = 0; i < nodeCount; i++) {
			// Get the next node
			CyNode node = nodes[i];
			
			View<CyNode> nodeView = view.getNodeView(node); //addNodeView(node.getRootGraphIndex());

			// If the node has an x,y position
			//if (mHasData) {
				
				//Double x = nodeView.getVisualProperty(BasicVisualLexicon.NODE_X_LOCATION);
				//Double y = nodeView.getVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION);

				//nodeView.setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, mNodePositions[nodeIndicies[i]][0]);
				//nodeView.setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, mNodePositions[nodeIndicies[i]][1]);
			nodeView.setLockedValue(BasicVisualLexicon.NODE_WIDTH, 30.0);
			nodeView.setLockedValue(BasicVisualLexicon.NODE_HEIGHT, 30.0);

			nodeView.setVisualProperty(BasicVisualLexicon.NODE_LABEL, i+"");
			nodeView.setVisualProperty(BasicVisualLexicon.NODE_VISIBLE, true);
			//?
			//nodeView.setLockedValue(BasicVisualLexicon., value);
			nodeView.setLockedValue(BasicVisualLexicon.NODE_SHAPE, NodeShapeVisualProperty.ELLIPSE);
			//nodeView.set
			cynetwork.getRow(node).set(CyNetwork.NAME,  i+"");
			   
			//Double x = nodeView.getVisualProperty(BasicVisualLexicon.NODE_X_LOCATION);
			//	Double y = nodeView.getVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION);

			//	nodeView.setXPosition(mNodePositions[nodeIndicies[i]][0]);
			//	nodeView.setYPosition(mNodePositions[nodeIndicies[i]][1]);
		//	}
		}
/*
		// Apply the style
		if (mHasData) {
			// If there is data, apply the zoom, visual style and center
			view.applyVizmapper(mVisualStyle);
			view.setZoom(mZoom);
			((DGraphView) view).setCenter(mCenterX, mCenterY);

			((DGraphView) view).getComponent().setSize((int) mWidth, (int) mHeight);

			InnerCanvas ic = (InnerCanvas) ((DGraphView) view).getComponent();
			// BirdsEyeView bev =
			// (BirdsEyeView)Cytoscape.getDesktop().getBirdsEyeViewHandler().getBirdsEyeView();
			ic.setBounds(0, 0, (int) mWidth, (int) mHeight);
			((DGraphView) view).updateView();
			// ic.setMinimumSize(new Dimension((int)mWidth, (int)mHeight));
			// ic.setPreferredSize(new Dimension((int)mWidth, (int)mHeight));

			// ((JLayeredPane)((DGraphView)view).getComponent().getParent()).setSize(mWidth,mHeight);

		} else {
*/
			// If there was no data, apply the grid node layout and
			// FIt the graph to the screen.
			//view.applyLayout(new GridNodeLayout());
		 eventHelper.flushPayloadEvents();

		 
		 CyLayoutAlgorithm layout = layoutAlgorithmManager.getLayout("force-directed");
	     String columnForLayout = "";

		 final TaskIterator itr = layout.createTaskIterator(view, layout.getDefaultLayoutContext(), CyLayoutAlgorithm.ALL_NODE_VIEWS, columnForLayout);
		    try {
		        itr.next().run(new HeadlessTaskMonitor());
		    } catch (Exception e) {
		        throw new RuntimeException("Could not apply layout.", e);
		    }
		view.updateView();
			//view.fitContent();
//		}

		// Change the view back to the main Network Panel
	//	Cytoscape.getDesktop().getCytoPanel(SwingConstants.WEST).setSelectedIndex(0);

		// Return the result
		return cynetwork;

	}
}
