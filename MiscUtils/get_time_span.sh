#now=$(date -u +%Y-%m-%dT%H:%M:%S-0000)

now=$(date -u +%Y-%m-%dT%H:%M:%SZ)

echo $now
#past=$(date -uv -15M +%Y-%m-%dT%H:%M:%S)

past=$(date -uv -15M +%Y-%m-%dT%H:%M:%3NZ)

echo $past
