package controllers;

import play.*;

import play.modules.siena.SienaFixtures;
import play.mvc.*;
import play.test.Fixtures;
import services.PortfolioServices;
import services.QuoteServices;
import services.ServicesException;
import services.financeinfo.QuoteInfo;

import java.io.ByteArrayInputStream;
import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.yaml.snakeyaml.Yaml;


import controllers.Secure.Security;

import models.*;
import models.Portfolio.WatchQuote;

@With(Secure.class)
public class Home extends Controller {


	
		public static void index(){
			redirect("MyPortfolio.index");
		}

}