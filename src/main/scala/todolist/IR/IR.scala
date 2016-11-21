/*
 * IR for ToDoLists
 * Allows for Categories and Lists, which will be compiled into a 
 * FinalList
 * 
 */
package todolist.ir

object DaysOfWeek extends Enumeration{
    type DaysOfWeek = Value
    val Mon, Tue, Wed, Thu, Fri, Sat, Sun = Value
}

import DaysOfWeek._
case class Task(name: String, dueDate: DaysOfWeek, priority:Int = 1) {
    def compareTo(task2:Task, startDay:DaysOfWeek) = {
        if(dueDate == task2.dueDate)
            priority < task2.priority
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

case class Category(name:String, tasks: List[Task] = Nil) {
    //def addTask(task:Task) = {tasks = tasks:+ task}
    override def toString = {
        var finalString = ""
        for(task <- tasks) {
            finalString = finalString ++ s"${task.name} for ${name} is due ${task.dueDate}\n"
        }
        finalString
    }
}

case class FinalList(tasks: List[(String,Task)]) {
    override def toString = {
        var finalString = ""
        for((category, task) <- tasks) {
            finalString = finalString ++ s"${task.name} for ${category} is due ${task.dueDate}\n"
        }
        finalString
    }
}

case class InitialList(tasks: List[Category], startDay:DaysOfWeek) {
    def merge(category: Category, runningList: FinalList): FinalList = {
        def mergeHelper(task:Task) = (category.name, task)
        FinalList(runningList.tasks ++ category.tasks.map(mergeHelper))
    }

    def sortCategories: FinalList = tasks match {
        case List() => FinalList(List())
        case firstTask :: otherTasks => {
            val newIL = InitialList(otherTasks, startDay)
            val flist = merge(firstTask, newIL.sortCategories)
            FinalList(flist.tasks.sortWith((t1, t2)=> t1._2.compareTo(t2._2, startDay)))
        }
    }
}
