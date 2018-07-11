/**
 * 
 */
package CompartmentPackage;

import NeuronPackage.PyramidalNeuron;

/**
 * @author lss
 *
 */
public class AxonHillock extends AbstractSpikingCompartment {

private double threshold ;
private double resetValue = 0 ;
// private double lastSpikeTime = -1 ; // -ve to show no previous spikes: now in AbstractSpikingCompartment
public double refractoryPeriod = 0.02 ;
/**
 * 
 * @param neuron neuron is the Pyramidal neuron object to which this axon hillock belongs
 * @param id id of this compartment
 * @param threshold threshold for firing
 * @param refractoryPeriod in seconds
 */
	public AxonHillock(PyramidalNeuron neuron, int id, double threshold, double refractoryPeriod) {
		super(neuron, id) ; // so compartment knows its neuron id and its own id
		compartmentType = "Axon Hillock Compartment" ;
		this.threshold  = threshold ;
		this.refractoryPeriod = refractoryPeriod ;
	}
	
	/**
	 * @param currentTime simulation time
	 * @return true if a spike occurs
	 */
	public Boolean runAndSpike(double currentTime){
		PyramidalNeuron neuron = (PyramidalNeuron) this.myNeuron ;
		// calculate activation by multiplying basal dendrite activation by apical dendrite activation
		this.activation = neuron.apicalDendrite.activation * neuron.basalDendrite.activation;
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
