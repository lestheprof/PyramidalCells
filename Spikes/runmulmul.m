for gradient = 2 ;
    for intercept = 1.0:0.5:2.5
        s_array = runMultiple(fileprefix, 1, 'logisticGradientBasal', gradient, 'logisticGradientTuft' , gradient, ...\
            'logisticInterceptBasal', intercept, 'logisticInterceptTuft', intercept) ;
        figure ; surf(s_array) ; title(['g = ', num2str(gradient), ' i = ', num2str(intercept) 'w-drive = 2.0 w-context = 5']) ;
        xlabel('context') ;
        ylabel('drive') ;
        zlabel('spikes') ;
        savefig([fileprefix 'mar15_s2_' num2str(gradient) '_' num2str(intercept) '.fig']) ;
        close ;
    end
end