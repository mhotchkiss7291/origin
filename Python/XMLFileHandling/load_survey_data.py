#! /usr/bin/env python
import argparse
import csv
import logging
import os
import shutil 
from xml.etree import ElementTree
from re import L, S
from os import listdir
from os.path import isfile, join

'''
This utility is for taking soundlog data, .xml, .wav, .txt files, in a flat
file structure and reorganizing it into a directory structure suitable
to use Rob's create_author_session.py script to create transcript.csv
and combined.wav for hand grading.

From the soundlog_tools directory run:

./scratch/load_survey_data.py -d <my_source_directory>

This will create a results.csv file copy all of the files from the source directory to:

<my_source_directory>.out_audio

Run:

./scripts/create_author_session.py \
    --xpath author \
    --xpath session_data/@score_threshold \
    --xpath session_data/phoneme_response \
    --xpath creation_date \
    <my_source_directory>.out_audio

...to get the transcript and combined audio file.
'''

class LoadGradedAudio:
    source_dir = None

    def __init__(self, source_dir):
        self.source_dir = source_dir

    def process_dir(self):
        full_list = copy_files(self)
        xml_data = get_xml_data(full_list)
        report = make_results_file(self, xml_data)

def make_results_file(self, file_list):
    output_dir = self.source_dir + ".out_audio"
    fields = [ 'soundlog_id', 'creation_date', 'language', 'application', 'mode' ] 
    rows = []

    i = 0
    while i < len(file_list):
        tree = ElementTree.parse(self.source_dir + "/" + file_list[i])
        soundlog_id = file_list[i][0:-4]
        creation_date = tree.find('creation_date').text
        language = tree.find('language').text.strip()
        application = tree.find('application').text.strip()
        mode = tree.find('mode').text.strip()
        rows.append([soundlog_id, creation_date, language, application, mode])
        i = i + 1

    with open(output_dir + '/results.csv', 'w') as f:
        write = csv.writer(f)
        write.writerow(fields)
        write.writerows(rows)

    return file_list

def copy_files(self):
    all_files = [f for f in listdir(self.source_dir) if isfile(join(self.source_dir, f))]
    file_count = len(all_files)
    output_dir = self.source_dir + ".out_audio"
    isExist = os.path.exists(output_dir)
    if not isExist:
        os.makedirs(output_dir)

    i = 0 
    while i < file_count:
        filename = all_files[i]
        subdir_name = filename[:2]
        new_dir = output_dir + "/" + subdir_name
        make_subdirs(new_dir)

        if all_files[i].startswith(subdir_name):
            shutil.copyfile(self.source_dir + "/" + filename, new_dir + "/" + filename)
        i = i + 1
    return all_files

def get_xml_data(full_list):
    files_by_id = []
    i = 0
    while i < len(full_list):
        if ".xml" in full_list[i]:
            files_by_id.append(full_list[i]) 
        i = i + 1
    return files_by_id

def make_subdirs(dir):
    isExist = os.path.exists(dir)
    if not isExist:
        os.makedirs(dir)
    
def main():
    logging.basicConfig(level=logging.INFO)
    parser = argparse.ArgumentParser(description='load wav txt files')
    parser.add_argument('-d', '--directory', required=1)
    args = parser.parse_args()
    delta = LoadGradedAudio(args.directory)
    delta.process_dir()

main()
