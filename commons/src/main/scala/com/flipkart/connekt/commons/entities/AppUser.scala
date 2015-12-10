package com.flipkart.connekt.commons.entities

import java.math.BigInteger
import javax.persistence.Column

import org.apache.commons.lang.StringUtils

/**
 *
 *
 * @author durga.s
 * @version 12/10/15
 */
class AppUser {

  @Column(name = "userId")
  var userId: String = _

  @Column(name = "apikey")
  var apiKey: String = _

  @Column(name = "groups")
  var groups: String = _

  @Column(name = "lastUpdatedTS")
  var lastUpdatedTs: BigInteger = BigInteger.valueOf(System.currentTimeMillis())

  @Column(name = "updatedBy")
  var updatedBy: String = StringUtils.EMPTY

  def this(userId: String,
           apiKey: String,
           groups: String,
           lastUpdatedTs: Long,
           updatedBy: String) {
    this()
    this.userId = userId
    this.apiKey = apiKey
    this.groups = groups
    this.lastUpdatedTs = BigInteger.valueOf(lastUpdatedTs)
    this.updatedBy = updatedBy
  }

}