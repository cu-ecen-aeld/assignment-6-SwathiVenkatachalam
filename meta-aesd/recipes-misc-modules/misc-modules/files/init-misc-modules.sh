#! /bin/sh

# Ref: Coursera Week 4 Linux System Initialization Video
# From Assignment 5 base_externel/rootfs_overlay/etc/S98lddmodules.sh
# Modified to just include misc-modules

case "$1" in
    start)
        echo "Starting module"
        /usr/bin/module_load faulty
        modprobe hello
        ;;
    stop)
        echo "Stopping module"
        /usr/bin/module_unload
        rmmod hello
        ;;
    *)
        echo "Usage: $0 {start|stop}"
    exit 1
esac

exit 0
