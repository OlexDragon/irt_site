package irt.data.companies;

public class Place {

    int id;
    String name;

    public Place() {
    }

    public Place(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        boolean isEquals = false;

        if (obj != null && obj.getClass().getSimpleName().equals(getClass().getSimpleName())) {
            isEquals = ((Place) obj).toString().equals(toString());
        }

        return isEquals;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return "Place [id=" + id + ", name=" + name + "]";
    }
}
