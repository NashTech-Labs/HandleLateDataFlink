name := "HandleLateDataFlink"

version := "0.1"

scalaVersion := "2.12.10"

libraryDependencies ++= Seq("org.apache.flink" %% "flink-streaming-scala" % "1.7.0",
                            "org.apache.flink" %% "flink-connector-kafka" % "1.7.0",
  "org.apache.flink" %% "flink-scala" % "1.7.0")

