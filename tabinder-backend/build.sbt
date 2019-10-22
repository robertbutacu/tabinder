name := "tabinder"

version := "1.0"

lazy val `tabinder` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
resolvers += "Sonatype OSS" at "https://oss.sonatype.org/content/groups/public"

val reactiveMongoVer = "0.16.0-play26"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(jdbc, ws, specs2 % Test, guice)
libraryDependencies ++= Seq(
  "eu.timepit"        %% "refined"                  % "0.9.4",
  "org.typelevel"     %% "cats-core"                % "1.6.0",
  "org.typelevel"     %% "cats-effect"              % "1.2.0",
  "org.scalacheck"    %% "scalacheck"               % "1.14.0"         % "test",
  "org.reactivemongo" %% "reactivemongo-play-json"  % "0.16.0-play26",
  "org.reactivemongo" %% "play2-reactivemongo"      % reactiveMongoVer,
  "be.venneborg"      %% "play26-refined"           % "0.3.0",
  "org.scalatest"     %% "scalatest"                % "3.0.4"          % "test",
)

routesImport ++= Seq(
  "be.venneborg.refined.play.RefinedPathBinders._",
  "be.venneborg.refined.play.RefinedQueryBinders._",
  "models.types.Types._" //This depends on the refined types you want to use
)

unmanagedResourceDirectories in Test <+= baseDirectory(_ / "target/web/public/test")

      