package controllers.admin;

import play.mvc.With;
import controllers.CRUD.For;
import controllers.Check;
import controllers.CRUD;
import controllers.Secure;

@For(models.PortfolioTransaction.class)
@With(Secure.class)
@Check("admin")
public class PortfolioTransactions extends controllers.CRUD{

}
