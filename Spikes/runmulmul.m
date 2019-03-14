for gradient = 1:1:3 ;
    for intercept = 0:0.5:1
        s_array = runMultiple(fileprefix, 1, 'logisticGradientBasal', gradient, 'logisticGradientTuft' , gradient, ...\
            'logisticInterceptBasal', intercept, 'logisticInterceptTuft', intercept) ;
        figure ; surf(s_array) ; title(['g = ', num2str(gradient), ' i = ', num2str(intercept)]) ;
        savefig([fileprefix 'mar14_s2_' num2str(gradient) '_' num2str(intercept) '.fig']) ;
        close ;
    end
end