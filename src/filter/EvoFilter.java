package filter;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class EvoFilter {

	static Random rnd = new Random();
	
	static double[][] targetRange = new double[6][2];
	static double[] minDiff = new double[6];
	static int minR=0;
	static int maxR=1;
	

	
	static double scoreFilter(double[][] raw, boolean[] filter, int criterion) {
		//Criterion  0 only score on range
		//Criterion  1 only score on diff
		//criterion  3 do both
		double scoreRange =0; //add a penalty for each item outside of the filter
		double scoreFreq =0;
		
		if ((criterion == 0)||(criterion==3)){

			for (int c=0; c< filter.length;c++){
				for(int r=0; r < raw[0].length;r++){
					if(targetRange[r][minR]!= -1){
						if(raw[c][r] > targetRange[r][maxR]){
							//Above range
							if (!filter[c]) 
								scoreRange +=(raw[c][r]-targetRange[r][maxR]);
						}

						if(raw[c][r] < targetRange[r][minR]){
							//below range
							if (!filter[c])
								scoreRange += (targetRange[r][minR]- raw[c][r]) ;
						}

						//Now check in range and filtered (bad)

						if((raw[c][r] < targetRange[r][maxR]) && 	(raw[c][r] > targetRange[r][minR])){
							if (filter[c]){
								scoreRange += 0.01;
							}
						}
					}
				}
			}
			//return scoreRange;
		}
		if ((criterion == 1)||(criterion==3)){
			//Check min spacing
			//Filter on 0
			
			for (int r=0; r < raw[0].length; r++){

				//copy
				ArrayList<Double> tmp = new ArrayList<Double>();
				for (int x=0; x< filter.length;x++){
					if (!filter[x])
						tmp.add(raw[x][r]);//staff
				}		

				Collections.sort(tmp);
				for (int x=0; x <tmp.size()-1;x++){
					double c = tmp.get(x);
					double n = tmp.get(x+1);
					double diff = Math.abs(c-n);
					if (diff < minDiff[r]){
						scoreFreq += (minDiff[r] - diff);

					}
				}	
			}
			//return scoreFreq;
		}
		if (criterion == 0)
			return scoreRange;
		if (criterion ==1)
			return scoreFreq;
		// Default if (criterion==3)
			return scoreRange+scoreFreq;
	}

	
	
	static int mutate(boolean[] filter) {
		int bit = rnd.nextInt(filter.length); 
		filter[bit] = !filter[bit];
		return bit;
	}

	static void reverse(boolean[] filter, int bit) {
		filter[bit] = !filter[bit];
	}

	public class Individ{
		double score;
		boolean[] filter;
		
		public Individ(){
			
		}
		
		public Individ(Individ p1, Individ p2){
			this.filter = new boolean[p1.filter.length];
			
			Individ p = p1;
			for(int x = 0; x < this.filter.length;x++){
				this.filter[x] = p.filter[x];
				if (p==p1)
					p = p2;
				else
					p = p1;
			}
		}
		
		public Individ clone(){
			Individ res = new Individ();
			res.score = this.score;
			res.filter = new boolean[this.filter.length];
			for (int x =0; x < this.filter.length;x++)
				res.filter[x] = this.filter[x];
			
			return res;
		}
	}
	
	public boolean[] evoFilter(double[][] raw, int criterion) {
		//setup pop
		int POP_SIZE = 20;
		ArrayList<Individ> pop = new ArrayList<Individ>();
		Individ besty = new Individ();
		besty.score = Double.MAX_VALUE;
		
		for (int c=0; c < POP_SIZE; c++){
			boolean[] filter = new boolean[raw.length];
			//True = out
			//False = in

			//randomize filter
			for (int x=0; x < filter.length;x++){
				filter[x] = rnd.nextBoolean(); 
			}
			Individ i = new Individ();
			i.filter = filter;
			i.score = scoreFilter(raw, filter, criterion);
			pop.add(i);
			if (i.score < besty.score)
				besty = i;
		}
		
		
		
		
		//Make a random change
		
		
		
		//for(int x=0; x < 40000; x++)
			int lastCh=0;
		while(lastCh <  10000){
			//System.out.println(lastCh);
			//select parent
			Individ c1 = pop.get(rnd.nextInt(pop.size()));
			Individ c2 = pop.get(rnd.nextInt(pop.size()));
			Individ child;
			if (rnd.nextBoolean()){//Clone from a single parent
				if (c1.score < c2.score)
					child = c1.clone();
				else
					child = c2.clone();
			}else{
				//Create from 2 parents
				Individ p1, p2;
				if (c1.score < c2.score)
					p1 = c1;
				else
					p1 = c2;

				c1 = pop.get(rnd.nextInt(pop.size()));
				c2 = pop.get(rnd.nextInt(pop.size()));
				
				if (c1.score < c2.score)
					p2 = c1;
				else
					p2 = c2;

				child = new Individ(p1,p2);
			}
			
			if (rnd.nextBoolean())
				mutate (child.filter);
			
			child.score = scoreFilter(raw, child.filter, criterion);
			
			//Add back into pop
			c1 = pop.get(rnd.nextInt(pop.size()));
			c2 = pop.get(rnd.nextInt(pop.size()));
			Individ rip;
			if (c1.score < c2.score)
				rip = c2;
			else
				rip = c1;
			
			if (rip.score > child.score){
				pop.remove(rip);
				pop.add(child);
				if ((child.score <= besty.score)){
					besty = child;
					lastCh=0;
					}
				}else{
					lastCh++;
				}
			
		}
		return besty.filter;
		
	}

}
