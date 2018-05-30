package NeuronPackage;

/*
 * Holds information about an interneuron
 */

public class InterNeuronInfo extends NeuronInfo {
	
	public double tauInhib ;
	public double threshold ;
	public double refractoryPeriod ;
	
	public InterNeuronInfo(int identity, int samplingRate, double tauInhib, double threshold, double refractoryPeriod) {
		super(identity, samplingRate);
		this.tauInhib = tauInhib ;
		this.threshold = threshold ;
		this.refractoryPeriod = refractoryPeriod ;
	}

}
