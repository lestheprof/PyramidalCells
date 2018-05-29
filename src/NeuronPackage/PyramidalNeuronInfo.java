package NeuronPackage;
/*
 * Class to hold information about a single Pyramidal neuron
 */
public class PyramidalNeuronInfo extends NeuronInfo {
	

	public double tauBasal ;
	public double tauApical ;
	public double apicalMultiplier ;
	public double apicalGradient ;
	public double threshold ;
	public double refractoryPeriod ;
	
	public PyramidalNeuronInfo(int identity, int samplingRate, double tauBasal, double tauApical, double apicalMultiplier, 
			double apicalGradient, double threshold, double refractoryPeriod) {
		super(identity, samplingRate) ; // for all sorts of neurons
		this.tauBasal = tauBasal ;
		this.tauApical = tauApical ;
		this.apicalMultiplier = apicalMultiplier ;
		this.apicalGradient = apicalGradient ;
		this.threshold = threshold ;
		this.refractoryPeriod = refractoryPeriod ;		
	}

}
