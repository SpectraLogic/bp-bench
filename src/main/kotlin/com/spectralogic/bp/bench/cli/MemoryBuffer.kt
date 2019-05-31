/*
 * ****************************************************************************
 *   Copyright 2014-2019 Spectra Logic Corporation. All Rights Reserved.
 * ***************************************************************************
 */
package com.spectralogic.bp.bench.cli

import com.spectralogic.ds3client.helpers.Ds3ClientHelpers
import java.nio.ByteBuffer
import java.nio.channels.SeekableByteChannel
import kotlin.random.Random

class MemoryBuffer(private val bufferSize: Int, private val sizeOfFiles: Long, private val random: Random) :
    Ds3ClientHelpers.ObjectChannelBuilder {

    override fun buildChannel(key: String): SeekableByteChannel = DevNullByteChannel(bufferSize, sizeOfFiles)

    private inner class DevNullByteChannel(private val bufferSize: Int, private val limit: Long) : SeekableByteChannel {
        private val backingArray: ByteArray = random.nextBytes(bufferSize)
        private var position: Int = 0
        private var isOpen: Boolean = true

        override fun isOpen(): Boolean = isOpen
        override fun position(): Long = position()
        override fun size(): Long = limit
        override fun truncate(size: Long): SeekableByteChannel = this

        override fun close() {
            isOpen = false
        }

        override fun read(dst: ByteBuffer): Int {
            val amountToRead = Math.min(dst.remaining(), bufferSize)
            dst.put(backingArray, 0, amountToRead)
            return amountToRead
        }

        override fun write(src: ByteBuffer): Int {
            val amountToWrite = Math.min(src.remaining(), bufferSize)
            src.get(backingArray, 0, amountToWrite)
            return amountToWrite
        }

        override fun position(newPosition: Long): SeekableByteChannel {
            position = newPosition.toInt()
            return this
        }
    }
}
