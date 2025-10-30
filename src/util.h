#pragma once

#include "D3velop.h"
#include <iostream>

namespace UTIL {
#define LOG_COL_MESSAGE IM_COL32(225, 225, 225, 255);
#define LOG_COL_SUCCESS IM_COL32(0, 255, 0, 255);
#define LOG_COL_ERROR IM_COL32(255, 0, 0, 255);
#define LOG_COL_INFO IM_COL32(0, 0, 255, 255);

enum LOG_TYPE { MESSAGE, SUCCESS, ERROR, INFO };

inline void print(const char *print) { std::cout << print << std::endl; }
inline void print(std::string print) { std::cout << print << std::endl; }

inline void log(LOG_TYPE log_type, const char *message) {
  D3velop &develop = D3velop::getInstance();
  switch (log_type) {
  case MESSAGE: {
    std::string out = std::string("[D3] [MESSAGE]: ").append(message);
    print(out);
    develop.log.log.push_back(out);
    break;
  }
  case SUCCESS: {
    std::string out = std::string("[D3] [SUCCESS]: ").append(message);
    print(out);
    develop.log.log.push_back(out);
    break;
  }
  case ERROR: {
    std::string out = std::string("[D3] [ERROR]: ").append(message);
    print(out);
    develop.log.log.push_back(out);
    break;
  }
  case INFO: {
    std::string out = std::string("[D3] [INFO]: ").append(message);
    print(out);
    develop.log.log.push_back(out);
    break;
  }
  default: {
    std::string out = std::string("[D3] [UNKNOWN]: ").append(message);
    print(out);
    develop.log.log.push_back(out);
    break;
  }
  };
  develop.log.log_scroll = true;
}

} // namespace UTIL
