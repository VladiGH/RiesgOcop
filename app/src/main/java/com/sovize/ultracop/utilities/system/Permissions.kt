package com.sovize.ultracop.utilities.system

object Permissions {

    /**
     * Permission:
     * --r to READ
     * --c to COMMENT
     * --w to WRITE, UPDATE reports its own reports
     * --h to HIDE, PUBLISH its own reports from public
     * --u to UPDATE any report
     * --d to DELETE any report
     * --g to become GOD AKA admin
     */

    /**
     * @type anonimus [r]
     */
    val anonimus = mutableListOf("r")
    /**
     * @type user [r,c]
     */
    val user = mutableListOf("r", "c")
    /**
     * @type userUca [r,c,w,h]
     */
    val userUca = mutableListOf("r", "c", "w", "h")
    /**
     * @type admin [r,c,w,h,u,d,g]
     */
    val admin = mutableListOf("r", "c", "w", "h", "u", "d", "g")
}