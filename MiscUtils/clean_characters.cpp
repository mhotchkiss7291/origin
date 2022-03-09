//
//  main.cpp
//  clean
//
//  Created by Barrett Davis on 1/13/21.
//

#include <fstream>
#include <iostream>

static int describe(void) {
  std::cout << "usage: clean file\n";
  return -1;
}

static void processLine(const std::string &line) {
  if (line.empty()) {
    return;
  }
  const char *ptr = line.c_str();
  while (*ptr != 0) {
    const int ch = *ptr++ & 0x00ff;
    if (ch != 0xE2 && ch != 0x86) {
      if (ch == 0xB5) {
        std::cout << std::endl;
      } else {
        std::cout << (char)ch;
      }
    }
  }
  std::cout << std::endl;
  return;
}

static bool process(const char *fileName) {
  if (fileName == nullptr || fileName[0] == 0) {
    return false;
  }
  std::ifstream stream(fileName);
  if (!stream) {
    return false;
  }
  std::string line;
  while (getline(stream, line)) {
    processLine(line);
    line.clear();
  }
  stream.close();
  return true;
}

int main(int argc, const char *argv[]) {
  if (argc < 2) {
    return describe();
  }
  for (int ii = 1; ii < argc; ii++) {
    process(argv[ii]);
  }
  return 0;
}
