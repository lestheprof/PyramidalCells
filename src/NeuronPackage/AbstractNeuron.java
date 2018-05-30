package NeuronPackage;

import java.util.List;

public abstract class AbstractNeuron {
public int neuronID ;
public int samplingRate ;
public double samplingInterval ;
public List<Double> spikesOut = null ;
/*
 * @param ID the (String) identity of this neuron
 */
	public AbstractNeuron(int ID, int samplingRate) {
		this.neuronID = ID ;
		// set up sampling rate and interval
		this.samplingRate = samplingRate ;
		this.samplingInterval = 1.0/samplingRate ;
	}

}
