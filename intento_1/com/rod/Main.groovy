package com.rod

def laberinto = {
  new File('com/rod/laberinto.txt').text
}()

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

def pintaLaberinto = {
  def lCount = 0
  bloques.each { b ->
    if(b.x != lCount) {
      lCount++
      print '\n'
    }
    print b.tipo    
  }  
  print '\n'
}

//Tipo de bloques
Character bLibre = ' '
Character bOcupado = '*'
Character bVisitado = '1'
Character bRepetido = '2'

def entrada = bloques.find { 
    it.x == 0 && it.tipo == bLibre 
}

def recorrer
recorrer = { b ->
  if(b != null) {        
    if(b.tipo == bLibre) { // Marca visitados
      b.tipo = bVisitado
    }
    if(b.abajo?.tipo == bLibre) { // Abren camino
      recorrer(b?.abajo)
    } else if(b.derecha?.tipo == bLibre) {
      recorrer(b?.derecha)
    } else if(b.izquierda?.tipo == bLibre) {
      recorrer(b?.izquierda)
    } else if(b.arriba?.tipo == bLibre) {
      recorrer(b?.arriba)
    } else if(b.derecha?.tipo == bVisitado) { // Repiten camino
      b.tipo = bRepetido
      recorrer(b?.derecha)
    } else if(b.arriba?.tipo == bVisitado) {
      b.tipo = bRepetido
      recorrer(b?.arriba)
    } else if(b.abajo?.tipo == bVisitado) {
      b.tipo = bRepetido
      recorrer(b?.abajo)
    } else if(b.izquierda?.tipo == bVisitado) {
      b.tipo = bRepetido
      recorrer(b?.izquierda)
    }
  }
  return null
}

recorrer(entrada)



pintaLaberinto()