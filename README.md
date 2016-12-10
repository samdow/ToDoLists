# ToDoLists
## Use
To use this DSL, run the following command while in the ToDoLists folder:  
`sbt "run-main todolist.ToDoList path/to/list/file"`

# Documentation
## Example 1-Basics
The most basic program has categories with tasks. It would be in a text file. One basic example looks like this:
```
Physics:
    PSet due Monday
Chemistry:
    Lab due Tues
```
Users could use the keywords due or by. The unindented words followed by colons are categories and the indented lines are tasks.
This example will produce:
```
1) PSet for Physics due Monday
2) Lab for Chemistry due Tuesday
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
1) PSet for Physics due Monday
2) Exam for Chemistry due Tuesday
3) Lab for Chemitsry due Tuesday
```
If two tasks were to have the same day and same precedence, they will be ordered arbitrarily.

## Example 3-Setting the Day
To set what day it is, the program requires an extra line at the top of the file. The language is currently only able to look
at a one week time period. An example program:
```
Today is Tuesday
Physics:
    Problem Set due Monday !!!
    PSet due R
Chemistry:
    Lab due Tues    !!
    Exam by Tu 5!
```
This will produce:
```
1) Exam for Chemistry due Tuesday
2) Lab for Chemitsry due Tuesday
3) PSet for Physics due Thursday
4) Problem Set for Physics due Monday
```

## Example 4-Updating the List Using Input/Output
After you've produced your list, the language will have an place to input another command.
You can either remove a task, indicating that it's done, switch two tasks, indicating that you want
a different order for the list, or exit, indicating that you are done. As an example, a user may have
the following list as an output from a program: 
```
1) Exam for Chemistry due Tuesday
2) Lab for Chemitsry due Tuesday
3) PSet for Physics due Thursday
4) Problem Set for Physics due Monday
```
Then, the user can use different commands to get different outputs:
```
> switch tasks 2 and 4
1) Exam for Chemistry due Tuesday
2) Problem Set for Physics due Monday
3) PSet for Physics due Thursday
4) Lab for Chemitsry due Tuesday
> remove task 3
To Do List:
1) Exam for Chemistry due Tuesday
2) Problem Set for Physics due Monday
3) Lab for Chemitsry due Tuesday

Finished Tasks:
PSet for Physics due Thursday
> switch tasks 2 and 1
To Do List:
1) Problem Set for Physics due Monday
2) Exam for Chemistry due Tuesday
3) Lab for Chemitsry due Tuesday

Finished Tasks:
PSet for Physics due Thursday
> exit
```
Right now, the program only supports those 3 commands. Any other command will result in the program printing out
the possible valid commands.
