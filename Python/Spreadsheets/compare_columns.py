#! /usr/bin/env python

import argparse
import csv
import logging

class CompareColumns:
    column_name = None

    def __init__(self, column_name):
        self.column_name = column_name

    def do_compare(self):

        list1 = make_list('./transcript_train66.csv', self)
        list2 = make_list('./transcript_train67.csv', self)
        
        if (list1 == list2):
            print('The columns are identical')
            exit
        else:
            print('The columns are different ..')
            score_difference(list1, list2)

def make_list(filename, self):

    with open(filename, 'r') as f:
        reader = csv.reader(f) 
        f1_data = list(reader)

    if self.column_name in f1_data[0]:
        column_number = f1_data[0].index(self.column_name)
    else: 
        print('\nNo column by that name in file...\n')
        exit

    file1_column = []
    row_count = len(f1_data)

    i = 1 # ignore header
    while i < row_count:
        file1_column.append(f1_data[i][column_number])
        i = i + 1

    return file1_column

def score_difference(list_1, list_2):

    row_count = len(list_1)
    fields = ['train66_score', 'train67_score', 'Difference'] 
    rows = []

    i = 1 
    while i < row_count:
        if list_1[i] != list_2[i]:
            val1 = int(list_1[i])
            val2 = int(list_2[i])
            diff = val2 - val1
            rows.append([val1, val2, diff])
        i = i + 1
    
    with open('Diff.csv', 'w') as f:
        write = csv.writer(f)
        write.writerow(fields)
        write.writerows(rows)

def main():
    logging.basicConfig(level=logging.INFO)
    parser = argparse.ArgumentParser(description='compare columns')
    parser.add_argument('-c', '--column', required=1)
    args = parser.parse_args()
    delta = CompareColumns(args.column)
    delta.do_compare()

main()
