/*
 * ****************************************************************************
 *   Copyright 2014-2019 Spectra Logic Corporation. All Rights Reserved.
 * ***************************************************************************
 */

package com.spectralogic.bp.bench.cli

import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import com.github.ajalt.clikt.parameters.options.validate
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.double
import com.github.ajalt.clikt.parameters.types.int
import com.spectralogic.ds3client.Ds3ClientBuilder
import com.spectralogic.ds3client.commands.HeadBucketRequest
import com.spectralogic.ds3client.commands.HeadBucketResponse
import com.spectralogic.ds3client.commands.PutObjectRequest
import com.spectralogic.ds3client.commands.spectrads3.PutBucketSpectraS3Request
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers
import com.spectralogic.ds3client.helpers.options.WriteJobOptions
import com.spectralogic.ds3client.models.Priority
import com.spectralogic.ds3client.models.bulk.Ds3Object
import com.spectralogic.ds3client.models.common.Credentials
import com.spectralogic.ds3client.networking.FailedRequestException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Instant
import kotlin.math.pow

class WriteToTapeCommand :
    BpCommand(name = "put", help = "Attempt to put <NUMBER> objects of <SIZE> to <BUCKET> without disk IO") {

    private val unitList = SizeUnits.names().joinToString(",") { it.first }

    private val itemNumber: Int by option(
        "-n",
        "--number",
        help = "Number of files to write"
    ).int().prompt("Number of files").validate { require(it > 0) { "Number of files must be positive" } }
    private val size by option(
        "-S",
        "--size",
        help = "Size of each file to write"
    ).double().prompt("Size of file(s)").validate { require(it >= 0L) { "Size of files must be non-negative" } }
    private val sizeUnit: SizeUnits by option(
        "-u",
        "--units",
        help = "Units of files to write <$unitList>"
    ).choice(*SizeUnits.names())
        .prompt("Units for size: ($unitList)")
    private val dataPolicy by option(
        "-d",
        "--datapolicy",
        envvar = "BP_DATA_POLICY",
        help = "name of the data policy to create the bucket with"
    )
        .prompt()
        .validate { require(it.isNotEmpty()) { "Data policy must not be blank" } }
    private val nakedPut by option("--naked-put", envvar = "NAKED_PUT").flag()
    private val priority: Priority by option(
        "-pri",
        "--priority",
        envvar = "BP_PRIORITY",
        help = "Priority for transfers"
    )
        .choice(*getPriorities())
        .default(Priority.NORMAL)

    override fun run() {
        val fileSize: Long = (size * 10.0.pow(sizeUnit.power)).toLong()
        val buffer = MemoryBuffer(bufferSize, fileSize, randomSource)
        val client = Ds3ClientHelpers.wrap(
            Ds3ClientBuilder.create(endpoint, Credentials(clientId, secretKey))
                .withHttps(false)
                .withBufferSize(bufferSize)
                .build()
        )
        client.ensureBucketExistsByName(bucket, dataPolicy)
        if (nakedPut) {
            CoroutineScope(Dispatchers.IO).launch {
                ds3ObjectSequence().asFlow()
                    .flatMapMerge(threads) {
                        flow {
                            try {
                                val p = PutObjectRequest(bucket, it.name, RandomByteChannel(it.size), it.size)
                                println("Sending putObject")
                                val response = client.client.putObject(p)
                                println("Sent the putObject")
                                emit(response)
                            } catch (t: Throwable) {
                                println(t.message ?: "Unknown error during putObject")
                            } finally {
                                println("finally putObject")
                            }
                        }
                    }
                    .collect()
            }.let { runBlocking { it.join() } }
        } else {
            val job = client.startWriteJob(
                bucket, ds3ObjectSequence().toList(),
                WriteJobOptions.create()
                    .withPriority(priority)
            )
            job
                .withMaxParallelRequests(threads)
                .transfer(buffer)
        }
    }

    private var itemName: Long = 0L

    private fun ds3ObjectSequence(): Sequence<Ds3Object> {
        val fileSize: Long = (size * 10.0.pow(sizeUnit.power)).toLong()
        return generateSequence { Ds3Object("bp-benchmark-${itemName++}-${Instant.now().toEpochMilli()}.txt", fileSize) }.take(itemNumber)
    }
}

fun Ds3ClientHelpers.ensureBucketExistsByName(bucket: String, dataPolicy: String) {
    val response = this.client.headBucket(HeadBucketRequest(bucket))
    if (response.status == HeadBucketResponse.Status.DOESNTEXIST) {
        try {
            this.client.putBucketSpectraS3(PutBucketSpectraS3Request(bucket).withDataPolicyId(dataPolicy))
        } catch (var5: FailedRequestException) {
            if (var5.statusCode != 409) {
                error(var5.message ?: "Encountered an unknown error creating bucket")
            }

            error("Creating $bucket failed because it was created by another thread or process")
        }
    }
}

fun getPriorities(): Array<Pair<String, Priority>> {
    return Priority.values()
        .map { Pair(it.name, it) }
        .toTypedArray()
}
