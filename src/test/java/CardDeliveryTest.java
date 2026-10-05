import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {

    // Текущая дата + нужное количество дней, метод
    public String generateDate(int days, String pattern) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern(pattern));
    }

    @Test
    void shouldSuccessfullyBookDelivery() {
        open("http://localhost:9999");

        // Дата на 3 дня вперед от актуальной
        String planningDate = generateDate(3, "dd.MM.yyyy");

        $("[data-test-id='city'] input").setValue("Казань");

        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(planningDate);

        $("[data-test-id='name'] input").setValue("Петров-Сидоров Ян");

        $("[data-test-id='phone'] input").setValue("+79123456789");

        $("[data-test-id='agreement']").click();

        $$("button").find(Condition.exactText("Забронировать")).click();

        $("[data-test-id='notification']")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Успешно!\nВстреча успешно забронирована на " + planningDate));
    }
}
