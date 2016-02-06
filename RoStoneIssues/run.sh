1.curl | python -mjson.tool > 1.out
2.curl | python -mjson.tool > 2.out
diff 1.out 2.out
rm *.out
3.curl | python -mjson.tool > 3.out
4.curl | python -mjson.tool > 4.out
diff 3.out 4.out
rm *.out
