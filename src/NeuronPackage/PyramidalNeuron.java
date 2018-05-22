package NeuronPackage;

import CompartmentPackage.*;
import SynapsePackage.ExternalSynapse;
import SynapsePackage.SynapseForm;

import java.util.ArrayList;
import java.util.List;


public class PyramidalNeuron extends AbstractNeuron {
// this neuron consists of 4 compartments below
	public ApicalTuft apicalTuft ;
	public ApicalDendrite apicalDendrite ;
	public AxonHillock axonHillock ;
	public BasalDendrite basalDendrite ;
	private ExternalSynapse[] extDrivingSynapses ;
	private ExternalSynapse[] extContextSynapses ;
	public List<Double> spikesOut = null ;
	
	private boolean debug = false ;
	
	/*
	 * ID String identity of the neuron
	 * samplingRate is sampling rate used to convert times to sample numbers
	 * tauBasal is time constant for basal dendrites
	 * tauApical is time constant for Apical tuft dendrites
	 */
	public PyramidalNeuron(int ID, int samplingRate, double tauBasal, double tauApical, 
			double apical_multiplier ,double apical_gradient, double threshold) {
		super(ID, samplingRate);
// set up the compartments of this neuron
		// Pyramidal neuron has 4 compartments: these are also numbered for identification purposes
		this.apicalTuft = new ApicalTuft(this, 2, tauApical) ;
		this.apicalDendrite = new ApicalDendrite(this,apical_multiplier, apical_gradient, 3) ;
		this.axonHillock = new AxonHillock(this,4, threshold) ;
		this.basalDendrite = new BasalDendrite(this, 1, tauBasal) ;
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
			// create the synapse. Note that synapse id's start at 1
			int synapseNumber = (int)(Math.round(extSynapticWeights[i][1])) ;
			if (synapseNumber > 0)
			extDrivingSynapses[synapseNumber] = // initialise the synapse
					new ExternalSynapse(extSynapticWeights[i][2], SynapseForm.EXCITATORY, this.basalDendrite, i+1, alpha) ;
			
		}
		// associate this synaptic array with the basal dendrite compartment
		this.basalDendrite.setExternalSynapses(extDrivingSynapses);
	}
	
	/*
	 * nExtContextSynapses synapses is the number of context synapses (external for now)
	 * alpha is the alpha value for the temporal distribution of post-synaptic output
	 */
	public void setUpExternalContextSynapses(double [][] extSynapticWeights, double alpha){
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
		extContextSynapses = new ExternalSynapse[nExtDrivingSynapses] ; // note these are not initialised
		// set these up in this neuron's BasalDendrite
		for (int i=0; i<extSynapticWeights.length; i++) {
			// create the synapse. Note that synapse id's start at 1
			int synapseNumber = (int)(Math.round(extSynapticWeights[i][1])) ;
			if (synapseNumber > 0)
			extContextSynapses[synapseNumber] = // initialise the synapse
					new ExternalSynapse(extSynapticWeights[i][2], SynapseForm.EXCITATORY, this.apicalTuft, i+1, alpha) ;
			
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
		int mySpikesNo = 0 ;
		for (int i = 0; i<drivingSpikeTimes.length; i++){
			if (drivingSpikeTimes[i][0] == this.neuronID) mySpikesNo = mySpikesNo + 1 ;
		}
		double [][] myDrivingSpikes  = new double[mySpikesNo][3] ;
		for (int i = 0; i<drivingSpikeTimes.length; i++){
			if (drivingSpikeTimes[i][0] == this.neuronID) 
				myDrivingSpikes[i] = drivingSpikeTimes[i] ;
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
		int mySpikesNo = 0 ;
		for (int i = 0; i<contextSpikeTimes.length; i++){
			if (contextSpikeTimes[i][0] == this.neuronID) mySpikesNo = mySpikesNo + 1 ;
		}
		double [][] myContextSpikes  = new double[mySpikesNo][3] ;
		for (int i = 0; i<contextSpikeTimes.length; i++){
			if (contextSpikeTimes[i][0] == this.neuronID) 
				myContextSpikes[i] = contextSpikeTimes[i] ;
		}
		// send these to the apical tuft
		apicalTuft.setContextSpikes(myContextSpikes);
	}

	public void run(double currentTime){
		// run neuron for a single time step
		basalDendrite.run(currentTime); // update state of basal dendrite
		if (debug){
			if (basalDendrite.activation > 0)
				System.out.println("time = " + currentTime + " Basal D activation = " + basalDendrite.activation);
		}
		apicalTuft.run(currentTime); // update state of  apical dendrite
		if (debug){
			if (apicalTuft.activation > 0)
				System.out.println("time = " + currentTime + " Apical Tuft activation = " + apicalTuft.activation);
		}
		apicalDendrite.run(currentTime); // use the above two to nonlinearly mix
		if (debug){
			//  (apicalDendrite.activation > 0)
				System.out.println("time = " + currentTime + " apical Dendrite activation = " + apicalDendrite.activation);
		}
		// what's below won't work: needs the code of the runs above to be instantiated (done)
		if (axonHillock.runAndSpike(currentTime))	// attempt to generate output spikes
			spikesOut.add(currentTime) ;
		if (debug){
			if (axonHillock.activation > 0)
				System.out.println("time = " + currentTime + " axon Hillock activation = " + axonHillock.activation);
		}
	}
}
