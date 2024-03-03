package proj.java.spring;

public class SimpleDestination {
    private String name;
    private String description;

    public long getName() {
        return Long.valueOf(name);
    }

    public long getDescription() {
        return Long.valueOf(description);
    }

    public void setName(String destinationName) {
        this.name=destinationName;
    }

    public void setDescription(String destinationDescription) {
        this.description = destinationDescription;
    }
    // getters and setters
}