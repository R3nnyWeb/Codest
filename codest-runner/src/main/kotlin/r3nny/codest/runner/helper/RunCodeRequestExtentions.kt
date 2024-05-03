package r3nny.codest.runner.helper

import r3nny.codest.shared.dto.runner.RunCodeRequestEvent

fun RunCodeRequestEvent.inputs(): List<String> = tests.map { it.inputData }.reduce { acc, inputs -> acc + inputs }