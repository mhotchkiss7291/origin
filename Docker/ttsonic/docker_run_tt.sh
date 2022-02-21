#!/bin/sh

docker run --privileged \
    -v /Users/mhotchkiss/sonic:/developer/sonic \
    -v /Users/mhotchkiss/corpora:/developer/corpora \
    -v /Users/mhotchkiss/traintestsonic:/developer/traintestsonic \
    -e ALIGN_BASE=/developer/sonic \
    -e TRAIN_TEST_DIR=/developer/traintestsonic \
    -e CORPORA=/developer/traintestsonic/train/corpora \
    -e PATH=$ALIGN_BASE/.build/bin:$TRAIN_TEST_DIR/test/scripts:$PATH:. \
    -it linux_dev_image:0.1
