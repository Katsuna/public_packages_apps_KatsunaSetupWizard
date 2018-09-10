LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_USE_AAPT2 := true

LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_MODULE_TAGS := optional

LOCAL_PACKAGE_NAME := KatsunaSetupWizard
LOCAL_CERTIFICATE := platform
LOCAL_PRIVILEGED_MODULE := true

LOCAL_PROGUARD_FLAG_FILES := proguard.flags

LOCAL_STATIC_ANDROID_LIBRARIES := \
    KatsunaCommon \
    android-support-v4 \
    android-support-v7-cardview \
    android-support-v7-recyclerview \
    android-support-v13 \
    android-support-design

LOCAL_JAVA_LIBRARIES := \
    telephony-common

LOCAL_RESOURCE_DIR := \
    $(addprefix $(LOCAL_PATH)/, res)

LOCAL_STATIC_JAVA_AAR_LIBRARIES := \
    roundedimageview

# Include KatsunaCommon into this app
LOCAL_REQUIRED_MODULES := KatsunaCommon

include frameworks/opt/setupwizard/library/common.mk

include $(BUILD_PACKAGE)
