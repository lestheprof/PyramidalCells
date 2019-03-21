/**
 * 
 */
package CompartmentPackage;

import NeuronPackage.PyramidalNeuron;

/**
 *
 * Implements an apical dendrite which takes the activation of the apical tuft as input (from this.myNeuron) 
 * and runs it through multiplier * logistic(gradient * input) to produce a value (activation) between 0 and multiplier which
 * will be used to modulate the effect of the basal dendrite's activation at the axon hillock.
 * Does not currently use synapses, though synapses can target this compartment because of the code in
 * AbstractCompartment: add code to run method to use them.
 * @author lss
 */
public class ApicalDendrite extends AbstractCompartment {
public double multiplier = 2 ;  // expected default value. when K2 in axon hillock = 1
public double gradient = 1 ;
PyramidalNeuron neuron = null ;
public int transferfunction = 1 ; // use to choose transfer function
private boolean errorReported = false ;


// output is multiplier * logistic(gradient * input)
	/**
	 * apical dendrite will enable interaction between apical tuft and basal dendrite
	 * @param neuron id of this neuron
	 * @param multiplier parameter to transfer function
	 * @param gradient parameter to transfer function // not currently used
	 * @param id id of this compartment
	 */
	public ApicalDendrite(PyramidalNeuron neuron, double multiplier, double gradient,int id, int transferfunction, boolean debug) {
		super(neuron, id, debug) ; // so compartment knows its neuron id and its own id
		this.multiplier = multiplier ;
		this.gradient = gradient ;
		compartmentType = "Apical Dendrite Compartment" ;
		this.neuron = (PyramidalNeuron) this.myNeuron ; // cast required to use .apicalTuft
		this.transferfunction = transferfunction ;
		// TODO Auto-generated constructor stub
	}
	

	public void run(double currentTime){
		if (transferfunction == 1)
			this.activation = multiplier * neuron.apicalTuft.activation ;
		else if (transferfunction == 2)
		{
			// simply scale the apical tuft activation
			this.activation = multiplier * neuron.apicalTuft.activation ;
		}
		else if (!errorReported)
		{
			System.out.println("ApicalDendrite: run: transferfunction = " + transferfunction + "value ignored.");
			errorReported = true ;
		}
		// end if		
	}
	
	

}
