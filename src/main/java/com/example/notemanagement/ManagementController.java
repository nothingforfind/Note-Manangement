package com.example.notemanagement;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ManagementController implements Initializable {

    public ListView lvNote;
    public TextArea taContext;
    public TextField tfTitle;
    public TextField tfDate;
    static List<Note> noteList = new ArrayList<>();
    public Button btnNew;
    public Button btnEdit;
    public Button btnUpdate;
    public Button btnRemove;

    /**
     * First, set title for List View
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        /*Use title of note for title's name*/
        setCellFactory();

        /*Restore data by file data.txt*/
        try {
            restoreData();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Create new Note and add to List, List View
     */
    @FXML
    void newNote() {

        /*Create new note, add to List and List View*/
        Note note = new Note("New Note", "");
        noteList.add(note);
        lvNote.getItems().clear();
        lvNote.getItems().addAll(noteList);

        /*When create new note, selected item always choose that new note*/
        lvNote.getSelectionModel().select(note);

        /*Default text of new note is show*/
        tfTitle.setText(note.getTitle());
        tfDate.setText(note.getDate());
        taContext.setText("");

        /*Text field is editable*/
        setAllEditable();

        /*Button New, Update, Remove enable*/
        setAllDisable();
        btnUpdate.setDisable(false);
        btnRemove.setDisable(false);
    }


    /**
     * Save Note to List, List View
     */
    @FXML
    void saveNote() {

        /*When title text field and context text field not null*/
        if (!tfTitle.getText().isEmpty() && !taContext.getText().isEmpty()) {

            /*Create new node and add to List*/
            noteList.add(new Note(tfTitle.getText(), taContext.getText()));

            /*Clear List View and add all data by List*/
            lvNote.getItems().clear();
            lvNote.getItems().addAll(noteList);

            /*When over, set all text field to emty*/
            setDefault();
        }

        /*Otherwise, alert is show*/
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cannot null!");
            alert.setContentText("Title, date, context cannot null.");
            alert.show();
        }

        /*Text field from now cannot editable*/
        setAllNotEditable();

        /*Only button New enable*/
        setAllDisable();
    }


    /**
     * Remove Note from List, List View
     */
    @FXML
    void removeNote() {

        /*Remove item from List & List View*/
        noteList.remove(lvNote.getSelectionModel().getSelectedIndex());
        lvNote.getItems().remove(lvNote.getSelectionModel().getSelectedIndex());

        /*Set all text field to emty*/
        setDefault();

        /*Text field from now cannot editable*/
        setAllNotEditable();

        /*Only button New enable*/
        setAllDisable();
    }


    /**
     * When select item from List View, get data and set text
     * @param arg0
     */
    @FXML
    public void selectedItemClick(MouseEvent arg0) {

        /*Get selected item*/
        if (lvNote.getSelectionModel().getSelectedItem() != null) {
            Note note = (Note) lvNote.getSelectionModel().getSelectedItem();

            /*Text fields is update text by selected item*/
            tfTitle.setText(note.getTitle());
            tfDate.setText(note.getDate());
            taContext.setText(note.getContext());

            /*All text field set not editable*/
            setAllNotEditable();

            /*Button New, Edit and Delete are show*/
            setAllDisable();
            btnEdit.setDisable(false);
            btnRemove.setDisable(false);
        }

        /*Otherwise, alert is show*/
        else {
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setTitle("Not fount item!");
            alert.setContentText("Item is not found. Try again, please!");
            alert.getButtonTypes().add(ButtonType.CLOSE);
            alert.show();
        }
    }


    /**
     * Edit Note
     */
    @FXML
    void editNote() {

        /*All text field are editable*/
        setAllEditable();

        /*Button New, Update, Remove is show*/
        setAllDisable();
        btnNew.setDisable(false);
        btnUpdate.setDisable(false);
        btnRemove.setDisable(false);
    }


    /**
     * With new data from text field, update data in List, List View
     */
    @FXML
    void updateNote() {

        /*Remove selected item*/
        noteList.remove(lvNote.getSelectionModel().getSelectedIndex());
        lvNote.getItems().remove(lvNote.getSelectionModel().getSelectedIndex());

        /*Create new data from text field text*/
        saveNote();
    }


    /**
     * Use title of note for title's name
     */
    void setCellFactory() {

        /*Set cell factory by function of List View*/
        lvNote.setCellFactory(param -> new ListCell<Note>() {
            @Override
            protected void updateItem(Note item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getTitle() == null) {
                    setText(null);
                } else {
                    setText(item.getTitle());
                }
            }
        });
    }


    /**
     * Set all text field and text area to emty
     */
    void setDefault() {
        tfTitle.setText("");
        tfDate.setText("");
        taContext.setText("");
    }


    /**
     * Only button New enable
     */
    void setAllDisable() {
        btnEdit.setDisable(true);
        btnUpdate.setDisable(true);
        btnRemove.setDisable(true);
    }


    /**
     * Set title text field and context text field not editable
     */
    void setAllNotEditable() {
        tfTitle.setEditable(false);
        taContext.setEditable(false);
    }


    /**
     * Set title text field and context text field editable
     */
    void setAllEditable() {
        tfTitle.setEditable(true);
        taContext.setEditable(true);
    }


    /**
     * Save data when exit application
     * @throws IOException
     */
    @FXML
    void saveData() throws IOException {

        /*Write file by BufferedWriter*/
        FileWriter fw = new FileWriter("data.txt");
        BufferedWriter bw = new BufferedWriter(fw);


        /*By foreach, read step by step any note*/
        for (Note note: noteList) {

            /*Keyword create model file*/
            bw.write("~~title~~\n");
            bw.write(note.getTitle() + "\n");
            bw.write("~~date~~\n");
            bw.write(note.getDate() + "\n");
            bw.write("~~context~~\n");
            bw.write(note.getContext() + "\n");
            bw.flush();
        }

        /*Close Buffered and FileWriter, exit the application*/
        bw.close();
        fw.close();
        Platform.exit();
        System.exit(0);
    }


    /**
     * From data.txt, create Note add to List, List View
     * @throws IOException
     */
    @FXML
    void restoreData() throws IOException {

        /*Read file from BufferedReader*/
        FileReader fr = new FileReader("data.txt");
        BufferedReader br = new BufferedReader(fr);

        /*Read line and check it*/
        String i = br.readLine();
        int num = 0;
        Note note = new Note();

        /*If i isn't end line, read it*/
        while (i != null) {

            /*Check by key word*/
            if (i.matches("~~title~~")) {
                if (note.getContext() != null) {
                    noteList.add(note);
                    note = new Note();
                }
                num = 1;
                i = br.readLine();
            }

            /*Num is 1, we will add line to Title*/
            if (num == 1) {

                /*Title is 2-3 line, we append line to line*/
                if (note.getTitle() != null) {
                    note.setTitle(note.getTitle() + "\n" + i);
                    i = br.readLine();
                }

                /*Title only 1 line, set Title by that line*/
                else {
                    note.setTitle(i);
                    i = br.readLine();
                }
            }

            /*Check by key word*/
            if (i.matches("~~date~~")) {
                num = 2;
                i = br.readLine();
            }

            /*Num is 2, we will add line to Date*/
            if (num == 2) {

                /*Date is 2-3 line, we append line to line*/
                if (note.getDate() != null) {
                    note.setDate(note.getDate() + "\n" + i);
                    i = br.readLine();
                }

                /*Date only 1 line, set Date by that line*/
                else {
                    note.setDate(i);
                    i = br.readLine();
                }
            }

            /*Check by key word*/
            if (i.matches("~~context~~")) {
                num = 3;
                i = br.readLine();
            }

            /*Num is 3, we will add line to Context*/
            if (num == 3) {

                /*Context is 2-3 line, we append line to line*/
                if (note.getContext() != null) {
                    note.setContext(note.getContext() + "\n" + i);
                    i = br.readLine();
                }

                /*Context only 1 line, set Context by that line*/
                else {
                    note.setContext(i);
                    i = br.readLine();
                }
            }
        }

        /*Last note cannot add in loop*/
        noteList.add(note);

        /*Clear List View and add all data by List*/
        lvNote.getItems().clear();
        lvNote.getItems().addAll(noteList);

        /*Close read file*/
        br.close();
        fr.close();
    }


    /**
     * Show information by alert
     */
    @FXML
    void about() {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Contact us!");
        alert.setContentText("This application created by Le Hoang Long");
        alert.getButtonTypes().add(ButtonType.CLOSE);
        alert.show();
    }
}