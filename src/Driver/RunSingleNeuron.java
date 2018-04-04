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


	/**
	 * @param args: -f filename for source of external spikes
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		List<String> spikeList ;
		int argno = 0 ;
		while (argno < args.length)
			switch(args[argno]){
			case "-f": // followed by input spike file name, so get file name for external spike inputs
				Path inputFilePath = Paths.get(args[argno + 1]); // get the path to the input file 
				spikeList = Files.readAllLines(inputFilePath);
				argno = argno + 2 ;
				break;
			default:
				System.out.println("Unexpected value in arguments = " + args[argno]);
				argno = argno + 1 ;
				break ;
			}
		
		// set up the neuron,
		PyramidalNeuron neuron = new PyramidalNeuron("SingleNeuron") ;

	}

}
