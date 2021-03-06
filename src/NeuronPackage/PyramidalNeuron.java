package NeuronPackage;

import CompartmentPackage.*;
import SynapsePackage.ExternalSynapse;
import SynapsePackage.SynapseForm;

import java.util.ArrayList;


public class PyramidalNeuron extends AbstractNeuron {
// this neuron consists of 4 compartments below
	public ApicalTuft apicalTuft ;
	public ApicalDendrite apicalDendrite ;
	public AxonHillock axonHillock ;
	public BasalDendrite basalDendrite ;
	private ExternalSynapse[] extDrivingSynapses ;
	private ExternalSynapse[] extContextSynapses ;
	public int transferfunction ; // used to select transfer function in apical dendrite and axon hillock
	public double K1 ; // only meaningful when transferfunction == 2: used as in Kay & Phillips 2011
	public double K2 ; // only meaningful when transferfunction == 2
	// public List<Double> spikesOut = null ; now in AbstractNeuron
	
	// private boolean debug = false ; now declared in AbstractNeuron
	
	/*
	 * ID String identity of the neuron
	 * samplingRate is sampling rate used to convert times to sample numbers
	 * tauBasal is time constant for basal dendrites
	 * logisticGradientBasal gradient for Basal compartment
	 * logisticInterceptBasal intercept for Basal compartment
	 * tauApicalTuft is time constant for Apical tuft dendrites
	 * logisticGradientTuft gradient for apical tuft compartment
	 * logisticInterceptTuft gradient for tuft compartment
	 * apical_multiplier multiplier used in apical dendrite compartment
	 * apical_gradient gradient used in apical compartment
	 * threshold for axon hillock
	 * 
	 */
	public PyramidalNeuron(int ID, int samplingRate, double tauBasal, double logisticGradientBasal, double logisticInterceptBasal, double tauApicalTuft, 
			double logisticGradientTuft, double logisticInterceptTuft, double apical_multiplier ,double apical_gradient, double threshold, double refractoryPeriod, 
			int transferfunction, double K1, double K2, boolean debug) {
		super(ID, samplingRate, debug);
// set up the compartments of this neuron
		// Pyramidal neuron has 4 compartments: these are also numbered for identification purposes
		this.transferfunction = transferfunction ;
		this.K1 = K1 ;
		this.K2 = K2 ;
		this.apicalTuft = new ApicalTuft(this, 2, tauApicalTuft, logisticGradientTuft,  logisticInterceptTuft, debug) ;
		this.apicalDendrite = new ApicalDendrite(this,apical_multiplier, apical_gradient, 3, transferfunction, debug) ;
		this.axonHillock = new AxonHillock(this,4, threshold, refractoryPeriod, transferfunction, K1, K2, debug) ;
		this.spikingCompartment = this.axonHillock ;
		this.basalDendrite = new BasalDendrite(this, 1, tauBasal,logisticGradientBasal,  logisticInterceptBasal,  debug) ;
		this.spikesOut = new ArrayList<> () ;

	}
	
	/*
	 * Create a pyramidal neuron from t PyramidalNeuyronInfo descriptor
	 */
	public PyramidalNeuron(PyramidalNeuronInfo p1, boolean debug) {
		super(p1.identity, p1.samplingRate, debug);
// set up the compartments of this neuron
		// Pyramidal neuron has 4 compartments: these are also numbered for identification purposes
		this.transferfunction = p1.transferfunction ;
		this.K1 = p1.K1 ;
		this.K2 = p1.K2 ;
		this.apicalTuft = new ApicalTuft(this, 2, p1.tauApicalTuft, p1.logisticGradientTuft, p1.logisticInterceptTuft, debug) ; // needs grad, intercept added
		this.apicalDendrite = new ApicalDendrite(this, p1.apicalMultiplier, p1.apicalGradient, 3, p1.transferfunction,
			debug) ;
		this.axonHillock = new AxonHillock(this,4, p1.threshold, p1.refractoryPeriod, transferfunction, p1.K1, p1.K2, debug) ;
		this.spikingCompartment = this.axonHillock ;
		this.basalDendrite = new BasalDendrite(this, 1, p1.tauBasal,p1.logisticGradientBasal, p1.logisticInterceptBasal, debug) ;  // needs grad, intercept added
		this.spikesOut = new ArrayList<> () ;

		

	}
	/*
	 * extSynapticWeights is the array of external synaptic weights read from the file)
	 * alpha is the alpha value for the temporal distribution of post-synaptic output
	 */
	public void setUpExternalDrivingSynapses(double [][] extSynapticWeights, double alpha){
		// find the highest synapse number
		int nExtDrivingSynapses = 0 ;
		for (int index = 0 ; index < extSynapticWeights.length ; index++){
			// use only synapses to this neuron
			if (extSynapticWeights[index][0] == this.neuronID){
			if (nExtDrivingSynapses < (int)(Math.round(extSynapticWeights[index][1])))
				nExtDrivingSynapses = (int)(Math.round(extSynapticWeights[index][1])) ;
			}	
		}
		nExtDrivingSynapses = nExtDrivingSynapses + 1 ; // because the numbers start at 1. Synapse 0 not used
		// set up an array of external driving synapses
		extDrivingSynapses = new ExternalSynapse[nExtDrivingSynapses] ; // note these are not initialised
		// set these up in this neuron's BasalDendrite
		for (int i=0; i<extSynapticWeights.length; i++) {
			// use only synapses to this neuron
			if (extSynapticWeights[i][0] == this.neuronID){
			// create the synapse. Note that synapse id's start at 1
			int synapseNumber = (int)(Math.round(extSynapticWeights[i][1])) ;
			if (synapseNumber > 0)
			extDrivingSynapses[synapseNumber] = // initialise the synapse
					new ExternalSynapse(extSynapticWeights[i][2], SynapseForm.EXCITATORY, 
							this.basalDendrite, synapseNumber, alpha) ;
			}
		}
		// associate this synaptic array with the basal dendrite compartment
		this.basalDendrite.setExternalSynapses(extDrivingSynapses);
	}
	
	/*
	 * nExtContextSynapses synapses is the number of context synapses (external for now)
	 * alpha is the alpha value for the temporal distribution of post-synaptic output
	 */
	public void setUpExternalContextSynapses(double [][] extSynapticWeights, double alpha){
		int nExtcontextSynapses = 0 ;
		for (int index = 0 ; index < extSynapticWeights.length ; index++){
			// use only synapses to this neuron
			if (extSynapticWeights[index][0] == this.neuronID){
			if (nExtcontextSynapses < (int)(Math.round(extSynapticWeights[index][1])))
				nExtcontextSynapses = (int)(Math.round(extSynapticWeights[index][1])) ;
			}	
		}
		nExtcontextSynapses = nExtcontextSynapses + 1 ; // because the numbers start at 1. Synapse 0 not used
		// set up an array of external driving synapses
		extContextSynapses = new ExternalSynapse[nExtcontextSynapses] ; // note these are not initialised
		// set these up in this neuron's BasalDendrite
		for (int i=0; i<extSynapticWeights.length; i++) {
			// use only synapses to this neuron
			if (extSynapticWeights[i][0] == this.neuronID) {
				// create the synapse. Note that synapse id's start at 1
				int synapseNumber = (int) (Math.round(extSynapticWeights[i][1]));
				if (synapseNumber > 0)
					extContextSynapses[synapseNumber] = // initialise thesynapse
							new ExternalSynapse(extSynapticWeights[i][2], SynapseForm.EXCITATORY, this.apicalTuft,
									synapseNumber, alpha);
			}
		}
		// associate this synaptic array with the apical tuft
		this.apicalTuft.setExternalSynapses(extContextSynapses);
	}
	
	/*
	 * @param drivingSpikeTimes driving spike times
	 */
	public void setDrivingInputs(double [][] drivingSpikeTimes){
		// store these at the basal dendrite
		// inputs are N by 3, namely neuron, synapse, time
		// use only those with this neuronID
		// how many are there?
		int mySpikesNo = 0 ; // count local spikes
		for (int i = 0; i<drivingSpikeTimes.length; i++){
			if (drivingSpikeTimes[i][0] == this.neuronID) mySpikesNo = mySpikesNo + 1 ;
		}
		double [][] myDrivingSpikes  = new double[mySpikesNo][3] ;
		mySpikesNo = 0 ; // reset local spike count for storing them
		for (int i = 0; i<drivingSpikeTimes.length; i++){
			if (drivingSpikeTimes[i][0] == this.neuronID) {
				myDrivingSpikes[mySpikesNo] = drivingSpikeTimes[i] ;
				mySpikesNo = mySpikesNo + 1 ;
			}
		}
		// send these to the basal dendrite
		basalDendrite.setDrivingSpikes(myDrivingSpikes);
	}

	/*
	 * @param contextSpikeTimes driving spike times
	 */
	public void setContextualInputs(double [][] contextSpikeTimes){
		// store these at the apical tuft
		// inputs are N by 3, namely neuron, synapse, time
		// use only those with this neuronID
		// how many are there?
		int mySpikesNo = 0 ; // count local spikes
		for (int i = 0; i<contextSpikeTimes.length; i++){
			if (contextSpikeTimes[i][0] == this.neuronID) mySpikesNo = mySpikesNo + 1 ;
		}
		double [][] myContextSpikes  = new double[mySpikesNo][3] ;
		mySpikesNo = 0 ; // reset local spike count for storage
		for (int i = 0; i<contextSpikeTimes.length; i++){
			if (contextSpikeTimes[i][0] == this.neuronID) { 
				myContextSpikes[mySpikesNo] = contextSpikeTimes[i] ;
				mySpikesNo = mySpikesNo + 1 ;
			}
		}
		// send these to the apical tuft
		apicalTuft.setContextSpikes(myContextSpikes);
	}

	public void run(double currentTime){
		// run neuron for a single time step
		basalDendrite.run(currentTime); // update state of basal dendrite
//		if (debug){
//			if (basalDendrite.activation > 0)
//				System.out.println("time = " + currentTime + " Basal D activation = " + basalDendrite.activation);
//		}
		apicalTuft.run(currentTime); // update state of  apical dendrite
//		if (debug){
//			if (apicalTuft.activation > 0)
//				System.out.println("time = " + currentTime + " Apical Tuft activation = " + apicalTuft.activation);
//		}
		apicalDendrite.run(currentTime); // use the above two to nonlinearly mix
//		if (debug){
//			//  (apicalDendrite.activation > 0)
//				System.out.println("time = " + currentTime + " apical Dendrite activation = " + apicalDendrite.activation);
//		}
		// what's below won't work: needs the code of the runs above to be instantiated (done)
		if (axonHillock.runAndSpike(currentTime)) // attempt to generate output spikes
		{
			// spike has been generated
			this.justSpiked = true ;
			spikesOut.add(currentTime) ; // add to list of spikes
			// but what else?
			// reset the activation of the Basal Dendrite
			// basalDendrite.activation = 0 ;
			basalDendrite.prelogisticActivation = 0 ; // set local variable to 0 as well
			// what should we do about the apical compartment activation?
			apicalTuft.prelogisticActivation = 0 ;
		}
		else this.justSpiked = false ;
		if (debug){
			// if (axonHillock.activation > 0)
				System.out.println(currentTime + "," +  basalDendrite.activation + "," + apicalTuft.activation + "," +
						+ apicalDendrite.activation + "," + axonHillock.activation);
		}
	}
	

}
