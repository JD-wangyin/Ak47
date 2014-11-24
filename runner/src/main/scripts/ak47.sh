#!/bin/bash
# Ak47 main startup


# Usage
usage(){
    echo "$0 {full_name_class} {params}"
    echo ""
}


# global var
AK47_APP_DIR="$(readlink -f `dirname $0`/..)"
AK47_APP_CONF_DIR="${AK47_APP_DIR}/conf"
AK47_APP_BIN_DIR="${AK47_APP_DIR}/bin"
AK47_APP_DATA_DIR="${AK47_APP_DIR}/data"
AK47_APP_LOG_DIR="${AK47_APP_DIR}/log"
AK47_APP_LIB_DIR="${AK47_APP_DIR}/lib"


# check params
if [ $# -eq 0 ];then
    usage
    exit 0
fi


# go!
mainclass="$1"
shift
java -jar $AK47_APP_BIN_DIR/ak47-boot.jar "$mainclass" $@



