package todolist

import todolist.ir._

import DaysOfWeek._
package object semantics {
    def eval(tuple: (List[Category], DaysOfWeek)):FinalList = {
        val il = InitialList(tuple._1, tuple._2)
        return il.sortCategories
    }

    def REPLeval(tuple:(String, List[Int]), lists:FinalList):FinalList = {
    	tuple._1 match {
    		case "delete" => lists.removeTask(tuple._2(0)-1)
    		case "switch" => lists.switchTasks(tuple._2(0)-1, tuple._2(1)-1)
    		case "exit" => sys.exit(0)
    	}
    }
}