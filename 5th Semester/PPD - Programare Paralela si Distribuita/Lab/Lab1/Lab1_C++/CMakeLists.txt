cmake_minimum_required(VERSION 3.29)
project(Lab1_C__)

include_directories("D:/Ariana/Facultate/SEM5/PPD/LAB/Lab1/Lab1_C++/Header")

set(CMAKE_CXX_STANDARD 20)

add_executable(main.cpp
        main.cpp
        Source/Dinamic/WriterAndReader.cpp
        Source/Dinamic/Convolutie.cpp
        Source/Dinamic/ThreadOrizontala.cpp
        Source/Dinamic/ThreadVerticala.cpp
        Source/Dinamic/ThreadBloc.cpp
        Source/STATIC/Secvential.cpp
        Source/STATIC/OrizontalaStatic.cpp
        Source/STATIC/VerticalStatic.cpp
        Source/STATIC/BlocStatic.cpp
)