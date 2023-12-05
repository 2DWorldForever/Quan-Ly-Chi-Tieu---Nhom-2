package nhom2.WebsiteQuanLyChiTieu.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "expense_seq_generator")
    @SequenceGenerator(name = "expense_seq_generator", sequenceName = "EXPENSE_SEQ", allocationSize = 1)
    private Long id;

    @NotEmpty(message = "Vui lòng điền tên hoặc mô tả chi phí")
    private String name;

    @NotEmpty(message = "Vui lòng chọn phân loại chi phí")
    private String expenseType;

    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer=15, fraction=0 )
    @NotNull(message = "Vui lòng điền số tiền đã giao dịch")
    private BigDecimal amount;

    @NotNull(message = "Vui lòng nhập ngày thực hiện giao dịch")
    private LocalDate date;

    @CreationTimestamp
    private Timestamp creationDate;


}
