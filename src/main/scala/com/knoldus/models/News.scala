package com.knoldus.models

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream}
import java.sql.Timestamp

import org.apache.flink.api.common.serialization.{DeserializationSchema, SerializationSchema}
import org.apache.flink.api.common.typeinfo.TypeInformation
import java.io.ObjectOutputStream

case class News(id:String,title:String,date:Timestamp,priority:Int)

class NewsSerializer extends SerializationSchema[News]{

  override def serialize(element: News): Array[Byte] ={
    val bos = new ByteArrayOutputStream
    val oos = new ObjectOutputStream(bos)
    oos.writeObject(element)
    oos.flush()
    bos.toByteArray
  }
}

class NewsDeserializer extends DeserializationSchema[News]{

  override def deserialize(message: Array[Byte]): News = {
    val bis = new ByteArrayInputStream(message)
    val in = new ObjectInputStream(bis)
    in.readObject().asInstanceOf[News]
  }

  override def isEndOfStream(nextElement: News): Boolean = false

  override def getProducedType: TypeInformation[News] = TypeInformation.of(classOf[News])
}
