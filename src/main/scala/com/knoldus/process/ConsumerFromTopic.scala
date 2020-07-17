package com.knoldus.process

import java.util.Properties

import com.knoldus.models.{News, NewsDeserializer}
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment}
import org.apache.flink.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer

object ConsumerFromTopic extends App {
  val properties = new Properties()
  properties.setProperty("bootstrap.servers", "localhost:9092")
  properties.setProperty("zookeeper.connect", "localhost:2181")
  properties.setProperty("group.id", "test")
  val env = StreamExecutionEnvironment.getExecutionEnvironment
  env.setParallelism(10)
  val stream = env
    .addSource(new FlinkKafkaConsumer[News]("my-topic", new NewsDeserializer(), properties))
    .keyBy(x=>x.priority)
    stream.countWindow(5).max("priority").setParallelism(10)
    .print()
  env.execute("Consume News from Topic")
}
