package com.jsyryczynski;

import lombok.AllArgsConstructor;

/**
 *
 */
public class Edge {
    String[] vertices;

    Edge(String a, String b) {
        this.vertices = new String[] {a,b};
    }

    boolean leadsTo(String vertex){
        return vertices[0].equals(vertex) || vertices[1].equals(vertex);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Edge))
            return false;
        Edge other = (Edge)o;
        if (vertices[0].equals(((Edge) o).vertices[0]) && vertices[1].equals(((Edge) o).vertices[1])) {
            return true;
        }
        else if (vertices[0].equals(((Edge) o).vertices[1]) && vertices[1].equals(((Edge) o).vertices[0])) {
            return true;
        }
        return false;
    }

    @Override
    public final int hashCode() {
        if (vertices[0].compareTo(vertices[1]) < 0) {
            return vertices[0].hashCode();
        }
        else {
            return vertices[1].hashCode();
        }
    }

    public void replace(String oldVertex, String newVertex) {
        if (vertices[0].equals(oldVertex)){
            vertices[0] = newVertex;
        }
        if (vertices[1].equals(oldVertex)){
            vertices[1] = newVertex;
        }
    }
}
