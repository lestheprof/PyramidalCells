/**
 * 
 */
package NeuronPackage;

import java.util.ArrayList;

import CompartmentPackage.SimpleLeaky;

/**
 * @author lss
 * Inhibitory interneuron. Single compartment.
 */
public class InterNeuron extends AbstractNeuron {
	// this neuron has a single compartment
	public SimpleLeaky simpleLeaky ;
	
	/**
	 * 
	 * @param ID id of this inhibitory interneuron
	 * @param samplingRate sampling rate
	 * @param tauInhibitory tau value for the compartment that will implement this inhibitory interneuron
	 * @param threshold threshold for neuron
	 * @param refractoryPeriod refractory period in seconds
	 */
	public InterNeuron(int ID, int samplingRate, double tauInhibitory,  double threshold, double refractoryPeriod, boolean debug) {
		super(ID, samplingRate, debug);
		// initialise output spikes
		this.spikesOut = new ArrayList<> () ;
		// create the single compartment for this neuron
		this.simpleLeaky = new SimpleLeaky(this, 1, tauInhibitory, refractoryPeriod, debug) ;	
		this.spikingCompartment = this.simpleLeaky ;
		this.spikesOut = new ArrayList<> () ;
	}
	
	/*
	 * Create an inhibitory interneuron from an InterNeuronInfo descriptor
	 */
	public InterNeuron(InterNeuronInfo i1, boolean debug) {
		super(i1.identity, i1.samplingRate, debug);
// set up the compartment of this neuron
		this.simpleLeaky = new SimpleLeaky(this, 1, i1.tauInhib, i1.refractoryPeriod, debug) ;
		this.spikingCompartment = this.simpleLeaky ;
		this.spikesOut = new ArrayList<> () ;
	}
	
	public void run(double currentTime){
		if (simpleLeaky.runAndSpike(currentTime)) // attempt to generate output spikes
		{
			// spike has been generated
			this.justSpiked = true ;
			spikesOut.add(currentTime) ; // add to list of spikes
		}
		else this.justSpiked = false ;
	}
	
	

}
