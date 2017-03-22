import com.typesafe.sbteclipse.plugin.EclipsePlugin._

EclipseKeys.createSrc := EclipseCreateSrc.Default
EclipseKeys.projectFlavor := EclipseProjectFlavor.Scala
EclipseKeys.skipParents in ThisBuild := false
EclipseKeys.withSource := false
EclipseKeys.withJavadoc := false
EclipseKeys.withBundledScalaContainers := true
