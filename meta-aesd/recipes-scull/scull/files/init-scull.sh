#! /bin/sh

# Ref: Coursera Week 4 Linux System Initialization Video
# From Assignment 5 base_externel/rootfs_overlay/etc/S98lddmodules.sh
# Modified to just include scull

case "$1" in
    start)
        echo "Starting scull"
        /usr/bin/scull_load
        ;;
    stop)
        echo "Stopping scull"
        /usr/bin/scull_unload
        ;;
    *)
        echo "Usage: $0 {start|stop}"
    exit 1
esac

exit 0
