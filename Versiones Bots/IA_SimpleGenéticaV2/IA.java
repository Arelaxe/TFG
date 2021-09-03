import aiinterface.AIInterface; 

import java.util.Random;
import struct.FrameData;
import struct.GameData;
import struct.Key;
import aiinterface.CommandCenter;
import enumerate.State;
import java.io.FileWriter;
import java.io.PrintWriter;

public class IA implements AIInterface{
	Key inputKey;
	Random rnd;
	FrameData frameData;
	GameData gameData;
	CommandCenter cc; 
	boolean playerNumber;
	boolean otherPlayer;
	GA ga = new GA(this);
	public int[] genotype;
	FileWriter escribir = null;
	PrintWriter pw = null;
	Individual asociado;
	int hijo_testeado;
	
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
		this.gameData = gameData;
		this.otherPlayer = !this.playerNumber;
		genotype = new int[4];
		
		ga.gaNextPhase(this);
		
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
				else if(frameData.getDistanceX() > genotype[3]) {
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
		try{
			escribir = new FileWriter("resultados.txt",true);
			pw = new PrintWriter(escribir);
			pw.println(frameData.getCharacter(playerNumber).getHp() + " " + frameData.getCharacter(otherPlayer).getHp());
		}catch (Exception ex2){
			System.out.println("Fallo de fichero resultados\n");
		}finally{
			try{
				if (null!=escribir)
					escribir.close();
					pw.close();
			} catch (Exception e2){
				e2.printStackTrace();
			}
		}
		
		
		System.out.println("Resultado: " + " " + frameData.getCharacter(playerNumber).getHp() + " " + frameData.getCharacter(otherPlayer).getHp());
		
		if (frameData.getRound() == 3) {
			System.out.println("Se ha probado: " + genotype[0] + " " + genotype[1] + " " + genotype[2] + " " + genotype[3]);
			try{
				ga.num_combate_individuo++;
				if (ga.num_combate_individuo == GA.NUM_BATTLES_INDIVIDUAL) {
					if (ga.num_fase ==  1) {
						ga.population.get(ga.num_individuo).fitness();
						ga.num_individuo++;
					}
					else {
						String nombre_fichero;
						if (hijo_testeado == 1) nombre_fichero = "hijo1.txt";
						else nombre_fichero = "hijo2.txt";
						
						try {
		        			escribir = new FileWriter(nombre_fichero,true);
		        			pw = new PrintWriter(escribir);
		        			pw.println(asociado.fitness());
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
					
					ga.num_combate_individuo = 0;
					
					if (ga.num_fase == 1 && ga.num_individuo == GA.NUM_INDIVIDUALS) {
		    			ga.num_fase = 2;
		    		}
					
					try{
						escribir = new FileWriter("resultados.txt",false);
						pw = new PrintWriter(escribir);
						pw.print("");
					}catch (Exception ex2){
						System.out.println("Fallo de fichero resultados p2\n");
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
				
				escribir = new FileWriter("poblacion.txt",false);
				pw = new PrintWriter(escribir);
				pw.println(ga.num_fase + " " + ga.num_individuo + " " + ga.num_combate_individuo + " " + ga.num_generacion);
				
				for (int i = 0; i < ga.population.size(); i++) {
					pw.println(ga.population.get(i).genotype[0] + " " + ga.population.get(i).genotype[1] + " " + 
							   ga.population.get(i).genotype[2] + " " + ga.population.get(i).genotype[3] + " " +
							   ga.population.get(i).fitness);
				}
			}catch (Exception ex2){
				System.out.println("Fallo de fichero población\n");
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
	}
	
	public String getCharacter(){
		return "CHARACTER_ZEN";
	}

}
