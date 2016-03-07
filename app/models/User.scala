package models

import play.api.data._
import play.api.data.Forms._
import slick.driver.MySQLDriver.api._
import scala.concurrent.{Future, Await}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global



case class User(id:Int,name:String,email:String,password:String)

case class UserFormData(name:String,email:String,password:String,repeatPassword:String)


object UserForm{

  val form=Form(
    mapping(
      "name"->nonEmptyText,
      "email"->email,
      "password"->nonEmptyText,
      "repeatPassword"->nonEmptyText
    )(UserFormData.apply)(UserFormData.unapply)
  )
}

class UserTableDef(tag: Tag) extends Table[User](tag, "user") {
  val id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  val name = column[String]("name", O.SqlType("VARCHAR(100)"))
  val email = column[String]("email", O.SqlType("VARCHAR(200)"))
  val password = column[String]("password", O.SqlType("VARCHAR(20)"))
  def * = (id, name, email, password) <> ((User.apply _).tupled, User.unapply)
}

object User{


  val userInfo=TableQuery[UserTableDef]

  val db=Database.forConfig("mysql")

  //val createStatement=userInfo.schema.create

  //val futureResult=db.run{ createStatement}

 // val sqlresult=Await.result(futureResult,10 second)

  //println("Result:  "+sqlresult)



   def add(user:User):Future[String]={
     val createStatement=userInfo.schema.create
     val futureResult=db.run{ createStatement}

     db.run(userInfo+=user).map(result=>"User Successfully Added").recover{
       case res:Exception=>res.getCause.getMessage
     }
   }
  def delete(id: Int): Future[Int] = {
      db.run(userInfo.filter(_.id === id).delete)
  }

  def get(id: Int): Future[Option[User]] = {
    db.run(userInfo.filter(_.id === id).result.headOption)
  }

  def listAll: Future[Seq[User]] = {
    db.run(userInfo.result)
  }



}



//
//object Users{
//
//  var users:Seq[User]=Seq()
//
//  def add(user:User):String={
//    users=users:+user.copy(id=users.length)
//    "User Successfully added"
//
//  }
//
//  def delete(id:Int):Option[Int]={
//    val size=users.length
//    users=users.filterNot(_.id==id)
//    Some(size-users.length)
//  }
//
//  def get(id:Int):Option[User]=users.find(_.id==id)
//
//  def listAll:Seq[User]=users
//
//}

