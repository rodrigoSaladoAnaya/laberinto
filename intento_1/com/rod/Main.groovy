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

//Tipo de bloques
Character bLibre = ' '
Character bOcupado = '*'
Character bActual = 'A'
Character bVisitado = '1'
Character bRepetido = '2'
Character bCamino = '+'

def pintaLaberinto = {
  def lCount = 0
  bloques.each { b ->
    if(b.x != lCount) {
      lCount++
      print '\n'
    }
    //print b.tipo
    
    if(b.tipo in [bActual, bVisitado]) {
      print bCamino      
    } 
    else if(b.tipo == bRepetido) {
      print bLibre
    } else {
      print b.tipo
    }
  }  
  print '\n'
}

def entrada = bloques.find { 
    it.x == 0 && it.tipo == bLibre 
}

entrada.tipo = bActual

def asignaActual = { b ->
  b.tipo = bActual
  if(b.abajo && b.abajo.tipo == bLibre) { //Abren camino
    b.tipo = bVisitado
    b.abajo.tipo = bActual    
  } else if(b.derecha && b.derecha.tipo == bLibre) {
    b.tipo = bVisitado
    b.derecha.tipo = bActual    
  } else if(b.izquierda && b.izquierda.tipo == bLibre) {
    b.tipo = bVisitado
    b.izquierda.tipo = bActual    
  } else if(b.arriba && b.arriba.tipo == bLibre) {
    b.tipo = bVisitado
    b.arriba.tipo = bActual    
  } else if(b.derecha && b.derecha.tipo == bVisitado) { // Repiten camino
      b.tipo = bRepetido
      b.derecha.tipo = bActual      
  } else if(b.arriba && b.arriba.tipo == bVisitado) {
      b.tipo = bRepetido
      b.arriba.tipo = bActual      
  } else if(b.izquierda && b.izquierda.tipo == bVisitado) {
      b.tipo = bRepetido
      b.izquierda.tipo = bActual
  } else if(b.abajo && b.abajo.tipo == bVisitado) {
      b.tipo = bRepetido
      b.abajo.tipo = bActual
  } else {    
    return false
  } 
  return true 
}
def seguir = true
while(seguir) {
  def actual = bloques.find { 
    it.tipo == bActual
  }
  if(actual.abajo == null) {
    seguir = false
  } else {
    seguir = asignaActual(actual)
  }
}

pintaLaberinto()