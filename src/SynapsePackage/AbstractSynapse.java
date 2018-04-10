package SynapsePackage;

import CompartmentPackage.AbstractCompartment;

/** 
	 * abstract synapse type for all sorts of synapses
	 */
public abstract class AbstractSynapse {
	
	public int stepNo ; // simulation step number
	public double postSynapticActivation ; 
	public int neuronID ;
	public int compartmentID ;
	public int synapseID ;
	
	public double samplingInterval ;
	public int samplingrate ;

	public double weight ; 
	public SynapseForm synapseType ;
	public AbstractCompartment targetCompartment ;
	
			/**
		 * simply initialises weight and synapseType
		 * @param weight strength of synapse
		 * @param stype nature of synapse (from SynapseForm.java)
		 * @param compartment the compartment which this synapse targets
		 * @param synapseID synapse identity (starts at 1)
		 */
	public AbstractSynapse(double weight, SynapseForm stype, AbstractCompartment compartment, int synapseID) {


			this.weight = weight ; // strength
			this.synapseType = stype ; // EXCITATORY, INHIBITORY, SHUNTING
			this.targetCompartment = compartment ;
			this.stepNo = 0 ;
			this.postSynapticActivation = 0.0 ;
			this.compartmentID = targetCompartment.compartmentID ;
			this.neuronID = targetCompartment.myNeuron.neuronID ;
			this.synapseID = synapseID ;
			this.samplingInterval = targetCompartment.samplingInterval ;
			this.samplingrate = targetCompartment.samplingrate ;
					
	}

}
