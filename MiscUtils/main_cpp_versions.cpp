//
//  main.cpp
//  VersionCheck
//
//  Created by Barrett Davis on 1/27/21.
//  https://en.cppreference.com/w/User:D41D8CD98F/feature_testing_macros
#include <algorithm>
#include <atomic>
#include <iostream>
#include <mutex>
#include <vector>


static void checkCpp1998( void ) {
    std::cout << "C++ 98:\n";
#ifdef __cpp_rtti
    std::cout << "    Run time type checking\n";
#endif // __cpp_rtti
    
#ifdef __cpp_exceptions
    std::cout << "    Exception handling\n";
#endif // __cpp_exceptions
    std::cout << "\n";
    return;
}

static void checkCpp2011( void ) {
    std::cout << "C++ 11:\n";
#ifdef __cpp_unicode_literals
    std::cout << "    Unicode string literals\n";
#endif // __cpp_unicode_literals
    
#ifdef __cpp_lambdas
    std::cout << "    Lamda experessions\n";
#endif // __cpp_lambdas
    
#ifdef __cpp_constexpr
    std::cout << "    constexpr\n";
#endif // __cpp_constexpr
    
#ifdef __cpp_decltype
    std::cout << "    decltype\n";
#endif // __cpp_decltype
    
#ifdef __cpp_initializer_lists
    std::cout << "    initializer lists\n";
#endif // __cpp_initializer_lists
    std::cout << "\n";
    return;
}

static void checkCpp2014( void ) {
    std::cout << "C++ 14:\n";
#ifdef __cpp_generic_lambdas
    std::cout << "    Generic lamda experessions\n";
#endif // __cpp_generic_lambdas
    
#ifdef __cpp_lib_make_unique
    std::cout << "    make_unique\n";
#endif // __cpp_lib_make_unique
    
#ifdef __cpp_lib_chrono_udls
    std::cout << "    chrono user defined literals\n";
#endif // __cpp_lib_chrono_udls
    
#ifdef __cpp_lib_shared_timed_mutex
    std::cout << "    shared_timed_mutex\n";
#endif // __cpp_lib_shared_timed_mutex
    std::cout << "\n";
    return;
}

static void checkCpp2017( void ) {
    std::cout << "C++ 17:\n";
#ifdef __cpp_noexcept_function_type
    std::cout << "    noexcept function type\n";
#endif // __cpp_noexcept_function_type
    
#ifdef __cpp_if_constexpr
    std::cout << "    constexpr if()\n";
#endif // __cpp_if_constexpr
    
#ifdef __cpp_lib_clamp
    std::cout << "    std::clamp\n";
#endif // __cpp_lib_clamp
    
#ifdef __cpp_lib_scoped_lock
    std::cout << "    std::lock_guard\n";
#endif // __cpp_lib_scoped_lock
    
    std::cout << "\n";
    return;
}

static void checkCpp2020( void ) {
    std::cout << "C++ 20:\n";
#ifdef __cpp_concepts
    std::cout << "    Concepts\n";
#endif // __cpp_concepts

#ifdef __cpp_char8_t
    std::cout << "    char8_t\n";
#endif // __cpp_char8_t
    
#ifdef __cpp_modules
    std::cout << "    Modules\n";
#endif // __cpp_modules
    
#ifdef __cpp_lib_constexpr_vector
    std::cout << "    constexpr std::vector\n";
#endif // __cpp_lib_constexpr_vector

#ifdef __cpp_lib_atomic_float
    std::cout << "    floating point atomic\n";
#endif // __cpp_lib_atomic_float
    std::cout << "\n";
    return;
}

static void checkCpp2023( void ) {
    std::cout << "C++ 23:\n";
#ifdef __cpp_size_t_suffix
    std::cout << "    Literal suffix for (signed) size_t\n";
#endif // __cpp_size_t_suffix
    std::cout << "\n";
    return;
}



int main( void ) {
    
    std::cout << "Checking for some C++ language features:\n";
    std::cout << "\n";

    checkCpp1998();
    checkCpp2011();
    checkCpp2014();
    checkCpp2017();
    checkCpp2020();
    checkCpp2023();

    std::cout << "done\n";
    return 0;
}
