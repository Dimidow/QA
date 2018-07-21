package com.umarket.test;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import org.apache.commons.io.FileUtils;


public class Louncher {

    private static WebDriver driver = null;
    public static String randomString() {
        String symbols = "RegistrationTesting";
        StringBuilder randString = new StringBuilder();
        int count = (int) (Math.random()) + 10;
        for (int i = 0; i < count; i++) {
            randString.append(symbols.charAt((int) (Math.random() * symbols.length())));
        }
        String s = randString.toString();
        return s;

    }

    public static void main(String[] args) throws IOException, InterruptedException {

        System.setProperty("webdriver.chrome.driver",
                new File(Louncher.class.getResource("/chromedriver.exe").getFile()).getPath());

        long startTime1;
        long timeSpent1 = 0;

        long startTime2;
        long timeSpent2 = 0;

        String urlAfterFirstStepReg = null;

        String lang[] = {"en/", "es/", "ar/", "ru/"};
        String passw[] = new String[lang.length];
        StringBuilder testResult = new StringBuilder();
        String login = null;
        driver = new ChromeDriver();
        driver.manage().window().maximize();


      testResult.append("Registration Testing" + "\n" + "Localization,1-st regStep,2-nd regStep,LangAccount,Status,Additional info" + "\n");
        // register
       for (int i = 0; i < lang.length; i++) {

            Date date = new Date();
            login = date.toString().replaceAll("\\W", "");
            String accountLang = null;
            testResult.append(lang[i] + ",");
            try {

                driver.get("https://www.umarkets.com/" + lang[i] + "registration/");

                startTime1 = System.currentTimeMillis();

                //firstStep. Name
                (new WebDriverWait(driver, 5)).until(ExpectedConditions.
                        elementToBeClickable(By.cssSelector("input[name='name']"))).sendKeys("Test" + randomString());

                //Surname
                driver.findElement(By.cssSelector("input[name='lastName']")).sendKeys("Test" + randomString());

                //email
                driver.findElement(By.cssSelector("input[name='email']")).sendKeys(login + "@test.com");

                //phone
                driver.findElement(By.cssSelector("input[name='phoneNumber']")).sendKeys("888888888");

                //privacy checkbox
                (new WebDriverWait(driver, 5)).until(ExpectedConditions.
                        elementToBeClickable(By.cssSelector("label[class='agree-terms-conditions-label']"))).click();

                //button click
                (new WebDriverWait(driver, 5)).until(ExpectedConditions.
                        elementToBeClickable(By.cssSelector("#RForm-0 > form > div:nth-child(6) > button"))).click();

                timeSpent1 = (System.currentTimeMillis() - startTime1) / 1000;

                testResult.append(timeSpent1 + ",");

                Thread.sleep(2000);
urlAfterFirstStepReg = driver.getCurrentUrl();

startTime2 = System.currentTimeMillis();

                //second step. pass
                (new WebDriverWait(driver, 15)).until(ExpectedConditions.
                        elementToBeClickable(By.cssSelector("input[id='pass1_sekond']"))).sendKeys(login);

                //confirm pass
                (new WebDriverWait(driver, 15)).until(ExpectedConditions.
                        elementToBeClickable(By.cssSelector("input[id='pass2_sekond']"))).sendKeys(login);

                passw[i] = login;

                //Accept checkbox
                (new WebDriverWait(driver, 15)).until(ExpectedConditions.
                        elementToBeClickable(By.cssSelector("input[id='agreement']"))).click();

                (new WebDriverWait(driver, 15)).until(ExpectedConditions.
                        elementToBeClickable(By.cssSelector("input[id='buttonSecondStep']"))).click();

//lang check
                accountLang = (new WebDriverWait(driver, 15)).until(ExpectedConditions.
                        elementToBeClickable(By.cssSelector("body > div.main-register-wrap.uk-flex.uk-flex-column > div > div.mm-header__nav.uk-flex.uk-flex-middle > div.userData > a"))).getText();
                timeSpent2 = (System.currentTimeMillis() - startTime2) / 1000;

                testResult.append(timeSpent2 + ",");
                testResult.append(accountLang + ",");
                testResult.append("ok" + "," + urlAfterFirstStepReg + "\n");


//cookies
                driver.manage().deleteAllCookies();

            } catch (Exception e) {

                Thread.sleep(500);
                File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(scrFile, new File("./src/test/testResults/" + login + ".png"));
                testResult.append(timeSpent1 + "," + timeSpent2 + "," + "null" +"," + login + ".png" + "\n");

            }
        }


        testResult.append("Authorization Testing" + "\n" + "Localization,Status" +"\n");
        //authorization
        for (int j = 0; j < lang.length; j++){
            testResult.append(lang[j] + ",");

            Date date = new Date();
            login = date.toString().replaceAll("\\W", "");

        try{
            driver.get("https://www.umarkets.com/" + lang[j] + "my-account/");

            driver.switchTo().frame(0);

            (new WebDriverWait(driver, 15)).until(ExpectedConditions.
                    elementToBeClickable(By.cssSelector("#email"))).sendKeys(passw[j] + "@test.com");

            (new WebDriverWait(driver, 15)).until(ExpectedConditions.
                    elementToBeClickable(By.cssSelector("#password"))).sendKeys(passw[j]);

            (new WebDriverWait(driver, 15)).until(ExpectedConditions.
                    elementToBeClickable(By.cssSelector("#login > form > div > div > div.form-group.form-group-btn > button"))).click();



            (new WebDriverWait(driver, 15)).until(ExpectedConditions.
                    elementToBeClickable(By.cssSelector("#page-myaccount > div > div.b-list > div.b-body > div > div.b-item_head > div.b-btns > a.btn.btn-success.js-login"))).getText();

            testResult.append("ok" + "\n");

            driver.manage().deleteAllCookies();

        }catch (Exception e){

            Thread.sleep(500);
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(scrFile, new File("./src/test/testResults/" + login + ".png"));
            testResult.append(login + ".png" + "\n");

        }

        }

        testResult.append("Forgot Password Testing" + "\n" + "Localization,mailSendStatus,mailReceivingStatus"+ "\n");
        //forgot password

        String placeholder[] = {"Email", "Correo electronico", "البريد الإلكتروني", "Электронная почта"};

        for (int k = 0; k < lang.length; k++){
            testResult.append(lang[k] + ",");

            Date date = new Date();
            login = date.toString().replaceAll("\\W", "");
            try {

                driver.get("https://www.umarkets.com/" + lang[k] + "my-account/");
                Thread.sleep(2000);

                driver.switchTo().frame(0);
                (new WebDriverWait(driver, 5)).until(ExpectedConditions.elementToBeClickable(By.
                        cssSelector("a[href='#forgotPassword']"))).click();

                (new WebDriverWait(driver, 5)).until(ExpectedConditions.elementToBeClickable(By.
                        cssSelector("button[type='submit']"))).getText();

                Thread.sleep(2000);

                (new WebDriverWait(driver, 5)).until(ExpectedConditions.elementToBeClickable(By.
                        cssSelector("input[placeholder='" + placeholder[k] + "']"))).sendKeys("test4umarkets@gmail.com" + Keys.ENTER);

                (new WebDriverWait(driver, 5)).until(ExpectedConditions.presenceOfElementLocated(By.
                        cssSelector("#forgot-password > div.row.clearfix.row-messages.row-messages-center.success > div.alert.alert-success > span.alert-txt"))).getText();
                testResult.append("ok" + ",");
                driver.manage().deleteAllCookies();
                driver.get("https://accounts.google.com");

                try {
//login
                    (new WebDriverWait(driver, 5)).until(ExpectedConditions.elementToBeClickable(By.
                            cssSelector("input[type='email']"))).sendKeys("test4umarkets" + Keys.ENTER);
                } catch (Exception e) {

                }

//pass
                try {
                    (new WebDriverWait(driver, 5)).until(ExpectedConditions.elementToBeClickable(By.
                            cssSelector("input[type='password']"))).sendKeys("^hfwNa#oe1^h" + Keys.ENTER);
                } catch (Exception e) {

                }

                try {
                    (new WebDriverWait(driver, 5)).until(ExpectedConditions.elementToBeClickable(By.
                            cssSelector("#\\3a 3m > div > div.aio.UKr6le > span > a"))).click();

                } catch (Exception e) {
                    driver.get("https://mail.google.com/mail/#inbox");
                }
//inbox, 30 sec max

                (new WebDriverWait(driver, 30)).until(ExpectedConditions.elementToBeClickable(By.
                        cssSelector("#\\3a 3f > span"))).click();
//deleting from inbox
                driver.navigate().back();
                (new WebDriverWait(driver, 5)).until(ExpectedConditions.elementToBeClickable(By.
                        cssSelector("#\\3a 3d > div"))).click();

                (new WebDriverWait(driver, 5)).until(ExpectedConditions.elementToBeClickable(By.
                        cssSelector("#\\3a 5 > div > div.nH.aqK > div.Cq.aqL > div > div > div:nth-child(2) > div.T-I.J-J5-Ji.nX.T-I-ax7.T-I-Js-Gs.W6eDmd"))).click();
                try {
//delete from trash
                    driver.get("https://mail.google.com/mail/#trash");
                    (new WebDriverWait(driver, 5)).until(ExpectedConditions.elementToBeClickable(By.
                            cssSelector("#\\3a 5f > div"))).click();

                    (new WebDriverWait(driver, 5)).until(ExpectedConditions.elementToBeClickable(By.
                            cssSelector("#\\3a 5 > div:nth-child(2) > div.nH.aqK > div.Cq.aqL > div > div > div:nth-child(2) > div > div"))).click();
                } catch (Exception e) {

                }
                testResult.append("ok" + "\n");
                Thread.sleep(2000);
                driver.manage().deleteAllCookies();
                System.out.println(k);

            }catch (Exception e){

                Thread.sleep(500);
                File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(scrFile, new File("./src/test/testResults/" + login + ".png"));
                testResult.append("Failed," + login + ".png" + "\n");

            }

        }


        //file write
        File file1 = new File("./testResults/", login + ".txt");
        try {

            FileOutputStream fos = new FileOutputStream("./src/test/testResults/" +  "Report.csv");
            testResult.toString();
            byte[] buffer = testResult.toString().getBytes();
            fos.write(buffer, 0, buffer.length);

        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
        driver.quit();
    }
}
