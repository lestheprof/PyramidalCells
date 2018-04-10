/**
 * 
 */
package CompartmentPackage;

import NeuronPackage.PyramidalNeuron;

/**
 * @author lss
 *
 */
public class BasalDendrite extends AbstractCompartment {

	public double tauBasal ; // time constant for this compartment
	/**
	 * neuron is the Pyramidal neuron object to which this basal dendrite belongs
	 */
	public BasalDendrite(PyramidalNeuron neuron, int id, double tauBasal) {
		super(neuron, id) ; // so compartment knows its neuron id and its own id
		this.tauBasal = tauBasal ;
		// TODO Auto-generated constructor stub
	}

	public void run(double currentTime){
		
	}

}
