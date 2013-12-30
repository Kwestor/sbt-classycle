package org.virtuslab.sbtclassycle

/**
 * Locations of resources to copy.
 *
 * @author Jerzy Müller
 */
private[sbtclassycle] trait PluginResources {

  private val resourceDir = "classycle"

  protected val xslTransformFile = "reportXMLtoHTML.xsl"

  protected lazy val xslTransformUrl =
    getClass.getClassLoader.getResource(resourceDir + "/" + xslTransformFile)

  private val imagesZip = "images.zip"

  protected lazy val imagesUrl =
    getClass.getClassLoader.getResource(resourceDir + "/" + imagesZip)
}
