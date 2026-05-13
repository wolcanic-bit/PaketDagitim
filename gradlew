#!/bin/sh
APP_HOME=`pwd -P`
exec $APP_HOME/gradle/wrapper/gradle $@
