import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import scalariform.formatter.preferences._

name := "dataPivot"

version := "0.1"

scalaVersion := "2.13.1"

ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(SpaceBeforeColon, false)
  .setPreference(CompactControlReadability, true)
  .setPreference(SpacesAroundMultiImports, false)
  .setPreference(FormatXml,false)
