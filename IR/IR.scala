object Test extends App {
    object DaysOfWeek extends Enumeration{
        type DaysOfWeek = Value
        val Mon, Tue, Wed, Thu, Fri, Sat, Sun = Value
    }

    import DaysOfWeek._
    case class Task(name: String, dueDate: DaysOfWeek)
    case class Category(name:String, tasks: List[Task] = Nil) {
        //def addTask(task:Task) = {tasks = tasks:+ task}
    }

    def merge(category: Category, runningList: List[Task]): List[Task] = {
        runningList ++ category.tasks
    }

    def sortCategories(categories: Category *): List[Task] = {
        if (categories.isEmpty) 
            List()
        else 
            merge (categories.head, sortCategories(categories.tail :_ *)).sortWith((t1, t2)=> t1.dueDate < t2.dueDate)
    }

    val dsls = Category("dsls", List(Task("Notebook", Sun), Task("Critique", Tue)))
    val algs = Category("algs", List(Task("PSet", Mon)))
    val anthro = Category("anthro", List(Task("Read Book", Thu), Task ("Read Article", Tue)))


    println(sortCategories(dsls, algs, anthro))
}
