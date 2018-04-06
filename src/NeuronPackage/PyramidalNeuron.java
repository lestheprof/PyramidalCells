package NeuronPackage;

import CompartmentPackage.*;
import SynapsePackage.ExternalSynapse;
import SynapsePackage.SynapseForm;
import java.util.List;


public class PyramidalNeuron extends AbstractNeuron {
// this neuron consists of 4 compartments below
	public ApicalTuft apicalTuft ;
	public ApicalDendrite apicalDendrite ;
	public AxonHillock axonHillock ;
	public BasalDendrite basalDendrite ;
	private ExternalSynapse[] extDrivingSynapses ;
	private ExternalSynapse[] extContextSynapses ;
	private int samplingRate ;
	public List<Double> spikesOut = null ;
	
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
		// set these up in this neuron's BasalDendrite
		for (int i=0; i<nExtDrivingSynapses; i++) {
			// create the synapse
			extDrivingSynapses[i] = new ExternalSynapse(0, SynapseForm.EXCITATORY, this.basalDendrite) ;
			// initialise the synapse
		}
	}
	
	/*
	 * nExtContextSynapses synapses is the number of context synapses (external for now)
	 */
	public void setUpExternalContextSynapses(int nExtContextSynapses){
		// set up an array of external driving synapses
		extContextSynapses = new ExternalSynapse[nExtContextSynapses] ; // note these are not initialised
		// set these up in this neuron's ApicalDendrite
		for (int i=0; i<nExtContextSynapses; i++) {
			// create the synapse
			extContextSynapses[i] = new ExternalSynapse(0, SynapseForm.EXCITATORY, this.apicalDendrite) ;
			// initialise the synapse
		}
		
	}

	public void run(double currentTime){
		// run neuron for a single time step
		basalDendrite.run(currentTime); // update state of basal dendrite
		apicalTuft.run(currentTime); // update state of  apical dendrite
		apicalDendrite.run(currentTime); // use the above two to nonlinearly mix
		// what's below won't work: needs the code of the runs above to be instantited
		if (axonHillock.runAndSpike(currentTime))	// attempt to generate output spikes
			spikesOut.add(currentTime) ;
	}
}
