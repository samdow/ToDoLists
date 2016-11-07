object Test extends App {
    object DaysOfWeek extends Enumeration{
        type DaysOfWeek = Value
        val Mon, Tue, Wed, Thu, Fri, Sat, Sun = Value
    }

    import DaysOfWeek._
    case class Task(name: String, dueDate: DaysOfWeek) {
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

    def merge(category: Category, runningList: FinalList): FinalList = {
        def mergeHelper(task:Task) = (category.name, task)
        FinalList(runningList.tasks ++ category.tasks.map(mergeHelper))
    }

    def sortCategories(categories: Category *): FinalList = {
        if (categories.isEmpty) 
            FinalList(List())
        else {
            val flist = merge(categories.head, sortCategories(categories.tail :_ *))
            FinalList(flist.tasks.sortWith((t1, t2)=> t1._2.dueDate < t2._2.dueDate))
        }
    }

    val dsls = Category("dsls", List(Task("Notebook", Sun), Task("Critique", Tue)))
    val algs = Category("algs", List(Task("PSet", Mon)))
    val anthro = Category("anthro", List(Task("Read Book", Thu), Task ("Read Article", Tue)))


    println(dsls)
    println(sortCategories(dsls, algs, anthro))
}
