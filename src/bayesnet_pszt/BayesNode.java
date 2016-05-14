package bayesnet_pszt;

import java.util.Vector;

public class BayesNode {
    /** Our probabilities to recalculate for each node */
    public Vector<BayesAttributePair<Float>> mAttrs;

    /** Name of the node, for clarity sake */
    public String mName;

    /** True if we are an evidence node, false if we are not. */
    private boolean mIsEvidence;

    /** Parents of given node, to preserve tree structure */
    Vector<BayesNode> mParents;

    public BayesNode(String name) {
        mIsEvidence = false;
        mName = name;

        // temporarily set attributes, just to display something
        mAttrs = new Vector<BayesAttributePair<Float>>();
        BayesAttributePair<Float> temp = new BayesAttributePair<Float>();
        temp.key = "A";
        temp.value = 0.28f;
        mAttrs.add(temp);
        temp = new BayesAttributePair<Float>();
        temp.key = "B";
        temp.value = 0.06f;
        mAttrs.add(temp);
        temp = new BayesAttributePair<Float>();
        temp.key = "0";
        temp.value = 0.66f;
        mAttrs.add(temp);
    }

    public void SetEvidence(boolean isEvidence, Vector<BayesAttributePair<Float>> evidenceAttrs) {
        mIsEvidence = isEvidence;

        if (mIsEvidence) {
            mAttrs = evidenceAttrs;
        }
    }

    public boolean IsEvidence() {
        return mIsEvidence;
    }

    public void AddParent(BayesNode parent) {
        if (parent == null)
            return;

        mParents.add(parent);
    }
}
