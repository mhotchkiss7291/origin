1. Add the variable "ECLIPSE_DEBUG" line to the "start" section displayed below before 
   the _RUNJAVA (java) command within /usr/local/jakarta/bin/catalina.sh. This 
   allows for remote applications debugging to Eclipse through Tomcat.

*****************
elif [ "$1" = "start" ] ; then

  shift
  touch "$CATALINA_BASE"/logs/catalina.out
  if [ "$1" = "-security" ] ; then
    echo "Using Security Manager"
    shift
  echo "four"
    "$_RUNJAVA" $JAVA_OPTS $CATALINA_OPTS \
      -Djava.endorsed.dirs="$JAVA_ENDORSED_DIRS" -classpath "$CLASSPATH" \
      -Djava.security.manager \
      -Djava.security.policy=="$CATALINA_BASE"/conf/catalina.policy \
      -Dcatalina.base="$CATALINA_BASE" \
      -Dcatalina.home="$CATALINA_HOME" \
      -Djava.io.tmpdir="$CATALINA_TMPDIR" \
      org.apache.catalina.startup.Bootstrap "$@" start \
      >> "$CATALINA_BASE"/logs/catalina.out 2>&1 &

      if [ ! -z "$CATALINA_PID" ]; then
        echo $! > $CATALINA_PID
      fi
  else
  echo "five"

--->    ECLIPSE_DEBUG="-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5006"

--->    "$_RUNJAVA" $ECLIPSE_DEBUG $JAVA_OPTS $CATALINA_OPTS \
      -Djava.endorsed.dirs="$JAVA_ENDORSED_DIRS" -classpath "$CLASSPATH" \
      -Dcatalina.base="$CATALINA_BASE" \
      -Dcatalina.home="$CATALINA_HOME" \
      -Djava.io.tmpdir="$CATALINA_TMPDIR" \
      org.apache.catalina.startup.Bootstrap "$@" start \
      >> "$CATALINA_BASE"/logs/catalina.out 2>&1 &

      if [ ! -z "$CATALINA_PID" ]; then
        echo $! > $CATALINA_PID
      fi
  fi
***************


