package filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SimpleFilter extends EvoFilter{

	

	
	public boolean[] evoFilter(double[][] raw, int xx) {
		int len = raw.length;
		int dims = raw[0].length; 
		boolean[] filter = new boolean[len];

		for (int i =0; i < len; i++){
			filter[i] = false;
			
			for (int d = 0; d < dims; d++ ){
				if (targetRange[d][0] > -1){
					if (raw[i][d] < targetRange[d][0])
						filter[i] = true;

					if (raw[i][d] > targetRange[d][1])
						filter[i] = true;
				}
				
				//						minDiff[dim] //%
			}
		}
		
		return filter;
		
	}

}
