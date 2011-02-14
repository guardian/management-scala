import java.io.File
import sbt._



class ManagementProject(info: ProjectInfo) extends ParentProject(info) {

  override def ivyUpdateLogging = UpdateLogging.Full

  lazy val managementLift = project("management-lift", "management-lift", new ManagementLift(_))
  lazy val versionInfoPlugin = project("sbt-version-info-plugin", "sbt-version-info-plugin", new VersionInfoPlugin(_))
  lazy val packageDeployArtifactPlugin =
    project("sbt-artifactrep-publish", "sbt-artifactrep-publish", new PublishToArtifactrepPlugin(_), versionInfoPlugin)
  lazy val guDeployArtifactPlugin =
    project("sbt-gu-deploy-artifactrep-publish", "sbt-gu-deploy-artifactrep-publish", new GuDeployPublishToArtifactrepPlugin(_), versionInfoPlugin)

  class ManagementLift(info: ProjectInfo) extends DefaultProject(info) with PublishSources {
    val liftWebkit = "net.liftweb" %% "lift-webkit" % "2.2" withSources()
    val slf4japi = "org.slf4j" % "slf4j-api" % "1.6.1" withSources()

    val scalatest = "org.scalatest" % "scalatest" % "1.2" % "test" withSources()
    val slf4jsimple = "org.slf4j" % "slf4j-simple" % "1.6.1" % "test" withSources()
  }

  class VersionInfoPlugin(info: ProjectInfo) extends PluginProject(info)
  class PublishToArtifactrepPlugin(info: ProjectInfo) extends PluginProject(info)
  class GuDeployPublishToArtifactrepPlugin(info: ProjectInfo) extends PluginProject(info)

  override def managedStyle = ManagedStyle.Maven

  val publishTo =
    if (projectVersion.value.toString.contains("-SNAPSHOT"))
      Resolver.file("guardian github snapshots", new File(System.getProperty("user.home")
            + "/guardian.github.com/maven/repo-snapshots"))
    else
      Resolver.file("guardian github releases", new File(System.getProperty("user.home")
            + "/guardian.github.com/maven/repo-releases"))

}

trait PublishSources extends BasicScalaProject with BasicPackagePaths {
  lazy val sourceArtifact = Artifact.sources(artifactID)
  override def packageSrcJar = defaultJarPath("-sources.jar")
  override def packageToPublishActions = super.packageToPublishActions ++ Seq(packageSrc)
}

