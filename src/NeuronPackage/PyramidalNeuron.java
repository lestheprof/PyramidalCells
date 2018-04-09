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
	 * samplingRate is sampling rather used to convert times to sample numbers
	 */
	public PyramidalNeuron(int ID, int samplingRate) {
		super(ID);
// set up the compartments of this neuron
		// Pyramidal neuron has 4 compartments: these are also numbered for identification purposes
		apicalTuft = new ApicalTuft(this, 2) ;
		apicalDendrite = new ApicalDendrite(this,3) ;
		axonHillock = new AxonHillock(this,4) ;
		basalDendrite = new BasalDendrite(this, 1) ;
		// set up sampling rate
		this.samplingRate = samplingRate ;
	}
	
	/*
	 * extSynapticWeights is the array of external synaptic weights read from the file)
	 */
	public void setUpExternalDrivingSynapses(double [][] extSynapticWeights){
		// find the highest synapse number
		int nExtDrivingSynapses = 0 ;
		for (int index = 0 ; index < extSynapticWeights.length ; index++){
			// use only synapses to this neuron
			if (extSynapticWeights[index][2] == this.neuronID){
			if (nExtDrivingSynapses < (int)(Math.round(extSynapticWeights[index][2])))
				nExtDrivingSynapses = (int)(Math.round(extSynapticWeights[index][2])) ;
			}	
		}
		// set up an array of external driving synapses
		extDrivingSynapses = new ExternalSynapse[nExtDrivingSynapses] ; // note these are not initialised
		// set these up in this neuron's BasalDendrite
		for (int i=0; i<extSynapticWeights.length; i++) {
			// create the synapse. Note that synapse id's start at 1
			int synapseNumber = (int)(Math.round(extSynapticWeights[i][2])) ;
			if (synapseNumber > 0)
			extDrivingSynapses[synapseNumber] = 
					new ExternalSynapse(extSynapticWeights[i][3], SynapseForm.EXCITATORY, this.basalDendrite, i+1) ;
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
			// create the synapse. Note that synapse ID's start at 1
			extContextSynapses[i] = new ExternalSynapse(0, SynapseForm.EXCITATORY, this.apicalDendrite, i+1) ;
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
