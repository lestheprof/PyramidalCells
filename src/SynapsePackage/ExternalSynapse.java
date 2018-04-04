/**
 * 
 */
package SynapsePackage;

import CompartmentPackage.AbstractCompartment;

/**
 * @author lss
 * receives input from outside of the system. May be any type of synapse
 */
public class ExternalSynapse extends AbstractSynapse {

	/**
	 * @param weight
	 * @param stype
	 * @param compartment
	 */
	public ExternalSynapse(double weight, SynapseForm stype, AbstractCompartment compartment) {
		super(weight, stype, compartment);
		// TODO Auto-generated constructor stub
	}
	
	public void setExternalInputs(){
		//takes a list of times (integer simulation times), and stores them
		// these originate outside of the system (in a file?)
		// used in runStep to show that an external action potential has arrived here
	}
	
	public double runStep(){
		
		return postSynapticActivation ; 	
	}
}
