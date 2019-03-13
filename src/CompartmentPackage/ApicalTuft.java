/**
 * 
 */
package CompartmentPackage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import NeuronPackage.PyramidalNeuron;
import SynapsePackage.*;

/**
 * Apical tuft compartment, part of pyramidal neuron, receiving contextual inputs
 * @author lss
 *
 */
public class ApicalTuft extends AbstractCompartment {

	public double tauApicalTuft ; // time constant
	public double logisticGradient ;
	public double logisticIntercept ;
	private ExternalSynapse[] extSynapses  = null;
	// private InternalSynapse[] intSynapses  = null; now in AbstractCompartment
	
	public double internalActivation ;
	public double externalActivation ;
	public double prelogisticActivation; // internal compartmental variable counting internal activation 

	public double activityChange ;

	
	/**
	 * @param neuron is the Pyramidal neuron object to which this apical tuft belongs
	 * @param id identity of this compartment
	 * @param tauApicalTuft is time constant for this compartment
	 * @param logisticGradient is steepness of logistic applies
	 * @param logisticIntercept is offset of logistic function to be applied
	 */
	public ApicalTuft(PyramidalNeuron neuron, int id, double tauApicalTuft, double logisticGradient, double logisticIntercept,  boolean debug) {
		super(neuron, id, debug) ; // so compartment knows its neuron id and its own id
		this.tauApicalTuft = tauApicalTuft ;
		compartmentType = "Apical Tuft Compartment" ;
		this.prelogisticActivation = 0 ;
		// calculate the leakiness per sample
		if (tauApicalTuft == 0)
			this.activityChange = 0 ;
		else
			this.activityChange = Math.exp(- neuron.samplingInterval / tauApicalTuft) ; // pre-calculate amount by which activation decreases each time interval
		// values for logistic function
		this.logisticGradient = logisticGradient ;
		this.logisticIntercept = logisticIntercept ;
	}
	
	public void setExternalSynapses(ExternalSynapse[] extSynapses){
		this.extSynapses = extSynapses ;
	}
	
	public void setContextSpikes(double[][] contextSpikes){
		// these need to be associated with the correct external synapse
		// each driving spike is neuron, synapse, time
		// calls the setExternalInputs method in the external synapse with a list of times of spikes
		// form a list of spike times for each synapse
		for (int synapseNo = 1; synapseNo < extSynapses.length; synapseNo++){ // synapse numbers start at 1
			List <Double> synList = new ArrayList<>() ; // to hold the spike times for this synapse
			for (int spikeNo=0 ; spikeNo < contextSpikes.length; spikeNo++){
				if ((int) Math.round(contextSpikes[spikeNo][1]) == extSynapses[synapseNo].synapseID) // add to list
					synList.add(contextSpikes[spikeNo][2]) ;
			}
			// turn the list into an array and send it to the synapse (yuk)
			extSynapses[synapseNo].setExternalInputs((Double [])  synList.toArray(new Double[0])); 
		}
	}
	
	public void run(double currentTime){
		// gather up the contribution from the synapses to this compartment
				double internalActivation = 0;
				double externalActivation = 0 ;
				
				// internal incoming synapses
				Iterator<InternalSynapse> synIterator;
				// internal synapses
				if (!(incomingSynapses == null)) {// if there's internal synapses
					synIterator = incomingSynapses.iterator();
					while (synIterator.hasNext())
						internalActivation = internalActivation + synIterator.next().runStep(currentTime);
					}
				if (!(extSynapses == null)) // if there's no external synapses
				for (int synapseNo = 1 ; synapseNo < extSynapses.length; synapseNo ++)
				{
					externalActivation = externalActivation + extSynapses[synapseNo].runStep(currentTime) ;
				}
				this.internalActivation = internalActivation ;
				this.externalActivation = externalActivation ;
				this.prelogisticActivation = (this.prelogisticActivation * this.activityChange) + this.internalActivation + this.externalActivation ;
				// now apply logistic to calculate output
				this.activation = 1.0 / (1 + Math.exp(-(this.logisticGradient * (this.prelogisticActivation - this.logisticIntercept)))) ;
	}

}
