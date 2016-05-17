package bayesnet_pszt;

import java.util.Vector;

public class BayesNode {
    /** Our probabilities to recalculate for each node */
    public Vector<BayesAttributePair<Float>> mAttrs = null;

    /** Probability matrix, used for calculations */
    public Vector<Float> mProbMatrix = new Vector<Float>();
    private int mAttributeCount = 0;
    private int mCombinationCount = 0;
    private final float UNFILLED_FIELD = -1.0f;

    /** Name of the node, for clarity sake */
    public String mName;

    /** True if we are an evidence node, false if we are not. */
    private boolean mIsEvidence = false;

    /** Parents of given node, to preserve tree structure */
    Vector<BayesNode> mParents = new Vector<BayesNode>();

    /** Children of given node, for tree structure */
    Vector<BayesNode> mChildren = new Vector<BayesNode>();

    public BayesNode(String name) {
        mName = name;
    }

    public void SetAttrs(Vector<BayesAttributePair<Float>> attrs)
    {
        // replace previous attribute array if new is provided
        if (attrs != null)
            mAttrs = attrs;

        InitializeProbMatrix();
    }

    public void InitializeProbMatrix()
    {
        // generate a probability matrix from the data we curretly have
        // Attribute count is easy - extract from mAttrs
        // Combination count depends on parents - it is all combinations
        // between all variables possible.
        mAttributeCount = mAttrs.size();
        mCombinationCount = 1;
        if (!mParents.isEmpty())
            for (BayesNode parent : mParents)
                mCombinationCount *= parent.GetAttributeCount();

        // drop previous attribute matrix and generate a new undefined one
        mProbMatrix = new Vector<Float>();
        for (int i = 0; i < mAttributeCount * mCombinationCount; ++i)
        {
            if (mParents.isEmpty())
                // we have no parents :( So we can take the probs from attrs
                mProbMatrix.add(mAttrs.get(i).value);
            else
                // we have parents, yay! The matrix will be manually set
                mProbMatrix.add(UNFILLED_FIELD);
        }
    }

    public void SetProbability(int combination, int attribute, float value)
    {
        if (combination > mCombinationCount)
            throw new ArrayIndexOutOfBoundsException();

        if (attribute > mAttributeCount)
            throw new ArrayIndexOutOfBoundsException();

        mProbMatrix.set(combination * mAttributeCount + attribute, value);
    }

    public void SetProbability(Vector<Float> probMatrix)
    {
        if (probMatrix == null)
            return;

        // probability matrix has size depending on attributes and parents
        // ensure this is a good matrix we receive here
        if (probMatrix.size() != mProbMatrix.size())
            return;

        mProbMatrix = probMatrix;
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
        InitializeProbMatrix(); // TODO this leads to double-updating the matrix.
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
        InitializeProbMatrix();
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
        InitializeProbMatrix();
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
        InitializeProbMatrix();
    }

    public int GetAttributeCount() {
        if (mAttrs != null)
            return mAttrs.size();
        else
            return 0;
    }

    // for debugging purposes
    public void PrintProbsToStdout() {
        if (mProbMatrix == null)
        {
            System.out.println("Node " + mName + " has no attributes.");
            return;
        }

        System.out.println("Node " + mName + " probability matrix:");
        int attributeCounter = 0;
        for (Float prob : mProbMatrix)
        {
            if (attributeCounter == mAttributeCount)
            {
                attributeCounter = 0;
                System.out.print("\n");
            }
            System.out.print(prob.toString() + " ");
            attributeCounter++;
        }

        System.out.println("\n");
    }
}
