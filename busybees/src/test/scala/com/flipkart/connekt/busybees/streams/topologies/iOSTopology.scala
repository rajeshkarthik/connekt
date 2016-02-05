package com.flipkart.connekt.busybees.streams.topologies

import akka.http.scaladsl.Http
import akka.stream.scaladsl.{Sink, Source}
import com.flipkart.connekt.busybees.streams.TopologyUTSpec
import com.flipkart.connekt.busybees.streams.flows.RenderFlow
import com.flipkart.connekt.busybees.streams.flows.dispatchers.APNSDispatcher
import com.flipkart.connekt.busybees.streams.flows.formaters.IOSChannelFormatter
import com.flipkart.connekt.busybees.streams.sources.RateControl
import com.flipkart.connekt.commons.entities.DeviceDetails
import com.flipkart.connekt.commons.iomodels.ConnektRequest
import com.flipkart.connekt.commons.services.DeviceDetailsService
import com.flipkart.connekt.commons.utils.StringUtils._

import scala.concurrent.Await
import scala.concurrent.duration._

/**
 * Created by kinshuk.bairagi on 05/02/16.
 */
class iOSTopology extends TopologyUTSpec {

  "AndroidTopology Test" should "run" in {

    DeviceDetailsService.add(
      DeviceDetails(
        deviceId = "kinshukIOSRetail",
        userId = "",
        token = "6b1e059bb2a51d03d37384d1493aaffbba4edc58f8e21eb2f80ad4851875ee25",
        osName = "UT", osVersion = "UT", appName = "UT", appVersion = "UT", brand = "UT", model = "UT"
      )
    )

    val cRequest = """
                     |{
                     |	"channel": "PN",
                     |	"sla": "H",
                     |	"templateId": "retail-app-base-0x23",
                     |	"scheduleTs": 12312312321,
                     |	"expiryTs": 3243243224,
                     |	"channelData": {
                     |		"type": "PN",
                     |		"data": {
                     |      "alert" : "Message received from Kinshuk 22",
                     |      "sound" : "default",
                     |      "badge" : 0
                     |		}
                     |	},
                     |	"channelInfo" : {
                     |	    "type" : "PN",
                     |	    "ackRequired": true,
                     |    	"delayWhileIdle": true,
                     |      "platform" :  "ios",
                     |      "appName" : "UT",
                     |      "deviceId" : "kinshukIOSRetail"
                     |	},
                     |	"meta": {}
                     |}
                   """.stripMargin.getObj[ConnektRequest]


    val result = Source.single(cRequest)
      .via(new RateControl[ConnektRequest](2, 1, 2))
      .via(new RenderFlow)
      .via(new IOSChannelFormatter)
      .via(new APNSDispatcher)
      .runWith(Sink.head)

    val response = Await.result(result, 60.seconds)

    Thread.sleep(2000)

    assert(null != response)


  }

}