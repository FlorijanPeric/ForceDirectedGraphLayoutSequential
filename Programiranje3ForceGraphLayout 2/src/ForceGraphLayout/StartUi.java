package ForceGraphLayout;

import javax.swing.*;
import java.awt.*;

public class StartUi extends JPanel {
    private int widthIn;
    private int heightIn;
    public StartUi(int width,int height){
        this.widthIn=width;
        this.heightIn=height;
        setPreferredSize(new Dimension(width,height));
    }


}
