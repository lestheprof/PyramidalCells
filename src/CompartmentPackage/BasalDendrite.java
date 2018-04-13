/**
 * 
 */
package CompartmentPackage;

import NeuronPackage.PyramidalNeuron;
import SynapsePackage.*;

/**
 * @author lss
 *
 */
public class BasalDendrite extends AbstractCompartment {

	public double tauBasal ; // time constant for this compartment
	private ExternalSynapse[] extSynapses  = null;
	private InternalSynapse[] intSynapses  = null;
	/**
	 * neuron is the Pyramidal neuron object to which this basal dendrite belongs
	 */
	public BasalDendrite(PyramidalNeuron neuron, int id, double tauBasal) {
		super(neuron, id) ; // so compartment knows its neuron id and its own id
		this.tauBasal = tauBasal ;
		// TODO Auto-generated constructor stub
	}
	
	public void setExternalSynapses(ExternalSynapse[] extSynapses){
		this.extSynapses = extSynapses ;
	}
	
	public void setInternalSynapses(InternalSynapse[] intSynapses){		
	}

	public void setDrivingSpikes(double[][] drivingSpikes){
		// these need to be associated with the correct external synapse
		// each driving spike is neuron, synapse, time
		// calls the setExternalInputs method in the external synapse with a list of times of spikes
	}
	
	public void run(double currentTime){
		
	}

}
