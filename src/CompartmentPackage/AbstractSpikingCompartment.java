/**
 * 
 */
package CompartmentPackage;

import java.util.List;
import java.util.ArrayList ;

import NeuronPackage.AbstractNeuron;
import SynapsePackage.InternalSynapse;
import SynapsePackage.SynapseForm;

/**
 * @author lss
 *
 */
public abstract class AbstractSpikingCompartment extends AbstractCompartment {

	protected double lastSpikeTime = -1 ;
	// has a list of the synapses for which this is the firing compartment of the presynaptic neuron
	public List <InternalSynapse> outgoingSynapses = null;
	
	/**
	 * @param neuron neuron to which this compartment belongs
	 * @param compartmentID ID pof compartment
	 */
	public AbstractSpikingCompartment(AbstractNeuron neuron, int compartmentID) {
		super(neuron, compartmentID); 
		outgoingSynapses = new ArrayList <InternalSynapse>() ;
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * adds an outgoing synapse to this spiking compartment
	 * @param weight weight of synapse
	 * @param delay delay in seconds
	 * @param stype synapse type: EXCITATORY or INHIBITORY
	 * @param fromCompartment presynaptic compartment of this synapse
	 * @param toCompartment postsynaptic sompartment for this synapse
	 * @param ID identity number
	 * @param alpha alpha value for this synapse
	 */
	public void addOutgoingSynapse(double weight,  double delay, SynapseForm stype, AbstractSpikingCompartment fromCompartment, 
			AbstractCompartment toCompartment, int ID, double alpha){
		// create the synapse and add it to the list
		InternalSynapse newSynapse = new InternalSynapse(weight, delay, stype, fromCompartment, toCompartment, ID, alpha) ;
		outgoingSynapses.add(newSynapse) ;
		// add it to the postsynaptic neuron compartment list of incoming synapses	
		newSynapse.targetCompartment.addIncomingSynapse(newSynapse);
	}

}
