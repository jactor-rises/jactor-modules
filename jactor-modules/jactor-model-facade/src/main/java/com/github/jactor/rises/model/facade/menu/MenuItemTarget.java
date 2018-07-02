package com.github.jactor.rises.model.facade.menu;

import com.github.jactor.rises.client.datatype.Parameter;
import org.apache.commons.lang3.Validate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.Objects.hash;

/** A bean representing the target of a menu item */
public class MenuItemTarget {

    private static final String COMMA = ",";
    private static final String QUESTION_MARK = "?";

    static final String THE_TARGET_CANNOT_BE_EMPTY = "The target cannot be empty";

    private final Set<Parameter> parameters = new HashSet<>();
    private final String target;

    public MenuItemTarget(String target) {
        Validate.notEmpty(target, THE_TARGET_CANNOT_BE_EMPTY);
        this.target = removeParametersFrom(target);
        parameters.addAll(findParametersWithValuesFrom(target));
    }

    public MenuItemTarget(HttpServletRequest httpServletRequest) {
        this(httpServletRequest != null ? httpServletRequest.getRequestURI() : null);
    }

    private String removeParametersFrom(String target) {
        int indexQuestion = target.indexOf(QUESTION_MARK);

        if (indexQuestion == -1) {
            return target;
        }

        return target.substring(0, indexQuestion);
    }

    private List<Parameter> findParametersWithValuesFrom(String target) {
        int indexQuestion = target.indexOf(QUESTION_MARK);

        if (indexQuestion == -1) {
            return Collections.emptyList();
        }

        return findParametersFromString(target.substring(indexQuestion + 1));
    }

    private List<Parameter> findParametersFromString(String parametersAsString) {
        String[] severalParameters = parametersAsString.split(COMMA);
        List<Parameter> parameterList = new ArrayList<>(severalParameters.length);

        for (String parameterAndValue : severalParameters) {
            parameterList.add(new Parameter(parameterAndValue));
        }

        return parameterList;
    }

    @Override public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        MenuItemTarget menuItemTarget = (MenuItemTarget) obj;

        return Objects.equals(target, menuItemTarget.target) && Objects.equals(parameters, menuItemTarget.parameters);
    }

    @Override public int hashCode() {
        return hash(target, parameters);
    }

    @Override public String toString() {
        return target + parametersAsString();
    }

    private String parametersAsString() {
        StringBuilder params = new StringBuilder(parameters.isEmpty() ? "" : QUESTION_MARK);

        for (Parameter parameter : parameters) {
            params.append(parameter.toString());
        }

        return params.toString();
    }

    public String getTarget() {
        return target;
    }

    public Set<Parameter> getParameters() {
        return parameters;
    }
}
