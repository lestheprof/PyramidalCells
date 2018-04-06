package CompartmentPackage;

import NeuronPackage.AbstractNeuron;

public abstract class AbstractCompartment {

	public AbstractNeuron myNeuron ;
	public double activation ;
	
	public AbstractCompartment(AbstractNeuron neuron) {
		// TODO Auto-generated constructor stub
		this.activation = 0 ; // initialise the activation, here in this abstract version, to 0
		this.myNeuron = neuron ;
	}
	
	public String getNeuronID(){
		return myNeuron.neuronID ;
	}

}
