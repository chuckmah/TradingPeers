package controllers.admin;


import play.mvc.With;
import controllers.CRUD.For;
import controllers.Check;
import controllers.CRUD;
import controllers.Secure;

@For(models.Community.class)
@With(Secure.class)
@Check("admin")
public class Communities extends controllers.CRUD{

}
