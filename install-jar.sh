#!/bin/bash

if ! [[ -v fxyz_version ]]
then fxyz_version=2016.0.0.1
fi

# at the time of this writing, group and artifact are unofficially assumed to just be FXyz
exec mvn install:install-file \
     -DgroupId=org.fxyz \
     -DartifactId=FXyz \
     -Dpackaging=jar \
     -Dversion=$fxyz_version \
     -Dfile=dist/FXyzLib.jar \
     -DgeneratePom=true
