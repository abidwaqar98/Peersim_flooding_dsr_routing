/*
 * Copyright (c) 2003-2005 The BISON Project
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 2 as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 */

package assignment.dsr;

import java.io.BufferedReader;
import java.io.IOException;

import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;

/**
 * <p>
 * This intialization class collects the simulation parameters from the config
 * file and generates uniformly random 2d-coordindates for each node. The
 * coordianates are distributed on a unit (1.0) square.
 * </p>
 * <p>
 * The first node in the {@link Network} is considered as the root node and its
 * coordinate is set to the center of the square.
 * </p>
 * 
 * 
 * @author Gian Paolo Jesi
 */
public class Initializer implements Control {
	/**
	 * The protocol to operate on.
	 * 
	 * @config
	 */
	private static final String PAR_PROT = "protocol";

	/** Protocol identifier, obtained from config property {@link #PAR_PROT}. */
	private static int pid;

	/**
	 * Standard constructor that reads the configuration parameters. Invoked by
	 * the simulation engine.
	 * 
	 * @param prefix
	 *            the configuration prefix for this class.
	 */
	public Initializer(String prefix) {
		pid = Configuration.getPid(prefix + "." + PAR_PROT);
	}

	/**
	 * Initialize the node coordinates. The first node in the {@link Network} is
	 * the root node by default and it is located in the middle (the center of
	 * the square) of the surface area.
	 */
	public boolean execute() {
		// Set the root: the index 0 node by default.
		Node n = Network.get(0);
		Coordinates prot = (Coordinates) n.getProtocol(pid);
		prot.setX(0.5);
		prot.setY(0.5);

		for (int i = 1; i < Network.size(); i++) 
		{
			n = Network.get(i);
			prot = (Coordinates) n.getProtocol(pid);
			prot.setX(Integer.valueOf(0));
			prot.setY(Integer.valueOf(0));
		}	
		return false;
	}

}
