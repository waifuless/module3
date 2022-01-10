package com.epam.esm.gcs.persistence.model;

public class TagModel {

    private final Long id;
    private final String name;

    public TagModel(String name) {
        this.id = null;
        this.name = name;
    }

    public TagModel(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TagModel tagModel = (TagModel) o;

        if (id != null ? !id.equals(tagModel.id) : tagModel.id != null) return false;
        return name != null ? name.equals(tagModel.name) : tagModel.name == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
