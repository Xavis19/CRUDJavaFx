package modelo;

public class Materia {
    private int id;
    private String nombre;
    private int creditos;
    private String profesor;

    public Materia(int id, String nombre, int creditos, String profesor) {
        this.id = id;
        this.nombre = nombre;
        this.creditos = creditos;
        this.profesor = profesor;
    }

    // Getters y setters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public int getCreditos() { return creditos; }
    public String getProfesor() { return profesor; }

    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setCreditos(int creditos) { this.creditos = creditos; }
    public void setProfesor(String profesor) { this.profesor = profesor; }
}