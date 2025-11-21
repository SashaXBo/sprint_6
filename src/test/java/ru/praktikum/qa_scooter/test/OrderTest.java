package ru.praktikum.qa_scooter.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.stream.Stream;

public class OrderTest {
    private WebDriver driver;
    private WebDriverWait wait;

    // Пример данных заказа
    static Stream<OrderData> orderDataProvider() {
        return Stream.of(
                new OrderData("Иван", "Иванов", "Москва, ул. Ленина, 1", "Черкизовская",
                        "+79001000000", "31.12.2025", "двое суток", "чёрный жемчуг",
                        "Комментарий для курьера"),
                new OrderData("Петр", "Петров", "Санкт-Петербург, пр. Мира, 5", "Сокольники",
                        "+79002000000", "01.01.2026", "трое суток", "серая безысходность",
                        "Позвонить за час")
        );
    }

    @BeforeEach
    void init() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://qa-scooter.praktikum-services.ru/");
    }

    @ParameterizedTest
    @MethodSource("orderDataProvider")
    void shouldOrderScooterFromTopButton(OrderData orderData) {
        placeOrder(orderData, By.className("Button_Button__ra12g"));
    }

    @ParameterizedTest
    @MethodSource("orderDataProvider")
    void shouldOrderScooterFromBottomButton(OrderData orderData) {
        // Пролистать вниз к кнопке заказа в середине страницы
        WebElement bottomButton = driver.findElement(By.xpath("//div[@class='Home_FinishButton__1_cWm']//button"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", bottomButton);
        placeOrder(orderData, By.xpath("//div[@class='Home_FinishButton__1_cWm']//button"));
    }

    private void placeOrder(OrderData order, By orderButtonSelector) {
        // Клик по кнопке "Заказать"
        WebElement orderButton = wait.until(ExpectedConditions.elementToBeClickable(orderButtonSelector));
        orderButton.click();

        // Заполнение первой формы
        fillFirstForm(order);

        // Заполнение второй формы
        fillSecondForm(order);

        // Подтверждение заказа
        confirmOrder();
    }

    private void fillFirstForm(OrderData order) {
        // Имя
        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input[placeholder='* Имя']")));
        nameInput.sendKeys(order.firstName);

        // Фамилия
        driver.findElement(By.cssSelector("input[placeholder='* Фамилия']")).sendKeys(order.lastName);

        // Адрес
        driver.findElement(By.cssSelector("input[placeholder='* Адрес: куда привезти заказ']"))
                .sendKeys(order.address);

        // Станция метро
        WebElement metroInput = driver.findElement(By.cssSelector("input[placeholder='* Станция метро']"));
        metroInput.click();
        metroInput.sendKeys(order.station);

        // Ждем появления выпадающего списка и выбираем станцию
        WebElement metroOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class, 'select-search__select')]//div[contains(text(), '"
                        + order.station + "')]")));
        metroOption.click();

        // Телефон
        driver.findElement(By.cssSelector("input[placeholder='* Телефон: на него позвонит курьер']"))
                .sendKeys(order.phone);

        // Клик по кнопке "Далее"
        driver.findElement(By.xpath("//button[contains(@class, 'Button_Middle') and text()='Далее']")).click();
    }

    private void fillSecondForm(OrderData order) {
        // Дата доставки
        WebElement dateInput = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("input[placeholder='* Когда привезти самокат']")));
        dateInput.click();
        dateInput.sendKeys(order.date);
        dateInput.sendKeys(Keys.ENTER);

        // Срок аренды - клик по дропдауну
        WebElement rentalPeriodDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.className("Dropdown-placeholder")));
        rentalPeriodDropdown.click();

        // Выбор срока аренды из списка
        WebElement rentalPeriodOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@class='Dropdown-option' and text()='" + order.rentalPeriod + "']")));
        rentalPeriodOption.click();

        // Выбор цвета самоката - клик по чекбоксу
        WebElement colorCheckbox = driver.findElement(
                By.xpath("//input[@id='black' or @id='grey']/parent::label[contains(text(), '"
                        + order.color + "')]"));

        // Прокрутка к чекбоксу если нужно
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", colorCheckbox);
        colorCheckbox.click();

        // Комментарий для курьера (необязательное поле)
        WebElement commentInput = driver.findElement(
                By.cssSelector("input[placeholder='Комментарий для курьера']"));
        if (order.comment != null && !order.comment.isEmpty()) {
            commentInput.sendKeys(order.comment);
        }

        // Клик по кнопке "Заказать" в форме
        WebElement orderButtonInForm = driver.findElement(
                By.xpath("//button[contains(@class, 'Button_Middle') and text()='Заказать']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", orderButtonInForm);
        orderButtonInForm.click();
    }

    private void confirmOrder() {
        // Подтверждение в модальном окне
        WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class, 'Button_Middle') and text()='Да']")));
        confirmButton.click();

        // Проверка успешного создания заказа
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(text(), 'Заказ оформлен')]")));

        assert successMessage.isDisplayed() : "Сообщение об успешном заказе не отображается";
    }

    @AfterEach
    void clean() {
        if (driver != null) {
            driver.quit();
        }
    }

    static class OrderData {
        String firstName, lastName, address, station, phone, date, rentalPeriod, color, comment;

        OrderData(String firstName, String lastName, String address, String station,
                  String phone, String date, String rentalPeriod, String color, String comment) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.address = address;
            this.station = station;
            this.phone = phone;
            this.date = date;
            this.rentalPeriod = rentalPeriod;
            this.color = color;
            this.comment = comment;
        }
    }
}
