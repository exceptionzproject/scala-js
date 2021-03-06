/* Scala.js compiler
 * Copyright 2013 LAMP/EPFL
 * @author  Sébastien Doeraene
 */

package scala.scalajs.compiler

import scala.tools.nsc._
import scala.tools.nsc.io.AbstractFile
import scala.reflect.internal.pickling.PickleBuffer

import java.io._

import scala.scalajs.ir
import ir.{Trees => js, Printers, SourceMapWriter}
import ir.Infos._

/** Send JS ASTs to files
 *
 *  @author Sébastien Doeraene
 */
trait GenJSFiles extends SubComponent { self: GenJSCode =>
  import global._
  import jsAddons._

  def genIRFile(cunit: CompilationUnit, sym: Symbol, tree: js.Tree,
      classInfo: ClassInfo): Unit = {
    val outfile = getFileFor(cunit, sym, ".sjsir")
    val output = outfile.bufferedOutput
    try {
      ir.InfoSerializers.serialize(output, classInfo)
      ir.Serializers.serialize(output, tree)
    } finally {
      output.close()
    }
  }

  def genIRFileText(cunit: CompilationUnit, sym: Symbol, tree: js.Tree,
      classInfo: ClassInfo): Unit = {
    val outfile = getFileFor(cunit, sym, ".ir.js")
    val output = bufferedWriter(outfile)
    try {
      val printer = new Printers.IRTreePrinter(output)
      printer.printTopLevelTree(tree)
      printer.close()

      val infoPrinter = new Printers.InfoPrinter(output)
      infoPrinter.printClassInfo(classInfo)
      infoPrinter.close()
    } finally {
      output.close()
    }
  }

  private def bufferedWriter(file: AbstractFile): Writer =
    new OutputStreamWriter(file.bufferedOutput, "UTF-8")

  private def getFileFor(cunit: CompilationUnit, sym: Symbol,
      suffix: String) = {
    val baseDir: AbstractFile =
      settings.outputDirs.outputDirFor(cunit.source.file)

    val pathParts = sym.fullName.split("[./]")
    val dir = (baseDir /: pathParts.init)(_.subdirectoryNamed(_))

    var filename = pathParts.last
    if (sym.isModuleClass && !sym.isImplClass)
      filename = filename + nme.MODULE_SUFFIX_STRING

    dir fileNamed (filename + suffix)
  }
}
