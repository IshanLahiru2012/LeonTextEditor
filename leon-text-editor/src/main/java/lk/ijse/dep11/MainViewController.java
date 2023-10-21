package lk.ijse.dep11;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.util.Optional;

public class MainViewController {
    public MenuItem MenuItemNew;
    public AnchorPane root;
    public MenuItem MenuItemOpen;
    public MenuItem MenuItemSave;
    public MenuItem MenuItemSaveAs;
    public MenuItem MenuItemPrint;
    public MenuItem MenuItemExit;
    public MenuItem MenuItemCut;
    public MenuItem MenuItemCopy;
    public MenuItem MenuItemPaste;
    public MenuItem MenuItemSelectAll;
    public MenuItem MenuItemUserGuide;
    public MenuItem MenuItemAboutUs;
    public javafx.scene.web.HTMLEditor HTMLEditor;

    public void initialize(){


    }
    public void initData(int index){
        fileIndex=index;
    }

    public void MenuItemNewOnAction(ActionEvent actionEvent) {

        System.out.println(fileIndex);
        Platform.runLater(()->{
            try {
                FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/view/MainView.fxml"));
                AnchorPane newRoot = mainLoader.load();

                Scene newScene = new Scene(newRoot);
                Stage newStage = new Stage();
                newStage.setScene(newScene);
                newStage.setTitle(getFileName());
                newStage.centerOnScreen();
                newStage.show();

                MainViewController mainViewController = mainLoader.getController();
                mainViewController.initData(fileIndex);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
    private int fileIndex;
    protected String getFileName(){

        String userName = System.getProperty("user.name");
        String pathName = String.format("/home/%s/Documents/Leon_Text_Editor",userName);
        File dircory = new File(pathName);
        if(!dircory.exists()){
            boolean created = dircory.mkdirs();
            if(created) System.out.println("created at " + pathName);
        }
        File[] files = dircory.listFiles();
        for (File file1 : files) {
            if((file1.getName().contains(".txt")|| file1.getName().contains(".html")) && file1.getName().startsWith("LTEditor-")){
                int check = Integer.parseInt(file1.getName().substring(9,file1.getName().indexOf(".")));
                if(check >fileIndex){
                    fileIndex=check;
                }
            }
        }
        String name = String.format("LTEditor-%d",++fileIndex);

        return name;
    }

    public void MenuItemOpenOnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Text File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text File","*.txt","*.html"));
        File openFile = fileChooser.showOpenDialog(root.getScene().getWindow());
        if(openFile== null)return;

        Platform.runLater(()->{
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/MainView.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                Scene mainScene = new Scene(anchorPane);
                Stage stage;

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Open Text");
                alert.setHeaderText("This is for confirm to Open window");
                alert.setContentText("would you like to open in new window");

                Optional<ButtonType> checkWindow = alert.showAndWait();
                if(checkWindow.get()== ButtonType.OK){
                    stage = new Stage();
                }else{
                    stage = (Stage) root.getScene().getWindow();
                }
                stage.setScene(mainScene);
                stage.setTitle(openFile.getName().substring(0,openFile.getName().indexOf('.')));
                stage.centerOnScreen();
                stage.show();

                FileReader fir = new FileReader(openFile);
                BufferedReader bir = new BufferedReader(fir);
                String read;

                String text="";
                while ((read = bir.readLine()) != null){
                    System.out.println("2");
                    text+=read;
                }
                MainViewController mainViewController = fxmlLoader.getController();
                mainViewController.HTMLEditor.setHtmlText(text);

            }catch (IOException e){
                e.printStackTrace();
            }

        });

    }

    public void MenuItemSaveOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage)root.getScene().getWindow();
        File fileList = new File(String.format("/home/%s/Documents/Leon_Text_Editor",System.getProperty("user.name")));
        File[] files = fileList.listFiles();
        String fileFormat= ".html";
        for (File file : files) {
            if((file.getName().contains(".txt")|| file.getName().contains(".html")) &&
                    file.getName().substring(0,file.getName().indexOf('.')).equals(stage.getTitle())){
                fileFormat= file.getName().substring(file.getName().indexOf('.'));
                break;
            }
        }
        String fileName = String.format("/home/%s/Documents/Leon_Text_Editor/%s%s",System.getProperty("user.name"),stage.getTitle(),fileFormat);
        File file = new File(fileName);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try(FileWriter fwr = new FileWriter(file);
            BufferedWriter bwr = new BufferedWriter(fwr)) {

//            Document document = Jsoup.parse(HTMLEditor.getHtmlText());
//            String textContent = document.text();
//            bwr.write(textContent);
            bwr.write(HTMLEditor.getHtmlText());

        }catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void MenuItemSaveAsOnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as Text File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text File (*.txt)","*.txt"),
                new FileChooser.ExtensionFilter("HTML File (*.HTML,*.html)","*.html"));
        File initialDirectory = new File(String.format("/home/%s/Documents/Leon_Text_Editor", System.getProperty("user.name")));

        if(!initialDirectory.exists())initialDirectory.mkdirs();
        fileChooser.setInitialDirectory(initialDirectory);
        fileChooser.setInitialFileName(getFileName());

        File file = fileChooser.showSaveDialog(root.getScene().getWindow());
        if(file == null)return;
        String selectedFormat = fileChooser.getSelectedExtensionFilter().getExtensions().get(0).substring(1);
        selectedFormat = file.getAbsolutePath().endsWith(selectedFormat) ? "" : selectedFormat;
        File file1 = new File(file.getAbsolutePath() + selectedFormat);


        try(FileWriter fileWriter = new FileWriter(file1);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)){
            bufferedWriter.write(HTMLEditor.getHtmlText());
        } catch (IOException e) {
            System.out.println("unable to save file");;
        }
    }

    public void MenuItemPrintOnAction(ActionEvent actionEvent) {
    }

    public void MenuItemExitOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    protected String textStor="";
    public void MenuItemCutOnACtion(ActionEvent actionEvent) {
        System.out.println("cut");
        

    }

    public void MenuItemCopyOnAction(ActionEvent actionEvent) {
//        WebView webView = (WebView) HTMLEditor.lookup(".web-view");
//        WebEngine webEngine = webView.getEngine();

        WebEngine webEngine = ((WebView)HTMLEditor.lookup(".web-view")).getEngine();
        // Get the selected text using JavaScript
        String selectedText = (String) webEngine.executeScript("window.getSelection().toString()");
        
        // Copy the selected text to the clipboard
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(selectedText);
        clipboard.setContent(content);

    }
    public void MenuItemPasteOnAction(ActionEvent actionEvent) {




    }

    public void MenuItemSelectAllOnAction(ActionEvent actionEvent) {
    }

    public void MenuItemUserGuideOnAction(ActionEvent actionEvent) {
    }

    public void MenuItemAboutUsOnAction(ActionEvent actionEvent) {
    }
}
