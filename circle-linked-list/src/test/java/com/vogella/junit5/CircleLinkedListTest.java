package com.vogella.junit5;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CircleLinkedListTest {

    // Usamos @Spy para ejecutar el código real de la lista
    // pero poder "vigilar" las llamadas a sus métodos.
    @Spy
    CircleLinkedList<String> list = new CircleLinkedList<>();

    @Test
    void testAppendIncrementsSizeAndCallsGetSize() {
        // Act
        list.append("Primer Elemento");
        list.append("Segundo Elemento");

        // Assert: Verificación de estado tradicional
        assertEquals(2, list.getSize());

        // Mockito Verify: Verificamos que se consultó el tamaño 
        // (comportamiento de interacción)
        verify(list, atLeastOnce()).getSize();
    }

    @Test
    void testRemoveBehaviorWithSpy() {
        list.append("A");
        list.append("B");
        list.append("C");

        // Act: Borramos la posición 1 (el elemento "B")
        String removed = list.remove(1);

        // Assert
        assertEquals("B", removed);
        assertEquals(2, list.getSize());
        
        // Mockito: Verificamos que el método remove fue llamado con el parámetro exacto
        verify(list).remove(1);
    }

    @Test
    void testToStringInteraction() {
        list.append("Data");
        
        // Llamamos al toString
        String result = list.toString();
        
        // Verificamos que el resultado contiene los corchetes de la estructura
        assertTrue(result.contains("["));
        assertTrue(result.contains("Data"));
        
        // Verificamos que el método toString fue invocado
        verify(list).toString();
    }
}