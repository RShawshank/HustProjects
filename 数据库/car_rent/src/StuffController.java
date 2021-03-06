import emtity.StuffCar;
import emtity.StuffCustomer;
import emtity.StuffDiary;
import emtity.StuffPerson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class StuffController implements Initializable {
    @FXML
    Button btn_exit1;
    @FXML
    Label label_title;
    @FXML
    TextField add_cmember1,add_cmember2,add_cmember3,add_cmember4,add_cmember5;
    @FXML
    private TableColumn<StuffCar,String>stuff_carid,stuff_carbrand,stuff_carstatus,stuff_carrent,stuff_carpledge,stuff_carvalid;
    @FXML
    private TableColumn<StuffCustomer,String>stuff_cusid,stuff_cuspasswd,stuff_cusname,stuff_cusmoral,stuff_cusmember;
    @FXML
    private TableColumn<StuffPerson,String> stuffid,stuffname,stufftitle,stuffpasswd;
    @FXML
    private TableColumn<StuffDiary,String>stuff_did,stuff_dcarid,stuff_dcusid,stuff_dstuffid,stuff_dtime,stuff_devent,stuff_dcost;
    @FXML
    private TableView<StuffDiary> table_diary;
    @FXML
    private TableView<StuffCar> table_car;
    @FXML
    private TableView<StuffPerson> table_stuff;
    @FXML
    private TableView<StuffCustomer> table_customer;
    @FXML
    TextField add_carid,add_carbrand,add_carstatus,add_carrent,add_carpledge;
    @FXML
    TextField add_cmid,add_cmname,add_cmpasswd,add_cmoral,add_cmember;
    @FXML
    TextField add_smid,add_smname,add_smpasswd,add_smtitle;
    @FXML
    TextField add_did,add_dcarid,add_dcusid,add_dstuffid,add_dtime,add_devent,add_dcost;
    @FXML
    private  PieChart pieChart=new PieChart();
    ObservableList<StuffCar> StuffCarlist = FXCollections.observableArrayList();
    ObservableList<StuffCustomer>StuffCustomerlist = FXCollections.observableArrayList();
    ObservableList<StuffDiary>StuffDiarylist = FXCollections.observableArrayList();
    ObservableList<StuffPerson>StuffPersonlist=FXCollections.observableArrayList();
    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

    DBConnector connector;
    public static String stuff_id;
    public static String stuff_name;
    public static void setStuff_id(String id)
    {
        stuff_id=id;
    }
    public static void setStuff_name(String name)
    {
        stuff_name=name;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connector= DBConnector.getInstance();
        label_title.setText("?????????"+stuff_name+"?????????????????????");
        try {
            this.show_carinfo();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return;
    }
    @FXML
    //??????????????????
    public void show_carinfo() throws SQLException {
        stuff_carid.setCellValueFactory(new PropertyValueFactory<StuffCar,String>("Carid"));
        stuff_carbrand.setCellValueFactory(new PropertyValueFactory<StuffCar,String>("Carbrand"));
        stuff_carrent.setCellValueFactory(new PropertyValueFactory<StuffCar,String>("Carrent_pay"));
        stuff_carstatus.setCellValueFactory(new PropertyValueFactory<StuffCar,String>("Carstatus"));
        stuff_carpledge.setCellValueFactory(new PropertyValueFactory<StuffCar,String>("Carpledge_pay"));
        stuff_carvalid.setCellValueFactory(new PropertyValueFactory<StuffCar,String>("Carvalid"));
        //??????????????????????????????
        stuff_carstatus.setCellFactory(TextFieldTableCell.forTableColumn());
        stuff_carpledge.setCellFactory(TextFieldTableCell.forTableColumn());
        stuff_carrent.setCellFactory(TextFieldTableCell.forTableColumn());
        stuff_carrent.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<StuffCar, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<StuffCar, String> stuffCarStringCellEditEvent) {
                stuffCarStringCellEditEvent.getTableView().getItems().get(stuffCarStringCellEditEvent.getTablePosition().getRow()).setCarrent_pay(stuffCarStringCellEditEvent.getNewValue());
            }
        });
        stuff_carpledge.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<StuffCar, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<StuffCar, String> stuffCarStringCellEditEvent) {
                stuffCarStringCellEditEvent.getTableView().getItems().get(stuffCarStringCellEditEvent.getTablePosition().getRow()).setCarpledge_pay(stuffCarStringCellEditEvent.getNewValue());
            }
        });
        stuff_carstatus.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<StuffCar, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<StuffCar, String> stuffCarStringCellEditEvent) {
                stuffCarStringCellEditEvent.getTableView().getItems().get(stuffCarStringCellEditEvent.getTablePosition().getRow()).setCarstatus(stuffCarStringCellEditEvent.getNewValue());
            }
        });
        add_carid.setPromptText("??????ID");
        add_carbrand.setPromptText("????????????");
        add_carstatus.setPromptText("????????????");
        add_carrent.setPromptText("????????????");
        add_carpledge.setPromptText("????????????");
        //??????????????????
        StuffCarlist.clear();
        StuffCarlist=connector.getAllCarinfo(StuffCarlist);
        table_car.setItems(StuffCarlist);
    }
    @FXML
    public void onExitClick()
    {
        Stage nextSage=(Stage)btn_exit1.getScene().getWindow();
        nextSage.close();
        Parent root = null;
        try
        {
            root = FXMLLoader.load(getClass().getResource("login.fxml"));
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        Scene scene =new Scene(root,1000,800);
        scene.setRoot(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

    }
    @FXML
    //????????????????????????
    public void onCarDeleteClick() throws SQLException {
        StuffCar stuffCar=table_car.getSelectionModel().getSelectedItem();
        if(stuffCar==null)
        {
            JOptionPane.showMessageDialog(null,"?????????????????????????????????");
            return;
        }
        connector.deleteReturnCar(stuffCar.getCarid(),stuffCar.getCarvalid());
        this.show_carinfo();//??????
    }
    @FXML
    //????????????????????????
    public void onCarAddClick() throws SQLException {
        String addcarid=add_carid.getText();
        String addcarbrand=add_carbrand.getText();
        String addcarstatu=add_carstatus.getText();
        String addcarrent=add_carrent.getText();
        String addcarpledge=add_carpledge.getText();
        String addcarvalid="0";
        if(addcarid.isEmpty()||addcarbrand.isEmpty()||addcarpledge.isEmpty()||addcarrent.isEmpty()||addcarstatu.isEmpty())
        {
            JOptionPane.showMessageDialog(null,"????????????????????????");
            return;
        }
        connector.addCarinfo(addcarid,addcarbrand,addcarstatu,addcarrent,addcarpledge,addcarvalid);
        System.out.println(addcarid);
        System.out.println(addcarbrand);
        System.out.println(addcarpledge);
        add_carid.clear();
        add_carbrand.clear();
        add_carpledge.clear();
        add_carrent.clear();
        add_carstatus.clear();
        this.show_carinfo();
    }
    //??????????????????
    @FXML
    public void show_customerinfo() throws SQLException {
        stuff_cusid.setCellValueFactory(new PropertyValueFactory<StuffCustomer,String>("Cusid"));
        stuff_cusname.setCellValueFactory(new PropertyValueFactory<StuffCustomer,String>("Cusname"));
        stuff_cuspasswd.setCellValueFactory(new PropertyValueFactory<StuffCustomer,String>("Cuspasswd"));
        stuff_cusmoral.setCellValueFactory(new PropertyValueFactory<StuffCustomer,String>("Cusmoral"));
        stuff_cusmember.setCellValueFactory(new PropertyValueFactory<StuffCustomer,String>("Cusmember"));
        stuff_cusname.setCellFactory(TextFieldTableCell.forTableColumn());
        stuff_cusmember.setCellFactory(TextFieldTableCell.forTableColumn());
        stuff_cuspasswd.setCellFactory(TextFieldTableCell.forTableColumn());
        stuff_cusmember.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<StuffCustomer, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<StuffCustomer, String> stuffCustomerStringCellEditEvent) {
                stuffCustomerStringCellEditEvent.getTableView().getItems().get(stuffCustomerStringCellEditEvent.getTablePosition().getRow()).setCusmember(stuffCustomerStringCellEditEvent.getNewValue());
            }
        });
        stuff_cusname.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<StuffCustomer, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<StuffCustomer, String> stuffCustomerStringCellEditEvent) {
                stuffCustomerStringCellEditEvent.getTableView().getItems().get(stuffCustomerStringCellEditEvent.getTablePosition().getRow()).setCusname(stuffCustomerStringCellEditEvent.getNewValue());

            }
        });
        stuff_cuspasswd.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<StuffCustomer, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<StuffCustomer, String> stuffCustomerStringCellEditEvent) {
                stuffCustomerStringCellEditEvent.getTableView().getItems().get(stuffCustomerStringCellEditEvent.getTablePosition().getRow()).setCuspasswd(stuffCustomerStringCellEditEvent.getNewValue());

            }
        });
        add_cmid.setPromptText("??????ID");
        add_cmname.setPromptText("????????????");
        add_cmpasswd.setPromptText("????????????");
        add_cmember.setPromptText("????????????");
        add_cmoral.setPromptText("????????????");
        StuffCustomerlist.clear();
        StuffCustomerlist=connector.getAllcustomerinfo();
        table_customer.setItems(StuffCustomerlist);
    }
    @FXML
    //??????????????????
    public void  onCustomerDeleteClick() throws SQLException {
        StuffCustomer stuffCustomer = table_customer.getSelectionModel().getSelectedItem();
        if(stuffCustomer==null)
        {
            JOptionPane.showMessageDialog(null,"?????????????????????????????????");
            return;
        }
        connector.deleteCusotomerinfo(stuffCustomer.getCusid());
        this.show_customerinfo();
    }
    //??????????????????
    public void onCustomerAddClick() throws SQLException {
        String addcmid=add_cmid.getText();
        String addcmname=add_cmname.getText();
        String addcmpasswd=add_cmpasswd.getText();
        String addmember =add_cmember.getText();
        if(addcmid.isEmpty()||addcmname.isEmpty()||addcmpasswd.isEmpty())
        {
            JOptionPane.showMessageDialog(null,"????????????????????????");
            return;
        }
        connector.addCustomerinfo(addcmid,addcmname,addmember,addcmpasswd);
        this.show_customerinfo();
    }
    @FXML
    //??????????????????
    public void show_stuffinfo() throws SQLException {
        stuffid.setCellValueFactory(new PropertyValueFactory<StuffPerson,String>("Stuffid"));
        stuffname.setCellValueFactory(new PropertyValueFactory<StuffPerson,String>("Stuffname"));
        stufftitle.setCellValueFactory(new PropertyValueFactory<StuffPerson,String>("Stufftitle"));
        stuffpasswd.setCellValueFactory(new PropertyValueFactory<StuffPerson,String>("Stuffpasswd"));
        add_smid.setPromptText("??????ID");
        add_smname.setPromptText("????????????");
        add_smtitle.setPromptText("????????????");
        add_smpasswd.setPromptText("????????????");
        StuffPersonlist.clear();
        StuffPersonlist=connector.getAllstuffinfo(StuffPersonlist);
        table_stuff.setItems(StuffPersonlist);
    }
    @FXML
    public void onStuffDeleteClick() throws SQLException {
        StuffPerson stuffPerson = table_stuff.getSelectionModel().getSelectedItem();
        if(stuffPerson==null)
        {
            JOptionPane.showMessageDialog(null,"?????????????????????????????????");
            return;
        }
        connector.deleteStuffinfo(stuffPerson.getStuffid());
        this.show_stuffinfo();
    }
    @FXML
    public void onStuffAddClick() throws SQLException {
        String stuffid=add_smid.getText();
        String stuffname=add_smname.getText();
        String stufftitle=add_smtitle.getText();
        String stuffpasswd=add_smpasswd.getText();
        if(stuffid.isEmpty()||stuffname.isEmpty()||stufftitle.isEmpty()||stuffpasswd.isEmpty())
        {
            JOptionPane.showMessageDialog(null,"????????????????????????");
            return;
        }
        connector.addStuffinfo(stuffid,stuffname,stufftitle,stuffpasswd);
        add_smid.clear();
        add_smname.clear();
        add_smpasswd.clear();
        add_smtitle.clear();
    }
    @FXML
    public void show_diaryinfo() throws SQLException {
        stuff_did.setCellValueFactory(new PropertyValueFactory<StuffDiary,String>("Did"));
        stuff_dcarid.setCellValueFactory(new PropertyValueFactory<StuffDiary,String>("Dcarid"));
        stuff_dcost.setCellValueFactory(new PropertyValueFactory<StuffDiary,String>("Dcost"));
        stuff_dcusid.setCellValueFactory(new PropertyValueFactory<StuffDiary,String>("Dcusid"));
        stuff_devent.setCellValueFactory(new PropertyValueFactory<StuffDiary,String>("Devent"));
        stuff_dstuffid.setCellValueFactory(new PropertyValueFactory<StuffDiary,String>("Dstuffid"));
        stuff_dtime.setCellValueFactory(new PropertyValueFactory<StuffDiary,String>("Dtime"));
        StuffDiarylist.clear();
        StuffDiarylist=connector.getAlldirayinfo(StuffDiarylist);
        table_diary.setItems(StuffDiarylist);
//        add_did.setPromptText("??????ID");
  //      add_dcarid.setPromptText("??????ID");
     //   add_dcusid.setPromptText("??????ID");
       // add_dstuffid.setPromptText("??????ID");
        //add_dtime.setPromptText("??????");
        //add_devent.setPromptText("????????????????????????");
        //add_dcost.setPromptText("????????????");
    }
    @FXML
    public void onDiaryDeleteClick() throws SQLException {
        StuffDiary stuffDiary = table_diary.getSelectionModel().getSelectedItem();
        if(stuffDiary==null)
        {
            JOptionPane.showMessageDialog(null,"?????????????????????????????????");
            return;
        }
        connector.deleteDiaryinfo(stuffDiary.getDid(),stuffDiary.getDevent());
        this.show_diaryinfo();
    }
    @FXML
    public void onDiaryAddClick() throws SQLException {
        String did=add_did.getText();
        String dcarid=add_dcarid.getText();
        String dcusid=add_dcusid.getText();
        String dstuffid=add_dstuffid.getText();
        String dtime=add_dtime.getText();
        String devent=add_devent.getText();
        String dcost=add_dcost.getText();
        if(did.isEmpty()||dcarid.isEmpty()||dcusid.isEmpty()||dstuffid.isEmpty()||dtime.isEmpty()||devent.isEmpty()||dcost.isEmpty()) {

            JOptionPane.showMessageDialog(null, "????????????????????????");
            return;
        }
        connector.addDiaryinfo(did,dcarid,dcusid,dstuffid,dtime,devent,dcost);
        this.show_diaryinfo();
        add_dcarid.clear();
        add_dcost.clear();
        add_dcusid.clear();
        add_devent.clear();
        add_did.clear();
        add_dstuffid.clear();
        add_dtime.clear();
    }
    @FXML
    public void show_tableinfo() throws SQLException {
        pieChartData.clear();
        pieChart.getData().clear();
        pieChartData = connector.getprofitinfo();
        pieChart.setData(pieChartData);
        pieChart.setTitle("???????????????????????????");
        pieChart.setClockwise(true);
        pieChart.setLabelLineLength(50);
        pieChart.setLabelsVisible(true);
        pieChart.setStartAngle(180);
        final Label caption = new Label("");
        caption.setStyle("-fx-font: 24 arial;");
        for (final PieChart.Data data : pieChart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                    e -> {
                        double total = 0;
                        for (PieChart.Data d : pieChart.getData()) {
                            total += d.getPieValue();
                        }
                        caption.setTranslateX(e.getSceneX());
                        caption.setTranslateY(e.getSceneY());
                        String text = String.format("%.1f%%", 100 * data.getPieValue() / total);
                        caption.setText(text);
                        pieChart.setAccessibleText(text);
                    }
            );
        }
    }
    @FXML
    public void get_year() throws SQLException {
        if(pieChart!=null)
            pieChart.getData().clear();
        pieChartData.clear();
        pieChartData=connector.getprofit_year(pieChartData);
        pieChart.setData(pieChartData);
        pieChart.setTitle("???????????????");
        add_cmember1.setText(String.valueOf(connector.getprofitall()));
        pieChart.setClockwise(true);
        pieChart.setLabelLineLength(50);
        pieChart.setLabelsVisible(true);
        pieChart.setStartAngle(180);
    }
    @FXML
    public void get_season() throws SQLException {
        int []num=new int[4];
        num=connector.getprofit_season(num);
        if(pieChart!=null)
        pieChart.getData().clear();
        pieChartData.add(new PieChart.Data("????????????",num[0]));
        add_cmember2.setText(String.valueOf(num[0]));
        pieChartData.add(new PieChart.Data("????????????",num[1]));
        add_cmember3.setText(String.valueOf(num[1]));
        pieChartData.add(new PieChart.Data("????????????",num[2]));
        add_cmember4.setText(String.valueOf(num[2]));
        pieChartData.add(new PieChart.Data("????????????",num[3]));
        add_cmember5.setText(String.valueOf(num[3]));
        int i=0;
        for (PieChart.Data data : pieChartData) {
            data.getNode().setStyle(
                    "-fx-pie-color: #ffd700+i;"
            );
            i++;
        }
            pieChart.setData(pieChartData);
        pieChart.setTitle("?????????????????????");
        pieChart.setClockwise(true);
        pieChart.setLabelLineLength(50);
        pieChart.setLabelsVisible(true);
        pieChart.setStartAngle(180);

    }
}
