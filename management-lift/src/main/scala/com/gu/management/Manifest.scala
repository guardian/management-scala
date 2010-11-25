package com.gu.management

import net.liftweb.http.PlainTextResponse
import io.Source

object Manifest extends ManagementPage {
  val managementSubPath = "manifest" :: Nil

  lazy val response = PlainTextResponse(
    Option(getClass.getResourceAsStream("/version.txt"))
            .map(Source.fromInputStream(_))
            .map(_.mkString)
            .getOrElse("Could not find version.txt on classpath.  Did you include the sbt-version-info-plugin?")
  )

}