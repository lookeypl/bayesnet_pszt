package bayesnet_pszt;

import java.util.Vector;

public class BayesNode {
    /** Our probabilities - the result of calculations for each node, with names */
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

    /** True if node is already evaluated, false if our attrs need to be evaluated */
    private boolean mIsEvaluated = false;

    /** True if we already gathered a Parent attribute iterator here */
    private boolean mParentAcquired = false;

    /** True if we acquired joint probability in current run */
    private boolean mJointAcquired = false;

    /** Parents of given node, to preserve tree structure */
    Vector<BayesNode> mParents = new Vector<BayesNode>();

    /** Children of given node, for tree structure */
    Vector<BayesNode> mChildren = new Vector<BayesNode>();

    public BayesNode(String name) {
        mName = name;
    }

    public void InitializeProbMatrix()
    {
        if (mIsEvidence) {
            // copy current attributes to prob matrix
            mAttributeCount = mAttrs.size();
            mCombinationCount = 1;

            if (mProbMatrix == null)
                mProbMatrix = new Vector<Float>(mAttributeCount);
            else
                mProbMatrix.setSize(mAttributeCount);

            for (int i = 0; i < mAttributeCount; ++i)
                mProbMatrix.set(i, mAttrs.get(i).value);
        } else {
            // First, check if the matrix has changed
            int newAttributeCount = mAttrs.size();
            int newCombinationCount = 1;
            if (!mParents.isEmpty())
                for (BayesNode parent : mParents)
                    newCombinationCount *= parent.GetAttributeCount();

            if ((newAttributeCount != mAttributeCount) ||
                (newCombinationCount != mCombinationCount))
            {
                // it did - resize it and set new fields to default value
                if (mProbMatrix == null)
                    mProbMatrix = new Vector<Float>(newAttributeCount * newCombinationCount);
                else
                    mProbMatrix.setSize(newAttributeCount * newCombinationCount);

                for (int i = mAttributeCount * mCombinationCount;
                         i < newAttributeCount * newCombinationCount;
                         ++i)
                    // initialize new fields
                    mProbMatrix.set(i, UNFILLED_FIELD);

                mAttributeCount = newAttributeCount;
                mCombinationCount = newCombinationCount;
            }
        }
    }

    public void SetAttributes(Vector<BayesAttributePair<Float>> attrs)
    {
        if (attrs == null)
            throw new IllegalArgumentException();

        int oldAttrCount = mAttrs.size();
        // replace previous attribute array if new is provided
        mAttrs = new Vector<BayesAttributePair<Float>>(attrs);
        InitializeProbMatrix();

        if (oldAttrCount != attrs.size())
        {
            for (BayesNode n : mChildren)
            {
                n.InitializeProbMatrix();
            }
        }
    }

    public void SetAttributeValue(int attr, float value)
    {
        if (attr >= mAttributeCount)
            throw new ArrayIndexOutOfBoundsException();

        mAttrs.get(attr).value = value;
        InitializeProbMatrix();
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

    public void SetEvidence(boolean isEvidence) {
        this.mIsEvidence = isEvidence;
        InitializeProbMatrix();
    }

    public boolean IsEvidence() {
        return mIsEvidence;
    }

    public boolean IsEvaluated() {
        return mIsEvaluated;
    }

    public void AddParent(BayesNode parent) {
        if (parent == null)
            throw new IllegalArgumentException();

        if (parent == this || mParents.contains(parent) || mChildren.contains(parent))
            return;

        mParents.add(parent);
        parent.AddChild(this);
        InitializeProbMatrix();
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
        Vector<BayesNode> children = new Vector<BayesNode>(mChildren);
        int childrenSize = mChildren.size();
        for (int i = 0; i < childrenSize; ++i)
            RemoveChild(children.get(i));

        mChildren.clear();
    }

    public void RemoveAllParents() {
        Vector<BayesNode> parents = new Vector<BayesNode>(mParents);
        int parentsSize = mParents.size();
        for (int i = 0; i < parentsSize; ++i)
            RemoveParent(parents.get(i));

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

    public void GetParentAttributes(Vector<BayesAttributeState> attr) {
        attr.add(new BayesAttributeState(this, 0));
        mParentAcquired = true;

        if (!HasParents())
            return;
        else
            for (BayesNode parent : mParents)
                if (!parent.mParentAcquired)
                    parent.GetParentAttributes(attr);
    }

    public float GetJointProbability(Vector<BayesAttributeState> attrStates) {
        if (mJointAcquired)
            return 1.0f;

        mJointAcquired = true;

        // get our attribute
        int attr = -1;
        for (BayesAttributeState curNode : attrStates)
        {
            if (curNode.node == this) {
                attr = curNode.curAttr;
                break; // there should be no duplicates in state vector
            }
        }

        if (attr == -1) {
            // unlikely, but better safe than sorry
            System.out.println("ERROR: Unable to find currently processed Node in states!");
            return -1.0f; // we'll know then for sure, that states are corrupted
        }

        if (!HasParents())
            // since we have no parents, matrix has only one row
            return GetProbability(attr, 0);
        else
        {
            // we have parents, let's find out which matrix row we are after
            float prob = 1.0f;

            // our current row depends solely on how many parents we have
            int[] parentStates = new int[mParents.size()];
            int[] maxParentStates = new int[mParents.size()];
            int stateIt = 0;

            for (BayesNode parent : mParents)
            {
                for (BayesAttributeState parentAttr : attrStates)
                {
                    if (parent == parentAttr.node) {
                        parentStates[stateIt] = parentAttr.curAttr;
                        maxParentStates[stateIt] = parentAttr.node.GetAttributeCount();
                        stateIt++;
                    }
                }
            }

            // finally calcualte our combination
            int comb = 0;
            for (int i = 0; i < stateIt; ++i)
            {
                int combMul = parentStates[i];
                for (int j = i + 1; j < stateIt; ++j)
                {
                    combMul *= maxParentStates[j];
                }
                comb += combMul;
            }

            prob *= GetProbability(attr, comb);
            for (BayesNode parent : mParents)
                prob *= parent.GetJointProbability(attrStates);

            return prob;
        }
    }

    public void ResetJointAcquired() {
        if (mJointAcquired) {
            mJointAcquired = false;
        }

        for (BayesNode parent : mParents)
            parent.ResetJointAcquired();
    }

    public void ResetParentAcquired() {
        if (mParentAcquired) {
            mParentAcquired = false;
        }

        for (BayesNode parent : mParents)
            parent.ResetParentAcquired();
    }

    // given current data, evaluate according to parents
    public void Evaluate() {
        if (mIsEvidence || mIsEvaluated)
            return;

        // shortcut for root nodes
        if (!HasParents())
        {
            for (int i = 0; i < mAttributeCount; ++i)
                mAttrs.get(i).value = mProbMatrix.get(i);
            return;
        }

        // our attribute state containing all the nodes with their currently processed attr
        Vector<BayesAttributeState> attrState = new Vector<BayesAttributeState>();
        ResetParentAcquired();
        GetParentAttributes(attrState);

        // summarize how many combinations this makes us (skipping current - zero state)
        int combTotal = 1;
        for (int i = 1; i < attrState.size(); ++i)
            combTotal *= attrState.get(i).node.GetAttributeCount();

        for (int i = 0; i < GetAttributeCount(); ++i)
        {
            float attributeValue = 0.0f;

            for (int j = 0; j < combTotal; ++j)
            {
                ResetJointAcquired(); // reset joint acquired visit flags
                attributeValue += GetJointProbability(attrState);

                // deliberately skip state 0 during iteration, as this is for sure our current node
                for (int state = attrState.size() - 1; state >= 1; --state)
                {
                    attrState.get(state).curAttr++;
                    if (attrState.get(state).curAttr == attrState.get(state).node.GetAttributeCount()) {
                        attrState.get(state).curAttr = 0;
                    } else {
                        break;
                    }
                }
            }

            mAttrs.get(i).value *= attributeValue;

            attrState.get(0).curAttr++;
        }

        mIsEvaluated = true;
    }

    public void Unevaluate() {
        mIsEvaluated = false;
        mParentAcquired = false;

        for (int i = 0; i < mAttributeCount; ++i)
            mAttrs.get(i).value = 1.0f;
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
