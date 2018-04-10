package NeuronPackage;

public abstract class AbstractNeuron {
public int neuronID ;
public int samplingRate ;
public double samplingInterval ;
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
