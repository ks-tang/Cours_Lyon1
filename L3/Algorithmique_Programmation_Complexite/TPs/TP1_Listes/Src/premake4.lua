solution "listes"
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

project "test_liste"
  language "c++"
  kind "ConsoleApp"
  targetdir "bin"
  files {"cellule.hpp", "cellule.cpp", "liste.hpp", "liste.cpp", "test_liste.cpp"}

project "serpent"
  language "c++"
  kind "ConsoleApp"
  targetdir "bin"
  files {"cellule.hpp", "cellule.cpp", "liste.hpp", "liste.cpp", "coordonnees.hpp", "coordonnees.cpp", "niveau.hpp", "niveau.cpp", "serpent.hpp", "serpent.cpp", "jeu_serpent.cpp"}
