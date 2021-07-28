import aiinterface.AIInterface; 
import java.util.Random;
import struct.FrameData;
import struct.GameData;
import struct.Key;
import aiinterface.CommandCenter;
import enumerate.State;

public class IA implements AIInterface{
	Key inputKey;
	Random rnd;
	FrameData frameData;
	CommandCenter cc; 
	boolean playerNumber;
	boolean otherPlayer;
	GA ga = new GA(this);
	public int[] genotype;
	
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
		genotype = new int[4];
        
		genotype = ga.population.get(ga.population.size()-1).genotype;
		
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
		System.out.println("Vida IA: " + frameData.getCharacter(playerNumber).getHp());
		System.out.println("Vida Enemigo: " + frameData.getCharacter(otherPlayer).getHp());
		System.out.println("Fitness: " + ga.population.get(ga.population.size()-1).fitness());
		ga.produceNextGen(this);
		genotype = ga.population.get(ga.population.size()-1).genotype;
	}
	
	public String getCharacter(){
		return "CHARACTER_ZEN";
	}

}
