package ru.uzaretskaya.todo.business.repository.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class TaskSearchValues {

    private String title;
    private String email;
    private Short completed;
    private Long priorityId;
    private Long categoryId;
    private String sortColumn;
    private String sortDirection;
    private Integer pageNumber;
    private Integer pageSize;

    private Date dateFrom;
    private Date dateTo;

    @Override
    public String toString() {
        return "TaskSearchValues{" +
                "title='" + title + '\'' +
                ", email='" + email + '\'' +
                ", completed='" + completed + '\'' +
                ", priorityId='" + priorityId + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", sortColumn='" + sortColumn + '\'' +
                ", sortDirection='" + sortDirection + '\'' +
                ", pageNumber='" + pageNumber + '\'' +
                ", pageSize='" + pageSize + '\'' +
                '}';
    }
}
