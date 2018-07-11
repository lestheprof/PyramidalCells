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
	private int spikeTimeIndex;
	private boolean inspike ;// set when we are inside the alpha function
	private boolean debug = true ;
	
	/**
	 * @param weight weight for thissynapse
	 * @param stype type of synapse: see SynapseForm
	 * @param compartment target (postsynaptic) compartment for this synapse
	 * @param ID identity number
	 * @param alpha alpha value
	 */
	public ExternalSynapse(double weight, SynapseForm stype, AbstractCompartment compartment, int ID, double alpha) {
		super(weight, stype, compartment, ID, alpha);
	// set 	spike time index to -1 ;
		spikeTimeIndex = -1 ;
		inspike = false ;
	}
	
	public void setExternalInputs(Double[] spikeTimes){
		//takes a list of times of external spikes, and stores them
		// these originate outside of the system (in a file referenced in the argc's
		// used in runStep to show that an external action potential has arrived here
		this.spikeTimes = spikeTimes  ;
		if (debug){
			System.out.print("Neuron id = " + this.neuronID + "." + targetCompartment.compartmentType) ;
			System.out.print(":  Synapse " + synapseID + " spike times " );
			for (int spikeNo = 0; spikeNo < spikeTimes.length; spikeNo++)
				System.out.print(spikeTimes[spikeNo].doubleValue() + " ");
			System.out.println() ;
			
		}
		
		// 
	}
	
	public double runStep(double currentTime){
		// calculate the post-synaptic activity from this synapse and return it
		// this version restarts the alpha function when a new spike occurs
		// is there a new spike at this time
		if ((spikeTimeIndex+1 < spikeTimes.length) && (spikeTimes[spikeTimeIndex + 1] <= currentTime)){
			// there is a new spike
			spikeTimeIndex = spikeTimeIndex + 1 ; // point to new spike
			inspike = true ;
			alphaIndex = 0 ;
		}
		else{
			// continue either with old spike or no spike
			if (! inspike){
				postSynapticActivation = 0;
				return postSynapticActivation ;
			}
			else{
				// where are we in the alpha function?
				postSynapticActivation = this.weight * this.alphaArray[this.alphaIndex] ;
				alphaIndex = alphaIndex + 1 ;
				if (alphaIndex == alphaArrayLength){
					inspike = false ;
				}
				return postSynapticActivation ;		
			}
		}
		
		postSynapticActivation = this.weight * this.alphaArray[this.alphaIndex] ;
		alphaIndex = alphaIndex + 1 ;
		if (alphaIndex == alphaArrayLength){
			inspike = false ;
		}
		return postSynapticActivation ; 	
	}
}
