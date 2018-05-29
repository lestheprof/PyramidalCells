/**
 * 
 */
package Network;

import java.util.Iterator;

import NeuronPackage.*;

/**
 * @author lss
 *
 */
public class NeuronalNetwork {

	public int samplingRate ; 
	public AbstractNeuron[] neurons ;
	/**
	 * 
	 */
	public NeuronalNetwork(int samplingRate) {
		this.samplingRate = samplingRate ;
	}
	
	public void setup(NeuronInfo[] networkInfo){
		// setup the network: that is, create the neurons
		// interconnection is defined by the internal synapses
		// defined by a string eventually: for now just a single pyramidal neuron
		// set up the neurons, with id = 1
		neurons = new AbstractNeuron[1] ;
		neurons[0] = new PyramidalNeuron((PyramidalNeuronInfo) networkInfo[0]) ;
	}
	
	/*
	 * set up external connections to driving synapses
	 *
	 */
	public void setUpExternalDrivingSynapses(double [][] extSynapticWeights, double alpha){
		for (int neuronNumber = 0; neuronNumber < neurons.length ; neuronNumber++){
			PyramidalNeuron pneuron =  (PyramidalNeuron) neurons[neuronNumber] ;
			pneuron.setUpExternalDrivingSynapses(extSynapticWeights, alpha) ;
		}
	}
	
	public void setUpExternalContextSynapses(double [][] extSynapticWeights, double alpha){
		for (int neuronNumber = 0; neuronNumber < neurons.length ; neuronNumber++){
			PyramidalNeuron pneuron =  (PyramidalNeuron) neurons[neuronNumber] ;
			pneuron.setUpExternalContextSynapses(extSynapticWeights, alpha) ;
		}
	}
	
	/* 
	 * set up the inputs
	 */
	public void setDrivingInputs(double [][] drivingSpikeTimes){
		for (int neuronNumber = 0; neuronNumber < neurons.length ; neuronNumber++){
			PyramidalNeuron pneuron =  (PyramidalNeuron) neurons[neuronNumber] ;
			pneuron.setDrivingInputs(drivingSpikeTimes) ;
		}	
		}
	
	public void setContextualInputs(double [][] contextualSpikeTimes){
		for (int neuronNumber = 0; neuronNumber < neurons.length ; neuronNumber++){
			PyramidalNeuron pneuron =  (PyramidalNeuron) neurons[neuronNumber] ;
			pneuron.setContextualInputs(contextualSpikeTimes) ;
		}	
		}

	public void run(double currentTime){
		for (int neuronNumber = 0; neuronNumber < neurons.length ; neuronNumber++){
			// need to check neuron type
			PyramidalNeuron pneuron =  (PyramidalNeuron) neurons[neuronNumber] ;
			pneuron.run(currentTime) ;
		}
	}
	
	public void displaySpikes(){
		for (int neuronNumber = 0; neuronNumber < neurons.length ; neuronNumber++){
			PyramidalNeuron pneuron =  (PyramidalNeuron) neurons[neuronNumber] ;
			Iterator <Double> spikeIterator = pneuron.spikesOut.iterator();
			while (spikeIterator.hasNext())
				System.out.println("Neuron " + neurons[neuronNumber].neuronID + " fired at time "+ spikeIterator.next()) ;
		}
	}

}
