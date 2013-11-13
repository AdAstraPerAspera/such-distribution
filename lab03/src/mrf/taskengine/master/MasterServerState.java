package mrf.taskengine.master;

public class MasterServerState {
	private MasterSettings setting;
	
	public MasterServerState(MasterSettings setting){
		this.setting = setting;
	}
	
	public String getSetting(String s){
		return setting.getValue(s);
	}
}
