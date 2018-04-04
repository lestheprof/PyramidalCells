package SynapsePackage;

import CompartmentPackage.AbstractCompartment;

/** 
	 * abstract synapse type for all sorts of synapses
	 */
public abstract class AbstractSynapse {
	
	public int stepNo ; // simulation step number
	public double postSynapticActivation ; 


	public double weight ; 
	public SynapseForm synapseType ;
	public AbstractCompartment targetCompartment ;
	
			/**
		 * simply initialises weight and synapseType
		 * @param weight strength of synapse
		 * @param stype nature of synapse (from SynapseForm.java)
		 * @param compartment the compartment which this synapse targets
		 */
	public AbstractSynapse(double weight, SynapseForm stype, AbstractCompartment compartment) {


			this.weight = weight ; // strength
			this.synapseType = stype ; // EXCITATORY, INHIBITORY, SHUNTING
			this.targetCompartment = compartment ;
			this.stepNo = 0 ;
			this.postSynapticActivation = 0.0 ;
		
	}

}
