package colorlines.color_lines;
import android.content.Context;
import android.util.AttributeSet;

public class GameButton extends  android.support.v7.widget.AppCompatButton {
    private GameButton topNeighbor;
    private GameButton bottomNeighbor;
    private GameButton leftNeighbor;
    private int btnId;

    public int getBtnId() {
        return btnId;
    }

    public void setBtnId(int id) {
        this.btnId = id;
    }



    public GameButton(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
    }

    public GameButton getTopNeighbor() {
        return topNeighbor;
    }

    public void setTopNeighbor(GameButton topNeighbor) {
        this.topNeighbor = topNeighbor;
    }

    public GameButton getBottomNeighbor() {
        return bottomNeighbor;
    }

    public void setBottomNeighbor(GameButton bottomNeighbor) {
        this.bottomNeighbor = bottomNeighbor;
    }

    public GameButton getLeftNeighbor() {
        return leftNeighbor;
    }

    public void setLeftNeighbor(GameButton leftNeighbor) {
        this.leftNeighbor = leftNeighbor;
    }

    public GameButton getRightNeighbor() {
        return rightNeighbor;
    }

    public void setRightNeighbor(GameButton rightNeighbor) {
        this.rightNeighbor = rightNeighbor;
    }

    private GameButton rightNeighbor;

    public GameButton(Context context) {
        super(context);
    }

}
