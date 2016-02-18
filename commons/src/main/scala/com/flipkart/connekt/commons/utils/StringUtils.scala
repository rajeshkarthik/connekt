package com.flipkart.connekt.commons.utils

import java.math.BigInteger
import java.security.{MessageDigest, SecureRandom}

import akka.http.scaladsl.model.HttpEntity
import akka.stream.Materializer
import akka.util.{ByteString, ByteStringBuilder}
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import com.flipkart.connekt.commons.utils.NullWrapper._
import org.apache.commons.codec.CharEncoding

import scala.collection.JavaConversions._
import scala.concurrent.Await
import scala.reflect.{ClassTag, _}
import scala.concurrent.duration._
/**
 *
 *
 * @author durga.s
 * @version 11/23/15
 */
object StringUtils {

  implicit class StringHandyFunctions(val s: String) {
    def getUtf8Bytes = s.getBytes(CharEncoding.UTF_8)

    def getUtf8BytesNullWrapped = Option(s).map(_.getUtf8Bytes).orNull.wrap

  }


  implicit class ByteArrayHandyFunctions(val b: Array[Byte]) {
    def getString = new String(b, CharEncoding.UTF_8)

    def getStringNullable = b.unwrap match {
      case array if array.isEmpty => null
      case value => new String(value, CharEncoding.UTF_8)
    }

  }

  val objMapper = new ObjectMapper() with ScalaObjectMapper
  objMapper.registerModules(Seq(DefaultScalaModule):_*)

  implicit class JSONMarshallFunctions(val o: AnyRef) {
    def getJson = objMapper.writeValueAsString(o)
  }
  
  implicit class JSONUnMarshallFunctions(val s: String) {
    def getObj[T: ClassTag] = objMapper.readValue(s, classTag[T].runtimeClass).asInstanceOf[T]
    def getObj(implicit cType: Class[_]) = objMapper.readValue(s, cType)
  }

  implicit  class HttpEntity2String(val entity:HttpEntity){
    def getString(implicit materializer: Materializer,  ec:scala.concurrent.ExecutionContext):String = {
      Await.result(entity.dataBytes.runFold[ByteStringBuilder](ByteString.newBuilder)((u, bs) => {u ++= bs}).map( bb => new String(bb.result().toArray)), 30.seconds)
    }
  }


  def isNullOrEmpty(o: Any): Boolean = o match {
    case m: Map[_, _] => m.isEmpty
    case i: Iterable[Any] => i.isEmpty
    case null | None | "" => true
    case Some(x) => isNullOrEmpty(x)
    case _ => false
  }

  def getObjectNode = objMapper.createObjectNode()

  def getArrayNode = objMapper.createArrayNode()

  def md5(s: String) : String = {
    val md5 = MessageDigest.getInstance("MD5")
    md5.reset()
    md5.update(s.getBytes)
    md5.digest().map(0xFF & _).map { "%02x".format(_) }.foldLeft(""){_ + _}
  }

  def generateRandomStr(len: Int): String = {
    val ZERO = Character.valueOf('0')
    val A = Character.valueOf('A')
    val sb = new StringBuffer()
    for (i <- 1 to len) {
      var n = (36.0 * Math.random).asInstanceOf[Int]
      if (n < 10) {
        n = (ZERO + n)
      }
      else {
        n -= 10
        n = (A + n)
      }
      sb.append(new Character(n.asInstanceOf[Char]))
    }
    return new String(sb)
  }

  def generateSecureRandom:String = {
    val random:SecureRandom = new SecureRandom()
    new BigInteger(130, random).toString(32)
  }

  def getDetail(obj: Any, path: String, splitter: String = "/"): Option[Any] = {

    var myObj = obj
    val parts = path.split(splitter)
    var i = 0
    do {
      var index = parts(i)
      var negated = false
      if (parts(i).toString.startsWith("~")) {
        index = parts(i).split("~").tail.head
        negated = true
      }
      val value = negated match {
        case true => myObj match {
          case _: Map[_, _] => myObj.asInstanceOf[Map[String, Any]].filter(_._1 != index)
          case _: List[_] =>
            if (myObj.asInstanceOf[List[Any]].length > 0)
              myObj.asInstanceOf[List[Any]] diff List[Any](myObj.asInstanceOf[List[Any]].get(index.toInt))
            else
              return None
          case _: java.util.Map[_, _] => myObj.asInstanceOf[java.util.Map[String, AnyRef]].filter(_._1 != index)
          case _: java.util.List[_] =>
            if (myObj.asInstanceOf[java.util.List[Any]].length > 0)
              myObj.asInstanceOf[java.util.List[Any]] diff List[Any](myObj.asInstanceOf[java.util.List[Any]].get(index.toInt))
            else
              return None
          case _ => return None
        }
        case false => myObj match {
          case _: Map[_, _] => myObj.asInstanceOf[Map[String, Any]].get(index)
          case _: List[_] =>
            if (myObj.asInstanceOf[List[Any]].length > index.toInt)
              myObj.asInstanceOf[List[Any]].get(index.toInt)
            else
              return None
          case _: java.util.Map[_, _] => myObj.asInstanceOf[java.util.Map[String, AnyRef]].get(index)
          case _: java.util.List[_] =>
            if (myObj.asInstanceOf[java.util.List[Any]].length >= index.toInt)
              myObj.asInstanceOf[java.util.List[Any]].get(index.toInt)
            else
              return None
          case _ => return None
        }
      }
      myObj = value match {
        case Some(null) => return None
        case Some(x) => x
        case Nil => return None
        case null => return None
        case None => return None
        case _ => value
      }
      i = i + 1
    } while (i < parts.size)
    Some(myObj)
  }

}
