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
	 * @param neuron
	 * @param compartmentID
	 */
	public AbstractSpikingCompartment(AbstractNeuron neuron, int compartmentID) {
		super(neuron, compartmentID); 
		outgoingSynapses = new ArrayList <InternalSynapse>() ;
		// TODO Auto-generated constructor stub
	}
	
	public void addOutgoingSynapse(double weight,  double delay, SynapseForm stype, AbstractSpikingCompartment fromCompartment, 
			AbstractCompartment toCompartment, int ID, double alpha){
		// create the synapse and add it to the list
		InternalSynapse newSynapse = new InternalSynapse(weight, delay, stype, fromCompartment, toCompartment, ID, alpha) ;
		outgoingSynapses.add(newSynapse) ;
		// add it to the postsynaptic neuron compartment list of incoming synapses	
		newSynapse.targetCompartment.addIncomingSynapse(newSynapse);
	}

}
