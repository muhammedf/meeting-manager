package muhammedf.meetingmanager.model;

/**
 * Interface for classes that have id field in.
 * @param <ID> type of the id field.
 */
public interface Identity <ID> {
    public ID getId();
    public void setId(ID id);
}
