function  [spikesOutArray] = runMultiple(fileprefix, tf)
%runMultiple Runs RunSpikeSimulator multiple times using different spike
%rates. 
%   Uased to run RunSpikeSimulator for a single set of parameters.
%  For proper sciuentific usage, some of the fixed values need to
%  be normal or varargin parameters so that a parameter search can be
%  accompolished
% returnsa an array, driveno by contextno, of integers where wach integer
% shows the number of neuron 1 output spikes for that run. This can thne
% be plotted.
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
outfileprefix = 'nspikesmar042019_' ;
%
for dd = 1:driveno
    dfname = [drivefileprefix num2str(dd) '.csv'] ;
    for cc = 1:contextno
        cfname = [contextfileprefix num2str(cc) '.csv'] ;
        outfilename = [outfileprefix num2str(dd) '_' num2str(cc) '.csv'] ;
        % and run it 
        RunSpikeSimulator('fileprefix', fileprefix, 'c', ...
            cfname, 'd', dfname, 'v', 0, 'debug', 0, ...
            'wc', cwfile, 'wd', dwfile, ...
        't_basal', 0.2, 'apical_multiplier', 2.0, 'wi', '', ...
         'snumbersout', outfilename, 'transferfunction', tf, 'p_refractory_period', 0.002) ;
    end
end

% now read the snumbersout files and create a 2D array from them
% may then delete these files (otherwise there's an awful lot of files
% generated)
spikesOutArray = zeros([driveno contextno]) ;
for dd = 1:driveno
    dfnostring = num2str(dd) ;
    for cc = 1:contextno
        filedata = csvread([fileprefix outfileprefix dfnostring '_' num2str(cc) '.csv']) ;
        spikesOutArray(dd, cc) = filedata(1, 2) ;
    end
end
% delete the output spike number files
system(['rm ' fileprefix outfileprefix '*']) ;


        
end

