/**
 * 
 */
package NeuronPackage;

import java.util.ArrayList;

import CompartmentPackage.AbstractSpikingCompartment;
import CompartmentPackage.ApicalDendrite;
import CompartmentPackage.ApicalTuft;
import CompartmentPackage.AxonHillock;
import CompartmentPackage.BasalDendrite;
import CompartmentPackage.SimpleLeaky;

/**
 * @author lss
 * Inhibitory interneuron. Single compartment.
 */
public class InterNeuron extends AbstractNeuron {
	// this neuron has a single compartment
	public SimpleLeaky simpleLeaky ;
	
	/**
	 * @param ID
	 * @param samplingRate
	 */
	public InterNeuron(int ID, int samplingRate, double tauInhibitory,  double threshold, double refractoryPeriod) {
		super(ID, samplingRate);
		// initialise output spikes
		this.spikesOut = new ArrayList<> () ;
		// create the single compartment for this neuron
		this.simpleLeaky = new SimpleLeaky(this, 1, tauInhibitory, refractoryPeriod) ;	
		this.spikingCompartment = this.simpleLeaky ;
		this.spikesOut = new ArrayList<> () ;
	}
	
	/*
	 * Create an inhibitory interneuron from an InterNeuronInfo descriptor
	 */
	public InterNeuron(InterNeuronInfo i1) {
		super(i1.identity, i1.samplingRate);
// set up the compartment of this neuron
		this.simpleLeaky = new SimpleLeaky(this, 1, i1.tauInhib, i1.refractoryPeriod) ;
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
