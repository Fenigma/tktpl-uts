cmake_minimum_required(VERSION 3.4.1)

add_library(permission-description SHARED
            permission-description.c)

target_link_libraries(permission-description
                      android
                      log)