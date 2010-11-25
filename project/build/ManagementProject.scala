import sbt._



class ManagementProject(info: ProjectInfo) extends ParentProject(info) {

  override def ivyUpdateLogging = UpdateLogging.Full

  val scalaToolsSnapshots = "Scala-Tools Maven2 Snapshots Repository" at "http://scala-tools.org/repo-snapshots"

  lazy val managementLift = project("management-lift", "management-lift", new ManagementLift(_))
  lazy val versionInfoPlugin = project("sbt-version-info-plugin", "sbt-version-info-plugin", new VersionInfoPlugin(_))


  class ManagementLift(info: ProjectInfo) extends DefaultProject(info) {
    val liftWebkit = "net.liftweb" %% "lift-webkit" % "2.2-SNAPSHOT" withSources()
    val slf4japi = "org.slf4j" % "slf4j-api" % "1.6.1" withSources()

    val scalatest = "org.scalatest" % "scalatest" % "1.2" % "test" withSources()
    val slf4jsimple = "org.slf4j" % "slf4j-simple" % "1.6.1" % "test" withSources()
  }

  class VersionInfoPlugin(info: ProjectInfo) extends PluginProject(info) {

  }
}