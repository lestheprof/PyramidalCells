/**
 * 
 */
package Driver;

import NeuronPackage.PyramidalNeuron;

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
public class RunSingleNeuron {

	/**
	 * 
	 */
	static int samplingRate = 10000 ;
	static double endTime = 5.0 ;

	/**
	 * @param args: -f filename for source of external spikes
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		double [][] contextArray = null ;
		double [][] drivingArray = null ;
		double [][] contextSynapseWeights = null ;
		double [][] drivingSynapseWeights = null ;
		
		double currentTime ; // now
		double deltaTime ; // interval between samples
		
		double tauBasal = 0.1 ; // time constant for basal compartment
		double tauApical = 0.1 ; // time constant for apical compartment
		
		double alphaDriving = 1000 ; // alpha value for driving synapses
		double alphaContext = 1000 ; // alpha value for contextual synapses
		
		double apicalMultiplier = 1 ; // multiplier for apical dendrite: output = mult * logistic(gradient * input)
		double apicalGradient = 1 ;
		
		double threshold = 1 ; // threshold for axon hillock


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
			case "-t_basal": // time constant (tau) for basal dendrite
				tauBasal = Double.parseDouble(args[argno + 1]) ;
				argno = argno + 2 ;
				break ;
			case "-t_apical": // time constant (tau) for basal dendrite
				tauApical = Double.parseDouble(args[argno + 1]) ;
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
			case "-apical_multiplier": // apical multiplier for apical dendrite	
				apicalMultiplier = Double.parseDouble(args[argno + 1]) ;
				argno = argno + 2 ;
				break  ;
			case "-apical_gradient": // apical gradient for apical dendrite	
				apicalGradient = Double.parseDouble(args[argno + 1]) ;
				argno = argno + 2 ;
				break  ;
			case "axon_threshold": // axon threshold
				threshold = Double.parseDouble(args[argno + 1]) ;
				argno = argno + 2 ;
				break  ;
			default:
				System.out.println("Unexpected value in arguments = " + args[argno]);
				argno = argno + 1 ;
				break ;
			}
		
		// set up the neuron, with id = 1
		PyramidalNeuron neuron = new PyramidalNeuron(1, samplingRate, tauBasal, tauApical, apicalMultiplier, 
				apicalGradient, threshold) ;
		// set up the synapses on this neuron
		
		if (contextSynapseWeights != null)
		{
			neuron.setUpExternalContextSynapses(contextSynapseWeights, alphaContext);
		}
		else {
			System.err.println("main: No context synapse file. Exiting. BooHooHoo");
			System.exit(1);
		}
		
		if (drivingSynapseWeights != null)
		{
			// System.out.println("drivingSynapseWeights.length = " + drivingSynapseWeights.length);
			neuron.setUpExternalDrivingSynapses(drivingSynapseWeights, alphaDriving);
		}
		else {
			System.err.println("main: No driving synapse file. Exiting. BooHoo");
			System.exit(1);
		}
		
		// store the driving inputs (drivingArray) at the neuron
		neuron.setDrivingInputs(drivingArray);
		// store the context inputs (contextArray) at the neuron
		neuron.setContextualInputs(contextArray);
		
		// simulation loop
		currentTime = 0 ; // start at 0
		deltaTime = 1.0/samplingRate ; // time increment (sample interval)
		System.out.println("Simulation starting");

		while (currentTime < endTime){
			// run neuron 1 time step
			neuron.run(currentTime);
			currentTime = currentTime + deltaTime ;
		}
		// generated spikes are in neuron.spikesOut
		System.out.println("Spikes generated:");
		Iterator <Double> spikeIterator = neuron.spikesOut.iterator();
		while (spikeIterator.hasNext())
			System.out.println("Neuron " + neuron.neuronID + " fired at time "+ spikeIterator.next()) ;
		System.out.println("Simulation ended");
		
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
	 * (neuron/channel) number, time (seconds)
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
