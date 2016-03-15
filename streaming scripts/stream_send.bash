#!/bin/bash
dsti6=$1
dstport=$2
file=$3
vlc -I dummy $file --sout '#std{access=udp,dst=['$dsti6']:'$dstport'}'
#vlc -I dummy $file --sout '#transcode{vcodec=mp2v,vb=500,scale=1,deinterlace}:std{access=udp,dst=['$dsti6']:'$dstport'}'


