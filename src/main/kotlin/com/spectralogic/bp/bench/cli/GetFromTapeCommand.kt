/*
 * ****************************************************************************
 *   Copyright 2014-2019 Spectra Logic Corporation. All Rights Reserved.
 * ***************************************************************************
 */

package com.spectralogic.bp.bench.cli

import com.spectralogic.ds3client.Ds3ClientBuilder
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers
import com.spectralogic.ds3client.models.common.Credentials

class GetFromTapeCommand :
    BpCommand(name = "get", help = "Attempt to download all objects in the target <BUCKET> without disk IO") {

    override fun run() {
        val client = Ds3ClientHelpers.wrap(
            Ds3ClientBuilder.create(endpoint, Credentials(clientId, secretKey))
                .withHttps(false)
                .withBufferSize(bufferSize)
                .build()
        )
        client.startReadAllJob(bucket)
            .withMaxParallelRequests(threads)
            .transfer(MemoryBuffer(bufferSize, 0L, randomSource))
    }
}
