package com.rod

/**
* 1er Intento para resolver: https://www.codeeval.com/browse/157/
* Use el scrit ../../play para ejecutarlo
*/

def inicio = System.currentTimeMillis();
def termino 
//Tipo de bloques
Character bLibre = ' '
Character bOcupado = '*'
Character bActual = 'A'
Character bVisitado = '1'
Character bRepetido = '2'
Character bCamino = '+'
def laberinto = new File('./laberintos/laberinto.txt').text
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

def setVecinos = { blqs ->
  blqs.collect { b ->
    b.abajo = bloque(b?.x + 1, b?.y)
    b.derecha = bloque(b?.x, b?.y + 1)
    b.izquierda = bloque(b?.x, b?.y - 1)
    b.arriba = bloque(b?.x - 1, b?.y)
  }
}(bloques)

def puerta = { blqs ->
  def p = blqs.find { 
      it.x == 0 && it.tipo == bLibre 
  }
  p.tipo = bActual
  return p
}(bloques)

def asignaActual = { b ->
  b.tipo = bActual
  def abreCamino = { leafString   ->
    if(b."${leafString}" && b."${leafString}".tipo == bLibre) {
      b.tipo = bVisitado
      b."${leafString}".tipo = bActual
      return true
    }
    return false
  }
  def repiteCamino = { leafString   ->
    if(b."${leafString}" && b."${leafString}".tipo == bVisitado) {
      b.tipo = bRepetido
      b."${leafString}".tipo = bActual
      return true
    }
    return false
  }

  //El orden Orden es muy importante
  def caminoOps = ["abajo", "derecha", "izquierda", "arriba"]
  return (caminoOps.find { abreCamino(it)} || caminoOps.find { repiteCamino(it)})  
}

def buscarSalida = { blqs ->
  def seguir = true
  while(seguir) {
    def actual = blqs.find { 
      it.tipo == bActual
    }
    def vecinos = [
        actual.abajo, actual.arriba, 
        actual.izquierda, actual.derecha
    ]    
    if(vecinos.findAll { it == null } && actual != puerta) {
      termino = System.currentTimeMillis();
      seguir = false
    } else {
      seguir = asignaActual(actual)
    }
  }  
}(bloques)

def pintaLaberinto = { blqs, debug = false ->
  def lCount = 0
  blqs.each { b ->    
    if(b.x != lCount) {
      lCount++
      print '\n'
    }
    if(debug) {
      print b.tipo
    } else {
      if(b.tipo in [bActual, bVisitado]) {
        print bCamino      
      } 
      else if(b.tipo == bRepetido) {
        print bLibre
      } else {
        print b.tipo
      }    
    }
  }  
  print '\n'
  println "Termino en ${termino - inicio} millis"
}(bloques)