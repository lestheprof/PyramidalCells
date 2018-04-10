package CompartmentPackage;

import NeuronPackage.AbstractNeuron;

public abstract class AbstractCompartment {

	public AbstractNeuron myNeuron ;
	public double activation ;
	public int compartmentID ;
	public double samplingInterval ;
	public int samplingrate ;
	
	public AbstractCompartment(AbstractNeuron neuron, int compartmentID) {
		// TODO Auto-generated constructor stub
		this.activation = 0 ; // initialise the activation, here in this abstract version, to 0
		this.myNeuron = neuron ;
		this.compartmentID = compartmentID ;
		this.samplingInterval = neuron.samplingInterval ;
		this.samplingrate = neuron.samplingRate ;
	}
	
	public int getNeuronID(){
		return myNeuron.neuronID ;
	}
	
	/*
	 * CurrentTime current simulation time
	 */
	public void run(double currentTime){
	}

}
