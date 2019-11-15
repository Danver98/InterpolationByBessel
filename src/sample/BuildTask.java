package sample;

import javafx.concurrent.Task;

public class BuildTask extends Task {

    BuildTask(){

    }
    protected Integer call(){
        return 1;
    }
}
