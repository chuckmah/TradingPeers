package controllers.admin;


import play.mvc.With;
import controllers.Secure;
import controllers.CRUD.For;
import controllers.Check;

@For(models.Quote.class)
@With(Secure.class)
@Check("admin")
public class Quotes  extends controllers.CRUD{

}
