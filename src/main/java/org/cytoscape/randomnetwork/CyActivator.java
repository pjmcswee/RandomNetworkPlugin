package org.cytoscape.randomnetwork;

import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.randomnetwork.gui.GenerateRandomPanel;
import org.cytoscape.randomnetwork.gui.MainPanel;
import org.cytoscape.randomnetwork.gui.RandomNetworkMainPanel;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.layout.CyLayoutAlgorithm;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.CyNetworkFactory;
import org.osgi.framework.BundleContext;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.util.swing.OpenBrowser;
import org.cytoscape.work.swing.DialogTaskManager;
import java.util.Properties;
import org.cytoscape.application.swing.*;
import org.cytoscape.event.CyEventHelper;


public class CyActivator extends AbstractCyActivator {
	public CyActivator() {
		super();
	}

	public void start(BundleContext context) throws Exception {
		CyApplicationManager manager = getService(context, CyApplicationManager.class);
		CySwingAppAdapter adapter = getService(context, CySwingAppAdapter.class);
		CySwingApplication desktopApp = adapter.getCySwingApplication();
        OpenBrowser openBrowser = getService(context,OpenBrowser.class);
        CyEventHelper eventHelper = getService(context, CyEventHelper.class);
        CyNetworkFactory networkFactory = getService(context, CyNetworkFactory.class);
        CyNetworkManager networkManager = getService(context, CyNetworkManager.class);
        CyNetworkViewFactory networkViewFactory = getService(context, CyNetworkViewFactory.class);
        CyNetworkViewManager networkViewManager = getService(context, CyNetworkViewManager.class);
        VisualMappingManager visualMappingManager = getService(context,VisualMappingManager.class);

        CyLayoutAlgorithm layoutManager = getService(context, CyLayoutAlgorithm.class);
		CyLayoutAlgorithmManager layoutAlgorithmManager  = getService(context, CyLayoutAlgorithmManager.class);
		
		DialogTaskManager dialogTaskManager = getService(context, DialogTaskManager.class);
		
		Properties properties = new Properties();

		ConverterUtils.init(networkFactory,
							networkManager,
						    networkViewFactory,
						    networkViewManager,
						    eventHelper,
						    layoutManager,
						    layoutAlgorithmManager,
						    manager,
						    dialogTaskManager);	
		
		RandomNetworkMainPanel randomPanel = new RandomNetworkMainPanel(manager, adapter, openBrowser, eventHelper, networkFactory, networkManager,
		              networkViewFactory, networkViewManager, visualMappingManager);
		//MainPanel randomPanel = new MainPanel(new GenerateRandomPanel(0), manager, adapter, openBrowser, eventHelper, networkFactory, networkManager,
        //        networkViewFactory, networkViewManager, visualMappingManager);
		registerService(context, randomPanel, CytoPanelComponent.class, properties);

		RandomNetworkPluginAction randomNetworksPluginAction = new RandomNetworkPluginAction(manager, adapter, desktopApp, randomPanel);
		registerService(context, randomNetworksPluginAction, CyAction.class, properties);
	}
	
	/*
	public void start(BundleContext bc) {

		CyNetworkNaming cyNetworkNamingServiceRef = getService(bc,CyNetworkNaming.class);
		
		CyNetworkFactory cyNetworkFactoryServiceRef = getService(bc,CyNetworkFactory.class);
		CyNetworkManager cyNetworkManagerServiceRef = getService(bc,CyNetworkManager.class);

		CyNetworkViewFactory cyNetworkViewFactoryServiceRef = getService(bc,CyNetworkViewFactory.class);
		CyNetworkViewManager cyNetworkViewManagerServiceRef = getService(bc,CyNetworkViewManager.class);
		
		RandomNetworkTaskFactory randomNetworkTaskFactory = new RandomNetworkTaskFactory(cyNetworkNamingServiceRef, cyNetworkFactoryServiceRef,cyNetworkManagerServiceRef, cyNetworkViewFactoryServiceRef,cyNetworkViewManagerServiceRef);
		ConverterUtils.init(cyNetworkFactoryServiceRef,
				 			cyNetworkViewFactoryServiceRef,
				 			cyNetworkViewManagerServiceRef);		
		
		Properties randomNetworkTaskFactoryProps = new Properties();
		randomNetworkTaskFactoryProps.setProperty("preferredMenu","Apps.RandomNetworks");
		randomNetworkTaskFactoryProps.setProperty("title","Generate Random Networks");
		registerService(bc,randomNetworkTaskFactory,TaskFactory.class, randomNetworkTaskFactoryProps);
	}	
	*/
}
