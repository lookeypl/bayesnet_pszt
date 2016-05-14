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

    /** Children of given node, for tree structure */
    Vector<BayesNode> mChildren;

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

        if (parent == this)
            return;

        if (mParents.contains(parent))
            return;

        mParents.add(parent);
        parent.AddChild(this);
    }

    public void AddChild(BayesNode child) {
        if (child == null)
            return;

        if (child == this)
            return;

        if (mChildren.contains(child))
            return;

        mChildren.add(child);
        child.AddParent(this);
    }

    public void RemoveParent(BayesNode parent) {
        if (parent == null)
            return;

        if (parent == this)
            return;

        if (!mParents.contains(parent))
            return;

        mParents.remove(parent);
        parent.RemoveChild(this);
    }

    public void RemoveChild(BayesNode child) {
        if (child == null)
            return;

        if (child == this)
            return;

        if (!mChildren.contains(child))
            return;

        mParents.remove(child);
        child.RemoveParent(this);
    }

    public int GetParamCount() {
        return mAttrs.size();
    }
}
