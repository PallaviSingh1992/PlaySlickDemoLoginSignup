
package controllers
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import javax.inject._
import models.{UserForm, User}
import play.api._
import play.api.mvc._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import services.UserService


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject() extends Controller {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
//  def index = Action {
//    Ok(views.html.index("Your new application is ready."))
//  }

   def index=Action.async{implicit request =>
    UserService.listUsers.map{
      users=>Ok(views.html.index(UserForm.form,users))
    }
  }

  def addUser()=Action.async{ implicit request =>
    UserForm.form.bindFromRequest.fold(
      errorForm=> Future.successful(Ok(views.html.index(errorForm,Seq.empty[User]))),
      dataForm =>{
        val newUser=User(0,dataForm.name,dataForm.email,dataForm.password)
        UserService.addUser(newUser).map(result=>Redirect(routes.HomeController.index()))
      }
    )
  }

  def deleteUser(id:Int)=Action.async{implicit request=>
   UserService.deleteUser(id).map{ result=>
     Redirect(routes.HomeController.index())
   }

  }

}
