logLevel := Level.Warn
resolvers += Classpaths.typesafeReleases

addSbtPlugin("com.itv.plugins" % "scalapact-plugin" % "2.0.0")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.12.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.0.0-M4")

addSbtPlugin("com.typesafe.sbt" % "sbt-multi-jvm" % "0.3.9")

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.8.1")

addSbtPlugin("au.com.dius" %% "pact-jvm-provider-sbt" % "2.4.4")