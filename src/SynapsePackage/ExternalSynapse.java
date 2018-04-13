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

	private Double[] spikeTimes = null; // external spike times for this synapse
	private boolean debug = true ;
	/**
	 * @param weight
	 * @param stype
	 * @param compartment
	 * @param ID
	 * @param alpha
	 */
	public ExternalSynapse(double weight, SynapseForm stype, AbstractCompartment compartment, int ID, double alpha) {
		super(weight, stype, compartment, ID, alpha);
		// TODO Auto-generated constructor stub
	}
	
	public void setExternalInputs(Double[] spikeTimes){
		//takes a list of times of external spikes, and stores them
		// these originate outside of the system (in a file referenced in the argc's
		// used in runStep to show that an external action potential has arrived here
		this.spikeTimes = spikeTimes  ;
		if (debug){
			System.out.println(targetCompartment.compartmentType) ;
			System.out.print("Synapse " + synapseID + " spike times " );
			for (int spikeNo = 0; spikeNo < spikeTimes.length; spikeNo++)
				System.out.print(spikeTimes[spikeNo].doubleValue() + " ");
			System.out.println() ;
			
		}
		
		// 
	}
	
	public double runStep(double currentTime){
		
		return postSynapticActivation ; 	
	}
}
