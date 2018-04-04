package NeuronPackage;

import CompartmentPackage.*;


public class PyramidalNeuron extends AbstractNeuron {
// this neuron consists of 4 compartments below
	public ApicalTuft apicalTuft ;
	public ApicalDendrite apicalDendrite ;
	public AxonHillock axonHillock ;
	public BasalDendrite basalDendrite ;
	
	/*
	 * ID String identity of the neuron
	 */
	public PyramidalNeuron(String ID) {
		super(ID);
// set up the compartments of this neuron
		// Pyramidal neuron has 4 compartments
		apicalTuft = new ApicalTuft(this) ;
		apicalDendrite = new ApicalDendrite(this) ;
		axonHillock = new AxonHillock(this) ;
		basalDendrite = new BasalDendrite(this) ;
	}

}
