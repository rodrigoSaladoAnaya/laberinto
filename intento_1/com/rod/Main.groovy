package com.rod

def inicio = System.currentTimeMillis();
def termino 

def laberinto = {
  new File('./laberintos/laberinto.txt').text
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
  println "Termino en ${termino - inicio} millis"
}

def asignaPuerta = {
  bloques.find { 
      it.x == 0 && it.tipo == bLibre 
  }.tipo = bActual
}()

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
  if(caminoOps.find { 
    abreCamino(it) == true 
  }){} else if(caminoOps.find { 
    repiteCamino(it) == true 
  }){} else {    
    return false
  } 
  return true 
}

def buscarSalida = {
  def seguir = true
  while(seguir) {
    def actual = bloques.find { 
      it.tipo == bActual
    }
    if(actual.abajo == null) {
      termino = System.currentTimeMillis();
      seguir = false
    } else {
      seguir = asignaActual(actual)
    }
  }  
}()

pintaLaberinto()