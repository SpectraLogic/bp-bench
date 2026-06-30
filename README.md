# bp-bench

`bp-bench` is a command-line benchmarking tool designed for Spectra Logic BlackPearl. It allows users to measure the performance of data transfers to and from BlackPearl without the overhead of disk I/O, by using memory buffers and generating/discarding data on the fly.

## Features

- **Put Benchmark**: Upload a specified number of objects of a given size to a BlackPearl bucket.
- **Get Benchmark**: Download all objects from a target BlackPearl bucket.
- **No Disk I/O**: Uses memory buffers to ensure performance measurements focus on network and BlackPearl processing.
- **Configurable**: Supports configuration of endpoint, credentials, bucket, thread count, buffer size, and more via command-line options or environment variables.

## Prerequisites

- **Java 26**: This tool is compiled for Java 26. Ensure you have Java 26 or later installed, or use the provided Gradle wrapper which will handle the toolchain for you.

## Usage

The tool provides two main subcommands: `put` and `get`.

### Put

```bash
bpBench put -n <NUMBER_OF_FILES> -S <SIZE> -u <UNITS> -b <BUCKET> -d <DATA_POLICY> [OPTIONS]
```

### Get

```bash
bpBench get -b <BUCKET> [OPTIONS]
```

## Common Options

- `-bp`, `--blackpearl`: BlackPearl data path (can be set via environment variable `BP_DATA_ENDPOINT`).
- `-a`, `--accessid`: BlackPearl access ID (can be set via environment variable `BP_ACCESS_ID`).
- `-s`, `--secretkey`: BlackPearl secret key (can be set via environment variable `BP_SECRET_KEY`).
- `-b`, `--bucket`: Target BlackPearl bucket (can be set via environment variable `BP_BUCKET`).
- `-t`, `--threads`: Number of threads to use for transfers (default: 10).