package filter;

import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletContext;

public class RunFilter {
	private static int STAFF_COST=0;
	private static int TRAVEL_COST=1;
	private static int CO2=2;
	private static int TOTAL_COST=3;
	private static int CAR_USE=4;
	private static int STAFF=5;
	
	private static  int cols=0;
	
	private static double[][] applyFilter(double[][] applyTo , boolean[] filter){
		double[][] filtered;
		
		//get len
		int len=0;
		for(int c=0; c < filter.length;c++)
			if(!filter[c])len++;
		
		filtered = new double[len][cols];
		int fCount=0;
		for(int c=0; c < filter.length;c++){
			if(!filter[c]){
				filtered[fCount] = new double[cols];
				for (int x=0; x < cols;x++)
					filtered[fCount][x] = applyTo[c][x];
				fCount++;
			}
		}
		
		return filtered;
	}
	

	private static String getCriterion(String[] filterCriterion, String criterion){
		for (int x=0; x < filterCriterion.length-1;x++  ){
			if (filterCriterion[x].equals(criterion)){
				return filterCriterion[x+1];
			}
		}
		return null;
	}
	
	private static boolean hasCriterion(String[] filterCriterion, String criterion){
		for (int x=0; x < filterCriterion.length-1;x++  ){
			if (filterCriterion[x].equals(criterion)){
				return true;
			}
		}
		return false;
	}
	
	public static String evoFilter(String fName, ServletContext context, PrintWriter out, boolean evolve){
		String criterion = "";
		ArrayList<String> lines = Reader.readFile(fName,context);
		double[][] raw = Reader.normalise(lines);
		System.out.print("No Filter count = " + raw.length);

		cols = raw[0].length;
		
		//First apply the simple filter
		EvoFilter fil  = new SimpleFilter();
		SimpleFilter sFil = (SimpleFilter) fil;
		boolean[] filter = sFil.evoFilter(raw,3);
		
		int plainFilterCount =0;
		for (boolean f : filter)
			if (!f)
				plainFilterCount++;
		
		System.out.print("Plain Filter count = " + plainFilterCount);

		int evoFilterCount = 0;
		
		if (evolve){
			fil = new EvoFilter();
			filter = fil.evoFilter(raw,3);
			
			for (boolean f : filter)
				if (!f)
					evoFilterCount++;
			
			System.out.print("Evo Filter count = " + evoFilterCount);
	
		}

		double[][] finished = applyFilter(raw,filter);
			
			
		writeFront(finished, out);
		return criterion;
	}

	private static void writeFront(double[][] finished, PrintWriter out) {
		out.println("var data = [");

			if (finished != null){
				for(int x=0; x < finished.length; x++){
					out.print("[");
					for (int y=0; y < finished[x].length;y++){
						out.print((finished[x][y]));
						if (y < finished[x].length-1)
							out.print(",");
					}

					out.print("],");
					
				}
				//out.println("];");
			}
			//add dummies top and bottom for PACO
			out.print("[");
			for (int y=0; y < cols;y++){
				out.print("0");
				if (y < cols-1)
					out.print(",");
			}
			out.println("],");
			out.print("[");
			for (int y=0; y < cols;y++){
				out.print("1");
				if (y < cols-1)
					out.print(",");
			}
			out.println("]");
			out.println("];");
	}



}
