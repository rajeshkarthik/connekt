package com.flipkart.connekt.commons.tests.metrics

import com.flipkart.connekt.commons.metrics.Instrumented
import com.flipkart.connekt.commons.tests.ConnektUTSpec
import com.flipkart.metrics.Timed

/**
 * Created by kinshuk.bairagi on 11/02/16.
 */
class MetricsTest  extends ConnektUTSpec with Instrumented {


  "Metrics Test " should "meter" in {

    @Timed("meter-test")
    def someSleep(x:String):String =  {
      Thread.sleep(2)
      x
    }


    1 to 100 foreach(x => someSleep("i"))


    Thread.sleep(30000)

    assert(true, true)

  }


}
