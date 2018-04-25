/* On my honor, I have neither given nor received unauthorized aid on this assignment */
/*sajal singhal */
import java.util.*;
import java.lang.*;
import java.io.*;
import java.util.Vector;

class track_inst{
	public String op_code;public String destination_value;	public String f_fs;
	public String s_fs,l,k;	public String regsnm;
	public int value_reg;
	int add_dt,tmp3;
	int val_dt;
        boolean bool=false;
        track_inst(){
        l=null;
        k=null;
        }
	
	track_inst(String str1,int ins){
		this.regsnm = str1;
                k=regsnm;
		this.value_reg = tmp3 = ins;
	}
        
        track_inst(String str1,String str2,String str3,String str4){
		this.op_code = str1;
		this.destination_value = str2;
		if(!bool)
                this.f_fs = str3;
		this.s_fs = str4;
	}
	
	track_inst(int a,int v){
            if(!bool){
		this.add_dt = a;
		this.val_dt = v;
                this.tmp3=a+v;}
            else 
                   this.tmp3=a-v;
               }
}
class Input extends track_inst{
        public String cur_line,curline=null,next=null; 
	public BufferedReader buf_read1 = null,buf_read2=null,buf_read3=null;	
	public Vector <track_inst> INM = new Vector<track_inst>();
	public BufferedReader bufread3 = null;
	public Vector<track_inst> DAM = new Vector<track_inst>();
	public track_inst data_mem = null;
        public	File simulation_file,simmfile;
	public File register_file,r_file2;
	public Vector<track_inst> SIB = new Vector<track_inst>();
	public File data_file,s_datafile=null;
	public FileWriter w_file;
	public Vector<track_inst> RGF = new Vector<track_inst>();
	public BufferedReader bufread2 = null;
	public Vector<track_inst> INB = new Vector<track_inst>();
	public File instruction_file;
	public Vector<track_inst> AIB = new Vector<track_inst>();
	public Vector<track_inst> PRB = new Vector<track_inst>();
	public Vector<track_inst> ADB = new Vector<track_inst>();
	public BufferedWriter writein_file; 
	public Vector<track_inst> REB = new Vector<track_inst>();
        public Vector<track_inst> Reb1 = new Vector<track_inst>();
	public track_inst in = null,out=null;
	public track_inst rg = null,regs=null;
	 Input(){
         in=null;
         rg=null;
         buf_read2=null;
         buf_read1=null;
         buf_read3=null;
         data_mem=null;    
             }
	public void Input_Instructions(){
		try{
				
		instruction_file = new File("instructions.txt");	
		buf_read1 = new BufferedReader(new FileReader(instruction_file.getAbsolutePath()));

		while ((cur_line = buf_read1.readLine()) != null) {
			int m = cur_line.indexOf(',');
			int l = cur_line.length();
			
			
			String commandstring = cur_line.substring(1, m);
			
			cur_line = cur_line.substring(m+1, l);
			m = cur_line.indexOf(',');
			if(true)
                        {}
			String destination_valuestring = cur_line.substring(0, m);
			
			l = cur_line.length();
			cur_line = cur_line.substring(m+1, l);
			m = cur_line.indexOf(',');
			String source1string = cur_line.substring(0, m);
			
			l = cur_line.length();
			String source2string = cur_line.substring(m+1, l-1);
		  
			
			in = new track_inst(commandstring,destination_valuestring,source1string,source2string);
		
			INM.add(in);
		}
		}catch(Exception e){}
		
}
         
	public void Input_Registers(){
		try{
		register_file = new File("registers.txt");
		buf_read2 = new BufferedReader(new FileReader(register_file.getAbsolutePath()));
		  
		  while((cur_line = buf_read2.readLine())!= null){
			  int m2 = cur_line.indexOf(',');
			  int l2 = cur_line.length();
			  
			  String regs = cur_line.substring(1, m2);
			  String regv = cur_line.substring(m2+1, l2-1);
			  int a = Integer.parseInt(regv);
			  
			  rg = new track_inst(regs,a);
			  RGF.add(rg);
		
			  }
			  
		}catch(Exception e){}
	}
		
        public void Input_Datamemory(){
			try{
		  data_file = new File("datamemory.txt");
		  buf_read3 = new BufferedReader(new FileReader(data_file.getAbsolutePath()));
		  while((cur_line = buf_read3.readLine())!= null)
		  {
			  int m3 = cur_line.indexOf(',');
			  int l3 = cur_line.length();
			  int add = Integer.parseInt(cur_line.substring(1, m3));
			  int val = Integer.parseInt(cur_line.substring(m3+1, l3-1));

			  data_mem = new track_inst(add,val);
			  DAM.add(data_mem);
			}
		}catch(Exception e){}

	}
	
        public void line_print() throws IOException{
		
		String printLinetrack;
		writein_file.write("INM:");
               int j=0;
               if(j<INM.size()){
                   if(INM.get(j).op_code.equals("null")){
			    printLinetrack = "";
                            writein_file.write(printLinetrack);
                            //writein_file.write("sajal");
			    }
			    else{
                            //writein_file.write(",");
				printLinetrack = '<'+INM.get(j).op_code+','+INM.get(j).destination_value+','+INM.get(j).f_fs+','+INM.get(j).s_fs+'>';
			
                        writein_file.write(printLinetrack);
                        }
                   
               }
              
		for(int i =1; i<INM.size();i++){
			if(INM.get(i).op_code.equals("null")){
			    printLinetrack = "";
                            writein_file.write(printLinetrack);
                            //writein_file.write("sajal");
			    }
			    else{
                            writein_file.write(",");
				printLinetrack = '<'+INM.get(i).op_code+','+INM.get(i).destination_value+','+INM.get(i).f_fs+','+INM.get(i).s_fs+'>';
			
                        writein_file.write(printLinetrack);
                        }
				
                                
				
		}
		writein_file.newLine();
		
	
                
                writein_file.write("INB:");
                if(0<INB.size()){
                    if(INB.get(0).op_code.equals("null")){
		    printLinetrack = "";
		    }
		    else{
			printLinetrack = '<'+INB.get(0).op_code+','+INB.get(0).destination_value+','+INB.get(0).f_fs+','+INB.get(0).s_fs+'>';
			}
		
			writein_file.write(printLinetrack);
                }
		for(int i =1; i<INB.size();i++){
			if(INB.get(i).op_code.equals("null")){
		    printLinetrack = "";
		    }
		    else{writein_file.write(",");
			printLinetrack = '<'+INB.get(i).op_code+','+INB.get(i).destination_value+','+INB.get(i).f_fs+','+INB.get(i).s_fs+'>';
			}
			
			writein_file.write(printLinetrack);
		}
		writein_file.newLine();
		
		
		
		writein_file.write("AIB:");
                if(0<AIB.size()){
                if(AIB.get(0).op_code.equals("null")){
			    printLinetrack = "";
			    }
			    else{
				printLinetrack = '<'+AIB.get(0).op_code+','+AIB.get(0).destination_value+','+AIB.get(0).f_fs+','+AIB.get(0).s_fs+'>';
				}
				
				writein_file.write(printLinetrack);
                }
		for(int i =1; i<AIB.size();i++){
			if(AIB.get(i).op_code.equals("null")){
			    printLinetrack = "";
			    }
			    else{writein_file.write(",");
				printLinetrack = '<'+AIB.get(i).op_code+','+AIB.get(i).destination_value+','+AIB.get(i).f_fs+','+AIB.get(i).s_fs+'>';
				}
			
				writein_file.write(printLinetrack);
		}
		writein_file.newLine();
		
		
	
		writein_file.write("SIB:");
                if(0<SIB.size()){
                    
                    if(SIB.get(0).op_code.equals("null")){
			    printLinetrack = "";
			    }
			    else{
				printLinetrack = '<'+SIB.get(0).op_code+','+SIB.get(0).destination_value+','+SIB.get(0).f_fs+','+SIB.get(0).s_fs+'>';
				}
			
				writein_file.write(printLinetrack);
                    
                }
		for(int i =1; i<SIB.size();i++){
			if(SIB.get(i).op_code.equals("null")){
			    printLinetrack = "";
			    }
			    else{writein_file.write(",");
				printLinetrack = '<'+SIB.get(i).op_code+','+SIB.get(i).destination_value+','+SIB.get(i).f_fs+','+SIB.get(i).s_fs+'>';
				}
				
				writein_file.write(printLinetrack);
		}
		writein_file.newLine();
		
		
		
		writein_file.write("PRB:");
                if(0<PRB.size()){
                if(PRB.get(0).op_code.equals("null")){
			    printLinetrack = "";
			    }
			    else{
				printLinetrack = '<'+PRB.get(0).op_code+','+PRB.get(0).destination_value+','+PRB.get(0).f_fs+','+PRB.get(0).s_fs+'>';
				}
			
				writein_file.write(printLinetrack);
                }
		for(int i =1; i<PRB.size();i++){
			if(PRB.get(i).op_code.equals("null")){
			    printLinetrack = "";
			    }
			    else{
                            writein_file.write(",");
				printLinetrack = '<'+PRB.get(i).op_code+','+PRB.get(i).destination_value+','+PRB.get(i).f_fs+','+PRB.get(i).s_fs+'>';
				}
				
				writein_file.write(printLinetrack);
		}
		writein_file.newLine();
		
		
		
		writein_file.write("ADB:");
                if(0<ADB.size()){
                if(ADB.get(0).regsnm.equals("null")){
		    	printLinetrack = "";
		    }
		    else{
			printLinetrack = '<'+ADB.get(0).regsnm+','+ADB.get(0).value_reg+'>';
			
		    }
			
			writein_file.write(printLinetrack);
                }
		for(int i =1; i<ADB.size();i++){
			if(ADB.get(i).regsnm.equals("null")){
		    	printLinetrack = "";
		    }
		    else{ writein_file.write(",");
			printLinetrack = '<'+ADB.get(i).regsnm+','+ADB.get(i).value_reg+'>';
			
		    }
	
			writein_file.write(printLinetrack);
		}
		writein_file.newLine();
		
		
		writein_file.write("REB:");
                if(0<REB.size()){
                if(REB.get(0).regsnm.equals("null")){
		    	printLinetrack = "";
		    }
		    else{
			printLinetrack = '<'+REB.get(0).regsnm+','+REB.get(0).value_reg+'>';
			
		    }

			writein_file.write(printLinetrack);
                }
		for(int i =1; i<REB.size();i++){
			if(REB.get(i).regsnm.equals("null")){
		    	printLinetrack = "";
		    }
		    else{
                            writein_file.write(",");
			printLinetrack = '<'+REB.get(i).regsnm+','+REB.get(i).value_reg+'>';
			
		    }
			
			writein_file.write(printLinetrack);
		}
		writein_file.newLine();
		
		

		writein_file.write("RGF:");
                
                int k=0;
               if(k<RGF.size()){
                  if(RGF.get(k).regsnm.equals("null")){
		    	printLinetrack = "";
		    }
		    else{
			printLinetrack = '<'+RGF.get(k).regsnm+','+RGF.get(k).value_reg+'>';
			
		    }
			writein_file.write(printLinetrack);
                   
               }
		for(int i =1; i<RGF.size();i++){
			if(RGF.get(i).regsnm.equals("null")){
		    	printLinetrack = "";
		    }
		    else{
                             writein_file.write(",");
			printLinetrack = '<'+RGF.get(i).regsnm+','+RGF.get(i).value_reg+'>';
			
		    }
			
			writein_file.write(printLinetrack);
		}
		writein_file.newLine();
		
		
		writein_file.write("DAM:");
                int l=0;
                if(l<DAM.size()){
                    if(String.valueOf(DAM.get(l).add_dt).equals("null")){
		    	printLinetrack = "";
		    }
		    else{
			printLinetrack = "<"+DAM.get(l).add_dt+","+DAM.get(l).val_dt+">";
			
		    }
		
			writein_file.write(printLinetrack);
                }
                
		for(int i =1; i<DAM.size();i++){
			if(String.valueOf(DAM.get(i).add_dt).equals("null")){
		    	printLinetrack = "";
		    }
		    else{
                            writein_file.write(",");
			printLinetrack = "<"+DAM.get(i).add_dt+","+DAM.get(i).val_dt+">";
			   }
			
			writein_file.write(printLinetrack);
		}
		writein_file.newLine();
		
	}
        public void decode_read(track_inst in){
		if (in.op_code.equalsIgnoreCase("ADD") || in.op_code.equalsIgnoreCase("MUL") || in.op_code.equalsIgnoreCase("SUB")){
			for (int i = 0; i<RGF.size() ; i++){
				if (RGF.get(i).regsnm.equals(in.f_fs))
					in.f_fs = String.valueOf(RGF.get(i).value_reg);
				if (RGF.get(i).regsnm.equals(in.s_fs))
					in.s_fs = String.valueOf(RGF.get(i).value_reg);
			}
		}
		else{
			for (int i = 0; i<RGF.size() ; i++){
				if (RGF.get(i).regsnm.equals(in.f_fs))
					in.f_fs = String.valueOf(RGF.get(i).value_reg);
			}
		}
		INB.add(in);	
	}
}

class Processor extends Input{

	public void main_task() throws IOException {
				
		  simulation_file = new File("simulation.txt");
                  boolean b;
                  b=simulation_file.exists();
                  //System.out.println(b);
		  if (!b) {
			simulation_file.createNewFile();
		}
			
				if(true){w_file = new FileWriter(simulation_file.getAbsoluteFile());
				writein_file = new BufferedWriter(w_file);}
						
				writein_file.write("STEP 0:");
				writein_file.newLine();
				super.line_print();
				
				
				
				int i=1;
				do{
				     if(!INM.isEmpty() || !REB.isEmpty() || !INB.isEmpty() ||
					  !AIB.isEmpty() || !SIB.isEmpty() || !PRB.isEmpty() ||
					  !ADB.isEmpty()){
							
			writein_file.newLine();
				writein_file.write("STEP "+i+":");
				writein_file.newLine();
				if (!REB.isEmpty())
					flag_u(REB.remove(0));
				if (!ADB.isEmpty())
					{
                                         track_inst adb= ADB.remove(0);
		boolean found = false;
		int rval = 0;
		track_inst d = new track_inst();
		for (int j = 0; j<RGF.size() ; j++){
			if (RGF.get(j).regsnm.equalsIgnoreCase(adb.regsnm)){
				rval = RGF.get(j).value_reg;
			}
		}
		
		for (int j = 0 ; j<DAM.size() ; j++){
			if (DAM.get(j).add_dt == adb.value_reg){
				DAM.get(j).val_dt = rval;
				found = true;
			}
		}
		if (!found){
			d.add_dt = adb.value_reg;
			d.val_dt = rval;
			DAM.add(loc_datamem(d), d);
		}
		
                                          }
				if(!PRB.isEmpty()){
					
                                track_inst prb=PRB.remove(0);
                                track_inst rebtemp = new track_inst();
                                rebtemp.value_reg =  Integer.parseInt(prb.f_fs) * Integer.parseInt(prb.s_fs);
                                rebtemp.regsnm = prb.destination_value;
                                REB.add(rebtemp);
                                }
				
                                if (!SIB.isEmpty()){
                                    track_inst sib=SIB.remove(0);
                                    track_inst tempadb = new track_inst();
                           		tempadb.value_reg =  Integer.parseInt(sib.f_fs) + Integer.parseInt(sib.s_fs);
                                    tempadb.regsnm = sib.destination_value;
                                    ADB.add(tempadb);
                }
		
	
				if (!AIB.isEmpty())
                                {track_inst in=AIB.remove(0);
                                track_inst r = new track_inst() ;
                                        if(true)
                                                    if (in.op_code.equalsIgnoreCase("ADD")){
                                                r.value_reg = Integer.parseInt(in.f_fs) + Integer.parseInt(in.s_fs);
                                                    r.regsnm = in.destination_value;
                                                        REB.add(r);
			
                                            }
                                               if (in.op_code.equalsIgnoreCase("SUB")){
                                        r.value_reg = Integer.parseInt(in.f_fs) - Integer.parseInt(in.s_fs);
                                                r.regsnm = in.destination_value;
                                                        REB.add(r);
			
                                                            }
                                                        if (in.op_code.equalsIgnoreCase("MUL")){
                                                            PRB.add(in);
			
		}
	}
				
                                if (!INB.isEmpty()){
                                    track_inst in,it;
                                in =INB.remove(0);
		                    if (in.op_code.equalsIgnoreCase("ADD") || in.op_code.equalsIgnoreCase("MUL") || in.op_code.equalsIgnoreCase("SUB")){
			                                           AIB.add(in);
		                                  }
                            		else 
			                     SIB.add(in);
	
                                }
					
				if (!INM.isEmpty())
					super.decode_read(INM.remove(0));
				super.line_print();
					i++;
				}
                                }while(!INM.isEmpty() || !REB.isEmpty() || !INB.isEmpty() ||
					  !AIB.isEmpty() || !SIB.isEmpty() || !PRB.isEmpty() ||
					  !ADB.isEmpty());		
			buf_read1.close();
			buf_read2.close();
			buf_read3.close();
			writein_file.close();
	}
		
	
	public int loc_datamem(track_inst dat){
	int i=0;	
            while (i < DAM.size()){
			if (dat.add_dt < DAM.get(i).add_dt)
				return i;
		i++;
                }
		return DAM.size();
	}
	
	
	
	public int loc_fd(track_inst temp){
		
		String t,t1,temp1=null;
		int pos1,pos2;
	t = temp.regsnm;
	t = t.substring(1, t.length());
        temp1=temp.regsnm;
	int pos = Integer.parseInt(t);
        if(true)
		for(int i=0;i<RGF.size();i++){
	t1 = RGF.get(i).regsnm;
	if(true)		
        t1 = t1.substring(1, t1.length());
		pos1 = Integer.parseInt(t1);
	if(pos<pos1){
            if(true)
			return i;
			}
		}
		return 0;
		
	}
	
	public void flag_u(track_inst reb)
	{
		
	int flag=0;
        int i=0;
	while( i<RGF.size()){
            if(true)
	if(reb.regsnm.equalsIgnoreCase(RGF.get(i).regsnm)){
		RGF.get(i).value_reg = reb.value_reg;
	flag=1;
	}
            i++;
}
	int pos = loc_fd(reb);
	if(flag==0){
		RGF.add(pos, reb);
			
		}
	}

	
}

public class MIPSsim {
	public static void main(String[]args) throws IOException{
		Processor pc = new Processor();
                Input inpt = new Input();
                pc.Input_Instructions();	
                pc.Input_Registers();	
                pc.Input_Datamemory();
		pc.main_task();
	}
}


