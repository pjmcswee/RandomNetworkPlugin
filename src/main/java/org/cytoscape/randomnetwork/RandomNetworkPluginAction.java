package org.cytoscape.randomnetwork;


import org.cytoscape.app.CyAppAdapter;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanel;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.randomnetwork.gui.MainPanel;
import org.cytoscape.randomnetwork.gui.RandomNetworkMainPanel;

import java.awt.event.ActionEvent;

public class RandomNetworkPluginAction extends AbstractCyAction {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final CyAppAdapter adapter;
    private CySwingApplication desktopApp;
    private final CyApplicationManager manager;
    private static final String MENU_NAME = "RandomNetworks";
    private final RandomNetworkMainPanel mainPanel;
    private final CytoPanel cytoPanelWest;

    public RandomNetworkPluginAction(final CyApplicationManager manager, final CyAppAdapter adapter,
                                 final CySwingApplication desktopApp, RandomNetworkMainPanel mainPanel) {
        super(MENU_NAME);
        this.adapter = adapter;
        this.manager = manager;
        this.desktopApp = desktopApp;

        setPreferredMenu("Apps");

        this.cytoPanelWest = this.desktopApp.getCytoPanel(CytoPanelName.WEST);
        this.mainPanel = mainPanel;

    }

    /**
     * @desc - This method opens the Slimscape settings panel on menu selection.
     * @param event - event triggered when menu item is clicked.
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        // Set slimpanel to visible
    	mainPanel.setVisible(true);
    }
}