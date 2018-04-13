package SynapsePackage;

import CompartmentPackage.AbstractCompartment;

/** 
	 * abstract synapse type for all sorts of synapses
	 */
public abstract class AbstractSynapse {
	
	private final double ALPHAARRAYSIZER = 7.0  ; // used to set length of alpha array
	
	public int stepNo ; // simulation step number
	public double postSynapticActivation ; 
	public int neuronID ;
	public int compartmentID ;
	public int synapseID ; // NB these start at 1
	
	public double samplingInterval ;
	public int samplingrate ;

	public double weight ; 
	public double alpha ;
	public double[] alphaArray ;
	public int alphaArrayLength;
	public SynapseForm synapseType ;
	public AbstractCompartment targetCompartment ;


	
			/**
		 * simply initialises weight and synapseType
		 * @param weight strength of synapse
		 * @param stype nature of synapse (from SynapseForm.java)
		 * @param compartment the compartment which this synapse targets
		 * @param synapseID synapse identity (starts at 1)
		 */
	public AbstractSynapse(double weight, SynapseForm stype, AbstractCompartment compartment, 
			int synapseID, double alpha) {


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
			this.alpha = alpha ;
			setupAlphaArray() ; // set up the array for transferring synaptic outputs. Note that this sums to 1.
	}
	
	/*
	 * Set up the alpha array
	 */
	public void setupAlphaArray() {
		// find the turning point of the alpha function = 1/alpha
		double maxtime = 1.0/this.alpha ;
		double lasttime = ALPHAARRAYSIZER * maxtime ;
		// calculate number of samples this corresponds to
		this.alphaArrayLength = (int) Math.floor(lasttime/samplingInterval) ;
		if (this.alphaArrayLength < 1) this.alphaArrayLength = 1 ; // must not be 0
		alphaArray = new double[this.alphaArrayLength] ;
		// fill the alpha array
		double alphaSum = 0 ;
		for (int i = 0 ; i < this.alphaArrayLength; i++){
			alphaArray[i] = ((i-1) * samplingInterval) * Math.exp(this.alpha * ((i-1) * samplingInterval)) ;
			alphaSum = alphaSum + alphaArray[i] ;
		}
		// normalise to add to 1. Length of 1 is a special case
		if (this.alphaArrayLength == 1) alphaArray[0] = 1 ;
		else for (int i = 0 ; i < this.alphaArrayLength; i++)
			alphaArray[i] = alphaArray[i] / alphaSum ;
	}

}
