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
	}
	
	public void setExternalSynapses(ExternalSynapse[] extSynapses){
		this.extSynapses = extSynapses ;
	}
	
	public void setInternalSynapses(InternalSynapse[] intSynapses){		
	}
	
	public void setContextSpikes(double[][] contextSpikes){
		
	}
	
	public void run(double currentTime){
		
	}

}
