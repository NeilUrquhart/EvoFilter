package filter;


import java.util.ArrayList;

public class BigTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		resetCriterion();
		//Generate a random filter!  
		
		EvoFilter.targetRange[Filter.STAFF_COST][EvoFilter.minR]=-1;
		EvoFilter.targetRange[Filter.STAFF_COST][EvoFilter.maxR]=-1;
		EvoFilter.targetRange[Filter.TRAVEL_COST][EvoFilter.minR]=-1;
		EvoFilter.targetRange[Filter.TRAVEL_COST][EvoFilter.maxR]=-1;
		EvoFilter.targetRange[Filter.CO2][EvoFilter.minR]=-1;
		EvoFilter.targetRange[Filter.CO2][EvoFilter.maxR]=-1;
		EvoFilter.targetRange[Filter.TOTAL_COST][EvoFilter.minR]=-1;
		EvoFilter.targetRange[Filter.TOTAL_COST][EvoFilter.maxR]=-1;
		EvoFilter.targetRange[Filter.CAR_USE][EvoFilter.minR]=-1;
		EvoFilter.targetRange[Filter.CAR_USE][EvoFilter.maxR]=-1;
		EvoFilter.targetRange[Filter.STAFF][EvoFilter.minR]=-1;
		EvoFilter.targetRange[Filter.STAFF][EvoFilter.maxR]=-1;

		runTest("h-blon-1--useCO2_CarPC.res.csv");

		

	}



	private static void resetCriterion() {
		for(int x=0; x < EvoFilter.targetRange.length;x++){			
			EvoFilter.targetRange[x][EvoFilter.minR]=-1;
			EvoFilter.targetRange[x][EvoFilter.maxR]=-1;
			EvoFilter.minDiff[x] = 0;
			//-1 = don't care
		}
	}

	
	
	private static void runTest(String fName) {
		System.out.print(fName+",");
		fName = "C:\\TomcatTest\\ParetoFilter\\WebContent\\" + fName;
		
		ArrayList<String> lines = Reader.readFile(fName, null);
		double[][] raw = Reader.normalise(lines);
		System.out.print("No Filter, " + raw.length);

		int cols = raw[0].length;
		
		//First apply the simple filter
		EvoFilter fil  = new SimpleFilter();
		SimpleFilter sFil = (SimpleFilter) fil;
		boolean[] filter = sFil.evoFilter(raw,3);
		
		int plainFilterCount =0;
		for (boolean f : filter)
			if (!f)
				plainFilterCount++;
		
		System.out.print(",Plain Filter count ," + plainFilterCount);

		int evoFilterCount = 0;
		
			fil = new EvoFilter();
			filter = fil.evoFilter(raw,3);
			
			for (boolean f : filter)
				if (!f)
					evoFilterCount++;
			
			System.out.println(",Evo Filter count , " + evoFilterCount);
	}

}
