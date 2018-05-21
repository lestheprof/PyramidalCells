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

private double threshold ;
private double resetValue = 0 ;
	/**
	 * neuron is the Pyramidal neuron object to which this axon hillock belongs
	 */
	public AxonHillock(PyramidalNeuron neuron, int id, double threshold) {
		super(neuron, id) ; // so compartment knows its neuron id and its own id
		compartmentType = "Axon Hillock Compartment" ;
		this.threshold  = threshold ;
	}
	
	/**
	 * @param currentTime simulation time
	 * @return currently always false
	 */
	public Boolean runAndSpike(double currentTime){
		PyramidalNeuron neuron = (PyramidalNeuron) this.myNeuron ;
		// calculate activation by multiplying basal dendrite activation by apical dendrite activation
		this.activation = neuron.apicalDendrite.activation * neuron.basalDendrite.activation;
		if (this.activation > this.threshold)
		{
			this.activation = resetValue ;
			return true ;
		}
		else return false ;
	}

}
