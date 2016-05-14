package bayesnet_pszt;

public class Bayes {
    private static class BayesSingletonHolder {
        private final static Bayes instance = new Bayes();
    }

    public static Bayes getInstance() {
        return BayesSingletonHolder.instance;
    }

    private Bayes() {}

    public BayesNode CreateNode(String name, BayesNode parent) {
        BayesNode newNode = new BayesNode(name);

        if (parent != null)
            newNode.AddParent(parent);

        return newNode;
    }

    public void Recalculate() {
        // TODO gather evidence nodes here, then do forward/backward evaluation
    }
}
