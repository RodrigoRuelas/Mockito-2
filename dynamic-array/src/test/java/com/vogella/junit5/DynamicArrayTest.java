package com.vogella.junit5;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.function.Consumer;

@ExtendWith(MockitoExtension.class)
class DynamicArrayTest {

    // Usamos @Spy para rastrear el crecimiento real del arreglo
    @Spy
    DynamicArray<String> dynamicArray = new DynamicArray<>(2); // Capacidad inicial pequeña

    @Test
    void testAddTriggersResize() {
        // Llenamos la capacidad inicial (2)
        dynamicArray.add("A");
        dynamicArray.add("B");
        
        // Este tercer elemento debería disparar la lógica de redimensionamiento
        dynamicArray.add("C");

        assertEquals(3, dynamicArray.getSize());
        // Verificamos que se llamó al método add tres veces
        verify(dynamicArray, times(3)).add(anyString());
    }

    @Test
    void testIteratorInteraction() {
        dynamicArray.add("Uno");
        dynamicArray.add("Dos");

        // Mockeamos un Consumer para ver si el iterador lo invoca correctamente
        Consumer<String> mockConsumer = mock(Consumer.class);
        
        // Act: Usamos el forEach del Iterable (que usa el iterador internamente)
        dynamicArray.forEach(mockConsumer);

        // Assert: Verificamos que el consumidor recibió los elementos
        verify(mockConsumer).accept("Uno");
        verify(mockConsumer).accept("Dos");
        verify(mockConsumer, times(2)).accept(anyString());
    }

    @Test
    void testStreamIntegrationWithMockito() {
        dynamicArray.add("StreamData");

        // Creamos un mock para interceptar la salida del stream
        Consumer<String> streamObserver = mock(Consumer.class);
        
        // Act
        dynamicArray.stream().forEach(streamObserver);

        // Verify: El stream debe haber pasado el dato al consumidor
        verify(streamObserver).accept("StreamData");
    }

    @Test
    void testRemoveCallsInternalMethods() {
        dynamicArray.add("Eliminar");
        
        // Act
        dynamicArray.remove(0);

        // Verificamos el estado real
        assertTrue(dynamicArray.isEmpty());
        
        // CORRECCIÓN: 
        // Si quieres verificar que el tamaño ahora es 0, debes LLAMAR al método primero:
        int actualSize = dynamicArray.getSize(); 
        
        // Ahora sí podemos verificar que se llamó
        verify(dynamicArray).getSize();
        assertEquals(0, actualSize);
    }
}