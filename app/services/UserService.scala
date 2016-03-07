package services

import models.User

import scala.concurrent.Future


object UserService {

  def addUser(user:User):Future[String]={
    User.add(user)
  }

  def deleteUser(id:Int):Future[Int]={
    User.delete(id)
  }

  def getUser(id:Int):Future[Option[User]]={
    User.get(id)
  }

  def listUsers:Future[Seq[User]]={
    User.listAll
  }
}
