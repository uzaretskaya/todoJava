package ru.uzaretskaya.todo.business.repository.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class CategorySearchValues {

    private String title;
    private String email;

    @Override
    public String toString() {
        return "CategorySearchValues{" +
                "title='" + title + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
