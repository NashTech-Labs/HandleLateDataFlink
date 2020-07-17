package com.knoldus.publish

import java.sql.Timestamp
import java.util.UUID

import com.knoldus.models.{News, NewsSerializer}
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.streaming.api.functions.source.{RichParallelSourceFunction, SourceFunction}
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer

import scala.util.Random

object PublishToTopic extends App {
  implicit val typeInfo = TypeInformation.of(classOf[News])
  val env = StreamExecutionEnvironment.getExecutionEnvironment
  env.setParallelism(10)
  val dataStream: DataStream[News] = env.addSource(new DataGenerator())
  val myProducer = new FlinkKafkaProducer[News](
    "localhost:9092", // broker list
    "my-topic",// target topic
      new NewsSerializer)

  dataStream.addSink(myProducer)
  env.execute("Publish News to Topic")
}

class DataGenerator extends RichParallelSourceFunction[News] {
  var run: Boolean = true

  override def run(ctx: SourceFunction.SourceContext[News]): Unit = {

    val random = new Random()
    println("Producing Data...")
    while (run) {
      val instant = new Timestamp(System.currentTimeMillis())
      val uuid = UUID.randomUUID().toString
      val title = "titleName " + ('a' + random.nextPrintableChar)
      ctx.collect(News(uuid, title, instant,random.nextInt(10)))
      Thread.sleep(5000)
    }
  }

  override def cancel(): Unit = run = false
}