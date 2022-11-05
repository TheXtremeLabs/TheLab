package com.riders.thelab.core.utils

import android.annotation.SuppressLint
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes

object LogManager {

    @SuppressLint("NewApi")
    fun logSongObject(directoryPath: String, fileName: String) {
        val file: Path = FileSystems.getDefault()
            .getPath(directoryPath, fileName)

        val attr: BasicFileAttributes = Files.readAttributes(
            file,
            BasicFileAttributes::class.java
        )

        println("creationTime: " + attr.creationTime())
        println("lastAccessTime: " + attr.lastAccessTime())
        println("lastModifiedTime: " + attr.lastModifiedTime())

        println("isDirectory: " + attr.isDirectory)
        println("isOther: " + attr.isOther)
        println("isRegularFile: " + attr.isRegularFile)
        println("isSymbolicLink: " + attr.isSymbolicLink)
        println("size: " + attr.size())
    }
}