curl \
"https://orcabe-qa1.dev.rosettastone.com/orca/courses" \
-H "Authorization: Bearer 9533fc57-558e-46c2-960b-71a8d61a464c" \
 | python -mjson.tool
