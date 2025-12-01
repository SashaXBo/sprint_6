package ru.praktikum.qa_scooter.page;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.praktikum.qa_scooter.model.OrderData;

import java.time.Duration;

public class OrderPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Локаторы первой формы
    private final By firstNameInput = By.cssSelector("input[placeholder='* Имя']");
    private final By lastNameInput = By.cssSelector("input[placeholder='* Фамилия']");
    private final By addressInput = By.cssSelector("input[placeholder='* Адрес: куда привезти заказ']");
    private final By metroInput = By.cssSelector("input[placeholder='* Станция метро']");
    private final By metroDropdownOptions =
            By.xpath("//div[contains(@class, 'select-search__select')]//div");
    private final By phoneInput = By.cssSelector("input[placeholder='* Телефон: на него позвонит курьер']");
    private final By nextButton = By.xpath("//button[contains(@class, 'Button_Middle') and text()='Далее']");

    // Локаторы второй формы
    private final By dateInput = By.cssSelector("input[placeholder='* Когда привезти самокат']");
    private final By rentalPeriodDropdown = By.className("Dropdown-placeholder");
    private final By rentalPeriodOptions = By.xpath("//div[@class='Dropdown-option']");
    private final By commentInput = By.cssSelector("input[placeholder='Комментарий для курьера']");
    private final By orderButtonInForm =
            By.xpath("//button[contains(@class, 'Button_Middle') and text()='Заказать']");

    // Модалка подтверждения
    private final By confirmButton =
            By.xpath("//button[contains(@class, 'Button_Middle') and text()='Да']");
    private final By successMessage =
            By.xpath("//div[contains(text(), 'Заказ оформлен')]");
    private OrderData order;

    public OrderPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void fillFirstForm(ru.praktikum.qa_scooter.model.OrderData order) {
        this.order = order;
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameInput)).sendKeys(order.firstName);
        driver.findElement(lastNameInput).sendKeys(order.lastName);
        driver.findElement(addressInput).sendKeys(order.address);

        WebElement metro = driver.findElement(metroInput);
        metro.click();
        metro.sendKeys(order.station);

        // выбираем нужную станцию по тексту
        WebElement metroOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class, 'select-search__select')]//div[contains(text(), '"
                        + order.station + "')]")));
        metroOption.click();

        driver.findElement(phoneInput).sendKeys(order.phone);
        driver.findElement(nextButton).click();
    }

    public void fillSecondForm(ru.praktikum.qa_scooter.model.OrderData order) {
        WebElement date = wait.until(ExpectedConditions.elementToBeClickable(dateInput));
        date.click();
        date.sendKeys(order.date);
        date.sendKeys(Keys.ENTER);

        WebElement rentalDropdown = wait.until(ExpectedConditions.elementToBeClickable(rentalPeriodDropdown));
        rentalDropdown.click();

        WebElement rentalOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@class='Dropdown-option' and text()='" + order.rentalPeriod + "']")));
        rentalOption.click();

        WebElement colorCheckbox = driver.findElement(
                By.xpath("//input[@id='black' or @id='grey']/parent::label[contains(text(), '"
                        + order.color + "')]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", colorCheckbox);
        colorCheckbox.click();

        if (order.comment != null && !order.comment.isEmpty()) {
            driver.findElement(commentInput).sendKeys(order.comment);
        }

        WebElement orderBtn = driver.findElement(orderButtonInForm);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", orderBtn);
        orderBtn.click();
    }

    public void confirmOrder() {
        wait.until(ExpectedConditions.elementToBeClickable(confirmButton)).click();
    }

    public void assertOrderCreated() {
        WebElement msg = wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
        if (!msg.isDisplayed()) {
            throw new AssertionError("Сообщение об успешном заказе не отображается");
        }
    }

}

