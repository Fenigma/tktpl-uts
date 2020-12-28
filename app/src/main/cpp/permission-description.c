#include <string.h>
#include <jni.h>

typedef struct{
    char description[1024];
} PermissionDisplayString;

JNIEXPORT jobject JNICALL
Java_id_ac_ui_cs_mobileprogramming_gregoriusaprisunnea_gtroy_PermissionDescriptionActivity_getPermissionDescription(JNIEnv *env, jobject obj, jstring str_description) {
    jobject displayStringObject = NULL;
    jclass displayStringClass = (*env)->FindClass(env, "id/ac/ui/cs/mobileprogramming/gregoriusaprisunnea/gtroy/data/models/PermissionDisplayString");

    jfieldID descriptionField  = (*env)->GetFieldID(env, displayStringClass, "description", "Ljava/lang/String;");

    displayStringObject = (*env)->AllocObject(env, displayStringClass);

    const char *descriptionFromCPP = (*env)->GetStringUTFChars(env, str_description, NULL);
    char *generatedFromJNIDescription = malloc(strlen(descriptionFromCPP) + strlen(" [[ generated by JNI ]]") + 1);
    strcpy(generatedFromJNIDescription, descriptionFromCPP);
    strcat(generatedFromJNIDescription, " [[ generated by JNI ]]");
    jstring finalDescription = (*env)->NewStringUTF(env, generatedFromJNIDescription);
    free(generatedFromJNIDescription);

    (*env)->SetObjectField(env, displayStringObject, descriptionField, finalDescription);

    return displayStringObject;
}