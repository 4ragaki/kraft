/*
 * Copyright (c) 2020 by  Aragaki<4ragaki@gmail.com>, All rights reserved.
 */

#include <jni.h>
#include <string>
#include <sys/file.h>
#include <unistd.h>
#include <android/log.h>

void withCheck(int status, const char *msg) {
    if (status == -1) perror(msg);
}