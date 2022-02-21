#!/bin/sh

docker run --privileged \
    -v /Users/mhotchkiss/sonic:/developer/sonic \
    -v /Users/mhotchkiss/corpora:/developer/corpora \
    -e ALIGN_BASE=~/sonic \
    -e TRAIN_TEST_DIR=~/traintestsonic \
    -e CORPORA=~/traintestsonic/train/corpora \
    -e PATH=$ALIGN_BASE/.build/bin:$TRAIN_TEST_DIR/test/scripts:$PATH:. \
    -it linux_dev_image:0.1
