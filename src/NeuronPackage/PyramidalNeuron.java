package NeuronPackage;

import CompartmentPackage.*;
import SynapsePackage.ExternalSynapse;


public class PyramidalNeuron extends AbstractNeuron {
// this neuron consists of 4 compartments below
	public ApicalTuft apicalTuft ;
	public ApicalDendrite apicalDendrite ;
	public AxonHillock axonHillock ;
	public BasalDendrite basalDendrite ;
	private ExternalSynapse[] extDrivingSynapses ;
	private ExternalSynapse[] extContextSynapses ;
	private int samplingRate ;
	
	/*
	 * ID String identity of the neuron
	 * samplingRate is sampling rathe used to convert times to sample numbers
	 */
	public PyramidalNeuron(String ID, int samplingRate) {
		super(ID);
// set up the compartments of this neuron
		// Pyramidal neuron has 4 compartments
		apicalTuft = new ApicalTuft(this) ;
		apicalDendrite = new ApicalDendrite(this) ;
		axonHillock = new AxonHillock(this) ;
		basalDendrite = new BasalDendrite(this) ;
		// set up sampling rate
		this.samplingRate = samplingRate ;
	}
	
	/*
	 * nExtDrivingSynapses synapses is the number of driving synapses (external for now)
	 */
	public void setUpExternalDrivingSynapses(int nExtDrivingSynapses){
		// set up an array of external driving synapses
		extDrivingSynapses = new ExternalSynapse[nExtDrivingSynapses] ; // note these are not initialised
	}
	
	/*
	 * nExtContextSynapses synapses is the number of context synapses (external for now)
	 */
	public void setUpExternalContextSynapses(int nExtContextSynapses){
		// set up an array of external driving synapses
		extContextSynapses = new ExternalSynapse[nExtContextSynapses] ; // note these are not initialised
	}

}
