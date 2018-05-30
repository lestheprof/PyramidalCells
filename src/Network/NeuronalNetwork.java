/**
 * 
 */
package Network;

import java.util.Iterator;

import NeuronPackage.*;

/**
 * @author lss
 * Class to hold the neural network itself. Uses and array of AbstractNeurons. 
 * Initialised in constructor only to supply sampling rate: otherwise initialised in setup
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
		neurons = new AbstractNeuron[2] ;
		for (int nno = 0 ; nno< neurons.length; nno++){
			if (networkInfo[nno] instanceof PyramidalNeuronInfo)
				neurons[nno] = new PyramidalNeuron((PyramidalNeuronInfo) networkInfo[nno]) ;
			else if (networkInfo[nno] instanceof InterNeuronInfo)
				neurons[nno] = new InterNeuron((InterNeuronInfo)networkInfo[nno]) ;
			else System.err.println("NeuronalNetwork.setup: " + "invalie networkInfo type");
		}
	}
	
	/*
	 * set up external connections to driving synapses
	 *
	 */
	public void setUpExternalDrivingSynapses(double [][] extSynapticWeights, double alpha){
		for (int neuronNumber = 0; neuronNumber < neurons.length ; neuronNumber++){
			if (neurons[neuronNumber] instanceof PyramidalNeuron)
			{
				PyramidalNeuron pneuron =  (PyramidalNeuron) neurons[neuronNumber] ;
				pneuron.setUpExternalDrivingSynapses(extSynapticWeights, alpha) ;
			}
		}
	}
	
	public void setUpExternalContextSynapses(double [][] extSynapticWeights, double alpha){
		for (int neuronNumber = 0; neuronNumber < neurons.length ; neuronNumber++){
			if (neurons[neuronNumber] instanceof PyramidalNeuron)
			{
				PyramidalNeuron pneuron =  (PyramidalNeuron) neurons[neuronNumber] ;
				pneuron.setUpExternalContextSynapses(extSynapticWeights, alpha) ;
			}
		}
	}
	
	/* 
	 * set up the inputs
	 */
	public void setDrivingInputs(double [][] drivingSpikeTimes){
		for (int neuronNumber = 0; neuronNumber < neurons.length ; neuronNumber++){
			if (neurons[neuronNumber] instanceof PyramidalNeuron)
			{
				PyramidalNeuron pneuron =  (PyramidalNeuron) neurons[neuronNumber] ;
				pneuron.setDrivingInputs(drivingSpikeTimes) ;
			}
		}	
		}
	
	public void setContextualInputs(double [][] contextualSpikeTimes){
		for (int neuronNumber = 0; neuronNumber < neurons.length ; neuronNumber++){
			if (neurons[neuronNumber] instanceof PyramidalNeuron)
			{
				PyramidalNeuron pneuron =  (PyramidalNeuron) neurons[neuronNumber] ;
				pneuron.setContextualInputs(contextualSpikeTimes) ;
			}
		}	
		}

	public void run(double currentTime){
		for (int neuronNumber = 0; neuronNumber < neurons.length ; neuronNumber++){
			// need to check neuron type
			if (neurons[neuronNumber] instanceof PyramidalNeuron)
			{
				PyramidalNeuron pneuron =  (PyramidalNeuron) neurons[neuronNumber] ;
				pneuron.run(currentTime) ;
			}
			else if (neurons[neuronNumber] instanceof InterNeuron)
			{
				InterNeuron ineuron = (InterNeuron)  neurons[neuronNumber] ;
				ineuron.run(currentTime);
			}
		}
	}
	
	public void displaySpikes(){
		for (int neuronNumber = 0; neuronNumber < neurons.length ; neuronNumber++){
			Iterator <Double> spikeIterator = neurons[neuronNumber].spikesOut.iterator(); // spikesOut is in AbstractNeuron 
			while (spikeIterator.hasNext())
				System.out.println("Neuron " + neurons[neuronNumber].neuronID + " fired at time "+ spikeIterator.next()) ;
		}
	}

}
