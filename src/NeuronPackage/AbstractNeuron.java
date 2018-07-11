package NeuronPackage;

import java.util.List;

import CompartmentPackage.AbstractSpikingCompartment;

public abstract class AbstractNeuron {
public int neuronID ;
public int samplingRate ;
public double samplingInterval ;
public List<Double> spikesOut = null ;
public boolean justSpiked ; // true only if neuron has spiked this sample
public AbstractSpikingCompartment spikingCompartment ;

/*
 * @param ID the (int) identity of this neuron
 * @param samplingRate sampling rate of the simulation 
 */
	public AbstractNeuron(int ID, int samplingRate) {
		this.neuronID = ID ;
		// set up sampling rate and interval
		this.samplingRate = samplingRate ;
		this.samplingInterval = 1.0/samplingRate ;
		this.justSpiked = false ;
	}
	
	// get the id of the compartment that spikes
		public AbstractSpikingCompartment getSpikingCompartment(){
			return this.spikingCompartment ;
		}

}
