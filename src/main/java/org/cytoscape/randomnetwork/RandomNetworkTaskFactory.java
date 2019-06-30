package org.cytoscape.randomnetwork;

import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.session.CyNetworkNaming;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;

public class RandomNetworkTaskFactory extends AbstractTaskFactory {

	private final CyNetworkFactory cnf;
	private final CyNetworkViewFactory cnvf;
	private final CyNetworkViewManager networkViewManager;
	private final CyNetworkManager networkManager;
	private final CyNetworkNaming cyNetworkNaming;
	
	public RandomNetworkTaskFactory(CyNetworkNaming cyNetworkNaming, CyNetworkFactory cnf, CyNetworkManager networkManager, CyNetworkViewFactory cnvf,
			 final CyNetworkViewManager networkViewManager){

		this.cnf = cnf;
		this.cnvf = cnvf;
		this.networkViewManager = networkViewManager;
		this.networkManager = networkManager;
		this.cyNetworkNaming = cyNetworkNaming;

	}
	
	public TaskIterator createTaskIterator(){
		return new TaskIterator(new RandomNetworkTask(cyNetworkNaming, cnf,networkManager, cnvf, networkViewManager));
	}
}
