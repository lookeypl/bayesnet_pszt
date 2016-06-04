package bayesnet_pszt;

import java.util.Vector;

public class BayesNode {
    /** Our probabilities to recalculate for each node */
    public Vector<BayesAttributePair<Float>> mAttrs = new Vector<BayesAttributePair<Float>>();

    /** Probability matrix, used for calculations */
    public Vector<Float> mProbMatrix = new Vector<Float>();
    private int mAttributeCount = 0;
    private int mCombinationCount = 0;
    public static final float UNFILLED_FIELD = 0.0f;

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
        if (attrs == null)
            throw new IllegalArgumentException();

        // replace previous attribute array if new is provided
        mAttrs = attrs;
        InitializeProbMatrix();
    }

    public void SetAttributeValue(int attr, float value)
    {
        if (attr >= mAttributeCount)
            throw new ArrayIndexOutOfBoundsException();

        mAttrs.get(attr).value = value;
    }

    public void InitializeProbMatrix()
    {
        // generate a probability matrix from the data we currently have
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

    public void SetProbability(int combination, int attribute, float value) {
        if (combination > mCombinationCount)
            throw new ArrayIndexOutOfBoundsException();

        if (attribute > mAttributeCount)
            throw new ArrayIndexOutOfBoundsException();

        mProbMatrix.set(combination * mAttributeCount + attribute, value);
    }

    public void SetProbability(Vector<Float> probMatrix) {
        if (probMatrix == null)
            throw new IllegalArgumentException();

        // probability matrix has size depending on attributes and parents
        // ensure this is a good matrix we receive here
        if (probMatrix.size() != mProbMatrix.size())
            throw new IllegalArgumentException();

        mProbMatrix = probMatrix;
    }

    public void SetEvidence(boolean isEvidence, Vector<BayesAttributePair<Float>> evidenceAttrs) {
        mIsEvidence = isEvidence;
        mAttrs = evidenceAttrs;

        if (mIsEvidence) {
            // TODO: recalc probabilities here
        }
    }

    public boolean IsEvidence() {
        return mIsEvidence;
    }

    public void AddParent(BayesNode parent) {
        if (parent == null)
            throw new IllegalArgumentException();

        if (parent == this || mParents.contains(parent) || mChildren.contains(parent))
            return;

        mParents.add(parent);
        parent.AddChild(this);
        InitializeProbMatrix(); // TODO this leads to double-updating the matrix.
    }

    public void AddChild(BayesNode child) {
        if (child == null)
            throw new IllegalArgumentException();

        if (child == this || mChildren.contains(child) || mParents.contains(child))
            return;

        mChildren.add(child);
        child.AddParent(this);
        InitializeProbMatrix();
    }

    public void RemoveParent(BayesNode parent) {
        if (parent == null)
            throw new IllegalArgumentException();

        if (parent == this || !mParents.contains(parent))
            return;

        mParents.remove(parent);
        parent.RemoveChild(this);
        InitializeProbMatrix();
    }

    public void RemoveChild(BayesNode child) {
        if (child == null)
            throw new IllegalArgumentException();

        if (child == this || !mChildren.contains(child))
            return;

        mChildren.remove(child);
        child.RemoveParent(this);
        InitializeProbMatrix();
    }

    public void RemoveAllChildren() {
        for (BayesNode child : mChildren)
            RemoveChild(child);

        mChildren.clear();
    }

    public void RemoveAllParents() {
        for (BayesNode parent : mParents)
            RemoveParent(parent);

        mParents.clear();
    }

    public int GetAttributeCount() {
        return mAttributeCount;
    }

    public int GetCombinationCount() {
        return mCombinationCount;
    }

    public BayesNode GetParent(int index) {
        return mParents.get(index);
    }

    public boolean HasParents() {
        return !mParents.isEmpty();
    }

    public boolean HasChildren() {
        return !mChildren.isEmpty();
    }

    public BayesAttributePair<Float> GetAttribute(int index) {
        return mAttrs.get(index);
    }

    public float GetProbability(int attribute, int combination) {
        if (combination > mCombinationCount)
            throw new ArrayIndexOutOfBoundsException();

        if (attribute > mAttributeCount)
            throw new ArrayIndexOutOfBoundsException();

        return mProbMatrix.get(combination * mAttributeCount + attribute);
    }

    // for debugging purposes
    public void PrintProbsToStdout() {
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
