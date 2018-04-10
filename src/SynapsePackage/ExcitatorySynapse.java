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
public class ExcitatorySynapse extends AbstractSynapse {

	/**
	 * @param weight
	 * @param stype
	 * @param compartment
	 * @param ID
	 */
	public ExcitatorySynapse(double weight, SynapseForm stype, AbstractCompartment compartment, int ID) {
		super(weight, stype, compartment, ID);
		// TODO Auto-generated constructor stub
	}

}
