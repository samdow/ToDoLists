name := "ToDoList"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= 
  Seq( "org.scalactic" %% "scalactic" % "3.0.0",
       "org.scalatest" %% "scalatest" % "3.0.0" % "test",
       "org.scalacheck" %% "scalacheck" % "1.12.5" % "test",
       "org.scalafx" % "scalafx_2.11" % "8.0.5-R5",
       "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4",
       "org.scala-lang" % "scala-compiler" % scalaVersion.value )

unmanagedClasspath in (Compile, runMain) += baseDirectory.value / "resources"
