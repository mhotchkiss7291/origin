curl \
	'https://prism-qa8.dev.rosettastone.com/reports/singleSolution/preTestResults/' \
	-H 'authority: prism-qa8.dev.rosettastone.com' \
	-H 'pragma: no-cache' \
	-H 'authorization: Bearer cca755fd-1439-468c-8fc2-5806fde0732f' \
	-H 'content-type: application/json' \
	-H 'accept: application/json' \
	-H 'cache-control: no-store, no-cache, must-revalidate, post-check=0, pre-check=0' \
	-H 'x-requested-with: Ceres' \
	-H 'user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36' \
	-H 'origin: https://admin-qa8.dev.rosettastone.com' \
	-H 'sec-fetch-site: same-site' \
	-H 'sec-fetch-mode: cors' \
	-H 'referer: https://admin-qa8.dev.rosettastone.com/' \
	-H 'accept-encoding: gzip, deflate, br' -H 'accept-language: en-US,en;q=0.9' \
	--data-binary ' 
	{
	  "organization": "991a94e6-1dd2-419e-9617-66a20dc81c9f",
	  "start": "2016-10-26",
	  "end": "2020-02-03",
	  "product": "product.e6e353e1-7e31-4f36-a995-8bd4facddaf5",
	  "async": false
	}' --compressed
