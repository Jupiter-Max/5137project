package GA;
import javax.swing.*;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Panel;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GA_refactoring {
	public int initgenelen;
	public int populationnum;
	public List population = new ArrayList();
	int numofGeneration = 500;
	List fitness_value = new ArrayList();
	List minfitness  = new ArrayList();
	double fitness_sum;
	double[][] table1 = {
			{12,12,3,3,15,0.41},
			{1,1,3,3,4,1},
			{1,1,1,1,2,0.99},
			{1,1,0,0,1,1},
			{1,1,0,0,1,1},
			{1,1,0,0,1,1},
			{1,1,0,0,1,1},
			{5,5,3,3,8,0.74},
			{12,12,5,5,17,0.98},
			{1,1,0,0,1,1},
			{1,1,0,0,1,0.99},
			{8,8,3,3,11,0.69}
	};
	
	double[][] table2 = {
			{15,0,0,0,0,0,0,0,12,0,0,0},
			{0,4,1,0,0,0,0,0,0,0,0,0},
			{12,0,2,0,0,0,0,0,0,0,0,0},
			{0,0,0,1,0,0,0,5,0,0,0,0},
			{0,0,0,0,1,0,0,5,0,0,0,0},
			{12,0,0,0,0,1,0,0,0,0,0,0},
			{12,0,0,0,0,0,1,0,0,0,0,0},
			{12,0,0,0,0,0,0,8,0,0,0,0},
			{0,0,0,0,0,0,0,0,17,0,0,0},
			{0,0,0,0,0,0,0,0,0,1,0,0},
			{0,0,0,0,0,0,0,0,0,1,1,8},
			{12,0,0,0,0,0,0,0,0,0,0,11}
	};
	
	double[][] table1_temp;
	double[][] table2_temp;
	
	List fitnessValue_table;
	List fitnessValue_temp;
	
	public GA_refactoring(int initgenelen, int populationnum) {
		this.initgenelen = initgenelen;
		this.populationnum = populationnum;
		//this.fitness_value = new double[populationnum];
		init_population(this.initgenelen,this.populationnum);
		for(int i=0;i<this.population.size();i++)
		{
			List individual = (List)population.get(i);
			for(int j=0;j<individual.size();j++)
			{
				System.out.print(individual.get(j)+" ");
			}
			System.out.println();
		}
		
		this.fitnessValue_table = fitness_table_value(this.table1,this.table2);
		for(int i=0;i<this.fitnessValue_table.size();i++)
		{
			List test = (List)this.fitnessValue_table.get(i);
			for(int j=0;j<test.size();j++)
			{
				System.out.print(test.get(j)+" ");
			}
			System.out.println();
		}
		
		this.fitness_value= fitness_value_population(this.population);
		
	}
	
	public boolean init_population(int genelength, int populationnum ) {
		if(genelength<=0||populationnum<=0)
		{
			System.out.println("the length of gene/the number of population does not satisfies requirement");
			return false;
		}
		
		for(int i=0;i<populationnum;i++)
		{
			List individual = new ArrayList();
			for(int j=0;j<genelength;j++)
			{
				Random random = new Random();//seed
				int refactoring_method = random.nextInt(2);
				individual.add(refactoring_method);
			}
			this.population.add(individual);
		}
		return true;
	}
	
	public List mutation(List a) {
		Random random = new Random(1000);//seed
		int cutpoint = random.nextInt(a.size());
		int flag = random.nextInt(2);
		if(flag==0)//randomly insert
		{
			int refactoring_temp = random.nextInt(2);
			a.add(cutpoint, refactoring_temp);
		}
		else if(flag==1)//randomly delete
		{
			a.remove(cutpoint);
		}
		return a;
	}
	
	public List crossover(List individual1, List individual2) {
		Random random = new Random(1000);
		List result = new ArrayList();
		int length1 = individual1.size();
		int length2 = individual2.size();
		int cutpoint = 0;
		if(length1<=length2)
		{
			cutpoint = random.nextInt(individual1.size());
		}
		else
		{
			cutpoint = random.nextInt(individual2.size());
		}
		List temp1 = new ArrayList();
		List temp2 = new ArrayList();
		for(int i=length1;i>cutpoint;i--)
		{
			temp1.add(individual1.get(i));
			individual1.remove(i);
		}
		
		for(int i=length2;i>cutpoint;i--)
		{
			temp2.add(individual2.get(i));
			individual2.remove(i);
		}
		individual1.addAll(temp2);
		individual2.addAll(temp1);
		result.add(individual1);
		result.add(individual2);
		
		return result;
	}
	
	public List selection(List population) {
		List matingpool = new ArrayList();
		double[] P = new double[population.size()];
		for(int i = 0;i<population.size();i++)
		{
			P[i] = (double)this.fitness_value.get(i) /this.fitness_sum;
			//System.out.println("S    "+this.fitness_value.get(i));
		}
		
//		for(int i = 0;i<population.size();i++)
//		{
//			System.out.println("P"+P[i]);
//		}
		
		Random random = new Random();
		double m = 0;
		double r = 0 + new Random().nextDouble() * (1 - 0); //r为0至1的随机数
		for(int j=1;j<population.size(); j++)
		{
		     m = m + P[j];
		     if(r<=m) 
		     {
		    	 System.out.println("selected");
		    	  matingpool.add(population.get(j));
		     }
		 }
		
		return matingpool;
	}
	
	public List generate_offspring(List population) {
		List offspring = this.selection(population);
		//crossover
		for(int i=0;i<offspring.size()-1;i++)
		{
			List individual1 = (List) offspring.get(i);
			List individual2 = (List) offspring.get(i+1);
			List crossover_result = this.crossover(individual1, individual2);
			offspring.set(i, crossover_result.get(0));
			offspring.set(i+1, crossover_result.get(1));
		}
		//mutation
		for(int i=0;i<offspring.size();i++)
		{
			double r = 0 + new Random().nextDouble() * (1 - 0);
			if(r<=0.01)
			{
				List individual_temp = (List) offspring.get(i);
				offspring.set(i, this.mutation(individual_temp));
			}
		}
		return offspring;
	}
	//compute the AI fitness value of a single chromosomes
	public List fitness_AI(double[][] table1, double[][] table2) {
		List result = new ArrayList();
		for(int i=0;i<table1.length;i++)
		{
			double result_temp = 1/3*(1/table1[i][0]+1/table1[i][1]+table1[i][3]/table1[i][4]);
			result.add(result_temp);
		}
		return result;
	}
	
	//compute the CE fitness value of a single chromosomes
		public List fitness_CE(double[][] table1, double[][] table2) {
			List result = new ArrayList();
			for(int i=0;i<table2.length;i++)
			{
				double sum_row = 0;
				for(int j= 0;j<table2[i].length;j++)
				{
					sum_row = sum_row + table2[i][j];
				}
				double sum_column = 0;
				for(int j= 0;j<table2.length;j++)
				{
					sum_column = sum_column + table2[j][i];
				}
				double result_temp = (sum_row+sum_column-2*table2[i][i])/(sum_row+sum_column-table2[i][i]);
				result.add(result_temp);
			}
			return result;
		}
	
	public List fitness_table_value(double[][] table1, double[][] table2) {
		//double result = 0.0;
		//AI
		List result_ai = fitness_AI(table1,table2);
		//CE
		List result_ce = fitness_CE(table1,table2);
		List row_3 = new ArrayList();
		for(int i = 0;i<result_ai.size();i++)
		{
			double temp = (double)result_ai.get(i)+(double)result_ce.get(i);
			row_3.add(temp);
		}
		List result = new ArrayList();
		result.add(result_ai);
		result.add(result_ce);
		result.add(row_3);
		return result;
	}
	
	public List fitness_value_population(List population) {
		double fitness_sum = 0;
		List result = new ArrayList();
		for(int i=0;i<population.size();i++)
		{
			List fitness_temp;
			refresh_table((List)population.get(i));
			fitness_temp = fitness_table_value(this.table1_temp,this.table2_temp);
			List fitrow_3 = (List)fitness_temp.get(2);
			double result_temp = 0;
			for(int k = 0;k<fitrow_3.size();k++)
			{
				result_temp = result_temp+ (double)fitrow_3.get(k);
				
			}
			fitness_sum = fitness_sum + result_temp;
			result.add(result_temp);
		}
		this.fitness_sum = fitness_sum;
		return result;
	}
	
	public void refresh_table(List individual) {
		this.table1_temp = this.table1;
		this.table2_temp = this.table2;
		this.fitnessValue_temp = this.fitnessValue_table;
		for(int i=0;i<individual.size();i++)
		{
			if((int)individual.get(i)==0)//pull interface
			{
				List ai_value = (List)this.fitnessValue_temp.get(0);
				int maxai_index = 0;
				double maxai_value = (double)ai_value.get(0);
				for(int j=0;j<ai_value.size();j++)
				{
					if((double)ai_value.get(i)>maxai_value)
					{
						maxai_index = i;
						maxai_value = (double)ai_value.get(i);
					}
				}
				
				if(this.table1_temp[maxai_index][2]>=1&&this.table1_temp[maxai_index][3]>=1)
				{
					this.table1_temp[maxai_index][0] = this.table1_temp[maxai_index][0] + 1;
					this.table1_temp[maxai_index][2] = this.table1_temp[maxai_index][2] + 1;
					this.table1_temp[maxai_index][3] = this.table1_temp[maxai_index][3] + 1;
					
					
					for(int m=0;m<12;m++)
					{
						if(this.table2_temp[m][maxai_index]>0)
						{
							this.table2_temp[m][maxai_index]++;
						}
					}
				}
			}
			else//push component
			{
				List ce_value = (List)this.fitnessValue_temp.get(1);
				int maxce_index = 0;
				int mince_index = 0;
				double maxce_value = (double)ce_value.get(0);
				double mince_value = (double)ce_value.get(0);
				for(int j=0;j<ce_value.size();j++)
				{
					if((double)ce_value.get(i)>maxce_value)
					{
						maxce_index = i;
						maxce_value = (double)ce_value.get(i);
					}
					
					if((double)ce_value.get(i)<mince_value)
					{
						mince_index = i;
						mince_value = (double)ce_value.get(i);
					}
				}
				
				for(int k=0;k<5;k++)
				{
					this.table1_temp[mince_index][k] = this.table1_temp[mince_index][k] + this.table1_temp[maxce_index][k];
					this.table1_temp[maxce_index][k] = 0;
				}
				this.table1_temp[mince_index][5] = (this.table1_temp[mince_index][5]+this.table1_temp[maxce_index][5])/2;
			
			
				for(int col = 0;col<12;col++)
				{
					if(col==mince_index)
					{
						this.table2_temp[maxce_index][maxce_index] = this.table2_temp[maxce_index][maxce_index] +this.table2_temp[mince_index][mince_index];
						this.table2_temp[mince_index][mince_index] = 0;
					}
					else
					{
						if(this.table2_temp[mince_index][col]!=0)
						{
							this.table2_temp[maxce_index][col] = this.table2_temp[maxce_index][col] + this.table2_temp[mince_index][col];
							this.table2_temp[mince_index][col] = 0;
							
						}
						
						if(this.table2_temp[col][mince_index]!=0)
						{
							this.table2_temp[col][maxce_index] = this.table2_temp[col][maxce_index] + this.table2_temp[col][mince_index];
							this.table2_temp[col][mince_index] = 0;
							
						}
					}
			    }
			
			}
		}
	}
	
	
	
	public void core() {
		double minfitnessvalue = 0;
		int numG = this.numofGeneration;
		
		while(numG!=0)
		{
			List matingpool = selection(this.population);
			
			List offspring = generate_offspring(matingpool);
			
			this.fitness_value = fitness_value_population(offspring);
			
			double minf = 0;
			for(int i=0;i<fitness_value.size();i++)
			{
				if((double)fitness_value.get(i)<minf)
				{
					minf = (double)fitness_value.get(i);
				}
			}
			this.minfitness.add(minf);
			this.population = offspring;
			numG--;
		}
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GA_refactoring test = new GA_refactoring(2,20);
		test.core();
		//test.init_population(0, 0)
		

		
//		Frame f = new Frame();
//        Panel p= new Panel() {
// 
//            @Override
//            public void paint(Graphics g) {
//                // TODO Auto-generated method stub
//                super.paint(g);
//                g.setColor(Color.RED); 
//                
//                
//                for(int i=1;i<test.numofGeneration;i++) //draw map
//                {
//         
//                	g.drawImage((double)i,(double)test.minfitness.get(i-1),(double)i+1,(double)test.minfitness.get(i));
//                }
//               
//                
//            }
//             
//        };
//        f.setVisible(true);
//        f.setBounds(100, 100, 400, 400);
//        f.add(p);
	}

}
