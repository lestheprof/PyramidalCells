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
 * Runs the neural network. Called with flags to set up operation.
 * @author lss
 */
public class RunNeuralNetwork {

	/**
	 * Runs the neural network: contains main method called from command line.
	 */
	static int samplingRate = 10000 ;
	static double endTime = 5.0 ;

	/**
	 * @param args: -alpha_context followed by alpha value for contextual synapses: default 1000
	 * @param args: -alpha_driver followed by alpha value for driving synapses: default 1000
	 * @param args: -alpha_internal_excitatory followed by alpha value for internal excitatory synapses: default 900
	 * @param args: -alpha_internal_inhibitory followed by alpha value for internal inhibitory synapses: default 200
	 * @param args: -apical_gradient followed by apical gradient for apical dendrite: default 1
	 * @param args: -apical_multiplier followed by apical multiplier for apical dendrite: default 1
	 * @param args: -axon_threshold followed by axon threshold: default 1 (named pyr_threshold)
	 * @param args: -c input spike file name, get file name for external contextual spike inputs
	 * @param args: -d followed by input spike file name, so get file name for external driving spike inputs
	 * @param args: -debug followed by 1 (true) or 0 (false) to set debug on/off
	 * @param args: -fileprefix followed by a string, to be prepended to file names: must be before other file names
	 * @param args: -i_refractory_period followed by inhibitory neuron refractory period: default 0
	 * @param args: -inhibitory_threshold followed by inhibitory neuron threshold: default 1
	 * @param args: -logisticGradientBasal followed by double defining logistic gradient for basal compartment
	 * @param args: -logisticGradientTuft followed by double defining logistic gradient for apical tuft compartment
	 * @param args: -logisticInterceptBasal followed by double defining logistic intercept for basal compartment
	 * @param args: -logisticInterceptTuft followed by double defining logistic gradient for apical tuft compartment
	 * @param args: -n followed by network specifier
	 * @param args: -p_refractory_period followed by pyramidal neuron refractory period: default 0
	 * @param args: -s followed by sampling rate (defaults to 10000)
	 * @param args: -snumbersout followed by file name for number of spikes emitted by each neuron. default null.
	 * @param args: -sout followed by spike output file name: will be csv, (neuron, time)
	 * @param args: -t followed by end time (defaults to 5.0)
	 * @param args: -t_apicaltuft followed by time constant (tau) for apical tuft: default 0.1
	 * @param args: -t_basal followed by time constant (tau) for basal dendrite: default 0.1
	 * @param args: -t_inhib followed by time constant (tau) for simple leaky compartment used in inhibitory neurons: default 0.2
	 * @param args: -tf2_k1 followed by K1 value to use when transferfunction==2 is selected.
	 * @param args: -tf2_k2 followed by K2 value to use when transferfunction==2 is selected.
	 * @param args: -transferfunction followed by 1 (original) or 2 (Kay and Phillips 2011) apical dendrite and axon hillock function selector
	 * @param args: -v followed by verbosity: controls amount of system.out data created: default 1
	 * @param args: -wc followed by weight file for contextual inputs
	 * @param args: -wd followed by weight file for driving inputs
	 * @param args: -wi followed by weight and delay file for internal synapses
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
		String spikeOutFileName = null ;
		String sNumbersOutName = null ;
		String filePrefix = null ; // used to prepend =to file names
		
		double currentTime ; // now
		double deltaTime ; // interval between samples
		
		double tauBasal = 0.1 ; // time constant for basal compartment
		double logisticGradientBasal = 3.0 ; // logistic gradient for basal compartment
		double logisticInterceptBasal = 0.5 ; // logistic intercept (offset) for basal compartment
		double tauApicalTuft = 0.1 ; // time constant for apical tuft compartment
		double logisticGradientTuft = 3.0 ; // logistic gradient for apical tuft compartment
		double logisticInterceptTuft = 0.5 ; // logistic intercept (offset) for apical tuft compartment

		double tauInhib = 0.2 ; // time constant for inhibitory interneuron single compartment
		
		double alphaDriving = 1000 ; // alpha value for driving synapses
		double alphaContext = 1000 ; // alpha value for contextual synapses
		double alphaInternalExcitatory = 900 ; // alpha value for internal excitatory sysnapses
		double alphaInternalInhibitory = 200 ; // alpha value for internal inhibitory sysnapses
		
		double apicalMultiplier = 1 ; // multiplier for apical dendrite: output = mult * logistic(gradient * input)
		double apicalGradient = 1 ;
		int transferfunction = 1 ; // defines the transfer function used in apical dendrite & axon hillock
		double K1 = 0.5 ; // for use with transfer function  == 2 (TF2_K1), from Kay & Phillips 2011
		double K2 = 1 ; // for use with transfer function  == 2 (TF2_K2), from Kay & Phillips 2011
		
		double pyrThreshold = 1 ; // threshold for axon hillock
		double inhThreshold = 1 ; // threshold for inhbitory neurons
		double pyrRefractoryPeriod = 0 ; // default pyramidal RP is no RP
		double inhRefractoryPeriod = 0 ; // default inhibitory refractory period is 0
		
		int verbosity = 1; // default for amount of system.out output created
		boolean debug = false ;
		


		int argno = 0 ;
		while (argno < args.length)
			switch(args[argno].toLowerCase()){
			case "-fileprefix": // must come before actual file names in which it's used
				filePrefix = args[argno + 1] ;
				argno = argno + 2 ;
				break;	
			case "-c": // followed by input spike file name, so get file name for external contextual spike inputs
				if (!args[argno + 1].isEmpty())// allow empty string to mean no file
					contextArray = readInputsToArrayFromFile(filePrefix + args[argno + 1], 3); // neuron, synapse time
				argno = argno + 2 ;
				break;
			case "-d": // followed by input spike file name, so get file name for external driving spike inputs
				if (!args[argno + 1].isEmpty())// allow empty string to mean no file
					drivingArray = readInputsToArrayFromFile(filePrefix + args[argno + 1], 3);	// neuron synapse time
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
				networkData = readNetworkFromFile(filePrefix + args[argno + 1]) ;
				numberOfNeurons = networkData.length ;
				argno = argno + 2 ;
				break ;
			case "-wd": // followed by weight file for driving inputs
				if (!args[argno + 1].isEmpty())// allow empty string to mean no file (but will cause system, to exit)
				// for now, we assume that the compartment number for these driving  weights is 1.
					drivingSynapseWeights = readInputsToArrayFromFile(filePrefix + args[argno + 1], 3); // neuron number, synapse number, weight
				argno = argno + 2 ;
				break ;
			case "-wc": // followed by weight file for contextual inputs
				if (!args[argno + 1].isEmpty())// allow empty string to mean no file (but will cause system to exit)
				// for now, we assume that the compartment number for these contextual weights is 2.
					contextSynapseWeights = readInputsToArrayFromFile(filePrefix + args[argno + 1], 3); // neuron number, synapse number, weight
				argno = argno + 2 ;
				break  ;
			case "-wi": // followed by weight and delay file for internal synapses
				if (!args[argno + 1].isEmpty()) // allow empty string to mean no file (but this is OK)
					internalSynapseWeightsDelays = readInputsToArrayFromFile(filePrefix + args[argno + 1], 5) ; // presynaptic neuron, postsynaptic neuron, postsynaptic compartment, weight, delay
				argno = argno + 2 ;
				break ;
			case "-sout": // followed by spike output file name: will be csv, <neuron, time>
				if (!new String(args[argno + 1]).isEmpty()) // allow empty string to mean no file
					spikeOutFileName = filePrefix + new String(args[argno + 1]);
				argno = argno + 2 ;
				break ;
			case "-snumbersout":
			if (!new String(args[argno + 1]).isEmpty()) // allow empty string to mean no file
				sNumbersOutName = filePrefix + new String(args[argno + 1]);
				argno = argno + 2 ;
				break ;
			case "-t_basal": // time constant (tau) for basal dendrite
				tauBasal = Double.parseDouble(args[argno + 1]) ;
				argno = argno + 2 ;
				break ;
			case "-t_apicaltuft": // time constant (tau) for apical tuft dendrite
				tauApicalTuft = Double.parseDouble(args[argno + 1]) ;
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
			case "-transferfunction":
				transferfunction = Integer.parseInt(args[argno + 1]) ;
				argno = argno + 2 ;
				break   ;
			case "-logisticgradientbasal":
				logisticGradientBasal = Double.parseDouble(args[argno + 1]) ;
				argno = argno + 2 ;
				break   ;
			case "-logisticgradienttuft":
				logisticGradientTuft = Double.parseDouble(args[argno + 1]) ;
				argno = argno + 2 ;
				break   ;
			case "-logisticinterceptbasal":
				logisticInterceptBasal = Double.parseDouble(args[argno + 1]) ;
				argno = argno + 2 ;
				break   ;
			case "-logisticintercepttuft":
				logisticInterceptTuft = Double.parseDouble(args[argno + 1]) ;
				argno = argno + 2 ;
				break   ;
			case "-tf2_k1":
				K1 = Double.parseDouble(args[argno + 1]) ;
				argno = argno + 2 ;
				break  ;
			case "-tf2_k2":
				K2 = Double.parseDouble(args[argno + 1]) ;
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
			case "-v": // verbosity of system.out data
				verbosity = Integer.parseInt(args[argno + 1]) ;
				argno = argno + 2 ;
				break ;
			case "-debug": // debug opnm or off
				int debuglevel = Integer.parseInt(args[argno + 1]) ; ;
				if (debuglevel == 1)
					debug = true ;
				argno = argno + 2 ;
				break  ;
			default:
				System.out.println("Unexpected value in arguments = " + args[argno]);
				argno = argno + 1 ;
				break ;
			}
		// set up the neuronalNetwork with values that go across all neurons
		NeuronalNetwork NN = new NeuronalNetwork(samplingRate, debug) ;
		// set up the neuron information, with id = 1 (only 1 for now)
		networkInfo = new NeuronInfo[numberOfNeurons] ;
		for (int nno = 0; nno < numberOfNeurons; nno++)
		{
			if (networkData[nno] == 'P')
					networkInfo[nno]  = new PyramidalNeuronInfo(nno + 1, samplingRate, tauBasal, logisticGradientBasal,
							logisticInterceptBasal, tauApicalTuft, logisticGradientTuft, logisticInterceptTuft, apicalMultiplier, 
				apicalGradient, pyrThreshold, pyrRefractoryPeriod, transferfunction, K1, K2) ;
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
		// can we assume that there are driving inputs?
		NN.setDrivingInputs(drivingArray);
		// store the context inputs (contextArray) at the neuron unless it's null
		if (contextArray != null)
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
		if (spikeOutFileName != null)
		{
			System.out.println("Spikes written to file: " + spikeOutFileName);
			// save spikes to  file here
			// save spikes to spikeOutFileName file using a method in NeuralNetwork
			// format should be the same as for the driving or context spikes. 
			// but there's no synapse number, just <neuron><time>, .csv format for maximal ease of reuse. 
			NN.writeSpikes(spikeOutFileName);
		}
		// output numbers of spikes emitted by each neuron to a file, so as to be reusable in Matlab
		if (sNumbersOutName != null)
		{
			System.out.println("Numbder of spikes emitted by each neuron written to file: " + sNumbersOutName);
			// save them here by calling a method in NN
			NN.writeNumbersOfSpikes(sNumbersOutName) ;
		}
		if (verbosity >= 1)
			NN.displayNumberOfSpikes();
		if (verbosity >=2)
		{
			System.out.println("Spikes Generated:") ;
			NN.displaySpikes();
		}
		System.out.println("Simulation ended");
		
	}
	
	/**
	 * Last update 8 November 2018
	 * @param filename: name of file from which network configuration is to be read
	 * Each line has form n P|I
	 * and there has to be an entry for each n, from 1 up to number of neurons.
	 * note that neurons in the simulation are numbered from 0 to n-1
	 * @return string of length number of neurons with each character being the type of the neuron (P or I currently).
	 * @throws IOException if file not found
	 * @throws NumberFormatException if neuron number not an integer 
	 * @author lss
	 */
	private static char[]  readNetworkFromFile(String filename) throws IOException, NumberFormatException
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
			try{
				nno = Integer.parseInt(inputComponents[0]) - 1; // neurons are numbered from 1 upwards
			} catch (NumberFormatException e) {
				System.err.println("readNetworkFromFile: Caught NumberFormatException: " + e.getMessage());
				System.exit(1);
			}
			returnString[nno] = inputComponents[1].charAt(inputComponents[1].length() - 1) ;
		}
		return returnString ;
	}
	
	/**
	 * @param filename: name of file o be read
	 * @param numPerLine number of doubles expected per line. 
	 * @return array of doubles,input list size by numPerLine  
	 * @throws IOException if file not found
	 * @throws NumberFormatException if number  in file not convertable to doubles
	 */
	private static double[][] readInputsToArrayFromFile(String filename, int numPerLine )  
			throws IOException, NumberFormatException
	/**
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
		} catch (IOException e) {
			System.err
					.println("readInputsToArrayFromFile: File= " + filename + " Caught IOException: " + e.getMessage());
			System.exit(1);
		}
		/* cope with empty file */
		if (InputList.isEmpty()) {
			return null;
		}

		double[][] inputArray = new double[InputList.size()][numPerLine];

		// now turn InputList into an array, line by line
		Iterator<String> inputIterator = InputList.iterator();
		while (inputIterator.hasNext()) {
			String nextInput = inputIterator.next().trim();
			if (!nextInput.isEmpty()) {
				// allows spaces or single commas as separators
				String[] inputComponents = nextInput.split("\\s+|,", numPerLine); 
				for (int i = 0; i < numPerLine; i++) {
					try {
						inputArray[lineNo][i] = Double.parseDouble(inputComponents[i]);
					} catch (NumberFormatException e) {
						System.err.println("readInputsToArrayFromFile: File= " + filename
								+ " Caught NumberFormatException: " + e.getMessage());
						System.exit(1);
					}

				}
				lineNo = lineNo + 1;
			}
		}

		return inputArray;
	}

}
