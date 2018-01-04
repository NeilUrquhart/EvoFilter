package filter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.servlet.ServletContext;

public class Reader {

	static double[][] normalise(ArrayList<String> lines) {
		//Normalise everything!
		int rows = lines.size();
		int cols = lines.get(0).split(",").length;
	
		double[][] raw = new double[rows][cols];
	
		for (int x=0; x < lines.size(); x++){
			raw[x] = new double[cols];
			String[] data = lines.get(x).split(",");
			for(int y=0; y < data.length;y++){
				raw[x][y] = Double.parseDouble(data[y]);
			}
		}
	
		for (int x=0; x < cols; x++){
			Reader.normaliseRow(raw,x);
		}
		return raw;
	}

	static ArrayList<String> readFile(String fName, ServletContext context) {
		String line;
		ArrayList<String> lines = new ArrayList<String>();
		try {
	
			/*InputStream is = context.getResourceAsStream(filename);
			if (is != null) {
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader reader = new BufferedReader(isr);
				PrintWriter writer = response.getWriter();
				String text = "";
				
				//
				// We read the file line by line and later will be displayed on the 
				// browser page.
				//
				text = reader.readLine();//skip headers
				text = reader.readLine();//skip headers
				boolean first = true;
				while ((text = reader.readLine()) != null) {
					
					String[] data = text.split(",");*/
			
			InputStream fis;
			if (context != null)
			  fis = context.getResourceAsStream(fName);
			else
				fis = new FileInputStream(fName);
			
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
	
	
			//skip headers
			System.out.println(br.readLine());
			//System.out.println(br.readLine());
	
	
			while ((line = br.readLine()) != null) {
				if (lines.indexOf(line)>-1){
					System.out.println("dup");
				}
				else{
					lines.add(line);
					// Deal with the line
				}
			}
		}catch(Exception e){
			System.out.println(e.toString());
		}
		return lines;
	}

	static void normaliseRow(double[][] raw, int col) {
		// TODO Auto-generated method stub
		
		double max = 0;
		double min = Double.MAX_VALUE;
		for (int x=0; x < raw.length; x++){
			if (raw[x][col]< min)
				min = raw[x][col];
			
			if (raw[x][col]> max)
				max = raw[x][col];
		}
		
		for (int x=0; x < raw.length; x++){
			raw[x][col] = raw[x][col]-min; 
		}
		
		double diff = max - min;
		for (int x=0; x < raw.length; x++){
			raw[x][col] = raw[x][col]/diff ; 
		}
		
	}
	
}
