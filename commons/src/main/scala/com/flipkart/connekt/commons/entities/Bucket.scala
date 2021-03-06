/*
 *         -╥⌐⌐⌐⌐            -⌐⌐⌐⌐-
 *      ≡╢░░░░⌐\░░░φ     ╓╝░░░░⌐░░░░╪╕
 *     ╣╬░░`    `░░░╢┘ φ▒╣╬╝╜     ░░╢╣Q
 *    ║╣╬░⌐        ` ╤▒▒▒Å`        ║╢╬╣
 *    ╚╣╬░⌐        ╔▒▒▒▒`«╕        ╢╢╣▒
 *     ╫╬░░╖    .░ ╙╨╨  ╣╣╬░φ    ╓φ░╢╢Å
 *      ╙╢░░░░⌐"░░░╜     ╙Å░░░░⌐░░░░╝`
 *        ``˚¬ ⌐              ˚˚⌐´
 *
 *      Copyright © 2016 Flipkart.com
 */
package com.flipkart.connekt.commons.entities

import javax.persistence.Column

class Bucket {
  @Column(name = "name")
  var name: String = _

  @Column(name = "id")
  var id: String = _

  override def toString = s"Bucket($name, $id)"

  def this(id:String, name:String){
    this()
    this.id = id
    this.name = name
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[Bucket]

  override def equals(other: Any): Boolean = other match {
    case that: Bucket =>
      (that canEqual this) &&
        name == that.name &&
        id == that.id
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(name, id)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}
