package com.gu.management

import net.liftweb.http.XmlResponse

object Status {
  def apply(metrics: Seq[TimingMetric]) = new Status(metrics)
}

class Status(metrics: Seq[TimingMetric]) extends ManagementPage {
  val managementSubPath = "status" :: Nil


  def response = XmlResponse(
    <status>
      <timings>
        {metrics map {_.toXml}}
      </timings>
    </status>)

}