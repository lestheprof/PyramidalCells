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

	/**
	 * neuron is the Pyramidal neuron object to which this apical tuft belongs
	 */
	public ApicalTuft(PyramidalNeuron neuron, int id) {
		super(neuron, id) ; // so compartment knows its neuron id and its own id
	}
	

}
