import java.io.File
import org.slf4j.LoggerFactory
import play.api._

object Global extends GlobalSettings {

  override def onLoadConfig(config: Configuration, path: File, classloader: ClassLoader, mode: Mode.Mode): Configuration = {
    LoggerFactory.getLogger(getClass).info("Application configuration loading")
    val applicationDir = config.getString("appDir") match {
      case Some(pathString) => pathString
      case None => config.getString("user.dir").getOrElse(path) + "/migthycrawler"
    }
    val file = new File(applicationDir)
    file.mkdirs()
    val from: Configuration = Configuration.from(Map("appDir" -> applicationDir))
    super.onLoadConfig( config ++ from, path, classloader, mode)
  }
}
