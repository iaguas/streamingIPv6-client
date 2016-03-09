#!/bin/bash
port=$1
vlc udp://@[::]:$port --sout '#display{noaudio,delay=2000}'
