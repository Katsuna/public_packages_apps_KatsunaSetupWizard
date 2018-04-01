LOCAL_PATH := $(call my-dir)
KATSUNA_COMMON_PATH := $(ANDROID_BUILD_TOP)/frameworks/KatsunaCommon
include $(CLEAR_VARS)

LOCAL_FULL_LIBS_MANIFEST_FILES := $(KATSUNA_COMMON_PATH)/commons/src/main/AndroidManifest.xml

LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_MODULE_TAGS := optional

LOCAL_PACKAGE_NAME := KatsunaSetupWizard
LOCAL_CERTIFICATE := platform
LOCAL_PRIVILEGED_MODULE := true

LOCAL_PROGUARD_FLAG_FILES := proguard.flags

LOCAL_STATIC_JAVA_LIBRARIES := \
    android-support-v4 \
    android-support-v7-cardview \
    android-support-v7-recyclerview \
    android-support-v13 \
    android-support-design

LOCAL_JAVA_LIBRARIES := telephony-common

LOCAL_RESOURCE_DIR := $(addprefix $(LOCAL_PATH)/, res) \
    frameworks/support/v7/appcompat/res \
    frameworks/support/v7/cardview/res \
    frameworks/support/v7/recyclerview/res \
    frameworks/support/design/res

LOCAL_STATIC_JAVA_AAR_LIBRARIES := roundedimageview

# Include KatsunaCommon into this app
LOCAL_REQUIRED_MODULES := KatsunaCommon
LOCAL_STATIC_JAVA_LIBRARIES += KatsunaCommon
# Include KatsunaCommon resources
LOCAL_RESOURCE_DIR += frameworks/KatsunaCommon/commons/src/main/res

LOCAL_AAPT_FLAGS := --auto-add-overlay \
    --extra-packages android.support.v7.appcompat \
    --extra-packages android.support.v17.preference \
    --extra-packages com.katsuna.commons \
    --extra-packages com.makeramen.roundedimageview

include frameworks/opt/setupwizard/library/common.mk

include $(BUILD_PACKAGE)
