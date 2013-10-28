name := "mightycrawlerlauncher"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  "no.bekk.bekkopen" % "mightycrawler" % "0.7"
)

play.Project.playScalaSettings
