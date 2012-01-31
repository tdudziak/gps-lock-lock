#!/bin/bash

SCRIPT_PATH=$(dirname "$(readlink -f $0)")
PROJECT_ROOT=$(readlink -f "$SCRIPT_PATH/../")
cd "$PROJECT_ROOT"

# resize a given file for multiple screen densities
function convert_file {
    in_path=$1       # input file name (.svgz)
    sizes_name=$2    # name of associative array dpi->size
    in_name=$(basename "$in_path")

    # passing associative arrays to functions is not directly possible
    sizes_decl=$(declare -p $sizes_name)
    eval "declare -A sizes=${sizes_decl#*=}"

    for dpi in ${!sizes[@]}; do
        out_path="res/drawable-$dpi/${in_name%.*}.png"
        echo "$in_path -> $out_path [${sizes[$dpi]}]"
        convert -background none "$in_path" -resize "${sizes[$dpi]}" "$out_path"
    done
}

# create large icon with drop-down shadow for Android Market
SHADOW_OPTS="( +clone -background black -shadow 200x5-3+3 ) +swap -background none -layers merge +repage"
convert -background none "svg/ic_launcher.svgz" $SHADOW_OPTS -resize 512x512 market_icon.png

# create launcher icons
declare -A LAUNCHER_SZ=([ldpi]='36x36' [mdpi]='48x48' [hdpi]='72x72' [xhdpi]='96x96')
convert_file "svg/ic_launcher.svgz" LAUNCHER_SZ

# create icons for ListView-based menu
declare -A MENU_ICON_SZ=([ldpi]='24x24' [mdpi]='32x32' [hdpi]='48x48')
FILES="svg/ic_exit.svgz svg/ic_restart.svgz svg/ic_help.svgz svg/ic_settings.svgz"
for fn in $FILES; do
    convert_file "$fn" MENU_ICON_SZ
done
