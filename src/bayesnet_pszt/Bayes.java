package bayesnet_pszt;

import java.util.Vector;

public class Bayes {
    private static class BayesSingletonHolder {
        private final static Bayes instance = new Bayes();
    }

    public static Bayes getInstance() {
        return BayesSingletonHolder.instance;
    }

    private Bayes() {}

    private Vector<BayesNode> mNodes = new Vector<BayesNode>();

    public BayesNode CreateNode(String name, BayesNode parent) {
        BayesNode newNode = new BayesNode(name);

        if (parent != null)
            newNode.AddParent(parent);

        mNodes.add(newNode);
        return newNode;
    }

    public void Recalculate() {
        // Stage 1 - mark all as not calculated
        for (BayesNode node : mNodes)
            node.Unevaluate();

        // Stage 2 - recalculate non-evidence nodes
        for (BayesNode node : mNodes)
        {
            if (!node.IsEvidence())
            {
                node.Evaluate();
            }
        }
    }
}
