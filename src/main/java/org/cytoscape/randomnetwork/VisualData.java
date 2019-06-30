package org.cytoscape.randomnetwork;

import java.awt.geom.Point2D;
import java.util.Collection;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;

public class VisualData {

	/**
	 * The node labels for the network
	 */
	private String[] mNodeLabels;
	/**
	 * The edge labels
	 */
	private String[] mEdgeLabels;
	/**
	 * Node Positions
	 */
	private double mNodePositions[][];
	/**
	 * Which visual style the original CyNetwork uses
	 */
	// private VisualStyle mVisualStyle;
	/**
	 *
	 */
	private double mCenterX;
	/**
	 *
	 */
	private double mCenterY;
	/**
	 *
	 */
	private double mWidth;
	/**
	 *
	 */
	private double mHeight;
	/**
	 *
	 */
	private double mZoom;

	private int numNodes;
	private int numEdges;
	//private static CyNetworkViewFactory networkViewFactory = getService(bc, CyNetworkViewFactory.class);
	public VisualData(int N, int E) {
		numNodes = N;
		numEdges = E;
		mNodeLabels = new String[numNodes];
		mEdgeLabels = new String[numEdges];
	}

	public VisualData(VisualData other) {
		numNodes = other.getNumNodes();
		numEdges = other.getNumNodes();
		// create the data containers
		mNodeLabels = new String[numNodes];
		mEdgeLabels = new String[numEdges];
		mNodePositions = new double[numNodes][2];
		// mVisualStyle = pNetwork.getVisualStyle();
		mZoom = other.getZoom();
		mCenterX = other.getCenterX();
		mCenterY = other.getCenterY();
		mWidth = other.getWidth();
		mHeight = other.getHeight();

		// For each node
		for (int i = 0; i < numNodes; i++) {
			mNodeLabels[i] = other.getNodeLabels()[i];
			mNodePositions[i][0] = other.getNodePositions()[i][0];
			mNodePositions[i][1] = other.getNodePositions()[i][1];
		}
		
		for (int i = 0; i < numEdges; i++) {
            mEdgeLabels[i] = other.getEdgeLabels()[i];            
		}
	}
	
	public VisualData(CyNetworkView view) {
		//mNetworkName = pCyNetwork.getDefaultNetworkTable().getTitle();
		//network.
		// set this value = 0
	    //numNodes = network.getNodeCount();
		
	    Collection<View<CyNode>> node_views = view.getNodeViews();
	    numNodes = node_views.size();
	    
		//network.getNodeCount();
		//CyNetworkView networkView = Cytoscape.getCurrentNetworkView();

	    CyNetwork net = view.getModel();
	    //view.
	  /*  
	    Point2D pt = ((DGraphView) orgView).getCenter();
		mCenterX = pt.getX();
		mCenterY = pt.getY();

		mWidth = ((DGraphView) orgView).getComponent().getWidth();
		mHeight = ((DGraphView) orgView).getComponent().getHeight();
		mZoom = orgView.getZoom();
		
		
		// Save the Node Positions
		mNodePositions = new double[numNodes][2];

		// Save the node indicies
		int nodeIndicies[] = new int[numNodes];

		// Save the cyNodes
		CyNode cynodes[] = new CyNode[numNodes];

		//for (network.getNodeList()
		for (View<CyNode> nodeView : node_views) {
			Double x = nodeView.getVisualProperty(BasicVisualLexicon.NODE_X_LOCATION);
			Double y = nodeView.getVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION);	
		}

		// determine the number of edges
		numEdges = 0;

		
		Point2D pt = view.//((DGraphView) orgView).getCenter();
		mCenterX = pt.getX();
		mCenterY = pt.getY();

		mWidth = ((DGraphView) orgView).getComponent().getWidth();
		mHeight = ((DGraphView) orgView).getComponent().getHeight();
		mZoom = orgView.getZoom();
*/
	}

	/**
	 * @return the mNodeLabels
	 */
	public String[] getNodeLabels() {
		return mNodeLabels;
	}

	/**
	 * @return the mEdgeLabels
	 */
	public String[] getEdgeLabels() {
		return mEdgeLabels;
	}

	/**
	 * @return the mNodePositions
	 */
	public double[][] getNodePositions() {
		return mNodePositions;
	}

	/**
	 * @return the mCenterX
	 */
	public double getCenterX() {
		return mCenterX;
	}

	/**
	 * @return the mCenterY
	 */
	public double getCenterY() {
		return mCenterY;
	}

	/**
	 * @return the mWidth
	 */
	public double getWidth() {
		return mWidth;
	}

	/**
	 * @return the mHeight
	 */
	public double getHeight() {
		return mHeight;
	}

	/**
	 * @return the mZoom
	 */
	public double getZoom() {
		return mZoom;
	}

	/**
	 * @return the numNodes
	 */
	public int getNumNodes() {
		return numNodes;
	}

	/**
	 * @return the numEdges
	 */
	public int getNumEdges() {
		return numEdges;
	}

}