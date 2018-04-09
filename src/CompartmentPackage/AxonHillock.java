/**
 * 
 */
package CompartmentPackage;

import NeuronPackage.PyramidalNeuron;

/**
 * @author lss
 *
 */
public class AxonHillock extends AbstractCompartment {

	/**
	 * neuron is the Pyramidal neuron object to which this axon hillock belongs
	 */
	public AxonHillock(PyramidalNeuron neuron, int id) {
		super(neuron, id) ; // so compartment knows its neuron id and its own id
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param currentTime simulation time
	 * @return currently always false
	 */
	public Boolean runAndSpike(double currentTime){
		this.run(currentTime);
		return false ;
	}

}
