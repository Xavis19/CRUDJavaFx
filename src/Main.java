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
    private TableView<Materia> table;
    private ObservableList<Materia> data;

    @Override
    public void start(Stage primaryStage) throws Exception {
        table = new TableView<>();
        TableColumn<Materia, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        TableColumn<Materia, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
        TableColumn<Materia, Integer> colCreditos = new TableColumn<>("Créditos");
        colCreditos.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getCreditos()).asObject());
        TableColumn<Materia, String> colProfesor = new TableColumn<>("Profesor");
        colProfesor.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getProfesor()));
        TableColumn<Materia, String> colAlumno = new TableColumn<>("Alumno");
        colAlumno.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAlumno()));


        table.getColumns().addAll(colId, colNombre, colCreditos, colProfesor, colAlumno);
        refrescarTabla();

        TextField tfNombre = new TextField();
        tfNombre.setPromptText("Nombre de la materia");
        TextField tfProfesor = new TextField();
        tfProfesor.setPromptText("Profesor");
        TextField tfAlumno = new TextField();
        tfAlumno.setPromptText("Alumno");


        // Variable para guardar los créditos automáticos
        final int[] creditosAuto = {0};

        tfNombre.textProperty().addListener((obs, oldText, newText) -> {
            // Puedes agregar más materias y sus créditos aquí
            if (newText.equalsIgnoreCase("Programacion1")) {
                creditosAuto[0] = 3;
            } else if (newText.equalsIgnoreCase("Matematicas")) {
                creditosAuto[0] = 4;
            } else if (newText.equalsIgnoreCase("Historia")) {
                creditosAuto[0] = 2;
            } else {
                creditosAuto[0] = 0; // Por defecto 0 si la materia no está en la lista
            }
        });

        Button btnAgregar = new Button("Agregar");
        btnAgregar.setOnAction(e -> {
            try {
                Materia m = new Materia(0, tfNombre.getText(), creditosAuto[0], tfProfesor.getText(), tfAlumno.getText());
                MateriaDAO.agregar(m);
                limpiarCampos(tfNombre, tfProfesor, tfAlumno);
                refrescarTabla();
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        Button btnActualizar = new Button("Actualizar");
        btnActualizar.setOnAction(e -> {
            Materia m = table.getSelectionModel().getSelectedItem();
            if (m != null) {
                try {
                    m.setNombre(tfNombre.getText());
                    m.setCreditos(creditosAuto[0]);
                    m.setProfesor(tfProfesor.getText());
                    m.setAlumno(tfAlumno.getText());
                    MateriaDAO.actualizar(m);
                    limpiarCampos(tfNombre, tfProfesor, tfAlumno);
                    refrescarTabla();
                } catch (Exception ex) { ex.printStackTrace(); }
            }
        });

        Button btnBorrar = new Button("Borrar");
        btnBorrar.setOnAction(e -> {
            Materia m = table.getSelectionModel().getSelectedItem();
            if (m != null) {
                try {
                    MateriaDAO.borrar(m.getId());
                    limpiarCampos(tfNombre, tfProfesor, tfAlumno);
                    refrescarTabla();
                } catch (Exception ex) { ex.printStackTrace(); }
            }
        });

        table.setOnMouseClicked(event -> {
            Materia m = table.getSelectionModel().getSelectedItem();
            if (m != null) {
                tfNombre.setText(m.getNombre());
                tfProfesor.setText(m.getProfesor());
                tfAlumno.setText(m.getAlumno());
                // Actualiza los créditos automáticos al seleccionar una fila
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

        HBox hBox = new HBox(10, tfNombre, tfProfesor, tfAlumno, btnAgregar, btnActualizar, btnBorrar);
        VBox vBox = new VBox(10, table, hBox);
        vBox.setPadding(new Insets(10));

        primaryStage.setScene(new Scene(vBox, 800, 400));
        primaryStage.setTitle("CRUD de Materias - Universidad");
        primaryStage.show();
    }

    private void limpiarCampos(TextField tfNombre, TextField tfProfesor, TextField tfAlumno) {
        tfNombre.clear();
        tfProfesor.clear();
        tfAlumno.clear();
    }

    private void refrescarTabla() {
        try {
            data = FXCollections.observableArrayList(MateriaDAO.listarTodos());
            table.setItems(data);
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    public static void main(String[] args) {
        launch(args);
    }
}