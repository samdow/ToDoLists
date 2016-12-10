package todolist.parser

import scala.language.postfixOps
import scala.util.parsing.combinator._
import todolist.ir._
import DaysOfWeek._

/*
 * Grammar:
 *  today:      "Today is " day | <Empty>
 *  category:   name ":\n" tasks
 *  tasks:      task "\n" tasks | <Empty>
 *  task:       "\t" taskName " due " day precedence "\n"
 *  precedence: "!"* | "\d!"
 *  day:      matches name to date with many different way to say each day
 */

object ToDoListParser extends JavaTokenParsers with RegexParsers
                                               with PackratParsers
{

    override val whiteSpace = """()+""".r
    def apply(s: String): ParseResult[(List[Category],DaysOfWeek)] = parseAll(program, s)

    lazy val program: PackratParser[(List[Category],DaysOfWeek)] = today

    lazy val today: PackratParser[(List[Category],DaysOfWeek)] =
        (("Today is" ~ wS ~ day ~ wSNL ~ categories ^^ {case "Today is" ~ _ ~ d ~ _ ~ c => (c, d)})
         |(categories ^^ {case c => (c, DaysOfWeek.Sunday)}))

    lazy val categories: PackratParser[List[Category]] = category*

    lazy val category: PackratParser[Category] = 
        (( name ~ wS ~":" ~ wSNL ~ taskList ^^ {case n ~ _ ~ ":" ~ _ ~ list => Category(n, list)}))

    lazy val taskList: PackratParser[List[Task]] = 
        (( necessaryWS ~ task ~ taskList ^^ {case _ ~ t  ~ list => t :: list})
         |("" ^^^ List()))

    lazy val task:PackratParser[Task] = 
        ((name ~ " due " ~ wS ~ day ~ wS ~ precedence  ^^ {case n ~ " due " ~ _ ~ d ~ _ ~ p => Task(n,d,p)})
         |(name ~ " by " ~ wS ~ day ~ wS ~ precedence ^^ {case n ~ " by " ~ _ ~ d ~ _ ~ p => Task(n,d,p)}))

    //Parses the days of the week, allowing for variation in naming
    lazy val day:PackratParser[DaysOfWeek] = 
        ((name ^^ {case n if n.toLowerCase.startsWith("m") => DaysOfWeek.Monday
                   case n if n.toLowerCase.startsWith("w") => DaysOfWeek.Wednesday
                   case n if n.toLowerCase.startsWith("f") => DaysOfWeek.Friday
                   case n if n.toLowerCase.startsWith("tu") || (n.toLowerCase == "t") => DaysOfWeek.Tuesday
                   case n if n.toLowerCase.startsWith("th") || (n.toLowerCase == "h") || (n.toLowerCase == "r") => DaysOfWeek.Thursday
                   case n if n.toLowerCase.startsWith("sa") || (n.toLowerCase == "s") => DaysOfWeek.Saturday
                   case n if n.toLowerCase.startsWith("su") || (n.toLowerCase == "u") => DaysOfWeek.Sunday}))


    //Parses name of each category or task
    lazy val name: Parser[String] = """([0-9]|[a-z]|[A-Z]|_)+""".r

    lazy val precedence: Parser[Int] = ((wSNL ^^^ 0)
                                        | ("""(!)+""".r ~ wSNL ^^ {case e ~ _ => e.toString.length})
                                        | ("""(0|[1-9]\d*)""".r ~ "!" ~ wSNL ^^ {case d ~ "!" ~ _ => d.toInt}))

    //This is whitespace that _must_ exist. It is used at the start of tasks
    lazy val necessaryWS: Parser[String] = """(\t| )+""".r 

    //This is whitespace that may or may not exist. It will exist throughout the parser
    lazy val wS: Parser[String] = """(\t| )*""".r

    //Whitespaces with newlines
    lazy val wSNL: Parser[String] = """((\t| )*\n)+""".r
}

object REPLParser extends JavaTokenParsers with RegexParsers
{
    def apply(s:String): ParseResult[(String,List[Int])] = parseAll(command, s)
    def command: Parser[(String, List[Int])] = removeItem | switch | exit
    def removeItem: Parser[(String, List[Int])] = (("remove task" ~ """(0|[1-9]\d*)""".r ^^ {case "remove task" ~ d => ("delete",List(d.toInt))}))
    def exit: Parser[(String, List[Int])] = ("exit" ^^^ ("exit",List()))
    def switch:Parser[(String, List[Int])] = (("switch tasks" ~ """(0|[1-9]\d*)""".r ~ "and" ~ """(0|[1-9]\d*)""".r ^^ {case _ ~ d1 ~ _ ~ d2 => ("switch", List(d1.toInt, d2.toInt))}))
}

