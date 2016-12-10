/*
 * IR for ToDoLists
 * Allows for Categories and Lists, which will be compiled into a 
 * FinalList
 * 
 */
package todolist.ir

object DaysOfWeek extends Enumeration{
    type DaysOfWeek = Value
    val Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday = Value
}

import DaysOfWeek._
case class Task(name: String, dueDate: DaysOfWeek, priority:Int = 1) {
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

case class Category(name:String, tasks: List[Task] = Nil) {
    //def addTask(task:Task) = {tasks = tasks:+ task}
    override def toString = {
        var finalString = ""
        for(task <- tasks) {
            finalString = finalString ++ s"${task.name} for ${name} is due ${task.dueDate} precedence ${task.priority}\n"
        }
        finalString
    }
}

case class FinalList(toDoTasks: List[(String,Task)], finishedTasks: List[(String,Task)] = Nil) {
    override def toString = {
            var index = 1
            var finalString = "To Do List:"
            for((category, task) <- toDoTasks) {
                finalString = finalString ++ s"\n${index}) ${task.name} for ${category} is due ${task.dueDate} precedence ${task.priority}"
                index = index + 1
            }
            if(finishedTasks.nonEmpty) {
                finalString = finalString ++ "\n\n Finished Tasks:"
                for((category, task) <- finishedTasks) {
                    finalString = finalString ++ s"\n${task.name} for ${category} is due ${task.dueDate} precedence ${task.priority}"
            }
            }
            finalString
    }

    def removeTask(index:Int):FinalList = {
        val doneTasks = toDoTasks(index) :: finishedTasks
        FinalList(toDoTasks diff List(toDoTasks(index)), doneTasks)
    }

    def switchTasks(index1:Int, index2:Int):FinalList = {
        if(index1 == index2) {
            FinalList(toDoTasks, finishedTasks)
        }
        else {
            val min = List(index1, index2).min
            val max = List(index1,index2).max
            val split = toDoTasks.splitAt(min)
            val switched1 = split._2(0)
            val split2 = split._2.splitAt(max-min)
            val switched2 = split2._2(0)
            val newToDo = split._1 ++ (switched2 :: split2._1.tail) ++ (switched1 :: split2._2.tail)
            FinalList(newToDo, finishedTasks)
        }
    }
}

case class InitialList(tasks: List[Category], startDay:DaysOfWeek) {
    def merge(category: Category, runningList: FinalList): FinalList = {
        def mergeHelper(task:Task) = (category.name, task)
        FinalList(runningList.toDoTasks ++ category.tasks.map(mergeHelper))
    }

    def sortCategories: FinalList = tasks match {
        case List() => FinalList(List())
        case firstTask :: otherTasks => {
            val newIL = InitialList(otherTasks, startDay)
            val flist = merge(firstTask, newIL.sortCategories)
            FinalList(flist.toDoTasks.sortWith((t1, t2)=> t1._2.compareTo(t2._2, startDay)))
        }
    }
}
