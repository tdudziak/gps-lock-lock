#!/bin/sh

shadow_opts="( +clone -background black -shadow 200x5-3+3 ) +swap -background none -layers merge +repage"
input_file="./icon.svgz"

res_path() {
    echo ./res/drawable-$1/ic_launcher.png
}

convert -background none "$input_file" $shadow_opts -resize 512x512 market_icon.png

convert -background none "$input_file" $shadow_opts -resize 96x96 `res_path xhdpi`
convert -background none "$input_file" $shadow_opts -resize 72x72 `res_path hdpi`
convert -background none "$input_file" $shadow_opts -resize 48x48 `res_path mdpi`
convert -background none "$input_file" -resize 36x36 `res_path ldpi`
