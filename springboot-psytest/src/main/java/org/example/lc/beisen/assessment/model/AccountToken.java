package org.example.lc.beisen.assessment.model;

public class AccountToken {
    
	public AccountToken(String tenantid,String token1,String tokensecret,String beisenaccount)
	{
		tenant_id = tenantid;	
		token = token1;
		token_secret = tokensecret;
		beisen_account = beisenaccount;
	}
	
   public String tenant_id; 
   public String token ;  
   public String token_secret ;  	   
   public String beisen_account ;  
}
