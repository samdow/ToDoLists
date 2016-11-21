package todolist

import todolist.ir._

import DaysOfWeek._
package object semantics {
    def eval(list: List[Category]):FinalList = {
        val il = InitialList(list, Wed)
        return il.sortCategories
    }
}