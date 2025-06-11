import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.*;
import javafx.geometry.Insets;

import modelo.Materia;
import dao.MateriaDAO;

public class Main extends Application {
    // Tabla principal donde se muestran las materias y sus datos
    private TableView<Materia> table;
    // Lista observable que contiene los datos que se visualizan en la tabla
    private ObservableList<Materia> data;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // --- Configuración de la tabla y sus columnas ---
        table = new TableView<>();

        // Columna para el ID de la materia (clave primaria autoincremental)
        TableColumn<Materia, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());

        // Columna para el nombre de la materia
        TableColumn<Materia, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));

        // Columna para los créditos de la materia
        TableColumn<Materia, Integer> colCreditos = new TableColumn<>("Créditos");
        colCreditos.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getCreditos()).asObject());

        // Columna para el profesor (se asigna automáticamente en el backend)
        TableColumn<Materia, String> colProfesor = new TableColumn<>("Profesor");
        colProfesor.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getProfesor()));

        // Columna para el alumno (dato ingresado por el usuario)
        TableColumn<Materia, String> colAlumno = new TableColumn<>("Alumno");
        colAlumno.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAlumno()));

        // Se agregan todas las columnas a la tabla
        table.getColumns().addAll(colId, colNombre, colCreditos, colProfesor, colAlumno);

        // Llama a la función para cargar los datos desde la base y mostrarlos en la tabla
        refrescarTabla();

        // --- Campos de texto para ingresar datos ---
        // Campo para el nombre de la materia
        TextField tfNombre = new TextField();
        tfNombre.setPromptText("Nombre de la materia");
        // *** Campo de profesor eliminado porque el profesor se asigna automático ***
        // Campo para el alumno
        TextField tfAlumno = new TextField();
        tfAlumno.setPromptText("Alumno");

        // Variable para guardar los créditos automáticos según la materia escrita
        final int[] creditosAuto = {0};

        // Listener para asignar créditos automáticamente según la materia escrita
        tfNombre.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.equalsIgnoreCase("Programacion1")) {
                creditosAuto[0] = 3;
            } else if (newText.equalsIgnoreCase("Matematicas")) {
                creditosAuto[0] = 4;
            } else if (newText.equalsIgnoreCase("Historia")) {
                creditosAuto[0] = 2;
            } else {
                creditosAuto[0] = 0; // Si la materia no está en la lista, créditos es 0
            }
            // El profesor se asigna automáticamente en el backend, no en un campo visual
        });

        // --- Botón para agregar una nueva materia ---
        Button btnAgregar = new Button("Agregar");
        btnAgregar.setOnAction(e -> {
            try {
                // Asigna automáticamente el profesor según el nombre de la materia
                String profesorAsignado = asignarProfesor(tfNombre.getText());
                // Crea el objeto Materia con los datos ingresados y automáticos
                Materia m = new Materia(0, tfNombre.getText(), creditosAuto[0], profesorAsignado, tfAlumno.getText());
                // Llama al DAO para agregar la materia a la base de datos
                MateriaDAO.agregar(m);
                // Limpia los campos de texto después de agregar
                limpiarCampos(tfNombre, tfAlumno);
                // Refresca la tabla para mostrar la materia agregada
                refrescarTabla();
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        // --- Botón para actualizar la materia seleccionada ---
        Button btnActualizar = new Button("Actualizar");
        btnActualizar.setOnAction(e -> {
            Materia m = table.getSelectionModel().getSelectedItem();
            if (m != null) {
                try {
                    // Actualiza los valores del objeto Materia con los datos actuales y automáticos
                    m.setNombre(tfNombre.getText());
                    m.setCreditos(creditosAuto[0]);
                    m.setProfesor(asignarProfesor(tfNombre.getText()));
                    m.setAlumno(tfAlumno.getText());
                    // Llama al DAO para actualizar en la base de datos
                    MateriaDAO.actualizar(m);
                    // Limpia los campos de texto
                    limpiarCampos(tfNombre, tfAlumno);
                    // Refresca la tabla para mostrar los cambios
                    refrescarTabla();
                } catch (Exception ex) { ex.printStackTrace(); }
            }
        });

        // --- Botón para borrar la materia seleccionada ---
        Button btnBorrar = new Button("Borrar");
        btnBorrar.setOnAction(e -> {
            Materia m = table.getSelectionModel().getSelectedItem();
            if (m != null) {
                try {
                    // Llama al DAO para eliminar la materia por su ID
                    MateriaDAO.borrar(m.getId());
                    // Limpia los campos de texto
                    limpiarCampos(tfNombre, tfAlumno);
                    // Refresca la tabla para reflejar el borrado
                    refrescarTabla();
                } catch (Exception ex) { ex.printStackTrace(); }
            }
        });

        // Al hacer clic en una fila de la tabla, carga los datos en los campos de texto
        table.setOnMouseClicked(event -> {
            Materia m = table.getSelectionModel().getSelectedItem();
            if (m != null) {
                tfNombre.setText(m.getNombre());
                tfAlumno.setText(m.getAlumno());
                // Asigna los créditos automáticamente según el nombre de la materia seleccionada
                if (m.getNombre().equalsIgnoreCase("Programacion")) {
                    creditosAuto[0] = 3;
                } else if (m.getNombre().equalsIgnoreCase("Matematicas")) {
                    creditosAuto[0] = 4;
                } else if (m.getNombre().equalsIgnoreCase("Historia")) {
                    creditosAuto[0] = 2;
                } else {
                    creditosAuto[0] = 0;
                }
            }
        });

        // --- Layouts para organizar los controles en la ventana ---
        // HBox: organiza horizontalmente los campos y botones (sin campo profesor)
        HBox hBox = new HBox(10, tfNombre, tfAlumno, btnAgregar, btnActualizar, btnBorrar);

        // VBox: organiza la tabla y el HBox en vertical
        VBox vBox = new VBox(10, table, hBox);
        vBox.setPadding(new Insets(10)); // Margen interno

        // Configura y muestra la ventana principal
        primaryStage.setScene(new Scene(vBox, 800, 400));
        primaryStage.setTitle("CRUD de Materias - Universidad");
        primaryStage.show();
    }

    // Función que asigna automáticamente el profesor según la materia
    private String asignarProfesor(String materia) {
        if (materia == null) return "";
        switch (materia.trim().toLowerCase()) {
            case "matematicas":
            case "cálculo diferencial":
                return "Mayra";
            case "programacion":
            case "programacion1":
            case "administracion del tiempo libre":
                return "Arle";
            case "fundamentos":
                return "Ana Maria";
            default:
                return "";
        }
    }

    // Función para limpiar los campos después de agregar, actualizar o borrar
    private void limpiarCampos(TextField tfNombre, TextField tfAlumno) {
        tfNombre.clear();
        tfAlumno.clear();
    }

    // Función para cargar los datos de la base de datos y mostrarlos en la tabla
    private void refrescarTabla() {
        try {
            // Solicita todas las materias al DAO y las coloca en la tabla
            data = FXCollections.observableArrayList(MateriaDAO.listarTodos());
            table.setItems(data);
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    // Método principal para lanzar la aplicación JavaFX
    public static void main(String[] args) {
        launch(args);
    }
}