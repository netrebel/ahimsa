package ahimsa.mongo;

public class MongoWriteResult {

    private final int n;
    private final boolean updatedExisting;

    public MongoWriteResult(int n, boolean updatedExisting) {
        this.n = n;
        this.updatedExisting = updatedExisting;
    }

    public int getNumberOfAffectedDocuments() {
        return n;
    }

    public boolean isUpdateOfExisting() {
        return updatedExisting;
    }

}
