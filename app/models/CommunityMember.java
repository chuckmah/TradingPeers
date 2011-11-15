package models;

import java.util.List;

import siena.*;



@Table("communitiesMembers")
public class CommunityMember extends Model{

	@Id(Generator.AUTO_INCREMENT)
    public Long id;
	
	@Column("community")
    public Community community;
	
	@Column("user")
    public User user;
	
	
	public CommunityMember(User user, Community community) {
		this.user = user;
		this.community = community;
	}

	
	public static List<CommunityMember> findByCommunity(Community community){
		return CommunityMember.all(CommunityMember.class).filter("community", community).fetch();
		
	}
	

	public static CommunityMember findByUserAndCommunity(Community community,User user){
		return CommunityMember.all(CommunityMember.class).filter("community", community).filter("user", user).get();
		
	}


	public static List<CommunityMember> findByUser(User user) {
		return CommunityMember.all(CommunityMember.class).filter("user", user).fetch();
	}
}
