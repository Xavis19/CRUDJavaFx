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

        table.getColumns().addAll(colId, colNombre, colCreditos, colProfesor);
        refrescarTabla();

        TextField tfNombre = new TextField();
        tfNombre.setPromptText("Nombre de la materia");
        TextField tfCreditos = new TextField();
        tfCreditos.setPromptText("Créditos");
        TextField tfProfesor = new TextField();
        tfProfesor.setPromptText("Profesor");

        Button btnAgregar = new Button("Agregar");
        btnAgregar.setOnAction(e -> {
            try {
                Materia m = new Materia(0, tfNombre.getText(), Integer.parseInt(tfCreditos.getText()), tfProfesor.getText());
                MateriaDAO.agregar(m);
                limpiarCampos(tfNombre, tfCreditos, tfProfesor);
                refrescarTabla();
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        Button btnActualizar = new Button("Actualizar");
        btnActualizar.setOnAction(e -> {
            Materia m = table.getSelectionModel().getSelectedItem();
            if (m != null) {
                try {
                    m.setNombre(tfNombre.getText());
                    m.setCreditos(Integer.parseInt(tfCreditos.getText()));
                    m.setProfesor(tfProfesor.getText());
                    MateriaDAO.actualizar(m);
                    limpiarCampos(tfNombre, tfCreditos, tfProfesor);
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
                    limpiarCampos(tfNombre, tfCreditos, tfProfesor);
                    refrescarTabla();
                } catch (Exception ex) { ex.printStackTrace(); }
            }
        });

        table.setOnMouseClicked(event -> {
            Materia m = table.getSelectionModel().getSelectedItem();
            if (m != null) {
                tfNombre.setText(m.getNombre());
                tfCreditos.setText(String.valueOf(m.getCreditos()));
                tfProfesor.setText(m.getProfesor());
            }
        });

        HBox hBox = new HBox(10, tfNombre, tfCreditos, tfProfesor, btnAgregar, btnActualizar, btnBorrar);
        VBox vBox = new VBox(10, table, hBox);
        vBox.setPadding(new Insets(10));

        primaryStage.setScene(new Scene(vBox, 800, 400));
        primaryStage.setTitle("CRUD de Materias - Universidad");
        primaryStage.show();
    }

    private void limpiarCampos(TextField tfNombre, TextField tfCreditos, TextField tfProfesor) {
        tfNombre.clear();
        tfCreditos.clear();
        tfProfesor.clear();
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