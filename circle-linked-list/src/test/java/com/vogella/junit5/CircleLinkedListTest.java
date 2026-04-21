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
        
        // 1. Ejecutamos el método real
        String result = list.toString();
        
        // 2. Comprobamos el resultado (Assertion tradicional)
        // NO usamos verify(list).toString()
        assertNotNull(result);
        assertTrue(result.contains("Data"), "El string debería contener el dato insertado");
        assertTrue(result.contains("[") && result.contains("]"), "Debería tener formato de lista");
    }
}