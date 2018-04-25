/* On my honor, I have neither given nor received unauthorized aid on this assignment */

import java.util.*;
import java.io.*;

class Simulator {
	/////////////////////////////////VARIABLES DECLARED START///////////////////////////////////	
	String sCurrentLine; 
	BufferedReader br1 = null;	
	BufferedReader br2 = null;
	BufferedReader br3 = null;
	
	File Outfile;
	File Instfile;
	File Regfile;
	File Datafile;
	
	FileWriter filewriter;
	BufferedWriter writer; 
				
	ArrayList<Instruction> INM = new ArrayList<Instruction>();
	ArrayList<Register> RGF = new ArrayList<Register>();
	ArrayList<DataMemory> DAM = new ArrayList<DataMemory>();
	ArrayList<Instruction> INB = new ArrayList<Instruction>();
	ArrayList<Instruction> AIB = new ArrayList<Instruction>();
	ArrayList<Instruction> SIB = new ArrayList<Instruction>();
	ArrayList<Instruction> PRB = new ArrayList<Instruction>();
	ArrayList<Register> ADB = new ArrayList<Register>();
	ArrayList<Register> REB = new ArrayList<Register>();
	
	Instruction in = null;
	Register rg = null;
	DataMemory dm = null;
	//////////////////////////////////////////////VARIABLES DECLARED END/////////////////////////////
	
	///////////////////////////////////TAKING INPUT FROM FILE METHOD START///////////////////////
	public void TakeInput(){
		try{
				
		Instfile = new File("instructions.txt");	
		br1 = new BufferedReader(new FileReader(Instfile.getAbsolutePath()));

		while ((sCurrentLine = br1.readLine()) != null) {
			int m = sCurrentLine.indexOf(',');
			int l = sCurrentLine.length();
			
			
			String commandstring = sCurrentLine.substring(1, m);
			
			sCurrentLine = sCurrentLine.substring(m+1, l);
			m = sCurrentLine.indexOf(',');
			
			
			String deststring = sCurrentLine.substring(0, m);
			
			l = sCurrentLine.length();
			sCurrentLine = sCurrentLine.substring(m+1, l);
			m = sCurrentLine.indexOf(',');
			String source1string = sCurrentLine.substring(0, m);
			
			l = sCurrentLine.length();
			String source2string = sCurrentLine.substring(m+1, l-1);
		  
			//Print(s1,s2,s3,s4);
			in = new Instruction(commandstring,deststring,source1string,source2string);
			//ins.write();
			INM.add(in);
		}
		
		Regfile = new File("registers.txt");
		br2 = new BufferedReader(new FileReader(Regfile.getAbsolutePath()));
		  
		  while((sCurrentLine = br2.readLine())!= null){
			  int m2 = sCurrentLine.indexOf(',');
			  int l2 = sCurrentLine.length();
			  
			  String regs = sCurrentLine.substring(1, m2);
			  String regv = sCurrentLine.substring(m2+1, l2-1);
			  int a = Integer.parseInt(regv);
			  
			  rg = new Register(regs,a);
			  RGF.add(rg);
		
			  }
		  Datafile = new File("datamemory.txt");
		  br3 = new BufferedReader(new FileReader(Datafile.getAbsolutePath()));
		  while((sCurrentLine = br3.readLine())!= null)
		  {
			  int m3 = sCurrentLine.indexOf(',');
			  int l3 = sCurrentLine.length();
			  int add = Integer.parseInt(sCurrentLine.substring(1, m3));
			  int val = Integer.parseInt(sCurrentLine.substring(m3+1, l3-1));

			  dm = new DataMemory(add,val);
			  DAM.add(dm);
			}
		}catch(Exception e){System.out.print(e);}

	}
	////////////////////////////////TAKING INPUT FROM FILE METHOD END///////////////////////////
	
	
	
	
	/////////////////////////////////MAIN WORKER OF THE SIMULATOR CALLS OTHER METHODS//////////
	public void worker() throws IOException {
		
		
		try{
			
		  TakeInput();	
		  Outfile = new File("output.txt");
		  if (!Outfile.exists()) {
				try {
					Outfile.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
				filewriter = new FileWriter(Outfile.getAbsoluteFile());
				writer = new BufferedWriter(filewriter);
				
				
				//System.out.print("\nStep0:");
				writer.write("Step 0:");
				writer.newLine();
				PrintQueues();//prints current status of all the lists
				
				
				///////////////////MAIN LOOP STARTS///////////
				/* checks the emptiness of each buffer and calls each function simultaneously*/
				int i=1;
				while(!INM.isEmpty() || !REB.isEmpty() || !INB.isEmpty() ||
					  !AIB.isEmpty() || !SIB.isEmpty() || !PRB.isEmpty() ||
					  !ADB.isEmpty())
				{
								
				//System.out.print("\n\nStep "+i+":");
				writer.newLine();
				writer.write("\n\nStep "+i+":");
				writer.newLine();
				if (!REB.isEmpty())
					WriteUnit(REB.remove(0));
				if (!ADB.isEmpty())
					Store(ADB.remove(0));
				if(!PRB.isEmpty())
					MultiplyUnit(PRB.remove(0));
				//if(!REB.isEmpty())
				//	Write(REB.removeFirst());
				if (!SIB.isEmpty())
					PunchAddress(SIB.remove(0));
				if (!AIB.isEmpty())
					AddSubUnit(AIB.remove(0));
				if (!INB.isEmpty())
					IssueInst(INB.remove(0));
				if (!INM.isEmpty())
					ReadNDecode(INM.remove(0));
				PrintQueues();
					i++;
				}
				/////MAIN LOOP ENDS////////////
		}
		catch(Exception e){
			System.out.println(e);
		}
		finally{
			br1.close();
			br2.close();
			br3.close();
			writer.close();
	}}
	
	
	////////READ N DECODES THE INCOMING INST//////////////
	public void ReadNDecode(Instruction in){
		if (in.opcode.equalsIgnoreCase("ADD") || in.opcode.equalsIgnoreCase("MUL") || in.opcode.equalsIgnoreCase("SUB")){
			for (int i = 0; i<RGF.size() ; i++){
				if (RGF.get(i).regname.equals(in.fso))
					in.fso = String.valueOf(RGF.get(i).regval);
				if (RGF.get(i).regname.equals(in.sso))
					in.sso = String.valueOf(RGF.get(i).regval);
			}
		}
		else{
			for (int i = 0; i<RGF.size() ; i++){
				if (RGF.get(i).regname.equals(in.fso))
					in.fso = String.valueOf(RGF.get(i).regval);
			}
		}
		INB.add(in);	
	}
	//////////READ N DECODE ENDS//////////////////////
	
	/////////ISSUE METHOD START//////////
	public void IssueInst(Instruction in){
		if (in.opcode.equalsIgnoreCase("ADD") || in.opcode.equalsIgnoreCase("MUL") || in.opcode.equalsIgnoreCase("SUB")){
			AIB.add(in);
		}
		else 
			SIB.add(in);
	}
	//////////ISSUE METHOD ENDS/////////
	
	//////////////////ADD SUB UNIT////////
	public void AddSubUnit(Instruction in){
		Register r = new Register() ;
		if (in.opcode.equalsIgnoreCase("ADD")){
			r.regval = Integer.parseInt(in.fso) + Integer.parseInt(in.sso);
			r.regname = in.dest;
			REB.add(r);
			//AIB.opcode = "null";
		}
		if (in.opcode.equalsIgnoreCase("SUB")){
			r.regval = Integer.parseInt(in.fso) - Integer.parseInt(in.sso);
			r.regname = in.dest;
			REB.add(r);
			//AIB.opcode = "null";
		}
		if (in.opcode.equalsIgnoreCase("MUL")){
			PRB.add(in);
			//AIB.opcode = "null";
		}
	}
	/////////////////////////ADD SUB UNIT END//////

	///////////////MULTIPLY UNIT////////
	public void MultiplyUnit(Instruction prb){
		Register rebtemp = new Register();
		rebtemp.regval =  Integer.parseInt(prb.fso) * Integer.parseInt(prb.sso);
		rebtemp.regname = prb.dest;
		REB.add(rebtemp);
		
	}
	//////////////MULTIPLY UNIT ENDS//////
	
	
	//////////////ADDRESS WRITER////////
	public void PunchAddress(Instruction sib){
		Register tempadb = new Register();
		tempadb.regval =  Integer.parseInt(sib.fso) + Integer.parseInt(sib.sso);
		tempadb.regname = sib.dest;
		ADB.add(tempadb);
		
	}
	/////////////ADDRESS WRITER ENDS////////
	
	
	/////////////GETS POSITION OF A PARTICULAR MEMORY LOCATION/////
	public int DAMpos(DataMemory dat){
		for (int i = 0 ; i < DAM.size() ; i++){
			if (dat.dataadd < DAM.get(i).dataadd)
				return i;
		}
		return 0;
	}
	//ENDS//
	
	/////////////STORES A NEW REGISTER////
	public void Store(Register adb){
		boolean found = false;
		int rval = 0;
		DataMemory d = new DataMemory();
		for (int i = 0; i<RGF.size() ; i++){
			if (RGF.get(i).regname.equalsIgnoreCase(adb.regname)){
				rval = RGF.get(i).regval;
			}
		}
		
		for (int i = 0 ; i<DAM.size() ; i++){
			if (DAM.get(i).dataadd == adb.regval){
				DAM.get(i).dataval = rval;
				found = true;
			}
		}
		if (!found){
			d.dataadd = adb.regval;
			d.dataval = rval;
			DAM.add(DAMpos(d), d);
		}
		
		//ADB.regname = "null";	
		
	}
	////ENDS///
	
	
	/////////GETS INDEX OF A PARTICULAR REGISTER IN MEMORY////
	public int getPos(Register temp){
		
		String t,t1;
		int pos1;
		t = temp.regname;
		t = t.substring(1, t.length());
		int pos = Integer.parseInt(t);
		for(int i=0;i<RGF.size();i++){
			t1 = RGF.get(i).regname;
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
	public void WriteUnit(Register reb)
	{
		/*String t = reb.regname;
		t = t.substring(1, t.length());
		//System.out.print("regpos:"+reb.regname);
		*/
		int flag=0;
		for(int i=0; i<RGF.size() ;i++){

			if(reb.regname.equalsIgnoreCase(RGF.get(i).regname)){
				RGF.get(i).regval = reb.regval;
				flag=1;
			}
		}
		int pos = getPos(reb);
		if(flag==0){
			RGF.add(pos, reb);
			//RGF.add(reb);
		}
	}
	//////ENDS//////////

	
	/////WRITES THE CURRENT STATUS OF THE QUEUES IN THE FILE////////
	public void PrintQueues() throws IOException{
		try{
		String Sret;
			
		//System.out.print("\nINM:");
		writer.write("\nINM:");
		for(int i =0; i<INM.size();i++){
			if(INM.get(i).opcode.equals("null")){
			    Sret = "";
			    }
			    else{
				Sret = '<'+INM.get(i).opcode+','+INM.get(i).dest+','+INM.get(i).fso+','+INM.get(i).sso+'>';
				}
				//System.out.print(Sret);
				writer.write(Sret);
		}
		writer.newLine();
		
		//System.out.print("\nINB:");
		writer.write("\nINB:");
		for(int i =0; i<INB.size();i++){
			if(INB.get(i).opcode.equals("null")){
		    Sret = "";
		    }
		    else{
			Sret = '<'+INB.get(i).opcode+','+INB.get(i).dest+','+INB.get(i).fso+','+INB.get(i).sso+'>';
			}
			//System.out.print(Sret);
			writer.write(Sret);
		}
		writer.newLine();
		
		
		//System.out.print("\nAIB:");
		writer.write("\nAIB:");
		for(int i =0; i<AIB.size();i++){
			if(AIB.get(i).opcode.equals("null")){
			    Sret = "";
			    }
			    else{
				Sret = '<'+AIB.get(i).opcode+','+AIB.get(i).dest+','+AIB.get(i).fso+','+AIB.get(i).sso+'>';
				}
				//System.out.print(Sret);
				writer.write(Sret);
		}
		writer.newLine();
		
		
		//System.out.print("\nSIB:");
		writer.write("\nSIB:");
		for(int i =0; i<SIB.size();i++){
			if(SIB.get(i).opcode.equals("null")){
			    Sret = "";
			    }
			    else{
				Sret = '<'+SIB.get(i).opcode+','+SIB.get(i).dest+','+SIB.get(i).fso+','+SIB.get(i).sso+'>';
				}
				//System.out.print(Sret);
				writer.write(Sret);
		}
		writer.newLine();
		
		
		//System.out.print("\nPRB:");
		writer.write("\nPRB:");
		for(int i =0; i<PRB.size();i++){
			if(PRB.get(i).opcode.equals("null")){
			    Sret = "";
			    }
			    else{
				Sret = '<'+PRB.get(i).opcode+','+PRB.get(i).dest+','+PRB.get(i).fso+','+PRB.get(i).sso+'>';
				}
				//System.out.print(Sret);
				writer.write(Sret);
		}
		writer.newLine();
		
		
		//System.out.print("\nADB:");
		writer.write("\nADB:");
		for(int i =0; i<ADB.size();i++){
			if(ADB.get(i).regname.equals("null")){
		    	Sret = "";
		    }
		    else{
			Sret = '<'+ADB.get(i).regname+','+ADB.get(i).regval+'>';
			
		    }
			//System.out.print(Sret);
			writer.write(Sret);
		}
		writer.newLine();
		
		//System.out.print("\nREB:");
		writer.write("\nREB:");
		for(int i =0; i<REB.size();i++){
			if(REB.get(i).regname.equals("null")){
		    	Sret = "";
		    }
		    else{
			Sret = '<'+REB.get(i).regname+','+REB.get(i).regval+'>';
			
		    }
			//System.out.print(Sret);
			writer.write(Sret);
		}
		writer.newLine();
		
		
		//System.out.print("\nRGF:");
		writer.write("\nRGF:");
		for(int i =0; i<RGF.size();i++){
			if(RGF.get(i).regname.equals("null")){
		    	Sret = "";
		    }
		    else{
			Sret = '<'+RGF.get(i).regname+','+RGF.get(i).regval+'>';
			
		    }
			//System.out.print(Sret);
			writer.write(Sret);
		}
		writer.newLine();
		
		//System.out.print("\nDAM:");
		writer.write("\nDAM:");
		for(int i =0; i<DAM.size();i++){
			if(String.valueOf(DAM.get(i).dataadd).equals("null")){
		    	Sret = "";
		    }
		    else{
			Sret = "<"+DAM.get(i).dataadd+","+DAM.get(i).dataval+">";
			
		    }
			//System.out.print(Sret);
			writer.write(Sret);
		}
		writer.newLine();
		
		}
		
		////WRITER FUNCTION ENDS////////////
		catch(Exception e){
			System.out.println(e);
		}
		
	}
	
}



class Instruction{
	
	String opcode;
	String dest;
	String fso;
	String sso;
	
	Instruction(){
	}
	
	Instruction(String s1,String s2,String s3,String s4){
		opcode = s1;
		dest = s2;
		fso = s3;
		sso = s4;
	}
	public void write(){
		System.out.println(opcode+sso);
	}
}

class Register{
	
	String regname;
	int regval;
	
	Register(){
	}
	
	Register(String s1,int i){
		regname = s1;
		regval = i;
	}
}

class DataMemory{
	
	int dataadd;
	int dataval;
	
	DataMemory(){
	}
	
	DataMemory(int a,int v){
		dataadd = a;
		dataval = v;
	}
}

public class MIPSsim {
	public static void main(String[]args) throws IOException{
		
		new Simulator().worker();
	}
}



/*	public String InsFormat(String s1,String s2,String s3,String s4){
		String S;
		
		S = '<'+s1+','+s2+','+s3+','+s4+'>';
		return S;
	}*/
