#!/bin/bash
PID_FILE="ap-p2p-sm-payment-execution.pid"
if [ -f "$PID_FILE" ]; then
	PID=$(cat $PID_FILE)
	kill $PID
else
    echo "PID file not found: $PID_FILE"
fi
exit 0