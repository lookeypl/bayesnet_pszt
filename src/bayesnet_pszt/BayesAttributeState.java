package bayesnet_pszt;

public class BayesAttributeState {
    public BayesNode node;
    public int curAttr;

    public BayesAttributeState(BayesNode node, int curAttr) {
        this.node = node;
        this.curAttr = curAttr;
    }
}
