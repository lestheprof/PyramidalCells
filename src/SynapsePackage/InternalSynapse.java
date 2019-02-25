/**
 * 
 */
package SynapsePackage;

import java.util.List;
import java.util.ArrayList ;
import java.util.Iterator;

import CompartmentPackage.AbstractCompartment;
import CompartmentPackage.AbstractSpikingCompartment;
import NeuronPackage.AbstractNeuron;


/**
 * @author lss
 * Internal synapse (from an axonic output to a compartment). Excitatory.
 * Note that synapse numbers start at 1. Synapse 0 not used.
 */
public class InternalSynapse extends AbstractSynapse {

	public double delay ; // synaptic delay (i.e. axon + synapse) in seconds
	private int delaySamples ; // delay in samples
	public AbstractSpikingCompartment fromCompartment ;
	public AbstractNeuron fromNeuron ;
	public List <Integer> incomingSpikeSampleTimes = null ;
	private boolean debug = false ;

	/**
	 * @param weight weight of synapse
	 * @param delay delay of synapse (i seconds)
	 * @param stype synapse type
	 * @param fromCompartment presynaptic compartment: must be a spiking compartment
	 * @param toCompartment postsynaptic compartment
	 * @param ID ID of this synapse
	 * @param alpha alpha for this synapse
	 */
	public InternalSynapse(double weight,  double delay, SynapseForm stype, AbstractSpikingCompartment fromCompartment, 
			AbstractCompartment toCompartment, int ID, double alpha) {
		super(weight, stype, toCompartment, ID, alpha);
		// initialise internal synapse object variables
		this.fromCompartment = fromCompartment ;
		this.fromNeuron = fromCompartment.myNeuron ;
		this.delay = delay ;
		this.delaySamples =  (int) Math.ceil(delay * this.samplingrate) ; // delay in sample times
		this.incomingSpikeSampleTimes = new ArrayList <Integer> ();
		if (debug)
		System.out.println("Internal synapse created: from neuron=" + fromNeuron.neuronID + 
				" Compartment=" + fromCompartment.compartmentID + " to Neuron=" + this.neuronID + " Compartment=" + this.compartmentID);
	}
	
	/**
	 * @param currentTime current time in seconds
	 * @return returns a double, giving the post-synaptic potential
	 */
	public double runStep(double currentTime){
		int currentSample = ((int) Math.round(currentTime * samplingrate) ); // current time in samples
		// have we just had a presynaptic spike?
		if (fromNeuron.justSpiked){
			//add to list of spiking times
			Integer currentSampleTime = new Integer (currentSample) ;
			incomingSpikeSampleTimes.add(currentSampleTime) ;
		}
		// if there are spikes in the buffer calculate postsynaptic effect
		if (incomingSpikeSampleTimes.size() == 0)
			return 0 ;
		else{
			// list is nonempty
			double psp = 0 ;
			// traverse list adding in the contribution from each spike in it
			Iterator <Integer> iter = incomingSpikeSampleTimes.iterator() ;
			while (iter.hasNext()){
				Integer currentSpike = iter.next() ;
				if ((currentSample - currentSpike) < delaySamples){
					// do nothing: inside delay
				}
				else if ((currentSample - (currentSpike + delaySamples)) < alphaArrayLength){
					//inside alpha function
					alphaIndex = (currentSample - (currentSpike + delaySamples)) ;
					psp = psp + alphaArray[alphaIndex] * this.weight ;
				}
				else if ((currentSample - (currentSpike + delaySamples + alphaArrayLength) > 0))
					// remove expired spikes
					iter.remove();
			}
			// remove elements from list that have expired
			return psp ;
		}
	}

}
