function  RunSpikeSimulator(varargin)
%RunSpikeSimulator run the Java spiking neural network
%   provide a set of default values for the actual command as well as
%   all the parameters of the java
%   spiking network simulator
%   use varargin to permit the user to override any of them
%   form the string for the parameters for the simulation
%   run the simulation itself
%
%   LSS 20 February 2019

% default values for all the parameters
% more common ones

% fileprefix followed by a string, to be prepended to file names: must be before other file names
% for peseta
% fileprefix = '/Users/lss/Documents/workspace/PyramidalCells/Test_nov2018/' ; 
% for laptop
fileprefix = '/Users/lss/Documents/Research/neuronsimulation/PyramidalCells/Test_Feb20_2019/' ;
% c input spike file name, get file name for external contextual spike inputs
c = 'contextspikes.csv' ;

% d followed by input spike file name, so get file name for external driving spike inputs
d = 'drivingspikes.csv' ;

% n followed by network specifier
n = 'networkconfig.txt' ;
% sout followed by spike output file name: will be csv, (neuron, time)
sout = '' ;
% snumbersout followed by name oif file to write number iof spikes emitted
% to
snumbersout = '' ;
% wc followed by weight file for contextual inputs
% wc = '' ;
wc = 'contextweights.txt' ;
% wd followed by weight file for driving inputs
wd = 'drivingweights.txt' ;
% wd = '' ;
% wi followed by weight and delay file for internal synapses
wi = 'internalweights.txt' ;
% wi = '' ;

% t followed by end time (defaults to 5.0)
t = 5.0 ;
% s followed by sampling rate (defaults to 10000)
s = 10000 ;

% actual command to run opyramidal neuron simulation
commandtorun = 'java -jar pyramidal.jar ' ;

% less common ones

% alpha_context followed by alpha value for contextual synapses: default 1000
alpha_context=100 ;
% alpha_driver followed by alpha value for driving synapses: default 1000
alpha_driver = 1000 ;
% alpha_internal_excitatory followed by alpha value for internal excitatory synapses: default 900
alpha_internal_excitatory = 900 ;
% alpha_internal_inhibitory followed by alpha value for internal inhibitory synapses: default 200
alpha_internal_inhibitory = 200 ;
% apical_gradient followed by apical gradient for apical dendrite: default 1
apical_gradient = 1 ;
% apical_multiplier followed by apical multiplier for apical dendrite: default 1
apical_multiplier = 1 ;
% axon_threshold followed by axon threshold: default 1 (named pyr_threshold)
axon_threshold = 1 ;
% i_refractory_period followed by inhibitory neuron refractory period: default 0
i_refractory_period = 0 ;
% inhibitory_threshold followed by inhibitory neuron threshold: default 1
inhibitory_threshold = 1 ;
% p_refractory_period followed by pyramidal neuron refractory period: default 0
% values for logistic functions

logisticGradientBasal = 1.0 ;
logisticGradientTuft = 1.0 ;
logisticInterceptBasal = 0 ;
logisticInterceptTuft = 0 ;



p_refractory_period = 0 ;
% t_apical followed by time constant (tau) for apical dendrite: default 0.1
t_apicaltuft = 0.1 ;
% t_basal followed by time constant (tau) for basal dendrite: default 0.1
t_basal = 0.1 ;
% t_inhib followed by time constant (tau) for simple leaky compartment used in inhibitory neurons: default 0.2
t_inhib = 0.2 ;
% transferfunction controls effect of apical tuft & axon hillock: 2 is Kay
% & Phillips 2011
transferfunction = 1 ;
% relevant only when transferfunction == 2
tf2_k1 = 0.5 ;
tf2_k2 = 2 ; % defaults from K&P 2011
% verbosity: v controls the amout of output generated
v = 1 ;
% debugging: debug controls whether the program outputs debug info (1) or
% not (0)
debug = 0 ;



% use varargin to overwrite parameter values
i=1 ;
while(i<=size(varargin,2))
    switch lower(varargin{i})
        
        case 'fileprefix'
            fileprefix = varargin{i+1}; 
            i=i+1 ;
        case 'c'
            c = varargin{i+1}; 
            i=i+1 ;
        case 'd'
            d = varargin{i+1}; 
            i=i+1 ;
        case 'n'
            n = varargin{i+1}; 
            i=i+1 ;
        case 'sout'
            sout = varargin{i+1}; 
            i=i+1 ;
        case 'snumbersout'
            snumbersout = varargin{i+1}; 
            i=i+1 ;
        case 'wc'
            wc = varargin{i+1}; 
            i=i+1 ;
        case 'wd'
            wd = varargin{i+1}; 
            i=i+1 ;
        case 'wi'
            wi = varargin{i+1}; 
            i=i+1 ;
        case 't'
            t = varargin{i+1}; 
            i=i+1 ;
        case 's'
            s = varargin{i+1}; 
            i=i+1 ;
        case 'commandtorun'
            commandtorun = varargin{i+1}; 
            i=i+1 ;
        case 'alpha_context'
            alpha_context = varargin{i+1}; 
            i=i+1 ;
        case 'alpha_driver'
            alpha_driver =  varargin{i+1}; 
            i=i+1 ;
        case 'alpha_internal_excitatory'
            alpha_internal_excitatory = varargin{i+1}; 
            i=i+1 ;
        case 'alpha_internal_inhibitory'
            alpha_internal_inhibitory = varargin{i+1}; 
            i=i+1 ;
        case 'apical_gradient'
            apical_gradient = varargin{i+1}; 
            i=i+1 ;
        case 'apical_multiplier'
            apical_multiplier = varargin{i+1}; 
            i=i+1 ;
        case 'axon_threshold'
           axon_threshold = varargin{i+1}; 
            i=i+1 ;
        case 'i_refractory_period'
            i_refractory_period = varargin{i+1}; 
            i=i+1 ;
        case 'inhibitory_threshold'
            inhibitory_threshold  = varargin{i+1}; 
            i=i+1 ;
        case 'p_refractory_period'
            p_refractory_period = varargin{i+1}; 
            i=i+1 ;
        case 't_apicaltuft'
            t_apicaltuft = varargin{i+1}; 
            i=i+1 ;
        case 'logisticgradientbasal'
            logisticGradientBasal = varargin{i+1}; 
            i=i+1 ;
        case 'logisticgradienttuft'
            logisticGradientTuft = varargin{i+1}; 
            i=i+1 ;
        case 'logisticinterceptbasal'
            logisticInterceptBasal = varargin{i+1}; 
            i=i+1 ;
        case 'logisticintercepttuft'
            logisticInterceptTuft = varargin{i+1}; 
            i=i+1 ;
        case 't_basal'
            t_basal = varargin{i+1}; 
            i=i+1 ;
        case 't_inhib'
            t_inhib = varargin{i+1}; 
            i=i+1 ;
        case 'transferfunction'
            transferfunction  = varargin{i+1}; 
            i=i+1 ;
        case 'tfr_k1'
            tf2_k1 = varargin{i+1}; 
            i=i+1 ;
        case 'tfr_k2'
            tf2_k2 = varargin{i+1}; 
            i=i+1 ;
        case 'v'
            v = varargin{i+1}; 
            i=i+1 ;
        case 'debug'
            debug = varargin{i+1}; 
            i=i+1 ;
        otherwise
            error('RunSpikeSimulator: Unknown argument %s given',varargin{i});
    end
    i=i+1 ;
end

% put together the string for the command 
% commandtorum defines the program
% note that for the parameters to the program, strings need to be in double
% quotes
% string will be long so do this in pieces for readability
part1 = ['-fileprefix "' fileprefix '" -c "' c '" -d "' d '" -n "' n '" -sout "' sout '"' ' -snumbersout "' snumbersout '"'] ;
part2 = [' -wc "' wc '" -wd "' wd '" -wi "' wi '" -t ' num2str(t) ' -s ' num2str(s) ] ;
part3 = [' -alpha_context ' num2str(alpha_context) ' -alpha_driver ' num2str(alpha_driver) ' -alpha_internal_excitatory ' num2str(alpha_internal_excitatory) ] ;
part4 = [' -alpha_internal_inhibitory ' num2str(alpha_internal_inhibitory) ' -apical_gradient ' num2str(apical_gradient) ' -axon_threshold ' num2str(axon_threshold)] ;
part4a = [' -apical_multiplier ' num2str(apical_multiplier)] ;
part5 = [' -i_refractory_period ' num2str(i_refractory_period) ' -inhibitory_threshold ' num2str(inhibitory_threshold) ' -p_refractory_period ' num2str(p_refractory_period)] ;
part5a = [' -transferfunction ' num2str(transferfunction) ' -tf2_k1 ' num2str(tf2_k1) ' -tf2_k2 ' num2str(tf2_k2) ] ;
part5b = [' -logisticGradientBasal ' num2str(logisticGradientBasal) ' -logisticGradientTuft ' num2str(logisticGradientTuft) ] ;
part5c = [' -logisticInterceptBasal ' num2str(logisticInterceptBasal) ' -logisticInterceptTuft ' num2str(logisticInterceptTuft) ] ;
part6 = [' -t_apicaltuft ' num2str(t_apicaltuft) ' -t_basal ' num2str(t_basal)  ' -t_inhib ' num2str(t_inhib) ' -v ' num2str(v) ' -debug ' num2str(debug)] ;
% now actually run the simulation
system([commandtorun part1 part2 part3 part4 part4a part5 part5a part5b part5c part6]) ; 
% note: can use this line to generate the command string for debugging in Eclipse.
% fileprefix will need to be fully qualified, but otherwise it's usable. 

end

