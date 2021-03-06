/*                     __                                               *\
**     ________ ___   / /  ___      __ ____  Scala.js tools             **
**    / __/ __// _ | / /  / _ | __ / // __/  (c) 2013-2014, LAMP/EPFL   **
**  __\ \/ /__/ __ |/ /__/ __ |/_// /_\ \    http://scala-js.org/       **
** /____/\___/_/ |_/____/_/ | |__/ /____/                               **
**                          |/____/                                     **
\*                                                                      */


package scala.scalajs.tools.packager

import java.io._
import java.net.URI

import scala.scalajs.tools.logging._
import scala.scalajs.tools.io._
import scala.scalajs.tools.classpath._
import scala.scalajs.tools.sourcemap._

import ScalaJSPackedClasspath.packOrderLine
import scala.scalajs.tools.corelib.CoreJSLibs

/** Scala.js packager: concatenates blindly all Scala.js class files. */
class ScalaJSPackager {
  import ScalaJSPackager._

  /** Package Scala.js output files as a single .js file.
   *  See [[ScalaJSOptimizer.Inputs]] for details about the required and
   *  optional inputs.
   *  See [[ScalaJSOptimizer.OutputConfig]] for details about the configuration
   *  for the output of this method.
   *  Returns a [[ScalaJSOptimizer.Result]] containing the result of the
   *  packaging. Its output file will contain, in that order:
   *  1. The Scala.js core lib,
   *  2. The Scala.js class files ordered appropriately,
   *  3. The custom .js files, in the same order as they were listed in inputs.
   */
  def packageScalaJS(inputs: Inputs, outputConfig: OutputConfig,
      logger: Logger): Unit = {
    val classpath = inputs.classpath

    import outputConfig._

    val builder = {
      if (wantSourceMap)
        new JSFileBuilderWithSourceMap(name,
            writer.contentWriter,
            writer.sourceMapWriter,
            relativizeSourceMapBase)
      else
        new JSFileBuilder(name, writer.contentWriter)
    }

    // Write pack order line first
    builder.addLine(packOrderLine(packOrder))

    classpath match {
      case ScalaJSClasspath(irFiles, _) =>
        /* For a Scala.js classpath, we can emit the IR tree directly to our
         * builder, instead of emitting each in a virtual file then appending
         * that to the builder.
         * This is mostly important for the source map, because otherwise the
         * intermediate source map has to be parsed again.
         */
        if (addCoreJSLibs)
          CoreJSLibs.libs.foreach(builder.addFile _)

        val infoAndTrees = irFiles.map(_.infoAndTree)
        for ((_, tree) <- infoAndTrees.sortBy(_._1.ancestorCount))
          builder.addIRTree(tree)

      case _ =>
        for (file <- classpath.mainJSFiles)
          builder.addFile(file)
    }

    for (file <- inputs.customScripts)
      builder.addFile(file)

    builder.complete()
  }
}

object ScalaJSPackager {
  /** Inputs of the Scala.js optimizer. */
  final case class Inputs(
      /** The (partial) Scala.js classpath entries. */
      classpath: JSClasspath,
      /** Additional scripts to be appended in the output. */
      customScripts: Seq[VirtualJSFile] = Nil
  )

  /** Configuration for the output of the Scala.js optimizer. */
  final case class OutputConfig(
      /** Name of the output file. (used to refer to sourcemaps) */
      name: String,
      /** Print writer for the output file. */
      writer: VirtualJSFileWriter,
      /** Pack order written to the .sjspack file to ensure proper ordering by
       *  other tools (notably when reading a JSClasspath from the packed files)
       */
      packOrder: Int,
      /** Whether or not corejslibs should be added at the beginning */
      addCoreJSLibs: Boolean,
      /** Ask to produce source map for the output. */
      wantSourceMap: Boolean = false,
      /** Base path to relativize paths in the source map. */
      relativizeSourceMapBase: Option[URI] = None
  )

}
