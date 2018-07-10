/**
 * 
 */
package SynapsePackage;

import CompartmentPackage.AbstractCompartment;
import CompartmentPackage.AbstractSpikingCompartment;
import NeuronPackage.AbstractNeuron;


/**
 * @author lss
 * Internal synapse (from an axonic output to a compartment). Excitatory.
 * Note that synapse numbers start at 1. Synapse 0 not used.
 */
public class InternalSynapse extends AbstractSynapse {

	public double delay ; // synaptic delay (i.e. axon + synapse) in seconds
	private int delaySamples ; // delay in samples
	public AbstractSpikingCompartment fromCompartment ;
	public AbstractNeuron fromNeuron ;
	/**
	 * @param weight
	 * @param stype
	 * @param compartment
	 * @param ID
	 */
	public InternalSynapse(double weight,  double delay, SynapseForm stype, AbstractSpikingCompartment fromCompartment, 
			AbstractCompartment toCompartment, int ID, double alpha) {
		super(weight, stype, toCompartment, ID, alpha);
		// initialise internal synapse object variables
		this.fromCompartment = fromCompartment ;
		this.fromNeuron = fromCompartment.myNeuron ;
		this.delay = delay ;
		this.delaySamples =  (int) Math.ceil(delay * this.samplingrate) ; // delay in sample times
	}
	
	public double runStep(double currentTime){
		return 0 ; // for now
	}

}
