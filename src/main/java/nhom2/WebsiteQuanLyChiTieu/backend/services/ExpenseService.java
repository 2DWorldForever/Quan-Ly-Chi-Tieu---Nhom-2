package nhom2.WebsiteQuanLyChiTieu.backend.services;

import nhom2.WebsiteQuanLyChiTieu.backend.model.Expense;
import nhom2.WebsiteQuanLyChiTieu.data.ExpenseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.stream.StreamSupport;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public Expense save(Expense entity) {
        return expenseRepository.save(entity);
    }

    /**
     * Tìm kiếm giao dịch
     * Truyền vào @param id
     * Trả về @returns nếu tìm thấy id của giao dịch cần tìm, nếu không thì báo lỗi EntityNotFoundException
     */
    public Expense findById(Long id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tồn tại dữ liệu bạn đang tìm kiếm"));
    }


    /**
     * Tìm kiếm và hiển thị một trang chứa các giao dịch đã thực hiện xếp theo ngày tạo thứ tự giảm dần
     * p
     * Tạo một đối tượng Pageable để định dạng số trang, số lượng trong một trang và các loại định dạng khác
     * Sortby dùng để hiển thị các giao dịch từ mới nhất đến muộn nhất
     *
     * @param pageable Dùng để định dạng số trang, số lượng và các định dạng khác
     * @return Trả về một trang chứa các giao dịch đã thực hiện xếp theo ngày tạo thứ tự giảm dần
     */
    public Page<Expense> findAll(Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("creationDate").descending());

        return expenseRepository.findAll(sortedPageable);
    }


    public Iterable<Expense> findAll() {
        return expenseRepository.findAll();
    }

    /**
     * Xóa giao dịch theo id
     * @param id là giao dịch
     * Trả về lệnh xóa giao dịch chứa đó
     */
    public void deleteById(Long id) {
        Expense expenseToBeDeleted = findById(id);
        expenseRepository.delete(expenseToBeDeleted);
    }

    /**
     * Tính tổng số tiền đã chi theo các giao dịch đã có

     * Interable dùng để duyệt mảng
     * StreamSupport.stream(expenses.spliterator(), false): Chuyển đối tượng Iterable thành một luồng Stream.
       Phương thức spliterator() được sử dụng để chia đối tượng Iterable thành các phần (spliterator),
       và sau đó, StreamSupport.stream tạo một luồng từ spliterator đó.
     * .toList(): Chuyển đổi Stream thành một danh sách (List). Điều này là cần thiết vì bạn không thể trực tiếp sử dụng phương thức stream() trên Iterable.
     * .stream(): Chuyển đổi danh sách thành một luồng Stream khác.
     * .map(Expense::getAmount): Ánh xạ mỗi đối tượng Expense thành giá trị của thuộc tính amount của nó.
       Expense::getAmount là một phương thức tham chiếu, thường được sử dụng để truy cập một phương thức của đối tượng.
     * .reduce(BigDecimal.ZERO, BigDecimal::add): Thực hiện phép giảm giá (reduce) trên các giá trị của Stream để tính tổng.
       Phương thức này sử dụng BigDecimal.ZERO làm giá trị ban đầu và BigDecimal::add làm hàm nối (accumulator function) để thực hiện phép cộng.

     * @param expenses là tham số chứa các chi phí đã có
     * @return Trả về tổng số lượng tiền đã giao dịch theo kiểu BigDecimal
     */
    public BigDecimal getTotalAmount(Iterable<Expense> expenses) {
        return StreamSupport.
                stream(expenses.spliterator(), false)
                .toList()
                .stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Retrieves a Page of Expense objects filtered by year, month, and expense type.
     * <p>
     * This method queries the expense repository to retrieve expenses within the specified year and month,
     * belonging to the given expense type. The results are ordered by creation date in descending order.
     *
     * @param year        The year for filtering expenses.
     * @param month       The month for filtering expenses.
     * @param expenseType The expense type for filtering expenses.
     * @param page        The Pageable object specifying the desired page and page size.
     * @return A Page containing filtered Expense objects.
     */
    public Page<Expense> getExpensesByYearMonthAndType(int year, Month month, String expenseType, Pageable page) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return expenseRepository.findByDateBetweenAndExpenseTypeOrderByCreationDateDesc(startDate, endDate, expenseType, page);
    }

    /**
     * Retrieves a Page of Expense objects filtered by year and month.
     * <p>
     * This method queries the expense repository to retrieve expenses within the specified year and month.
     * The results are ordered by creation date in descending order.
     *
     * @param year  The year for filtering expenses.
     * @param month The month for filtering expenses.
     * @param page  The Pageable object specifying the desired page and page size.
     * @return A Page containing filtered Expense objects.
     */
    public Page<Expense> getExpensesByYearMonth(int year, Month month, Pageable page) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return expenseRepository.findByDateBetweenOrderByCreationDateDesc(startDate, endDate, page);
    }


    /**
     * Retrieves a Page of Expense objects filtered by expense type.
     * <p>
     * This method queries the expense repository to retrieve expenses of the specified expense type.
     * The results are ordered by creation date in descending order.
     *
     * @param expenseType The expense type for filtering expenses.
     * @param page        The Pageable object specifying the desired page and page size.
     * @return A Page containing filtered Expense objects.
     */
    public Page<Expense> getExpensesByType(String expenseType, Pageable page) {
        return expenseRepository.findByExpenseTypeOrderByCreationDateDesc(expenseType, page);
    }


    /**
     * Converts a collection of Expense objects into a CSV-formatted string.
     * For a high number of records, it would be ineffective to convert all to String and hold in memory.
     * But for the case of simplicity, I wrote the code in this way since I am dealing with a little amount of data.
     * <p>
     * This method iterates through the collection of Expense objects and formats them into
     * a CSV format, including the Id, Name of Expense, Type of Expense, Amount, Date, and Creation Timestamp.
     * The formatted CSV string is returned.
     *
     * @param expenses The collection of Expense objects to be converted.
     * @return A CSV-formatted string containing the data from the Expense objects.
     */
    public String convertToCSV(Iterable<Expense> expenses) {
        StringBuilder expensesAsCSV = new StringBuilder();
        expensesAsCSV.append("ID,Tên chi phí,Loại chi phí,Số tiền,Ngày,Thời gian tạo bản ghi\n");

        for (Expense expense : expenses) {
            expensesAsCSV.append(expense.getId()).append(",")
                    .append(expense.getName()).append(",")
                    .append(expense.getExpenseType()).append(",")
                    .append(expense.getAmount()).append(",")
                    .append(expense.getDate()).append(",")
                    .append(expense.getCreationDate()).append("\n");
        }

        return expensesAsCSV.toString();
    }

}
