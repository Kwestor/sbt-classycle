package org.virtuslab.sbtclassycle

import sbt._
import sbt.Keys._
import sbt.File

/**
 * Plugin for classycle report generation.
 *
 * @author Jerzy Müller
 */
object SbtClassyclePlugin extends Plugin {

  /** Task for cyclic dependencies analysis */
  val classycle = taskKey[File]("run Classycle and generates xml/html reports")

  lazy val classycleTask = classycle := {
    val log = streams.value.log

    try {
      val name = moduleName.value

      log.info(s"Generating cyclic dependency report for module: $name")

      val _ = (compile in Compile).value // run it, ignore result

      val target = baseDirectory.value / "target"

      val analyser = new CycleAnalyser(target, name)

      val classDir = (classDirectory in Compile).value.getAbsolutePath.toString

      log.info("Initializing files ...")
      analyser.initializeDirectories()

      log.info("Analysing dependencies ...")
      analyser.analyse(classDir)

      log.info("Transforming xml report into html ...")
      analyser.transformToHtml()

      log.success("Generation complete!")
      analyser.generatedFile
    } catch {
      case e: Throwable =>
        log.error(s"Analysis failed due to: '${e.getMessage}'")
        throw e
    }
  }

  def classycleSettings: Seq[Setting[_]] = Seq(classycleTask)
}

