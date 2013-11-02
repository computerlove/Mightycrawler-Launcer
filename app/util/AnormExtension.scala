package util
import anorm._
import java.time.format.DateTimeFormatter
import java.time.{ZoneId, ZoneOffset, LocalDateTime}

object AnormExtension {
  val dateFormatGeneration: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSS")

  implicit def rowToDateTime: Column[LocalDateTime] = Column.nonNull { (value, meta) =>
    val MetaDataItem(qualified, nullable, clazz) = meta
    value match {
      case ts: java.sql.Timestamp => Right(LocalDateTime.ofInstant(ts.toInstant, ZoneId.systemDefault()))
      case d: java.sql.Date => Right(LocalDateTime.ofInstant(d.toInstant, ZoneId.systemDefault()))
      case str: java.lang.String => Right(LocalDateTime.parse(str, dateFormatGeneration))
      case _ => Left(TypeDoesNotMatch("Cannot convert " + value + ":" + value.asInstanceOf[AnyRef].getClass) )
    }
  }

  implicit val dateTimeToStatement = new ToStatement[LocalDateTime] {
    def set(s: java.sql.PreparedStatement, index: Int, aValue: LocalDateTime): Unit = {
      s.setTimestamp(index, new java.sql.Timestamp(aValue.toInstant(ZoneOffset.UTC).toEpochMilli) )
    }
  }

}