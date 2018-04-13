/**
 * 
 */
package CompartmentPackage;

import NeuronPackage.PyramidalNeuron;

/**
 * @author lss
 *
 */
public class ApicalDendrite extends AbstractCompartment {

	/**
	 * neuron is the Pyramidal neuron object to which this apical dendrite belongs
	 */
	public ApicalDendrite(PyramidalNeuron neuron, int id) {
		super(neuron, id) ; // so compartment knows its neuron id and its own id
		compartmentType = "Apical Dendrite Compartment" ;
		// TODO Auto-generated constructor stub
	}

}
