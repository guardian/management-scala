SBT Guardian Plugin
===================
SBT plugin for custom Guardian build patterns.

Usage
=====
Plugins file:

    import sbt._

    class Plugins(info: ProjectInfo) extends PluginDefinition(info) {
      val guardian = "Guardian GitHub" at "http://guardian.github.com/maven/repo-releases"
      val gu = "com.gu" % "sbt-gu-plugin" % "0.1"
    }


Project file:

    import sbt._
    import com.gu.solr.SolrProject

    class MyProject(info: ProjectInfo) extends DefaultWebProject(info)
        with ManifestProject {

      // After SBT running with Scala > 2.8 :
      // override def build = super.build.copy(artifact="content-api-custom", branch="release-101")
      override def build = super.build.copy_("content-api-custom")

	  ...
    }


