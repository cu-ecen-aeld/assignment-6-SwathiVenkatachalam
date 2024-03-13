#! /bin/sh

# Ref: Coursera Week 4 Linux System Initialization Video
# From Assignment 5 base_externel/rootfs_overlay/etc/S98lddmodules.sh
# From Assignment 7 misc-modules/module_load and module_unload
# Modified to just include misc-modules

case "$1" in
    start)
        echo "Starting module"

        # Use the same name for the device as the name used for the module
        device="faulty"
        module="faulty"
        # Support read/write for owner and group, read only for everyone using 644
        mode="664"
        
        set -e
        # Group: since distributions do it differently, look for wheel or use staff
        # These are groups which correspond to system administrator accounts
        if grep -q '^staff:' /etc/group; then
            group="staff"
        else
            group="wheel"
        fi

        echo "Load our module, exit on failure"
        # Added insmode for faulty.ko with path
        # Instead of modprobe $module $* || exit 1
        insmod "/lib/modules/5.15.148-yocto-standard/faulty.ko" || exit 1
        echo "Get the major number (allocated with allocate_chrdev_region) from /proc/devices"
        major=$(awk "\$2==\"$module\" {print \$1}" /proc/devices)
        if [ ! -z ${major} ]; then
            echo "Remove any existing /dev node for /dev/${device}"
            rm -f /dev/${device}
            echo "Add a node for our device at /dev/${device} using mknod"
            mknod /dev/${device} c $major 0
            echo "Change group owner to ${group}"
            chgrp $group /dev/${device}
            echo "Change access mode to ${mode}"
            chmod $mode  /dev/${device}
        else
            echo "No device found in /proc/devices for driver ${module} (this driver may not allocate a device)"
        fi

        modprobe hello
        ;;
    stop)
        echo "Stopping module"
        
        device="faulty"

        # invoke rmmod with all arguments we got
        rmmod faulty || exit 1

        # Remove stale nodes
        rm -f /dev/${device}

        rmmod hello
        ;;
    *)
        echo "Usage: $0 {start|stop}"
    exit 1
esac

exit 0
