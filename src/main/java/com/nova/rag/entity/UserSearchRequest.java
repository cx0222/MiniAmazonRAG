package com.nova.rag.entity;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Objects;

@Validated
public class UserSearchRequest {
    @Size(min = 10, max = 100)
    private String query;
    @Min(2)
    @Max(10)
    private int limit;

    public UserSearchRequest() {
    }

    public UserSearchRequest(String query, int limit) {
        this.query = query;
        this.limit = limit;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        UserSearchRequest that = (UserSearchRequest) object;
        return limit == that.limit
                && Objects.equals(query, that.query);
    }

    @Override
    public int hashCode() {
        return Objects.hash(query, limit);
    }

    @Override
    public String toString() {
        return "UserSearchRequest{" +
                "query='" + query + '\'' +
                ", limit=" + limit +
                '}';
    }
}
