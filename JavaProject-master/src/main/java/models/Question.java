package models;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name="tbl_questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(length = 500, nullable = false)
    private String name;

    @OneToMany(mappedBy = "question")
    private List<Answer> answers;

    public Question() {
        answers = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.name);
        builder.append("\n");
        int i = 1;
        for (Answer answer : answers) {
            builder.append(i + ". " + answer.getText() + "\n");
            i++;
        }

        return builder.toString();
    }
}
