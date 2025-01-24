#include <jni.h>
#include <string>
#include <assert.h>
#include "secp256k1.h"
#include "secp256k1_ecdh.h"

extern "C" JNIEXPORT jstring JNICALL
Java_id_yuana_secp256k1_demo_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";

    //todo

    unsigned char seckey1[32];
    unsigned char seckey2[32];
    unsigned char compressed_pubkey1[33];
    unsigned char compressed_pubkey2[33];
    unsigned char shared_secret1[32];
    unsigned char shared_secret2[32];
    unsigned char randomize[32];
    int return_val;
    size_t len;
    secp256k1_pubkey pubkey1;
    secp256k1_pubkey pubkey2;

    /* Before we can call actual API functions, we need to create a "context". */
    secp256k1_context* ctx = secp256k1_context_create(SECP256K1_CONTEXT_NONE);


    //TODO TESTING


    return env->NewStringUTF(hello.c_str());
}