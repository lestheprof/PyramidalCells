/**
 * 
 */
package SynapsePackage;

import CompartmentPackage.AbstractCompartment;

/**
 * @author lss
 * Internal synapse (from an axonic output to a compartment). Excitatory.
 */
public class ExcitatorySynapse extends AbstractSynapse {

	/**
	 * @param weight
	 * @param stype
	 * @param compartment
	 */
	public ExcitatorySynapse(double weight, SynapseForm stype, AbstractCompartment compartment) {
		super(weight, stype, compartment);
		// TODO Auto-generated constructor stub
	}

}
