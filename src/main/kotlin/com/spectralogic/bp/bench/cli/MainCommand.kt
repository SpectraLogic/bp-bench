/*
 * ****************************************************************************
 *   Copyright 2014-2019 Spectra Logic Corporation. All Rights Reserved.
 * ***************************************************************************
 */
package com.spectralogic.bp.bench.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.findObject
import com.github.ajalt.clikt.core.findOrSetObject
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option

class MainCommand
constructor(
    writeToTapeCommand: CliktCommand,
    getFromTapeCommand: CliktCommand
) :
    CliktCommand(name = "bpBench") {

    init {
        subcommands(
            writeToTapeCommand,
            getFromTapeCommand
        )
    }

    private val verbose by option("-v", "--verbose", help = "enable verbose output").flag()

    override fun run() {
        findOrSetObject { verbose }
    }
}
