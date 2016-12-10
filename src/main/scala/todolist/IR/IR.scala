/*
 * IR for ToDoLists by Samantha Andow
 * Creates the data structures for days, tasks, and categories. These will be
 * compiled into inital lists which will end up as final lists.
 * 
 */
package todolist.ir

// Possible days of the week
object DaysOfWeek extends Enumeration{
    type DaysOfWeek = Value
    val Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday = Value
}

import DaysOfWeek._
/*
 * The data structure for tasks allows for the name of the task, its due date,
 * and its priority. Priority is unnecessary, so it will be default initalized
 * to 1.
 */
case class Task(name: String, dueDate: DaysOfWeek, priority:Int = 1) {
    
    // This allows 2 tasks to be compared. They are first compared based
    // on their due dates(relative to the start day). If they are due on 
    // the same day, they are ranked based on priority
    def compareTo(task2:Task, startDay:DaysOfWeek) = {
        if(dueDate == task2.dueDate)
            priority > task2.priority
        else if ((dueDate >= startDay) && (task2.dueDate < startDay)) 
            true
        else if ((task2.dueDate >= startDay) && (dueDate < startDay))
            false
        else
            dueDate < task2.dueDate
    }

    override def toString = 
        s"${name} is due ${dueDate}"
}

/* 
 * The data structure for categories is the name of the category and a list of tasks
 */
case class Category(name:String, tasks: List[Task] = Nil) {
    override def toString = {
        var finalString = ""
        for(task <- tasks) {
            finalString = finalString ++ s"${task.name} for ${name} is due ${task.dueDate} precedence ${task.priority}\n"
        }
        finalString
    }
}

/*
 * The data structure for the inital list. This is necessary because the lists
 * come in initally as a bunch of categories, so this allows it to be turned
 * into a final list.
 */
case class InitialList(tasks: List[Category], startDay:DaysOfWeek) {
    //This takes in an inital list and makes it into a final list.
    def toFinal: FinalList = {
        //These helper functions help flat map each category
        def merge(category: Category, runningList: FinalList): FinalList = {
            def mergeHelper(task:Task) = (category.name, task)
            FinalList(runningList.toDoTasks ++ category.tasks.map(mergeHelper))
        }

        //This recursively flat maps each category and puts them together
        //into one list
        tasks match {
            case List() => FinalList(List())
            case firstCategory :: otherCategory => {
                val newIL = InitialList(otherCategory, startDay)
                merge(firstCategory, newIL.toFinal)
            }
        }
    }
}

/*
 * The final list created will have two lists--one that has the to do list in
 * order and one that has the finished tasks in no particular order.
 */
case class FinalList(toDoTasks: List[(String,Task)], finishedTasks: List[(String,Task)] = Nil, startDay:DaysOfWeek = DaysOfWeek.Sunday) {
    // Pretty printer for the lists
    override def toString = {
            var index = 1
            var finalString = "To Do List:"
            for((category, task) <- toDoTasks) {
                finalString = finalString ++ s"\n${index}) ${task.name} for ${category} is due ${task.dueDate} precedence ${task.priority}"
                index = index + 1
            }
            if(finishedTasks.nonEmpty) {
                finalString = finalString ++ "\n\nFinished Tasks:"
                for((category, task) <- finishedTasks) {
                    finalString = finalString ++ s"\n${task.name} for ${category} is due ${task.dueDate} precedence ${task.priority}"
            }
            }
            finalString
    }

    //This sorts the list based on the comparison of tasks (days, then priority)
    def sort:FinalList = FinalList(toDoTasks.sortWith((t1, t2)=> t1._2.compareTo(t2._2, startDay)), finishedTasks, startDay)

    //Allows the user to remove a task from the to do list. It gets put in the
    //finished task list. This removes it based on its index, indexed from 0
    def removeTask(index:Int):FinalList = {
        val doneTasks = toDoTasks(index) :: finishedTasks
        FinalList(toDoTasks diff List(toDoTasks(index)), doneTasks)
    }

    //This allows users to switch two tasks in the to do list based on their
    //indices, indexed from 0
    def switchTasks(index1:Int, index2:Int):FinalList = {

        //If the user is trying to switch the same task, it's the same list
        if(index1 == index2) {
            FinalList(toDoTasks, finishedTasks)
        }

        //This splits the list into 3--before the smaller index, between the
        //two indices, and after the larger index. This way we are able to piece
        //the list back together with only the two elements switched
        else {
            //We don't gurantee that the smaller index is first, so we must
            //find out which one is smaller
            val min = List(index1, index2).min
            val max = List(index1,index2).max

            //split._1 is every element before the smaller index
            val split = toDoTasks.splitAt(min)
            //switched1 is one of the 2 switched elements
            val switched1 = split._2(0)

            //split2._1 is the elements between the two indices. We have
            //saved the head in switched1
            val split2 = split._2.tail.splitAt(max-min-1)

            //switched2 is the second of the switched elements
            val switched2 = split2._2(0)
            val newToDo = split._1 ++ (switched2 :: split2._1) ++ (switched1 :: split2._2.tail)
            FinalList(newToDo, finishedTasks)
        }
    }
}
