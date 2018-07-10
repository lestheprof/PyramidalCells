package CompartmentPackage;

import NeuronPackage.InterNeuron;
import SynapsePackage.InternalSynapse; ;
/*
 * Simple leaky compartment, with leak tauInhib, threshold of 1.
 * 
 */
public class SimpleLeaky extends AbstractSpikingCompartment {
	public double tauInhib ;
	private InternalSynapse[] intSynapses  = null;

	double activityChange ;
	double refractoryPeriod ;
	double threshold = 1 ;
	// private double lastSpikeTime = -1 ; // -ve to show no previous spikes: now in AbstractSpikingCompartment
	private double resetValue = 0 ; // reset value for activation after spiking



	public SimpleLeaky(InterNeuron neuron, int compartmentID, double tauInhib, double refractoryPeriod) {
		super(neuron, compartmentID);
		this.tauInhib = tauInhib ;
		this.refractoryPeriod = refractoryPeriod ;
		// pre-calculate activity change
		if (tauInhib == 0)
			this.activityChange = 0; 
		else
		 this.activityChange = Math.exp(- neuron.samplingInterval / tauInhib) ; // pre-calculate amount by which activation decreases each time interval	
	}
	
	
	public boolean runAndSpike(double currentTime){
		super.run(currentTime) ;
		double internalActivation = 0 ;
		if (!(intSynapses == null)) // if there's no internal synapses
		for (int synapseNo = 1 ; synapseNo < intSynapses.length; synapseNo ++)
		{
			internalActivation = internalActivation + intSynapses[synapseNo].runStep(currentTime) ;
		}
		// calculate activation with leak
		this.activation = (this.activation * this.activityChange) + internalActivation  ;
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
