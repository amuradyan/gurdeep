name := "gurdeep"
scalaVersion := "2.11.12"
version := "0.1"

resolvers += "scalac repo" at "https://raw.githubusercontent.com/ScalaConsultants/mvn-repo/master/"

libraryDependencies ++= Seq("io.scalac" %% "slack-scala-bot-core" % "0.2.1")