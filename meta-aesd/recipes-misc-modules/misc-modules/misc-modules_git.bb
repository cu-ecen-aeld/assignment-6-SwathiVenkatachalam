# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

# WARNING: the following LICENSE and LIC_FILES_CHKSUM values are best guesses - it is
# your responsibility to verify that the values are complete and correct.
#
# The following license files were not able to be identified and are
# represented as "Unknown" below, you will need to check them yourself:
#   LICENSE
# Reference: [1] meta-aesd/recipes-aesd-assignments/aesd-assignments/aesd-assignment_git.bb
LICENSE = "MIT"
#LIC_FILES_CHKSUM = "file://LICENSE;md5=f098732a73b5f6f3430472f5b094ffdb"
# Added from [1]
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

#SRC_URI = "git://git@github.com/cu-ecen-aeld/assignment-7-SwathiVenkatachalam.git;protocol=ssh;branch=master"
# Added file init-scull.sh
SRC_URI = "git://git@github.com/cu-ecen-aeld/assignment-7-SwathiVenkatachalam.git;protocol=ssh;branch=master file://init-misc-modules.sh"

# Modify these as desired
PV = "1.0+git${SRCPV}"
SRCREV = "51876b23773c0f3e65d11a4ff5b328c9fe43cfa5"

# Updated work dir to misc-modules
S = "${WORKDIR}/git/misc-modules"

inherit module

# Added
MODULES_INSTALL_TARGET = "install"

# Modify the task-install in the .bb file to use the correct module folder for the M argument.  
# You can do this with an EXTRA_OEMAKE_append_task-install = " -C ${STAGING_KERNEL_DIR} M=${S}/scull"
# EXTRA_OEMAKE:append:task-install = " -C ${STAGING_KERNEL_DIR} M=${S}"
# Updated for misc-modules
EXTRA_OEMAKE:append_task-install = " -C ${STAGING_KERNEL_DIR} M=${S}/misc-modules"
EXTRA_OEMAKE += "KERNELDIR=${STAGING_KERNEL_DIR}"

# Added from [1]
# TODO: Add the aesdsocket application and any other files you need to install
# See https://git.yoctoproject.org/poky/plain/meta/conf/bitbake.conf?h=kirkstone
# FILES:${PN} += "${bindir}/aesdsocket"
# FILES:${PN} += "${bindir}/aesdsocket-start-stop.sh"

# Updated for modules
FILES:${PN} += "${bindir}/module_load"
FILES:${PN} += "${bindir}/module_unload"
FILES:${PN} += "${sysconfdir}/*"

# TODO: customize these as necessary for any libraries you need for your application
# (and remove comment)
# TARGET_LDFLAGS += "-pthread -lrt"

#Start Script Implementation in Yocto
#References class which handles install scripts
inherit update-rc.d 
#flag your package as one which uses init scripts
INITSCRIPT_PACKAGES = "${PN}"
# Updated for misc-modules
INITSCRIPT_NAME:${PN} = "init-misc-modules.sh"

do_configure () {
	:
}

do_compile () {
	oe_runmake
}

do_install () {
	# TODO: Install your binaries/scripts here.
	# Be sure to install the target directory with install -d first
	# Yocto variables ${D} and ${S} are useful here, which you can read about at 
	# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-D
	# and
	# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-S
	# See example at https://github.com/cu-ecen-aeld/ecen5013-yocto/blob/ecen5013-hello-world/meta-ecen5013/recipes-ecen5013/ecen5013-hello-world/ecen5013-hello-world_git.bb
	install -d ${D}${bindir}

	# install -m 0755 ${S}/aesdsocket ${D}${bindir}/
        # Added instead of aesdsocket
        install -m 0755 ${S}/module_load ${D}${bindir}/
        install -m 0755 ${S}/module_unload ${D}${bindir}/

	install -d ${D}${sysconfdir}/init.d

	# install -m 0755 ${S}/aesdsocket-start-stop.sh ${D}${sysconfdir}/init.d
        # Added init-misc modules script and .ko modules
        install -d ${D}${base_libdir}/modules/5.15.91-yocto-standard/

	install -m 0755 ${WORKDIR}/init-misc-modules.sh ${D}${sysconfdir}/init.d
        install -m 0755 ${S}/hello.ko ${D}/${base_libdir}/modules/5.15.91-yocto-standard/
        install -m 0755 ${S}/faulty.ko ${D}/${base_libdir}/modules/5.15.91-yocto-standard/
}
