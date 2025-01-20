solution "avl"
  configurations {"debug", "release"}

  configuration {"debug"}
    defines {"DEBUG"}
    if _PREMAKE_VERSION >="5.0" then
      symbols "On"
		else
			flags { "Symbols" }
		end


  configuration {"release"}
		if _PREMAKE_VERSION >="5.0" then
			optimize "speed"
		else
			flags { "OptimizeSpeed" }
		end

  configuration {"linux"}
		buildoptions { "-mtune=native -march=native" }
		buildoptions { "-std=c++11" }
		buildoptions { "-W -Wall -Wextra", "-pipe" }

  configuration {"macosx"}
		buildoptions { "-std=c++11" }

  configuration {"linux", "debug"}
    buildoptions {"-g"}
    linkoptions {"-g"}

  configuration {"windows"}
    defines {"AFFICHAGE_SIMPLE"}

project "avl"
  language "c++"
  kind "ConsoleApp"
  targetdir "bin"
  files {"noeud.hpp", "noeud.cpp", "arbre.hpp", "arbre.cpp", "main.cpp"}
