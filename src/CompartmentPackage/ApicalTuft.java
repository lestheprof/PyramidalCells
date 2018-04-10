/**
 * 
 */
package CompartmentPackage;

import NeuronPackage.PyramidalNeuron;

/**
 * @author lss
 *
 */
public class ApicalTuft extends AbstractCompartment {

	public double tauApical ; // time constant
	/**
	 * neuron is the Pyramidal neuron object to which this apical tuft belongs
	 * tauApical is time constant for this compartment
	 */
	public ApicalTuft(PyramidalNeuron neuron, int id, double tauApical) {
		super(neuron, id) ; // so compartment knows its neuron id and its own id
		this.tauApical = tauApical ;
	}
	

}
