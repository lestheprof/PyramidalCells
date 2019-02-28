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
	public int transferfunction ;
	public double K1 ; // only meaningful when transferfunction == 2: used as in Kay & Phillips 2011
	public double K2 ; // only meaningful when transferfunction == 2
	
	public PyramidalNeuronInfo(int identity, int samplingRate, double tauBasal, double tauApical, double apicalMultiplier, 
			double apicalGradient, double threshold, double refractoryPeriod, int transferfunction, double K1, double K2) {
		super(identity, samplingRate) ; // for all sorts of neurons
		this.tauBasal = tauBasal ;
		this.tauApical = tauApical ;
		this.apicalMultiplier = apicalMultiplier ;
		this.apicalGradient = apicalGradient ;
		this.threshold = threshold ;
		this.refractoryPeriod = refractoryPeriod ;
		this.transferfunction = transferfunction ;
		this.K1 = K1 ;
		this.K2 = K2 ;
	}

}
