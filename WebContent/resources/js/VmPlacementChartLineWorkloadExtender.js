/**
 * 
 */

function VmPlacementChartStackedWorkloadExtender(){	
	this.cfg.legend= {
			renderer: $.jqplot.EnhancedLegendRenderer,
	        show: true,
	        location: 'e',
	        placement: 'insideGrid'
	    };
	
	this.cfg.axes.xaxis = {
			tickInterval: 20	           
	};
	
	this.cfg.axes.xaxis.tickOptions = {
	            mark: 'outside',    // Where to put the tick mark on the axis
	                                // 'outside', 'inside' or 'cross'
	            showMark: true,     // whether or not to show the mark on the axis
	            showGridline: true, // whether to draw a gridline (across the whole grid) at this tick
	            isMinorTick: false, // whether this is a minor tick
	            markSize: 4,        // length the tick will extend beyond the grid in pixels.  For
	                                // 'cross', length will be added above and below the grid boundary
	            show: true,         // whether to show the tick (mark and label)
	            showLabel: false,    // whether to show the text label at the tick
	            prefix: '',         // String to prepend to the tick label.
	                                // Prefix is prepended to the formatted tick label
	            suffix: '',         // String to append to the tick label.
	                                // Suffix is appended to the formatted tick label
	            formatString: '%d',   // format string to use with the axis tick formatter
	            fontFamily: '',     // css spec for the font-size css attribute
	            fontSize: '',       // css spec for the font-size css attribute
	            textColor: '',      // css spec for the color attribute
	            escapeHTML: false   // true to escape HTML entities in the label
	};	          
}