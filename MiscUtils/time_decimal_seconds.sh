now=$(gdate -u +%Y-%m-%dT%H:%M:%S.%03NZ)
echo $now

past=$(gdate --date='15 minutes ago' -u +%Y-%m-%dT%H:%M:%S.%03NZ)
echo $past
