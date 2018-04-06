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
		double currentTime ; // now
		double deltaTime ; // interval between samples

		int argno = 0 ;
		while (argno < args.length)
			switch(args[argno]){
			case "-c": // followed by input spike file name, so get file name for external contextual spike inputs
				contextArray = readInputsToArrayFromFile(args[argno + 1]);
				argno = argno + 2 ;
				break;
			case "-d": // followed by input spike file name, so get file name for external driving spike inputs
				drivingArray = readInputsToArrayFromFile(args[argno + 1]);	
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
			default:
				System.out.println("Unexpected value in arguments = " + args[argno]);
				argno = argno + 1 ;
				break ;
			}
		
		// set up the neuron,
		PyramidalNeuron neuron = new PyramidalNeuron("SingleNeuron", samplingRate) ;
		// set up the synapses on this neuron
		
		neuron.setUpExternalContextSynapses(contextArray.length);
		neuron.setUpExternalDrivingSynapses(drivingArray.length);
		
		// simulation loop
		currentTime = 0 ; // start at 0
		deltaTime = 1.0/samplingRate ; // time increment (sample interval)
		while (currentTime < endTime){
			// run neuron 1 time step
			neuron.run(currentTime);
			currentTime = currentTime + deltaTime ;
		}
		System.out.println("Simulation ended");
		
	}
	
	private static double[][] readInputsToArrayFromFile(String filename )  throws IOException, NumberFormatException
	/*
	 * Reads the input in to a 2D
	 * array where the array is N by 2, with each row being input
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

		double[][] inputArray = new double[InputList.size()][2];
		// now turn InputList into an array, line by line
		Iterator<String> inputIterator = InputList.iterator();
		while (inputIterator.hasNext()) {
			String nextInput = inputIterator.next().trim();
			String[] inputComponents = nextInput.split("\\s+", 2);
			for (int i = 0; i < 2; i++) {
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
