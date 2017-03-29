package com.github.thirstycrow.sbtpublish2

import sbt._
import Keys._
import complete._
import complete.DefaultParsers._

object SbtPublish2 extends AutoPlugin {

  object autoImport {

    object Publish2 {
      def maven(resolver: Resolver) = Publish2(resolver, true)
      def ivy(resolver: Resolver) = Publish2(resolver, false)
    }
    case class Publish2(resolver: Resolver, mavenStyle: Boolean, overwrite: Option[Boolean] = None) {
      def overwrite(value: Boolean = true) = copy(overwrite = Some(value))
    }

    val publish2Configs = settingKey[Seq[Publish2]]("repositories to which artifacts can be published")
    val publish2 = inputKey[Unit]("publish to specific repositories")

    lazy val basePublishSettings: Seq[Def.Setting[_]] = Seq(
      publish2Configs := Seq(),
      otherResolvers ++= publish2Configs.value.map(_.resolver),
      publish2 := {
        val log = streams.value.log
        val pubCfgs = publish2Configs.value
        val configNames = parser.parsed match {
          case Nil   => Set(Classpaths.getPublishTo(publishTo.value).name)
          case names => names.toSet
        }
        configNames.foreach { name =>
          log.info("publishing artifacts to " + name)
          val cfg = pubCfgs.find(_.resolver.name == name)
          val mavenStyle = cfg.map(_.mavenStyle).getOrElse(publishMavenStyle.value)
          val overwrite = cfg.flatMap(_.overwrite).getOrElse(isSnapshot.value)
          val ivyFile = (name, mavenStyle) match {
            case ("local", _) => Some(deliverLocal.value)
            case (_, false)   => Some(deliver.value)
            case (_, true)    => None
          }
          val publishConfiguration = Classpaths.publishConfig(
            artifacts = packagedArtifacts.in(publish).value,
            ivyFile = ivyFile,
            resolverName = name,
            checksums = checksums.in(publish).value,
            logging = ivyLoggingLevel.value,
            overwrite = overwrite
          )
          IvyActions.publish(ivyModule.value, publishConfiguration, streams.value.log)
        }
      }
    )
  }

  import autoImport._

  override def requires = plugins.IvyPlugin

  override def trigger = allRequirements

  override val projectSettings = basePublishSettings

  lazy val parser = Def.setting { (state: State) =>
    val repoName = otherResolvers.value.map(_.name: Parser[String]).reduce(_ | _) | "local"
    (token(Space) ~> repoName).*
  }
}
