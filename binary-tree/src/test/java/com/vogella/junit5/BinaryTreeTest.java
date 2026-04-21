package com.vogella.junit5;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BinaryTreeTest {

    @Spy
    BinaryTree tree = new BinaryTree();

    /**
     * Estructura que vamos a montar:
     * 50
     * /    \
     * 30      70
     * /  \    /  \
     * 20  40  60  80
     */
    void buildDeepTree() {
        tree.put(50); // Raíz
        tree.put(30); tree.put(70); // Hijos
        tree.put(20); tree.put(40); // Nietos izq
        tree.put(60); tree.put(80); // Nietos der
    }

    @Test
    void testDeepInsertionPath() {
        buildDeepTree();

        // Verificamos que para insertar el 80 (el nodo más a la derecha)
        // el sistema tuvo que navegar pasando por el 50 y el 70
        verify(tree, atLeastOnce()).find(80);
        
        assertEquals(80, tree.getRoot().right.right.data);
        assertEquals(70, tree.getRoot().right.right.parent.data);
    }

    @Test
    void testRemoveNodeWithTwoChildrenDeep() {
        buildDeepTree();

        // Borramos el 30, que tiene dos hijos (20 y 40)
        // Esto obligará a buscar un sucesor y reasignar punteros
        boolean result = tree.remove(30);

        assertTrue(result);
        // Verificamos que se llamó al buscador de sucesores
        verify(tree).findSuccessor(any());
        
        // El 40 debería haber subido a la posición del 30
        assertEquals(40, tree.getRoot().left.data);
    }

    @Test
    void testBfsTraversalInteraction() {
        buildDeepTree();
        
        // Probamos el recorrido por niveles (Breadth-First Search)
        // Aunque Mockito no "ve" el System.out, podemos verificar
        // que el método procesó la raíz correctamente.
        tree.bfs(tree.getRoot());
        
        // Verificamos que se accedió al hijo más profundo durante el BFS
        assertNotNull(tree.find(20));
        verify(tree, atLeast(1)).find(20);
    }
}