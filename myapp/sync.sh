#!/bin/bash
if [ -d $1 ]; then
  cd $1
  git pull
else
  git clone $2 $1
fi

ln -s $1/src/myimg resources/public/myimg

echo "OK"
