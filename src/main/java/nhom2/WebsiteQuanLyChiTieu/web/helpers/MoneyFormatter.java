package nhom2.WebsiteQuanLyChiTieu.web.helpers;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

@Component
public class MoneyFormatter implements Formatter<BigDecimal> {
    @Override
    public BigDecimal parse(String text, Locale locale) throws ParseException {
        return null;
    }

    @Override
    public String print(BigDecimal object, Locale locale) {
        DecimalFormat dcf = new DecimalFormat("#,###");

//        return object.toString() + " VN";
        return dcf.format(object)+ " đồng";
    }
}
