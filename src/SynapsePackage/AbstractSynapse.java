package SynapsePackage;
	/** 
	 * abstract synapse type for all sorts of synapses
	 */
public abstract class AbstractSynapse {

	public double weight ; 
	public SynapseForm synapseType ;
	
			/**
		 * simply initialises weight and synapseType
		 * @param weight strength of synapse
		 * @param stype nature of synapse (from SynapseForm.java)
		 */
	public AbstractSynapse(double weight, SynapseForm stype) {


			this.weight = weight ; // strength
			this.synapseType = stype ; // EXCITATORY, INHIBITORY, SHUNTING
		
	}

}
