package todolist

import scala.tools.nsc.EvalLoop
import java.io.FileNotFoundException
import todolist.semantics._
import todolist.ir._
import todolist.parser._
import scalafx.application.JFXApp


/*
 * Main executable for ToDoLists by Samantha Andow
 */
object ToDoList extends EvalLoop with App {

  override def prompt = "> "

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
      var list:FinalList = eval(program.get)
      println(list)
      //The REPL happens after the first list has been processed
      loop { line => 
        REPLParser(line) match {
          case REPLParser.Success(t,_) => {list = REPLeval(t,list); println(list)}
          
          //If it's not a valid command, give the user the options of valid commands
          case e: REPLParser.NoSuccess => println("""Not a valid command. Valid commands are
          remove task (number)
          switch tasks (number) and (number)
          exit""")
        }
      }
    }
  }

  /** A string that describes how to use the program **/
  def usage = "usage: todolist.ToDoList <list-file>"

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