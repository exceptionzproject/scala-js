/*                     __                                               *\
**     ________ ___   / /  ___      __ ____  Scala.js API               **
**    / __/ __// _ | / /  / _ | __ / // __/  (c) 2013, LAMP/EPFL        **
**  __\ \/ /__/ __ |/ /__/ __ |/_// /_\ \    http://scala-lang.org/     **
** /____/\___/_/ |_/____/_/ | |__/ /____/                               **
**                          |/____/                                     **
\*                                                                      */



package scala.scalajs

/** Contains primitive types for interoperability with JavaScript libraries.
 *  This package is only relevant to the Scala.js compiler, and should not be
 *  referenced by any project compiled to the JVM.
 *
 *  All the values and methods in this package object are representatives of
 *  standard variables and functions available in the top-level scope, as
 *  standardized in ECMAScript 5.1.
 *
 *  == Guide ==
 *
 *  General documentation on Scala.js can is available at
 *  [[http://lampwww.epfl.ch/~doeraene/scala-js/doc/]].
 *
 *  == Overview ==
 *
 *  The trait [[js.Any]] is the super type of all JavaScript values. It has
 *  subtraits [[js.Number]], [[js.Boolean]], [[js.String]] and [[js.Undefined]]
 *  which represent values of primitive JavaScript types. It also has a
 *  subclass [[js.Object]], which is the base class of all JavaScript objects,
 *  and represents the predefined `Object` class of JavaScript.
 *
 *  All class, trait and object definitions that inherit, directly or
 *  indirectly, from [[js.Any]] do not have actual implementations in Scala.
 *  They are only the manifestation of static types representing libraries
 *  written directly in JavaScript. It is not possible to implement yourself
 *  a subclass of [[js.Any]]: all the method definitions will be ignored when
 *  compiling to JavaScript.
 *
 *  The trait [[js.Dynamic]] is a special subtrait of [[js.Any]]. It can
 *  represent any JavaScript value in a dynamically typed way. It is possible
 *  to call any method and read and write any field of a value of type
 *  [[js.Dynamic]].
 *
 *  The trait [[js.Dictionary]] provides read and write access to any field of
 *  any JavaScript value with the array access syntax of Scala, mapping to the
 *  array access syntax of JavaScript.
 */
package object js extends js.GlobalScope {
  /** The type of JavaScript numbers, which is [[scala.Double]]. */
  type Number = scala.Double
  /** The type of JavaScript booleans, which is [[scala.Boolean]]. */
  type Boolean = scala.Boolean
  /** The type of JavaScript strings, which is [[java.lang.String]]. */
  type String = java.lang.String
  /** The type of the JavaScript undefined value, which is [[scala.Unit]]. */
  type Undefined = scala.Unit

  /** The top-level `Number` JavaScript object. */
  val Number: js.prim.Number.type = ???
  /** The top-level `Boolean` JavaScript object. */
  val Boolean: js.prim.Boolean.type = ???
  /** The top-level `String` JavaScript object. */
  val String: js.prim.String.type = ???

  /** The constant Not-a-Number. */
  val NaN: Double = ???
  /** The constant Positive Infinity. */
  val Infinity: Double = ???

  /** The undefined value. */
  def undefined: js.prim.Undefined = sys.error("stub")

  /** Tests whether the given value is undefined. */
  def isUndefined(v: scala.Any): Boolean = sys.error("stub")

  /** Returns the type of `x` as identified by `typeof x` in JavaScript. */
  def typeOf(x: Any): String = sys.error("stub")

  /** Invokes any available debugging functionality.
   *  If no debugging functionality is available, this statement has no effect.
   *
   *  MDN
   *
   *  Browser support:
   *    * Has no effect in Rhino nor, apparently, in Firefox
   *    * In Chrome, it has no effect unless the developer tools are opened
   *      beforehand.
   */
  def debugger(): Unit = sys.error("stub")

  /** Evaluates JavaScript code and returns the result. */
  def eval(x: String): Any = ???

  /** Parses a string as an integer with a given radix. */
  def parseInt(s: String, radix: Int): js.Number = ???
  /** Parses a string as an integer with auto-detected radix. */
  def parseInt(s: String): js.Number = ???
  /** Parses a string as a floating point number. */
  def parseFloat(string: String): Double = ???

  /** Tests whether the given value is Not-a-Number. */
  def isNaN(number: Double): Boolean = ???
  /** Tests whether the given value is a finite number. */
  def isFinite(number: Double): Boolean = ???

  /** Decodes a Uniform Resource Identifier (URI).
   *  @see [[encodeURI]]
   */
  def decodeURI(encodedURI: String): String = ???

  /** Decodes a Uniform Resource Identifier (URI) component.
   *  @see [[encodeURIComponent]]
   */
  def decodeURIComponent(encodedURIComponent: String): String = ???

  /** Encodes a Uniform Resource Identifier (URI).
   *  @see [[decodeURI]]
   */
  def encodeURI(uri: String): String = ???

  /** Encodes a Uniform Resource Identifier (URI) component.
   *  @see [[decodeURIComponent]]
   */
  def encodeURIComponent(uriComponent: String): String = ???
}
