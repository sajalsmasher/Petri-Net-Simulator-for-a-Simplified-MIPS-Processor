/* On my honor, I have neither given nor received unauthorized aid on this assignment */
/*sajal singhal */
import java.util.*;
import java.lang.*;
import java.io.*;
import java.util.Vector;

class track_inst{
	
	public String op_code;
	public String destination_value;
	public String f_fs;
	public String s_fs;
	public String regsnm;
	public int value_reg;
	int add_dt;
	int val_dt;
        track_inst(){}
	
	track_inst(String str1,int ins){
		regsnm = str1;
		value_reg = ins;
	}
        
        track_inst(String str1,String str2,String str3,String str4){
		this.op_code = str1;
		this.destination_value = str2;
		this.f_fs = str3;
		this.s_fs = str4;
	}
	
	track_inst(int a,int v){
		add_dt = a;
		val_dt = v;
	}
}

class Simulator {
	
	String cur_line; 
	BufferedReader buf_read1 = null;	
	Vector <track_inst> INM = new Vector<track_inst>();
	
	BufferedReader buf_read3 = null;
	Vector<track_inst> DAM = new Vector<track_inst>();
	
	track_inst data_mem = null;
	
	File simulation_file;
	
	File register_file;
	Vector<track_inst> SIB = new Vector<track_inst>();
	File data_file;
	
	FileWriter w_file;
	
				
	
	Vector<track_inst> RGF = new Vector<track_inst>();
	BufferedReader buf_read2 = null;
	
	Vector<track_inst> INB = new Vector<track_inst>();
	
	File instruction_file;
	Vector<track_inst> AIB = new Vector<track_inst>();
	
	Vector<track_inst> PRB = new Vector<track_inst>();
	Vector<track_inst> ADB = new Vector<track_inst>();
	BufferedWriter writein_file; 
	
	Vector<track_inst> REB = new Vector<track_inst>();
	
	track_inst in = null;
	track_inst rg = null;
	
	
	
	
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
			
			
			String destination_valuestring = cur_line.substring(0, m);
			
			l = cur_line.length();
			cur_line = cur_line.substring(m+1, l);
			m = cur_line.indexOf(',');
			String source1string = cur_line.substring(0, m);
			
			l = cur_line.length();
			String source2string = cur_line.substring(m+1, l-1);
		  
			//Print(s1,s2,s3,s4);
			in = new track_inst(commandstring,destination_valuestring,source1string,source2string);
			//ins.write();
			INM.add(in);
		}
		}catch(Exception e){System.out.print(e);}
		
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
			  
		}catch(Exception e){System.out.print(e);}
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
		}catch(Exception e){System.out.print(e);}

	}
	
	////////////////////////////////TAKING INPUT FROM FILE METHOD END///////////////////////////
	
	
	
	
	/////////////////////////////////MAIN WORKER OF THE SIMULATOR CALLS OTHER METHODS//////////
	public void main_task() throws IOException {
		
		
		
		 /* Input_Instructions();	
                  Input_Registers();	
                  Input_Datamemory();	*/
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
				line_print();
				
				
				///////////////////MAIN LOOP STARTS///////////
				/* checks the emptiness of each buffer and calls each function simultaneously*/
				int i=1;
				do{
				     if(!INM.isEmpty() || !REB.isEmpty() || !INB.isEmpty() ||
					  !AIB.isEmpty() || !SIB.isEmpty() || !PRB.isEmpty() ||
					  !ADB.isEmpty()){
							
			writein_file.newLine();
				writein_file.write("STEP "+i+":");
				writein_file.newLine();
				if (!REB.isEmpty())
					Unit_w(REB.remove(0));
				if (!ADB.isEmpty())
					str_data(ADB.remove(0));
				if(!PRB.isEmpty())
					Unit_mul(PRB.remove(0));
				if (!SIB.isEmpty())
					add_pnh(SIB.remove(0));
				if (!AIB.isEmpty())
					Unit_add_sub(AIB.remove(0));
				if (!INB.isEmpty())
					inst_issue(INB.remove(0));
				if (!INM.isEmpty())
					decode_read(INM.remove(0));
				line_print();
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
	
	
	////////READ N DECODES THE INCOMING INST//////////////
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
	//////////READ N DECODE ENDS//////////////////////
	
	/////////ISSUE METHOD START//////////
	public void inst_issue(track_inst in){
		if (in.op_code.equalsIgnoreCase("ADD") || in.op_code.equalsIgnoreCase("MUL") || in.op_code.equalsIgnoreCase("SUB")){
			AIB.add(in);
		}
		else 
			SIB.add(in);
	}
	//////////ISSUE METHOD ENDS/////////
	
	//////////////////ADD SUB UNIT////////
	public void Unit_add_sub(track_inst in){
		track_inst r = new track_inst() ;
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
	/////////////////////////ADD SUB UNIT END//////

	///////////////MULTIPLY UNIT////////
	public void Unit_mul(track_inst prb){
		track_inst rebtemp = new track_inst();
		rebtemp.value_reg =  Integer.parseInt(prb.f_fs) * Integer.parseInt(prb.s_fs);
		rebtemp.regsnm = prb.destination_value;
		REB.add(rebtemp);
		
	}
	//////////////MULTIPLY UNIT ENDS//////
	
	
	//////////////ADDRESS WRITER////////
	public void add_pnh(track_inst sib){
		track_inst tempadb = new track_inst();
		tempadb.value_reg =  Integer.parseInt(sib.f_fs) + Integer.parseInt(sib.s_fs);
		tempadb.regsnm = sib.destination_value;
		ADB.add(tempadb);
		
	}
	/////////////ADDRESS WRITER ENDS////////
	
	
	/////////////GETS POSITION OF A PARTICULAR MEMORY LOCATION/////
	public int position_DAM(track_inst dat){
		for (int i = 0 ; i < DAM.size() ; i++){
			if (dat.add_dt < DAM.get(i).add_dt)
				return i;
		}
		return 0;
	}
	//ENDS//
	
	/////////////STORES A NEW REGISTER////
	public void str_data(track_inst adb){
		boolean found = false;
		int rval = 0;
		track_inst d = new track_inst();
		for (int i = 0; i<RGF.size() ; i++){
			if (RGF.get(i).regsnm.equalsIgnoreCase(adb.regsnm)){
				rval = RGF.get(i).value_reg;
			}
		}
		
		for (int i = 0 ; i<DAM.size() ; i++){
			if (DAM.get(i).add_dt == adb.value_reg){
				DAM.get(i).val_dt = rval;
				found = true;
			}
		}
		if (!found){
			d.add_dt = adb.value_reg;
			d.val_dt = rval;
			DAM.add(position_DAM(d), d);
		}
		
		//ADB.regsnm = "null";	
		
	}
	////ENDS///
	
	
	/////////GETS INDEX OF A PARTICULAR REGISTER IN MEMORY////
	public int find_position(track_inst temp){
		
		String t,t1;
		int pos1;
		t = temp.regsnm;
		t = t.substring(1, t.length());
		int pos = Integer.parseInt(t);
		for(int i=0;i<RGF.size();i++){
			t1 = RGF.get(i).regsnm;
			t1 = t1.substring(1, t1.length());
			pos1 = Integer.parseInt(t1);
			if(pos<pos1){
				return i;
			}
		}
		return 0;
		
	}
	////////////ENDS/////////
	
	//////////////WRITES A NEW REGISTER//////
	public void Unit_w(track_inst reb)
	{
		/*String t = reb.regsnm;
		t = t.substring(1, t.length());
		//System.out.print("regpos:"+reb.regsnm);
		*/
		int flag=0;
		for(int i=0; i<RGF.size() ;i++){

			if(reb.regsnm.equalsIgnoreCase(RGF.get(i).regsnm)){
				RGF.get(i).value_reg = reb.value_reg;
				flag=1;
			}
		}
		int pos = find_position(reb);
		if(flag==0){
			RGF.add(pos, reb);
			//RGF.add(reb);
		}
	}
	//////ENDS//////////

	
	/////WRITES THE CURRENT STATUS OF THE QUEUES IN THE FILE////////
	public void line_print() throws IOException{
		try{
		String Sret;
			
		//System.out.print("\nINM:");
		writein_file.write("INM:");
               int j=0;
               if(j<INM.size()){
                   if(INM.get(j).op_code.equals("null")){
			    Sret = "";
                            writein_file.write(Sret);
                            //writein_file.write("sajal");
			    }
			    else{
                            //writein_file.write(",");
				Sret = '<'+INM.get(j).op_code+','+INM.get(j).destination_value+','+INM.get(j).f_fs+','+INM.get(j).s_fs+'>';
			
                        writein_file.write(Sret);
                        }
                   
               }
               // writein_file.write(INM.size());
		for(int i =1; i<INM.size();i++){
			if(INM.get(i).op_code.equals("null")){
			    Sret = "";
                            writein_file.write(Sret);
                            //writein_file.write("sajal");
			    }
			    else{
                            writein_file.write(",");
				Sret = '<'+INM.get(i).op_code+','+INM.get(i).destination_value+','+INM.get(i).f_fs+','+INM.get(i).s_fs+'>';
			
                        writein_file.write(Sret);
                        }
				//System.out.print(Sret);
                                
				
		}
		writein_file.newLine();
		
		//System.out.print("\nINB:");
                
                writein_file.write("INB:");
                if(0<INB.size()){
                    if(INB.get(0).op_code.equals("null")){
		    Sret = "";
		    }
		    else{
			Sret = '<'+INB.get(0).op_code+','+INB.get(0).destination_value+','+INB.get(0).f_fs+','+INB.get(0).s_fs+'>';
			}
			//System.out.print(Sret);
			writein_file.write(Sret);
                }
		for(int i =1; i<INB.size();i++){
			if(INB.get(i).op_code.equals("null")){
		    Sret = "";
		    }
		    else{writein_file.write(",");
			Sret = '<'+INB.get(i).op_code+','+INB.get(i).destination_value+','+INB.get(i).f_fs+','+INB.get(i).s_fs+'>';
			}
			//System.out.print(Sret);
			writein_file.write(Sret);
		}
		writein_file.newLine();
		
		
		//System.out.print("\nAIB:");
		writein_file.write("AIB:");
                if(0<AIB.size()){
                if(AIB.get(0).op_code.equals("null")){
			    Sret = "";
			    }
			    else{
				Sret = '<'+AIB.get(0).op_code+','+AIB.get(0).destination_value+','+AIB.get(0).f_fs+','+AIB.get(0).s_fs+'>';
				}
				//System.out.print(Sret);
				writein_file.write(Sret);
                }
		for(int i =1; i<AIB.size();i++){
			if(AIB.get(i).op_code.equals("null")){
			    Sret = "";
			    }
			    else{writein_file.write(",");
				Sret = '<'+AIB.get(i).op_code+','+AIB.get(i).destination_value+','+AIB.get(i).f_fs+','+AIB.get(i).s_fs+'>';
				}
				//System.out.print(Sret);
				writein_file.write(Sret);
		}
		writein_file.newLine();
		
		
		//System.out.print("\nSIB:");
		writein_file.write("SIB:");
                if(0<SIB.size()){
                    
                    if(SIB.get(0).op_code.equals("null")){
			    Sret = "";
			    }
			    else{
				Sret = '<'+SIB.get(0).op_code+','+SIB.get(0).destination_value+','+SIB.get(0).f_fs+','+SIB.get(0).s_fs+'>';
				}
				//System.out.print(Sret);
				writein_file.write(Sret);
                    
                }
		for(int i =1; i<SIB.size();i++){
			if(SIB.get(i).op_code.equals("null")){
			    Sret = "";
			    }
			    else{writein_file.write(",");
				Sret = '<'+SIB.get(i).op_code+','+SIB.get(i).destination_value+','+SIB.get(i).f_fs+','+SIB.get(i).s_fs+'>';
				}
				//System.out.print(Sret);
				writein_file.write(Sret);
		}
		writein_file.newLine();
		
		
		//System.out.print("\nPRB:");
		writein_file.write("PRB:");
                if(0<PRB.size()){
                if(PRB.get(0).op_code.equals("null")){
			    Sret = "";
			    }
			    else{
				Sret = '<'+PRB.get(0).op_code+','+PRB.get(0).destination_value+','+PRB.get(0).f_fs+','+PRB.get(0).s_fs+'>';
				}
				//System.out.print(Sret);
				writein_file.write(Sret);
                }
		for(int i =1; i<PRB.size();i++){
			if(PRB.get(i).op_code.equals("null")){
			    Sret = "";
			    }
			    else{
                            writein_file.write(",");
				Sret = '<'+PRB.get(i).op_code+','+PRB.get(i).destination_value+','+PRB.get(i).f_fs+','+PRB.get(i).s_fs+'>';
				}
				//System.out.print(Sret);
				writein_file.write(Sret);
		}
		writein_file.newLine();
		
		
		//System.out.print("\nADB:");
		writein_file.write("ADB:");
                if(0<ADB.size()){
                if(ADB.get(0).regsnm.equals("null")){
		    	Sret = "";
		    }
		    else{
			Sret = '<'+ADB.get(0).regsnm+','+ADB.get(0).value_reg+'>';
			
		    }
			//System.out.print(Sret);
			writein_file.write(Sret);
                }
		for(int i =1; i<ADB.size();i++){
			if(ADB.get(i).regsnm.equals("null")){
		    	Sret = "";
		    }
		    else{ writein_file.write(",");
			Sret = '<'+ADB.get(i).regsnm+','+ADB.get(i).value_reg+'>';
			
		    }
			//System.out.print(Sret);
			writein_file.write(Sret);
		}
		writein_file.newLine();
		
		//System.out.print("\nREB:");
		writein_file.write("REB:");
                if(0<REB.size()){
                if(REB.get(0).regsnm.equals("null")){
		    	Sret = "";
		    }
		    else{
			Sret = '<'+REB.get(0).regsnm+','+REB.get(0).value_reg+'>';
			
		    }
			//System.out.print(Sret);
			writein_file.write(Sret);
                }
		for(int i =1; i<REB.size();i++){
			if(REB.get(i).regsnm.equals("null")){
		    	Sret = "";
		    }
		    else{
                            writein_file.write(",");
			Sret = '<'+REB.get(i).regsnm+','+REB.get(i).value_reg+'>';
			
		    }
			//System.out.print(Sret);
			writein_file.write(Sret);
		}
		writein_file.newLine();
		
		
		//System.out.print("\nRGF:");
		writein_file.write("RGF:");
                
                int k=0;
               if(k<RGF.size()){
                  if(RGF.get(k).regsnm.equals("null")){
		    	Sret = "";
		    }
		    else{
			Sret = '<'+RGF.get(k).regsnm+','+RGF.get(k).value_reg+'>';
			
		    }
			//System.out.print(Sret);
			writein_file.write(Sret);
                   
               }
		for(int i =1; i<RGF.size();i++){
			if(RGF.get(i).regsnm.equals("null")){
		    	Sret = "";
		    }
		    else{
                             writein_file.write(",");
			Sret = '<'+RGF.get(i).regsnm+','+RGF.get(i).value_reg+'>';
			
		    }
			//System.out.print(Sret);
			writein_file.write(Sret);
		}
		writein_file.newLine();
		
		//System.out.print("\nDAM:");
		writein_file.write("DAM:");
                int l=0;
                if(l<DAM.size()){
                    if(String.valueOf(DAM.get(l).add_dt).equals("null")){
		    	Sret = "";
		    }
		    else{
			Sret = "<"+DAM.get(l).add_dt+","+DAM.get(l).val_dt+">";
			
		    }
			//System.out.print(Sret);
			writein_file.write(Sret);
                }
                
		for(int i =1; i<DAM.size();i++){
			if(String.valueOf(DAM.get(i).add_dt).equals("null")){
		    	Sret = "";
		    }
		    else{
                            writein_file.write(",");
			Sret = "<"+DAM.get(i).add_dt+","+DAM.get(i).val_dt+">";
			
		    }
			//System.out.print(Sret);
			writein_file.write(Sret);
		}
		writein_file.newLine();
		
		}
		
		
		catch(Exception e){
			System.out.println(e);
		}
		
	}
	
}

public class MIPSsim {
	public static void main(String[]args) throws IOException{
		Simulator sim1 = new Simulator();
                sim1.Input_Instructions();	
                sim1.Input_Registers();	
                sim1.Input_Datamemory();
		sim1.main_task();
	}
}


