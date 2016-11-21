package todolist.parser

import scala.language.postfixOps
import scala.util.parsing.combinator._
import todolist.ir._
import DaysOfWeek._

/*
 * Grammar:
 *  category: name ":\n" tasks
 *  tasks:    task "\n" tasks | <Empty>
 *  task:     "\t" taskName " due " day "\n"
 */

object ToDoListParser extends JavaTokenParsers with RegexParsers
                                               with PackratParsers
{

    override val whiteSpace = """( )+""".r
    def apply(s: String): ParseResult[List[Category]] = parseAll(program, s)

    lazy val program: PackratParser[List[Category]] = category*

    lazy val category: PackratParser[Category] = 
        (( name ~ ":" ~ taskList ^^ {case n ~ ":" ~ list => Category(n, list)}))

    lazy val taskList: PackratParser[List[Task]] = 
        (("\n" ~ task ~ taskList ^^ {case "\n" ~ t ~ list => t :: list})
         |("\n" ^^^ List()))

    lazy val task:PackratParser[Task] = 
        (("\t" ~ name ~ " due ".r ~ day ^^ {case "\t" ~ n ~ " due ".r ~ d => Task(n,d)})

    lazy val day:PackratParser[DaysOfWeek] = 
        (("Mon" ^^^ DaysOfWeek.Mon)
         |("Tue" ^^^ DaysOfWeek.Tue)
         |("Wed" ^^^ DaysOfWeek.Wed)
         |("Thu" ^^^ DaysOfWeek.Thu)
         |("Fri" ^^^ DaysOfWeek.Fri)
         |("Sat" ^^^ DaysOfWeek.Sat)
         |("Sun" ^^^ DaysOfWeek.Sun))

    //Parses name of each category or task
    lazy val name: Parser[String] = """([0-9]|[a-z]|[A-Z]|_)+""".r
}
