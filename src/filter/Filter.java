package filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Filter
 */
@WebServlet("/Filter")
public class Filter extends HttpServlet {
	
	public static int STAFF_COST=0;
	public static int TRAVEL_COST=1;
	public static int CO2=2;
	public static int TOTAL_COST=3;
	public static int CAR_USE=4;
	public static int STAFF=5;
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Filter() {
        super();
        // TODO Auto-generated constructor stub
    }

 
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    public void doGet(HttpServletRequest request, 		HttpServletResponse response) 
		throws ServletException, IOException {
    	
    	PrintWriter out = response.getWriter();
    	//Get parameters
    	//Header
		writeHeader(out);
    	
    	String fName = request.getParameter("file");
    	if (fName != null){
    		
    		out.println("<h2>Key: 0 = Staff Cost, 1 = Travel Cost, 2 = CO2, 3 = Total Cost, 4 = Car Use (%), 5 = Staff </h2>");
    		
    		out.println("<div id='example' class='parcoords' style='width:1200px;height:600px'></div>");
    		
    		out.println("<script>");
    		ServletContext context = getServletContext();
    		if (setFilterCriterion(request)){
    			RunFilter.evoFilter(fName, context,out,true);
    		}else{
    			loadFile(fName, context, response, out);

    		}
  
    		
    		out.println(" var pc = d3.parcoords(/*{nullValueSeparator: \"top\"}*/)('#example')");
    		out.println(" .data(data)");
    		out.println(" .render()");
    		out.println(" .createAxes();");
    		out.println(" </script>");
    	}
    	writeForm(out,fName);
    	writeFooter(out);
	}


    private String getOptionRange(int dimension){
    	if (EvoFilter.targetRange[dimension][1] ==0.2){
    		return "VLOW";
    	}
    	
    	if (EvoFilter.targetRange[dimension][1] ==0.4){
    		return "LOW";
    	}
    	if (EvoFilter.targetRange[dimension][0] ==0.4){
    		return "MEDIUM";
    	}
    	if (EvoFilter.targetRange[dimension][0] ==0.6){
    		return "HIGH";
    	}
    	if (EvoFilter.targetRange[dimension][0] ==0.8){
    		return "VHIGH";
    	}
    	return "";
    }
    
    private String getOptionSig(int dimension){
    	if (EvoFilter.minDiff[dimension] ==0.01){
    		return "1";
    	}
    	if (EvoFilter.minDiff[dimension]==0.05){
    		return "5";
    	}
    	if (EvoFilter.minDiff[dimension] ==0.1){
    		return "10";
    	}
    	if (EvoFilter.minDiff[dimension] ==0.15){
    		return "15";
    	}
    	if (EvoFilter.minDiff[dimension] ==0.2){
    		return "20";
    	}
    	return "";
    }
    private void writeForm(PrintWriter out, String fileName) {
    	out.println("<form name='loginForm' method='get' action='Filter'> "
    			+ "<h3>Results file : "

    			+ "<select name='file'>"
    			+ "<datalist id='file'>"
    			+ "<option value='/h-blon-1--useCO2_CarPC.res.csv'>	/h-blon-1--useCO2_CarPC.res.csv</option>"
    			+ "<option value='/h-blon-1--useCO2_StaffCost.res.csv'>	/h-blon-1--useCO2_StaffCost.res.csv</option>"
    			+ "<option value='/h-blon-1--useCO2_TotalCost.res.csv'>	/h-blon-1--useCO2_TotalCost.res.csv</option>"
    			+ "<option value='/h-blon-1--useCO2_TotalCost_CarPC.res.csv'>	/h-blon-1--useCO2_TotalCost_CarPC.res.csv</option>"
    			+ "<option value='/h-blon-1--useCO2_TravelCost.res.csv'>	/h-blon-1--useCO2_TravelCost.res.csv</option>"
    			+ "<option value='/h-blon-1--useTravelCost_StaffCost.res.csv'>	/h-blon-1--useTravelCost_StaffCost.res.csv</option>"
    			+ "<option value='/h-blon-2--useCO2_CarPC.res.csv'>	/h-blon-2--useCO2_CarPC.res.csv</option>"
    			+ "<option value='/h-blon-2--useCO2_StaffCost.res.csv'>	/h-blon-2--useCO2_StaffCost.res.csv</option>"
    			+ "<option value='/h-blon-2--useCO2_TotalCost.res.csv'>	/h-blon-2--useCO2_TotalCost.res.csv</option>"
    			+ "<option value='/h-blon-2--useCO2_TotalCost_CarPC.res.csv'>	/h-blon-2--useCO2_TotalCost_CarPC.res.csv</option>"
    			+ "<option value='/h-blon-2--useCO2_TravelCost.res.csv'>	/h-blon-2--useCO2_TravelCost.res.csv</option>"
    			+ "<option value='/h-blon-2--useTravelCost_StaffCost.res.csv'>	/h-blon-2--useTravelCost_StaffCost.res.csv</option>"
    			+ "<option value='/h-blon-4--useCO2_CarPC.res.csv'>	/h-blon-4--useCO2_CarPC.res.csv</option>"
    			+ "<option value='/h-blon-4--useCO2_StaffCost.res.csv'>	/h-blon-4--useCO2_StaffCost.res.csv</option>"
    			+ "<option value='/h-blon-4--useCO2_TotalCost.res.csv'>	/h-blon-4--useCO2_TotalCost.res.csv</option>"
    			+ "<option value='/h-blon-4--useCO2_TotalCost_CarPC.res.csv'>	/h-blon-4--useCO2_TotalCost_CarPC.res.csv</option>"
    			+ "<option value='/h-blon-4--useCO2_TravelCost.res.csv'>	/h-blon-4--useCO2_TravelCost.res.csv</option>"
    			+ "<option value='/h-blon-4--useTravelCost_StaffCost.res.csv'>	/h-blon-4--useTravelCost_StaffCost.res.csv</option>"
    			+ "<option value='/h-blon-8--useCO2_CarPC.res.csv'>	/h-blon-8--useCO2_CarPC.res.csv</option>"
    			+ "<option value='/h-blon-8--useCO2_StaffCost.res.csv'>	/h-blon-8--useCO2_StaffCost.res.csv</option>"
    			+ "<option value='/h-blon-8--useCO2_TotalCost.res.csv'>	/h-blon-8--useCO2_TotalCost.res.csv</option>"
    			+ "<option value='/h-blon-8--useCO2_TotalCost_CarPC.res.csv'>	/h-blon-8--useCO2_TotalCost_CarPC.res.csv</option>"
    			+ "<option value='/h-blon-8--useCO2_TravelCost.res.csv'>	/h-blon-8--useCO2_TravelCost.res.csv</option>"
    			+ "<option value='/h-blon-8--useTravelCost_StaffCost.res.csv'>	/h-blon-8--useTravelCost_StaffCost.res.csv</option>"
    			+ "<option value='/h-blon-r00--useCO2_CarPC.res.csv'>	/h-blon-r00--useCO2_CarPC.res.csv</option>"
    			+ "<option value='/h-blon-r00--useCO2_StaffCost.res.csv'>	/h-blon-r00--useCO2_StaffCost.res.csv</option>"
    			+ "<option value='/h-blon-r00--useCO2_TotalCost.res.csv'>	/h-blon-r00--useCO2_TotalCost.res.csv</option>"
    			+ "<option value='/h-blon-r00--useCO2_TotalCost_CarPC.res.csv'>	/h-blon-r00--useCO2_TotalCost_CarPC.res.csv</option>"
    			+ "<option value='/h-blon-r00--useCO2_TravelCost.res.csv'>	/h-blon-r00--useCO2_TravelCost.res.csv</option>"
    			+ "<option value='/h-blon-r00--useTravelCost_StaffCost.res.csv'>	/h-blon-r00--useTravelCost_StaffCost.res.csv</option>"
    			+ "<option value='/h-blon-rnd--useCO2_CarPC.res.csv'>	/h-blon-rnd--useCO2_CarPC.res.csv</option>"
    			+ "<option value='/h-blon-rnd--useCO2_StaffCost.res.csv'>	/h-blon-rnd--useCO2_StaffCost.res.csv</option>"
    			+ "<option value='/h-blon-rnd--useCO2_TotalCost.res.csv'>	/h-blon-rnd--useCO2_TotalCost.res.csv</option>"
    			+ "<option value='/h-blon-rnd--useCO2_TotalCost_CarPC.res.csv'>	/h-blon-rnd--useCO2_TotalCost_CarPC.res.csv</option>"
    			+ "<option value='/h-blon-rnd--useCO2_TravelCost.res.csv'>	/h-blon-rnd--useCO2_TravelCost.res.csv</option>"
    			+ "<option value='/h-blon-rnd--useTravelCost_StaffCost.res.csv'>	/h-blon-rnd--useTravelCost_StaffCost.res.csv</option>"


    			+ "<option value='/h-cluster-1--useCO2_CarPC.res.csv'>	/h-cluster-1--useCO2_CarPC.res.csv</option>"
    			+ "<option value='/h-cluster-1--useCO2_StaffCost.res.csv'>	/h-cluster-1--useCO2_StaffCost.res.csv</option>"
    			+ "<option value='/h-cluster-1--useCO2_TotalCost.res.csv'>	/h-cluster-1--useCO2_TotalCost.res.csv</option>"
    			+ "<option value='/h-cluster-1--useCO2_TotalCost_CarPC.res.csv'>	/h-cluster-1--useCO2_TotalCost_CarPC.res.csv</option>"
    			+ "<option value='/h-cluster-1--useCO2_TravelCost.res.csv'>	/h-cluster-1--useCO2_TravelCost.res.csv</option>"
    			+ "<option value='/h-cluster-1--useTravelCost_StaffCost.res.csv'>	/h-cluster-1--useTravelCost_StaffCost.res.csv</option>"
    			+ "<option value='/h-cluster-2--useCO2_CarPC.res.csv'>	/h-cluster-2--useCO2_CarPC.res.csv</option>"
    			+ "<option value='/h-cluster-2--useCO2_StaffCost.res.csv'>	/h-cluster-2--useCO2_StaffCost.res.csv</option>"
    			+ "<option value='/h-cluster-2--useCO2_TotalCost.res.csv'>	/h-cluster-2--useCO2_TotalCost.res.csv</option>"
    			+ "<option value='/h-cluster-2--useCO2_TotalCost_CarPC.res.csv'>	/h-cluster-2--useCO2_TotalCost_CarPC.res.csv</option>"
    			+ "<option value='/h-cluster-2--useCO2_TravelCost.res.csv'>	/h-cluster-2--useCO2_TravelCost.res.csv</option>"
    			+ "<option value='/h-cluster-2--useTravelCost_StaffCost.res.csv'>	/h-cluster-2--useTravelCost_StaffCost.res.csv</option>"
    			+ "<option value='/h-cluster-4--useCO2_CarPC.res.csv'>	/h-cluster-4--useCO2_CarPC.res.csv</option>"
    			+ "<option value='/h-cluster-4--useCO2_StaffCost.res.csv'>	/h-cluster-4--useCO2_StaffCost.res.csv</option>"
    			+ "<option value='/h-cluster-4--useCO2_TotalCost.res.csv'>	/h-cluster-4--useCO2_TotalCost.res.csv</option>"
    			+ "<option value='/h-cluster-4--useCO2_TotalCost_CarPC.res.csv'>	/h-cluster-4--useCO2_TotalCost_CarPC.res.csv</option>"
    			+ "<option value='/h-cluster-4--useCO2_TravelCost.res.csv'>	/h-cluster-4--useCO2_TravelCost.res.csv</option>"
    			+ "<option value='/h-cluster-4--useTravelCost_StaffCost.res.csv'>	/h-cluster-4--useTravelCost_StaffCost.res.csv</option>"
    			+ "<option value='/h-cluster-8--useCO2_CarPC.res.csv'>	/h-cluster-8--useCO2_CarPC.res.csv</option>"
    			+ "<option value='/h-cluster-8--useCO2_StaffCost.res.csv'>	/h-cluster-8--useCO2_StaffCost.res.csv</option>"
    			+ "<option value='/h-cluster-8--useCO2_TotalCost.res.csv'>	/h-cluster-8--useCO2_TotalCost.res.csv</option>"
    			+ "<option value='/h-cluster-8--useCO2_TotalCost_CarPC.res.csv'>	/h-cluster-8--useCO2_TotalCost_CarPC.res.csv</option>"
    			+ "<option value='/h-cluster-8--useCO2_TravelCost.res.csv'>	/h-cluster-8--useCO2_TravelCost.res.csv</option>"
    			+ "<option value='/h-cluster-8--useTravelCost_StaffCost.res.csv'>	/h-cluster-8--useTravelCost_StaffCost.res.csv</option>"
    			+ "<option value='/h-cluster-r00--useCO2_CarPC.res.csv'>	/h-cluster-r00--useCO2_CarPC.res.csv</option>"
    			+ "<option value='/h-cluster-r00--useCO2_StaffCost.res.csv'>	/h-cluster-r00--useCO2_StaffCost.res.csv</option>"
    			+ "<option value='/h-cluster-r00--useCO2_TotalCost.res.csv'>	/h-cluster-r00--useCO2_TotalCost.res.csv</option>"
    			+ "<option value='/h-cluster-r00--useCO2_TotalCost_CarPC.res.csv'>	/h-cluster-r00--useCO2_TotalCost_CarPC.res.csv</option>"
    			+ "<option value='/h-cluster-r00--useCO2_TravelCost.res.csv'>	/h-cluster-r00--useCO2_TravelCost.res.csv</option>"
    			+ "<option value='/h-cluster-r00--useTravelCost_StaffCost.res.csv'>	/h-cluster-r00--useTravelCost_StaffCost.res.csv</option>"
    			+ "<option value='/h-cluster-rnd--useCO2_CarPC.res.csv'>	/h-cluster-rnd--useCO2_CarPC.res.csv</option>"
    			+ "<option value='/h-cluster-rnd--useCO2_StaffCost.res.csv'>	/h-cluster-rnd--useCO2_StaffCost.res.csv</option>"
    			+ "<option value='/h-cluster-rnd--useCO2_TotalCost.res.csv'>	/h-cluster-rnd--useCO2_TotalCost.res.csv</option>"
    			+ "<option value='/h-cluster-rnd--useCO2_TotalCost_CarPC.res.csv'>	/h-cluster-rnd--useCO2_TotalCost_CarPC.res.csv</option>"
    			+ "<option value='/h-cluster-rnd--useCO2_TravelCost.res.csv'>	/h-cluster-rnd--useCO2_TravelCost.res.csv</option>"
    			+ "<option value='/h-cluster-rnd--useTravelCost_StaffCost.res.csv'>	/h-cluster-rnd--useTravelCost_StaffCost.res.csv</option>"


    			+ "<option value='/h-lon-1--useCO2_CarPC.res.csv'>	/h-lon-1--useCO2_CarPC.res.csv</option>"
    			+ "<option value='/h-lon-1--useCO2_StaffCost.res.csv'>	/h-lon-1--useCO2_StaffCost.res.csv</option>"
    			+ "<option value='/h-lon-1--useCO2_TotalCost.res.csv'>	/h-lon-1--useCO2_TotalCost.res.csv</option>"
    			+ "<option value='/h-lon-1--useCO2_TotalCost_CarPC.res.csv'>	/h-lon-1--useCO2_TotalCost_CarPC.res.csv</option>"
    			+ "<option value='/h-lon-1--useCO2_TravelCost.res.csv'>	/h-lon-1--useCO2_TravelCost.res.csv</option>"
    			+ "<option value='/h-lon-1--useTravelCost_StaffCost.res.csv'>	/h-lon-1--useTravelCost_StaffCost.res.csv</option>"
    			+ "<option value='/h-lon-2--useCO2_CarPC.res.csv'>	/h-lon-2--useCO2_CarPC.res.csv</option>"
    			+ "<option value='/h-lon-2--useCO2_StaffCost.res.csv'>	/h-lon-2--useCO2_StaffCost.res.csv</option>"
    			+ "<option value='/h-lon-2--useCO2_TotalCost.res.csv'>	/h-lon-2--useCO2_TotalCost.res.csv</option>"
    			+ "<option value='/h-lon-2--useCO2_TotalCost_CarPC.res.csv'>	/h-lon-2--useCO2_TotalCost_CarPC.res.csv</option>"
    			+ "<option value='/h-lon-2--useCO2_TravelCost.res.csv'>	/h-lon-2--useCO2_TravelCost.res.csv</option>"
    			+ "<option value='/h-lon-2--useTravelCost_StaffCost.res.csv'>	/h-lon-2--useTravelCost_StaffCost.res.csv</option>"
    			+ "<option value='/h-lon-4--useCO2_CarPC.res.csv'>	/h-lon-4--useCO2_CarPC.res.csv</option>"
    			+ "<option value='/h-lon-4--useCO2_StaffCost.res.csv'>	/h-lon-4--useCO2_StaffCost.res.csv</option>"
    			+ "<option value='/h-lon-4--useCO2_TotalCost.res.csv'>	/h-lon-4--useCO2_TotalCost.res.csv</option>"
    			+ "<option value='/h-lon-4--useCO2_TotalCost_CarPC.res.csv'>	/h-lon-4--useCO2_TotalCost_CarPC.res.csv</option>"
    			+ "<option value='/h-lon-4--useCO2_TravelCost.res.csv'>	/h-lon-4--useCO2_TravelCost.res.csv</option>"
    			+ "<option value='/h-lon-4--useTravelCost_StaffCost.res.csv'>	/h-lon-4--useTravelCost_StaffCost.res.csv</option>"
    			+ "<option value='/h-lon-8--useCO2_CarPC.res.csv'>	/h-lon-8--useCO2_CarPC.res.csv</option>"
    			+ "<option value='/h-lon-8--useCO2_StaffCost.res.csv'>	/h-lon-8--useCO2_StaffCost.res.csv</option>"
    			+ "<option value='/h-lon-8--useCO2_TotalCost.res.csv'>	/h-lon-8--useCO2_TotalCost.res.csv</option>"
    			+ "<option value='/h-lon-8--useCO2_TotalCost_CarPC.res.csv'>	/h-lon-8--useCO2_TotalCost_CarPC.res.csv</option>"
    			+ "<option value='/h-lon-8--useCO2_TravelCost.res.csv'>	/h-lon-8--useCO2_TravelCost.res.csv</option>"
    			+ "<option value='/h-lon-8--useTravelCost_StaffCost.res.csv'>	/h-lon-8--useTravelCost_StaffCost.res.csv</option>"
    			+ "<option value='/h-lon-r00--useCO2_CarPC.res.csv'>	/h-lon-r00--useCO2_CarPC.res.csv</option>"
    			+ "<option value='/h-lon-r00--useCO2_StaffCost.res.csv'>	/h-lon-r00--useCO2_StaffCost.res.csv</option>"
    			+ "<option value='/h-lon-r00--useCO2_TotalCost.res.csv'>	/h-lon-r00--useCO2_TotalCost.res.csv</option>"
    			+ "<option value='/h-lon-r00--useCO2_TotalCost_CarPC.res.csv'>	/h-lon-r00--useCO2_TotalCost_CarPC.res.csv</option>"
    			+ "<option value='/h-lon-r00--useCO2_TravelCost.res.csv'>	/h-lon-r00--useCO2_TravelCost.res.csv</option>"
    			+ "<option value='/h-lon-r00--useTravelCost_StaffCost.res.csv'>	/h-lon-r00--useTravelCost_StaffCost.res.csv</option>"
    			+ "<option value='/h-lon-rnd--useCO2_CarPC.res.csv'>	/h-lon-rnd--useCO2_CarPC.res.csv</option>"
    			+ "<option value='/h-lon-rnd--useCO2_StaffCost.res.csv'>	/h-lon-rnd--useCO2_StaffCost.res.csv</option>"
    			+ "<option value='/h-lon-rnd--useCO2_TotalCost.res.csv'>	/h-lon-rnd--useCO2_TotalCost.res.csv</option>"
    			+ "<option value='/h-lon-rnd--useCO2_TotalCost_CarPC.res.csv'>	/h-lon-rnd--useCO2_TotalCost_CarPC.res.csv</option>"
    			+ "<option value='/h-lon-rnd--useCO2_TravelCost.res.csv'>	/h-lon-rnd--useCO2_TravelCost.res.csv</option>"
    			+ "<option value='/h-lon-rnd--useTravelCost_StaffCost.res.csv'>	/h-lon-rnd--useTravelCost_StaffCost.res.csv</option>"


    			+ "<option value='/h-offset-1--useCO2_CarPC.res.csv'>	/h-offset-1--useCO2_CarPC.res.csv</option>"
    			+ "<option value='/h-offset-1--useCO2_StaffCost.res.csv'>	/h-offset-1--useCO2_StaffCost.res.csv</option>"
    			+ "<option value='/h-offset-1--useCO2_TotalCost.res.csv'>	/h-offset-1--useCO2_TotalCost.res.csv</option>"
    			+ "<option value='/h-offset-1--useCO2_TotalCost_CarPC.res.csv'>	/h-offset-1--useCO2_TotalCost_CarPC.res.csv</option>"
    			+ "<option value='/h-offset-1--useCO2_TravelCost.res.csv'>	/h-offset-1--useCO2_TravelCost.res.csv</option>"
    			+ "<option value='/h-offset-1--useTravelCost_StaffCost.res.csv'>	/h-offset-1--useTravelCost_StaffCost.res.csv</option>"
    			+ "<option value='/h-offset-2--useCO2_CarPC.res.csv'>	/h-offset-2--useCO2_CarPC.res.csv</option>"
    			+ "<option value='/h-offset-2--useCO2_StaffCost.res.csv'>	/h-offset-2--useCO2_StaffCost.res.csv</option>"
    			+ "<option value='/h-offset-2--useCO2_TotalCost.res.csv'>	/h-offset-2--useCO2_TotalCost.res.csv</option>"
    			+ "<option value='/h-offset-2--useCO2_TotalCost_CarPC.res.csv'>	/h-offset-2--useCO2_TotalCost_CarPC.res.csv</option>"
    			+ "<option value='/h-offset-2--useCO2_TravelCost.res.csv'>	/h-offset-2--useCO2_TravelCost.res.csv</option>"
    			+ "<option value='/h-offset-2--useTravelCost_StaffCost.res.csv'>	/h-offset-2--useTravelCost_StaffCost.res.csv</option>"
    			+ "<option value='/h-offset-4--useCO2_CarPC.res.csv'>	/h-offset-4--useCO2_CarPC.res.csv</option>"
    			+ "<option value='/h-offset-4--useCO2_StaffCost.res.csv'>	/h-offset-4--useCO2_StaffCost.res.csv</option>"
    			+ "<option value='/h-offset-4--useCO2_TotalCost.res.csv'>	/h-offset-4--useCO2_TotalCost.res.csv</option>"
    			+ "<option value='/h-offset-4--useCO2_TotalCost_CarPC.res.csv'>	/h-offset-4--useCO2_TotalCost_CarPC.res.csv</option>"
    			+ "<option value='/h-offset-4--useCO2_TravelCost.res.csv'>	/h-offset-4--useCO2_TravelCost.res.csv</option>"
    			+ "<option value='/h-offset-4--useTravelCost_StaffCost.res.csv'>	/h-offset-4--useTravelCost_StaffCost.res.csv</option>"
    			+ "<option value='/h-offset-8--useCO2_CarPC.res.csv'>	/h-offset-8--useCO2_CarPC.res.csv</option>"
    			+ "<option value='/h-offset-8--useCO2_StaffCost.res.csv'>	/h-offset-8--useCO2_StaffCost.res.csv</option>"
    			+ "<option value='/h-offset-8--useCO2_TotalCost.res.csv'>	/h-offset-8--useCO2_TotalCost.res.csv</option>"
    			+ "<option value='/h-offset-8--useCO2_TotalCost_CarPC.res.csv'>	/h-offset-8--useCO2_TotalCost_CarPC.res.csv</option>"
    			+ "<option value='/h-offset-8--useCO2_TravelCost.res.csv'>	/h-offset-8--useCO2_TravelCost.res.csv</option>"
    			+ "<option value='/h-offset-8--useTravelCost_StaffCost.res.csv'>	/h-offset-8--useTravelCost_StaffCost.res.csv</option>"
    			+ "<option value='/h-offset-r00--useCO2_CarPC.res.csv'>	/h-offset-r00--useCO2_CarPC.res.csv</option>"
    			+ "<option value='/h-offset-r00--useCO2_StaffCost.res.csv'>	/h-offset-r00--useCO2_StaffCost.res.csv</option>"
    			+ "<option value='/h-offset-r00--useCO2_TotalCost.res.csv'>	/h-offset-r00--useCO2_TotalCost.res.csv</option>"
    			+ "<option value='/h-offset-r00--useCO2_TotalCost_CarPC.res.csv'>	/h-offset-r00--useCO2_TotalCost_CarPC.res.csv</option>"
    			+ "<option value='/h-offset-r00--useCO2_TravelCost.res.csv'>	/h-offset-r00--useCO2_TravelCost.res.csv</option>"
    			+ "<option value='/h-offset-r00--useTravelCost_StaffCost.res.csv'>	/h-offset-r00--useTravelCost_StaffCost.res.csv</option>"
    			+ "<option value='/h-offset-rnd--useCO2_CarPC.res.csv'>	/h-offset-rnd--useCO2_CarPC.res.csv</option>"
    			+ "<option value='/h-offset-rnd--useCO2_StaffCost.res.csv'>	/h-offset-rnd--useCO2_StaffCost.res.csv</option>"
    			+ "<option value='/h-offset-rnd--useCO2_TotalCost.res.csv'>	/h-offset-rnd--useCO2_TotalCost.res.csv</option>"
    			+ "<option value='/h-offset-rnd--useCO2_TotalCost_CarPC.res.csv'>	/h-offset-rnd--useCO2_TotalCost_CarPC.res.csv</option>"
    			+ "<option value='/h-offset-rnd--useCO2_TravelCost.res.csv'>	/h-offset-rnd--useCO2_TravelCost.res.csv</option>"
    			+ "<option value='/h-offset-rnd--useTravelCost_StaffCost.res.csv'>	/h-offset-rnd--useTravelCost_StaffCost.res.csv</option>"




    			+ "<option selected='selected'>" + fileName +"</option>"
    			+ "</select> </h3>"

    			+ "<h3>Ranges </h3>"

		+ "<h3>Staff Cost "
		+ "<select name='STAFF_COST_RANGE'>"
		+ "<datalist id='STAFF_COST_RANGE'>"
		+ "<option value='-1'> Don't Care</option>"
		+ "<option value='VLOW'> VLOW</option>"
		+ "<option value='LOW'> LOW</option>"
		+ "<option value='MEDIUM'> MEDIUM</option>"
		+ "<option value='HIGH'> HIGH</option>"
		+ "<option value='VHIGH'> VHIGH</option>"
		+ "<option selected='selected'>"+getOptionRange(STAFF_COST)+"</option>"
		+ "</select>"

				+ "Travel Cost "
				+ "<select name='TRAVEL_COST_RANGE'>"
				+ "<datalist id='TRAVEL_COST_RANGE'>"
				+ "<option value='-1'> Don't Care</option>"
				+ "<option value='VLOW'> VLOW</option>"
				+ "<option value='LOW'> LOW</option>"
				+ "<option value='MEDIUM'> MEDIUM</option>"
				+ "<option value='HIGH'> HIGH</option>"
				+ "<option value='VHIGH'> VHIGH</option>"
				+ "<option selected='selected'>"+getOptionRange(TRAVEL_COST)+"</option>"
				+ "</select>"

				+ "CO2 "
				+ "<select name='CO2_RANGE'>"
				+ "<datalist id='CO2'>"
				+ "<option value='-1'> Don't Care</option>"
				+ "<option value='VLOW'> VLOW</option>"
				+ "<option value='LOW'> LOW</option>"
				+ "<option value='MEDIUM'> MEDIUM</option>"
				+ "<option value='HIGH'> HIGH</option>"
				+ "<option value='VHIGH'> VHIGH</option>"
				+ "<option selected='selected'>"+getOptionRange(CO2)+"</option>"
				+ "</select>"


	+ "Total Cost "
	+ "<select name='TOTAL_COST_RANGE'>"
	+ "<datalist id='TOTAL_COST_RANGE'>"
	+ "<option value='-1'> Don't Care</option>"
	+ "<option value='VLOW'> VLOW</option>"
	+ "<option value='LOW'> LOW</option>"
	+ "<option value='MEDIUM'> MEDIUM</option>"
	+ "<option value='HIGH'> HIGH</option>"
	+ "<option value='VHIGH'> VHIGH</option>"
	+ "<option selected='selected'>"+getOptionRange(TOTAL_COST)+"</option>"
	+ "</select>"

	+ "Car Use "
	+ "<select name='CAR_USE_RANGE'>"
	+ "<datalist id='CAR_USE_RANGE'>"
	+ "<option value='-1'> Don't Care</option>"
	+ "<option value='VLOW'> VLOW</option>"
	+ "<option value='LOW'> LOW</option>"
	+ "<option value='MEDIUM'> MEDIUM</option>"
	+ "<option value='HIGH'> HIGH</option>"
	+ "<option value='VHIGH'> VHIGH</option>"
	+ "<option selected='selected'>"+getOptionRange(CAR_USE)+"</option>"
	+ "</select>"

	+ "Staff "
	+ "<select name='STAFF_RANGE'>"
	+ "<datalist id='STAFF_RANGE'>"
	+ "<option value='-1'> Don't Care</option>"
	+ "<option value='VLOW'> VLOW</option>"
	+ "<option value='LOW'> LOW</option>"
	+ "<option value='MEDIUM'> MEDIUM</option>"
	+ "<option value='HIGH'> HIGH</option>"
	+ "<option value='VHIGH'> VHIGH</option>"
	+ "<option selected='selected'>"+getOptionRange(STAFF)+"</option>"
	+ "</select>"



	+ "</h3>"
	+ "<h3>Significance (%)</h3> "


+ "<h3>Staff Cost "
+ "<select name='STAFF_COST_SIG'>"
+ "<datalist id='STAFF_COST_SIG'>"
+ "<option value='-1'> Don't Care</option>"
+ "<option value='1'> 1</option>"
+ "<option value='5'> 5</option>"
+ "<option value='10'> 10</option>"
+ "<option value='15'> 15</option>"
+ "<option value='20'> 20</option>"
+ "<option selected='selected'> "+ getOptionSig(STAFF_COST)+"</option>"
+ "</select>"

+ "Travel Cost "
+ "<select name='TRAVEL_COST_SIG'>"
+ "<datalist id='TRAVEL_COST_SIG'>"
+ "<option value='-1'> Don't Care</option>"
+ "<option value='1'> 1</option>"
+ "<option value='5'> 5</option>"
+ "<option value='10'> 10</option>"
+ "<option value='15'> 15</option>"
+ "<option value='20'> 20</option>"
+ "<option selected='selected'> "+ getOptionSig(TRAVEL_COST)+"</option>"
+ "</select>"

+ "CO2 "
+ "<select name='CO2_SIG'>"
+ "<datalist id='CO2_SIG'>"
+ "<option value='-1'> Don't Care</option>"
+ "<option value='1'> 1%</option>"
+ "<option value='5'> 5%</option>"
+ "<option value='10'> 10%</option>"
+ "<option value='15'> 15%</option>"
+ "<option value='20'> 20%</option>"
+ "<option selected='selected'> "+ getOptionSig(CO2)+"</option>"
+ "</select>"


+ "Total Cost "
+ "<select name='TOTAL_COST_SIG'>"
+ "<datalist id='TOTAL_COST_SIG'>"
+ "<option value='-1'> Don't Care</option>"
+ "<option value='1'> 1</option>"
+ "<option value='5'> 5</option>"
+ "<option value='10'> 10</option>"
+ "<option value='15'> 15</option>"
+ "<option value='20'> 20</option>"
+ "<option selected='selected'> "+ getOptionSig(TOTAL_COST)+"</option>"
+ "</select>"

+ "Car use "
+ "<select name='CAR_USE_SIG'>"
+ "<datalist id='CAR_USE_SIG'>"
+ "<option value='-1'> Don't Care</option>"
+ "<option value='1'> 1%</option>"
+ "<option value='5'> 5%</option>"
+ "<option value='10'> 10%</option>"
+ "<option value='15'> 15%</option>"
+ "<option value='20'> 20%</option>"
+ "<option selected='selected'> "+ getOptionSig(CAR_USE)+"</option>"
+ "</select>"

+ "Staff "
+ "<select name='STAFF_SIG'>"
+ "<datalist id='STAFF_SIG'>"
+ "<option value='-1'> Don't Care</option>"
+ "<option value='1'> 1%</option>"
+ "<option value='5'> 5%</option>"
+ "<option value='10'> 10%</option>"
+ "<option value='15'> 15%</option>"
+ "<option value='20'> 20%</option>"
+ "<option selected='selected'> "+ getOptionSig(STAFF)+"</option>"
+ "</select>"



    			+ "</h3>"
    			+ "<input type='submit' value='Filter' />"
    			+ "    </form>");

    }
    
	private void writeHeader(PrintWriter out) {
		out.println("<HTML> "
				+ "<HEAD> "
				+ "	<TITLE>Workforce Scheduling and Routing</TITLE>"
				+ "		</HEAD>");
		out.println("<script src='http://d3js.org/d3.v3.min.js'></script> ");
		out.println("<script src='d3.parcoords.js'></script>");
		out.println("<link rel='stylesheet' type='text/css' href='d3.parcoords.css'>");
		out.println("<BODY>"
				+ "<H1>Parallel Coordinates: Evolvable Filter</H1>");
	}
    
	private void writeFooter(PrintWriter out) {
		out.println("</BODY> "
				+ "</HTML> ");
	}
	private void loadFile(String filename, ServletContext context, HttpServletResponse response, PrintWriter out)  {
		
		out.println("// " + filename);
		out.println("var data = [");
    	//Read data file
    	//
		// We are going to read a file called configuration.properties. This
		// file is placed under the WEB-INF directory.
		//
		
		
		//
		// First get the file InputStream using ServletContext.getResourceAsStream()
		// method.
		//
		try{
		InputStream is = context.getResourceAsStream(filename);
		if (is != null) {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader reader = new BufferedReader(isr);
			String text = "";
			
			//
			// We read the file line by line and later will be displayed on the 
			// browser page.
			//
			text = reader.readLine();//skip headers
			out.println("// File header " + text);
			
			//text = reader.readLine();//skip headers
			//out.println("// File header " + text);
			
			boolean first = true;
			while ((text = reader.readLine()) != null) {
				
				String[] data = text.split(",");
				
				String line = "";
				if (!first){
					line += ",";
					
				}
				first = false;
				line += "[";
				for (int x=0; x < data.length;x++){
					line += data[x];//truncate to 1 dp
					if ( x < data.length-1)
						line += ",";
				}
				line += "]";
				out.println(line);
			}
		}
		}catch(Exception e){
			
		}
    	out.println(" ];");
	}

	public boolean setFilterCriterion(HttpServletRequest request){
		boolean filter = false;
		/*
		 * Setup all criterion
		 * 
		 */
		resetCriterion();
		String tmp = request.getParameter("TRAVEL_COST_RANGE");
		if (tmp!= null)
			if (!tmp.equals("")){
				EvoFilter.targetRange[TRAVEL_COST] = convertRange(tmp);
				filter = true;
			}

		tmp = request.getParameter("TRAVEL_COST_RANGE");
		if (tmp!= null)
			if (!tmp.equals("")){
				EvoFilter.targetRange[TRAVEL_COST] = convertRange(tmp);
				filter = true;
			}
		tmp = request.getParameter("STAFF_COST_RANGE");
		if (tmp!= null)
			if (!tmp.equals("")){
				EvoFilter.targetRange[STAFF_COST] = convertRange(tmp);
				filter = true;
			}
		tmp = request.getParameter("TOTAL_COST_RANGE");
		if (tmp!= null)
			if (!tmp.equals("")){
				EvoFilter.targetRange[TOTAL_COST] = convertRange(tmp);
				filter = true;
			}
		tmp = request.getParameter("CO2_RANGE");
		if (tmp!= null)
			if (!tmp.equals("")){
				EvoFilter.targetRange[CO2] = convertRange(tmp);
				filter = true;
			}
		tmp = request.getParameter("STAFF_RANGE");
		if (tmp!= null)
			if (!tmp.equals("")){
				EvoFilter.targetRange[STAFF] = convertRange(tmp);
				filter = true;
			}
		tmp = request.getParameter("CAR_USE_RANGE");
		if (tmp!= null)
			if (!tmp.equals("")){
				EvoFilter.targetRange[CAR_USE] = convertRange(tmp);
				filter = true;
			}
		
		tmp = request.getParameter("TRAVEL_COST_SIG");
		if (tmp!= null)
			if (!tmp.equals("")){
				EvoFilter.minDiff[TRAVEL_COST] = convertSig(tmp);
				filter = true;
			}
		
		tmp = request.getParameter("STAFF_COST_SIG");
		if (tmp!= null)
			if (!tmp.equals("")){
				EvoFilter.minDiff[STAFF_COST] = convertSig(tmp);
				filter = true;
			}
		
		tmp = request.getParameter("TOTAL_COST_SIG");
		if (tmp!= null)
			if (!tmp.equals("")){
				EvoFilter.minDiff[TOTAL_COST] = convertSig(tmp);
				filter = true;
			}
		
		tmp = request.getParameter("CO2_SIG");
		if (tmp!= null)
			if (!tmp.equals("")){
				EvoFilter.minDiff[CO2] = convertSig(tmp);
				filter = true;
			}
		
		tmp = request.getParameter("CAR_USE_SIG");
		if (tmp!= null)
			if (!tmp.equals("")){
				EvoFilter.minDiff[CAR_USE] = convertSig(tmp);
				filter = true;
			}
		
		tmp = request.getParameter("STAFF_SIG");
		if (tmp!= null)
			if (!tmp.equals("")){
				EvoFilter.minDiff[STAFF_COST] = convertSig(tmp);
				filter = true;
			}
		return filter;
	}
	
	public static double convertSig(String input){
		if (input.equals("1"))
			return 0.01;
		if (input.equals("5"))
			return 0.05;
		if (input.equals("10"))
			return 0.1;
		if (input.equals("15"))
			return 0.15;
		if (input.equals("20"))
			return 0.2;

		return 0;
	}
	
	public static double[] convertRange(String input){
		if (input.equals("LOW"))
			return new double[]{0,0.4};
		if (input.equals("MEDIUM"))
			return new double[]{0.4,0.8};
		if (input.equals("HIGH"))
			return new double[]{0.6,1};
		if (input.equals("VLOW"))
			return new double[]{0,0.2};
		if (input.equals("VHIGH"))
			return new double[]{0.8,1};
		
		return new double[]{0,1};
		
	}
	
	private static void resetCriterion() {
		for(int x=0; x < EvoFilter.targetRange.length;x++){			
			EvoFilter.targetRange[x][EvoFilter.minR]=-1;
			EvoFilter.targetRange[x][EvoFilter.maxR]=-1;
			EvoFilter.minDiff[x] = 0;
			//-1 = don't care
		}
	}


}
