SBT Guardian Plugin
===================
SBT plugin for `gu-deploy-libs` Guardian build patterns.

Usage
=====
Plugins file:

    import sbt._

    class Plugins(info: ProjectInfo) extends PluginDefinition(info) {
      val guardian = "Guardian Github" at "http://guardian.github.com/maven/repo-releases"
      val deploy = "com.gu" % "sbt-gu-deploy-artifactrep-publish" % "1.1"
    }


Project file:

    import sbt._
    import com.gu.solr.SolrProject

    class MyProject(info: ProjectInfo) extends DefaultWebProject(info)
        with PackagedWebapp {

      override def distributableElements = List(
        DeploySupport(this src "deploy", deployLibJar),
        WebApp(this get "artifact-name*.war", artifact, "artifact-name.war")
      )

	  ...
    }

And include a `deploy` directory at `src/main/deploy` which has a wrapper
`remoteDeploy.sh` and an implementing `remoteDeploy[ArtifactName].py`.

Please do not implement ad hoc deployment schemes. Ultimately, the desired aim
is to generate war artifacts rather than self-deployable zips.

You should only have to include the `distributableElements` but on occasion it
may be necessary to rename your artifact. To this end, it is possible to
override the `artifact` definition but this is to be avoided:


    import sbt._
    import com.gu.solr.SolrProject

    class MyProject(info: ProjectInfo) extends DefaultWebProject(info)
        with PackagedWebapp {

      override def artifact = "my_new_artifact_name"

      override def distributableElements = List(
        DeploySupport(this src "deploy", deployLibJar),
        WebApp(this get "artifact-name*.war", artifact, "artifact-name.war")
      )

	  ...
    }

A second exception is the case of Solr applications. Those built with
`sbt-solr-plugin` are the most straightforward to deploy, but do require
an additional line to add the Solr configuration to the deployment artifact:

    import sbt._
    import com.gu.solr.SolrProject

    class MyProject(info: ProjectInfo) extends DefaultWebProject(info)
        with PackagedWebapp {

      override def distributableElements = List(
         DeploySupport(this src "deploy", deployLibJar),
         WebApp(solrWar, artifact, "solr.war"),
         Paths((sourcePath / "main" / "solr").head, artifact, "solr")
      )

	  ...
    }

Final warning: This deployment scheme does not include a `jmxclient` jar for you.

Speak to Daithi for any additional explanation or assistance.

