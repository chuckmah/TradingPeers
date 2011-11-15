package models;

public class CommunityStat {

	public Community community;
	
	public Integer usersTotal;

	
	public CommunityStat(Community community){
		this.community = community;
		this.usersTotal = new Integer(0);

	}
	
}
