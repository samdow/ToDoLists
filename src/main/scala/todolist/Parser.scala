package todolist.parser

import scala.language.postfixOps
import scala.util.parsing.combinator.{PackratParsers, RegexParsers}
import todolist.ir._
import DaysOfWeek._

/*
 * Grammar:
 *  category: name ":\n" tasks
 *  tasks:    task "\n" tasks | <Empty>
 *  task:     "\t" taskName " due " day "\n"
 */

object ToDoListParser extends RegexParsers with PackratParsers{

    override val whiteSpace = """( )+""".r
    def apply(s: String): ParseResult[List[Category]] = parseAll(program, s)

    def program: Parser[List[Category]] = category*

    def category: Parser[Category] = 
        (("[0-9a-zA-z]+".r ~ ":" ~ "\n" ~ taskList ^^ {case n ~ ":" ~ "\n" ~ list => Category(n, list)}))

    def taskList: Parser[List[Task]] = 
        ((task ~ "\n" ~ taskList ^^ {case t ~ "\n" ~ list => t::list})
         |("\n" ^^ {case "\n" => List()}))

    def task:Parser[Task] = 
        (("[0-9a-zA-z]+".r ~ " due " ~ day ^^ {case n ~ " due " ~ d => Task(n,d)}))

    def day:Parser[DaysOfWeek] = 
        (("Mon" ^^^ DaysOfWeek.Mon)
         |("Tue" ^^^ DaysOfWeek.Tue)
         |("Wed" ^^^ DaysOfWeek.Wed)
         |("Thu" ^^^ DaysOfWeek.Thu)
         |("Fri" ^^^ DaysOfWeek.Fri)
         |("Sat" ^^^ DaysOfWeek.Sat)
         |("Sun" ^^^ DaysOfWeek.Sun))
}
