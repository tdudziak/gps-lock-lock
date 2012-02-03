#!/bin/bash

SCRIPT_PATH=$(dirname "$(readlink -f $0)")
PROJECT_ROOT=$(readlink -f "$SCRIPT_PATH/../")

MARKDOWN_OUTPUT=$(markdown "$PROJECT_ROOT/README.markdown")

mkdir "$PROJECT_ROOT/assets" 2> /dev/null
cat > "$PROJECT_ROOT/assets/help.html" <<EOF
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
      "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>GPS Lock-Lock Help</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<style type="text/css">
    h1 { font-size: 110%; }
    p { margin-left: 0.5em; margin-right: 0.5em; }
</style>
<body>
$MARKDOWN_OUTPUT
</body>
</html>
EOF
