/**
 * 
 */
package CompartmentPackage;

import java.util.ArrayList;
import java.util.List;

import NeuronPackage.PyramidalNeuron;
import SynapsePackage.*;

/**
 * @author lss
 *
 */
public class ApicalTuft extends AbstractCompartment {

	public double tauApical ; // time constant
	private ExternalSynapse[] extSynapses  = null;
	private InternalSynapse[] intSynapses  = null;

	
	/**
	 * neuron is the Pyramidal neuron object to which this apical tuft belongs
	 * tauApical is time constant for this compartment
	 */
	public ApicalTuft(PyramidalNeuron neuron, int id, double tauApical) {
		super(neuron, id) ; // so compartment knows its neuron id and its own id
		this.tauApical = tauApical ;
		compartmentType = "Apical Tuft Compartment" ;
	}
	
	public void setExternalSynapses(ExternalSynapse[] extSynapses){
		this.extSynapses = extSynapses ;
	}
	
	public void setInternalSynapses(InternalSynapse[] intSynapses){		
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
		
	}

}
