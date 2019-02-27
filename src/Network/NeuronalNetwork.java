/**
 * 
 */
package Network;

import java.util.Iterator;
import java.io.FileWriter;
import java.io.IOException;

import CompartmentPackage.AbstractCompartment;
import NeuronPackage.*;
import SynapsePackage.SynapseForm;

/**
 * Class to hold the neural network itself. Uses an array of AbstractNeurons. 
 * Initialised in constructor only to supply sampling rate: otherwise initialised in setup
 * @author lss
 *
 */
public class NeuronalNetwork {

	public boolean debug ;
	public int samplingRate ; 
	public AbstractNeuron[] neurons ;
	
/**
 * set sampling rate
 * @param samplingRate global sampling rate
 */
	public NeuronalNetwork(int samplingRate, boolean debug) {
		this.samplingRate = samplingRate ;
		this.debug = debug ;
	}
	/**
	 * 
	 * @param networkInfo array of information about each neuron in the network
	 */
	public void setup(NeuronInfo[] networkInfo){
		// setup the network: that is, create the neurons
		// interconnection is defined by the internal synapses
		// defined by a string eventually:
		// set up the neurons, with id = 1
		neurons = new AbstractNeuron[networkInfo.length] ;
		for (int nno = 0 ; nno< neurons.length; nno++){
			if (networkInfo[nno] instanceof PyramidalNeuronInfo)
				neurons[nno] = new PyramidalNeuron((PyramidalNeuronInfo) networkInfo[nno], debug) ;
			else if (networkInfo[nno] instanceof InterNeuronInfo)
				neurons[nno] = new InterNeuron((InterNeuronInfo) networkInfo[nno], debug) ;
			else System.err.println("NeuronalNetwork.setup: " + "invalid networkInfo type");
		}
	}
	
	
	/**
	 * set up external connections to driving synapses
	 * @param extSynapticWeights 2D array of externally connected weights
	 * @param alpha fixed value of alpha for all external weights
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
	
	/**
	 * 
	 * @param extSynapticWeights 2D array of external connected weights
	 * @param alpha fixed value of alpha for all of these
	 */
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
	 * set up the internal synapses
	 * internalSynapseWeightsDelays contains weights and delays: format is from_neuron, to_neuron, to_compartment, weight, delay
	 * alphaInternalExcitatory alpha factor for excitatory synapses
	 * alphaInternalInhibitory alpha factor for inhibitory synapses
	 */
	public void setUpInternalSynapses(double[][] internalSynapseWeightsDelays, double alphaInternalExcitatory,
			double alphaInternalInhibitory) {
		SynapseForm stype;
		int synapseID = 0;
		double alpha;
		AbstractCompartment toCompartment = null;
		// for each spiking compartment that's a presynaptic compartment, call
		// the method
		// addOutgoingSynapse
		// note that index of neuron in neurons array is 1 less than actual neuron identity
		for (int synapseNo = 0; synapseNo < internalSynapseWeightsDelays.length; synapseNo++) {
			int fromNeuronIndex = (int) (Math.round(internalSynapseWeightsDelays[synapseNo][0])) - 1;
			int toNeuronIndex = (int) (Math.round(internalSynapseWeightsDelays[synapseNo][1])) - 1;

			// alpha is different for excitatory and inhibitory synapses
			if (internalSynapseWeightsDelays[synapseNo][3] < 0) {
				stype = SynapseForm.INHIBITORY;
				alpha = alphaInternalInhibitory;
			} else {
				stype = SynapseForm.EXCITATORY;
				alpha = alphaInternalExcitatory;
			}
			synapseID = synapseID + 1; // start them at 1
			if (toNeuronIndex >= neurons.length)
				// invalid synapse endpoint
				System.out.println("NeuronalNetwork.setUpInternalSynapses: synapse endpoint = " + toNeuronIndex + " past last neuron = " +
				(neurons.length -1)) ;
				
			// find the toCompartment
			if (neurons[toNeuronIndex] instanceof InterNeuron)
				switch ((int) (Math.round(internalSynapseWeightsDelays[synapseNo][2]))) {
				case 1:
					toCompartment = ((InterNeuron) neurons[toNeuronIndex]).simpleLeaky;
					break ;
				default:
					System.out.println("NeuronalNetwork:setUpInternalSynapses: invalid synapse to_compartment (Inter Neuron) = " + 
							(int) (Math.round(internalSynapseWeightsDelays[synapseNo][2])));
				}
			else {
				if (neurons[toNeuronIndex] instanceof PyramidalNeuron) {
					switch ((int) (Math.round(internalSynapseWeightsDelays[synapseNo][2]))) {
					case 1:
						toCompartment = ((PyramidalNeuron) neurons[toNeuronIndex]).basalDendrite;
						break;
					case 2:
						toCompartment = ((PyramidalNeuron) neurons[toNeuronIndex]).apicalTuft;
						break;
					case 3:
						toCompartment = ((PyramidalNeuron) neurons[toNeuronIndex]).apicalDendrite;
						break;
					case 4:
						toCompartment = ((PyramidalNeuron) neurons[toNeuronIndex]).axonHillock;
						break;
					default:
						System.out.println("NeuronalNetwork:setUpInternalSynapses: invalid synapse to_compartment (Pyramidal Neuron) = " + 
								(int) (Math.round(internalSynapseWeightsDelays[synapseNo][2])));
					}
				}
			}

			neurons[fromNeuronIndex].getSpikingCompartment().addOutgoingSynapse(internalSynapseWeightsDelays[synapseNo][3],
					internalSynapseWeightsDelays[synapseNo][4], stype, neurons[fromNeuronIndex].getSpikingCompartment(),
					toCompartment, 
					synapseID, alpha);
		}

	}
	
/**
 * set up the drivinginputs
 * @param drivingSpikeTimes 2D array drivingSpikeTimes times from external to the system
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
	/**
	 * set up the contextual inputs
	 * @param contextualSpikeTimes 2D array of external contextual inputs
	 */
	public void setContextualInputs(double [][] contextualSpikeTimes){
		for (int neuronNumber = 0; neuronNumber < neurons.length ; neuronNumber++){
			if (neurons[neuronNumber] instanceof PyramidalNeuron)
			{
				PyramidalNeuron pneuron =  (PyramidalNeuron) neurons[neuronNumber] ;
				pneuron.setContextualInputs(contextualSpikeTimes) ;
			}
		}	
		}
/**
 * run the simulation for one time step
 * @param currentTime time of current timestep
 */
	public void run(double currentTime){
		for (int neuronNumber = 0; neuronNumber < neurons.length ; neuronNumber++){
			// need to check neuron type (do we really?)
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
	
	/**
	 * display all firing times at end of simulation
	 */
	public void displaySpikes(){
		for (int neuronNumber = 0; neuronNumber < neurons.length ; neuronNumber++){
			Iterator <Double> spikeIterator = neurons[neuronNumber].spikesOut.iterator(); // spikesOut is in AbstractNeuron 
			while (spikeIterator.hasNext())
				System.out.println("Neuron " + neurons[neuronNumber].neuronID + " fired at time "+ spikeIterator.next()) ;
		}
	}
	
	/**
	 * Writes all spikes to a file, csv format
	 * @param fileName String name of file to be written to
	 */
	public void writeSpikes(String fileName) {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(fileName);
			for (int neuronNumber = 0; neuronNumber < neurons.length; neuronNumber++) {
				Iterator<Double> spikeIterator = neurons[neuronNumber].spikesOut.iterator(); // spikesOut
																								// is
																								// in
																								// AbstractNeuron
				while (spikeIterator.hasNext()) { // write a line of the file
					fileWriter.append(String.valueOf(neurons[neuronNumber].neuronID));
					fileWriter.append(",");
					fileWriter.append(String.valueOf(spikeIterator.next()));
					fileWriter.append("\n");
				}
			}

		} catch (Exception e) {
			System.out.println("NeuronalNetwork:writeSpikes error writing file " + fileName);
		} finally {
			try {
				fileWriter.flush(); // flush and close file
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
			}
		}
	} // writeSpikes



}
