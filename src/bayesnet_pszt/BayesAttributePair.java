package bayesnet_pszt;

/** A pair of key-value store. Key is a string, value is of type T */
public final class BayesAttributePair<T> {
    String key;
    T value;

    public BayesAttributePair()
    {
        //
    }

    public BayesAttributePair(String key, T value)
    {
        this.key = key;
        this.value = value;
    }
}
