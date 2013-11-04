name := "mightycrawlerlauncher"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  "no.bekk.bekkopen" % "mightycrawler" % "0.8-SNAPSHOT"
)

play.Project.playScalaSettings
