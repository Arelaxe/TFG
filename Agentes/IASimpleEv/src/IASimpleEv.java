import aiinterface.AIInterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Random;
import struct.FrameData;
import struct.GameData;
import struct.Key;
import aiinterface.CommandCenter;
import enumerate.State;

public class IASimpleEv implements AIInterface {
	Key inputKey;
	Random rnd;
	FrameData frameData;
	CommandCenter cc; 
	boolean playerNumber;
	boolean otherPlayer;
	
	// Para la correcta ejecución del Algoritmo Genético
	int[] genotype;
	int individualNumber;
	int generationNumber;
	
	// Declaración de variables para leer/escribir archivos
	FileWriter escribir = null;
	PrintWriter pw = null;
	
	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public int initialize(GameData gameData, boolean playerNumber) {
		this.playerNumber = playerNumber;
		this.inputKey = new Key();
		cc = new CommandCenter();
		frameData = new FrameData();
		this.otherPlayer = !this.playerNumber;
		
		genotype = new int [5];
		
		File file = null;
		FileReader fr = null;
		BufferedReader br = null;
		
		try {
			file = new File ("IndividuoAEvaluar.txt");
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String line = br.readLine();
			String[] genotypeString = line.split(" ");
			
			for (int i = 0; i < 4; i++) {
				genotype[i] = Integer.parseInt(genotypeString[i]);
			}
			
		} catch (Exception ex){
			ex.printStackTrace();
		} finally{
			try {
				if (null != fr){
					fr.close();
					br.close();
				}
			} catch (Exception ex2){
				System.out.println("Fallo de fichero\n");
			}
		}
		
		try {
	        escribir = new FileWriter("C:\\Users\\n-o-e\\OneDrive\\Escritorio\\IA'S\\LANZADOR\\GameData.txt",false);
	        pw = new PrintWriter(escribir);
	        pw.println(gameData.getStageWidth());
	    } catch (Exception ex2){
	        System.out.println("Fallo de fichero\n");
	    }finally{
	    	try{
	    		if (null!=escribir)
	        		escribir.close();
	        		pw.close();
	        } catch (Exception e2){
	        		e2.printStackTrace();
	        }
	     }
		
		System.out.println("hola");
		return 0;
	}
	
	@Override
	public void getInformation(FrameData frameData, boolean arg1) {
		this.frameData = frameData;
		cc.setFrameData(this.frameData, playerNumber);
	}

	@Override
	public Key input() {
		// TODO Auto-generated method stub
		return inputKey;
	}

	@Override
	public void processing() {
		if(!frameData.getEmptyFlag() && frameData.getRemainingFramesNumber()>0) {
			if(cc.getSkillFlag()) {
				inputKey = cc.getSkillKey();
			}
			else {
				inputKey.empty();
				cc.skillCancel();
				if(frameData.getDistanceX() < genotype[0]) {
					cc.commandCall("THROW_A");
				}
				else if(frameData.getDistanceX() > genotype[0] && frameData.getDistanceX() < genotype[1]) {
					if(frameData.getDistanceY() > 0) {
						cc.commandCall("AIR_A");
					}
					else if (frameData.getCharacter(this.otherPlayer).getState() == State.CROUCH) {
						cc.commandCall("AIR _B");
					}
					else {
						cc.commandCall("STAND_A");
					}
				}
				else if(frameData.getDistanceX() > genotype[1] && frameData.getDistanceX() < genotype[2]) {
					if(frameData.getDistanceY() > 0) {
						cc.commandCall("AIR _B");
					}
					else if (frameData.getCharacter(this.otherPlayer).getState() == State.CROUCH) {
						cc.commandCall("AIR _B");
					}
					else {
						cc.commandCall("STAND_B");
					}
					
				}
				else if(frameData.getDistanceX() > genotype[2] && frameData.getDistanceX() < genotype[3]) {
					if(frameData.getDistanceY() > 0) {
						cc.commandCall("AIR _B");
					}
					else {
						cc.commandCall("2 3 6 _ A");
					}
				}
				else if(frameData.getDistanceX() > genotype[4]) {
					cc.commandCall("6");
				}
				else {
					cc.commandCall("AIR_A");
				}
			}
		}

	}

	@Override
	public void roundEnd(int arg0, int arg1, int arg2) {
		System.out.println(frameData.getCharacter(playerNumber).getHp() + " " + frameData.getCharacter(otherPlayer).getHp()
        		+ " " + frameData.getRemainingTimeMilliseconds());
		try {
	        escribir = new FileWriter("C:\\Users\\n-o-e\\OneDrive\\Escritorio\\IA'S\\LANZADOR\\Evaluado.txt",true);
	        pw = new PrintWriter(escribir);
	        pw.println(frameData.getCharacter(playerNumber).getHp() + " " + frameData.getCharacter(otherPlayer).getHp()
	        		+ " " + frameData.getRemainingTimeMilliseconds());
	    } catch (Exception ex2){
	        System.out.println("Fallo de fichero\n");
	    }finally{
	    	try{
	    		if (null!=escribir)
	        		escribir.close();
	        		pw.close();
	        } catch (Exception e2){
	        		e2.printStackTrace();
	        }
	     }
	}
	
	public String getCharacter(){
		return "CHARACTER_ZEN";
	}

}
