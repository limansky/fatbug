lazy val fatbug = (project in file("."))
  .settings(
    organization := "me.limansky",
    name := "fatbug",
    version := "0.1",
    Compile / assembly / artifact := {
      val art = (Compile / assembly / artifact).value
      art.withClassifier(Some("assembly"))
    },
    addArtifact(Compile / assembly / artifact, assembly),
    docker / dockerfile := {
      val artifact = (Compile / packageBin / assembly).value
      new Dockerfile {
        from("openjdk:8-alpine3.9")
        workDir("/opt")
        copy(artifact, ".")
        entryPoint("java", "-jar", artifact.name)
      }
    },
    docker / imageNames := Seq(
      ImageName(repository = name.value, tag = Some(version.value))
    )
  )
  .enablePlugins(sbtdocker.DockerPlugin)
