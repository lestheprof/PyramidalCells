/**
 * 
 */
package Driver;

import NeuronPackage.* ; 
import Network.NeuronalNetwork;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import java.util.Properties;
import java.util.Enumeration;
import java.io.* ;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author lss
 *
 */
public class RunNeuralNetwork {

	/**
	 * 
	 */
	static int samplingRate = 10000 ;
	static double endTime = 5.0 ;

	/**
	 * @param args: -f filename for source of external spikes
	 * @throws IOException for file not found
	 */
	public static void main(String[] args) throws IOException {
		char[] networkData = null ; // holds an array of P for pyramidal or I for inhibitory
		NeuronInfo[] networkInfo = null ;
		int numberOfNeurons = 0 ;
		
		double [][] contextArray = null ;
		double [][] drivingArray = null ;
		double [][] contextSynapseWeights = null ;
		double [][] drivingSynapseWeights = null ;
		double [][] internalSynapseWeightsDelays = null ;
		
		double currentTime ; // now
		double deltaTime ; // interval between samples
		
		double tauBasal = 0.1 ; // time constant for basal compartment
		double tauApical = 0.1 ; // time constant for apical compartment
		double tauInhib = 0.2 ; // time constant for inhibitory interneuron single compartment
		
		double alphaDriving = 1000 ; // alpha value for driving synapses
		double alphaContext = 1000 ; // alpha value for contextual synapses
		double alphaInternalExcitatory = 900 ; // alpha value for internal excitatory sysnapses
		double alphaInternalInhibitory = 200 ; // alpha value for internal inhibitory sysnapses
		
		double apicalMultiplier = 1 ; // multiplier for apical dendrite: output = mult * logistic(gradient * input)
		double apicalGradient = 1 ;
		
		double pyrThreshold = 1 ; // threshold for axon hillock
		double inhThreshold = 1 ; // threshold for inhbitory neurons
		double pyrRefractoryPeriod = 0 ; // default pyramidal RP is no RP
		double inhRefractoryPeriod = 0 ; // default inhibitory refractory period is 0
		


		int argno = 0 ;
		while (argno < args.length)
			switch(args[argno]){
			case "-c": // followed by input spike file name, so get file name for external contextual spike inputs
				contextArray = readInputsToArrayFromFile(args[argno + 1], 3); // neuron, synapse time
				argno = argno + 2 ;
				break;
			case "-d": // followed by input spike file name, so get file name for external driving spike inputs
				drivingArray = readInputsToArrayFromFile(args[argno + 1], 3);	// neuron synapse time
				argno = argno + 2 ;
				break;
			case "-s": // followed by sampling rate (defaults to 10000)
				samplingRate = Integer.parseInt(args[argno + 1]) ;
				argno = argno + 2 ;
				break ;
			case "-t": // followed by end time (defaults to 5.0)
				endTime = Double.parseDouble(args[argno + 1]) ;
				argno = argno + 2 ;
				break ;
			case "-n": // network specifier
				networkData = readNetworkFromFile(args[argno + 1]) ;
				numberOfNeurons = networkData.length ;
				argno = argno + 2 ;
				break ;
			case "-wd": // followed by weight file for driving inputs
				// for now, we assume that the compartment number for these driving  weights is 1.
				drivingSynapseWeights = readInputsToArrayFromFile(args[argno + 1], 3); // neuron number, synapse number, weight
				argno = argno + 2 ;
				break ;
			case "-wc": // followed by weight file for contextual inputs
				// for now, we assume that the compartment number for these contextual weights is 2.
				contextSynapseWeights = readInputsToArrayFromFile(args[argno + 1], 3); // neuron number, synapse number, weight
				argno = argno + 2 ;
				break  ;
			case "-wi": // followed by weight and delay file for internal synapses
				internalSynapseWeightsDelays = readInputsToArrayFromFile(args[argno + 1], 5) ; // presynaptic neuron, postsynaptic neuron, postsynaptic compartment, weight, delay
				argno = argno + 2 ;
				break ;
			case "-t_basal": // time constant (tau) for basal dendrite
				tauBasal = Double.parseDouble(args[argno + 1]) ;
				argno = argno + 2 ;
				break ;
			case "-t_apical": // time constant (tau) for basal dendrite
				tauApical = Double.parseDouble(args[argno + 1]) ;
				argno = argno + 2 ;
				break ;
			case "-t_inhib": // time constant (tau) for simple leaky compartment used in inhibitory neurons
				tauInhib = Double.parseDouble(args[argno + 1]) ;
				argno = argno + 2 ;
				break ;
			case "-alpha_driver": // alpha value for driving synapses
				alphaDriving = Double.parseDouble(args[argno + 1]) ;
				argno = argno + 2 ;
				break ;
			case "-alpha_context": // alpha value for contextual synapses	
				alphaContext = Double.parseDouble(args[argno + 1]) ;
				argno = argno + 2 ;
				break ;	
			case "-alpha_internal_excitatory": // alpha value for internal excitatory synaposes
				alphaInternalExcitatory = Double.parseDouble(args[argno + 1]) ;
				argno = argno + 2 ;
				break ;	
			case "-alpha_internal_inhibitory": // alpha value for internal inhibitory synapses
				alphaInternalInhibitory = Double.parseDouble(args[argno + 1]) ;
				argno = argno + 2 ;
				break ;	
			case "-apical_multiplier": // apical multiplier for apical dendrite	
				apicalMultiplier = Double.parseDouble(args[argno + 1]) ;
				argno = argno + 2 ;
				break  ;
			case "-apical_gradient": // apical gradient for apical dendrite	
				apicalGradient = Double.parseDouble(args[argno + 1]) ;
				argno = argno + 2 ;
				break  ;
			case "-axon_threshold": // axon threshold
				pyrThreshold = Double.parseDouble(args[argno + 1]) ;
				argno = argno + 2 ;
				break  ;
			case "-inhibitory_threshold": // inhibitory neuron threshold
				inhThreshold = Double.parseDouble(args[argno + 1]) ;
				argno = argno + 2 ;
				break  ;
			case "-p_refractory_period": // pyramidal neuron refractory period
				pyrRefractoryPeriod = Double.parseDouble(args[argno + 1]) ;
				argno = argno + 2 ;
				break  ;
			case "-i_refractory_period": // inhibitory neuron refractory period
				inhRefractoryPeriod = Double.parseDouble(args[argno + 1]) ;
				argno = argno + 2 ;
				break  ;
			default:
				System.out.println("Unexpected value in arguments = " + args[argno]);
				argno = argno + 1 ;
				break ;
			}
		// set up the neuronalNetwork with values that go across all neurons
		NeuronalNetwork NN = new NeuronalNetwork(samplingRate) ;
		// set up the neuron information, with id = 1 (only 1 for now)
		networkInfo = new NeuronInfo[numberOfNeurons] ;
		for (int nno = 0; nno < numberOfNeurons; nno++)
		{
			if (networkData[nno] == 'P')
					networkInfo[nno]  = new PyramidalNeuronInfo(nno + 1, samplingRate, tauBasal, tauApical, apicalMultiplier, 
				apicalGradient, pyrThreshold, pyrRefractoryPeriod) ;
			else if (networkData[nno] == 'I')
				networkInfo[nno]  = new InterNeuronInfo(nno + 1, samplingRate, tauInhib, inhThreshold, inhRefractoryPeriod) ;
			else
				System.err.println("RunSingleNeuron.main: unidentified neuron type = " + networkData[nno]);
		}
		// set up the neural network
		NN.setup(networkInfo) ;
		
		// set up the synapses on this network
		
		if (contextSynapseWeights != null)
		{
			NN.setUpExternalContextSynapses(contextSynapseWeights, alphaContext);
		}
		else {
			System.err.println("main: No context synapse file. Exiting. BooHooHoo");
			System.exit(1);
		}
		
		if (drivingSynapseWeights != null)
		{
			// System.out.println("drivingSynapseWeights.length = " + drivingSynapseWeights.length);
			NN.setUpExternalDrivingSynapses(drivingSynapseWeights, alphaDriving);
		}
		else {
			System.err.println("main: No driving synapse file. Exiting. BooHoo");
			System.exit(1);
		}
		
		if (internalSynapseWeightsDelays != null)
		{
			// set up internal synapse
			NN.setUpInternalSynapses(internalSynapseWeightsDelays, alphaInternalExcitatory, alphaInternalInhibitory);
		}
		else System.out.println("No internal synapses provided") ;
		// store the driving inputs (drivingArray) at the neuron
		NN.setDrivingInputs(drivingArray);
		// store the context inputs (contextArray) at the neuron
		NN.setContextualInputs(contextArray);
		
		// simulation loop
		currentTime = 0 ; // start at 0
		deltaTime = 1.0/samplingRate ; // time increment (sample interval)
		System.out.println("Simulation starting");

		while (currentTime < endTime){
			// run neuron 1 time step
			NN.run(currentTime);
			currentTime = currentTime + deltaTime ;
		}
		// generated spikes are in neuron.spikesOut
		System.out.println("Spikes generated:");
		NN.displaySpikes();
		System.out.println("Simulation ended");
		
	}
	
	private static char[]  readNetworkFromFile(String filename) throws IOException
	{
		List<String> InputList = null;

		Path inputFilePath = Paths.get(filename);
		try {
			InputList = Files.readAllLines(inputFilePath);
		} catch (IOException e) {
			System.err.println("readNetworkFromFile: Caught IOException: " + e.getMessage());
			System.exit(1);
		}
		// have content in InputList
		char[] returnString = new char[InputList.size()] ;
		Iterator<String> inputIterator = InputList.iterator();
		int nno = 0 ;
		while (inputIterator.hasNext()) {
			String nextInput = inputIterator.next().trim();
			String[] inputComponents = nextInput.split("\\s+|,", 2); // allows spaces or single commas as separators
			returnString[nno] = inputComponents[1].charAt(inputComponents[1].length() - 1) ;
			nno = nno + 1 ;
		}
		return returnString ;
	}
	
	/*
	 * @param filename: name of file o be read
	 * @param numPerLine number of doubles expected per line. 
	 */
	private static double[][] readInputsToArrayFromFile(String filename, int numPerLine )  
			throws IOException, NumberFormatException
	/*
	 * Reads the input in to a 2D
	 * array where the array is N by numPerLine, with each row being input
	 * (neuron/channel) number, synapse, time (seconds) for external inputs
	 * neuron number, synapse number, weight for external synapses
	 * presynaptic neuron, postsynaptic neuron, postsynaptic compartment, weight, delay for internal synapses
	 */
	{
		int lineNo = 0;
		List<String> InputList = null;

		Path inputFilePath = Paths.get(filename);
		try {
		InputList = Files.readAllLines(inputFilePath);
		} catch (IOException e)
		{
			System.err.println("readInputsToArrayFromFile: Caught IOException: " + e.getMessage());
			System.exit(1); 
		}

		double[][] inputArray = new double[InputList.size()][numPerLine];
		// now turn InputList into an array, line by line
		Iterator<String> inputIterator = InputList.iterator();
		while (inputIterator.hasNext()) {
			String nextInput = inputIterator.next().trim();
			String[] inputComponents = nextInput.split("\\s+|,", numPerLine); // allows spaces or single commas as separators
			for (int i = 0; i < numPerLine; i++) {
				try{
				inputArray[lineNo][i] = Double.parseDouble(inputComponents[i]);
				} catch (NumberFormatException e)
				{
					System.err.println("readInputsToArrayFromFile: Caught NumberFormatException: " + e.getMessage());
					System.exit(1);
				}
				
			}
			lineNo = lineNo + 1;
		}
		return inputArray;
	}

}
