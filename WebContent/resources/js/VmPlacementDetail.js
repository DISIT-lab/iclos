/**
 * 
 */
jQuery(document).ready(function() {
  // Handler for .ready() called.	
	$("[type='checkbox']").bootstrapSwitch({
		size:'large',
		onColor: 'info',
		offColor: 'default',
		onText: 'Show detailed workload',
		offText: 'Show overall workload',
			});
	
	$("[type='checkbox']").on('switchChange.bootstrapSwitch', function(event, state) {
		  //$(this).trigger('click');
	});	
});