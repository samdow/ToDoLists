package todolist

import todolist.ir._

import DaysOfWeek._

/*
 * Semantics for ToDoLists by Samantha Andow
 */
package object semantics {
    // The evaluation used intially to get the list developed from the text file
    def eval(tuple: (List[Category], DaysOfWeek)):FinalList = {
        val il = InitialList(tuple._1, tuple._2)
        return il.toFinal.sort
    }

    //The evaluation that allows the users to 
    def REPLeval(tuple:(String, List[Int]), lists:FinalList):FinalList = {
        
        //Check that all numbers given are valid indices of tasks
        if(tuple._2.filter((i:Int)=>i > lists.toDoTasks.length) == Nil) {
            tuple._1 match {
                case "exit"   => sys.exit(0)
                case "delete" => lists.removeTask(tuple._2(0)-1)
                case "switch" => lists.switchTasks(tuple._2(0)-1, tuple._2(1)-1)
            }
        }

        // If they are all right, it will delete, switch, or exit as the user requested
        else {
            println("\nAll numbers must be valid indices of tasks in the to do list\n")
            lists
        }
    }
}