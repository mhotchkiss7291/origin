curl -X POST https://mercury-qa1.dev.rosettastone.com/aggregation/authentication -d "username=mhotchkiss-qa1@rosettastone.com" -d "password=password" -d "version=2.8" | python -mjson.tool > john1.out

curl -X POST https://mercury-qa1.dev.rosettastone.com/aggregation/authentication -d "username=mhotchkiss-qa1@rosettastone.com" -d "password=password" -d "version=2.9" | python -mjson.tool > john2.out

diff john1.out john2.out
