# ToDoLists
To use this DSL, run the following command while in the ToDoLists folder:  
`sbt run-main todolist.ToDoList path/to/list/file`

## Example 1-Basics
The most basic program has categories with tasks. It would be in a text file. One basic example looks like this:
```
Physics:
    PSet due Monday
Chemistry:
    Lab due Tues
```
The unindented words followed by colons are categories and the indented lines are tasks.
This example will produce:
```
PSet for Physics due Monday
Lab for Chemistry due Tuesday
```
### Whitespace
The tasks must have some amount of whitespace in front of the name of the task.
It can be tabs or spaces--so long as there is at least some whitespace. Otherwise, whitespace is ignored. 

### Syntax of Days
Users should be able to write days in whatever way they please. Specifically, anything that starts 
with Th, H, or R will be Thursday and anything else that starts with T will be interpreted as Tuesday. 
Similarly, anything that starts with Su or U will be Sunday and anything else that starts with S will be 
interpreted as Saturday.  
Additionally, all programs without a specific note at the start of the program will assume to start on Sunday.
This means that they will list tasks due Sunday first, Monday next, and so on.

## Example 2-Precedence
This next program shows the feature of precedence that users can write. Users denote precedence with exclamation
points at the end of the line of a task. They can either have a line of exclamation points or a number followed by
a single exclamation point. A larger number or more exclamation points means that it has a higher precedence.
```
Physics:
    PSet due Monday !!!
Chemistry:
    Lab due Tues    !!
    Exam by Tu 5!
```
The separation of days is more important than the precedence denoted by exclamation points. So, in the example, the task due Monday
will still be put before the ones due Tuesday. The tasks on Tuesday will be sorted based on which one has a higher
precedence. so, this example produces:
```
PSet for Physics due Monday
Exam for Chemistry due Tuesday
Lab for Chemitsry due Tuesday
```


