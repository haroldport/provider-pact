package com.latamautos.serializers

/**
  * Created by Harold on 23/11/16.
  */
import java.sql.Timestamp

import org.json4s.CustomSerializer
import org.json4s.JsonAST.{JInt, JNull}

object CustomSerializers {
  val all = List(CustomTimestampSerializer)
}

case object CustomTimestampSerializer extends CustomSerializer[Timestamp](format =>
  ({
    case JInt(x) => new Timestamp(x.longValue * 1000)
    case JNull => null
  },
    {
      case date: Timestamp => JInt(date.getTime / 1000)
    }))
