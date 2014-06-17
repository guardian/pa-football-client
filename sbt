#!/bin/sh

# all feed times are in Europe/London, so setting it as default here so our tests will run anywhere
java -Duser.timezone=Europe/London -Xms512M -Xmx1536M -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=384M -jar `dirname $0`/sbt-launch.jar "$@"
