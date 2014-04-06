import sbt._
import com.typesafe.sbt.packager.Keys._
import sbt.Keys._
import com.typesafe.sbt.SbtNativePackager._
import scala.Some

name := "mightycrawlerlauncher"

version := "1.4"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  "no.bekk.bekkopen" % "mightycrawler" % "0.10",
  "mysql" % "mysql-connector-java" % "5.1.25"
)

play.Project.playScalaSettings ++ Seq(
  name in Rpm := "mightycrawlerlauncher",
  version in Rpm := "4",
  rpmRelease := "4",
  packageSummary := "Webapp for crawling sites",
  rpmVendor := "Kantega",
  rpmUrl := Some("http://kantega.no"),
  rpmLicense := Some("Apache 2"),
  packageDescription := "Crawls a site and reports status",
  rpmGroup := Some("www"),
  name in Debian := "mightycrawlerlauncher",
  maintainer in Debian := "Kantega",
  version in Debian := "4"
)
