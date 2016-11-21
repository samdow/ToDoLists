package todolist

import java.io.FileNotFoundException
import todolist.semantics._
import todolist.ir._
import todolist.parser._
import scalafx.application.JFXApp


object ToDoList extends App {

  //val args = parameters.raw

  // Error handling: did the user pass two arguments?
  if (args.length != 1) {
    println(usage)
    sys.exit(1)
  }

  // parse the program file
  val programFilename = args(0)
  val program = ToDoListParser(getFileContents(programFilename))

  // process the results of parsing
  program match {
    // Error handling: syntax errors
    case e: ToDoListParser.NoSuccess  ⇒ println(e)

    // if parsing succeeded...
    case ToDoListParser.Success(t, _) ⇒ {
      println(eval(program.get))
    }
  }

  /** A string that describes how to use the program **/
  def usage = "usage: piconot.external.Piconot <maze-file> <rules-file>"

  /**
    * Given a filename, get a list of the lines in the file
    */
  def getFileLines(filename: String): List[String] =
  try {
    io.Source.fromFile(filename).getLines().toList
  }
  catch { // Error handling: non-existent file
    case e: FileNotFoundException ⇒ { println(e.getMessage()); sys.exit(1) }
  }

  /**
    * Given a filename, get the contents of the file
    */
  def getFileContents(filename: String): String =
  try {
    io.Source.fromFile(filename).mkString
  }
  catch { // Error handling: non-existent file
    case e: FileNotFoundException ⇒ { println(e.getMessage()); sys.exit(1) }
  }
}