package controllers;

import java.io.ByteArrayInputStream;

import models.Portfolio;
import models.User;

import org.yaml.snakeyaml.Yaml;

import play.Logger;
import play.modules.siena.SienaFixtures;
import play.mvc.Controller;


public class DataLoader extends Controller {

    public static void yaml(long portfolioId) {
    	

       	Portfolio portfolio = Portfolio.all(Portfolio.class).filter("id", new Long(137)).get();
       	User user = portfolio.user;
       	user.get();
        Yaml yaml = new Yaml();
        String output = yaml.dump(portfolio);
        renderBinary(new ByteArrayInputStream(output.getBytes()));
	
    }
    

    public static void dataLoad() {

    	
        String fileName = "data.yml";
        try {
        	SienaFixtures.deleteAllModels();
			SienaFixtures.loadModels(fileName);
	        Logger.info("data.yml Loaded!");
		} catch (Exception e) {
			e.printStackTrace();
		}

    }
	
}
