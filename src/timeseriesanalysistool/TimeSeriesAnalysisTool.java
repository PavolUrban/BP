/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timeseriesanalysistool;

import Actions.Actions;
import Algorithms.CorrelationNetwork;
import Algorithms.NVG;
import Layouts.Layout;
import Layouts.LayoutCircular;
import Layouts.LayoutISOM;
import Layouts.LayoutKK;
import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import NetworkMetrics.ThreeVerticesMotifs;
import cern.colt.map.PrimeFinder;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import static edu.uci.ics.jung.graph.util.EdgeType.DIRECTED;
import edu.uci.ics.jung.graph.util.Pair;
import java.awt.Graphics2D;
import java.awt.SplashScreen;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import timeseriesanalysistool.GUI.ChartMaker;
import timeseriesanalysistool.GUI.Design;

/**
 *
 * @author pavol
 */
public class TimeSeriesAnalysisTool extends Application {

    Canvas canvas1 = new Canvas(Design.canvasWidth, Design.canvasHeight);
    private Graph<Vertex, Edge> network;
    private final TableView<FilesToConvert> table = new TableView<>();
    private TableView table2 = new TableView();
    Button buttonChooseSeries1 = new Button("Add Time Series files into Group 1...");
    Button buttonClearGroup1 = new Button("Clear list of series of Group 1");
    Button buttonClearGroup2 = new Button("Clear list of series of Group 2");
    Button buttonSaveNetworkAsPNG = new Button("Save network as PNG");
    Button buttonChooseSeries2 = new Button("Add Time Series files into Group 2...");
    Label labelgroup1 = new Label("Group 1");
    Label labelgroup2 = new Label("Group 2");
    Label labelRange = new Label("Change critical value");
    Label labelRadioButton = new Label("Choose algorithm which you want to use for time series conversion");
    Separator separator1 = new Separator();
    Separator separator2 = new Separator();
    Slider slider = new Slider(0.1, 0.9, 0.5);
   

    int radioButtonValue = 1;
    double criticalValue = 0.5;

    Stage loadingStage;

    Label labelWaitPlease;
    Boolean networkIsNull = true;

    ObservableList<FilesToConvert> data = FXCollections.observableArrayList();
    ObservableList<FilesToConvert> data2 = FXCollections.observableArrayList();
    List<File> files = new ArrayList();
    List<File> files2 = new ArrayList();

    private void drawEdge(GraphicsContext gc, double fromX, double fromY, double toX, double toY, double edgeWidth, Color color) {
        gc.setLineWidth(edgeWidth);
        gc.setStroke(color);
        gc.strokeLine(fromX, fromY, toX, toY);
    }

    private void drawVertex(GraphicsContext gc, double positionX, double positionY, double size, Color color) {
        double leftX = positionX - size / 2;
        double leftY = positionY - size / 2;
        double sizeLine = size * 0.05;
        gc.setFill(color);
        gc.fillOval(leftX, leftY, size, size);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(sizeLine);
        gc.strokeOval(leftX, leftY, size, size);
    }

    public void drawVertex(GraphicsContext gc, Vertex v, Color color) {
        drawVertex(gc, v.getPositionX(), v.getPositionY(), v.getSize(), color);
    }

    public void loadSeries(int idOfTable) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
        chooser.setFileFilter(filter);
        chooser.setMultiSelectionEnabled(true);
        // Show the dialog; wait until dialog is closed
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            // Retrieve the selected files.

            if (idOfTable == 1) {
                files.addAll(Arrays.asList(chooser.getSelectedFiles()));
                data.clear();

                for (File f : files) {

                    String nameOfFile = f.getName();
                    System.out.println("You choose file in table 1: " + nameOfFile);
                    FilesToConvert myFile = new FilesToConvert(nameOfFile);
                    data.add(myFile);
                }
            } else {
                files2.addAll(Arrays.asList(chooser.getSelectedFiles()));
                data2.clear();

                for (File f : files2) {

                    String nameOfFile = f.getName();
                    System.out.println("You choose file in table 2: " + nameOfFile);
                    FilesToConvert myFile = new FilesToConvert(nameOfFile);
                    data2.add(myFile);
                }
            }

        }

    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root, Design.sceneWidth, Design.sceneHeight);

        primaryStage.setTitle("Time Series Analysis Tool");
        primaryStage.setWidth(Design.sceneWidth);
        primaryStage.setHeight(Design.sceneHeight);

        separator1.setMinHeight(25); //TODO ponastavovat dynamicky vsetko
        separator1.setVisible(false);

        separator2.setMinHeight(25); //TODO ponastavovat dynamicky vsetko
        separator2.setVisible(false);

        final HBox hbox = new HBox();
        hbox.setMinWidth(Design.minTableWidth);
        hbox.setSpacing(5);
        buttonChooseSeries1.setMinWidth(Design.minTableWidth / 2 - 2.5);
        buttonClearGroup1.setMinWidth(Design.minTableWidth / 2 - 2.5);
        hbox.getChildren().addAll(buttonChooseSeries1, buttonClearGroup1);

        final HBox hbox2 = new HBox();
        hbox2.setMinWidth(Design.minTableWidth);
        hbox2.setSpacing(5);
        buttonChooseSeries2.setMinWidth(Design.minTableWidth / 2 - 2.5);
        buttonClearGroup2.setMinWidth(Design.minTableWidth / 2 - 2.5);
        hbox2.getChildren().addAll(buttonChooseSeries2, buttonClearGroup2);

        Label label = new Label();
        Popup popup = new Popup();
        popup.getContent().add(label);

        double offset = 17;
        slider.setMajorTickUnit(0.1);
        slider.setMinorTickCount(0);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setSnapToTicks(true);
        slider.setMinHeight(Slider.USE_PREF_SIZE);

        slider.setOnMouseMoved(e -> {
            NumberAxis axis = (NumberAxis) slider.lookup(".axis");
            Point2D locationInAxis = axis.sceneToLocal(e.getSceneX(), e.getSceneY());
            double mouseX = locationInAxis.getX();
            double value = axis.getValueForDisplay(mouseX).doubleValue();
            if (value >= slider.getMin() && value <= slider.getMax()) {
                label.setText("If you choose larger critical value, there will be more edges between vertices. If you choose lower critical value, there will be less edges between vertices.");
            }

            popup.setAnchorX(e.getScreenX());
            popup.setAnchorY(e.getScreenY() + offset);
        });

        slider.setOnMouseEntered(e -> popup.show(slider, e.getScreenX(), e.getScreenY() + offset));
        slider.setOnMouseExited(e -> popup.hide());

        labelgroup1.setFont(new Font("Arial", 20.0));
        labelgroup2.setFont(new Font("Arial", 20.0));

        final ToggleGroup group = new ToggleGroup();
        RadioButton rb1 = new RadioButton("NVG");
        rb1.setToggleGroup(group);
        rb1.setUserData("NVG");

        RadioButton rb2 = new RadioButton("HVG");
        rb2.setToggleGroup(group);
        rb2.setUserData("HVG");

        RadioButton rb3 = new RadioButton("Correlation network - undirected");
        rb3.setToggleGroup(group);
        rb3.setUserData("3rd");

        RadioButton rb4 = new RadioButton("Correlation network - directed");
        rb4.setToggleGroup(group);
        rb4.setUserData("4th");

        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                    Toggle old_toggle, Toggle new_toggle) {
                if (group.getSelectedToggle() != null) {
                    if (group.getSelectedToggle() == rb1) {
                        labelRange.setVisible(false);
                        slider.setVisible(false);

                        radioButtonValue = 1;
                        System.out.println("Tlacidlo 1");
                    } else if (group.getSelectedToggle() == rb2) {
                        labelRange.setVisible(false);
                        slider.setVisible(false);
                        radioButtonValue = 2;
                        System.out.println("Tlacidlo 2");
                    } else if (group.getSelectedToggle() == rb3) {
                        radioButtonValue = 3;
                        labelRange.setVisible(true);
                        slider.setVisible(true);
                        System.out.println("Correlation network - undirected");
                    } else if (group.getSelectedToggle() == rb4) {
                        labelRange.setVisible(false);
                        slider.setVisible(false);
                        radioButtonValue = 4;
                        System.out.println("Tlacidlo 4");
                    }
                }

            }
        });

        labelRange.setVisible(false);
        slider.setVisible(false);

        slider.valueProperty().addListener((obs, oldval, newVal) -> {
         //code
         criticalValue = slider.getValue();
         slider.setValue((newVal.doubleValue()));
         System.out.println("Value"+criticalValue);
         });
         
        final HBox hboxButtons = new HBox();
        hboxButtons.setMinWidth(Design.minTableWidth);
        hboxButtons.setSpacing(25);

        hboxButtons.getChildren().addAll(rb1, rb2, rb3, rb4);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(30, 0, 0, 930)); //TODO dynamicky
        vbox.getChildren().addAll(labelRange, slider, separator1, labelRadioButton, hboxButtons, labelgroup1, hbox, createTable(table, 1), separator2, labelgroup2, hbox2, createTable(table2, 2), buttonSaveNetworkAsPNG);

        root.getChildren().addAll(vbox);

        GraphicsContext gc = canvas1.getGraphicsContext2D();
        gc.setFill(Color.CORNSILK);
        gc.setStroke(Color.BLUE);
        gc.fillRect(0, 0, Design.canvasWidth, Design.canvasHeight);

        final VBox vboxCanvas = new VBox();
        vboxCanvas.setSpacing(5);
        vboxCanvas.setPadding(new Insets(75, 0, 0, 10)); //TODO dynamicky
        vboxCanvas.getChildren().addAll(canvas1);

        root.getChildren().add(vboxCanvas);
        root.getChildren().add(createMainMenu());
        primaryStage.setScene(scene);
        primaryStage.show();

        buttonChooseSeries1.setOnAction((event) -> {
            // Button was clicked, do something...
            System.out.println("Adding into group 1");
            loadSeries(1);
        });

        buttonChooseSeries2.setOnAction((event) -> {
            // Button was clicked, do something...
            System.out.println("Adding into group 2");
            loadSeries(2);
        });

        buttonClearGroup1.setOnAction((event) -> {
            // Button was clicked, do something...
            if (table.getItems().size() > 0) {
                for (int i = 0; i < table.getItems().size(); i++) {
                    table.getItems().clear();
                    files.clear();
                    data.clear();
                }
            }

        });

        buttonClearGroup2.setOnAction((event) -> {
            // Button was clicked, do something...
            if (table2.getItems().size() > 0) {
                for (int i = 0; i < table2.getItems().size(); i++) {
                    table2.getItems().clear();
                    files2.clear();
                    data2.clear();
                }
            }

        });

        buttonSaveNetworkAsPNG.setOnAction((event) -> {
            // Button was clicked, do something...
            Actions a = new Actions();
            a.saveNetworkAsImage(canvas1, primaryStage);
        });

    }

    public void displayLoading(File f) {
        final ProgressIndicator pin = new ProgressIndicator();
        pin.setProgress(-1.0f);
        pin.setVisible(false);

        final VBox vb = new VBox();
        labelWaitPlease = new Label("Choosen file is converted right now. Wait, please");
        labelWaitPlease.setVisible(false);

        Button start = new Button("Start conversion");

        start.setOnAction((event) -> {
            networkIsNull = false;
            labelWaitPlease.setVisible(true);
            pin.setVisible(true);
            start.setDisable(true);
            startTask(f);
        });

        Button cancel = new Button("Cancel");
        cancel.setOnAction((event) -> {
            network = null;
            networkIsNull = true;
            loadingStage.close();
            GraphicsContext gc = canvas1.getGraphicsContext2D();
            gc.setFill(Color.WHITE);
            gc.fillRect(0, 0, Design.canvasWidth, Design.canvasHeight);
        });

        Group root = new Group();
        Scene scene = new Scene(root, 350, 350);
        loadingStage = new Stage();
        loadingStage.setScene(scene);
        loadingStage.setTitle("Progress Controls");
        start.setMinWidth(150);
        start.setMaxWidth(150); //dynamicky
        cancel.setMaxWidth(150);
        cancel.setMinWidth(150);

        HBox optionButtons = new HBox();
        optionButtons.setSpacing(5);
        optionButtons.getChildren().addAll(start, cancel);
        optionButtons.setAlignment(Pos.CENTER);
        vb.setSpacing(5);
        vb.setAlignment(Pos.CENTER);
        vb.getChildren().addAll(pin, labelWaitPlease, optionButtons);
        scene.setRoot(vb);
        loadingStage.initStyle(StageStyle.UNDECORATED);
        loadingStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private MenuBar createMainMenu() {
        MenuBar menu = new MenuBar();
        menu.setMinSize(Design.sceneWidth, 10);

        Menu menuFile = new Menu("Metrics");
        MenuItem betweennessCentrality = new MenuItem("Betweenness centrality");
        betweennessCentrality.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                ChartMaker ch = new ChartMaker();
                ch.createChart("Betweenness centrality", network, 5);
            }
        });

        menuFile.getItems().addAll(betweennessCentrality);

        MenuItem closenessCentrality = new MenuItem("Closeness centrality");
        closenessCentrality.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                ChartMaker ch = new ChartMaker();
                ch.createChart("Closeness centrality", network, 5);
            }
        });
        menuFile.getItems().addAll(closenessCentrality);

        MenuItem motifsThreeVertices = new MenuItem("Motifs three vertices");
        motifsThreeVertices.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                /*ChartMaker ch= new ChartMaker();
                 ch.createChart("Closeness centrality", network, range);*/
                ThreeVerticesMotifs tvm = new ThreeVerticesMotifs(network);
                tvm.countMotifs();
            }
        });
        menuFile.getItems().addAll(motifsThreeVertices);

        Menu layoutsMenu = new Menu("Layouts");

        MenuItem itemISOMLayout = new MenuItem("ISOM Layout");
        itemISOMLayout.setOnAction((event) -> {
            System.out.println("ISOM layout");
            runLayout(new LayoutISOM());
        });

        MenuItem itemCircularLayout = new MenuItem("Circular Layout");
        itemCircularLayout.setOnAction((event) -> {
            runLayout(new LayoutCircular());
        });

        MenuItem itemKKLayout = new MenuItem("KK Layout");
        itemKKLayout.setOnAction((event) -> {
            runLayout(new LayoutKK());
        });
        layoutsMenu.getItems().addAll(itemCircularLayout, itemISOMLayout, itemKKLayout);

        Menu menu2 = new Menu("File");
        MenuItem exitItem = new MenuItem("Exit", null);
        exitItem.setMnemonicParsing(true);
        exitItem.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN));
        exitItem.setOnAction((event) -> {
            Platform.exit();
        });

        /* MenuItem saveItem = new MenuItem("Save network as PNG", null);
         saveItem.setMnemonicParsing(true);
         saveItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
         saveItem.setOnAction((event) -> {
         Actions a = new Actions();
         a.saveNetworkAsImage(canvas1,transportStage );
         });*/
        menu2.getItems().add(exitItem);

        menu.getMenus().addAll(menuFile, layoutsMenu, menu2);

        return menu;

    }

    private TableView createTable(TableView table, int id) {

        table.setMaxHeight(Design.maxTableHeight);
        table.setMinWidth(Design.minTableWidth);

        TableColumn lastNameCol = new TableColumn(" Group " + id + " files names");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn actionCol = new TableColumn(" Action for group" + id);
        actionCol.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

        Callback<TableColumn<FilesToConvert, String>, TableCell<FilesToConvert, String>> cellFactory;
        cellFactory
                = new Callback<TableColumn<FilesToConvert, String>, TableCell<FilesToConvert, String>>() {
                    @Override
                    public TableCell call(final TableColumn<FilesToConvert, String> param) {
                        final TableCell<FilesToConvert, String> cell;
                        cell = new TableCell<FilesToConvert, String>() {

                            final Button btn = new Button(id + "Convert by NVG");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                } else {
                                    btn.setOnAction(event -> {

                                        FilesToConvert myFile = getTableView().getItems().get(getIndex());
                                        System.out.println("Zlozka : " + myFile.getFirstName());

                                        String fileFromTableName = myFile.firstName.get(); //name of file which was clicked in tableview

                                        if (id == 1) {
                                            for (File f : files) {
                                                String fileToDraw = f.getName();
                                                if (fileToDraw.equals(fileFromTableName)) {
                                                    System.out.println("Su zhodne a meno je " + f.getName());
                                                    displayLoading(f);
                                                }
                                            }
                                        } else {
                                            for (File f : files2) {
                                                String fileToDraw = f.getName();
                                                if (fileToDraw.equals(fileFromTableName)) {
                                                    System.out.println("Su zhodne a meno je " + f.getName());
                                                    displayLoading(f);
                                                }
                                            }
                                        }

                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };

        actionCol.setCellFactory(cellFactory);

        TableColumn<FilesToConvert, FilesToConvert> unfriendCol = new TableColumn<>(id + "Delete file");
        unfriendCol.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        unfriendCol.setCellFactory(param -> new TableCell<FilesToConvert, FilesToConvert>() {
            private final Button deleteButton = new Button("Remove from list");

            @Override
            protected void updateItem(FilesToConvert person, boolean empty) {
                super.updateItem(person, empty);

                if (person == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);

                deleteButton.setOnAction((event) -> {

                    String fileToDelete = person.firstName.getValue();

                    getTableView().getItems().remove(person);
                    if (id == 1) {
                        //TODO Delete from data and files
                    } else {

                    }
                });
            }
        });

        if (id == 1) {
            table.setItems(data);
        } else {
            table.setItems(data2);
        }

        table.getColumns().addAll(lastNameCol, actionCol, unfriendCol);

        lastNameCol.setMinWidth(Design.minTableColumnWidth);
        actionCol.setMinWidth(Design.minTableColumnWidth);
        unfriendCol.setMinWidth(Design.minTableColumnWidth);

        return table;
    }

    public static class FilesToConvert {

        private final SimpleStringProperty firstName;

        private FilesToConvert(String fileName) {
            this.firstName = new SimpleStringProperty(fileName);

        }

        public String getFirstName() {
            return firstName.get();
        }

        public void setFirstName(String fName) {
            firstName.set(fName);
        }

    }

    public void startTask(File f) {
        // Create a Runnable
        Runnable task = new Runnable() {
            public void run() {

                runTask(f);

            }
        };

        // Run the task in a background thread
        Thread backgroundThread = new Thread(task);
        // Terminate the running thread if the application exits
        backgroundThread.setDaemon(true);
        // Start the thread
        backgroundThread.start();
    }

    public void runTask(File f) {

        try {

            if (radioButtonValue == 4) {
                network = new CorrelationNetwork().createCorrelationNetwork(f, true, 0.8); //directed
            } else if (radioButtonValue == 3) {
                network = new CorrelationNetwork().createCorrelationNetwork(f, false, criticalValue); //undirected
            } else if (radioButtonValue == 2) {

            } else {
                network = new NVG().createNVGNetwork(f);
            }

            GraphicsContext gc = canvas1.getGraphicsContext2D();
            gc.setFill(Color.WHITE);
            gc.fillRect(0, 0, Design.canvasWidth, Design.canvasHeight);

            if (networkIsNull) {

            } else {
                for (Edge e : network.getEdges()) {
                    if (networkIsNull) {
                        break;
                    } else {

                        if (network.getEdgeType(e) == DIRECTED) {
                            Vertex v = network.getSource(e);
                            Vertex v2 = network.getDest(e);

                            drawDirectedEdge(gc, v.getPositionX(), v.getPositionY(), v2.getPositionX(), v2.getPositionY(), 0.1, Color.BLACK);

                        } else {
                            Pair<Vertex> p = network.getEndpoints(e);
                            Vertex v = p.getFirst();
                            Vertex v2 = p.getSecond();
                            drawEdge(gc, v.getPositionX(), v.getPositionY(), v2.getPositionX(), v2.getPositionY(), 0.1, Color.BLACK);
                            System.out.println("NEEEEEEEEEEEEEEEEEEEEEEEORIENTOVANY");
                        }

                      //  System.out.println("Hrana z uzla: " + v.toString() + "na pozicii " + v.getPositionX() + " do uzla" + p.getSecond());
                    }
                }
            }

            if (networkIsNull) {
                System.out.println("Siet je null uzly nevykreslim");

            } else {

                for (Vertex v : network.getVertices()) {

                    if (networkIsNull) {
                        break;
                    } else {
                        System.out.println("Som tu");
                    }
                    drawVertex(gc, v, Color.CORAL); //skontrolovat ci je spravne

                }

            }

            networkIsNull = true;

            // Update the Label on the JavaFx Application Thread
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    loadingStage.hide();
                    //loadingStage.close();

                }
            });

            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void runLayout(final Layout layout) {
        if (network == null) {
            return;
        }

        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                layout.runLayout(network);
                layout.fitToScrean(network);
                drawNetwork();
                return null;
            }
        };
        beginTask(task);
    }

    public void beginTask(Task task) {
        Thread tr = new Thread(task);
        tr.setDaemon(true);
        tr.start();
    }

    public void drawNetwork() {

        GraphicsContext gc = canvas1.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, Design.canvasWidth, Design.canvasHeight);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                for (Edge e : network.getEdges()) {
                    Pair<Vertex> p = network.getEndpoints(e);
                    Vertex v = p.getFirst();
                    Vertex v2 = p.getSecond();
                    drawEdge(gc, v.getPositionX(), v.getPositionY(), v2.getPositionX(), v2.getPositionY(), 0.1, Color.BLACK);
                }

                for (Vertex v : network.getVertices()) {
                    drawVertex(gc, v, Color.CHARTREUSE); //skontrolovat ci je spravne
                }

            }
        });
    }

    public void drawDirectedEdge(GraphicsContext gc, double fromX, double fromY, double toX, double toY, Color color) {
        double difX = fromX - toX;
        double difY = fromY - toY;
        double difZ = Math.sqrt(difX * difX + difY * difY);
        double shift = 10 / 2; //Vertex size
        double newX = -(difX / difZ) * (difZ - shift) + fromX;
        double newY = -(difY / difZ) * (difZ - shift) + fromY;
        drawDirectedEdge(gc, fromX, fromY,
                newX, newY, 0.1, color);
    }

    private void drawDirectedEdge(GraphicsContext gc, double fromX, double fromY, double toX, double toY, double edgeWidth, Color color) {
        double angle = Math.atan2(fromY - toY, fromX - toX);
        double tmp1 = angle + 0.17453;
        double x1 = Math.cos(tmp1) * 15 + toX;
        double y1 = Math.sin(tmp1) * 15 + toY;
        double tmp2 = angle - 0.17453;
        double x2 = Math.cos(tmp2) * 15 + toX;
        double y2 = Math.sin(tmp2) * 15 + toY;
        gc.setLineWidth(edgeWidth);
        gc.setStroke(color);
        gc.strokeLine(fromX, fromY, toX, toY);
        gc.strokeLine(toX, toY, x1, y1);
        gc.strokeLine(toX, toY, x2, y2);
    }

}
