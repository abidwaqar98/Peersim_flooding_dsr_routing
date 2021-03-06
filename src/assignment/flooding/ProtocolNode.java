package assignment.flooding;

import peersim.config.*;
import peersim.core.*;
import peersim.transport.Transport;

import java.util.Collection;
import java.util.Iterator;
import peersim.cdsim.CDProtocol;
import peersim.edsim.EDProtocol;

/**
 * Event driven version of epidemic averaging.
 */
public class ProtocolNode implements CDProtocol, EDProtocol, Linkable {

	/** Neighbors currently in the cache */
	private Node[] cache;

	/** Time stamps currently in the cache */
	private int[] tstamps;

	/** * @param prefix string prefix for config properties */
	public ProtocolNode(String prefix) {
	}

	public Object clone() {
		return this;
	}

	// --------------------------------------------------------------------------
	/**
	 * * This is the standard method the define periodic activity. The frequency
	 * of execution of this method is defined by a
	 * {@link peersim.edsim.CDScheduler} component in the configuration.
	 */

	public void nextCycle(Node node, int pid) {
	}

	// --------------------------------------------------------------------------
	public ProtocolNode() {

	}

	// --------------------------------------------------------------------------
	/*** This is the standard method to define to process incoming messages. */
	public void processEvent(Node node, int pid, Object event) {
		
		NetworkInfo net_in = NetworkInfo.getInstance();
		Message aem = (Message) event;
		GeneralNode current_node = (GeneralNode) node;
		int msg_seq_no = aem.msg_seq_no;
		
		if(current_node.getID() == aem.dest_id)
		{
			System.out.println("Dest " + aem.dest_id + " received msg " + aem.msg_seq_no + " from node " + aem.sender.getID());
		}
		else if (current_node.msgs_seq_no.contains(msg_seq_no))
		{
			System.out.println("Node " + current_node.getID() + " has already sent msg " + msg_seq_no);
		}
		else
		{
			current_node.msgs_seq_no.add(msg_seq_no);
			
			Collection<Integer> neighbor = net_in.getGraph().getNeighbours((int)node.getID());
			if (aem.sender != null) {
				{
//					System.out.println("Node: " + current_node.getID() + " array size: " + current_node.msgs_seq_no.size());
					Iterator<Integer> itr = neighbor.iterator();
					while(itr.hasNext())
					{
						int node_id = itr.next();
						int node_pid = net_in.getInstance().getUnreliable_transport_pid();
						System.out.println("Node " + node.getID() + " sending message " + msg_seq_no + " to Node " + node_id);
						((Transport) node.getProtocol(FastConfig.getTransport(node_pid))).send(node, Network.get(node_id), aem, node_pid);
//						System.out.println("Cycle time: " + CommonState.getTime());
					}
				}
			}
		}
		
	}

	// ====================== Linkable implementation =====================
	/***
	 * Does not check if the index is out of bound (larger than
	 * {@link #degree()})
	 */
	public Node getNeighbor(int i) {
		return cache[i];
	}

	// --------------------------------------------------------------------
	/** Might be less than cache size. */
	public int degree() {
		int len = cache.length - 1;
		while (len >= 0 && cache[len] == null)
			len--;
		return len + 1;
	}

	// --------------------------------------------------------------------
	public boolean addNeighbor(Node node) {
		int i;
		for (i = 0; i < cache.length && cache[i] != null; i++) {
			if (cache[i] == node)
				return false;
		}
		if (i < cache.length) {
			if (i > 0 && tstamps[i - 1] < CommonState.getIntTime()) {
				// we need to insert to the first position
				for (int j = cache.length - 2; j >= 0; --j) {
					cache[j + 1] = cache[j];
					tstamps[j + 1] = tstamps[j];
				}
				i = 0;
			}
			cache[i] = node;
			tstamps[i] = CommonState.getIntTime();
			return true;
		} else
			throw new IndexOutOfBoundsException();
	}

	// --------------------------------------------------------------------
	public void pack() {
	}

	// --------------------------------------------------------------------
	public boolean contains(Node n) {
		for (int i = 0; i < cache.length; i++) {
			if (cache[i] == n)
				return true;
		}
		return false;
	}

	// --------------------------------------------------------------------
	public void onKill() {
		cache = null;
		tstamps = null;
	}
}