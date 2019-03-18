function  [spikesOutArray] = runMultipleIntercept(fileprefix, varargin)
%runMultipleIntercept Runs RunSpikeSimulator multiple times using different
%intercepts
%   Used to run RunSpikeSimulator for a single set of parameters (with null file inputs).
%  For proper sciuentific usage, some of the fixed values need to
%  be normal or varargin parameters so that a parameter search can be
%  accompolished
% returns an array, driveintercepts by contextintercepts, of integers where wach integer
% shows the number of neuron 1 output spikes for that run. This can thne
% be plotted.
%
%
% set number of context intercept levels
driveintercepts = 20 ;%10 ;
% set number of driving intercept levels
contextintercepts = 25 ; %10 ;
% set drive file name prefix
drivefileprefix = 'drive__' ;
% set context name prefix
contextfileprefix = 'context__' ;
% set drive file weight name
dwfile = 'drivingweights.txt' ; % needed but won't change result
% set context file weight name
cwfile = 'contextweights.txt' ;  % needed but won't change result
% set outputfile prefix
outfileprefix = 'nspikesmar182019_' ;
%
% runing simulated time
runtime = 5 ;
% defaults for logistic functions.
logisticGradientBasal = 1.0 ;
logisticGradientTuft = 1.0 ;
logisticInterceptBasalLow = 0 ;
logisticInterceptTuftLow = 0 ;
logisticInterceptBasalHigh = -1.0 ;
logisticInterceptTuftHigh = -1.0 ;

tf =  1 ; % just one transfer function at the momnt.

% use varargin to overwrite parameter values
i=1 ;
while(i<=size(varargin,2))
    switch lower(varargin{i})
        case 'logisticgradientbasal'
            logisticGradientBasal = varargin{i+1};
            i=i+1 ;
        case 'logisticgradienttuft'
            logisticGradientTuft = varargin{i+1};
            i=i+1 ;
        case 'logisticinterceptbasallow'
            logisticInterceptBasalLow = varargin{i+1};
            i=i+1 ;
        case 'logisticintercepttuftlow'
            logisticInterceptTuftLow = varargin{i+1};
            i=i+1 ;
        case 'logisticinterceptbasalhigh'
            logisticInterceptBasalHigh = varargin{i+1};
            i=i+1 ;
        case 'logisticintercepttufthigh'
            logisticInterceptTuftHigh = varargin{i+1};
            i=i+1 ;
        case 'driveintercepts'
            driveintercepts = varargin{i+1};
            i=i+1 ;
        case 'contextintercepts'
            contextintercepts = varargin{i+1};
            i=i+1 ;
        case 't' % running time
            runtime = varargin{i+1};
            i=i+1 ;
        otherwise
            error('runMultiple: Unknown argument %s given',varargin{i});
    end
    i=i+1 ;
end

% use null files for both context and drive
cfname = '' ;
dfname = [drivefileprefix num2str(0) '.csv'] ;
BasalDelta =  (logisticInterceptBasalHigh - logisticInterceptBasalLow) / driveintercepts ;
TuftDelta = (logisticInterceptTuftHigh - logisticInterceptTuftLow) / contextintercepts ;

for bi = 1:driveintercepts
    logisticInterceptBasal = logisticInterceptBasalLow + ((bi - 1) * BasalDelta) ;
    for ci = 1:contextintercepts
        logisticInterceptTuft = logisticInterceptTuftLow + ((ci - 1) * TuftDelta) ;
        outfilename = [outfileprefix num2str(bi) '_' num2str(ci) '.csv'] ;
        % and run it 
        RunSpikeSimulator('fileprefix', fileprefix, 'c', ...
            cfname,'t', runtime , 'd', dfname, 'v', 1, 'debug', 0, ...
            'wc', cwfile, 'wd', dwfile, ...
        't_basal', 0.05, 't_apicaltuft', 0.05, 'alpha_context', 400, 'alpha_driver', 400, 'apical_multiplier', 1.0, 'wi', '', ...
         'snumbersout', outfilename, 'transferfunction', tf, 'p_refractory_period', 0.012, ...
     'logisticGradientBasal',   logisticGradientBasal,  'logisticGradientTuft', logisticGradientTuft, ... 
     'logisticInterceptBasal', logisticInterceptBasal, 'logisticInterceptTuft', logisticInterceptTuft ...
     ) ;
    end
end

% now read the snumbersout files and create a 2D array from them
% may then delete these files (otherwise there's an awful lot of files
% generated)
spikesOutArray = zeros([driveintercepts contextintercepts]) ; % both now have a number 0
for bi = 1:driveintercepts
    dfnostring = num2str(bi) ;
    for cc = 1:contextintercepts
        filedata = csvread([fileprefix outfileprefix dfnostring '_' num2str(cc) '.csv']) ;
        spikesOutArray(bi, cc) = filedata(1, 2) ;
    end
end
% delete the output spike number files
system(['rm ' fileprefix outfileprefix '*']) ;

        figure ; surf(spikesOutArray) ; title(['g basal = ', num2str(logisticGradientBasal), ' g tuft', num2str(logisticGradientTuft) ]) ;
        xlabel('context intercept') ;
        ylabel('drive intercept') ;
        zlabel('spikes') ;


        
end

