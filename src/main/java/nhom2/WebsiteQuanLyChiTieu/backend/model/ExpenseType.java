package nhom2.WebsiteQuanLyChiTieu.backend.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseType {

    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty(message = "Hãy phân loại chi phí của bạn")
    @Column(unique = true)
    private String expenseCategory;


}
