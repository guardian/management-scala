package com.gu.management

import net.liftweb.common._
import net.liftweb.http._
import scala.xml.NodeSeq

trait ManagementPage {
  val managementSubPath: List[String]

  lazy val path = "management" :: managementSubPath

  // the backticks here make path not be a catch-all new variable,
  // but mean "match against the value of path"
  lazy val dispatch: LiftRules.DispatchPF = {
    case r@Req(`path`, _, GetRequest) => () => Full(render(r))
  }

  def render(req: Req): LiftResponse

  def url = path.mkString("/")
  def linktext = "/" + managementSubPath.mkString("/")
}

trait XhmlManagementPage extends ManagementPage {
  final def render(r: Req) = XhtmlResponse(
      <html xmlns="http://www.w3.org/1999/xhtml">
        <head>
          <title>{title}</title>
        </head>
        <body>
          <h2>{title}</h2>
          { body(r) }
        </body>
      </html>,
      Full(DocType.html5),
      Nil,
      cookies = Nil,
      code = 200, renderInIEMode = false)

  def title: String
  def body(r: Req): NodeSeq
}

object Management {
  def publishWithIndex(pages: ManagementPage*): LiftRules.DispatchPF =
    pages.foldLeft(new ManagementIndex(pages).dispatch) { _ orElse _.dispatch }
  
}