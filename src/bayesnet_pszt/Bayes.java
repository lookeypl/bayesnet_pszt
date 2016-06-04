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
        for (BayesNode node : mNodes)
        {
            // TODO TEMPORARY!!!
            if (node.HasParents() && !node.HasChildren())
            {
                // this is our child node, calculate its attributes
                for (int i = 0; i < node.GetAttributeCount(); ++i)
                {
                    float attributeValue = 0.0f;
                    BayesNode parentM = node.GetParent(0);
                    BayesNode parentF = node.GetParent(1);
                    int parentMAttrCount = node.GetParent(0).GetAttributeCount();
                    int iteratorM = 0, iteratorF = 0;
                    for (int j = 0; j < node.GetCombinationCount(); ++j)
                    {
                        if (iteratorM == parentMAttrCount)
                        {
                            iteratorF++;
                            iteratorM = 0;
                        }

                        // Calculate the probability by using Joint Probability Function,
                        // and add it to overall attribute value.
                        attributeValue += parentM.GetAttribute(iteratorM).value *
                                          parentF.GetAttribute(iteratorF).value *
                                          node.GetProbability(i, j);
                        iteratorM++;
                    }
                    node.SetAttributeValue(i, attributeValue);
                }
            }
        }
    }
}
