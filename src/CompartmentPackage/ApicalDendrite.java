/**
 * 
 */
package CompartmentPackage;

import NeuronPackage.PyramidalNeuron;

/**
 * @author lss
 *
 * Implements an apical dendrite which takes the activation of the apical tuft as input (from this.myNeuron) 
 * and runs it through multiplier * logistic(gradient * input) to produce a value (activation) between 0 and multiplier which
 * will be used to modulate the effect of the basal dendrite's activation at the axon hillock.
 */
public class ApicalDendrite extends AbstractCompartment {
public double multiplier = 1 ;  
public double gradient = 1 ;

// output is multiplier * logistic(gradient * input)
	/**
	 * neuron is the Pyramidal neuron object to which this apical dendrite belongs
	 */
	public ApicalDendrite(PyramidalNeuron neuron, double multiplier, double gradient,int id) {
		super(neuron, id) ; // so compartment knows its neuron id and its own id
		this.multiplier = multiplier ;
		this.gradient = gradient ;
		compartmentType = "Apical Dendrite Compartment" ;
		// TODO Auto-generated constructor stub
	}
	
	public void run(double currentTime){
		PyramidalNeuron neuron = (PyramidalNeuron) this.myNeuron ;
		// apply a logistic here to get a numeric output to be used to modify basal input to axon hillock.
		this.activation = multiplier/(1 + Math.exp( - gradient *  neuron.apicalTuft.activation)) ;
	}
	
	

}
