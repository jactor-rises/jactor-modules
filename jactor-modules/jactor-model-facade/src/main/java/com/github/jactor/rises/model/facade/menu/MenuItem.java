package com.github.jactor.rises.model.facade.menu;

import com.github.jactor.rises.client.datatype.Name;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.hash;

public class MenuItem {

    private final Name itemName;
    private final String description;
    private final List<MenuItem> children = new ArrayList<>();
    private final String target;

    MenuItem(Name itemName, String description, String target) {
        this.itemName = itemName;
        this.description = description;
        this.target = target;
    }

    MenuItem appendChildren(List<MenuItem> children) {
        this.children.addAll(children);
        return this;
    }

    public boolean isChosen(String target) {
        return target != null && target.equals(this.target);
    }

    public boolean isChildChosen(String target) {
        for (MenuItem menuItem : children) {
            if (menuItem.isChosen(target)) {
                return true;
            }
        }

        return false;
    }

    @Override public int hashCode() {
        return hash(itemName, getChildren(), getDescription(), getTarget());
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MenuItem other = (MenuItem) o;

        return Objects.equals(getItemName(), other.getItemName()) &&
                Objects.equals(getChildren(), other.getChildren()) &&
                Objects.equals(getDescription(), other.getDescription()) &&
                Objects.equals(getTarget(), other.getTarget());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_FIELD_NAMES_STYLE)
                .append(getItemName())
                .append(getDescription())
                .append(getChildren())
                .append(getTarget())
                .toString();
    }

    public List<MenuItem> getChildren() {
        return children;
    }

    public String getDescription() {
        return description;
    }

    public String getTarget() {
        return target;
    }

    public Name getItemName() {
        return itemName;
    }

    public static MenuItemBuilder aMenuItem() {
        return new MenuItemBuilder();
    }
}
