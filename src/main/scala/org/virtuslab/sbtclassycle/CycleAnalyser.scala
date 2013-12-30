package org.virtuslab.sbtclassycle

import sbt._
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.{StreamResult, StreamSource}
import java.io.FileOutputStream

/**
 * Class for doing actual work.
 *
 * @param target file for `target` directory
 * @param name name that would be prepended into report file (for example module name)
 */
case class CycleAnalyser(target: File, name: String) extends PluginResources {

  private val classycleDir = "classycle"

  private val images = "images"

  /** Copies files from sbt-classycle jar to your_project/target/classycle */
  def initializeDirectories() {
    IO.createDirectory(target / classycleDir)
    IO.download(xslTransformUrl, target / classycleDir / xslTransformFile)
    IO.delete(target / classycleDir / images)
    IO.unzipURL(imagesUrl, target / classycleDir)
  }

  private def xmlReport(moduleName: String) = s"$moduleName-classycle.xml"

  private def htmlReport(moduleName: String) = s"$moduleName-classycle.html"

  /** Runs classycle analyser and saves result in `target / classycleDir / xmlReport(name)` */
  def analyse(classDirectory: String) {
    val path = (target / classycleDir / xmlReport(name)).getAbsolutePath
    _root_.classycle.Analyser.main(Array(
      s"-xmlFile=$path",
      "-mergeInnerClasses",
      classDirectory))
  }

  /** Applies XSL transform to classycle result producing HTML report */
  def transformToHtml() {
    val tFactory = TransformerFactory.newInstance
    val transformer = tFactory.newTransformer(new StreamSource(target / classycleDir / xslTransformFile))

    transformer.transform(
      new StreamSource(target / classycleDir / xmlReport(name)),
      new StreamResult(new FileOutputStream(target / classycleDir / htmlReport(name)))
    )
  }

  /** */
  def generatedFile = target / classycleDir / htmlReport(name)

}
