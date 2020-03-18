package com.company;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static java.lang.Thread.sleep;


/**
 * Class representing model
 */
public class App extends Application {
    private Client client;
    Button fold;
    Button call;
    Button bet;
    Button wait;
    Model state;
    Stage window;
    Label stack;
    Label[] bankrolls = new Label[10];
    Label[] current = new Label[10];
    Label[] color1 = new Label[10];
    Label[] color2 = new Label[10];
    Label[] card1 = new Label[10];
    Label[] card2 = new Label[10];
    Label[] color = new Label[5];
    Label[] card = new Label[5];
    Label komunikat = new Label("THIS IS YOU!");
    Label whoseturn = new Label("MY TURN");
    int tmp =0;
    private int mynum;
    private IntegerProperty alert = new SimpleIntegerProperty();
    public void setLabels(){
    }
    public void setTexts(){
    }

    public static void main(String[] args) {
        launch(args);
    }
    public void start(Stage primaryStage) throws Exception {
        alert.setValue(0);
        window =primaryStage;
        client = new Client(answer -> {
            Platform.runLater(() -> {
                state = (Model) answer;
                alert.setValue(alert.getValue()+1);
                mynum = state.getMynum();
            });
        });
        client.StartClient();
        primaryStage.setTitle("Pokerek");
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10,10,10,10));
        grid.setVgap(16);
        grid.setHgap(20);
        setLabels();
        for( int i =0 ; i < 9 ;i+=4){
            for( int j =0; j < 8; j+=3){
                if(i==4 && j==3)
                    continue;
                card1[tmp]=new Label("Waiting");
                card2[tmp]=new Label("");
                color1[tmp]=new Label("");
                color2[tmp]= new Label("");
                bankrolls[tmp]=new Label("");
                current[tmp]=new Label("");
                GridPane.setConstraints(card1[tmp],i,j);
                GridPane.setConstraints(card2[tmp],i,j+1);
                GridPane.setConstraints(color1[tmp],i+1,j);
                GridPane.setConstraints(color2[tmp],i+1,j+1);
                GridPane.setConstraints(bankrolls[tmp],i+2,j);
                GridPane.setConstraints(current[tmp],i+2,j+1);
                tmp++;
            }
        }
        for(int i =0 ; i<5 ; i++){
            card[i] = new Label("");
            color[i] = new Label("");
            GridPane.setConstraints(card[i],5+i,3);
            GridPane.setConstraints(color[i],5+i,4);
        }
        alert.addListener(new ChangeListener(){
            public void changed(ObservableValue o, Object oldval, Object newval){
                mynum=client.Getmynumber();
                tmp=0;
                for( int i =0 ; i < 9 ;i+=4){
                    for( int j =0; j < 8; j+=3){
                        if(i==4 && j==3)
                            continue;
                        if(tmp == mynum){
                            GridPane.setConstraints(komunikat,i+3,j);
                        }
                        if(tmp == state.GetTurn())
                            GridPane.setConstraints(whoseturn,i+3,j+1);
                        if(state.GetActive(tmp) == 0){
                            Label inactive = new Label("I folded");
                            GridPane.setConstraints(inactive,i+3,j+1);
                            grid.getChildren().add(inactive);
                        }
                        tmp++;
                    }
                }
                for(int i =0 ; i< 8 ;i++){
                    bankrolls[i].setText(String.valueOf(state.GetBankroll(i)));
                    card1[i].setText(String.valueOf(state.GetCard1(i).number()));
                    color1[i].setText(String.valueOf(state.GetCard1(i).color()));
                    card2[i].setText(String.valueOf(state.GetCard2(i).number()));
                    color2[i].setText(String.valueOf(state.GetCard2(i).color()));
                    current[i].setText(String.valueOf(state.GetCurrent(i)));
                }
                if(state.getPhase()==0){
                    for(int i=0;i<5;i++) {
                        color[i].setText("");
                        card[i].setText("");
                    }
                }
                else {
                    for (int i = 0; i < state.getPhase() + 2; i++) {
                        color[i].setText(String.valueOf(state.GetTable(i).color()));
                        card[i].setText(String.valueOf(state.GetTable(i).number()));
                    }
                    for (int i = state.getPhase() + 2; i < 5; i++) {
                        color[i].setText("");
                        card[i].setText("");
                    }
                }
            }
        });
        setTexts();
        //buttons
        TextField betInput = new TextField();
        fold = new Button("FOLD");
        GridPane.setConstraints(fold,4,5);
        call = new Button("CALL");
        GridPane.setConstraints(call,5,5);
        bet = new Button("BET");
        wait = new Button("WAIT");
        GridPane.setConstraints(wait,7,5);
        fold.setOnAction(e-> client.SetAnswer(0, state.GetTurn())); // not everything should be acceptable
        call.setOnAction(e-> client.SetAnswer(1, state.GetTurn())); // not everything should be acceptable
        bet.setOnAction(e-> {
            int w = Integer.parseInt(betInput.getText());
            client.SetAnswer( w, state.GetTurn());
        }); // not everything should be acceptable
        wait.setOnAction(e-> client.SetAnswer(3, state.GetTurn())); // not everything should be acceptable
        VBox  betBox= new VBox();
        betBox.getChildren().addAll(betInput,bet);
        GridPane.setConstraints(betBox,6,5);
grid.getChildren().addAll(fold,call, betBox, wait,komunikat,whoseturn);
for( int i =0 ;i<8;i++){
    grid.getChildren().add(bankrolls[i]);
    grid.getChildren().add(card1[i]);
    grid.getChildren().add(card2[i]);
    grid.getChildren().add(color1[i]);
    grid.getChildren().add(color2[i]);
    grid.getChildren().add(current[i]);
}
for(int i=0;i<5;i++){
    grid.getChildren().add(card[i]);
    grid.getChildren().add(color[i]);

}
Scene scene = new Scene(grid,900,700);
window.setScene(scene);
        //
        window.show();
    }
}
