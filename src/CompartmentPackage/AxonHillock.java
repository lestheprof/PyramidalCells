/**
 * 
 */
package CompartmentPackage;

import NeuronPackage.PyramidalNeuron;

/**
 * spiking compartment intended for pyramidal neurons
 * does not currently use any synapses, but these can be created (using the code in AbstractCompartment)
 * and implemented in runAndSpike
 * @author lss
 */
public class AxonHillock extends AbstractSpikingCompartment {

private double threshold ;
private double resetValue = 0 ;
// private double lastSpikeTime = -1 ; // -ve to show no previous spikes: now in AbstractSpikingCompartment
public double refractoryPeriod = 0.02 ;
public int transferfunction = 1;
private double K1 = 0.5 ; // for use with transfer function  == 2, from Kay & Phillips 2011
private double K2 = 1 ; // for use with transfer function  == 2, from Kay & Phillips 2011

private boolean errorReported = false ;
/**
 * 
 * @param neuron neuron is the Pyramidal neuron object to which this axon hillock belongs
 * @param id id of this compartment
 * @param threshold threshold for firing
 * @param refractoryPeriod in seconds
 */
	public AxonHillock(PyramidalNeuron neuron, int id, double threshold, double refractoryPeriod, int transferfunction, 
			double K1, double K2, boolean debug) {
		super(neuron, id, debug) ; // so compartment knows its neuron id and its own id
		compartmentType = "Axon Hillock Compartment" ;
		this.threshold  = threshold ;
		this.refractoryPeriod = refractoryPeriod ;
		this.transferfunction = transferfunction ;
		this.K1 = K1 ;
		this.K2 = K2 ;
	}
	
	/**
	 * @param currentTime simulation time
	 * @return true if a spike occurs
	 */
	public Boolean runAndSpike(double currentTime){
		PyramidalNeuron neuron = (PyramidalNeuron) this.myNeuron ;
		if (transferfunction == 1)
			// apply A(r, c) = r[k1 + (1 − k1) exp(k2rc)] from Kay & Phillips 2011
						this.activation = neuron.basalDendrite.activation * 
							(K1 + (1-K1) * Math.exp(K2 * neuron.basalDendrite.activation * neuron.apicalDendrite.activation)) ;

		else if (transferfunction == 2)
		{
			// apply A(r, c) = r[k1 + (1 − k1) exp(k2rc)] from Kay & Phillips 2011
			this.activation = neuron.basalDendrite.activation * 
				(K1 + (1-K1) * Math.exp(K2 * neuron.basalDendrite.activation * neuron.apicalDendrite.activation)) ;
			if (!errorReported)
			{
				System.out.println("ApicalDendrite: run: transferfunction = " + transferfunction + "value ignored.");
				errorReported = true ;
			}
		}	
		if (this.activation > this.threshold)
		{
			this.activation = resetValue ; // not really useful, as the activation does not have any historic information: it's reset each time. 
			if (lastSpikeTime >= 0) // there have been previous spikes
			{
				if ((currentTime - lastSpikeTime) > refractoryPeriod) // not in refractory period
				{
					lastSpikeTime = currentTime ; // update previous spike to now
					return true ;
				}
				else return false ;
			}
			else // this is first spike
			{
				lastSpikeTime = currentTime ;
				return true ;
			}
		}
		else return false ;
	}

}
