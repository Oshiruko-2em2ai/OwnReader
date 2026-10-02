#!/bin/sh

#
# Copyright 2015 the original author or authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

# Attempt to set APP_HOME
# Resolve links: $0 may be a symlink
app_path=$0

# Need this for daisy-chained symlinks.
while
    APP_HOME=${app_path%"${app_path##*/}"}
    [ -n "$APP_HOME" ]
do
    app_path=$(readlink "$app_path") || break
done

APP_HOME=$(cd "${APP_HOME%.}" && pwd -P) || exit

# Use the maximum available, or set MAX_FD != -1 to use that value.
MAX_FD=maximum

warn() {
    echo "$*" >&2
}

die() {
    echo
    echo "$*"
    echo
    exit 1
}

# OS specific support (must be 'true' or 'false').
cygwin=false
msys=false
win32=false
native=false

case "$(uname)" in
CYGWIN* | MINGW* | MSYS* )
    cygwin=true
    ;;
esac

if [ "$cygwin" = true ] || [ "$msys" = true ]; then
    [ -z "$JAVA_HOME" ] && JAVA_HOME=$(dirname "$(command -v javac)")
    JAVA_HOME=$(cd "$JAVA_HOME" && pwd)
fi

if [ -z "$JAVA_HOME" ]; then
    javaExecutable=$(command -v java)
    [ -z "$javaExecutable" ] && die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH."
    javaExecutable=$(cd "$(dirname "$javaExecutable")" && pwd)
    JAVA_HOME=$(dirname "$javaExecutable")
fi

if [ ! -x "$JAVA_HOME/bin/java" ] && ! [ -x "$JAVA_HOME/bin/java.exe" ]; then
    die "ERROR: JAVA_HOME is set to \"$JAVA_HOME\", but \"$JAVA_HOME/bin/java\" is not executable by you."
fi

JAVA_EXE="$JAVA_HOME/bin/java"
PROG="$0"

# Increase the maximum file descriptors if we can.
if ! "$cygwin" && ! "$msys" ; then
    case $MAX_FD in #(
        max*)
            # In POSIX sh, ulimit -H is undefined. That's why the result is checked to see if it worked.
            # shellcheck disable=SC3045
            MAX_FD=$(ulimit -H -n) ||
                warn "Could not query system maximum file descriptor limit"
            case $MAX_FD in #(
                ''|soft) :;; #(
                *)
                    # In POSIX sh, ulimit -n may round down to even lower than $MAX_FD
                    # shellcheck disable=SC3045
                    ulimit -n "$MAX_FD" ||
                        warn "Could not set maximum file descriptor limit to $MAX_FD"
            esac
    esac
fi

# Collect all arguments for the java command, and put them in JAVA_OPTS
# so that people can pass -Xmx512m -Xms128m etc. directly to gradle.
if [ "$cygwin" = true ] || [ "$msys" = true ] ; then
    APP_HOME=$( cygpath --path --mixed "$APP_HOME" )
    CP=$(
        cygpath --path --mixed "$APP_HOME/gradle/wrapper/gradle-wrapper.jar"
    )
    CLASSPATH=$CP
    iojava_opts=$(
        echo "$JAVA_OPTS" | sed -e "s/'/\"'/g" -e "s/^  \(.*\) $ /\1/" | tr '\r\n' ' '
    )
    eval set -- $default_jvm_opts $iojava_opts -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
else
    eval set -- $DEFAULT_JVM_OPTS -classpath "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain "$@"
fi

exec "$JAVA_EXE" "$@"
