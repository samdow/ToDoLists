package todolist.parser

import org.scalatest._
import todolist.ir._
import todolist.parser._
import DaysOfWeek._

class parserTests extends FunSuite with Matchers {
  // some syntactic sugar for expressing parser tests
  implicit class ParseResultChecker(input: String) {
    def ~>(output: List[Category]) = {
      val result = ToDoListParser(input)
      result.successful && result.get == output
    }
  }

  test("empty category") {
  	"""dsls: 


  	""" ~> List(Category("dsls"))
  }
  
  test("one category") {
  	"""dsls:
  		notebook due Sun

  	""" ~> List(Category("dsls", List(Task("notebook", Sun))))
  }

  test("multiple categories") {
  	"""dsls:
        	notebook due Sun
        	critique due Tue

       algs:
       		Problem Set due Sun

  	""" ~> List(Category("dsls", List(Task("notebook", Sun), Task("critique", Tue))),
  				Category("algs", List(Task("Problem Set", Sun))))
  }

  test("does this pass everything?") {
  	""" """ ~> List(Category("dsls", List()))
  }
}
