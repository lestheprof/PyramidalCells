function  [spikesOutArray] = runMultiple(fileprefix, tf, varargin)
%runMultiple Runs RunSpikeSimulator multiple times using different spike
%rates for both driving and context. 
%   Used to run RunSpikeSimulator for a single set of parameters (with different file inputs).
%  For proper sciuentific usage, some of the fixed values need to
%  be normal or varargin parameters so that a parameter search can be
%  accompolished
% returns an array, driveno by contextno, of integers where wach integer
% shows the number of neuron 1 output spikes for that run. This can thne
% be plotted.
%
% first column of output now is for no context file at all. (LSS 8 March
% 2019)
%
% set number of driving files
driveno = 10 ;%10 ;
% set number of context files
contextno = 10 ; %10 ;
% set drive file name prefix
drivefileprefix = 'drive__' ;
% set context name prefix
contextfileprefix = 'context__' ;
% set drive file weight name
dwfile = 'drivingweights.txt' ;
% set context file weight name
cwfile = 'contextweights.txt' ;
% set outputfile prefix
outfileprefix = 'nspikesmar142019_' ;
%
% runing simulated time
runtime = 5 ;
% defaults for logistic functions.
logisticGradientBasal = 1.0 ;
logisticGradientTuft = 1.0 ;
logisticInterceptBasal = 0 ;
logisticInterceptTuft = 0 ;

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
        case 'logisticinterceptbasal'
            logisticInterceptBasal = varargin{i+1};
            i=i+1 ;
        case 'logisticintercepttuft'
            logisticInterceptTuft = varargin{i+1};
            i=i+1 ;
        case 't' % running time
            runtime = varargin{i+1};
            i=i+1 ;
        otherwise
            error('runMultiple: Unknown argument %s given',varargin{i});
    end
    i=i+1 ;
end

for dd = 1:10
    dfname = [drivefileprefix num2str(dd) '.csv'] ;
    for cc = 0:10
        if (cc == 0) 
            cfname = '' ; % no context at all
        else
            cfname = [contextfileprefix num2str(cc) '.csv'] ;
        end
        outfilename = [outfileprefix num2str(dd) '_' num2str(cc) '.csv'] ;
        % and run it 
        RunSpikeSimulator('fileprefix', fileprefix, 'c', ...
            cfname,'t', runtime , 'd', dfname, 'v', 0, 'debug', 0, ...
            'wc', cwfile, 'wd', dwfile, ...
        't_basal', 0.05, 't_apicaltuft', 0.05, 'alpha_context', 400, 'alpha_driver', 400, 'apical_multiplier', 1.0, 'wi', '', ...
         'snumbersout', outfilename, 'transferfunction', tf, 'p_refractory_period', 0.002, ...
     'logisticGradientBasal',   logisticGradientBasal,  'logisticGradientTuft', logisticGradientTuft, ... 
     'logisticInterceptBasal', logisticInterceptBasal, 'logisticInterceptTuft', logisticInterceptTuft ...
     ) ;
    end
end

% now read the snumbersout files and create a 2D array from them
% may then delete these files (otherwise there's an awful lot of files
% generated)
spikesOutArray = zeros([driveno contextno + 1]) ;
for dd = 1:driveno
    dfnostring = num2str(dd) ;
    for cc = 1:contextno + 1
        filedata = csvread([fileprefix outfileprefix dfnostring '_' num2str(cc - 1) '.csv']) ;
        spikesOutArray(dd, cc) = filedata(1, 2) ;
    end
end
% delete the output spike number files
system(['rm ' fileprefix outfileprefix '*']) ;


        
end

