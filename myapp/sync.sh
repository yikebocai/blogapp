#!/bin/bash
mypwd=`pwd`

if [ -d $1 ]; then
  cd $1
  git pull
else
  git clone $2 $1
fi

cd $mypwd
myimg="resources/public/myimg"
if [ ! -f "$myimg" ]; then
	#echo "$myimg"
  	rm -rf $myimg
fi
ln -s "$1/src/myimg" $myimg

echo "OK"
