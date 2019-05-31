/*
 * ****************************************************************************
 *   Copyright 2014-2019 Spectra Logic Corporation. All Rights Reserved.
 * ***************************************************************************
 */

package com.spectralogic.bp.bench.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import com.github.ajalt.clikt.parameters.options.validate
import com.github.ajalt.clikt.parameters.types.int
import kotlin.random.Random

abstract class BpCommand(
    help: String = "",
    epilog: String = "",
    name: String? = null,
    invokeWithoutSubcommand: Boolean = false,
    printHelpOnEmptyArgs: Boolean = false
) : CliktCommand(help, epilog, name, invokeWithoutSubcommand, printHelpOnEmptyArgs) {
    val ONE_MB = 1048576
    internal val endpoint by option(
        "-bp",
        "--blackpearl",
        envvar = "BP_DATA_ENDPOINT",
        help = "The data path on the Black Pearl.\nSet with BP_DATA_ENDPOINT"
    ).prompt("Black Pearl data path").validate { require(it.isNotEmpty()) { "Black Pearl data path cannot be empty" } }
    internal val clientId: String by option(
        "-a",
        "--accessid",
        envvar = "BP_ACCESS_ID",
        help = "The access ID of the user you want to transfer as\nSet with BP_ACCESS_ID"
    ).prompt("Black Pearl access id?").validate { require(it.isNotEmpty()) { "User name cannot be empty" } }
    internal val secretKey: String by option(
        "-s",
        "--secretkey",
        envvar = "BP_SECRET_KEY",
        help = "The secret key of the user you want to transfer as\nSet with BP_SECRET_KEY"
    ).prompt("Black Pearl secret key?").validate { require(it.isNotEmpty()) { "Password cannot be empty" } }
    internal val bucket: String by option(
        "-b",
        "--bucket",
        envvar = "BP_BUCKET",
        help = "The bucket to use for benchmarking.\nSet with BP_BUCKET"
    ).prompt("Target Black Pearl bucket").validate { require(it.isNotEmpty()) { "Bucket name cannot be empty" } }
    internal val bufferSize by option("-bb", "--buffer-size", help = "Size of buffer in bytes. Defaults to 1 MB")
        .int()
        .default(ONE_MB)
        .validate { require(it > 0) { "Buffer size must be a positive number" } }
    internal val threads by option("-t", "--threads", help = "Threads to use during transfer. Defaults to 10")
        .int()
        .default(10)
        .validate { require(it > 0) { "Thread number must be a positive number" } }
    internal val randomSource: Random by option("-r", "--random-seed", help = "Random seed for RNG")
        .convert {
            try {
                Random(it.toLong())
            } catch (t: Throwable) {
                fail("seed could not be converted to a number")
            }
        }
        .default(Random)
}