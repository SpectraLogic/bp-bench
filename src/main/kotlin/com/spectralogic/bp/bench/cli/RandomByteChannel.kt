/*
 * ****************************************************************************
 *   Copyright 2014-2021 Spectra Logic Corporation. All Rights Reserved.
 * ***************************************************************************
 */
package com.spectralogic.bp.bench.cli

import java.nio.ByteBuffer
import java.nio.channels.SeekableByteChannel
import kotlin.random.Random

class RandomByteChannel(private val fileSize: Long) : SeekableByteChannel {
    private var position: Long = 0L
    private var closed = false

    override fun close() {
        closed = true
    }

    override fun isOpen(): Boolean = !closed

    override fun read(dst: ByteBuffer): Int {
        val bytesLeft = fileSize - position
        val remaning = dst.remaining()
        return when {
            bytesLeft == 0L -> {
                -1
            }
            remaning >= bytesLeft -> {
                position += bytesLeft
                dst.put(Random.nextBytes(bytesLeft.toInt()))
                bytesLeft.toInt()
            }
            else -> {
                position += remaning
                dst.put(Random.nextBytes(remaning))
                remaning
            }
        }
    }

    override fun write(src: ByteBuffer?): Int {
        throw NotImplementedError("A RandomByte Channel cannot be used to write")
    }

    override fun position(): Long = position

    override fun position(newPosition: Long): SeekableByteChannel {
        position = newPosition
        return this
    }

    override fun size(): Long = fileSize

    override fun truncate(size: Long): SeekableByteChannel {
        throw NotImplementedError("Truncate not there")
    }
}
