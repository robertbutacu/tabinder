name := "tabinder"

version := "1.0"

lazy val `tabinder` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
resolvers += "Sonatype OSS" at "https://oss.sonatype.org/content/groups/public"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(jdbc, ehcache, ws, specs2 % Test, guice)
libraryDependencies ++= Seq(
  "eu.timepit"        %% "refined"            % "0.9.4",
  "com.olegpy"        %% "meow-mtl"           % "0.2.0",
  "org.scalacheck"    %% "scalacheck"         % "1.14.0",
  "ai.snips"          %% "play-mongo-bson"    % "0.5.1",
  "org.mongodb.scala" %% "mongo-scala-driver" % "2.5.0",
  "be.venneborg"      %% "play26-refined"          % "0.3.0"
)
routesGenerator := InjectedRoutesGenerator

routesImport ++= Seq(
  "be.venneborg.refined.play.RefinedPathBinders._",
  "be.venneborg.refined.play.RefinedQueryBinders._",
  "models.types.Types.Tuning", //This depends on the refined types you want to use
  "models.types.Types.Artist", //This depends on the refined types you want to use
  "models.types.Types.SongName" //This depends on the refined types you want to use
)

unmanagedResourceDirectories in Test <+= baseDirectory(_ / "target/web/public/test")

      