package com.rod

def laberinto = """\
************************* *************************
*                                   * *           *
* * *** *** ******************* ***** * * * * * ***
"""
def bloques = []
def bloque = { lab ->	
	lab.eachLine { linea, index ->
		def yCont = 0
		linea.getChars().each { b ->
			bloques << new Bloque(
				x: index, y: yCont++, tipo: b
			)
		}
	}
	return { x, y -> 
		bloques.find { 
				it.x == x && it.y == y
		}		
	}
}(laberinto)

def setVecinos = {
	bloques.collect { b ->
		b.abajo = bloque(b?.x + 1, b?.y)
		b.derecha = bloque(b?.x, b?.y + 1)
		b.izquierda = bloque(b?.x, b?.y - 1)
		b.arriba = bloque(b?.x - 1, b?.y) 
	}
}()
