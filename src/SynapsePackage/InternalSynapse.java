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

	/**
	 * @param weight
	 * @param stype
	 * @param compartment
	 * @param ID
	 */
	public InternalSynapse(double weight, SynapseForm stype, AbstractCompartment compartment, int ID, double alpha) {
		super(weight, stype, compartment, ID, alpha);
		// TODO Auto-generated constructor stub
	}
	
	public double runStep(double currentTime){
		return 0 ; // for now
	}

}
