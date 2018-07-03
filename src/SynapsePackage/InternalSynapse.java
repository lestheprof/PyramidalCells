/**
 * 
 */
package SynapsePackage;

import CompartmentPackage.AbstractCompartment;

/**
 * @author lss
 * Internal synapse (from an axonic output to a compartment). Excitatory.
 * Note that synapse numbers start at 1. Synapse 0 not used.
 */
public class InternalSynapse extends AbstractSynapse {

	public double delay ; // synaptic delay (i.e. axon + synapse) in seconds
	private int delaySamples ; // delay in samples
	/**
	 * @param weight
	 * @param stype
	 * @param compartment
	 * @param ID
	 */
	public InternalSynapse(double weight, SynapseForm stype, AbstractCompartment compartment, int ID, double alpha, double delay) {
		super(weight, stype, compartment, ID, alpha);
		// initialise internal synapse object variables
		this.delay = delay ;
		this.delaySamples =  (int) Math.ceil(delay * this.samplingrate) ; // delay in sample times
		// TODO Auto-generated constructor stub
	}
	
	public double runStep(double currentTime){
		return 0 ; // for now
	}

}
