function  runMultiple(fileprefix, tf)
%runMultiple Summary of this function goes here
%   Detailed explanation goes here
% set number of driving files
driveno = 10 ;
% set number of context files
contextno = 10 ;
% set drive file name prefix
drivefileprefix = 'drive__' ;
% set context name prefix
contextfileprefix = 'context__' ;
% set drive file weight name
dwfile = 'drivingweights.txt' ;
% set context file weight name
cwfile = 'contextweights.txt' ;
% set outputfile prefix
outfileprefix = 'textmar042019_' ;
%
for dd = 1:driveno
    dfname = [drivefileprefix num2str(dd) '.csv'] ;
    for cc = 1:contextno
        cfname = [contextfileprefix num2str(cc) '.csv'] ;
        outfilename = [outfileprefix num2str(dd) '_' num2str(cc) '.csv'] ;
        % and run it 
        RunSpikeSimulator('fileprefix', fileprefix, 'c', ...
            cfname, 'd', dfname, 'v', 1, 'debug', 0, ...
            'wd', cwfile, 'wd', dwfile, ...
        't_basal', 0.2, 'apical_multiplier', 2.0, 'wi', 'internalweights.txt', ...
         'sout', outfilename, 'transferfunction', tf) ;
    end
end
        
        
end

