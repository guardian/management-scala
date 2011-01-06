package com.gu.management

import net.liftweb.common._
import java.util.concurrent.atomic.AtomicBoolean
import net.liftweb.http._

/**
 * This trait should be used by anything that wants to read
 * the state of a switch
 */
trait Switch {
  def isSwitchedOn: Boolean

  def whenOn(block: => Unit) = if (isSwitchedOn) block

  def opt[T](block: => T): Option[T] = if (isSwitchedOn) Some(block) else None
}

/**
 * This trait should be used by anything that wants to
 * mutate the state of a switch
 */
trait Switchable extends Switch {
  def switchOn(): Unit
  def switchOff(): Unit

  /**
   * @return a single url-safe word that can be used to construct urls
   * for this switch.
   */
  def name: String

  /**
   * @return a sentence that describes, in websys understandable terms, the
   * effect of switching this switch
   */
  def description: String
}


/**
 * A simple implementation of Switchable that does the right thing in most cases
 */
case class DefaultSwitch(name: String, description: String, initiallyOn: Boolean = true) extends Switchable with Loggable {
  private val isOn = new AtomicBoolean(initiallyOn)

  def isSwitchedOn = isOn.get

  def switchOn() {
    logger.info("Switching on " + name)
    isOn set true
  }

  def switchOff() {
    logger.info("Switching off " + name)
    isOn set false
  }
}


class Switchboard(switches: Switchable*) extends XhmlManagementPage with Postable {
  val title = "Switchboard"
  val managementSubPath = "switchboard" :: Nil

  def body(r: Req) = {
    val switchToShow = r.param("switch")
    def shouldShow(s: Switchable) = switchToShow.isEmpty || switchToShow === s.name

    <form method="POST">
      <table border="1">
        <tr><th>Switch name</th><th>Description</th><th>State</th></tr>
        { for (switch <- switches.filter(shouldShow(_)).sortBy(_.name)) yield renderSwitch(switch) }
      </table>
    </form>
  }

  private def renderSwitch(s: Switchable) =
    <tr>
      <td><a href={ "?switch=" + s.name }>{s.name}</a></td>
      <td>{s.description}</td>
      <td style="width: 100px; text-align: center;">{renderButtons(s)}</td>
    </tr>


  private def renderButtons(s: Switchable) =
    if (s.isSwitchedOn)
      <xml:group><span style="color: ForestGreen"> ON </span><input type="submit" name={s.name} value="OFF"/></xml:group>
    else
      <xml:group><input type="submit" name={s.name} value="ON"/><span style="color: DarkRed"> OFF </span></xml:group>


  def processPost(r: Req) =
    for (switch <- switches) {
      r.param(switch.name) match {
        case Full("ON") => switch.switchOn()
        case Full("OFF") => switch.switchOff()
        case Full(other) => error("Expected ON or OFF as value for "+ switch.name +" parameter")
        case _ => // ignore
      }
    }
}
