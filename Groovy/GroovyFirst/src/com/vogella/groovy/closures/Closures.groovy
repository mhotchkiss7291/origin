package com.vogella.groovy.closures

def multiply = {int a, int  b = 10 -> a * b}

assert multiply(2) == 20
assert multiply(2,5) == 10

List<Integer> list = [5, 6, 7, 8]
list.each({line -> println line})
list.each({println it})

// calculate the sum of the number up to 10
def total = 0
(1..10).each {total+=it}
